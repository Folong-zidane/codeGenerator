package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PythonControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String lowerName = className.toLowerCase();
        
        code.append("from fastapi import APIRouter, Depends, HTTPException\n");
        code.append("from sqlalchemy.orm import Session\n");
        code.append("from typing import List\n");
        code.append("from models.").append(lowerName).append(" import ").append(className).append("\n");
        code.append("from services.").append(lowerName).append("_service import ").append(className).append("Service\n");
        code.append("from database import get_db\n\n");
        
        code.append("router = APIRouter(prefix='/api/").append(lowerName).append("s', tags=['").append(lowerName).append("s'])\n\n");
        
        // GET all
        code.append("@router.get('/', response_model=List[").append(className).append("])\n");
        code.append("def get_all_").append(lowerName).append("s(db: Session = Depends(get_db)):\n");
        code.append("    service = ").append(className).append("Service(db)\n");
        code.append("    return service.get_all()\n\n");
        
        // GET by id
        code.append("@router.get('/{id}', response_model=").append(className).append(")\n");
        code.append("def get_").append(lowerName).append("(id: int, db: Session = Depends(get_db)):\n");
        code.append("    service = ").append(className).append("Service(db)\n");
        code.append("    entity = service.get_by_id(id)\n");
        code.append("    if not entity:\n");
        code.append("        raise HTTPException(status_code=404, detail='").append(className).append(" not found')\n");
        code.append("    return entity\n\n");
        
        // POST create
        code.append("@router.post('/', response_model=").append(className).append(", status_code=201)\n");
        code.append("def create_").append(lowerName).append("(entity: ").append(className).append(", db: Session = Depends(get_db)):\n");
        code.append("    service = ").append(className).append("Service(db)\n");
        code.append("    return service.create(entity)\n\n");
        
        // DELETE
        code.append("@router.delete('/{id}', status_code=204)\n");
        code.append("def delete_").append(lowerName).append("(id: int, db: Session = Depends(get_db)):\n");
        code.append("    service = ").append(className).append("Service(db)\n");
        code.append("    if not service.delete(id):\n");
        code.append("        raise HTTPException(status_code=404, detail='").append(className).append(" not found')\n\n");
        
        // State management endpoints
        if (enhancedClass.isStateful()) {
            code.append("@router.patch('/{id}/suspend', response_model=").append(className).append(")\n");
            code.append("def suspend_").append(lowerName).append("(id: int, db: Session = Depends(get_db)):\n");
            code.append("    service = ").append(className).append("Service(db)\n");
            code.append("    try:\n");
            code.append("        return service.suspend_").append(lowerName).append("(id)\n");
            code.append("    except ValueError as e:\n");
            code.append("        raise HTTPException(status_code=400, detail=str(e))\n\n");
            
            code.append("@router.patch('/{id}/activate', response_model=").append(className).append(")\n");
            code.append("def activate_").append(lowerName).append("(id: int, db: Session = Depends(get_db)):\n");
            code.append("    service = ").append(className).append("Service(db)\n");
            code.append("    try:\n");
            code.append("        return service.activate_").append(lowerName).append("(id)\n");
            code.append("    except ValueError as e:\n");
            code.append("        raise HTTPException(status_code=400, detail=str(e))\n");
        }
        
        return code.toString();
    }
    
    @Override
    public String getControllerDirectory() {
        return "routers";
    }
}