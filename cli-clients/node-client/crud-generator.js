#!/usr/bin/env node

const { Command } = require('commander');
const axios = require('axios');
const fs = require('fs');
const path = require('path');

const program = new Command();
const API_BASE_URL = 'http://localhost:8080/api/v2/generate';

program
  .name('crud-generator')
  .description('UML-to-CRUD Generator CLI Client')
  .version('1.0.0');

program
  .command('generate')
  .description('Generate CRUD code from UML diagram')
  .requiredOption('-u, --uml <file>', 'UML diagram file (.mermaid)')
  .requiredOption('-o, --output <dir>', 'Output directory path')
  .option('-p, --package <name>', 'Base package name', 'com.example')
  .option('-f, --framework <type>', 'Target framework', 'spring-boot')
  .option('-t, --type <type>', 'Generation type', 'complete-project')
  .action(async (options) => {
    console.log(`🚀 Generating ${options.framework} project from ${options.uml}`);
    
    try {
      const umlContent = fs.readFileSync(options.uml, 'utf8');
      
      const payload = {
        umlContent,
        packageName: options.package,
        outputPath: path.resolve(options.output),
        generationType: options.type.toUpperCase().replace('-', '_'),
        framework: options.framework.toUpperCase().replace('-', '_')
      };
      
      const response = await axios.post(`${API_BASE_URL}/files`, payload);
      
      if (response.data.success) {
        console.log(`✅ ${response.data.message}`);
        console.log(`📊 Generated ${response.data.classCount} classes`);
      } else {
        console.error(`❌ Generation failed: ${response.data.message}`);
      }
      
    } catch (error) {
      console.error(`❌ Error: ${error.message}`);
    }
  });

program.parse();