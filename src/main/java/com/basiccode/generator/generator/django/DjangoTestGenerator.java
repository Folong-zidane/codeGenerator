package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ✅ Django Test Generator (Phase 1)
 *
 * Génère des tests Django/Pytest avec:
 * - Test de modèles (Model tests)
 * - Test d'API (API tests)
 * - Test de sérializers
 * - Fixtures pytest
 * - Tests unitaires complets
 * - Coverage reports
 *
 * NOUVEAU: Phase 1 addition
 * Génère: tests unitaires + tests d'intégration
 */
@Component
@Slf4j
public class DjangoTestGenerator {

    public String generateModelTests(EnhancedClass enhancedClass) {
        log.info("🧪 Generating model tests for: {}", enhancedClass.getOriginalClass().getName());
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        code.append(generateTestHeader());
        code.append(generateModelTestClass(className, enhancedClass));

        return code.toString();
    }

    public String generateApiTests(EnhancedClass enhancedClass) {
        log.info("🧪 Generating API tests for: {}", enhancedClass.getOriginalClass().getName());
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        code.append(generateTestHeader());
        code.append(generateApiTestClass(className, enhancedClass));

        return code.toString();
    }

    public String generateSerializerTests(EnhancedClass enhancedClass) {
        log.info("🧪 Generating serializer tests for: {}", enhancedClass.getOriginalClass().getName());
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        code.append(generateTestHeader());
        code.append(generateSerializerTestClass(className, enhancedClass));

        return code.toString();
    }

    private String generateTestHeader() {
        StringBuilder header = new StringBuilder();

        header.append("# Generated Django Tests\n");
        header.append("# By BasicCode Generator - Phase 1\n\n");

        header.append("import pytest\n");
        header.append("from django.test import TestCase, Client\n");
        header.append("from django.contrib.auth.models import User\n");
        header.append("from rest_framework.test import APIClient, APITestCase\n");
        header.append("from rest_framework import status\n");
        header.append("from .models import *\n");
        header.append("from .serializers import *\n");
        header.append("from .enums import *\n\n");

        header.append("@pytest.mark.django_db\n");
        header.append("class TestFixtures:\n");
        header.append("    \"\"\"Pytest fixtures for tests\"\"\"\n\n");

        header.append("    @pytest.fixture\n");
        header.append("    def api_client(self):\n");
        header.append("        return APIClient()\n\n");

        header.append("    @pytest.fixture\n");
        header.append("    def authenticated_client(self):\n");
        header.append("        client = APIClient()\n");
        header.append("        user = User.objects.create_user(username='testuser', password='testpass')\n");
        header.append("        client.force_authenticate(user=user)\n");
        header.append("        return client\n\n");

        return header.toString();
    }

