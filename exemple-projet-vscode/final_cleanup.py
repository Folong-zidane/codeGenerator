#!/usr/bin/env python3
import os
import re

def clean_entity(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Remove invalid getters that don't match field names
    content = re.sub(r'public\s+(?:Integer|String|Float|DateTime|JSON)\s+get(?:Integer|String|Float|DateTime|JSON)\(\)\s*\{[^}]*\}', '', content)
    
    # Remove duplicate getters/setters
    lines = content.split('\n')
    seen_methods = set()
    new_lines = []
    skip_until_brace = False
    
    for i, line in enumerate(lines):
        # Check for method signature
        method_match = re.match(r'\s*public\s+[\w<>\.]+\s+(get\w+|set\w+)\(', line)
        if method_match:
            method_sig = method_match.group(1)
            if method_sig in seen_methods:
                skip_until_brace = True
                continue
            else:
                seen_methods.add(method_sig)
        
        if skip_until_brace:
            if line.strip() == '}':
                skip_until_brace = False
            continue
        
        new_lines.append(line)
    
    content = '\n'.join(new_lines)
    
    # Clean up multiple blank lines
    content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    
    return True

entity_dir = 'entity'
for filename in sorted(os.listdir(entity_dir)):
    if filename.endswith('.java'):
        filepath = os.path.join(entity_dir, filename)
        clean_entity(filepath)
        print(f'Cleaned: {filename}')

print('\nAll entities cleaned!')
