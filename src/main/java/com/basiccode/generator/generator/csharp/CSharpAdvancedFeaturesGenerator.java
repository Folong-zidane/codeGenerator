package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.EnhancedClass;

/**
 * CSharpAdvancedFeaturesGenerator - Generate advanced .NET features
 * Creates Background Jobs, Event handlers, Specifications, Mappers, and more
 * 
 * Phase 2 Week 3 - C# ADVANCED FEATURES
 * 
 * Generates:
 * - Background job handlers (Hangfire)
 * - Domain event handlers
 * - Specifications for complex queries (Specification pattern)
 * - AutoMapper profiles
 * - API Resources (Response DTOs)
 * - Middleware and exception handlers
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpAdvancedFeaturesGenerator {

    private final String projectName;

    public CSharpAdvancedFeaturesGenerator(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Generate background job handler (Hangfire)
     */
    public String generateBackgroundJobHandler(String entityName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using Hangfire;\n");
        code.append("using ").append(packageName).append(".Services;\n");
        code.append("using Microsoft.Extensions.Logging;\n\n");

        code.append("namespace ").append(packageName).append(".Jobs\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Background job handler for ").append(entityName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(entityName).append("BackgroundJob\n");
        code.append("    {\n");
        code.append("        private readonly I").append(entityName).append("Service _service;\n");
        code.append("        private readonly ILogger<").append(entityName).append("BackgroundJob> _logger;\n\n");

        code.append("        public ").append(entityName).append("BackgroundJob(I").append(entityName).append("Service service, ILogger<").append(entityName).append("BackgroundJob> logger)\n");
        code.append("        {\n");
        code.append("            _service = service;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");

        code.append("        [AutomaticRetry(Attempts = 3)]\n");
        code.append("        public async Task ProcessAsync(Guid id)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                _logger.LogInformation($\"Processing background job for ").append(entityName).append(" {id}\");\n");
        code.append("                // Add job processing logic\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError($\"Error processing job: {ex.Message}\");\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n\n");

        code.append("        [AutomaticRetry(Attempts = 3)]\n");
        code.append("        public async Task SendNotificationAsync(Guid id, string message)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Sending notification: {message}\");\n");
        code.append("            // Add notification logic\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate domain event handler
     */
    public String generateEventHandler(String eventName, String entityName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using MediatR;\n");
        code.append("using Microsoft.Extensions.Logging;\n\n");

        code.append("namespace ").append(packageName).append(".Events.Handlers\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Event handler for ").append(eventName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(eventName).append("Handler : INotificationHandler<").append(eventName).append(">\n");
        code.append("    {\n");
        code.append("        private readonly ILogger<").append(eventName).append("Handler> _logger;\n\n");

        code.append("        public ").append(eventName).append("Handler(ILogger<").append(eventName).append("Handler> logger)\n");
        code.append("        {\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");

        code.append("        public async Task Handle(").append(eventName).append(" notification, CancellationToken cancellationToken)\n");
        code.append("        {\n");
        code.append("            _logger.LogInformation($\"Handling ").append(eventName).append(" for ").append(entityName).append(" {").append("notification.Id}\");\n");
        code.append("            // Add event handling logic\n");
        code.append("            await Task.CompletedTask;\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate domain event
     */
    public String generateDomainEvent(String eventName, String entityName) {
        StringBuilder code = new StringBuilder();

        code.append("using MediatR;\n\n");
        code.append("namespace ").append(projectName).append(".Events\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Domain event: ").append(eventName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(eventName).append(" : INotification\n");
        code.append("    {\n");
        code.append("        public Guid Id { get; set; }\n");
        code.append("        public ").append(entityName).append("? Entity { get; set; }\n");
        code.append("        public DateTime OccurredAt { get; set; } = DateTime.UtcNow;\n");
        code.append("        public string? Description { get; set; }\n\n");

        code.append("        public ").append(eventName).append("(Guid id, ").append(entityName).append(" entity)\n");
        code.append("        {\n");
        code.append("            Id = id;\n");
        code.append("            Entity = entity;\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate Specification pattern for complex queries
     */
    public String generateSpecification(String entityName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using System.Linq.Expressions;\n\n");
        code.append("namespace ").append(packageName).append(".Specifications\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Base specification for complex queries\n");
        code.append("    /// </summary>\n");
        code.append("    public abstract class BaseSpecification<T> where T : class\n");
        code.append("    {\n");
        code.append("        public Expression<Func<T, bool>>? Criteria { get; protected set; }\n");
        code.append("        public List<Expression<Func<T, object>>> Includes { get; } = new();\n");
        code.append("        public Expression<Func<T, object>>? OrderBy { get; protected set; }\n");
        code.append("        public Expression<Func<T, object>>? OrderByDescending { get; protected set; }\n");
        code.append("        public int PageNumber { get; protected set; }\n");
        code.append("        public int PageSize { get; protected set; }\n");
        code.append("        public bool IsPagingEnabled { get; protected set; }\n\n");

        code.append("        protected virtual void AddInclude(Expression<Func<T, object>> includeExpression)\n");
        code.append("        {\n");
        code.append("            Includes.Add(includeExpression);\n");
        code.append("        }\n\n");

        code.append("        protected virtual void ApplyPaging(int pageNumber, int pageSize)\n");
        code.append("        {\n");
        code.append("            PageNumber = pageNumber;\n");
        code.append("            PageSize = pageSize;\n");
        code.append("            IsPagingEnabled = true;\n");
        code.append("        }\n");
        code.append("    }\n\n");

        code.append("    /// <summary>\n");
        code.append("    /// Specification for ").append(entityName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(entityName).append("Specification : BaseSpecification<").append(entityName).append(">\n");
        code.append("    {\n");
        code.append("        public ").append(entityName).append("Specification()\n");
        code.append("        {\n");
        code.append("            // Default criteria\n");
        code.append("            Criteria = x => !x.IsDeleted;\n");
        code.append("            // Add includes\n");
        code.append("            // AddInclude(x => x.RelatedEntity);\n");
        code.append("            // Default ordering\n");
        code.append("            OrderByDescending = x => x.CreatedAt;\n");
        code.append("        }\n\n");

        code.append("        public ").append(entityName).append("Specification(int pageNumber, int pageSize) : this()\n");
        code.append("        {\n");
        code.append("            ApplyPaging(pageNumber, pageSize);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate AutoMapper profile
     */
    public String generateMapperProfile(String entityName, String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using AutoMapper;\n");
        code.append("using ").append(packageName).append(".Models;\n");
        code.append("using ").append(packageName).append(".DTOs;\n\n");

        code.append("namespace ").append(packageName).append(".Mappings\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// AutoMapper profile for ").append(entityName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(entityName).append("MapperProfile : Profile\n");
        code.append("    {\n");
        code.append("        public ").append(entityName).append("MapperProfile()\n");
        code.append("        {\n");
        code.append("            // Entity to DTO\n");
        code.append("            CreateMap<").append(entityName).append(", ").append(entityName).append("ReadDto>()\n");
        code.append("                .ReverseMap();\n\n");

        code.append("            // Create DTO to Entity\n");
        code.append("            CreateMap<").append(entityName).append("CreateDto, ").append(entityName).append(">()\n");
        code.append("                .ForMember(dest => dest.Id, opt => opt.Ignore())\n");
        code.append("                .ForMember(dest => dest.CreatedAt, opt => opt.Ignore())\n");
        code.append("                .ForMember(dest => dest.UpdatedAt, opt => opt.Ignore());\n\n");

        code.append("            // Update DTO to Entity\n");
        code.append("            CreateMap<").append(entityName).append("UpdateDto, ").append(entityName).append(">()\n");
        code.append("                .ForMember(dest => dest.Id, opt => opt.Ignore())\n");
        code.append("                .ForMember(dest => dest.CreatedAt, opt => opt.Ignore())\n");
        code.append("                .ForMember(dest => dest.UpdatedAt, opt => opt.Ignore());\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate API Response DTO
     */
    public String generateApiResponse(String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("namespace ").append(packageName).append(".Resources\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Generic API response wrapper\n");
        code.append("    /// </summary>\n");
        code.append("    public class ApiResponse<T>\n");
        code.append("    {\n");
        code.append("        public bool Success { get; set; }\n");
        code.append("        public string? Message { get; set; }\n");
        code.append("        public T? Data { get; set; }\n");
        code.append("        public List<string>? Errors { get; set; }\n");
        code.append("        public int StatusCode { get; set; }\n");
        code.append("        public DateTime Timestamp { get; set; } = DateTime.UtcNow;\n\n");

        code.append("        public static ApiResponse<T> SuccessResponse(T data, string message = \"Success\", int statusCode = 200)\n");
        code.append("        {\n");
        code.append("            return new ApiResponse<T>\n");
        code.append("            {\n");
        code.append("                Success = true,\n");
        code.append("                Message = message,\n");
        code.append("                Data = data,\n");
        code.append("                StatusCode = statusCode\n");
        code.append("            };\n");
        code.append("        }\n\n");

        code.append("        public static ApiResponse<T> ErrorResponse(string message, List<string>? errors = null, int statusCode = 400)\n");
        code.append("        {\n");
        code.append("            return new ApiResponse<T>\n");
        code.append("            {\n");
        code.append("                Success = false,\n");
        code.append("                Message = message,\n");
        code.append("                Errors = errors ?? new List<string> { message },\n");
        code.append("                StatusCode = statusCode\n");
        code.append("            };\n");
        code.append("        }\n");
        code.append("    }\n\n");

        code.append("    /// <summary>\n");
        code.append("    /// Paginated response wrapper\n");
        code.append("    /// </summary>\n");
        code.append("    public class PaginatedResponse<T>\n");
        code.append("    {\n");
        code.append("        public List<T>? Items { get; set; }\n");
        code.append("        public int PageNumber { get; set; }\n");
        code.append("        public int PageSize { get; set; }\n");
        code.append("        public int TotalCount { get; set; }\n");
        code.append("        public int TotalPages { get; set; }\n");
        code.append("        public bool HasPrevious => PageNumber > 1;\n");
        code.append("        public bool HasNext => PageNumber < TotalPages;\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate global exception handler middleware
     */
    public String generateExceptionHandlerMiddleware(String packageName) {
        StringBuilder code = new StringBuilder();

        code.append("using ").append(packageName).append(".Resources;\n");
        code.append("using Microsoft.Extensions.Logging;\n\n");

        code.append("namespace ").append(packageName).append(".Middleware\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Global exception handling middleware\n");
        code.append("    /// </summary>\n");
        code.append("    public class ExceptionHandlerMiddleware\n");
        code.append("    {\n");
        code.append("        private readonly RequestDelegate _next;\n");
        code.append("        private readonly ILogger<ExceptionHandlerMiddleware> _logger;\n\n");

        code.append("        public ExceptionHandlerMiddleware(RequestDelegate next, ILogger<ExceptionHandlerMiddleware> logger)\n");
        code.append("        {\n");
        code.append("            _next = next;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");

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

        code.append("        private static Task HandleExceptionAsync(HttpContext context, Exception exception)\n");
        code.append("        {\n");
        code.append("            context.Response.ContentType = \"application/json\";\n");
        code.append("            context.Response.StatusCode = StatusCodes.Status500InternalServerError;\n\n");

        code.append("            var response = ApiResponse<object>.ErrorResponse(\n");
        code.append("                \"An internal server error occurred\",\n");
        code.append("                new List<string> { exception.Message },\n");
        code.append("                StatusCodes.Status500InternalServerError\n");
        code.append("            );\n\n");

        code.append("            return context.Response.WriteAsJsonAsync(response);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }
}
