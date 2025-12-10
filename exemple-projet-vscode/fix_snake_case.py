#!/usr/bin/env python3
import re

files = [
    ('src/main/java/com/example/blog/entity/ContentVersion.java', 'restore_point', 'restorePoint'),
    ('src/main/java/com/example/blog/entity/SEOConfig.java', 'robots_index', 'robotsIndex'),
    ('src/main/java/com/example/blog/entity/WCAGAudit.java', 'auto_fix_available', 'autoFixAvailable'),
    ('src/main/java/com/example/blog/entity/Page.java', 'contenu_html', 'contenuHtml'),
    ('src/main/java/com/example/blog/entity/ProduitPremium.java', 'mobile_money_actif', 'mobileMoneyActif'),
    ('src/main/java/com/example/blog/entity/RecoveryPoint.java', 'auto_interval', 'autoInterval'),
    ('src/main/java/com/example/blog/entity/MediaProcessingJob.java', 'error_message', 'errorMessage'),
    ('src/main/java/com/example/blog/entity/Paywall.java', 'preview_content', 'previewContent'),
    ('src/main/java/com/example/blog/entity/DocumentPreview.java', 'ocr_text', 'ocrText'),
    ('src/main/java/com/example/blog/entity/NotificationTemplate.java', 'corps_template', 'corpsTemplate'),
    ('src/main/java/com/example/blog/entity/NotificationPreference.java', 'email_actif', 'emailActif'),
    ('src/main/java/com/example/blog/entity/AbonnementPayant.java', 'auto_renouvellement', 'autoRenouvellement'),
    ('src/main/java/com/example/blog/entity/MediaLicense.java', 'usage_restrictions', 'usageRestrictions'),
    ('src/main/java/com/example/blog/entity/LangueContenu.java', 'auto_traduit', 'autoTraduit'),
    ('src/main/java/com/example/blog/entity/CDNConfig.java', 'purge_on_update', 'purgeOnUpdate'),
    ('src/main/java/com/example/blog/entity/PWAConfig.java', 'push_notifications', 'pushNotifications'),
    ('src/main/java/com/example/blog/entity/EmailNotification.java', 'corps_html', 'corpsHtml'),
    ('src/main/java/com/example/blog/entity/AbonnementNewsletter.java', 'double_optin', 'doubleOptin'),
]

for file_path, snake, camel in files:
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Replace snake_case reference in getBoolean() method
    content = re.sub(
        r'public \w+ get\w+\(\) \{\s*return ' + snake + ';',
        f'public Boolean getBoolean() {{\n        return {camel};',
        content
    )
    
    with open(file_path, 'w') as f:
        f.write(content)
    print(f"Fixed {file_path}")

print("All files fixed!")
