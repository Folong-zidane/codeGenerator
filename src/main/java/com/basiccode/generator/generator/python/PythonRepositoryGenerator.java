package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PythonRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("# Repository pattern implementation for ").append(className).append("\n");
        code.append("from sqlalchemy.orm import Session\n");
        code.append("from typing import List, Optional\n");
        code.append("from models.").append(className.toLowerCase()).append(" import ").append(className).append("\n\n");
        
        code.append("class ").append(className).append("Repository:\n");
        code.append("    def __init__(self, db: Session):\n");
        code.append("        self.db = db\n\n");
        
        code.append("    def find_all(self) -> List[").append(className).append("]:\n");
        code.append("        return self.db.query(").append(className).append(").all()\n\n");
        
        code.append("    def find_by_id(self, id: int) -> Optional[").append(className).append("]:\n");
        code.append("        return self.db.query(").append(className).append(").filter(").append(className).append(".id == id).first()\n\n");
        
        code.append("    def save(self, entity: ").append(className).append(") -> ").append(className).append(":\n");
        code.append("        self.db.add(entity)\n");
        code.append("        self.db.commit()\n");
        code.append("        self.db.refresh(entity)\n");
        code.append("        return entity\n\n");
        
        code.append("    def delete_by_id(self, id: int) -> bool:\n");
        code.append("        entity = self.find_by_id(id)\n");
        code.append("        if entity:\n");
        code.append("            self.db.delete(entity)\n");
        code.append("            self.db.commit()\n");
        code.append("            return True\n");
        code.append("        return False\n");
        
        return code.toString();
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "repositories";
    }
}