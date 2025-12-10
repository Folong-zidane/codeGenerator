#!/usr/bin/env python3
import os
import re

def snake_to_camel(snake_str):
    components = snake_str.split('_')
    return components[0] + ''.join(x.capitalize() for x in components[1:])

def camel_to_pascal(camel_str):
    return camel_str[0].upper() + camel_str[1:] if camel_str else ''

def extract_fields(content):
    fields = {}
    pattern = r'@Column[^;]*\s+private\s+(\w+)\s+(\w+);'
    for match in re.finditer(pattern, content):
        field_type = match.group(1)
        field_name = match.group(2)
        fields[field_name] = field_type
    
    pattern = r'@Id[^;]*\s+private\s+(\w+)\s+(\w+);'
    for match in re.finditer(pattern, content):
        field_type = match.group(1)
        field_name = match.group(2)
        fields[field_name] = field_type
    
    return fields

def fix_getters_setters(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    fields = extract_fields(content)
    
    if not fields:
        return False
    
    content = re.sub(r'public\s+\w+:\s+get\w+(\s+\w+)?\(\)\s*\{[^}]*\}', '', content)
    content = re.sub(r'public\s+void\s+set\w+(\s+\w+)?\([^)]*\)\s*\{[^}]*\}', '', content)
    
    method_pattern = r'(\n\s+public\s+(?:void|[\w<>\.]+)\s+(?!get|set)\w+\([^)]*\)\s*\{)'
    match = re.search(method_pattern, content)
    
    if match:
        insert_pos = match.start()
    else:
        insert_pos = content.rfind('}')
    
    getters_setters = []
    for field_name, field_type in sorted(fields.items()):
        pascal_name = camel_to_pascal(field_name)
        
        getter = f'\n    public {field_type} get{pascal_name}() {{\n        return {field_name};\n    }}'
        setter = f'\n    public void set{pascal_name}({field_type} {field_name}) {{\n        this.{field_name} = {field_name};\n    }}'
        
        getters_setters.append(getter)
        getters_setters.append(setter)
    
    new_content = content[:insert_pos] + ''.join(getters_setters) + '\n' + content[insert_pos:]
    new_content = re.sub(r'\n\s*\n\s*\n+', '\n\n', new_content)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    return True

entity_dir = 'entity'
fixed_count = 0

for filename in sorted(os.listdir(entity_dir)):
    if filename.endswith('.java'):
        filepath = os.path.join(entity_dir, filename)
        try:
            if fix_getters_setters(filepath):
                fixed_count += 1
                print(f'Fixed: {filename}')
        except Exception as e:
            print(f'Error: {filename}: {e}')

print(f'\nTotal: {fixed_count}')
