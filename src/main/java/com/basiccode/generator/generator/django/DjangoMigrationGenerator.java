package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;

public class DjangoMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        if (enhancedClasses.isEmpty()) return "";
        
        EnhancedClass enhancedClass = enhancedClasses.get(0);
        UmlClass umlClass = enhancedClass.getOriginalClass();
        StringBuilder code = new StringBuilder();
        
        code.append("from django.db import migrations, models\n");
        code.append("import uuid\n\n");
        
        code.append("class Migration(migrations.Migration):\n");
        code.append("    initial = True\n");
        code.append("    dependencies = []\n\n");
        
        code.append("    operations = [\n");
        code.append("        migrations.CreateModel(\n");
        code.append("            name='").append(umlClass.getName()).append("',\n");
        code.append("            fields=[\n");
        
        for (UmlAttribute attr : umlClass.getAttributes()) {
            code.append("                ").append(generateMigrationField(attr, enhancedClass.isStateful() && attr.getName().equals("status"))).append(",\n");
        }
        
        code.append("            ],\n");
        code.append("            options={\n");
        code.append("                'db_table': '").append(umlClass.getName().toLowerCase()).append("s',\n");
        code.append("                'ordering': ['-id'],\n");
        code.append("            },\n");
        code.append("        ),\n");
        code.append("    ]\n");
        
        return code.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "migrations";
    }
    
    private String generateMigrationField(UmlAttribute attr, boolean isStatusField) {
        String fieldName = attr.getName();
        String fieldType = attr.getType();
        
        if (isStatusField) {
            return "('" + fieldName + "', models.CharField(max_length=20, default='ACTIVE'))";
        }
        
        switch (fieldType.toLowerCase()) {
            case "uuid":
                return "('" + fieldName + "', models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False))";
            case "string":
                return "('" + fieldName + "', models.CharField(max_length=255))";
            case "int":
            case "integer":
                return "('" + fieldName + "', models.IntegerField())";
            case "float":
            case "double":
                return "('" + fieldName + "', models.FloatField())";
            case "boolean":
            case "bool":
                return "('" + fieldName + "', models.BooleanField(default=False))";
            case "date":
                return "('" + fieldName + "', models.DateField())";
            case "datetime":
                return "('" + fieldName + "', models.DateTimeField(auto_now_add=True))";
            case "text":
                return "('" + fieldName + "', models.TextField())";
            case "email":
                return "('" + fieldName + "', models.EmailField())";
            default:
                return "('" + fieldName + "', models.CharField(max_length=255))";
        }
    }
}