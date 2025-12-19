package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de services Django fonctionnel
 */
public class DjangoServiceGenerator implements IServiceGenerator {
    
    @Override
    public String getServiceDirectory() {
        return "services";
    }
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder service = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String serviceName = className + "Service";
        
        // Imports
        service.append("from django.core.exceptions import ObjectDoesNotExist, ValidationError\n");
        service.append("from django.db import transaction\n");
        service.append("from django.core.cache import cache\n");
        service.append("from django.utils import timezone\n");
        service.append("from typing import List, Optional, Dict, Any\n");
        service.append("import logging\n\n");
        
        service.append("from .models import ").append(className).append("\n");
        service.append("from .exceptions import ").append(className).append("ServiceError\n\n");
        
        // Logger
        service.append("logger = logging.getLogger(__name__)\n\n");
        
        // Service class
        service.append("class ").append(serviceName).append(":\n");
        service.append("    \"\"\"Service for managing ").append(className).append(" operations\"\"\"\n\n");
        
        // Create method with validation
        service.append("    @staticmethod\n");
        service.append("    @transaction.atomic\n");
        service.append("    def create(").append(className.toLowerCase()).append("_data: Dict[str, Any]) -> ").append(className).append(":\n");
        service.append("        \"\"\"Create a new ").append(className.toLowerCase()).append(" with validation\"\"\"\n");
        service.append("        try:\n");
        service.append("            # Validate business rules\n");
        service.append("            ").append(serviceName).append("._validate_create_data(").append(className.toLowerCase()).append("_data)\n");
        service.append("            \n");
        service.append("            # Create instance\n");
        service.append("            ").append(className.toLowerCase()).append(" = ").append(className).append(".objects.create(**").append(className.toLowerCase()).append("_data)\n");
        service.append("            \n");
        service.append("            # Clear cache\n");
        service.append("            cache.delete_many([\n");
        service.append("                f'").append(className.toLowerCase()).append("_list',\n");
        service.append("                f'").append(className.toLowerCase()).append("_count'\n");
        service.append("            ])\n");
        service.append("            \n");
        service.append("            logger.info(f'Created ").append(className.toLowerCase()).append(": {").append(className.toLowerCase()).append(".id}')\n");
        service.append("            return ").append(className.toLowerCase()).append("\n");
        service.append("        except ValidationError as e:\n");
        service.append("            logger.warning(f'Validation error creating ").append(className.toLowerCase()).append(": {e}')\n");
        service.append("            raise ").append(className).append("ServiceError(f'Validation failed: {e}')\n");
        service.append("        except Exception as e:\n");
        service.append("            logger.error(f'Error creating ").append(className.toLowerCase()).append(": {e}')\n");
        service.append("            raise ").append(className).append("ServiceError(f'Failed to create ").append(className.toLowerCase()).append(": {e}')\n\n");
        
        // Get by ID method with cache
        service.append("    @staticmethod\n");
        service.append("    def get_by_id(").append(className.toLowerCase()).append("_id: str) -> Optional[").append(className).append("]:\n");
        service.append("        \"\"\"Get ").append(className.toLowerCase()).append(" by ID with caching\"\"\"\n");
        service.append("        cache_key = f'").append(className.toLowerCase()).append("_{").append(className.toLowerCase()).append("_id}'\n");
        service.append("        \n");
        service.append("        # Try cache first\n");
        service.append("        cached_result = cache.get(cache_key)\n");
        service.append("        if cached_result is not None:\n");
        service.append("            return cached_result\n");
        service.append("        \n");
        service.append("        try:\n");
        service.append("            ").append(className.toLowerCase()).append(" = ").append(className).append(".objects.select_related().get(id=").append(className.toLowerCase()).append("_id)\n");
        service.append("            \n");
        service.append("            # Cache for 15 minutes\n");
        service.append("            cache.set(cache_key, ").append(className.toLowerCase()).append(", 900)\n");
        service.append("            return ").append(className.toLowerCase()).append("\n");
        service.append("        except ObjectDoesNotExist:\n");
        service.append("            logger.warning(f'").append(className).append(" not found: {").append(className.toLowerCase()).append("_id}')\n");
        service.append("            cache.set(cache_key, None, 300)  # Cache miss for 5 minutes\n");
        service.append("            return None\n\n");
        
