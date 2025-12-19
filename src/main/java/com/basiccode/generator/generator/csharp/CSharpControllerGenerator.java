package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLMethod;
import java.util.Map;

/**
 * Générateur de contrôleurs C# ASP.NET Core
 */
public class CSharpControllerGenerator implements IControllerGenerator {
    
    @Override
    public String getControllerDirectory() {
        return "Controllers";
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        return generateController(enhancedClass, packageName, null);
    }
    
    public String generateController(EnhancedClass enhancedClass, String packageName, Map<String, String> metadata) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder controller = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String controllerName = className + "Controller";
        String serviceName = className + "Service";
        
        // Using statements
        controller.append("using System;\n");
        controller.append("using System.Collections.Generic;\n");
        controller.append("using System.Threading.Tasks;\n");
        controller.append("using Microsoft.AspNetCore.Mvc;\n");
        controller.append("using Microsoft.Extensions.Logging;\n");
        controller.append("using ").append(packageName).append(".Entities;\n");
        controller.append("using ").append(packageName).append(".Services;\n");
        controller.append("using ").append(packageName).append(".DTOs;\n\n");
        
        // Namespace
        controller.append("namespace ").append(packageName).append(".Controllers\n{\n");
        
        // Controller attributes
        controller.append("    [ApiController]\n");
        controller.append("    [Route(\"api/[controller]\")]\n");
        controller.append("    [Produces(\"application/json\")]\n");
        
        // Controller class
        controller.append("    public class ").append(controllerName).append(" : ControllerBase\n    {\n");
        
        // Fields
        controller.append("        private readonly I").append(serviceName).append(" _service;\n");
        controller.append("        private readonly ILogger<").append(controllerName).append("> _logger;\n\n");
        
        // Constructor
        controller.append("        public ").append(controllerName).append("(\n");
        controller.append("            I").append(serviceName).append(" service,\n");
        controller.append("            ILogger<").append(controllerName).append("> logger)\n");
        controller.append("        {\n");
        controller.append("            _service = service ?? throw new ArgumentNullException(nameof(service));\n");
        controller.append("            _logger = logger ?? throw new ArgumentNullException(nameof(logger));\n");
        controller.append("        }\n\n");
        
        // CRUD endpoints
        controller.append(generateCrudEndpoints(className));
        
