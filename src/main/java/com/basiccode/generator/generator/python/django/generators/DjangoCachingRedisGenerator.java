package com.basiccode.generator.generator.python.django.generators;

import com.basiccode.generator.model.UmlClass;
import java.util.*;

/**
 * DjangoCachingRedisGenerator - Phase 2
 * Redis caching integration for query optimization and performance
 *
 * Generates:
 * - Redis configuration settings
 * - Cache decorators for views
 * - Query result caching
 * - Cache invalidation strategies
 * - Cache utilities and helpers
 * - Performance optimization recommendations
 *
 * @version 1.0
 */
public class DjangoCachingRedisGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoCachingRedisGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates Redis cache configuration for settings.py
     */
    public String generateRedisConfiguration() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== Redis Cache Configuration =====\n");
        code.append("import os\n\n");

        code.append("# Redis Configuration\n");
        code.append("REDIS_HOST = os.getenv('REDIS_HOST', 'localhost')\n");
        code.append("REDIS_PORT = int(os.getenv('REDIS_PORT', 6379))\n");
        code.append("REDIS_DB = int(os.getenv('REDIS_DB', 0))\n");
        code.append("REDIS_PASSWORD = os.getenv('REDIS_PASSWORD', None)\n");
        code.append("REDIS_TIMEOUT = int(os.getenv('REDIS_TIMEOUT', 300))\n\n");

        code.append("CACHES = {\n");
        code.append("    'default': {\n");
        code.append("        'BACKEND': 'django_redis.cache.RedisCache',\n");
        code.append("        'LOCATION': f'redis://").append("{").append("REDIS_HOST}:")
            .append("{").append("REDIS_PORT}:").append("{").append("REDIS_DB}',\n");
        code.append("        'OPTIONS': {\n");
        code.append("            'CLIENT_CLASS': 'django_redis.client.DefaultClient',\n");
        code.append("            'CONNECTION_POOL_KWARGS': {\n");
        code.append("                'password': REDIS_PASSWORD,\n");
        code.append("                'retry_on_timeout': True,\n");
        code.append("            },\n");
        code.append("            'SOCKET_CONNECT_TIMEOUT': 5,\n");
        code.append("            'SOCKET_TIMEOUT': 5,\n");
        code.append("            'COMPRESSOR': 'django_redis.compressors.zlib.ZlibCompressor',\n");
        code.append("            'IGNORE_EXCEPTIONS': False,\n");
        code.append("            'PARSER_KWARGS': {},\n");
        code.append("        },\n");
        code.append("        'TIMEOUT': REDIS_TIMEOUT,\n");
        code.append("        'KEY_PREFIX': '").append(appName.toLowerCase()).append("',\n");
        code.append("        'VERSION': 1,\n");
        code.append("    },\n");
        code.append("    'session': {\n");
        code.append("        'BACKEND': 'django_redis.cache.RedisCache',\n");
        code.append("        'LOCATION': f'redis://").append("{").append("REDIS_HOST}:")
            .append("{").append("REDIS_PORT}:1',\n");
        code.append("        'OPTIONS': {'CLIENT_CLASS': 'django_redis.client.DefaultClient'},\n");
        code.append("        'TIMEOUT': 86400,  # 1 day\n");
        code.append("    },\n");
        code.append("}\n\n");

        code.append("SESSION_ENGINE = 'django.contrib.sessions.backends.cache'\n");
        code.append("SESSION_CACHE_ALIAS = 'session'\n");

        return code.toString();
    }

    /**
     * Generates cache decorator utilities
     */
    public String generateCacheDecorators() {
        StringBuilder code = new StringBuilder();

        code.append("from django.core.cache import cache\n");
        code.append("from django.views.decorators.cache import cache_page\n");
        code.append("from functools import wraps\n");
        code.append("import hashlib\n");
        code.append("from rest_framework.response import Response\n\n");

        // cache_api_response decorator
        code.append("def cache_api_response(timeout=300, key_prefix=None):\n");
        code.append("    \"\"\"\n");
        code.append("    Decorator to cache API response based on request parameters\n");
        code.append("    \"\"\"\n");
        code.append("    def decorator(view_func):\n");
        code.append("        @wraps(view_func)\n");
        code.append("        def wrapper(request, *args, **kwargs):\n");
        code.append("            # Generate cache key from request\n");
        code.append("            query_string = request.GET.urlencode() if request.GET else ''\n");
        code.append("            cache_key = f'{key_prefix or view_func.__name__}:{query_string}'\n");
        code.append("            cache_key = hashlib.md5(cache_key.encode()).hexdigest()\n\n");
        code.append("            # Try to get from cache\n");
        code.append("            cached_response = cache.get(cache_key)\n");
        code.append("            if cached_response is not None:\n");
        code.append("                return Response(cached_response, headers={'X-Cache': 'HIT'})\n\n");
        code.append("            # Call original function\n");
        code.append("            response = view_func(request, *args, **kwargs)\n");
        code.append("            if response.status_code == 200:\n");
        code.append("                cache.set(cache_key, response.data, timeout)\n");
        code.append("                response['X-Cache'] = 'MISS'\n");
        code.append("            return response\n");
        code.append("        return wrapper\n");
        code.append("    return decorator\n\n");

        // cache_queryset decorator
        code.append("def cache_queryset(timeout=300, key_prefix=None):\n");
        code.append("    \"\"\"\n");
        code.append("    Decorator to cache queryset results\n");
        code.append("    \"\"\"\n");
        code.append("    def decorator(get_queryset_func):\n");
        code.append("        @wraps(get_queryset_func)\n");
        code.append("        def wrapper(self, *args, **kwargs):\n");
        code.append("            cache_key = f'{key_prefix or get_queryset_func.__name__}'\n");
        code.append("            cached_result = cache.get(cache_key)\n");
        code.append("            if cached_result is not None:\n");
        code.append("                return cached_result\n\n");
        code.append("            queryset = get_queryset_func(self, *args, **kwargs)\n");
        code.append("            # Cache the list representation\n");
        code.append("            cache.set(cache_key, list(queryset), timeout)\n");
        code.append("            return queryset\n");
        code.append("        return wrapper\n");
        code.append("    return decorator\n\n");

        // throttle_and_cache decorator
        code.append("def throttle_and_cache(timeout=300):\n");
        code.append("    \"\"\"\n");
        code.append("    Combine throttling with caching for optimal performance\n");
        code.append("    \"\"\"\n");
        code.append("    def decorator(view_func):\n");
        code.append("        @wraps(view_func)\n");
        code.append("        def wrapper(request, *args, **kwargs):\n");
        code.append("            user_id = request.user.id if request.user.is_authenticated else 'anonymous'\n");
        code.append("            cache_key = f'throttle:{user_id}:{view_func.__name__}'\n");
        code.append("            \n");
        code.append("            request_count = cache.get(cache_key, 0)\n");
        code.append("            if request_count >= 100:  # 100 requests per period\n");
        code.append("                return Response({'error': 'Rate limit exceeded'}, status=429)\n");
        code.append("            \n");
        code.append("            cache.set(cache_key, request_count + 1, timeout)\n");
        code.append("            return view_func(request, *args, **kwargs)\n");
        code.append("        return wrapper\n");
        code.append("    return decorator\n");

        return code.toString();
    }

    /**
     * Generates cache utilities for common operations
     */
    public String generateCacheUtilities(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("from django.core.cache import cache\n");
        code.append("from typing import List, Optional\n");
        code.append("import json\n\n");

        code.append("class ").append(modelName).append("CacheManager:\n");
        code.append("    \"\"\"\n");
        code.append("    Cache management utilities for ").append(modelName).append(" model\n");
        code.append("    \"\"\"\n\n");

        code.append("    CACHE_TIMEOUT = 300  # 5 minutes\n");
        code.append("    PREFIX = '").append(modelName.toLowerCase()).append("'\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_cache_key(cls, key_type, *args):\n");
        code.append("        \"\"\"Generate cache key with prefix and arguments\"\"\"\n");
        code.append("        key_parts = [cls.PREFIX, key_type] + list(map(str, args))\n");
        code.append("        return ':'.join(key_parts)\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_by_id(cls, obj_id: int):\n");
        code.append("        \"\"\"Get single object with caching\"\"\"\n");
        code.append("        cache_key = cls.get_cache_key('by_id', obj_id)\n");
        code.append("        obj = cache.get(cache_key)\n");
        code.append("        if obj is None:\n");
        code.append("            from .models import ").append(modelName).append("\n");
        code.append("            obj = ").append(modelName).append(".objects.get(pk=obj_id)\n");
        code.append("            cache.set(cache_key, obj, cls.CACHE_TIMEOUT)\n");
        code.append("        return obj\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_all(cls):\n");
        code.append("        \"\"\"Get all objects with caching\"\"\"\n");
        code.append("        cache_key = cls.get_cache_key('all')\n");
        code.append("        objects = cache.get(cache_key)\n");
        code.append("        if objects is None:\n");
        code.append("            from .models import ").append(modelName).append("\n");
        code.append("            objects = list(").append(modelName).append(".objects.all())\n");
        code.append("            cache.set(cache_key, objects, cls.CACHE_TIMEOUT)\n");
        code.append("        return objects\n\n");

        code.append("    @classmethod\n");
        code.append("    def invalidate(cls, obj_id: Optional[int] = None):\n");
        code.append("        \"\"\"Invalidate cache for object or all objects\"\"\"\n");
        code.append("        if obj_id:\n");
        code.append("            cache_key = cls.get_cache_key('by_id', obj_id)\n");
        code.append("            cache.delete(cache_key)\n");
        code.append("        else:\n");
        code.append("            # Invalidate all related caches\n");
        code.append("            cache_key = cls.get_cache_key('all')\n");
        code.append("            cache.delete(cache_key)\n\n");

        code.append("    @classmethod\n");
        code.append("    def filter_cached(cls, **filters):\n");
        code.append("        \"\"\"Get filtered results with caching\"\"\"\n");
        code.append("        cache_key = cls.get_cache_key('filter', json.dumps(filters, sort_keys=True))\n");
        code.append("        results = cache.get(cache_key)\n");
        code.append("        if results is None:\n");
        code.append("            from .models import ").append(modelName).append("\n");
        code.append("            results = list(").append(modelName).append(".objects.filter(**filters))\n");
        code.append("            cache.set(cache_key, results, cls.CACHE_TIMEOUT)\n");
        code.append("        return results\n");

        return code.toString();
    }

    /**
     * Generates cache invalidation strategy
     */
    public String generateCacheInvalidationSignals() {
        StringBuilder code = new StringBuilder();

        code.append("from django.db.models.signals import post_save, post_delete\n");
        code.append("from django.dispatch import receiver\n");
        code.append("from django.core.cache import cache\n\n");

        code.append("# Cache invalidation for model changes\n\n");

        code.append("@receiver(post_save)\n");
        code.append("def invalidate_cache_on_save(sender, instance, created, **kwargs):\n");
        code.append("    \"\"\"\n");
        code.append("    Invalidate cache when model instance is saved\n");
        code.append("    \"\"\"\n");
        code.append("    model_name = sender.__name__.lower()\n");
        code.append("    # Delete instance cache\n");
        code.append("    cache.delete(f'{model_name}:by_id:{instance.id}')\n");
        code.append("    # Delete list cache\n");
        code.append("    cache.delete(f'{model_name}:all')\n");
        code.append("    # Delete filter caches (pattern matching)\n");
        code.append("    cache.delete_pattern(f'{model_name}:filter:*')\n");
        code.append("    # Delete search cache\n");
        code.append("    cache.delete_pattern(f'{model_name}:search:*')\n\n");

        code.append("@receiver(post_delete)\n");
        code.append("def invalidate_cache_on_delete(sender, instance, **kwargs):\n");
        code.append("    \"\"\"\n");
        code.append("    Invalidate cache when model instance is deleted\n");
        code.append("    \"\"\"\n");
        code.append("    model_name = sender.__name__.lower()\n");
        code.append("    # Delete instance cache\n");
        code.append("    cache.delete(f'{model_name}:by_id:{instance.id}')\n");
        code.append("    # Delete list cache\n");
        code.append("    cache.delete(f'{model_name}:all')\n");
        code.append("    # Delete filter caches\n");
        code.append("    cache.delete_pattern(f'{model_name}:filter:*')\n");

        return code.toString();
    }

    /**
     * Generates cache performance monitoring
     */
    public String generateCacheMonitoring() {
        StringBuilder code = new StringBuilder();

        code.append("from django.core.cache import cache\n");
        code.append("from django.utils.decorators import method_decorator\n");
        code.append("from django.views.decorators.cache import cache_page\n");
        code.append("import time\n\n");

        code.append("class CacheMetrics:\n");
        code.append("    \"\"\"\n");
        code.append("    Monitor cache performance and hits/misses\n");
        code.append("    \"\"\"\n");
        code.append("    stats = {'hits': 0, 'misses': 0, 'sets': 0, 'deletes': 0}\n\n");

        code.append("    @classmethod\n");
        code.append("    def record_hit(cls):\n");
        code.append("        cls.stats['hits'] += 1\n\n");

        code.append("    @classmethod\n");
        code.append("    def record_miss(cls):\n");
        code.append("        cls.stats['misses'] += 1\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_hit_rate(cls):\n");
        code.append("        total = cls.stats['hits'] + cls.stats['misses']\n");
        code.append("        return (cls.stats['hits'] / total * 100) if total > 0 else 0\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_stats(cls):\n");
        code.append("        return {\n");
        code.append("            'hits': cls.stats['hits'],\n");
        code.append("            'misses': cls.stats['misses'],\n");
        code.append("            'hit_rate': f\"{cls.get_hit_rate():.2f}%\",\n");
        code.append("            'sets': cls.stats['sets'],\n");
        code.append("            'deletes': cls.stats['deletes'],\n");
        code.append("        }\n");

        return code.toString();
    }

    /**
     * Generates caching best practices guide
     */
    public String generateCachingGuide() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("Redis Caching Best Practices Guide\n");
        for(int i = 0; i < 60; i++) code.append("=");
        code.append("\n\n");

        code.append("1. INSTALLATION\n");
        code.append("   pip install django-redis redis\n\n");

        code.append("2. CONFIGURATION\n");
        code.append("   - Add 'django_redis' to INSTALLED_APPS\n");
        code.append("   - Configure Redis connection in settings.py\n");
        code.append("   - Set cache timeout appropriately\n\n");

        code.append("3. CACHE STRATEGIES\n");
        code.append("   A) Cache-Aside Pattern:\n");
        code.append("      - Try cache first\n");
        code.append("      - If miss, fetch from DB\n");
        code.append("      - Store in cache\n");
        code.append("   \n");
        code.append("   B) Write-Through Pattern:\n");
        code.append("      - Update cache immediately on write\n");
        code.append("      - Good for frequently read data\n");
        code.append("   \n");
        code.append("   C) Write-Behind Pattern:\n");
        code.append("      - Async cache updates\n");
        code.append("      - Better for high write throughput\n\n");

        code.append("4. COMMON OPERATIONS\n");
        code.append("   cache.get(key)              # Retrieve from cache\n");
        code.append("   cache.set(key, value, timeout)  # Store in cache\n");
        code.append("   cache.delete(key)           # Remove from cache\n");
        code.append("   cache.clear()               # Clear entire cache\n\n");

        code.append("5. CACHE INVALIDATION\n");
        code.append("   - Use signals for automatic invalidation\n");
        code.append("   - Tag-based invalidation for related items\n");
        code.append("   - Time-based expiration (TTL)\n\n");

        code.append("6. PERFORMANCE CONSIDERATIONS\n");
        code.append("   - Keep cache keys short\n");
        code.append("   - Use consistent key naming\n");
        code.append("   - Monitor hit rate (target: >80%)\n");
        code.append("   - Compress large cached objects\n");
        code.append("   - Use connection pooling\n\n");

        code.append("7. MONITORING\n");
        code.append("   - Redis CLI: redis-cli MONITOR\n");
        code.append("   - Stats endpoint: /api/cache/stats/\n");
        code.append("   - Hit rate tracking via CacheMetrics\n\n");

        code.append("8. ENVIRONMENT VARIABLES\n");
        code.append("   REDIS_HOST=localhost\n");
        code.append("   REDIS_PORT=6379\n");
        code.append("   REDIS_DB=0\n");
        code.append("   REDIS_PASSWORD=\n");
        code.append("   REDIS_TIMEOUT=300\n");
        code.append("\"\"\"\n");

        return code.toString();
    }
}
