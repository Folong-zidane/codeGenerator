#!/usr/bin/env python3
import os
import re

repo_dir = 'src/main/java/com/example/blog/repository'

for filename in os.listdir(repo_dir):
    if filename.endswith('Repository.java'):
        filepath = os.path.join(repo_dir, filename)
        entity_name = filename.replace('Repository.java', '')
        
        with open(filepath, 'r') as f:
            content = f.read()
        
        # Remove broken duplicated lines
        content = re.sub(
            r'Page<\w+> findAllActive\(Pageable pageable\); e WHERE e\.status = \\\\'ACTIVE\\\\'"\)\s*',
            '',
            content
        )
        
        # Remove any standalone broken findAllActive without @Query
        lines = content.split('\n')
        new_lines = []
        skip_next = False
        
        for i, line in enumerate(lines):
            if skip_next:
                skip_next = False
                continue
                
            # If we find a findAllActive without @Query on previous line, skip it
            if 'findAllActive(Pageable pageable);' in line:
                if i > 0 and '@Query' not in lines[i-1]:
                    continue
            
            new_lines.append(line)
        
        content = '\n'.join(new_lines)
        
        # Ensure @Query import exists
        if 'findAllActive' in content and 'import org.springframework.data.jpa.repository.Query;' not in content:
            content = content.replace(
                'import org.springframework.data.jpa.repository.JpaRepository;',
                'import org.springframework.data.jpa.repository.JpaRepository;\nimport org.springframework.data.jpa.repository.Query;'
            )
        
        # Ensure @Query annotation exists before findAllActive
        if 'findAllActive' in content:
            # Check if @Query already exists
            if not re.search(r'@Query.*\n.*findAllActive', content):
                # Add @Query before findAllActive
                content = re.sub(
                    r'(\s+)(Page<\w+> findAllActive\(Pageable pageable\);)',
                    f'\\1@Query("SELECT e FROM {entity_name} e WHERE e.status = \'ACTIVE\'")\n\\1\\2',
                    content
                )
        
        with open(filepath, 'w') as f:
            f.write(content)
        
        print(f"Fixed {filename}")

print("All repositories fixed!")
