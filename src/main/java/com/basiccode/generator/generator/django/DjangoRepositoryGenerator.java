package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de repositories Django (managers)
 */
public class DjangoRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String getRepositoryDirectory() {
        return "managers";
    }
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder manager = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String managerName = className + "Manager";
        
        // Imports
        manager.append("from django.db import models\n");
        manager.append("from django.db.models import Q\n\n");
        
        // Manager class
        manager.append("class ").append(managerName).append("(models.Manager):\n");
        manager.append("    \"\"\"Custom manager for ").append(className).append(" model\"\"\"");
        manager.append("\n\n");
        
        // Active objects method
        manager.append("    def get_active(self):\n");
        manager.append("        \"\"\"Get all active ").append(className.toLowerCase()).append(" instances\"\"\"");
        manager.append("\n");
        manager.append("        return self.filter(is_active=True)\n\n");
        
        // Search method
        manager.append("    def search(self, query):\n");
        manager.append("        \"\"\"Search ").append(className.toLowerCase()).append(" instances\"\"\"");
        manager.append("\n");
        manager.append("        return self.filter(\n");
        manager.append("            Q(name__icontains=query) |\n");
        manager.append("            Q(description__icontains=query)\n");
        manager.append("        )\n\n");
        
        // Find by name method
        manager.append("    def find_by_name(self, name):\n");
        manager.append("        \"\"\"Find ").append(className.toLowerCase()).append(" by name\"\"\"");
        manager.append("\n");
        manager.append("        try:\n");
        manager.append("            return self.get(name=name)\n");
        manager.append("        except self.model.DoesNotExist:\n");
        manager.append("            return None\n");
        
        return manager.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
}