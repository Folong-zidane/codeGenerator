package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.ClassModel;

/**
 * C# Unit Test Generator
 */
public class CSharpTestGenerator {
    
    public String generateServiceTests(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using Moq;\n");
        code.append("using Xunit;\n");
        code.append("using AutoMapper;\n");
        code.append("using ").append(netNamespace).append(".Models;\n");
        code.append("using ").append(netNamespace).append(".DTOs;\n");
        code.append("using ").append(netNamespace).append(".Services;\n");
        code.append("using ").append(netNamespace).append(".Repositories.Interfaces;\n");
        code.append("using ").append(netNamespace).append(".Exceptions;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Tests.Services\n");
        code.append("{\n");
        
        // Test class
        code.append("    /// <summary>\n");
        code.append("    /// Unit tests for ").append(className).append("Service\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("ServiceTests\n");
        code.append("    {\n");
        
        // Fields
        code.append("        private readonly Mock<I").append(className).append("Repository> _mockRepository;\n");
        code.append("        private readonly Mock<IMapper> _mockMapper;\n");
        code.append("        private readonly Mock<ILogger<").append(className).append("Service>> _mockLogger;\n");
        code.append("        private readonly ").append(className).append("Service _service;\n\n");
        
        // Constructor
        code.append("        public ").append(className).append("ServiceTests()\n");
        code.append("        {\n");
        code.append("            _mockRepository = new Mock<I").append(className).append("Repository>();\n");
        code.append("            _mockMapper = new Mock<IMapper>();\n");
        code.append("            _mockLogger = new Mock<ILogger<").append(className).append("Service>>();\n");
        code.append("            \n");
        code.append("            _service = new ").append(className).append("Service(\n");
        code.append("                _mockRepository.Object,\n");
        code.append("                _mockMapper.Object,\n");
        code.append("                _mockLogger.Object);\n");
        code.append("        }\n\n");
        
        // Test methods
        generateGetByIdTests(code, className);
        generateCreateTests(code, className);
        generateUpdateTests(code, className);
        generateDeleteTests(code, className);
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private void generateGetByIdTests(StringBuilder code, String className) {
        // GetByIdAsync - Success
        code.append("        [Fact]\n");
        code.append("        public async Task GetByIdAsync_WithValidId_ReturnsDto()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            var entity = new ").append(className).append(" { Id = id };\n");
        code.append("            var dto = new ").append(className).append("ReadDto { Id = id };\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync(entity);\n");
        code.append("            _mockMapper.Setup(m => m.Map<").append(className).append("ReadDto>(entity))\n");
        code.append("                .Returns(dto);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _service.GetByIdAsync(id);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.NotNull(result);\n");
        code.append("            Assert.Equal(id, result.Id);\n");
        code.append("            _mockRepository.Verify(r => r.GetByIdAsync(id), Times.Once);\n");
        code.append("        }\n\n");
        
        // GetByIdAsync - Not Found
        code.append("        [Fact]\n");
        code.append("        public async Task GetByIdAsync_WithInvalidId_ReturnsNull()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync((").append(className).append("?)null);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _service.GetByIdAsync(id);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.Null(result);\n");
        code.append("            _mockRepository.Verify(r => r.GetByIdAsync(id), Times.Once);\n");
        code.append("        }\n\n");
    }
    
    private void generateCreateTests(StringBuilder code, String className) {
        // CreateAsync - Success
        code.append("        [Fact]\n");
        code.append("        public async Task CreateAsync_WithValidDto_ReturnsCreatedDto()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var createDto = new ").append(className).append("CreateDto();\n");
        code.append("            var entity = new ").append(className).append(" { Id = Guid.NewGuid() };\n");
        code.append("            var readDto = new ").append(className).append("ReadDto { Id = entity.Id };\n");
        code.append("            \n");
        code.append("            _mockMapper.Setup(m => m.Map<").append(className).append(">(createDto))\n");
        code.append("                .Returns(entity);\n");
        code.append("            _mockRepository.Setup(r => r.CreateAsync(It.IsAny<").append(className).append(">()))\n");
        code.append("                .ReturnsAsync(entity);\n");
        code.append("            _mockMapper.Setup(m => m.Map<").append(className).append("ReadDto>(entity))\n");
        code.append("                .Returns(readDto);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _service.CreateAsync(createDto);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.NotNull(result);\n");
        code.append("            Assert.Equal(entity.Id, result.Id);\n");
        code.append("            _mockRepository.Verify(r => r.CreateAsync(It.IsAny<").append(className).append(">()), Times.Once);\n");
        code.append("        }\n\n");
    }
    
    private void generateUpdateTests(StringBuilder code, String className) {
        // UpdateAsync - Success
        code.append("        [Fact]\n");
        code.append("        public async Task UpdateAsync_WithValidDto_ReturnsUpdatedDto()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            var updateDto = new ").append(className).append("UpdateDto { Id = id };\n");
        code.append("            var existingEntity = new ").append(className).append(" { Id = id };\n");
        code.append("            var updatedEntity = new ").append(className).append(" { Id = id };\n");
        code.append("            var readDto = new ").append(className).append("ReadDto { Id = id };\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync(existingEntity);\n");
        code.append("            _mockMapper.Setup(m => m.Map(updateDto, existingEntity));\n");
        code.append("            _mockRepository.Setup(r => r.UpdateAsync(existingEntity))\n");
        code.append("                .ReturnsAsync(updatedEntity);\n");
        code.append("            _mockMapper.Setup(m => m.Map<").append(className).append("ReadDto>(updatedEntity))\n");
        code.append("                .Returns(readDto);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _service.UpdateAsync(id, updateDto);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.NotNull(result);\n");
        code.append("            Assert.Equal(id, result.Id);\n");
        code.append("            _mockRepository.Verify(r => r.UpdateAsync(existingEntity), Times.Once);\n");
        code.append("        }\n\n");
        
        // UpdateAsync - Not Found
        code.append("        [Fact]\n");
        code.append("        public async Task UpdateAsync_WithInvalidId_ThrowsEntityNotFoundException()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            var updateDto = new ").append(className).append("UpdateDto { Id = id };\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync((").append(className).append("?)null);\n");
        code.append("            \n");
        code.append("            // Act & Assert\n");
        code.append("            await Assert.ThrowsAsync<EntityNotFoundException>(\n");
        code.append("                () => _service.UpdateAsync(id, updateDto));\n");
        code.append("            \n");
        code.append("            _mockRepository.Verify(r => r.GetByIdAsync(id), Times.Once);\n");
        code.append("            _mockRepository.Verify(r => r.UpdateAsync(It.IsAny<").append(className).append(">()), Times.Never);\n");
        code.append("        }\n\n");
    }
    
    private void generateDeleteTests(StringBuilder code, String className) {
        // DeleteAsync - Success
        code.append("        [Fact]\n");
        code.append("        public async Task DeleteAsync_WithValidId_ReturnsTrue()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.ExistsAsync(id))\n");
        code.append("                .ReturnsAsync(true);\n");
        code.append("            _mockRepository.Setup(r => r.DeleteAsync(id))\n");
        code.append("                .Returns(Task.CompletedTask);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _service.DeleteAsync(id);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.True(result);\n");
        code.append("            _mockRepository.Verify(r => r.DeleteAsync(id), Times.Once);\n");
        code.append("        }\n\n");
        
        // DeleteAsync - Not Found
        code.append("        [Fact]\n");
        code.append("        public async Task DeleteAsync_WithInvalidId_ThrowsEntityNotFoundException()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            \n");
        code.append("            _mockRepository.Setup(r => r.ExistsAsync(id))\n");
        code.append("                .ReturnsAsync(false);\n");
        code.append("            \n");
        code.append("            // Act & Assert\n");
        code.append("            await Assert.ThrowsAsync<EntityNotFoundException>(\n");
        code.append("                () => _service.DeleteAsync(id));\n");
        code.append("            \n");
        code.append("            _mockRepository.Verify(r => r.ExistsAsync(id), Times.Once);\n");
        code.append("            _mockRepository.Verify(r => r.DeleteAsync(id), Times.Never);\n");
        code.append("        }\n");
    }
    
    public String generateControllerTests(ClassModel classModel, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = classModel.getName();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Threading.Tasks;\n");
        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using Moq;\n");
        code.append("using Xunit;\n");
        code.append("using ").append(netNamespace).append(".Controllers;\n");
        code.append("using ").append(netNamespace).append(".DTOs;\n");
        code.append("using ").append(netNamespace).append(".Services.Interfaces;\n");
        code.append("using ").append(netNamespace).append(".Exceptions;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Tests.Controllers\n");
        code.append("{\n");
        
        // Test class
        code.append("    /// <summary>\n");
        code.append("    /// Unit tests for ").append(className).append("Controller\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("ControllerTests\n");
        code.append("    {\n");
        
        // Fields
        code.append("        private readonly Mock<I").append(className).append("Service> _mockService;\n");
        code.append("        private readonly Mock<ILogger<").append(className).append("Controller>> _mockLogger;\n");
        code.append("        private readonly ").append(className).append("Controller _controller;\n\n");
        
        // Constructor
        code.append("        public ").append(className).append("ControllerTests()\n");
        code.append("        {\n");
        code.append("            _mockService = new Mock<I").append(className).append("Service>();\n");
        code.append("            _mockLogger = new Mock<ILogger<").append(className).append("Controller>>();\n");
        code.append("            \n");
        code.append("            _controller = new ").append(className).append("Controller(\n");
        code.append("                _mockService.Object,\n");
        code.append("                _mockLogger.Object);\n");
        code.append("        }\n\n");
        
        // GetById test
        code.append("        [Fact]\n");
        code.append("        public async Task GetById_WithValidId_ReturnsOkResult()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            var dto = new ").append(className).append("ReadDto { Id = id };\n");
        code.append("            \n");
        code.append("            _mockService.Setup(s => s.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync(dto);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _controller.GetById(id);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            var okResult = Assert.IsType<OkObjectResult>(result.Result);\n");
        code.append("            Assert.Equal(dto, okResult.Value);\n");
        code.append("        }\n\n");
        
        // GetById not found test
        code.append("        [Fact]\n");
        code.append("        public async Task GetById_WithInvalidId_ReturnsNotFound()\n");
        code.append("        {\n");
        code.append("            // Arrange\n");
        code.append("            var id = Guid.NewGuid();\n");
        code.append("            \n");
        code.append("            _mockService.Setup(s => s.GetByIdAsync(id))\n");
        code.append("                .ReturnsAsync((").append(className).append("ReadDto?)null);\n");
        code.append("            \n");
        code.append("            // Act\n");
        code.append("            var result = await _controller.GetById(id);\n");
        code.append("            \n");
        code.append("            // Assert\n");
        code.append("            Assert.IsType<NotFoundObjectResult>(result.Result);\n");
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