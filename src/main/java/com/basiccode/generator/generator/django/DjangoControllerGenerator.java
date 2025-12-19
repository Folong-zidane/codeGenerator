package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de contrôleurs Django REST fonctionnel
 */
public class DjangoControllerGenerator implements IControllerGenerator {
    
    @Override
    public String getControllerDirectory() {
        return "views";
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder controller = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String viewSetName = className + "ViewSet";
        String serializerName = className + "Serializer";
        String serviceName = className + "Service";
        
        // Imports
        controller.append("from rest_framework import viewsets, status, filters\n");
        controller.append("from rest_framework.decorators import action\n");
        controller.append("from rest_framework.response import Response\n");
        controller.append("from rest_framework.permissions import IsAuthenticated\n");
        controller.append("from rest_framework.pagination import PageNumberPagination\n");
        controller.append("from django_filters.rest_framework import DjangoFilterBackend\n");
        controller.append("from django.shortcuts import get_object_or_404\n");
        controller.append("from django.utils.decorators import method_decorator\n");
        controller.append("from django.views.decorators.cache import cache_page\n");
        controller.append("import logging\n\n");
        
        controller.append("from .models import ").append(className).append("\n");
        controller.append("from .serializers import ").append(serializerName).append(", ").append(className).append("CreateSerializer\n");
        controller.append("from .services import ").append(serviceName).append("\n");
        controller.append("from .exceptions import ").append(className).append("ServiceError\n\n");
        
        // Logger
        controller.append("logger = logging.getLogger(__name__)\n\n");
        
        // Custom pagination
        controller.append("class ").append(className).append("Pagination(PageNumberPagination):\n");
        controller.append("    page_size = 20\n");
        controller.append("    page_size_query_param = 'page_size'\n");
        controller.append("    max_page_size = 100\n\n");
        
        // ViewSet class
        controller.append("class ").append(viewSetName).append("(viewsets.ModelViewSet):\n");
        controller.append("    \"\"\"Advanced ViewSet for ").append(className).append(" with filtering and pagination\"\"\"\n");
        controller.append("    queryset = ").append(className).append(".objects.select_related().prefetch_related()\n");
        controller.append("    serializer_class = ").append(serializerName).append("\n");
        controller.append("    permission_classes = [IsAuthenticated]\n");
        controller.append("    pagination_class = ").append(className).append("Pagination\n");
        controller.append("    filter_backends = [DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter]\n");
        controller.append("    search_fields = ['name', 'title']  # Customize based on model fields\n");
        controller.append("    ordering_fields = ['created_at', 'updated_at']\n");
        controller.append("    ordering = ['-created_at']\n\n");
        
        controller.append("    def get_serializer_class(self):\n");
        controller.append("        \"\"\"Return appropriate serializer based on action\"\"\"\n");
        controller.append("        if self.action == 'create':\n");
        controller.append("            return ").append(className).append("CreateSerializer\n");
        controller.append("        return self.serializer_class\n\n");
        
        // Create method with enhanced error handling
        controller.append("    def create(self, request):\n");
        controller.append("        \"\"\"Create a new ").append(className.toLowerCase()).append(" with validation\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            serializer = self.get_serializer(data=request.data)\n");
        controller.append("            serializer.is_valid(raise_exception=True)\n");
        controller.append("            \n");
        controller.append("            ").append(className.toLowerCase()).append(" = ").append(serviceName).append(".create(serializer.validated_data)\n");
        controller.append("            response_serializer = ").append(serializerName).append("(").append(className.toLowerCase()).append(")\n");
        controller.append("            \n");
        controller.append("            return Response({\n");
        controller.append("                'success': True,\n");
        controller.append("                'message': '").append(className).append(" created successfully',\n");
        controller.append("                'data': response_serializer.data\n");
        controller.append("            }, status=status.HTTP_201_CREATED)\n");
        controller.append("            \n");
        controller.append("        except ").append(className).append("ServiceError as e:\n");
        controller.append("            logger.warning(f'Service error creating ").append(className.toLowerCase()).append(": {e}')\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': str(e)\n");
        controller.append("            }, status=status.HTTP_400_BAD_REQUEST)\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Unexpected error creating ").append(className.toLowerCase()).append(": {e}')\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': 'Internal server error'\n");
        controller.append("            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n\n");
        
        // List method with caching
        controller.append("    @method_decorator(cache_page(60 * 5))  # Cache for 5 minutes\n");
        controller.append("    def list(self, request):\n");
        controller.append("        \"\"\"List ").append(className.toLowerCase()).append("s with filtering and pagination\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            queryset = self.filter_queryset(self.get_queryset())\n");
        controller.append("            page = self.paginate_queryset(queryset)\n");
        controller.append("            \n");
        controller.append("            if page is not None:\n");
        controller.append("                serializer = self.get_serializer(page, many=True)\n");
        controller.append("                return self.get_paginated_response(serializer.data)\n");
        controller.append("            \n");
        controller.append("            serializer = self.get_serializer(queryset, many=True)\n");
        controller.append("            return Response({\n");
        controller.append("                'success': True,\n");
        controller.append("                'count': queryset.count(),\n");
        controller.append("                'results': serializer.data\n");
        controller.append("            })\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Error listing ").append(className.toLowerCase()).append("s: {e}')\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': 'Internal server error'\n");
        controller.append("            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n\n");
        
        // Retrieve method
        controller.append("    def retrieve(self, request, pk=None):\n");
        controller.append("        \"\"\"Retrieve a specific ").append(className.toLowerCase()).append("\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            ").append(className.toLowerCase()).append(" = ").append(serviceName).append(".get_by_id(pk)\n");
        controller.append("            if ").append(className.toLowerCase()).append(":\n");
        controller.append("                serializer = self.get_serializer(").append(className.toLowerCase()).append(")\n");
        controller.append("                return Response(serializer.data)\n");
        controller.append("            return Response({'error': '").append(className).append(" not found'}, status=status.HTTP_404_NOT_FOUND)\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Error retrieving ").append(className.toLowerCase()).append(": {e}')\n");
        controller.append("            return Response({'error': 'Internal server error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n\n");
        
        // Update method
        controller.append("    def update(self, request, pk=None):\n");
        controller.append("        \"\"\"Update a ").append(className.toLowerCase()).append("\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            serializer = self.get_serializer(data=request.data)\n");
        controller.append("            if serializer.is_valid():\n");
        controller.append("                ").append(className.toLowerCase()).append(" = ").append(serviceName).append(".update(pk, serializer.validated_data)\n");
        controller.append("                if ").append(className.toLowerCase()).append(":\n");
        controller.append("                    response_serializer = self.get_serializer(").append(className.toLowerCase()).append(")\n");
        controller.append("                    return Response(response_serializer.data)\n");
        controller.append("                return Response({'error': '").append(className).append(" not found'}, status=status.HTTP_404_NOT_FOUND)\n");
        controller.append("            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Error updating ").append(className.toLowerCase()).append(": {e}')\n");
        controller.append("            return Response({'error': 'Internal server error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n\n");
        
        // Delete method with confirmation
        controller.append("    def destroy(self, request, pk=None):\n");
        controller.append("        \"\"\"Delete a ").append(className.toLowerCase()).append(" with confirmation\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            success = ").append(serviceName).append(".delete(pk)\n");
        controller.append("            if success:\n");
        controller.append("                return Response({\n");
        controller.append("                    'success': True,\n");
        controller.append("                    'message': '").append(className).append(" deleted successfully'\n");
        controller.append("                }, status=status.HTTP_200_OK)\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': '").append(className).append(" not found'\n");
        controller.append("            }, status=status.HTTP_404_NOT_FOUND)\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Error deleting ").append(className.toLowerCase()).append(": {e}')\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': 'Internal server error'\n");
        controller.append("            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n\n");
        
        // Add bulk operations
        controller.append("    @action(detail=False, methods=['post'])\n");
        controller.append("    def bulk_create(self, request):\n");
        controller.append("        \"\"\"Bulk create multiple ").append(className.toLowerCase()).append("s\"\"\"\n");
        controller.append("        try:\n");
        controller.append("            serializer = self.get_serializer(data=request.data, many=True)\n");
        controller.append("            serializer.is_valid(raise_exception=True)\n");
        controller.append("            \n");
        controller.append("            created_objects = []\n");
        controller.append("            for item_data in serializer.validated_data:\n");
        controller.append("                obj = ").append(serviceName).append(".create(item_data)\n");
        controller.append("                created_objects.append(obj)\n");
        controller.append("            \n");
        controller.append("            response_serializer = self.get_serializer(created_objects, many=True)\n");
        controller.append("            return Response({\n");
        controller.append("                'success': True,\n");
        controller.append("                'message': f'Created {len(created_objects)} ").append(className.toLowerCase()).append("s',\n");
        controller.append("                'data': response_serializer.data\n");
        controller.append("            }, status=status.HTTP_201_CREATED)\n");
        controller.append("        except Exception as e:\n");
        controller.append("            logger.error(f'Error in bulk create: {e}')\n");
        controller.append("            return Response({\n");
        controller.append("                'success': False,\n");
        controller.append("                'message': 'Bulk creation failed'\n");
        controller.append("            }, status=status.HTTP_400_BAD_REQUEST)\n");
        
        return controller.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
}