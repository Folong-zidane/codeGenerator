package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PythonProjectGenerator {
    
    public void generateCompleteProject(List<ClassModel> classes, String packageName, Path outputDir) throws IOException {
        // 1. Create project structure
        createProjectStructure(outputDir);
        
        // 2. Generate configuration files
        generateRequirements(outputDir);
        generateMainApp(outputDir, classes);
        generateDatabase(outputDir);
        
        // 3. Generate entities with correct imports
        generateEntities(classes, outputDir);
        
        // 4. Generate repositories with DB implementation
        generateRepositories(classes, outputDir);
        
        // 5. Generate services with DI
        generateServices(classes, outputDir);
        
        // 6. Generate controllers with proper routing
        generateControllers(classes, outputDir);
    }
    
    private void createProjectStructure(Path outputDir) throws IOException {
        Files.createDirectories(outputDir.resolve("entities"));
        Files.createDirectories(outputDir.resolve("repositories"));
        Files.createDirectories(outputDir.resolve("services"));
        Files.createDirectories(outputDir.resolve("controllers"));
        Files.createDirectories(outputDir.resolve("config"));
    }
    
    private void generateRequirements(Path outputDir) throws IOException {
        String requirements = """
            fastapi==0.104.1
            uvicorn==0.24.0
            sqlalchemy==2.0.23
            pydantic==2.5.0
            python-multipart==0.0.6
            """;
        Files.writeString(outputDir.resolve("requirements.txt"), requirements);
    }
    
    private void generateMainApp(Path outputDir, List<ClassModel> classes) throws IOException {
        StringBuilder imports = new StringBuilder();
        StringBuilder routers = new StringBuilder();
        
        for (ClassModel cls : classes) {
            String controllerName = cls.getName() + "Controller";
            imports.append("from controllers.").append(controllerName).append(" import router as ").append(cls.getName().toLowerCase()).append("_router\n");
            routers.append("app.include_router(").append(cls.getName().toLowerCase()).append("_router)\n");
        }
        
        String mainApp = String.format("""
            from fastapi import FastAPI
            from config.database import engine, Base
            %s
            
            # Create tables
            Base.metadata.create_all(bind=engine)
            
            app = FastAPI(title="Generated Python API", version="1.0.0")
            
            %s
            
            @app.get("/")
            def read_root():
                return {"message": "Generated Python API is running", "docs": "/docs"}
            
            if __name__ == "__main__":
                import uvicorn
                uvicorn.run(app, host="0.0.0.0", port=8000)
            """, imports.toString(), routers.toString());
        
        Files.writeString(outputDir.resolve("main.py"), mainApp);
    }
    
    private void generateDatabase(Path outputDir) throws IOException {
        String database = """
            from sqlalchemy import create_engine
            from sqlalchemy.ext.declarative import declarative_base
            from sqlalchemy.orm import sessionmaker
            
            SQLALCHEMY_DATABASE_URL = "sqlite:///./app.db"
            
            engine = create_engine(
                SQLALCHEMY_DATABASE_URL, 
                connect_args={"check_same_thread": False}
            )
            
            SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
            Base = declarative_base()
            
            def get_db():
                db = SessionLocal()
                try:
                    yield db
                finally:
                    db.close()
            """;
        Files.writeString(outputDir.resolve("config/database.py"), database);
        Files.writeString(outputDir.resolve("config/__init__.py"), "");
    }
    
    private void generateEntities(List<ClassModel> classes, Path outputDir) throws IOException {
        Files.writeString(outputDir.resolve("entities/__init__.py"), "");
        
        for (ClassModel cls : classes) {
            StringBuilder entity = new StringBuilder();
            entity.append("from sqlalchemy import Column, String, DateTime, Boolean, Integer, Float\n");
            entity.append("from sqlalchemy.dialects.postgresql import UUID\n");
            entity.append("from config.database import Base\n");
            entity.append("from datetime import datetime\n");
            entity.append("import uuid\n\n");
            
            entity.append("class ").append(cls.getName()).append("(Base):\n");
            entity.append("    __tablename__ = '").append(toSnakeCase(cls.getName())).append("'\n\n");
            entity.append("    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)\n");
            
            for (Field field : cls.getFields()) {
                entity.append("    ").append(field.getName())
                      .append(" = Column(").append(getPythonSQLType(field.getType())).append(")\n");
            }
            
            entity.append("    created_at = Column(DateTime, default=datetime.utcnow)\n");
            entity.append("    updated_at = Column(DateTime, onupdate=datetime.utcnow)\n");
            
            Files.writeString(outputDir.resolve("entities/" + cls.getName() + ".py"), entity.toString());
        }
    }
    
    private void generateRepositories(List<ClassModel> classes, Path outputDir) throws IOException {
        Files.writeString(outputDir.resolve("repositories/__init__.py"), "");
        
        for (ClassModel cls : classes) {
            String repository = String.format("""
                from sqlalchemy.orm import Session
                from typing import Optional, List
                from uuid import UUID
                from entities.%s import %s
                
                class %sRepository:
                    def __init__(self, db: Session):
                        self.db = db
                
                    def find_by_id(self, id: UUID) -> Optional[%s]:
                        return self.db.query(%s).filter(%s.id == id).first()
                
                    def find_all(self) -> List[%s]:
                        return self.db.query(%s).all()
                
                    def save(self, entity: %s) -> %s:
                        self.db.add(entity)
                        self.db.commit()
                        self.db.refresh(entity)
                        return entity
                
                    def delete(self, entity: %s):
                        self.db.delete(entity)
                        self.db.commit()
                
                    def delete_by_id(self, id: UUID):
                        entity = self.find_by_id(id)
                        if entity:
                            self.delete(entity)
                """, 
                cls.getName(), cls.getName(), cls.getName(), 
                cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName());
            
            Files.writeString(outputDir.resolve("repositories/" + cls.getName() + "Repository.py"), repository);
        }
    }
    
    private void generateServices(List<ClassModel> classes, Path outputDir) throws IOException {
        Files.writeString(outputDir.resolve("services/__init__.py"), "");
        
        for (ClassModel cls : classes) {
            String service = String.format("""
                from typing import Optional, List
                from uuid import UUID
                from entities.%s import %s
                from repositories.%sRepository import %sRepository
                
                class %sService:
                    def __init__(self, repository: %sRepository):
                        self.repository = repository
                
                    def create(self, entity: %s) -> %s:
                        return self.repository.save(entity)
                
                    def find_by_id(self, id: UUID) -> Optional[%s]:
                        return self.repository.find_by_id(id)
                
                    def find_all(self) -> List[%s]:
                        return self.repository.find_all()
                
                    def update(self, entity: %s) -> %s:
                        return self.repository.save(entity)
                
                    def delete(self, id: UUID):
                        self.repository.delete_by_id(id)
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("services/" + cls.getName() + "Service.py"), service);
        }
    }
    
    private void generateControllers(List<ClassModel> classes, Path outputDir) throws IOException {
        Files.writeString(outputDir.resolve("controllers/__init__.py"), "");
        
        for (ClassModel cls : classes) {
            String controller = String.format("""
                from fastapi import APIRouter, Depends, HTTPException
                from sqlalchemy.orm import Session
                from typing import List
                from uuid import UUID
                from entities.%s import %s
                from repositories.%sRepository import %sRepository
                from services.%sService import %sService
                from config.database import get_db
                
                router = APIRouter(prefix="/api/%s", tags=["%s"])
                
                def get_service(db: Session = Depends(get_db)) -> %sService:
                    repository = %sRepository(db)
                    return %sService(repository)
                
                @router.post("/")
                async def create_%s(entity: %s, service: %sService = Depends(get_service)):
                    return service.create(entity)
                
                @router.get("/{id}")
                async def get_%s(id: UUID, service: %sService = Depends(get_service)):
                    entity = service.find_by_id(id)
                    if not entity:
                        raise HTTPException(status_code=404, detail="Not found")
                    return entity
                
                @router.get("/")
                async def get_all_%s(service: %sService = Depends(get_service)) -> List[%s]:
                    return service.find_all()
                
                @router.put("/{id}")
                async def update_%s(id: UUID, entity: %s, service: %sService = Depends(get_service)):
                    entity.id = id
                    return service.update(entity)
                
                @router.delete("/{id}")
                async def delete_%s(id: UUID, service: %sService = Depends(get_service)):
                    service.delete(id)
                    return {"message": "Deleted successfully"}
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName().toLowerCase(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(),
                cls.getName().toLowerCase(), cls.getName(), cls.getName(),
                cls.getName().toLowerCase(), cls.getName(),
                cls.getName().toLowerCase(), cls.getName(), cls.getName(),
                cls.getName().toLowerCase(), cls.getName(), cls.getName(),
                cls.getName().toLowerCase(), cls.getName());
            
            Files.writeString(outputDir.resolve("controllers/" + cls.getName() + "Controller.py"), controller);
        }
    }
    
    private String getPythonSQLType(String type) {
        return switch (type) {
            case "String" -> "String";
            case "Integer", "int" -> "Integer";
            case "Float", "float" -> "Float";
            case "Boolean", "boolean" -> "Boolean";
            default -> "String";
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}