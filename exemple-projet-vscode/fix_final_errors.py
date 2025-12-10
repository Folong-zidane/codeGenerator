#!/usr/bin/env python3
import re

# Fix entities with wrong getBoolean() return types or duplicates
entity_fixes = {
    'src/main/java/com/example/blog/entity/Page.java': [
        (r'    public Boolean getBoolean\(\) \{\s*return contenuHtml;\s*\}', ''),
        (r'    public Boolean getBoolean\(\) \{\s*return visible;\s*\}\n', '')
    ],
    'src/main/java/com/example/blog/entity/Paywall.java': [
        (r'    public Boolean getBoolean\(\) \{\s*return previewContent;\s*\}', ''),
    ],
    'src/main/java/com/example/blog/entity/DocumentPreview.java': [
        (r'    public Boolean getBoolean\(\) \{\s*return ocrText;\s*\}', ''),
    ],
    'src/main/java/com/example/blog/entity/NotificationTemplate.java': [
        (r'    public Boolean getBoolean\(\) \{\s*return corpsTemplate;\s*\}', ''),
    ],
}

for file_path, patterns in entity_fixes.items():
    try:
        with open(file_path, 'r') as f:
            content = f.read()
        
        for pattern, replacement in patterns:
            content = re.sub(pattern, replacement, content, flags=re.MULTILINE)
        
        with open(file_path, 'w') as f:
            f.write(content)
        print(f"Fixed {file_path}")
    except Exception as e:
        print(f"Error fixing {file_path}: {e}")

# Remove @Slf4j and log statements from services/controllers that use it
files_with_log = [
    'src/main/java/com/example/blog/service/PartageConfigService.java',
    'src/main/java/com/example/blog/service/ABTestService.java',
    'src/main/java/com/example/blog/service/CardPreviewService.java',
    'src/main/java/com/example/blog/service/ContratMaintenanceService.java',
    'src/main/java/com/example/blog/service/PerformanceMetricService.java',
    'src/main/java/com/example/blog/service/WCAGAuditService.java',
    'src/main/java/com/example/blog/controller/ABTestController.java',
    'src/main/java/com/example/blog/controller/ContratMaintenanceController.java',
    'src/main/java/com/example/blog/controller/MenuController.java',
    'src/main/java/com/example/blog/controller/PartageConfigController.java',
    'src/main/java/com/example/blog/controller/PerformanceMetricController.java',
    'src/main/java/com/example/blog/controller/WCAGAuditController.java',
]

for file_path in files_with_log:
    try:
        with open(file_path, 'r') as f:
            content = f.read()
        
        # Remove @Slf4j annotation
        content = re.sub(r'@Slf4j\n', '', content)
        content = re.sub(r'import lombok\.extern\.slf4j\.Slf4j;\n', '', content)
        
        # Remove log statements
        content = re.sub(r'\s*log\.(debug|info|warn|error)\([^)]+\);\n', '', content)
        
        with open(file_path, 'w') as f:
            f.write(content)
        print(f"Removed logging from {file_path}")
    except Exception as e:
        print(f"Error processing {file_path}: {e}")

print("All fixes applied!")
