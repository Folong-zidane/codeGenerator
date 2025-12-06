package com.basiccode.generator.generator.csharp;

/**
 * C# Exception Generator for standardized error handling
 */
public class CSharpExceptionGenerator {
    
    public String generateCustomExceptions(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Exceptions\n");
        code.append("{\n");
        
        // EntityNotFoundException
        code.append("    /// <summary>\n");
        code.append("    /// Exception thrown when an entity is not found\n");
        code.append("    /// </summary>\n");
        code.append("    public class EntityNotFoundException : Exception\n");
        code.append("    {\n");
        code.append("        public EntityNotFoundException() : base(\"Entity not found\")\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public EntityNotFoundException(string message) : base(message)\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public EntityNotFoundException(string message, Exception innerException) : base(message, innerException)\n");
        code.append("        {\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // ValidationException
        code.append("    /// <summary>\n");
        code.append("    /// Exception thrown when validation fails\n");
        code.append("    /// </summary>\n");
        code.append("    public class ValidationException : Exception\n");
        code.append("    {\n");
        code.append("        public ValidationException() : base(\"Validation failed\")\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public ValidationException(string message) : base(message)\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public ValidationException(string message, Exception innerException) : base(message, innerException)\n");
        code.append("        {\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // BusinessRuleException
        code.append("    /// <summary>\n");
        code.append("    /// Exception thrown when business rules are violated\n");
        code.append("    /// </summary>\n");
        code.append("    public class BusinessRuleException : Exception\n");
        code.append("    {\n");
        code.append("        public BusinessRuleException() : base(\"Business rule violation\")\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public BusinessRuleException(string message) : base(message)\n");
        code.append("        {\n");
        code.append("        }\n\n");
        
        code.append("        public BusinessRuleException(string message, Exception innerException) : base(message, innerException)\n");
        code.append("        {\n");
        code.append("        }\n");
        code.append("    }\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateGlobalExceptionHandler(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Net;\n");
        code.append("using System.Text.Json;\n");
        code.append("using Microsoft.AspNetCore.Http;\n");
        code.append("using Microsoft.Extensions.Logging;\n");
        code.append("using ").append(netNamespace).append(".Exceptions;\n");
        code.append("using ").append(netNamespace).append(".Models.Responses;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Middleware\n");
        code.append("{\n");
        
        // Global exception handler
        code.append("    /// <summary>\n");
        code.append("    /// Global exception handling middleware\n");
        code.append("    /// </summary>\n");
        code.append("    public class GlobalExceptionHandlerMiddleware\n");
        code.append("    {\n");
        
        // Fields
        code.append("        private readonly RequestDelegate _next;\n");
        code.append("        private readonly ILogger<GlobalExceptionHandlerMiddleware> _logger;\n\n");
        
        // Constructor
        code.append("        public GlobalExceptionHandlerMiddleware(\n");
        code.append("            RequestDelegate next,\n");
        code.append("            ILogger<GlobalExceptionHandlerMiddleware> logger)\n");
        code.append("        {\n");
        code.append("            _next = next;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");
        
        // InvokeAsync method
        code.append("        public async Task InvokeAsync(HttpContext context)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                await _next(context);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"An unhandled exception occurred\");\n");
        code.append("                await HandleExceptionAsync(context, ex);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        // HandleExceptionAsync method
        code.append("        private static async Task HandleExceptionAsync(HttpContext context, Exception exception)\n");
        code.append("        {\n");
        code.append("            context.Response.ContentType = \"application/json\";\n\n");
        
        code.append("            var response = new ApiErrorResponse();\n\n");
        
        code.append("            switch (exception)\n");
        code.append("            {\n");
        code.append("                case EntityNotFoundException ex:\n");
        code.append("                    context.Response.StatusCode = (int)HttpStatusCode.NotFound;\n");
        code.append("                    response.Message = ex.Message;\n");
        code.append("                    response.StatusCode = (int)HttpStatusCode.NotFound;\n");
        code.append("                    break;\n\n");
        
        code.append("                case ValidationException ex:\n");
        code.append("                    context.Response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    response.Message = ex.Message;\n");
        code.append("                    response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    break;\n\n");
        
        code.append("                case BusinessRuleException ex:\n");
        code.append("                    context.Response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    response.Message = ex.Message;\n");
        code.append("                    response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    break;\n\n");
        
        code.append("                case ArgumentException ex:\n");
        code.append("                    context.Response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    response.Message = ex.Message;\n");
        code.append("                    response.StatusCode = (int)HttpStatusCode.BadRequest;\n");
        code.append("                    break;\n\n");
        
        code.append("                default:\n");
        code.append("                    context.Response.StatusCode = (int)HttpStatusCode.InternalServerError;\n");
        code.append("                    response.Message = \"An internal server error occurred\";\n");
        code.append("                    response.StatusCode = (int)HttpStatusCode.InternalServerError;\n");
        code.append("                    break;\n");
        code.append("            }\n\n");
        
        code.append("            var jsonResponse = JsonSerializer.Serialize(response, new JsonSerializerOptions\n");
        code.append("            {\n");
        code.append("                PropertyNamingPolicy = JsonNamingPolicy.CamelCase\n");
        code.append("            });\n\n");
        
        code.append("            await context.Response.WriteAsync(jsonResponse);\n");
        code.append("        }\n");
        
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateApiResponseModels(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        // Using statements
        code.append("using System;\n");
        code.append("using System.Collections.Generic;\n\n");
        
        // Namespace
        code.append("namespace ").append(netNamespace).append(".Models.Responses\n");
        code.append("{\n");
        
        // ApiResponse<T>
        code.append("    /// <summary>\n");
        code.append("    /// Generic API response wrapper\n");
        code.append("    /// </summary>\n");
        code.append("    public class ApiResponse<T>\n");
        code.append("    {\n");
        code.append("        public bool Success { get; set; }\n");
        code.append("        public T? Data { get; set; }\n");
        code.append("        public string? Message { get; set; }\n");
        code.append("        public int StatusCode { get; set; }\n");
        code.append("        public DateTime Timestamp { get; set; } = DateTime.UtcNow;\n\n");
        
        // Success factory method
        code.append("        public static ApiResponse<T> SuccessResponse(T data, string? message = null)\n");
        code.append("        {\n");
        code.append("            return new ApiResponse<T>\n");
        code.append("            {\n");
        code.append("                Success = true,\n");
        code.append("                Data = data,\n");
        code.append("                Message = message ?? \"Operation completed successfully\",\n");
        code.append("                StatusCode = 200\n");
        code.append("            };\n");
        code.append("        }\n\n");
        
        // Error factory method
        code.append("        public static ApiResponse<T> ErrorResponse(string message, int statusCode = 400)\n");
        code.append("        {\n");
        code.append("            return new ApiResponse<T>\n");
        code.append("            {\n");
        code.append("                Success = false,\n");
        code.append("                Data = default,\n");
        code.append("                Message = message,\n");
        code.append("                StatusCode = statusCode\n");
        code.append("            };\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // ApiErrorResponse
        code.append("    /// <summary>\n");
        code.append("    /// API error response\n");
        code.append("    /// </summary>\n");
        code.append("    public class ApiErrorResponse\n");
        code.append("    {\n");
        code.append("        public bool Success { get; set; } = false;\n");
        code.append("        public string Message { get; set; } = string.Empty;\n");
        code.append("        public int StatusCode { get; set; }\n");
        code.append("        public List<string> Errors { get; set; } = new();\n");
        code.append("        public DateTime Timestamp { get; set; } = DateTime.UtcNow;\n");
        code.append("        public string? TraceId { get; set; }\n");
        code.append("    }\n\n");
        
        // PaginatedResponse<T>
        code.append("    /// <summary>\n");
        code.append("    /// Paginated API response\n");
        code.append("    /// </summary>\n");
        code.append("    public class PaginatedResponse<T> : ApiResponse<IEnumerable<T>>\n");
        code.append("    {\n");
        code.append("        public int Page { get; set; }\n");
        code.append("        public int PageSize { get; set; }\n");
        code.append("        public int TotalCount { get; set; }\n");
        code.append("        public int TotalPages => (int)Math.Ceiling((double)TotalCount / PageSize);\n");
        code.append("        public bool HasNextPage => Page < TotalPages;\n");
        code.append("        public bool HasPreviousPage => Page > 1;\n\n");
        
        code.append("        public static PaginatedResponse<T> Create(IEnumerable<T> data, int page, int pageSize, int totalCount)\n");
        code.append("        {\n");
        code.append("            return new PaginatedResponse<T>\n");
        code.append("            {\n");
        code.append("                Success = true,\n");
        code.append("                Data = data,\n");
        code.append("                Page = page,\n");
        code.append("                PageSize = pageSize,\n");
        code.append("                TotalCount = totalCount,\n");
        code.append("                StatusCode = 200,\n");
        code.append("                Message = \"Data retrieved successfully\"\n");
        code.append("            };\n");
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