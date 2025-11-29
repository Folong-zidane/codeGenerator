package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.BusinessMethod;

public class DjangoServiceGenerator implements IServiceGenerator {
    
    @Override
    public String generateService(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append(generateViewSet(className, packageName, enhancedClass));
        code.append("\n\n");
        code.append(generateServiceClass(className, packageName, enhancedClass));
        
        return code.toString();
    }
    
    @Override
    public String getServiceDirectory() {
        return "views";
    }
    
    private String generateViewSet(String className, String packageName, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();
        String modelName = className.toLowerCase();
        
        code.append("from rest_framework import viewsets, status\n");
        code.append("from rest_framework.decorators import action\n");
        code.append("from rest_framework.response import Response\n");
        code.append("from django.shortcuts import get_object_or_404\n");
        code.append("from .models import ").append(className).append("\n");
        code.append("from .serializers import ").append(className).append("Serializer\n");
        code.append("from .services import ").append(className).append("Service\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("from .enums import ").append(className).append("Status\n\n");
        }
        
        code.append("class ").append(className).append("ViewSet(viewsets.ModelViewSet):\n");
        code.append("    queryset = ").append(className).append(".objects.all()\n");
        code.append("    serializer_class = ").append(className).append("Serializer\n");
        code.append("    service = ").append(className).append("Service()\n\n");
        
        code.append("    def create(self, request):\n");
        code.append("        try:\n");
        code.append("            instance = self.service.create_").append(modelName).append("(request.data)\n");
        code.append("            serializer = self.get_serializer(instance)\n");
        code.append("            return Response(serializer.data, status=status.HTTP_201_CREATED)\n");
        code.append("        except ValueError as e:\n");
        code.append("            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n\n");
        
        code.append("    def update(self, request, pk=None):\n");
        code.append("        try:\n");
        code.append("            instance = self.service.update_").append(modelName).append("(pk, request.data)\n");
        code.append("            serializer = self.get_serializer(instance)\n");
        code.append("            return Response(serializer.data)\n");
        code.append("        except ValueError as e:\n");
        code.append("            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("    @action(detail=True, methods=['post'])\n");
            code.append("    def suspend(self, request, pk=None):\n");
            code.append("        try:\n");
            code.append("            instance = self.service.suspend_").append(modelName).append("(pk)\n");
            code.append("            serializer = self.get_serializer(instance)\n");
            code.append("            return Response(serializer.data)\n");
            code.append("        except ValueError as e:\n");
            code.append("            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n\n");
            
            code.append("    @action(detail=True, methods=['post'])\n");
            code.append("    def activate(self, request, pk=None):\n");
            code.append("        try:\n");
            code.append("            instance = self.service.activate_").append(modelName).append("(pk)\n");
            code.append("            serializer = self.get_serializer(instance)\n");
            code.append("            return Response(serializer.data)\n");
            code.append("        except ValueError as e:\n");
            code.append("            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n\n");
        }
        
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("    @action(detail=False, methods=['post'])\n");
                code.append("    def ").append(method.getName().toLowerCase()).append("(self, request):\n");
                code.append("        try:\n");
                code.append("            result = self.service.").append(method.getName().toLowerCase()).append("()\n");
                code.append("            return Response({'result': result})\n");
                code.append("        except Exception as e:\n");
                code.append("            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n\n");
            }
        }
        
        return code.toString();
    }
    
    private String generateServiceClass(String className, String packageName, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();
        String modelName = className.toLowerCase();
        
        code.append("from django.core.exceptions import ValidationError\n");
        code.append("from django.shortcuts import get_object_or_404\n");
        code.append("from .models import ").append(className).append("\n");
        code.append("from .serializers import ").append(className).append("Serializer\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("from .enums import ").append(className).append("Status\n\n");
        }
        
        code.append("class ").append(className).append("Service:\n\n");
        
        code.append("    def create_").append(modelName).append("(self, data):\n");
        code.append("        serializer = ").append(className).append("Serializer(data=data)\n");
        code.append("        if serializer.is_valid():\n");
        code.append("            self._validate_").append(modelName).append("_data(serializer.validated_data)\n");
        code.append("            return serializer.save()\n");
        code.append("        else:\n");
        code.append("            raise ValueError(serializer.errors)\n\n");
        
        code.append("    def update_").append(modelName).append("(self, pk, data):\n");
        code.append("        instance = get_object_or_404(").append(className).append(", pk=pk)\n");
        code.append("        serializer = ").append(className).append("Serializer(instance, data=data, partial=True)\n");
        code.append("        if serializer.is_valid():\n");
        code.append("            self._validate_").append(modelName).append("_data(serializer.validated_data)\n");
        code.append("            return serializer.save()\n");
        code.append("        else:\n");
        code.append("            raise ValueError(serializer.errors)\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("    def suspend_").append(modelName).append("(self, pk):\n");
            code.append("        instance = get_object_or_404(").append(className).append(", pk=pk)\n");
            code.append("        if instance.status == ").append(className).append("Status.SUSPENDED:\n");
            code.append("            raise ValueError('").append(className).append(" is already suspended')\n");
            code.append("        instance.status = ").append(className).append("Status.SUSPENDED\n");
            code.append("        instance.save()\n");
            code.append("        return instance\n\n");
            
            code.append("    def activate_").append(modelName).append("(self, pk):\n");
            code.append("        instance = get_object_or_404(").append(className).append(", pk=pk)\n");
            code.append("        if instance.status == ").append(className).append("Status.ACTIVE:\n");
            code.append("            raise ValueError('").append(className).append(" is already active')\n");
            code.append("        instance.status = ").append(className).append("Status.ACTIVE\n");
            code.append("        instance.save()\n");
            code.append("        return instance\n\n");
        }
        
        if (enhancedClass.getBehavioralMethods() != null) {
            for (BusinessMethod method : enhancedClass.getBehavioralMethods()) {
                code.append("    def ").append(method.getName().toLowerCase()).append("(self):\n");
                for (String logic : method.getBusinessLogic()) {
                    code.append("        # ").append(logic).append("\n");
                }
                code.append("        pass\n\n");
            }
        }
        
        code.append("    def _validate_").append(modelName).append("_data(self, data):\n");
        code.append("        pass\n");
        
        return code.toString();
    }
}