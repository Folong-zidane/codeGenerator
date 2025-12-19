package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;
import java.util.List;

/**
 * Générateur d'URLs Django ultra-optimisé
 */
public class DjangoUrlsGenerator {
    
    public String generateUrls(List<EnhancedClass> entities, String packageName) {
        StringBuilder urls = new StringBuilder();
        
        urls.append("from django.urls import path, include\n");
        urls.append("from rest_framework.routers import DefaultRouter\n");
        urls.append("from rest_framework.authtoken.views import obtain_auth_token\n\n");
        
        // Import ViewSets
        for (EnhancedClass entity : entities) {
            String className = entity.getOriginalClass().getName();
            urls.append("from .views import ").append(className).append("ViewSet\n");
        }
        
        urls.append("\n# Router configuration\n");
        urls.append("router = DefaultRouter()\n");
        
        // Register ViewSets
        for (EnhancedClass entity : entities) {
            String className = entity.getOriginalClass().getName();
            String urlName = className.toLowerCase() + "s";
            urls.append("router.register(r'").append(urlName).append("', ").append(className).append("ViewSet)\n");
        }
        
        urls.append("\nurlpatterns = [\n");
        urls.append("    # Authentication\n");
        urls.append("    path('auth/token/', obtain_auth_token, name='api_token_auth'),\n\n");
        
        urls.append("    # API endpoints\n");
        urls.append("    path('', include(router.urls)),\n");
        urls.append("]\n");
        
        return urls.toString();
    }
    
    public String generateMainUrls(String packageName, String projectName) {
        StringBuilder urls = new StringBuilder();
        
        urls.append("from django.contrib import admin\n");
        urls.append("from django.urls import path, include\n");
        urls.append("from drf_spectacular.views import SpectacularAPIView, SpectacularSwaggerView\n");
        urls.append("from django.conf import settings\n");
        urls.append("from django.conf.urls.static import static\n\n");
        
        urls.append("urlpatterns = [\n");
        urls.append("    # Admin\n");
        urls.append("    path('admin/', admin.site.urls),\n\n");
        
        urls.append("    # API Documentation\n");
        urls.append("    path('api/schema/', SpectacularAPIView.as_view(), name='schema'),\n");
        urls.append("    path('api/docs/', SpectacularSwaggerView.as_view(url_name='schema'), name='swagger-ui'),\n\n");
        
        urls.append("    # API endpoints\n");
        urls.append("    path('api/v1/', include('").append(packageName.replace(".", "_")).append(".urls')),\n");
        urls.append("]\n\n");
        
        urls.append("# Serve media files in development\n");
        urls.append("if settings.DEBUG:\n");
        urls.append("    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)\n");
        
        return urls.toString();
    }
    
    public String getFileExtension() {
        return ".py";
    }
}