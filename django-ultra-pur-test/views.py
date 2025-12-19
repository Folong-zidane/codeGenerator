# Django Views - Ultra-Pure Generated

from rest_framework import viewsets, status, filters
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework.pagination import PageNumberPagination
from django_filters.rest_framework import DjangoFilterBackend
from django.shortcuts import get_object_or_404
from django.utils.decorators import method_decorator
from django.views.decorators.cache import cache_page
import logging

from .models import Product
from .serializers import ProductSerializer, ProductCreateSerializer
from .services import ProductService
from .exceptions import ProductServiceError

logger = logging.getLogger(__name__)

class ProductPagination(PageNumberPagination):
    page_size = 20
    page_size_query_param = 'page_size'
    max_page_size = 100

class ProductViewSet(viewsets.ModelViewSet):
    """Advanced ViewSet for Product with filtering and pagination"""
    queryset = Product.objects.select_related().prefetch_related()
    serializer_class = ProductSerializer
    permission_classes = [IsAuthenticated]
    pagination_class = ProductPagination
    filter_backends = [DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter]
    search_fields = ['name', 'title']  # Customize based on model fields
    ordering_fields = ['created_at', 'updated_at']
    ordering = ['-created_at']

    def get_serializer_class(self):
        """Return appropriate serializer based on action"""
        if self.action == 'create':
            return ProductCreateSerializer
        return self.serializer_class

    def create(self, request):
        """Create a new product with validation"""
        try:
            serializer = self.get_serializer(data=request.data)
            serializer.is_valid(raise_exception=True)
            
            product = ProductService.create(serializer.validated_data)
            response_serializer = ProductSerializer(product)
            
            return Response({
                'success': True,
                'message': 'Product created successfully',
                'data': response_serializer.data
            }, status=status.HTTP_201_CREATED)
            
        except ProductServiceError as e:
            logger.warning(f'Service error creating product: {e}')
            return Response({
                'success': False,
                'message': str(e)
            }, status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            logger.error(f'Unexpected error creating product: {e}')
            return Response({
                'success': False,
                'message': 'Internal server error'
            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    @method_decorator(cache_page(60 * 5))  # Cache for 5 minutes
    def list(self, request):
        """List products with filtering and pagination"""
        try:
            queryset = self.filter_queryset(self.get_queryset())
            page = self.paginate_queryset(queryset)
            
            if page is not None:
                serializer = self.get_serializer(page, many=True)
                return self.get_paginated_response(serializer.data)
            
            serializer = self.get_serializer(queryset, many=True)
            return Response({
                'success': True,
                'count': queryset.count(),
                'results': serializer.data
            })
        except Exception as e:
            logger.error(f'Error listing products: {e}')
            return Response({
                'success': False,
                'message': 'Internal server error'
            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def retrieve(self, request, pk=None):
        """Retrieve a specific product"""
        try:
            product = ProductService.get_by_id(pk)
            if product:
                serializer = self.get_serializer(product)
                return Response(serializer.data)
            return Response({'error': 'Product not found'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            logger.error(f'Error retrieving product: {e}')
            return Response({'error': 'Internal server error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def update(self, request, pk=None):
        """Update a product"""
        try:
            serializer = self.get_serializer(data=request.data)
            if serializer.is_valid():
                product = ProductService.update(pk, serializer.validated_data)
                if product:
                    response_serializer = self.get_serializer(product)
                    return Response(response_serializer.data)
                return Response({'error': 'Product not found'}, status=status.HTTP_404_NOT_FOUND)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            logger.error(f'Error updating product: {e}')
            return Response({'error': 'Internal server error'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    def destroy(self, request, pk=None):
        """Delete a product with confirmation"""
        try:
            success = ProductService.delete(pk)
            if success:
                return Response({
                    'success': True,
                    'message': 'Product deleted successfully'
                }, status=status.HTTP_200_OK)
            return Response({
                'success': False,
                'message': 'Product not found'
            }, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            logger.error(f'Error deleting product: {e}')
            return Response({
                'success': False,
                'message': 'Internal server error'
            }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

    @action(detail=False, methods=['post'])
    def bulk_create(self, request):
        """Bulk create multiple products"""
        try:
            serializer = self.get_serializer(data=request.data, many=True)
            serializer.is_valid(raise_exception=True)
            
            created_objects = []
            for item_data in serializer.validated_data:
                obj = ProductService.create(item_data)
                created_objects.append(obj)
            
            response_serializer = self.get_serializer(created_objects, many=True)
            return Response({
                'success': True,
                'message': f'Created {len(created_objects)} products',
                'data': response_serializer.data
            }, status=status.HTTP_201_CREATED)
        except Exception as e:
            logger.error(f'Error in bulk create: {e}')
            return Response({
                'success': False,
                'message': 'Bulk creation failed'
            }, status=status.HTTP_400_BAD_REQUEST)


