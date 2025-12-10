#!/usr/bin/env python3
import os
import re

repo_dir = 'src/main/java/com/example/blog/repository'

for filename in os.listdir(repo_dir):
    if filename.endswith('Repository.java'):
        filepath = os.path.join(repo_dir, filename)
        entity_name = filename.replace('Repository.java', '')
        
        with open(filepath, 'r') as f:
            lines = f.readlines()
        
        new_lines = []
        i = 0
        while i < len(lines):
            line = lines[i]
            
            # Skip broken duplicate lines
            if '; e WHERE e.status =' in line or "ACTIVE'" in line and 'findAllActive' not in line:
                i += 1
                continue
            
            # If this is findAllActive without @Query on previous line, check if we need to add it
            if 'findAllActive(Pageable pageable);' in line:
                # Check if previous line has @Query
                if new_lines and '@Query' not in new_lines[-1]:
                    # Add @Query before this line
                    indent = len(line) - len(line.lstrip())
                    query_line = ' ' * indent + f'@Query("SELECT e FROM {entity_name} e WHERE e.status = \'ACTIVE\'")\n'
                    new_lines.append(query_line)
            
            new_lines.append(line)
            i += 1
        
        content = ''.join(new_lines)
        
        # Ensure @Query import exists
        if 'findAllActive' in content and 'import org.springframework.data.jpa.repository.Query;' not in content:
            content = content.replace(
                'import org.springframework.data.jpa.repository.JpaRepository;',
                'import org.springframework.data.jpa.repository.JpaRepository;\nimport org.springframework.data.jpa.repository.Query;'
            )
        
        with open(filepath, 'w') as f:
            f.write(content)
        
        print(f"Fixed {filename}")

print("All repositories fixed!")
