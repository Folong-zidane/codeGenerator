package com.basiccode.generator.generator.csharp;

/**
 * C# Serilog Structured Logging Generator - Phase 3
 */
public class CSharpSerilogGenerator {
    
    public String generateSerilogConfiguration(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Serilog;\n");
        code.append("using Serilog.Events;\n");
        code.append("using Serilog.Formatting.Compact;\n");
        code.append("using Serilog.Sinks.Elasticsearch;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Configuration\n");
        code.append("{\n");
        code.append("    public static class SerilogConfiguration\n");
        code.append("    {\n");
        code.append("        public static void ConfigureSerilog(this IServiceCollection services, IConfiguration configuration, IWebHostEnvironment environment)\n");
        code.append("        {\n");
        code.append("            var loggerConfig = new LoggerConfiguration()\n");
        code.append("                .MinimumLevel.Information()\n");
        code.append("                .MinimumLevel.Override(\"Microsoft\", LogEventLevel.Warning)\n");
        code.append("                .MinimumLevel.Override(\"Microsoft.Hosting.Lifetime\", LogEventLevel.Information)\n");
        code.append("                .MinimumLevel.Override(\"System\", LogEventLevel.Warning)\n");
        code.append("                .Enrich.FromLogContext()\n");
        code.append("                .Enrich.WithProperty(\"Application\", configuration[\"ApplicationName\"] ?? \"MyApp\")\n");
        code.append("                .Enrich.WithProperty(\"Environment\", environment.EnvironmentName)\n");
        code.append("                .Enrich.WithMachineName()\n");
        code.append("                .Enrich.WithThreadId();\n\n");
        
        code.append("            // Console logging\n");
        code.append("            loggerConfig.WriteTo.Console(new CompactJsonFormatter());\n\n");
        
        code.append("            // File logging\n");
        code.append("            var logPath = configuration[\"Serilog:LogPath\"] ?? \"logs/app-.log\";\n");
        code.append("            loggerConfig.WriteTo.File(\n");
        code.append("                new CompactJsonFormatter(),\n");
        code.append("                logPath,\n");
        code.append("                rollingInterval: RollingInterval.Day,\n");
        code.append("                retainedFileCountLimit: 30,\n");
        code.append("                fileSizeLimitBytes: 100_000_000,\n");
        code.append("                rollOnFileSizeLimit: true\n");
        code.append("            );\n\n");
        
        code.append("            // Elasticsearch logging (if configured)\n");
        code.append("            var elasticsearchUrl = configuration[\"Elasticsearch:Url\"];\n");
        code.append("            if (!string.IsNullOrEmpty(elasticsearchUrl))\n");
        code.append("            {\n");
        code.append("                loggerConfig.WriteTo.Elasticsearch(new ElasticsearchSinkOptions(new Uri(elasticsearchUrl))\n");
        code.append("                {\n");
        code.append("                    IndexFormat = $\"logs-{configuration[\"ApplicationName\"]?.ToLower()}-{DateTime.UtcNow:yyyy-MM}\",\n");
        code.append("                    AutoRegisterTemplate = true,\n");
        code.append("                    AutoRegisterTemplateVersion = AutoRegisterTemplateVersion.ESv7,\n");
        code.append("                    MinimumLogEventLevel = LogEventLevel.Information\n");
        code.append("                });\n");
        code.append("            }\n\n");
        
        code.append("            // Development specific logging\n");
        code.append("            if (environment.IsDevelopment())\n");
        code.append("            {\n");
        code.append("                loggerConfig.MinimumLevel.Debug();\n");
        code.append("                loggerConfig.WriteTo.Debug();\n");
        code.append("            }\n\n");
        
        code.append("            Log.Logger = loggerConfig.CreateLogger();\n");
        code.append("            services.AddSingleton(Log.Logger);\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateLoggingMiddleware(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Serilog;\n");
        code.append("using Serilog.Context;\n");
        code.append("using System.Diagnostics;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Middleware\n");
        code.append("{\n");
        code.append("    public class RequestLoggingMiddleware\n");
        code.append("    {\n");
        code.append("        private readonly RequestDelegate _next;\n");
        code.append("        private readonly ILogger<RequestLoggingMiddleware> _logger;\n\n");
        
        code.append("        public RequestLoggingMiddleware(RequestDelegate next, ILogger<RequestLoggingMiddleware> logger)\n");
        code.append("        {\n");
        code.append("            _next = next;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");
        
        code.append("        public async Task InvokeAsync(HttpContext context)\n");
        code.append("        {\n");
        code.append("            var correlationId = Guid.NewGuid().ToString();\n");
        code.append("            var stopwatch = Stopwatch.StartNew();\n\n");
        
        code.append("            using (LogContext.PushProperty(\"CorrelationId\", correlationId))\n");
        code.append("            using (LogContext.PushProperty(\"RequestPath\", context.Request.Path))\n");
        code.append("            using (LogContext.PushProperty(\"RequestMethod\", context.Request.Method))\n");
        code.append("            using (LogContext.PushProperty(\"UserAgent\", context.Request.Headers[\"User-Agent\"].ToString()))\n");
        code.append("            using (LogContext.PushProperty(\"RemoteIpAddress\", context.Connection.RemoteIpAddress?.ToString()))\n");
        code.append("            {\n");
        code.append("                // Add correlation ID to response headers\n");
        code.append("                context.Response.Headers.Add(\"X-Correlation-ID\", correlationId);\n\n");
        
        code.append("                _logger.LogInformation(\"Request started: {Method} {Path}\", \n");
        code.append("                    context.Request.Method, context.Request.Path);\n\n");
        
        code.append("                try\n");
        code.append("                {\n");
        code.append("                    await _next(context);\n");
        code.append("                }\n");
        code.append("                catch (Exception ex)\n");
        code.append("                {\n");
        code.append("                    _logger.LogError(ex, \"Request failed: {Method} {Path}\", \n");
        code.append("                        context.Request.Method, context.Request.Path);\n");
        code.append("                    throw;\n");
        code.append("                }\n");
        code.append("                finally\n");
        code.append("                {\n");
        code.append("                    stopwatch.Stop();\n");
        code.append("                    _logger.LogInformation(\"Request completed: {Method} {Path} {StatusCode} in {ElapsedMs}ms\",\n");
        code.append("                        context.Request.Method, \n");
        code.append("                        context.Request.Path, \n");
        code.append("                        context.Response.StatusCode,\n");
        code.append("                        stopwatch.ElapsedMilliseconds);\n");
        code.append("                }\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateLoggingExtensions(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Serilog;\n");
        code.append("using Serilog.Context;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Extensions\n");
        code.append("{\n");
        code.append("    public static class LoggingExtensions\n");
        code.append("    {\n");
        code.append("        public static IDisposable BeginScope(this ILogger logger, string name, object value)\n");
        code.append("        {\n");
        code.append("            return LogContext.PushProperty(name, value);\n");
        code.append("        }\n\n");
        
        code.append("        public static IDisposable BeginUserScope(this ILogger logger, Guid userId, string username)\n");
        code.append("        {\n");
        code.append("            return LogContext.Push(\n");
        code.append("                LogContext.PushProperty(\"UserId\", userId),\n");
        code.append("                LogContext.PushProperty(\"Username\", username)\n");
        code.append("            );\n");
        code.append("        }\n\n");
        
        code.append("        public static void LogUserAction(this ILogger logger, string action, object parameters = null)\n");
        code.append("        {\n");
        code.append("            logger.LogInformation(\"User action: {Action} with parameters {Parameters}\", \n");
        code.append("                action, parameters);\n");
        code.append("        }\n\n");
        
        code.append("        public static void LogPerformance(this ILogger logger, string operation, TimeSpan duration, object context = null)\n");
        code.append("        {\n");
        code.append("            if (duration.TotalMilliseconds > 1000) // Log slow operations\n");
        code.append("            {\n");
        code.append("                logger.LogWarning(\"Slow operation: {Operation} took {DurationMs}ms. Context: {Context}\",\n");
        code.append("                    operation, duration.TotalMilliseconds, context);\n");
        code.append("            }\n");
        code.append("            else\n");
        code.append("            {\n");
        code.append("                logger.LogDebug(\"Operation: {Operation} completed in {DurationMs}ms\",\n");
        code.append("                    operation, duration.TotalMilliseconds);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public static void LogBusinessEvent(this ILogger logger, string eventName, object eventData)\n");
        code.append("        {\n");
        code.append("            logger.LogInformation(\"Business event: {EventName} with data {EventData}\",\n");
        code.append("                eventName, eventData);\n");
        code.append("        }\n\n");
        
        code.append("        public static void LogSecurityEvent(this ILogger logger, string eventType, string details, bool isSuccess = true)\n");
        code.append("        {\n");
        code.append("            if (isSuccess)\n");
        code.append("            {\n");
        code.append("                logger.LogInformation(\"Security event: {EventType} - {Details}\", eventType, details);\n");
        code.append("            }\n");
        code.append("            else\n");
        code.append("            {\n");
        code.append("                logger.LogWarning(\"Security event failed: {EventType} - {Details}\", eventType, details);\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generatePerformanceLoggingAttribute(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.AspNetCore.Mvc.Filters;\n");
        code.append("using System.Diagnostics;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Attributes\n");
        code.append("{\n");
        code.append("    [AttributeUsage(AttributeTargets.Method | AttributeTargets.Class)]\n");
        code.append("    public class LogPerformanceAttribute : Attribute, IAsyncActionFilter\n");
        code.append("    {\n");
        code.append("        private readonly int _warningThresholdMs;\n\n");
        
        code.append("        public LogPerformanceAttribute(int warningThresholdMs = 1000)\n");
        code.append("        {\n");
        code.append("            _warningThresholdMs = warningThresholdMs;\n");
        code.append("        }\n\n");
        
        code.append("        public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)\n");
        code.append("        {\n");
        code.append("            var logger = context.HttpContext.RequestServices.GetRequiredService<ILogger<LogPerformanceAttribute>>();\n");
        code.append("            var stopwatch = Stopwatch.StartNew();\n\n");
        
        code.append("            var actionName = $\"{context.Controller.GetType().Name}.{context.ActionDescriptor.DisplayName}\";\n\n");
        
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var result = await next();\n");
        code.append("                stopwatch.Stop();\n\n");
        
        code.append("                if (stopwatch.ElapsedMilliseconds > _warningThresholdMs)\n");
        code.append("                {\n");
        code.append("                    logger.LogWarning(\"Slow action execution: {ActionName} took {ElapsedMs}ms\",\n");
        code.append("                        actionName, stopwatch.ElapsedMilliseconds);\n");
        code.append("                }\n");
        code.append("                else\n");
        code.append("                {\n");
        code.append("                    logger.LogDebug(\"Action executed: {ActionName} in {ElapsedMs}ms\",\n");
        code.append("                        actionName, stopwatch.ElapsedMilliseconds);\n");
        code.append("                }\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                stopwatch.Stop();\n");
        code.append("                logger.LogError(ex, \"Action failed: {ActionName} after {ElapsedMs}ms\",\n");
        code.append("                    actionName, stopwatch.ElapsedMilliseconds);\n");
        code.append("                throw;\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateSerilogSettings() {
        return """
            {
              "Serilog": {
                "LogPath": "logs/app-.log",
                "MinimumLevel": {
                  "Default": "Information",
                  "Override": {
                    "Microsoft": "Warning",
                    "System": "Warning"
                  }
                }
              },
              "Elasticsearch": {
                "Url": "http://localhost:9200"
              },
              "ApplicationName": "MyApplication"
            }
            """;
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