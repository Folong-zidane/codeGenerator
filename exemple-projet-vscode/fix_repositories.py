#!/usr/bin/env python3
import os
import re

repo_dir = 'src/main/java/com/example/blog/repository'

for filename in os.listdir(repo_dir):
    if filename.endswith('Repository.java'):
        filepath = os.path.join(repo_dir, filename)
        
        with open(filepath, 'r') as f:
            content = f.read()
        
        # Add @Query import if not present
        if '@Query' not in content and 'findAllActive' in content:
            content = content.replace(
                'import org.springframework.data.jpa.repository.JpaRepository;',
                'import org.springframework.data.jpa.repository.JpaRepository;\nimport org.springframework.data.jpa.repository.Query;'
            )
        
        # Add @Query annotation before findAllActive method
        content = re.sub(
            r'(\s+)(Page<\w+> findAllActive\(Pageable pageable\);)',
            r'\1@Query("SELECT e FROM \2 e WHERE e.status = \'ACTIVE\'")\n\1\2',
            content
        )
        
        # Fix the query - extract entity name from repository name
        entity_name = filename.replace('Repository.java', '')
        content = re.sub(
            r'@Query\("SELECT e FROM Page<(\w+)> findAllActive',
            f'@Query("SELECT e FROM {entity_name} e WHERE e.status = \'ACTIVE\'")\n    Page<\\1> findAllActive',
            content
        )
        
        with open(filepath, 'w') as f:
            f.write(content)
        
        print(f"Fixed {filename}")

print("All repositories fixed!")