        // List all method
        service.append("    @staticmethod\n");
        service.append("    def list_all() -> List[").append(className).append("]:\n");
        service.append("        \"\"\"Get all ").append(className.toLowerCase()).append("s\"\"\"\n");
        service.append("        return ").append(className).append(".objects.all()\n\n");
        
        // Update method
        service.append("    @staticmethod\n");
        service.append("    @transaction.atomic\n");
        service.append("    def update(").append(className.toLowerCase()).append("_id: str, update_data: dict) -> Optional[").append(className).append("]:\n");
        service.append("        \"\"\"Update ").append(className.toLowerCase()).append("\"\"\"\n");
        service.append("        try:\n");
        service.append("            ").append(className.toLowerCase()).append(" = ").append(className).append(".objects.get(id=").append(className.toLowerCase()).append("_id)\n");
        service.append("            for key, value in update_data.items():\n");
        service.append("                setattr(").append(className.toLowerCase()).append(", key, value)\n");
        service.append("            ").append(className.toLowerCase()).append(".save()\n");
        service.append("            logger.info(f'Updated ").append(className.toLowerCase()).append(": {").append(className.toLowerCase()).append(".id}')\n");
        service.append("            return ").append(className.toLowerCase()).append("\n");
        service.append("        except ObjectDoesNotExist:\n");
        service.append("            logger.warning(f'").append(className).append(" not found for update: {").append(className.toLowerCase()).append("_id}')\n");
        service.append("            return None\n\n");
        
        // Delete method with cache invalidation
        service.append("    @staticmethod\n");
        service.append("    @transaction.atomic\n");
        service.append("    def delete(").append(className.toLowerCase()).append("_id: str) -> bool:\n");
        service.append("        \"\"\"Delete ").append(className.toLowerCase()).append(" with cache cleanup\"\"\"\n");
        service.append("        try:\n");
        service.append("            ").append(className.toLowerCase()).append(" = ").append(className).append(".objects.get(id=").append(className.toLowerCase()).append("_id)\n");
        service.append("            \n");
        service.append("            # Soft delete if applicable\n");
        service.append("            if hasattr(").append(className.toLowerCase()).append(", 'is_active'):\n");
        service.append("                ").append(className.toLowerCase()).append(".is_active = False\n");
        service.append("                ").append(className.toLowerCase()).append(".save()\n");
        service.append("            else:\n");
        service.append("                ").append(className.toLowerCase()).append(".delete()\n");
        service.append("            \n");
        service.append("            # Clear all related cache\n");
        service.append("            cache.delete_many([\n");
        service.append("                f'").append(className.toLowerCase()).append("_{").append(className.toLowerCase()).append("_id}',\n");
        service.append("                f'").append(className.toLowerCase()).append("_list',\n");
        service.append("                f'").append(className.toLowerCase()).append("_count'\n");
        service.append("            ])\n");
        service.append("            \n");
        service.append("            logger.info(f'Deleted ").append(className.toLowerCase()).append(": {").append(className.toLowerCase()).append("_id}')\n");
        service.append("            return True\n");
        service.append("        except ObjectDoesNotExist:\n");
        service.append("            logger.warning(f'").append(className).append(" not found for deletion: {").append(className.toLowerCase()).append("_id}')\n");
        service.append("            return False\n\n");
        
        // Add validation method
        service.append("    @staticmethod\n");
        service.append("    def _validate_create_data(data: Dict[str, Any]) -> None:\n");
        service.append("        \"\"\"Validate business rules for creation\"\"\"\n");
        service.append("        if not data:\n");
        service.append("            raise ValidationError('Data cannot be empty')\n");
        service.append("        \n");
        service.append("        # Add specific validation rules here\n");
        service.append("        # Example: price validation, email format, etc.\n");
        service.append("        pass\n");
        
        return service.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
}