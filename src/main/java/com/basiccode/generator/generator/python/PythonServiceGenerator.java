package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PythonServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("from sqlalchemy.orm import Session\n");
        code.append("from typing import List, Optional\n");
        code.append("from models.").append(className.toLowerCase()).append(" import ").append(className).append("\n\n");
        
        code.append("class ").append(className).append("Service:\n");
        code.append("    def __init__(self, db: Session):\n");
        code.append("        self.db = db\n\n");
        
        // CRUD methods
        code.append("    def get_all(self) -> List[").append(className).append("]:\n");
        code.append("        return self.db.query(").append(className).append(").all()\n\n");
        
        code.append("    def get_by_id(self, id: int) -> Optional[").append(className).append("]:\n");
        code.append("        return self.db.query(").append(className).append(").filter(").append(className).append(".id == id).first()\n\n");
        
        code.append("    def create(self, entity: ").append(className).append(") -> ").append(className).append(":\n");
        code.append("        self.db.add(entity)\n");
        code.append("        self.db.commit()\n");
        code.append("        self.db.refresh(entity)\n");
        code.append("        return entity\n\n");
        
        code.append("    def delete(self, id: int) -> bool:\n");
        code.append("        entity = self.get_by_id(id)\n");
        code.append("        if entity:\n");
        code.append("            self.db.delete(entity)\n");
        code.append("            self.db.commit()\n");
        code.append("            return True\n");
        code.append("        return False\n\n");
        
        // State management methods
        if (enhancedClass.isStateful()) {
            code.append("    def suspend_").append(className.toLowerCase()).append("(self, id: int) -> ").append(className).append(":\n");
            code.append("        entity = self.get_by_id(id)\n");
            code.append("        if not entity:\n");
            code.append("            raise ValueError(f'").append(className).append(" not found with id: {id}')\n");
            code.append("        entity.suspend()\n");
            code.append("        self.db.commit()\n");
            code.append("        return entity\n\n");
            
            code.append("    def activate_").append(className.toLowerCase()).append("(self, id: int) -> ").append(className).append(":\n");
            code.append("        entity = self.get_by_id(id)\n");
            code.append("        if not entity:\n");
            code.append("            raise ValueError(f'").append(className).append(" not found with id: {id}')\n");
            code.append("        entity.activate()\n");
            code.append("        self.db.commit()\n");
            code.append("        return entity\n");
        }
        
        return code.toString();
    }
    
    @Override
    public String getServiceDirectory() {
        return "services";
    }
}