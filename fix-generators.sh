#!/bin/bash

# Remove all the incorrectly implemented generators
echo "🧹 Cleaning up incorrect generator implementations..."

# Remove C# generators
rm -rf src/main/java/com/basiccode/generator/generator/csharp/

# Remove TypeScript generators  
rm -rf src/main/java/com/basiccode/generator/generator/typescript/

# Remove PHP generators
rm -rf src/main/java/com/basiccode/generator/generator/php/

# Remove Django generators
rm -rf src/main/java/com/basiccode/generator/generator/django/

echo "✅ Cleanup completed. Generators removed."
echo "📝 You can now implement them correctly following the existing patterns."