from django.core.exceptions import ObjectDoesNotExist, ValidationError
from django.db import transaction
from django.core.cache import cache
from django.utils import timezone
from typing import List, Optional, Dict, Any
import logging

from .models import Product
from .exceptions import ProductServiceError

logger = logging.getLogger(__name__)

class ProductService:
    """Service for managing Product operations"""

    @staticmethod
    @transaction.atomic
    def create(product_data: Dict[str, Any]) -> Product:
        """Create a new product with validation"""
        try:
            # Validate business rules
            ProductService._validate_create_data(product_data)
            
            # Create instance
            product = Product.objects.create(**product_data)
            
            # Clear cache
            cache.delete_many([
                f'product_list',
                f'product_count'
            ])
            
            logger.info(f'Created product: {product.id}')
            return product
        except ValidationError as e:
            logger.warning(f'Validation error creating product: {e}')
            raise ProductServiceError(f'Validation failed: {e}')
        except Exception as e:
            logger.error(f'Error creating product: {e}')
            raise ProductServiceError(f'Failed to create product: {e}')

    @staticmethod
    def get_by_id(product_id: str) -> Optional[Product]:
        """Get product by ID with caching"""
        cache_key = f'product_{product_id}'
        
        # Try cache first
        cached_result = cache.get(cache_key)
        if cached_result is not None:
            return cached_result
        
        try:
            product = Product.objects.select_related().get(id=product_id)
            
            # Cache for 15 minutes
            cache.set(cache_key, product, 900)
            return product
        except ObjectDoesNotExist:
            logger.warning(f'Product not found: {product_id}')
            cache.set(cache_key, None, 300)  # Cache miss for 5 minutes
            return None

    @staticmethod
    def list_all() -> List[Product]:
        """Get all products"""
        return Product.objects.all()

    @staticmethod
    @transaction.atomic
    def update(product_id: str, update_data: dict) -> Optional[Product]:
        """Update product"""
        try:
            product = Product.objects.get(id=product_id)
            for key, value in update_data.items():
                setattr(product, key, value)
            product.save()
            logger.info(f'Updated product: {product.id}')
            return product
        except ObjectDoesNotExist:
            logger.warning(f'Product not found for update: {product_id}')
            return None

    @staticmethod
    @transaction.atomic
    def delete(product_id: str) -> bool:
        """Delete product with cache cleanup"""
        try:
            product = Product.objects.get(id=product_id)
            
            # Soft delete if applicable
            if hasattr(product, 'is_active'):
                product.is_active = False
                product.save()
            else:
                product.delete()
            
            # Clear all related cache
            cache.delete_many([
                f'product_{product_id}',
                f'product_list',
                f'product_count'
            ])
            
            logger.info(f'Deleted product: {product_id}')
            return True
        except ObjectDoesNotExist:
            logger.warning(f'Product not found for deletion: {product_id}')
            return False

    @staticmethod
    def _validate_create_data(data: Dict[str, Any]) -> None:
        """Validate business rules for creation"""
        if not data:
            raise ValidationError('Data cannot be empty')
        
        # Add specific validation rules here
        # Example: price validation, email format, etc.
        pass
