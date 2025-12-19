package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur de tests Django ultra-complets
 */
public class DjangoTestGenerator {
    
    public String generateTests(EnhancedClass enhancedClass, String packageName) {
        StringBuilder tests = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        tests.append("from django.test import TestCase\n");
        tests.append("from django.contrib.auth.models import User\n");
        tests.append("from rest_framework.test import APITestCase, APIClient\n");
        tests.append("from rest_framework import status\n");
        tests.append("from django.urls import reverse\n");
        tests.append("from unittest.mock import patch, Mock\n");
        tests.append("import json\n\n");
        
        tests.append("from .models import ").append(className).append("\n");
        tests.append("from .services import ").append(className).append("Service\n\n");
        
        // Model tests
        tests.append("class ").append(className).append("ModelTest(TestCase):\n");
        tests.append("    \"\"\"Test ").append(className).append(" model\"\"\"\n\n");
        
        tests.append("    def setUp(self):\n");
        tests.append("        self.").append(className.toLowerCase()).append("_data = {\n");
        tests.append("            'name': 'Test ").append(className).append("',\n");
        tests.append("            'is_active': True,\n");
        tests.append("        }\n\n");
        
        tests.append("    def test_create_").append(className.toLowerCase()).append("(self):\n");
        tests.append("        \"\"\"Test ").append(className.toLowerCase()).append(" creation\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append(".objects.create(**self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        self.assertTrue(isinstance(").append(className.toLowerCase()).append(", ").append(className).append("))\n");
        tests.append("        self.assertEqual(str(").append(className.toLowerCase()).append("), f'").append(className).append(" {").append(className.toLowerCase()).append(".id}')\n\n");
        
        tests.append("    def test_").append(className.toLowerCase()).append("_str_representation(self):\n");
        tests.append("        \"\"\"Test string representation\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("(**self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        expected = f'").append(className).append(" {").append(className.toLowerCase()).append(".id}'\n");
        tests.append("        self.assertEqual(str(").append(className.toLowerCase()).append("), expected)\n\n");
        
        // Service tests
        tests.append("class ").append(className).append("ServiceTest(TestCase):\n");
        tests.append("    \"\"\"Test ").append(className).append("Service\"\"\"\n\n");
        
        tests.append("    def setUp(self):\n");
        tests.append("        self.").append(className.toLowerCase()).append("_data = {\n");
        tests.append("            'name': 'Test ").append(className).append("',\n");
        tests.append("            'is_active': True,\n");
        tests.append("        }\n\n");
        
        tests.append("    def test_create_").append(className.toLowerCase()).append("(self):\n");
        tests.append("        \"\"\"Test service create method\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        self.assertIsInstance(").append(className.toLowerCase()).append(", ").append(className).append(")\n");
        tests.append("        self.assertEqual(").append(className.toLowerCase()).append(".name, 'Test ").append(className).append("')\n\n");
        
        tests.append("    def test_get_by_id(self):\n");
        tests.append("        \"\"\"Test service get_by_id method\"\"\"\n");
        tests.append("        created_").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        retrieved_").append(className.toLowerCase()).append(" = ").append(className).append("Service.get_by_id(str(created_").append(className.toLowerCase()).append(".id))\n");
        tests.append("        self.assertEqual(created_").append(className.toLowerCase()).append(".id, retrieved_").append(className.toLowerCase()).append(".id)\n\n");
        
        tests.append("    @patch('django.core.cache.cache')\n");
        tests.append("    def test_cache_integration(self, mock_cache):\n");
        tests.append("        \"\"\"Test cache integration\"\"\"\n");
        tests.append("        mock_cache.get.return_value = None\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        ").append(className).append("Service.get_by_id(str(").append(className.toLowerCase()).append(".id))\n");
        tests.append("        mock_cache.set.assert_called()\n\n");
        
        // API tests
        tests.append("class ").append(className).append("APITest(APITestCase):\n");
        tests.append("    \"\"\"Test ").append(className).append(" API endpoints\"\"\"\n\n");
        
        tests.append("    def setUp(self):\n");
        tests.append("        self.user = User.objects.create_user(\n");
        tests.append("            username='testuser',\n");
        tests.append("            password='testpass123'\n");
        tests.append("        )\n");
        tests.append("        self.client = APIClient()\n");
        tests.append("        self.client.force_authenticate(user=self.user)\n");
        tests.append("        \n");
        tests.append("        self.").append(className.toLowerCase()).append("_data = {\n");
        tests.append("            'name': 'Test ").append(className).append("',\n");
        tests.append("            'is_active': True,\n");
        tests.append("        }\n\n");
        
        tests.append("    def test_create_").append(className.toLowerCase()).append("_api(self):\n");
        tests.append("        \"\"\"Test POST /api/v1/").append(className.toLowerCase()).append("s/\"\"\"\n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-list')\n");
        tests.append("        response = self.client.post(url, self.").append(className.toLowerCase()).append("_data, format='json')\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_201_CREATED)\n");
        tests.append("        self.assertTrue(response.data['success'])\n");
        tests.append("        self.assertIn('data', response.data)\n\n");
        
        tests.append("    def test_list_").append(className.toLowerCase()).append("s_api(self):\n");
        tests.append("        \"\"\"Test GET /api/v1/").append(className.toLowerCase()).append("s/\"\"\"\n");
        tests.append("        # Create test data\n");
        tests.append("        ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-list')\n");
        tests.append("        response = self.client.get(url)\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        tests.append("        self.assertTrue(response.data['success'])\n");
        tests.append("        self.assertIn('results', response.data)\n\n");
        
        tests.append("    def test_retrieve_").append(className.toLowerCase()).append("_api(self):\n");
        tests.append("        \"\"\"Test GET /api/v1/").append(className.toLowerCase()).append("s/{id}/\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-detail', kwargs={'pk': ").append(className.toLowerCase()).append(".id})\n");
        tests.append("        response = self.client.get(url)\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        tests.append("        self.assertEqual(response.data['id'], str(").append(className.toLowerCase()).append(".id))\n\n");
        
        tests.append("    def test_update_").append(className.toLowerCase()).append("_api(self):\n");
        tests.append("        \"\"\"Test PUT /api/v1/").append(className.toLowerCase()).append("s/{id}/\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        \n");
        tests.append("        updated_data = self.").append(className.toLowerCase()).append("_data.copy()\n");
        tests.append("        updated_data['name'] = 'Updated ").append(className).append("'\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-detail', kwargs={'pk': ").append(className.toLowerCase()).append(".id})\n");
        tests.append("        response = self.client.put(url, updated_data, format='json')\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        tests.append("        self.assertEqual(response.data['name'], 'Updated ").append(className).append("')\n\n");
        
        tests.append("    def test_delete_").append(className.toLowerCase()).append("_api(self):\n");
        tests.append("        \"\"\"Test DELETE /api/v1/").append(className.toLowerCase()).append("s/{id}/\"\"\"\n");
        tests.append("        ").append(className.toLowerCase()).append(" = ").append(className).append("Service.create(self.").append(className.toLowerCase()).append("_data)\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-detail', kwargs={'pk': ").append(className.toLowerCase()).append(".id})\n");
        tests.append("        response = self.client.delete(url)\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        tests.append("        self.assertTrue(response.data['success'])\n\n");
        
        tests.append("    def test_bulk_create_api(self):\n");
        tests.append("        \"\"\"Test POST /api/v1/").append(className.toLowerCase()).append("s/bulk_create/\"\"\"\n");
        tests.append("        bulk_data = [\n");
        tests.append("            {'name': 'Bulk ").append(className).append(" 1', 'is_active': True},\n");
        tests.append("            {'name': 'Bulk ").append(className).append(" 2', 'is_active': True},\n");
        tests.append("        ]\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-bulk-create')\n");
        tests.append("        response = self.client.post(url, bulk_data, format='json')\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_201_CREATED)\n");
        tests.append("        self.assertTrue(response.data['success'])\n");
        tests.append("        self.assertEqual(len(response.data['data']), 2)\n\n");
        
        tests.append("    def test_unauthorized_access(self):\n");
        tests.append("        \"\"\"Test unauthorized access\"\"\"\n");
        tests.append("        self.client.force_authenticate(user=None)\n");
        tests.append("        \n");
        tests.append("        url = reverse('").append(className.toLowerCase()).append("-list')\n");
        tests.append("        response = self.client.get(url)\n");
        tests.append("        \n");
        tests.append("        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)\n");
        
        return tests.toString();
    }
    
    public String getFileExtension() {
        return ".py";
    }
    
    public String getTestDirectory() {
        return "tests";
    }
}