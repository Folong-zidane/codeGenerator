from django.test import TestCase
from django.contrib.auth.models import User
from rest_framework.test import APITestCase, APIClient
from rest_framework import status
from django.urls import reverse
from unittest.mock import patch, Mock
import json

from .models import Product
from .services import ProductService

class ProductModelTest(TestCase):
    """Test Product model"""

    def setUp(self):
        self.product_data = {
            'name': 'Test Product',
            'is_active': True,
        }

    def test_create_product(self):
        """Test product creation"""
        product = Product.objects.create(**self.product_data)
        self.assertTrue(isinstance(product, Product))
        self.assertEqual(str(product), f'Product {product.id}')

    def test_product_str_representation(self):
        """Test string representation"""
        product = Product(**self.product_data)
        expected = f'Product {product.id}'
        self.assertEqual(str(product), expected)

class ProductServiceTest(TestCase):
    """Test ProductService"""

    def setUp(self):
        self.product_data = {
            'name': 'Test Product',
            'is_active': True,
        }

    def test_create_product(self):
        """Test service create method"""
        product = ProductService.create(self.product_data)
        self.assertIsInstance(product, Product)
        self.assertEqual(product.name, 'Test Product')

    def test_get_by_id(self):
        """Test service get_by_id method"""
        created_product = ProductService.create(self.product_data)
        retrieved_product = ProductService.get_by_id(str(created_product.id))
        self.assertEqual(created_product.id, retrieved_product.id)

    @patch('django.core.cache.cache')
    def test_cache_integration(self, mock_cache):
        """Test cache integration"""
        mock_cache.get.return_value = None
        product = ProductService.create(self.product_data)
        ProductService.get_by_id(str(product.id))
        mock_cache.set.assert_called()

class ProductAPITest(APITestCase):
    """Test Product API endpoints"""

    def setUp(self):
        self.user = User.objects.create_user(
            username='testuser',
            password='testpass123'
        )
        self.client = APIClient()
        self.client.force_authenticate(user=self.user)
        
        self.product_data = {
            'name': 'Test Product',
            'is_active': True,
        }

    def test_create_product_api(self):
        """Test POST /api/v1/products/"""
        url = reverse('product-list')
        response = self.client.post(url, self.product_data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertTrue(response.data['success'])
        self.assertIn('data', response.data)

    def test_list_products_api(self):
        """Test GET /api/v1/products/"""
        # Create test data
        ProductService.create(self.product_data)
        
        url = reverse('product-list')
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue(response.data['success'])
        self.assertIn('results', response.data)

    def test_retrieve_product_api(self):
        """Test GET /api/v1/products/{id}/"""
        product = ProductService.create(self.product_data)
        
        url = reverse('product-detail', kwargs={'pk': product.id})
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['id'], str(product.id))

    def test_update_product_api(self):
        """Test PUT /api/v1/products/{id}/"""
        product = ProductService.create(self.product_data)
        
        updated_data = self.product_data.copy()
        updated_data['name'] = 'Updated Product'
        
        url = reverse('product-detail', kwargs={'pk': product.id})
        response = self.client.put(url, updated_data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['name'], 'Updated Product')

    def test_delete_product_api(self):
        """Test DELETE /api/v1/products/{id}/"""
        product = ProductService.create(self.product_data)
        
        url = reverse('product-detail', kwargs={'pk': product.id})
        response = self.client.delete(url)
        
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertTrue(response.data['success'])

    def test_bulk_create_api(self):
        """Test POST /api/v1/products/bulk_create/"""
        bulk_data = [
            {'name': 'Bulk Product 1', 'is_active': True},
            {'name': 'Bulk Product 2', 'is_active': True},
        ]
        
        url = reverse('product-bulk-create')
        response = self.client.post(url, bulk_data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertTrue(response.data['success'])
        self.assertEqual(len(response.data['data']), 2)

    def test_unauthorized_access(self):
        """Test unauthorized access"""
        self.client.force_authenticate(user=None)
        
        url = reverse('product-list')
        response = self.client.get(url)
        
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)
