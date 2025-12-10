#!/usr/bin/env python3
import os
import re

def fix_entity_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Skip if already fixed
    if '@Id' in content and 'private Integer id;' in content:
        return False
    
    lines = content.split('\n')
    new_lines = []
    i = 0
    
    while i < len(lines):
        line = lines[i]
        
        # Fix field declarations with invalid syntax
        if re.match(r'\s*@Column', line) and i + 1 < len(lines):
            next_line = lines[i + 1]
            # Check for invalid field syntax like "private id: Integer PK;"
            if re.match(r'\s*private\s+\w+:\s+\w+', next_line):
                # Extract field info
                match = re.match(r'\s*private\s+(\w+):\s+(\w+)(\s+\w+)?;', next_line)
                if match:
                    field_name = match.group(1)
                    field_type = match.group(2)
                    
                    # Convert snake_case to camelCase
                    camel_name = ''.join(word.capitalize() if idx > 0 else word 
                                        for idx, word in enumerate(field_name.split('_')))
                    
                    # Map types
                    type_map = {
                        'Integer': 'Integer',
                        'String': 'String',
                        'Boolean': 'Boolean',
                        'Float': 'Float',
                        'Double': 'Double',
                        'Long': 'Long',
                        'Text': 'String',
                        'DateTime': 'LocalDateTime',
                        'JSON': 'String'
                    }
                    java_type = type_map.get(field_type, 'String')
                    
                    # Special handling for id field
                    if field_name == 'id':
                        new_lines.append('    @Id')
                        new_lines.append('    @GeneratedValue(strategy = GenerationType.IDENTITY)')
                        new_lines.append(f'    private {java_type} id;')
                    else:
                        # Add column annotation with proper name
                        col_def = ''
                        if field_type == 'Text':
                            col_def = ', columnDefinition = "TEXT"'
                        elif field_type == 'JSON':
                            col_def = ', columnDefinition = "JSON"'
                        
                        new_lines.append(f'    @Column(name = "{field_name}"{col_def})')
                        new_lines.append(f'    private {java_type} {camel_name};')
                    
                    i += 2
                    continue
        
        new_lines.append(line)
        i += 1
    
    # Fix getters and setters
    content = '\n'.join(new_lines)
    
    # Fix invalid getter/setter patterns
    content = re.sub(
        r'public\s+(\w+):\s+get(\w+)(\s+\w+)?\(\)\s*\{\s*return\s+\w+(\s+\w+)?;',
        lambda m: f'public {m.group(2) if m.group(2) != "Integer" else "Integer"} get{m.group(2)}() {{\n        return {m.group(1)};',
        content
    )
    
    # Remove invalid enum method declarations
    content = re.sub(r'public\s+void\s+\w+:\s+Enum\([^)]+\)\s*\{[^}]*\}\s*', '', content)
    
    # Fix List~Type~ to List<Type>
    content = re.sub(r'List~(\w+)~', r'java.util.List<\1>', content)
    
    # Fix HTML type to String
    content = re.sub(r'public\s+HTML\s+', 'public String ', content)
    
    # Fix File type
    content = re.sub(r'public\s+File\s+', 'public java.io.File ', content)
    
    # Fix float literal
    content = re.sub(r'return\s+0\.0;', 'return 0.0f;', content)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    
    return True

# Process all entity files
entity_dir = 'entity'
fixed_count = 0

for filename in sorted(os.listdir(entity_dir)):
    if filename.endswith('.java'):
        filepath = os.path.join(entity_dir, filename)
        if fix_entity_file(filepath):
            fixed_count += 1
            print(f'Fixed: {filename}')

print(f'\nTotal files processed: {fixed_count}')