    private String generateModelTestClass(String className, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();

        code.append("class Test").append(className).append("Model(TestCase):\n");
        code.append("    \"\"\"Tests for ").append(className).append(" model\"\"\"\n\n");

        code.append("    @classmethod\n");
        code.append("    def setUpTestData(cls):\n");
        code.append("        \"\"\"Set up test data for all tests\"\"\"\n");
        code.append("        cls.instance = ").append(className).append(".objects.create(\n");

        // Add sample data for each attribute
        enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
            if (!attr.getName().equals("id")) {
                code.append("            ").append(attr.getName()).append("=");
                switch (attr.getType().toLowerCase()) {
                    case "string":
                    case "name":
                        code.append("'Test ").append(className).append("'");
                        break;
                    case "email":
                        code.append("'test@example.com'");
                        break;
                    case "int":
                    case "integer":
                        code.append("1");
                        break;
                    case "float":
                    case "double":
                        code.append("1.0");
                        break;
                    case "boolean":
                        code.append("True");
                        break;
                    default:
                        code.append("'test'");
                }
                code.append(",\n");
            }
        });

        code.append("        )\n\n");

        code.append("    def test_model_creation(self):\n");
        code.append("        self.assertTrue(").append(className).append(".objects.filter(id=self.instance.id).exists())\n\n");

        code.append("    def test_model_str_representation(self):\n");
        code.append("        self.assertEqual(str(self.instance), f'").append(className).append("({self.instance.id})')\n\n");

        code.append("    def test_model_fields(self):\n");
        code.append("        instance = ").append(className).append(".objects.get(id=self.instance.id)\n");
        code.append("        self.assertIsNotNone(instance.id)\n");
        code.append("        self.assertIsNotNone(instance.created_at)\n\n");

        if (enhancedClass.isStateful()) {
            code.append("    def test_status_field(self):\n");
            code.append("        self.assertEqual(self.instance.status, ").append(className).append("Status.ACTIVE)\n\n");

            code.append("    def test_can_suspend(self):\n");
            code.append("        self.assertTrue(self.instance.can_suspend())\n\n");

            code.append("    def test_can_activate(self):\n");
            code.append("        self.instance.status = ").append(className).append("Status.SUSPENDED\n");
            code.append("        self.instance.save()\n");
            code.append("        self.assertTrue(self.instance.can_activate())\n\n");
        }

        return code.toString();
    }

    private String generateApiTestClass(String className, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();
        String modelNameLower = className.substring(0, 1).toLowerCase() + className.substring(1);

        code.append("class Test").append(className).append("API(APITestCase):\n");
        code.append("    \"\"\"Tests for ").append(className).append(" API endpoints\"\"\"\n\n");

        code.append("    def setUp(self):\n");
        code.append("        self.client = APIClient()\n");
        code.append("        self.user = User.objects.create_user(username='testuser', password='testpass')\n");
        code.append("        self.client.force_authenticate(user=self.user)\n");
        code.append("        self.endpoint = '/api/").append(modelNameLower).append("s/'\n");
        code.append("        self.instance = ").append(className).append(".objects.create(\n");

        enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
            if (!attr.getName().equals("id")) {
                code.append("            ").append(attr.getName()).append("='test',\n");
            }
        });

        code.append("        )\n\n");

        code.append("    def test_list_").append(modelNameLower).append("s(self):\n");
        code.append("        response = self.client.get(self.endpoint)\n");
        code.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        code.append("        self.assertGreaterEqual(len(response.data), 1)\n\n");

        code.append("    def test_create_").append(modelNameLower).append("(self):\n");
        code.append("        data = {\n");
        enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
            if (!attr.getName().equals("id")) {
                code.append("            '").append(attr.getName()).append("': 'new_value',\n");
            }
        });
        code.append("        }\n");
        code.append("        response = self.client.post(self.endpoint, data)\n");
        code.append("        self.assertEqual(response.status_code, status.HTTP_201_CREATED)\n\n");

        code.append("    def test_retrieve_").append(modelNameLower).append("(self):\n");
        code.append("        response = self.client.get(f'{self.endpoint}{self.instance.id}/')\n");
        code.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n");
        code.append("        self.assertEqual(response.data['id'], str(self.instance.id))\n\n");

        code.append("    def test_update_").append(modelNameLower).append("(self):\n");
        code.append("        data = {'name': 'updated_name'}\n");
        code.append("        response = self.client.patch(f'{self.endpoint}{self.instance.id}/', data)\n");
        code.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n\n");

        code.append("    def test_delete_").append(modelNameLower).append("(self):\n");
        code.append("        response = self.client.delete(f'{self.endpoint}{self.instance.id}/')\n");
        code.append("        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)\n");
        code.append("        self.assertFalse(").append(className).append(".objects.filter(id=self.instance.id).exists())\n\n");

        if (enhancedClass.isStateful()) {
            code.append("    def test_suspend_").append(modelNameLower).append("(self):\n");
            code.append("        response = self.client.post(f'{self.endpoint}{self.instance.id}/suspend/')\n");
            code.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n\n");

            code.append("    def test_activate_").append(modelNameLower).append("(self):\n");
            code.append("        response = self.client.post(f'{self.endpoint}{self.instance.id}/activate/')\n");
            code.append("        self.assertEqual(response.status_code, status.HTTP_200_OK)\n\n");
        }

        return code.toString();
    }

    private String generateSerializerTestClass(String className, EnhancedClass enhancedClass) {
        StringBuilder code = new StringBuilder();

        code.append("class Test").append(className).append("Serializer(TestCase):\n");
        code.append("    \"\"\"Tests for ").append(className).append(" serializer\"\"\"\n\n");

        code.append("    def setUp(self):\n");
        code.append("        self.instance = ").append(className).append(".objects.create(\n");

        enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
            if (!attr.getName().equals("id")) {
                code.append("            ").append(attr.getName()).append("='test',\n");
            }
        });

        code.append("        )\n\n");

        code.append("    def test_serializer_valid_data(self):\n");
        code.append("        serializer = ").append(className).append("Serializer(self.instance)\n");
        code.append("        self.assertEqual(serializer.data['id'], str(self.instance.id))\n\n");

        code.append("    def test_create_serializer(self):\n");
        code.append("        data = {\n");
        enhancedClass.getOriginalClass().getAttributes().forEach(attr -> {
            if (!attr.getName().equals("id")) {
                code.append("            '").append(attr.getName()).append("': 'new_value',\n");
            }
        });
        code.append("        }\n");
        code.append("        serializer = ").append(className).append("CreateSerializer(data=data)\n");
        code.append("        self.assertTrue(serializer.is_valid())\n\n");

        code.append("    def test_update_serializer(self):\n");
        code.append("        data = {'name': 'updated'}\n");
        code.append("        serializer = ").append(className).append("UpdateSerializer(self.instance, data=data, partial=True)\n");
        code.append("        self.assertTrue(serializer.is_valid())\n\n");

        return code.toString();
    }
}
