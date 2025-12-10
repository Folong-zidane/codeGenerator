#!/usr/bin/env python3
import os
import re
import glob

# Corriger les imports d'enums dans les repositories
def fix_repository_imports(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Extraire le nom de l'entité depuis le nom du fichier
    entity_name = os.path.basename(filepath).replace('Repository.java', '')
    enum_name = f"{entity_name}Status"
    
    # Vérifier si l'enum est utilisé mais pas importé
    if enum_name in content and f'import com.example.blog.enums.{enum_name}' not in content:
        # Ajouter l'import après les autres imports
        import_line = f'import com.example.blog.enums.{enum_name};\n'
        # Trouver la position après le dernier import
        last_import = content.rfind('import ')
        if last_import != -1:
            end_of_line = content.find('\n', last_import)
            content = content[:end_of_line+1] + import_line + content[end_of_line+1:]
    
    with open(filepath, 'w') as f:
        f.write(content)

# Corriger les types Text et JSON dans les entités
def fix_entity_types(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    modified = False
    
    # Remplacer "public Text " par "public String "
    if 'public Text ' in content:
        content = content.replace('public Text ', 'public String ')
        modified = True
    
    # Remplacer "public JSON " par "public String "
    if 'public JSON ' in content:
        content = content.replace('public JSON ', 'public String ')
        modified = True
    
    # Remplacer "public User " par "public Utilisateur "
    if 'public User ' in content:
        content = content.replace('public User ', 'public Utilisateur ')
        modified = True
    
    if modified:
        with open(filepath, 'w') as f:
            f.write(content)

# Corriger le conflit de nom Page
def fix_page_conflict(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    if 'com.example.blog.entity.Page' in content:
        # Remplacer l'import Spring Data Page par un alias
        content = content.replace(
            'import org.springframework.data.domain.Page;',
            'import org.springframework.data.domain.Page as SpringPage;'
        )
        # Utiliser le nom complet pour l'entité Page
        content = content.replace(
            'Page<',
            'org.springframework.data.domain.Page<'
        )
    
    with open(filepath, 'w') as f:
        f.write(content)

print("Correction des repositories...")
for repo_file in glob.glob('src/main/java/com/example/blog/repository/*.java'):
    fix_repository_imports(repo_file)
    print(f"  Fixed: {os.path.basename(repo_file)}")

print("\nCorrection des entités...")
for entity_file in glob.glob('src/main/java/com/example/blog/entity/*.java'):
    fix_entity_types(entity_file)
    print(f"  Fixed: {os.path.basename(entity_file)}")

print("\nCorrection du conflit Page...")
for file in ['src/main/java/com/example/blog/repository/PageRepository.java',
             'src/main/java/com/example/blog/service/PageService.java']:
    if os.path.exists(file):
        fix_page_conflict(file)
        print(f"  Fixed: {os.path.basename(file)}")

print("\nToutes les corrections appliquées!")
