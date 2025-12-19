# Django Serializers - Ultra-Pure Generated

from rest_framework import serializers
from django.core.validators import MinValueValidator, MaxValueValidator
from django.utils.translation import gettext_lazy as _
from typing import Dict, Any

from .models import Product

class ProductSerializer(serializers.ModelSerializer):
    """Comprehensive serializer for Product with validation"""

    # Computed fields
    full_name = serializers.SerializerMethodField()
    age_in_days = serializers.SerializerMethodField()
    is_recent = serializers.SerializerMethodField()

    class Meta:
        model = Product
        fields = '__all__'
        read_only_fields = ('id', 'created_at', 'updated_at')
        extra_kwargs = {
            'name': {
                'min_length': 1,
                'max_length': 255,
                'error_messages': {
                    'required': _('This field is required.'),
                    'blank': _('This field cannot be blank.'),
                }
            },
            'price': {
                'min_value': 0,
                'max_digits': 12,
                'decimal_places': 2,
                'error_messages': {
                    'required': _('This field is required.'),
                    'blank': _('This field cannot be blank.'),
                }
            },
            'stock': {
                'min_value': 0,
                'error_messages': {
                    'required': _('This field is required.'),
                    'blank': _('This field cannot be blank.'),
                }
            },
            'is_active': {
                'error_messages': {
                    'required': _('This field is required.'),
                    'blank': _('This field cannot be blank.'),
                }
            },
            'launch_date': {
                'error_messages': {
                    'required': _('This field is required.'),
                    'blank': _('This field cannot be blank.'),
                }
            },
        }

    def validate(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """Cross-field validation"""
        # Add business logic validation here
        return data

    def get_full_name(self, obj) -> str:
        """Generate full name if applicable"""
        if hasattr(obj, 'first_name') and hasattr(obj, 'last_name'):
            return f'{obj.first_name} {obj.last_name}'
        return str(obj)

    def get_age_in_days(self, obj) -> int:
        """Calculate age in days since creation"""
        from django.utils import timezone
        return (timezone.now() - obj.created_at).days

    def get_is_recent(self, obj) -> bool:
        """Check if object was created recently"""
        return self.get_age_in_days(obj) <= 7

class ProductCreateSerializer(serializers.ModelSerializer):
    """Optimized serializer for Product creation"""

    class Meta:
        model = Product
        exclude = ('id', 'created_at', 'updated_at')

    def create(self, validated_data: Dict[str, Any]) -> Product:
        """Create with additional processing"""
        # Pre-processing logic here
        instance = super().create(validated_data)
        # Post-processing logic here
        return instance


