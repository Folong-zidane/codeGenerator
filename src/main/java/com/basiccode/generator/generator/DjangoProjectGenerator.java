package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DjangoProjectGenerator {
    
    public void generateCompleteProject(List<ClassModel> classes, String packageName, Path outputDir) throws IOException {
        createProjectStructure(outputDir);
        generateRequirements(outputDir);
        generateSettings(outputDir, packageName);
        generateUrls(outputDir, classes);
        generateManagePy(outputDir, packageName);
        generateModels(classes, outputDir);
        generateViews(classes, outputDir);
        generateSerializers(classes, outputDir);
        generateAppUrls(classes, outputDir);
    }
    
    private void createProjectStructure(Path outputDir) throws IOException {
        // Structure Django standard
        Files.createDirectories(outputDir.resolve("myproject"));
        Files.createDirectories(outputDir.resolve("apps"));
        Files.createDirectories(outputDir.resolve("apps/api"));
        Files.createDirectories(outputDir.resolve("apps/api/migrations"));
        Files.createDirectories(outputDir.resolve("apps/users"));
        Files.createDirectories(outputDir.resolve("apps/users/migrations"));
        Files.createDirectories(outputDir.resolve("config"));
        Files.createDirectories(outputDir.resolve("static"));
        Files.createDirectories(outputDir.resolve("media"));
        Files.createDirectories(outputDir.resolve("templates"));
    }
    
    private void generateRequirements(Path outputDir) throws IOException {
        String requirements = """
            Django==4.2.7
            djangorestframework==3.14.0
            django-cors-headers==4.3.1
            drf-spectacular==0.26.5
            django-filter==23.3
            Pillow==10.0.1
            python-decouple==3.8
            """;
        Files.writeString(outputDir.resolve("requirements.txt"), requirements);
        
        // Générer .env exemple
        String envExample = """
            DEBUG=True
            SECRET_KEY=your-secret-key-here
            DATABASE_URL=sqlite:///db.sqlite3
            """;
        Files.writeString(outputDir.resolve(".env.example"), envExample);
        
        // Générer README.md
        String readme = """
            # Generated Django API
            
            ## Installation
            
            1. Create virtual environment:
            ```bash
            python -m venv venv
            source venv/bin/activate  # On Windows: venv\\Scripts\\activate
            ```
            
            2. Install dependencies:
            ```bash
            pip install -r requirements.txt
            ```
            
            3. Run migrations:
            ```bash
            python manage.py makemigrations
            python manage.py migrate
            ```
            
            4. Create superuser:
            ```bash
            python manage.py createsuperuser
            ```
            
            5. Run server:
            ```bash
            python manage.py runserver
            ```
            
            ## API Documentation
            
            - Swagger UI: http://localhost:8000/api/documentation/
            - Admin Panel: http://localhost:8000/admin/
            - API Base: http://localhost:8000/api/v1/
            
            ## Authentication
            
            - Register: POST /api/auth/register/
            - Login: POST /api/auth/login/
            - Logout: POST /api/auth/logout/
            - Profile: GET /api/auth/profile/
            """;
        Files.writeString(outputDir.resolve("README.md"), readme);
    }
    
    private void generateSettings(Path outputDir, String packageName) throws IOException {
        String settings = String.format("""
            import os
            from pathlib import Path
            
            BASE_DIR = Path(__file__).resolve().parent.parent
            
            SECRET_KEY = 'django-insecure-generated-key-change-in-production'
            DEBUG = True
            ALLOWED_HOSTS = ['*']
            
            INSTALLED_APPS = [
                'django.contrib.admin',
                'django.contrib.auth',
                'django.contrib.contenttypes',
                'django.contrib.sessions',
                'django.contrib.messages',
                'django.contrib.staticfiles',
                'rest_framework',
                'rest_framework.authtoken',
                'corsheaders',
                'drf_spectacular',
                'apps.api',
                'apps.users',
            ]
            
            MIDDLEWARE = [
                'corsheaders.middleware.CorsMiddleware',
                'django.middleware.security.SecurityMiddleware',
                'django.contrib.sessions.middleware.SessionMiddleware',
                'django.middleware.common.CommonMiddleware',
                'django.middleware.csrf.CsrfViewMiddleware',
                'django.contrib.auth.middleware.AuthenticationMiddleware',
                'django.contrib.messages.middleware.MessageMiddleware',
                'django.middleware.clickjacking.XFrameOptionsMiddleware',
            ]
            
            ROOT_URLCONF = 'myproject.urls'
            WSGI_APPLICATION = 'myproject.wsgi.application'
            
            DATABASES = {
                'default': {
                    'ENGINE': 'django.db.backends.sqlite3',
                    'NAME': BASE_DIR / 'db.sqlite3',
                }
            }
            
            LANGUAGE_CODE = 'en-us'
            TIME_ZONE = 'UTC'
            USE_I18N = True
            USE_TZ = True
            
            STATIC_URL = '/static/'
            STATIC_ROOT = BASE_DIR / 'staticfiles'
            STATICFILES_DIRS = [BASE_DIR / 'static']
            
            MEDIA_URL = '/media/'
            MEDIA_ROOT = BASE_DIR / 'media'
            
            DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'
            
            REST_FRAMEWORK = {
                'DEFAULT_SCHEMA_CLASS': 'drf_spectacular.openapi.AutoSchema',
                'DEFAULT_AUTHENTICATION_CLASSES': [
                    'rest_framework.authentication.TokenAuthentication',
                    'rest_framework.authentication.SessionAuthentication',
                ],
                'DEFAULT_PERMISSION_CLASSES': [
                    'rest_framework.permissions.IsAuthenticatedOrReadOnly',
                ],
                'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
                'PAGE_SIZE': 20
            }
            
            SPECTACULAR_SETTINGS = {
                'TITLE': 'Generated Django API',
                'DESCRIPTION': 'API generated from UML diagram',
                'VERSION': '1.0.0',
                'SERVE_INCLUDE_SCHEMA': False,
            }
            
            CORS_ALLOW_ALL_ORIGINS = True
            CORS_ALLOW_CREDENTIALS = True
            """);
        Files.writeString(outputDir.resolve("myproject/settings.py"), settings);
        Files.writeString(outputDir.resolve("myproject/__init__.py"), "");
    }
    
    private void generateUrls(Path outputDir, List<ClassModel> classes) throws IOException {
        String mainUrls = """
            from django.contrib import admin
            from django.urls import path, include
            from django.conf import settings
            from django.conf.urls.static import static
            from drf_spectacular.views import SpectacularAPIView, SpectacularSwaggerView
            
            urlpatterns = [
                path('admin/', admin.site.urls),
                path('api/v1/', include('apps.api.urls')),
                path('api/auth/', include('apps.users.urls')),
                path('api/schema/', SpectacularAPIView.as_view(), name='schema'),
                path('api/docs/', SpectacularSwaggerView.as_view(url_name='schema'), name='swagger-ui'),
            ]
            
            if settings.DEBUG:
                urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
                urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
            """;
        Files.writeString(outputDir.resolve("myproject/urls.py"), mainUrls);
        
        // Générer wsgi.py
        String wsgi = """
            import os
            from django.core.wsgi import get_wsgi_application
            
            os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'myproject.settings')
            application = get_wsgi_application()
            """;
        Files.writeString(outputDir.resolve("myproject/wsgi.py"), wsgi);
    }
    
    private void generateManagePy(Path outputDir, String packageName) throws IOException {
        String managePy = """
            #!/usr/bin/env python
            import os
            import sys
            
            if __name__ == '__main__':
                os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'myproject.settings')
                try:
                    from django.core.management import execute_from_command_line
                except ImportError as exc:
                    raise ImportError(
                        "Couldn't import Django. Are you sure it's installed and "
                        "available on your PYTHONPATH environment variable? Did you "
                        "forget to activate a virtual environment?"
                    ) from exc
                execute_from_command_line(sys.argv)
            """;
        Files.writeString(outputDir.resolve("manage.py"), managePy);
    }
    
    private void generateModels(List<ClassModel> classes, Path outputDir) throws IOException {
        // Générer models pour l'app api
        StringBuilder apiModels = new StringBuilder();
        apiModels.append("from django.db import models\n");
        apiModels.append("from django.contrib.auth.models import User\n");
        apiModels.append("import uuid\n\n");
        
        apiModels.append("class BaseModel(models.Model):\n");
        apiModels.append("    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)\n");
        apiModels.append("    created_at = models.DateTimeField(auto_now_add=True)\n");
        apiModels.append("    updated_at = models.DateTimeField(auto_now=True)\n");
        apiModels.append("    created_by = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='%(class)s_created')\n");
        apiModels.append("    updated_by = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='%(class)s_updated')\n\n");
        apiModels.append("    class Meta:\n");
        apiModels.append("        abstract = True\n\n");
        
        for (ClassModel cls : classes) {
            apiModels.append("class ").append(cls.getName()).append("(BaseModel):\n");
            
            for (Field field : cls.getFields()) {
                apiModels.append("    ").append(field.getName())
                         .append(" = ").append(getDjangoFieldType(field.getType())).append("\n");
            }
            
            apiModels.append("\n    class Meta:\n");
            apiModels.append("        db_table = '").append(toSnakeCase(cls.getName())).append("'\n");
            apiModels.append("        verbose_name = '").append(cls.getName()).append("'\n");
            apiModels.append("        verbose_name_plural = '").append(cls.getName()).append("s'\n");
            apiModels.append("        ordering = ['-created_at']\n\n");
            apiModels.append("    def __str__(self):\n");
            apiModels.append("        return f'").append(cls.getName()).append(" {self.id}'\n\n");
        }
        
        Files.writeString(outputDir.resolve("apps/api/models.py"), apiModels.toString());
        Files.writeString(outputDir.resolve("apps/api/__init__.py"), "");
        Files.writeString(outputDir.resolve("apps/__init__.py"), "");
        
        // Générer apps.py pour api
        String apiApps = """
            from django.apps import AppConfig
            
            class ApiConfig(AppConfig):
                default_auto_field = 'django.db.models.BigAutoField'
                name = 'apps.api'
                verbose_name = 'API'
            """;
        Files.writeString(outputDir.resolve("apps/api/apps.py"), apiApps);
        
        // Générer app users
        generateUsersApp(outputDir);
    }
    
    private void generateViews(List<ClassModel> classes, Path outputDir) throws IOException {
        StringBuilder views = new StringBuilder();
        views.append("from rest_framework import viewsets, status, filters\n");
        views.append("from rest_framework.decorators import action\n");
        views.append("from rest_framework.response import Response\n");
        views.append("from rest_framework.permissions import IsAuthenticatedOrReadOnly\n");
        views.append("from django_filters.rest_framework import DjangoFilterBackend\n");
        views.append("from .models import ");
        
        for (int i = 0; i < classes.size(); i++) {
            views.append(classes.get(i).getName());
            if (i < classes.size() - 1) views.append(", ");
        }
        views.append("\n");
        
        views.append("from .serializers import ");
        for (int i = 0; i < classes.size(); i++) {
            views.append(classes.get(i).getName()).append("Serializer");
            if (i < classes.size() - 1) views.append(", ");
        }
        views.append("\n\n");
        
        for (ClassModel cls : classes) {
            views.append("class ").append(cls.getName()).append("ViewSet(viewsets.ModelViewSet):\n");
            views.append("    queryset = ").append(cls.getName()).append(".objects.all()\n");
            views.append("    serializer_class = ").append(cls.getName()).append("Serializer\n");
            views.append("    permission_classes = [IsAuthenticatedOrReadOnly]\n");
            views.append("    filter_backends = [DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter]\n");
            views.append("    search_fields = ['id']\n");
            views.append("    ordering_fields = ['created_at', 'updated_at']\n");
            views.append("    ordering = ['-created_at']\n\n");
            
            views.append("    def perform_create(self, serializer):\n");
            views.append("        serializer.save(created_by=self.request.user if self.request.user.is_authenticated else None)\n\n");
            
            views.append("    def perform_update(self, serializer):\n");
            views.append("        serializer.save(updated_by=self.request.user if self.request.user.is_authenticated else None)\n\n");
            
            views.append("    @action(detail=True, methods=['post'])\n");
            views.append("    def toggle_status(self, request, pk=None):\n");
            views.append("        obj = self.get_object()\n");
            views.append("        # Add custom toggle logic here\n");
            views.append("        return Response({'status': 'toggled'})\n\n");
        }
        
        Files.writeString(outputDir.resolve("apps/api/views.py"), views.toString());
        
        // Générer admin.py pour api
        generateApiAdmin(classes, outputDir);
    }
    
    private void generateSerializers(List<ClassModel> classes, Path outputDir) throws IOException {
        StringBuilder serializers = new StringBuilder();
        serializers.append("from rest_framework import serializers\n");
        serializers.append("from .models import ");
        
        for (int i = 0; i < classes.size(); i++) {
            serializers.append(classes.get(i).getName());
            if (i < classes.size() - 1) serializers.append(", ");
        }
        serializers.append("\n\n");
        
        for (ClassModel cls : classes) {
            // Serializer principal
            serializers.append("class ").append(cls.getName()).append("Serializer(serializers.ModelSerializer):\n");
            serializers.append("    created_by_name = serializers.CharField(source='created_by.username', read_only=True)\n");
            serializers.append("    updated_by_name = serializers.CharField(source='updated_by.username', read_only=True)\n\n");
            serializers.append("    class Meta:\n");
            serializers.append("        model = ").append(cls.getName()).append("\n");
            serializers.append("        fields = '__all__'\n");
            serializers.append("        read_only_fields = ('id', 'created_at', 'updated_at', 'created_by', 'updated_by')\n\n");
            
            // Serializer pour la création
            serializers.append("class ").append(cls.getName()).append("CreateSerializer(serializers.ModelSerializer):\n");
            serializers.append("    class Meta:\n");
            serializers.append("        model = ").append(cls.getName()).append("\n");
            serializers.append("        exclude = ('created_at', 'updated_at', 'created_by', 'updated_by')\n\n");
            
            // Serializer pour la liste (plus léger)
            serializers.append("class ").append(cls.getName()).append("ListSerializer(serializers.ModelSerializer):\n");
            serializers.append("    class Meta:\n");
            serializers.append("        model = ").append(cls.getName()).append("\n");
            serializers.append("        fields = ('id', 'created_at', 'updated_at')\n\n");
        }
        
        Files.writeString(outputDir.resolve("apps/api/serializers.py"), serializers.toString());
    }
    
    private void generateAppUrls(List<ClassModel> classes, Path outputDir) throws IOException {
        StringBuilder urls = new StringBuilder();
        urls.append("from django.urls import path, include\n");
        urls.append("from rest_framework.routers import DefaultRouter\n");
        urls.append("from .views import ");
        
        for (int i = 0; i < classes.size(); i++) {
            urls.append(classes.get(i).getName()).append("ViewSet");
            if (i < classes.size() - 1) urls.append(", ");
        }
        urls.append("\n\n");
        
        urls.append("router = DefaultRouter()\n");
        for (ClassModel cls : classes) {
            urls.append("router.register(r'").append(cls.getName().toLowerCase())
                .append("', ").append(cls.getName()).append("ViewSet)\n");
        }
        
        urls.append("\nurlpatterns = [\n");
        urls.append("    path('', include(router.urls)),\n");
        urls.append("]\n");
        
        Files.writeString(outputDir.resolve("apps/api/urls.py"), urls.toString());
    }
    
    private String getDjangoFieldType(String type) {
        return switch (type) {
            case "String" -> "models.CharField(max_length=255)";
            case "Integer", "int" -> "models.IntegerField()";
            case "Float", "float" -> "models.FloatField()";
            case "Boolean", "boolean" -> "models.BooleanField(default=False)";
            default -> "models.CharField(max_length=255)";
        };
    }
    
    private void generateUsersApp(Path outputDir) throws IOException {
        // Models pour users
        String userModels = """
            from django.contrib.auth.models import AbstractUser
            from django.db import models
            import uuid
            
            class CustomUser(AbstractUser):
                id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
                email = models.EmailField(unique=True)
                first_name = models.CharField(max_length=30)
                last_name = models.CharField(max_length=30)
                is_verified = models.BooleanField(default=False)
                created_at = models.DateTimeField(auto_now_add=True)
                updated_at = models.DateTimeField(auto_now=True)
                
                USERNAME_FIELD = 'email'
                REQUIRED_FIELDS = ['username', 'first_name', 'last_name']
                
                class Meta:
                    db_table = 'custom_user'
                    verbose_name = 'User'
                    verbose_name_plural = 'Users'
                
                def __str__(self):
                    return self.email
            """;
        Files.writeString(outputDir.resolve("apps/users/models.py"), userModels);
        
        // Serializers pour users
        String userSerializers = """
            from rest_framework import serializers
            from django.contrib.auth import authenticate
            from .models import CustomUser
            
            class UserSerializer(serializers.ModelSerializer):
                class Meta:
                    model = CustomUser
                    fields = ('id', 'username', 'email', 'first_name', 'last_name', 'is_verified', 'created_at')
                    read_only_fields = ('id', 'created_at', 'is_verified')
            
            class UserRegistrationSerializer(serializers.ModelSerializer):
                password = serializers.CharField(write_only=True, min_length=8)
                password_confirm = serializers.CharField(write_only=True)
                
                class Meta:
                    model = CustomUser
                    fields = ('username', 'email', 'first_name', 'last_name', 'password', 'password_confirm')
                
                def validate(self, attrs):
                    if attrs['password'] != attrs['password_confirm']:
                        raise serializers.ValidationError("Passwords don't match")
                    return attrs
                
                def create(self, validated_data):
                    validated_data.pop('password_confirm')
                    user = CustomUser.objects.create_user(**validated_data)
                    return user
            
            class LoginSerializer(serializers.Serializer):
                email = serializers.EmailField()
                password = serializers.CharField()
                
                def validate(self, attrs):
                    email = attrs.get('email')
                    password = attrs.get('password')
                    
                    if email and password:
                        user = authenticate(username=email, password=password)
                        if not user:
                            raise serializers.ValidationError('Invalid credentials')
                        if not user.is_active:
                            raise serializers.ValidationError('User account is disabled')
                        attrs['user'] = user
                    return attrs
            """;
        Files.writeString(outputDir.resolve("apps/users/serializers.py"), userSerializers);
        
        // Views pour users
        String userViews = """
            from rest_framework import status, generics
            from rest_framework.decorators import api_view, permission_classes
            from rest_framework.permissions import AllowAny, IsAuthenticated
            from rest_framework.response import Response
            from rest_framework.authtoken.models import Token
            from django.contrib.auth import login
            from .models import CustomUser
            from .serializers import UserSerializer, UserRegistrationSerializer, LoginSerializer
            
            class RegisterView(generics.CreateAPIView):
                queryset = CustomUser.objects.all()
                serializer_class = UserRegistrationSerializer
                permission_classes = [AllowAny]
                
                def create(self, request, *args, **kwargs):
                    serializer = self.get_serializer(data=request.data)
                    serializer.is_valid(raise_exception=True)
                    user = serializer.save()
                    token, created = Token.objects.get_or_create(user=user)
                    return Response({
                        'user': UserSerializer(user).data,
                        'token': token.key
                    }, status=status.HTTP_201_CREATED)
            
            @api_view(['POST'])
            @permission_classes([AllowAny])
            def login_view(request):
                serializer = LoginSerializer(data=request.data)
                serializer.is_valid(raise_exception=True)
                user = serializer.validated_data['user']
                token, created = Token.objects.get_or_create(user=user)
                return Response({
                    'user': UserSerializer(user).data,
                    'token': token.key
                })
            
            @api_view(['POST'])
            @permission_classes([IsAuthenticated])
            def logout_view(request):
                try:
                    request.user.auth_token.delete()
                    return Response({'message': 'Successfully logged out'})
                except:
                    return Response({'error': 'Error logging out'}, status=status.HTTP_400_BAD_REQUEST)
            
            @api_view(['GET'])
            @permission_classes([IsAuthenticated])
            def profile_view(request):
                serializer = UserSerializer(request.user)
                return Response(serializer.data)
            """;
        Files.writeString(outputDir.resolve("apps/users/views.py"), userViews);
        
        // URLs pour users
        String userUrls = """
            from django.urls import path
            from . import views
            
            urlpatterns = [
                path('register/', views.RegisterView.as_view(), name='register'),
                path('login/', views.login_view, name='login'),
                path('logout/', views.logout_view, name='logout'),
                path('profile/', views.profile_view, name='profile'),
            ]
            """;
        Files.writeString(outputDir.resolve("apps/users/urls.py"), userUrls);
        
        // Apps.py pour users
        String userApps = """
            from django.apps import AppConfig
            
            class UsersConfig(AppConfig):
                default_auto_field = 'django.db.models.BigAutoField'
                name = 'apps.users'
                verbose_name = 'Users'
            """;
        Files.writeString(outputDir.resolve("apps/users/apps.py"), userApps);
        
        // Admin pour users
        String userAdmin = """
            from django.contrib import admin
            from django.contrib.auth.admin import UserAdmin
            from .models import CustomUser
            
            @admin.register(CustomUser)
            class CustomUserAdmin(UserAdmin):
                list_display = ('email', 'username', 'first_name', 'last_name', 'is_verified', 'is_active', 'created_at')
                list_filter = ('is_verified', 'is_active', 'is_staff', 'created_at')
                search_fields = ('email', 'username', 'first_name', 'last_name')
                ordering = ('-created_at',)
                
                fieldsets = UserAdmin.fieldsets + (
                    ('Additional Info', {'fields': ('is_verified',)}),
                )
            """;
        Files.writeString(outputDir.resolve("apps/users/admin.py"), userAdmin);
        
        Files.writeString(outputDir.resolve("apps/users/__init__.py"), "");
    }
    
    private void generateApiAdmin(List<ClassModel> classes, Path outputDir) throws IOException {
        StringBuilder admin = new StringBuilder();
        admin.append("from django.contrib import admin\n");
        admin.append("from .models import ");
        
        for (int i = 0; i < classes.size(); i++) {
            admin.append(classes.get(i).getName());
            if (i < classes.size() - 1) admin.append(", ");
        }
        admin.append("\n\n");
        
        for (ClassModel cls : classes) {
            admin.append("@admin.register(").append(cls.getName()).append(")\n");
            admin.append("class ").append(cls.getName()).append("Admin(admin.ModelAdmin):\n");
            admin.append("    list_display = ('id', ");
            
            // Ajouter les premiers champs comme colonnes
            int fieldCount = 0;
            for (Field field : cls.getFields()) {
                if (fieldCount < 3) {
                    admin.append("'").append(field.getName()).append("', ");
                    fieldCount++;
                }
            }
            admin.append("'created_at', 'updated_at')\n");
            
            admin.append("    list_filter = ('created_at', 'updated_at')\n");
            admin.append("    search_fields = ('id',)\n");
            admin.append("    ordering = ('-created_at',)\n");
            admin.append("    readonly_fields = ('id', 'created_at', 'updated_at', 'created_by', 'updated_by')\n");
            admin.append("    ");
            
            admin.append("fieldsets = (\n");
            admin.append("        ('General Information', {\n");
            admin.append("            'fields': (");
            for (Field field : cls.getFields()) {
                admin.append("'").append(field.getName()).append("', ");
            }
            admin.append(")\n");
            admin.append("        }),\n");
            admin.append("        ('Metadata', {\n");
            admin.append("            'fields': ('created_at', 'updated_at', 'created_by', 'updated_by'),\n");
            admin.append("            'classes': ('collapse',)\n");
            admin.append("        }),\n");
            admin.append("    )\n\n");
            
            admin.append("    def save_model(self, request, obj, form, change):\n");
            admin.append("        if not change:\n");
            admin.append("            obj.created_by = request.user\n");
            admin.append("        obj.updated_by = request.user\n");
            admin.append("        super().save_model(request, obj, form, change)\n\n");
        }
        
        Files.writeString(outputDir.resolve("apps/api/admin.py"), admin.toString());
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}