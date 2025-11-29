package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class DjangoControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();
        
        code.append("from django.urls import path, include\n");
        code.append("from rest_framework.routers import DefaultRouter\n");
        code.append("from .views import ").append(className).append("ViewSet\n\n");
        
        code.append("router = DefaultRouter()\n");
        code.append("router.register(r'").append(className.toLowerCase()).append("s', ").append(className).append("ViewSet)\n\n");
        
        code.append("urlpatterns = [\n");
        code.append("    path('api/', include(router.urls)),\n");
        code.append("]\n");
        
        return code.toString();
    }
    
    @Override
    public String getControllerDirectory() {
        return "urls";
    }
}