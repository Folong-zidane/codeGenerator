package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.UmlAttribute;

/**
 * Fixed C# Controller Generator with correct types and namespaces
 */
public class CSharpControllerGeneratorFixed {
    
    public String generateController(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String controllerName = className + "Controller";
        String serviceName = "I" + className + "Service";
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using ").append(netNamespace).append(".Models;\n");
        code.append("using ").append(netNamespace).append(".Services.Interfaces;\n");
        code.append("using ").append(netNamespace).append(".DTOs;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Controllers\n");
        code.append("{\n");
        
        // Controller class
        code.append("    [ApiController]\n");
        code.append("    [Route(\"api/[controller]\")]\n");
        code.append("    public class ").append(controllerName).append(" : ControllerBase\n");
        code.append("    {\n");
        
        // Fields
        code.append("        private readonly ").append(serviceName).append(" _service;\n");
        code.append("        private readonly ILogger<").append(controllerName).append("> _logger;\n\n");
        
        // Constructor
        code.append("        public ").append(controllerName).append("(\n");
        code.append("            ").append(serviceName).append(" service,\n");
        code.append("            ILogger<").append(controllerName).append("> logger)\n");
        code.append("        {\n");
        code.append("            _service = service;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");
        
        // GET all
        code.append("        /// <summary>\n");
        code.append("        /// Get all ").append(className.toLowerCase()).append("s\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpGet]\n");
        code.append("        public async Task<ActionResult<IEnumerable<").append(className).append(">>> GetAll()\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var entities = await _service.GetAllAsync();\n");
        code.append("                return Ok(entities);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting all ").append(className.toLowerCase()).append("s\");\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\" });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // GET by ID (using Guid)
        code.append("        /// <summary>\n");
        code.append("        /// Get ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpGet(\"{id:guid}\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> GetById(Guid id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var entity = await _service.GetByIdAsync(id);\n");
        code.append("                if (entity == null)\n");
        code.append("                {\n");
        code.append("                    return NotFound(new { error = \"").append(className).append(" not found\", id });\n");
        code.append("                }\n");
        code.append("                return Ok(entity);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting ").append(className.toLowerCase()).append(" {Id}\", id);\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\" });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // POST
        code.append("        /// <summary>\n");
        code.append("        /// Create new ").append(className.toLowerCase()).append("\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPost]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Create([FromBody] ").append(className).append("CreateDto dto)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                if (!ModelState.IsValid)\n");
        code.append("                {\n");
        code.append("                    return BadRequest(ModelState);\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                var created = await _service.CreateAsync(dto);\n");
        code.append("                return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return BadRequest(new { error = \"Validation error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error creating ").append(className.toLowerCase()).append("\");\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\" });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // PUT
        code.append("        /// <summary>\n");
        code.append("        /// Update ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPut(\"{id:guid}\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Update(Guid id, [FromBody] ").append(className).append("UpdateDto dto)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                if (!ModelState.IsValid)\n");
        code.append("                {\n");
        code.append("                    return BadRequest(ModelState);\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                var updated = await _service.UpdateAsync(id, dto);\n");
        code.append("                return Ok(updated);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return NotFound(new { error = \"Entity not found\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error updating ").append(className.toLowerCase()).append(" {Id}\", id);\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\" });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // DELETE
        code.append("        /// <summary>\n");
        code.append("        /// Delete ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpDelete(\"{id:guid}\")]\n");
        code.append("        public async Task<IActionResult> Delete(Guid id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                await _service.DeleteAsync(id);\n");
        code.append("                return NoContent();\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return NotFound(new { error = \"Entity not found\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error deleting ").append(className.toLowerCase()).append(" {Id}\", id);\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\" });\n");
        code.append("            }\n");
        code.append("        }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String convertToNetNamespace(String javaPackage) {
        if (javaPackage == null || javaPackage.isEmpty()) return "Application";
        
        String[] parts = javaPackage.split("\\.");
        if (parts.length >= 2) {
            return capitalize(parts[1]) + ".Application";
        }
        return capitalize(javaPackage.replace(".", "")) + ".Application";
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}