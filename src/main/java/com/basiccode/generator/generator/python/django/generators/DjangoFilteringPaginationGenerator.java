package com.basiccode.generator.generator.python.django.generators;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DjangoFilteringPaginationGenerator - Phase 2
 * Advanced filtering, searching, and pagination capabilities with DjangoFilterBackend.
 *
 * Generates:
 * - FilterSet classes with advanced filtering
 * - Search field configuration
 * - Ordering field configuration
 * - Custom pagination classes
 * - Pagination serializers
 * - ViewSet filter_backends integration
 *
 * @version 1.0
 */
public class DjangoFilteringPaginationGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoFilteringPaginationGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates FilterSet class for model
     */
    public String generateFilterSet(ClassModel entity, List<Field> attributes) {
        StringBuilder code = new StringBuilder();
        String modelName = formatClassName(entity.getName());
        String filtersetName = modelName + "FilterSet";

        code.append("import django_filters\n");
        code.append("from django_filters import rest_framework as filters\n");
        code.append("from rest_framework import serializers\n");
        code.append("from .models import ").append(modelName).append("\n\n");

        code.append("class ").append(filtersetName).append("(django_filters.FilterSet):\n");
        code.append("    \"\"\"\n");
        code.append("    Advanced FilterSet for ").append(modelName).append(" with:\n");
        code.append("    - Range filtering for numeric fields\n");
        code.append("    - Date range filtering\n");
        code.append("    - Choice filtering\n");
        code.append("    - Text search capabilities\n");
        code.append("    \"\"\"\n\n");

        // Generate filter fields
        for (Field attr : attributes) {
            code.append(generateFilterField(attr));
        }

        // Meta class
        code.append("    class Meta:\n");
        code.append("        model = ").append(modelName).append("\n");
        code.append("        fields = [\n");
        attributes.forEach(attr -> 
            code.append("            '").append(formatFieldName(attr.getName())).append("',\n")
        );
        code.append("        ]\n\n");

        code.append("        filter_overrides = {\n");
        code.append("            models.CharField: {\n");
        code.append("                'filter_class': django_filters.CharFilter,\n");
        code.append("                'extra': lambda f: {'lookup_expr': 'icontains'},\n");
        code.append("            },\n");
        code.append("        }\n");

        return code.toString();
    }

    /**
     * Generates individual filter field
     */
    private String generateFilterField(Field attribute) {
        StringBuilder code = new StringBuilder();
        String fieldName = formatFieldName(attribute.getName());
        String fieldType = attribute.getType().toLowerCase();

        code.append("    # Filter for ").append(fieldName).append("\n");

        switch (fieldType) {
            case "string":
            case "text":
                code.append("    ").append(fieldName).append("__icontains = django_filters.CharFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='icontains'\n");
                code.append("    )\n");
                break;
            case "integer":
            case "int":
                code.append("    ").append(fieldName).append("__gte = django_filters.NumberFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='gte'\n");
                code.append("    )\n");
                code.append("    ").append(fieldName).append("__lte = django_filters.NumberFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='lte'\n");
                code.append("    )\n");
                break;
            case "date":
                code.append("    ").append(fieldName).append("__gte = django_filters.DateFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='gte'\n");
                code.append("    )\n");
                code.append("    ").append(fieldName).append("__lte = django_filters.DateFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='lte'\n");
                code.append("    )\n");
                break;
            case "datetime":
                code.append("    ").append(fieldName).append("__gte = django_filters.DateTimeFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='gte'\n");
                code.append("    )\n");
                code.append("    ").append(fieldName).append("__lte = django_filters.DateTimeFilter(\n");
                code.append("        field_name='").append(fieldName).append("',\n");
                code.append("        lookup_expr='lte'\n");
                code.append("    )\n");
                break;
            case "boolean":
                code.append("    ").append(fieldName).append(" = django_filters.BooleanFilter(\n");
                code.append("        field_name='").append(fieldName).append("'\n");
                code.append("    )\n");
                break;
        }

        code.append("\n");
        return code.toString();
    }

    /**
     * Generates ViewSet filter configuration
     */
    public String generateViewSetFilterConfig(ClassModel entity, List<Field> attributes) {
        StringBuilder code = new StringBuilder();
        String modelName = formatClassName(entity.getName());

        // Search fields
        List<String> searchFields = attributes.stream()
            .filter(a -> a.getType().toLowerCase().matches("string|text"))
            .map(a -> formatFieldName(a.getName()))
            .collect(Collectors.toList());

        // Ordering fields
        List<String> orderingFields = attributes.stream()
            .map(a -> formatFieldName(a.getName()))
            .collect(Collectors.toList());

        code.append("    # Filter and Search Configuration\n");
        code.append("    filter_backends = [\n");
        code.append("        DjangoFilterBackend,\n");
        code.append("        SearchFilter,\n");
        code.append("        OrderingFilter,\n");
        code.append("    ]\n\n");

        code.append("    filterset_class = ").append(modelName).append("FilterSet\n\n");

        if (!searchFields.isEmpty()) {
            code.append("    search_fields = [\n");
            searchFields.forEach(field ->
                code.append("        '@").append(field).append("',  # Search by ").append(field).append("\n")
            );
            code.append("    ]\n\n");
        }

        code.append("    ordering_fields = [\n");
        orderingFields.forEach(field ->
            code.append("        '").append(field).append("',\n")
        );
        code.append("    ]\n");
        code.append("    ordering = ['-created_at']\n\n");

        return code.toString();
    }

    /**
     * Generates custom pagination classes
     */
    public String generateCustomPaginationClasses() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework.pagination import PageNumberPagination, CursorPagination\n");
        code.append("from rest_framework.response import Response\n\n");

        code.append("class ").append("StandardPagination").append("(PageNumberPagination):\n");
        code.append("    \"\"\"\n");
        code.append("    Standard page number pagination with configurable page size\n");
        code.append("    \"\"\"\n");
        code.append("    page_size = 20\n");
        code.append("    page_size_query_param = 'page_size'\n");
        code.append("    page_size_query_description = 'Number of results to return per page'\n");
        code.append("    max_page_size = 100\n");
        code.append("    page_query_param = 'page'\n");
        code.append("    page_query_description = 'A page number within the paginated result set'\n\n");

        code.append("class ").append("CursorPaginationOptimized").append("(CursorPagination):\n");
        code.append("    \"\"\"\n");
        code.append("    Cursor-based pagination for better performance with large datasets\n");
        code.append("    \"\"\"\n");
        code.append("    page_size = 20\n");
        code.append("    page_size_query_param = 'page_size'\n");
        code.append("    max_page_size = 100\n");
        code.append("    ordering = '-created_at'\n");
        code.append("    template = 'rest_framework/pagination/numbers.html'\n\n");

        code.append("class ").append("LimitOffsetPaginationOptimized").append("(PageNumberPagination):\n");
        code.append("    \"\"\"\n");
        code.append("    Limit/Offset pagination for GraphQL-like queries\n");
        code.append("    \"\"\"\n");
        code.append("    page_size = 20\n");
        code.append("    page_size_query_param = 'limit'\n");
        code.append("    page_size_query_description = 'Number of results to return'\n");
        code.append("    template = None\n\n");

        return code.toString();
    }

    /**
     * Generates pagination serializers
     */
    public String generatePaginationSerializers(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("class ").append("PaginatedResponse").append("Serializer(serializers.Serializer):\n");
        code.append("    \"\"\"\n");
        code.append("    Generic paginated response serializer\n");
        code.append("    \"\"\"\n");
        code.append("    count = serializers.IntegerField()\n");
        code.append("    next = serializers.URLField(required=False, allow_null=True)\n");
        code.append("    previous = serializers.URLField(required=False, allow_null=True)\n");
        code.append("    results = serializers.ListField()\n\n");

        code.append("class ").append(modelName).append("PaginatedSerializer(serializers.Serializer):\n");
        code.append("    \"\"\"\n");
        code.append("    Paginated list response for ").append(modelName).append("\n");
        code.append("    \"\"\"\n");
        code.append("    count = serializers.IntegerField(help_text='Total number of results')\n");
        code.append("    next = serializers.URLField(\n");
        code.append("        required=False,\n");
        code.append("        allow_null=True,\n");
        code.append("        help_text='URL for next page'\n");
        code.append("    )\n");
        code.append("    previous = serializers.URLField(\n");
        code.append("        required=False,\n");
        code.append("        allow_null=True,\n");
        code.append("        help_text='URL for previous page'\n");
        code.append("    )\n");
        code.append("    results = ").append(modelName).append("ListSerializer(\n");
        code.append("        many=True,\n");
        code.append("        help_text='List of ").append(modelName).append(" items'\n");
        code.append("    )\n\n");

        return code.toString();
    }

    /**
     * Generates complete ViewSet with filtering and pagination
     */
    public String generateViewSetWithFiltering(ClassModel entity) {
        StringBuilder code = new StringBuilder();
        String modelName = formatClassName(entity.getName());
        String viewsetName = modelName + "ViewSet";

        code.append("from rest_framework import viewsets, filters\n");
        code.append("from django_filters.rest_framework import DjangoFilterBackend\n");
        code.append("from rest_framework.filters import SearchFilter, OrderingFilter\n");
        code.append("from rest_framework.decorators import action\n");
        code.append("from rest_framework.response import Response\n\n");

        code.append("class ").append(viewsetName).append("(viewsets.ModelViewSet):\n");
        code.append("    \"\"\"\n");
        code.append("    Complete ViewSet for ").append(modelName).append(" with advanced filtering\n");
        code.append("    \"\"\"\n");
        code.append("    queryset = ").append(modelName).append(".objects.all()\n");
        code.append("    serializer_class = ").append(modelName).append("ListSerializer\n");
        code.append("    pagination_class = StandardPagination\n\n");

        code.append(generateViewSetFilterConfig(entity, new ArrayList<>()));

        code.append("    def get_serializer_class(self):\n");
        code.append("        if self.action == 'create':\n");
        code.append("            return ").append(modelName).append("CreateSerializer\n");
        code.append("        elif self.action == 'update' or self.action == 'partial_update':\n");
        code.append("            return ").append(modelName).append("UpdateSerializer\n");
        code.append("        elif self.action == 'retrieve':\n");
        code.append("            return ").append(modelName).append("DetailSerializer\n");
        code.append("        return ").append(modelName).append("ListSerializer\n\n");

        code.append("    @action(detail=False, methods=['get'])\n");
        code.append("    def summary(self, request, *args, **kwargs):\n");
        code.append("        \"\"\"Get summary statistics of filtered results\"\"\"\n");
        code.append("        queryset = self.filter_queryset(self.get_queryset())\n");
        code.append("        return Response({\n");
        code.append("            'total': queryset.count(),\n");
        code.append("            'filters_applied': request.query_params.dict(),\n");
        code.append("        })\n");

        return code.toString();
    }

    /**
     * Generates API documentation for filtering
     */
    public String generateFilteringDocumentation(ClassModel entity, List<Field> attributes) {
        StringBuilder code = new StringBuilder();
        String modelName = formatClassName(entity.getName());

        code.append("\"\"\"\n");
        code.append("Filtering and Search Documentation for ").append(modelName).append("\n");
        for(int i = 0; i < 60; i++) code.append("=");
        code.append("\n\n");

        code.append("1. BASIC FILTERING\n");
        code.append("   GET /api/").append(modelName.toLowerCase()).append("/?field=value\n\n");

        code.append("2. SEARCH\n");
        code.append("   GET /api/").append(modelName.toLowerCase()).append("/?search=term\n\n");

        code.append("3. ORDERING\n");
        code.append("   GET /api/").append(modelName.toLowerCase()).append("/?ordering=field\n");
        code.append("   GET /api/").append(modelName.toLowerCase()).append("/?ordering=-field (descending)\n\n");

        code.append("4. PAGINATION\n");
        code.append("   GET /api/").append(modelName.toLowerCase()).append("/?page=1&page_size=20\n\n");

        code.append("5. COMBINED FILTERS\n");
        code.append("   GET /api/").append(modelName.toLowerCase())
            .append("/?search=term&ordering=-created_at&page=1\n\n");

        code.append("6. RANGE FILTERING\n");
        attributes.stream()
            .filter(a -> a.getType().toLowerCase().matches("integer|int|date|datetime|decimal|double|float"))
            .forEach(a -> {
                String fieldName = formatFieldName(a.getName());
                code.append("   GET /api/").append(modelName.toLowerCase()).append("/?")
                    .append(fieldName).append("__gte=value\n");
                code.append("   GET /api/").append(modelName.toLowerCase()).append("/?")
                    .append(fieldName).append("__lte=value\n");
            });

        code.append("\"\"\"\n");
        return code.toString();
    }

    /**
     * Formats field name (snake_case)
     */
    private String formatFieldName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Formats class name (PascalCase)
     */
    private String formatClassName(String name) {
        String[] parts = name.split("_");
        return Arrays.stream(parts)
            .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
            .collect(Collectors.joining());
    }
}
