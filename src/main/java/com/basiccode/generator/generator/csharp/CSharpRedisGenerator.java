package com.basiccode.generator.generator.csharp;

/**
 * C# Redis Caching Generator - Phase 3
 */
public class CSharpRedisGenerator {
    
    public String generateRedisConfiguration(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.Extensions.Caching.Distributed;\n");
        code.append("using Microsoft.Extensions.Caching.StackExchangeRedis;\n");
        code.append("using StackExchange.Redis;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Configuration\n");
        code.append("{\n");
        code.append("    public static class RedisConfiguration\n");
        code.append("    {\n");
        code.append("        public static void ConfigureRedis(this IServiceCollection services, IConfiguration configuration)\n");
        code.append("        {\n");
        code.append("            var redisConnectionString = configuration.GetConnectionString(\"Redis\");\n\n");
        
        code.append("            // Add Redis distributed cache\n");
        code.append("            services.AddStackExchangeRedisCache(options =>\n");
        code.append("            {\n");
        code.append("                options.Configuration = redisConnectionString;\n");
        code.append("                options.InstanceName = configuration[\"Redis:InstanceName\"] ?? \"MyApp\";\n");
        code.append("            });\n\n");
        
        code.append("            // Add Redis connection multiplexer\n");
        code.append("            services.AddSingleton<IConnectionMultiplexer>(provider =>\n");
        code.append("            {\n");
        code.append("                var configuration = ConfigurationOptions.Parse(redisConnectionString);\n");
        code.append("                configuration.AbortOnConnectFail = false;\n");
        code.append("                return ConnectionMultiplexer.Connect(configuration);\n");
        code.append("            });\n\n");
        
        code.append("            // Add cache service\n");
        code.append("            services.AddScoped<ICacheService, CacheService>();\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateCacheService(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.Extensions.Caching.Distributed;\n");
        code.append("using System.Text.Json;\n");
        code.append("using StackExchange.Redis;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Services\n");
        code.append("{\n");
        code.append("    public interface ICacheService\n");
        code.append("    {\n");
        code.append("        Task<T> GetAsync<T>(string key) where T : class;\n");
        code.append("        Task SetAsync<T>(string key, T value, TimeSpan? expiry = null) where T : class;\n");
        code.append("        Task RemoveAsync(string key);\n");
        code.append("        Task RemoveByPatternAsync(string pattern);\n");
        code.append("        Task<bool> ExistsAsync(string key);\n");
        code.append("        Task<long> IncrementAsync(string key, long value = 1);\n");
        code.append("        Task<double> IncrementAsync(string key, double value);\n");
        code.append("        Task SetHashAsync(string key, string field, string value);\n");
        code.append("        Task<string> GetHashAsync(string key, string field);\n");
        code.append("    }\n\n");
        
        code.append("    public class CacheService : ICacheService\n");
        code.append("    {\n");
        code.append("        private readonly IDistributedCache _distributedCache;\n");
        code.append("        private readonly IConnectionMultiplexer _connectionMultiplexer;\n");
        code.append("        private readonly ILogger<CacheService> _logger;\n");
        code.append("        private readonly IDatabase _database;\n\n");
        
        code.append("        public CacheService(\n");
        code.append("            IDistributedCache distributedCache,\n");
        code.append("            IConnectionMultiplexer connectionMultiplexer,\n");
        code.append("            ILogger<CacheService> logger)\n");
        code.append("        {\n");
        code.append("            _distributedCache = distributedCache;\n");
        code.append("            _connectionMultiplexer = connectionMultiplexer;\n");
        code.append("            _logger = logger;\n");
        code.append("            _database = _connectionMultiplexer.GetDatabase();\n");
        code.append("        }\n\n");
        
        code.append("        public async Task<T> GetAsync<T>(string key) where T : class\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var cachedValue = await _distributedCache.GetStringAsync(key);\n");
        code.append("                if (string.IsNullOrEmpty(cachedValue))\n");
        code.append("                {\n");
        code.append("                    return null;\n");
        code.append("                }\n\n");
        
        code.append("                var result = JsonSerializer.Deserialize<T>(cachedValue);\n");
        code.append("                _logger.LogDebug(\"Cache hit for key: {Key}\", key);\n");
        code.append("                return result;\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting cache value for key: {Key}\", key);\n");
        code.append("                return null;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task SetAsync<T>(string key, T value, TimeSpan? expiry = null) where T : class\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var serializedValue = JsonSerializer.Serialize(value);\n");
        code.append("                var options = new DistributedCacheEntryOptions();\n\n");
        
        code.append("                if (expiry.HasValue)\n");
        code.append("                {\n");
        code.append("                    options.SetAbsoluteExpiration(expiry.Value);\n");
        code.append("                }\n");
        code.append("                else\n");
        code.append("                {\n");
        code.append("                    options.SetSlidingExpiration(TimeSpan.FromMinutes(30));\n");
        code.append("                }\n\n");
        
        code.append("                await _distributedCache.SetStringAsync(key, serializedValue, options);\n");
        code.append("                _logger.LogDebug(\"Cache set for key: {Key}\", key);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error setting cache value for key: {Key}\", key);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task RemoveAsync(string key)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                await _distributedCache.RemoveAsync(key);\n");
        code.append("                _logger.LogDebug(\"Cache removed for key: {Key}\", key);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error removing cache value for key: {Key}\", key);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task RemoveByPatternAsync(string pattern)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                var server = _connectionMultiplexer.GetServer(_connectionMultiplexer.GetEndPoints().First());\n");
        code.append("                var keys = server.Keys(pattern: pattern);\n\n");
        
        code.append("                foreach (var key in keys)\n");
        code.append("                {\n");
        code.append("                    await _database.KeyDeleteAsync(key);\n");
        code.append("                }\n\n");
        
        code.append("                _logger.LogDebug(\"Cache cleared for pattern: {Pattern}\", pattern);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error removing cache by pattern: {Pattern}\", pattern);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task<bool> ExistsAsync(string key)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                return await _database.KeyExistsAsync(key);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error checking cache existence for key: {Key}\", key);\n");
        code.append("                return false;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task<long> IncrementAsync(string key, long value = 1)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                return await _database.StringIncrementAsync(key, value);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error incrementing cache value for key: {Key}\", key);\n");
        code.append("                return 0;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task<double> IncrementAsync(string key, double value)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                return await _database.StringIncrementAsync(key, value);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error incrementing cache value for key: {Key}\", key);\n");
        code.append("                return 0;\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task SetHashAsync(string key, string field, string value)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                await _database.HashSetAsync(key, field, value);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error setting hash value for key: {Key}, field: {Field}\", key, field);\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        public async Task<string> GetHashAsync(string key, string field)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                return await _database.HashGetAsync(key, field);\n");
        code.append("            }\n");
        code.append("            catch (Exception ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Error getting hash value for key: {Key}, field: {Field}\", key, field);\n");
        code.append("                return null;\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateCacheAttribute(String packageName) {
        StringBuilder code = new StringBuilder();
        String netNamespace = convertToNetNamespace(packageName);
        
        code.append("using Microsoft.AspNetCore.Mvc;\n");
        code.append("using Microsoft.AspNetCore.Mvc.Filters;\n\n");
        
        code.append("namespace ").append(netNamespace).append(".Attributes\n");
        code.append("{\n");
        code.append("    [AttributeUsage(AttributeTargets.Method)]\n");
        code.append("    public class CacheAttribute : Attribute, IAsyncActionFilter\n");
        code.append("    {\n");
        code.append("        private readonly int _durationInSeconds;\n");
        code.append("        private readonly string _keyPrefix;\n\n");
        
        code.append("        public CacheAttribute(int durationInSeconds = 300, string keyPrefix = \"\")\n");
        code.append("        {\n");
        code.append("            _durationInSeconds = durationInSeconds;\n");
        code.append("            _keyPrefix = keyPrefix;\n");
        code.append("        }\n\n");
        
        code.append("        public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)\n");
        code.append("        {\n");
        code.append("            var cacheService = context.HttpContext.RequestServices.GetRequiredService<ICacheService>();\n");
        code.append("            var cacheKey = GenerateCacheKey(context);\n\n");
        
        code.append("            var cachedResponse = await cacheService.GetAsync<object>(cacheKey);\n");
        code.append("            if (cachedResponse != null)\n");
        code.append("            {\n");
        code.append("                context.Result = new OkObjectResult(cachedResponse);\n");
        code.append("                return;\n");
        code.append("            }\n\n");
        
        code.append("            var executedContext = await next();\n\n");
        
        code.append("            if (executedContext.Result is OkObjectResult okResult)\n");
        code.append("            {\n");
        code.append("                await cacheService.SetAsync(cacheKey, okResult.Value, TimeSpan.FromSeconds(_durationInSeconds));\n");
        code.append("            }\n");
        code.append("        }\n\n");
        
        code.append("        private string GenerateCacheKey(ActionExecutingContext context)\n");
        code.append("        {\n");
        code.append("            var controller = context.RouteData.Values[\"controller\"];\n");
        code.append("            var action = context.RouteData.Values[\"action\"];\n");
        code.append("            var parameters = string.Join(\"-\", context.ActionArguments.Values.Select(v => v?.ToString() ?? \"null\"));\n\n");
        
        code.append("            return $\"{_keyPrefix}{controller}-{action}-{parameters}\";\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateRedisSettings() {
        return """
            {
              "ConnectionStrings": {
                "Redis": "localhost:6379"
              },
              "Redis": {
                "InstanceName": "MyApplication",
                "DefaultExpiration": "00:30:00"
              }
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