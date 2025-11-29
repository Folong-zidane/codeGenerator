package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

/**
 * C# Controller generator for .NET Core
 * Generates ASP.NET Core Web API controllers with proper REST endpoints
 */
public class CSharpControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Using statements
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using ").append(packageName).append(".Services;\n");
        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using System.Collections.Generic;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using System;\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("using ").append(packageName).append(".Enums;\n\n");
        }
        
        // Namespace and class declaration
        code.append("namespace ").append(packageName).append(".Controllers\n");
        code.append("{\n");
        code.append("    [ApiController]\n");
        code.append("    [Route(\"api/[controller]\")]\n");
        code.append("    public class ").append(className).append("Controller : ControllerBase\n");
        code.append("    {\n");
        code.append("        private readonly I").append(className).append("Service _service;\n\n");
        
        // Constructor with DI
        code.append("        public ").append(className).append("Controller(I").append(className).append("Service service)\n");
        code.append("        {\n");
        code.append("            _service = service;\n");
        code.append("        }\n\n");
        
        // GET all endpoint
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
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // GET by ID endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Get ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpGet(\"{id}\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> GetById(int id)\n");
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
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // POST endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Create new ").append(className.toLowerCase()).append("\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPost]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Create([FromBody] ").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                if (!ModelState.IsValid)\n");
        code.append("                {\n");
        code.append("                    return BadRequest(ModelState);\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                var created = await _service.CreateAsync(entity);\n");
        code.append("                return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return BadRequest(new { error = \"Validation error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // PUT endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Update ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPut(\"{id}\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Update(int id, [FromBody] ").append(className).append(" entity)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                if (!ModelState.IsValid)\n");
        code.append("                {\n");
        code.append("                    return BadRequest(ModelState);\n");
        code.append("                }\n");
        code.append("                \n");
        code.append("                var updated = await _service.UpdateAsync(id, entity);\n");
        code.append("                return Ok(updated);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return NotFound(new { error = \"Entity not found\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // DELETE endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Delete ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpDelete(\"{id}\")]\n");
        code.append("        public async Task<IActionResult> Delete(int id)\n");
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
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // State management endpoints if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementEndpoints(code, className);
        }
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getControllerDirectory() {
        return "Controllers";
    }
    
    private void generateStateManagementEndpoints(StringBuilder code, String className) {
        // Suspend endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Suspend ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPatch(\"{id}/suspend\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Suspend(int id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var entity = await _service.Suspend").append(className).append("Async(id);\n");
        code.append("                return Ok(entity);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return NotFound(new { error = \"Entity not found\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (InvalidOperationException ex)\n");
        code.append("            {\n");
        code.append("                return BadRequest(new { error = \"Invalid state transition\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // Activate endpoint
        code.append("        /// <summary>\n");
        code.append("        /// Activate ").append(className.toLowerCase()).append(" by ID\n");
        code.append("        /// </summary>\n");
        code.append("        [HttpPatch(\"{id}/activate\")]\n");
        code.append("        public async Task<ActionResult<").append(className).append(">> Activate(int id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var entity = await _service.Activate").append(className).append("Async(id);\n");
        code.append("                return Ok(entity);\n");
        code.append("            }\n");
        code.append("            catch (ArgumentException ex)\n");
        code.append("            {\n");
        code.append("                return NotFound(new { error = \"Entity not found\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (InvalidOperationException ex)\n");
        code.append("            {\n");
        code.append("                return BadRequest(new { error = \"Invalid state transition\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                return StatusCode(500, new { error = \"Internal server error\", message = ex.Message });\n");
        code.append("            }\n");
        code.append("        }\n\n");
    }
}