        // Business endpoints from UML methods
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (UMLMethod method : enhancedClass.getOriginalClass().getMethods()) {
                controller.append(generateBusinessEndpoint(method, className));
            }
        }
        
        controller.append("    }\n");
        controller.append("}\n");
        
        return controller.toString();
    }
    
    private String generateCrudEndpoints(String className) {
        StringBuilder endpoints = new StringBuilder();
        String entityVar = className.toLowerCase();
        
        // GET all
        endpoints.append("        /// <summary>\n");
        endpoints.append("        /// Get all ").append(className).append(" entities\n");
        endpoints.append("        /// </summary>\n");
        endpoints.append("        [HttpGet]\n");
        endpoints.append("        [ProducesResponseType(typeof(IEnumerable<").append(className).append(">), 200)]\n");
        endpoints.append("        public async Task<ActionResult<IEnumerable<").append(className).append(">>> GetAll()\n");
        endpoints.append("        {\n");
        endpoints.append("            try\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogInformation(\"Getting all ").append(className).append(" entities\");\n");
        endpoints.append("                var entities = await _service.GetAllAsync();\n");
        endpoints.append("                return Ok(entities);\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (Exception ex)\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogError(ex, \"Error getting all ").append(className).append(" entities\");\n");
        endpoints.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoints.append("            }\n");
        endpoints.append("        }\n\n");
        
        // GET by ID
        endpoints.append("        /// <summary>\n");
        endpoints.append("        /// Get ").append(className).append(" by ID\n");
        endpoints.append("        /// </summary>\n");
        endpoints.append("        [HttpGet(\"{id}\")]\n");
        endpoints.append("        [ProducesResponseType(typeof(").append(className).append("), 200)]\n");
        endpoints.append("        [ProducesResponseType(404)]\n");
        endpoints.append("        public async Task<ActionResult<").append(className).append(">> GetById(long id)\n");
        endpoints.append("        {\n");
        endpoints.append("            try\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogInformation(\"Getting ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                var entity = await _service.GetByIdAsync(id);\n");
        endpoints.append("                return Ok(entity);\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (KeyNotFoundException)\n");
        endpoints.append("            {\n");
        endpoints.append("                return NotFound($\"").append(className).append(" with ID {id} not found\");\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (Exception ex)\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogError(ex, \"Error getting ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoints.append("            }\n");
        endpoints.append("        }\n\n");
        
        // POST
        endpoints.append("        /// <summary>\n");
        endpoints.append("        /// Create new ").append(className).append("\n");
        endpoints.append("        /// </summary>\n");
        endpoints.append("        [HttpPost]\n");
        endpoints.append("        [ProducesResponseType(typeof(").append(className).append("), 201)]\n");
        endpoints.append("        [ProducesResponseType(400)]\n");
        endpoints.append("        public async Task<ActionResult<").append(className).append(">> Create([FromBody] ").append(className).append(" ").append(entityVar).append(")\n");
        endpoints.append("        {\n");
        endpoints.append("            try\n");
        endpoints.append("            {\n");
        endpoints.append("                if (!ModelState.IsValid)\n");
        endpoints.append("                    return BadRequest(ModelState);\n");
        endpoints.append("                \n");
        endpoints.append("                _logger.LogInformation(\"Creating new ").append(className).append("\");\n");
        endpoints.append("                var created").append(className).append(" = await _service.CreateAsync(").append(entityVar).append(");\n");
        endpoints.append("                return CreatedAtAction(nameof(GetById), new { id = created").append(className).append(".Id }, created").append(className).append(");\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (Exception ex)\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogError(ex, \"Error creating ").append(className).append("\");\n");
        endpoints.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoints.append("            }\n");
        endpoints.append("        }\n\n");
        
        // PUT
        endpoints.append("        /// <summary>\n");
        endpoints.append("        /// Update ").append(className).append("\n");
        endpoints.append("        /// </summary>\n");
        endpoints.append("        [HttpPut(\"{id}\")]\n");
        endpoints.append("        [ProducesResponseType(typeof(").append(className).append("), 200)]\n");
        endpoints.append("        [ProducesResponseType(400)]\n");
        endpoints.append("        [ProducesResponseType(404)]\n");
        endpoints.append("        public async Task<ActionResult<").append(className).append(">> Update(long id, [FromBody] ").append(className).append(" ").append(entityVar).append(")\n");
        endpoints.append("        {\n");
        endpoints.append("            try\n");
        endpoints.append("            {\n");
        endpoints.append("                if (id != ").append(entityVar).append(".Id)\n");
        endpoints.append("                    return BadRequest(\"ID mismatch\");\n");
        endpoints.append("                \n");
        endpoints.append("                if (!ModelState.IsValid)\n");
        endpoints.append("                    return BadRequest(ModelState);\n");
        endpoints.append("                \n");
        endpoints.append("                _logger.LogInformation(\"Updating ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                var updated").append(className).append(" = await _service.UpdateAsync(").append(entityVar).append(");\n");
        endpoints.append("                return Ok(updated").append(className).append(");\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (KeyNotFoundException)\n");
        endpoints.append("            {\n");
        endpoints.append("                return NotFound($\"").append(className).append(" with ID {id} not found\");\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (Exception ex)\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogError(ex, \"Error updating ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoints.append("            }\n");
        endpoints.append("        }\n\n");
        
        // DELETE
        endpoints.append("        /// <summary>\n");
        endpoints.append("        /// Delete ").append(className).append("\n");
        endpoints.append("        /// </summary>\n");
        endpoints.append("        [HttpDelete(\"{id}\")]\n");
        endpoints.append("        [ProducesResponseType(204)]\n");
        endpoints.append("        [ProducesResponseType(404)]\n");
        endpoints.append("        public async Task<IActionResult> Delete(long id)\n");
        endpoints.append("        {\n");
        endpoints.append("            try\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogInformation(\"Deleting ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                var deleted = await _service.DeleteAsync(id);\n");
        endpoints.append("                \n");
        endpoints.append("                if (!deleted)\n");
        endpoints.append("                    return NotFound($\"").append(className).append(" with ID {id} not found\");\n");
        endpoints.append("                \n");
        endpoints.append("                return NoContent();\n");
        endpoints.append("            }\n");
        endpoints.append("            catch (Exception ex)\n");
        endpoints.append("            {\n");
        endpoints.append("                _logger.LogError(ex, \"Error deleting ").append(className).append(" with ID: {Id}\", id);\n");
        endpoints.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoints.append("            }\n");
        endpoints.append("        }\n\n");
        
        return endpoints.toString();
    }
    
    private String generateBusinessEndpoint(UMLMethod method, String className) {
        StringBuilder endpoint = new StringBuilder();
        String methodName = method.getName();
        String returnType = mapToCSharpType(method.getReturnType());
        
        endpoint.append("        /// <summary>\n");
        endpoint.append("        /// Business operation: ").append(methodName).append("\n");
        endpoint.append("        /// </summary>\n");
        endpoint.append("        [HttpPost(\"").append(methodName.toLowerCase()).append("\")]\n");
        endpoint.append("        [ProducesResponseType(typeof(").append(returnType).append("), 200)]\n");
        endpoint.append("        public async Task<ActionResult<").append(returnType).append(">> ").append(methodName).append("(");
        
        // Parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) endpoint.append(", ");
                var param = method.getParameters().get(i);
                endpoint.append("[FromBody] ").append(mapToCSharpType(param.getType())).append(" ").append(param.getName());
            }
        }
        
        endpoint.append(")\n        {\n");
        endpoint.append("            try\n");
        endpoint.append("            {\n");
        endpoint.append("                _logger.LogInformation(\"Executing business operation: ").append(methodName).append("\");\n");
        endpoint.append("                var result = await _service.").append(methodName).append("Async(");
        
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i > 0) endpoint.append(", ");
                endpoint.append(method.getParameters().get(i).getName());
            }
        }
        
        endpoint.append(");\n");
        endpoint.append("                return Ok(result);\n");
        endpoint.append("            }\n");
        endpoint.append("            catch (Exception ex)\n");
        endpoint.append("            {\n");
        endpoint.append("                _logger.LogError(ex, \"Error executing ").append(methodName).append("\");\n");
        endpoint.append("                return StatusCode(500, \"Internal server error\");\n");
        endpoint.append("            }\n");
        endpoint.append("        }\n\n");
        
        return endpoint.toString();
    }
    
    private String mapToCSharpType(String umlType) {
        if (umlType == null) return "object";
        
        switch (umlType.toLowerCase()) {
            case "string": return "string";
            case "integer": case "int": return "int";
            case "long": return "long";
            case "boolean": case "bool": return "bool";
            case "double": return "double";
            case "float": return "float";
            case "decimal": case "bigdecimal": return "decimal";
            case "datetime": case "localdatetime": return "DateTime";
            case "void": return "bool";
            default: return "object";
        }
    }
}