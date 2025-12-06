package com.basiccode.generator.generator.python.django.generators;

import java.util.*;

/**
 * DjangoAuthenticationJWTGenerator - Phase 2
 * JWT Authentication and Permission system for Django REST Framework
 *
 * Generates:
 * - JWT authentication configuration
 * - Permission classes (IsAuthenticated, IsAdmin, IsOwner)
 * - Token generation and refresh logic
 * - Authentication serializers
 * - User authentication views
 * - Permission inheritance hierarchy
 *
 * @version 1.0
 */
public class DjangoAuthenticationJWTGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoAuthenticationJWTGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates JWT settings configuration for settings.py
     */
    public String generateJWTSettings() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== JWT Authentication Settings =====\n");
        code.append("from datetime import timedelta\n\n");

        code.append("SIMPLE_JWT = {\n");
        code.append("    'ACCESS_TOKEN_LIFETIME': timedelta(minutes=5),\n");
        code.append("    'REFRESH_TOKEN_LIFETIME': timedelta(days=1),\n");
        code.append("    'ROTATE_REFRESH_TOKENS': True,\n");
        code.append("    'BLACKLIST_AFTER_ROTATION': True,\n");
        code.append("    'UPDATE_LAST_LOGIN': True,\n");
        code.append("    'ALGORITHM': 'HS256',\n");
        code.append("    'SIGNING_KEY': SECRET_KEY,\n\n");

        code.append("    'VERIFYING_KEY': None,\n");
        code.append("    'AUDIENCE': None,\n");
        code.append("    'ISSUER': None,\n");
        code.append("    'JTI_CLAIM': 'jti',\n");
        code.append("    'TOKEN_TYPE_CLAIM': 'token_type',\n\n");

        code.append("    'AUTH_TOKEN_CLASSES': ('rest_framework_simplejwt.tokens.AccessToken',),\n");
        code.append("    'TOKEN_USER_CLASS': 'rest_framework_simplejwt.models.TokenUser',\n\n");

        code.append("    'SLIDING_TOKEN_REFRESH_EXP_CLAIM': 'refresh_exp',\n");
        code.append("    'SLIDING_TOKEN_LIFETIME': timedelta(days=1),\n");
        code.append("    'SLIDING_TOKEN_REFRESH_LIFETIME': timedelta(days=7),\n");
        code.append("}\n\n");

        code.append("REST_FRAMEWORK = {\n");
        code.append("    'DEFAULT_AUTHENTICATION_CLASSES': [\n");
        code.append("        'rest_framework_simplejwt.authentication.JWTAuthentication',\n");
        code.append("        'rest_framework.authentication.SessionAuthentication',\n");
        code.append("    ],\n");
        code.append("    'DEFAULT_PERMISSION_CLASSES': [\n");
        code.append("        'rest_framework.permissions.IsAuthenticated',\n");
        code.append("    ],\n");
        code.append("    'DEFAULT_THROTTLE_CLASSES': [\n");
        code.append("        'rest_framework.throttling.AnonRateThrottle',\n");
        code.append("        'rest_framework.throttling.UserRateThrottle',\n");
        code.append("    ],\n");
        code.append("    'DEFAULT_THROTTLE_RATES': {\n");
        code.append("        'anon': '100/hour',\n");
        code.append("        'user': '1000/hour',\n");
        code.append("    },\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generates custom permission classes
     */
    public String generatePermissionClasses() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework import permissions\n\n");

        // IsAuthenticated
        code.append("class IsAuthenticated(permissions.IsAuthenticated):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow access only to authenticated users.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'Authentication credentials were not provided or are invalid.'\n\n");

        // IsAdmin
        code.append("class IsAdmin(permissions.BasePermission):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow access only to admin users.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'Admin access required.'\n\n");
        code.append("    def has_permission(self, request, view):\n");
        code.append("        return (\n");
        code.append("            request.user and\n");
        code.append("            request.user.is_authenticated and\n");
        code.append("            request.user.is_staff\n");
        code.append("        )\n\n");

        // IsSuperUser
        code.append("class IsSuperUser(permissions.BasePermission):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow access only to superusers.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'Superuser access required.'\n\n");
        code.append("    def has_permission(self, request, view):\n");
        code.append("        return (\n");
        code.append("            request.user and\n");
        code.append("            request.user.is_authenticated and\n");
        code.append("            request.user.is_superuser\n");
        code.append("        )\n\n");

        // IsOwnerOrReadOnly
        code.append("class IsOwnerOrReadOnly(permissions.BasePermission):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow only owners of an object to edit it.\n");
        code.append("    Read-only access for others.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'You do not have permission to perform this action.'\n\n");
        code.append("    def has_object_permission(self, request, view, obj):\n");
        code.append("        if request.method in permissions.SAFE_METHODS:\n");
        code.append("            return True\n");
        code.append("        return obj.user == request.user or request.user.is_staff\n\n");

        // IsOwner
        code.append("class IsOwner(permissions.BasePermission):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow only owners of an object to access it.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'You do not have permission to access this resource.'\n\n");
        code.append("    def has_object_permission(self, request, view, obj):\n");
        code.append("        return obj.user == request.user or request.user.is_staff\n\n");

        // IsOwnerOrAdmin
        code.append("class IsOwnerOrAdmin(permissions.BasePermission):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow owners or admin users to access.\n");
        code.append("    \"\"\"\n");
        code.append("    message = 'You do not have permission to perform this action.'\n\n");
        code.append("    def has_object_permission(self, request, view, obj):\n");
        code.append("        return (\n");
        code.append("            obj.user == request.user or\n");
        code.append("            request.user.is_staff or\n");
        code.append("            request.user.is_superuser\n");
        code.append("        )\n");

        return code.toString();
    }

    /**
     * Generates authentication serializers
     */
    public String generateAuthenticationSerializers() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework import serializers\n");
        code.append("from rest_framework_simplejwt.serializers import TokenObtainPairSerializer\n");
        code.append("from django.contrib.auth.models import User\n\n");

        // CustomTokenObtainPairSerializer
        code.append("class CustomTokenObtainPairSerializer(TokenObtainPairSerializer):\n");
        code.append("    \"\"\"\n");
        code.append("    Custom token serializer with additional user information\n");
        code.append("    \"\"\"\n");
        code.append("    @classmethod\n");
        code.append("    def get_token(cls, user):\n");
        code.append("        token = super().get_token(user)\n");
        code.append("        token['user_id'] = user.id\n");
        code.append("        token['email'] = user.email\n");
        code.append("        token['username'] = user.username\n");
        code.append("        token['is_staff'] = user.is_staff\n");
        code.append("        token['is_superuser'] = user.is_superuser\n");
        code.append("        return token\n\n");

        // UserSerializer
        code.append("class UserSerializer(serializers.ModelSerializer):\n");
        code.append("    class Meta:\n");
        code.append("        model = User\n");
        code.append("        fields = ['id', 'username', 'email', 'first_name', 'last_name', 'is_staff', 'is_superuser']\n");
        code.append("        read_only_fields = ['id', 'is_staff', 'is_superuser']\n\n");

        // UserRegistrationSerializer
        code.append("class UserRegistrationSerializer(serializers.ModelSerializer):\n");
        code.append("    password = serializers.CharField(write_only=True, min_length=8)\n");
        code.append("    password_confirm = serializers.CharField(write_only=True, min_length=8)\n\n");
        code.append("    class Meta:\n");
        code.append("        model = User\n");
        code.append("        fields = ['username', 'email', 'password', 'password_confirm', 'first_name', 'last_name']\n\n");
        code.append("    def validate(self, data):\n");
        code.append("        if data['password'] != data.pop('password_confirm'):\n");
        code.append("            raise serializers.ValidationError({'password': 'Passwords do not match.'})\n");
        code.append("        return data\n\n");
        code.append("    def create(self, validated_data):\n");
        code.append("        user = User.objects.create_user(**validated_data)\n");
        code.append("        return user\n\n");

        // UserDetailSerializer
        code.append("class UserDetailSerializer(serializers.ModelSerializer):\n");
        code.append("    class Meta:\n");
        code.append("        model = User\n");
        code.append("        fields = ['id', 'username', 'email', 'first_name', 'last_name', 'date_joined', 'last_login', 'is_staff']\n");
        code.append("        read_only_fields = ['id', 'date_joined', 'last_login']\n");

        return code.toString();
    }

    /**
     * Generates authentication views
     */
    public String generateAuthenticationViews() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework import viewsets, status\n");
        code.append("from rest_framework.decorators import action, api_view, permission_classes\n");
        code.append("from rest_framework.response import Response\n");
        code.append("from rest_framework.permissions import IsAuthenticated, AllowAny\n");
        code.append("from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView\n");
        code.append("from django.contrib.auth.models import User\n\n");

        // CustomTokenObtainPairView
        code.append("class CustomTokenObtainPairView(TokenObtainPairView):\n");
        code.append("    \"\"\"\n");
        code.append("    Override the token obtain endpoint to return additional user info\n");
        code.append("    \"\"\"\n");
        code.append("    serializer_class = CustomTokenObtainPairSerializer\n\n");

        // UserViewSet
        code.append("class UserViewSet(viewsets.ModelViewSet):\n");
        code.append("    \"\"\"\n");
        code.append("    ViewSet for user management with authentication\n");
        code.append("    \"\"\"\n");
        code.append("    queryset = User.objects.all()\n");
        code.append("    permission_classes = [IsAuthenticated]\n\n");

        code.append("    def get_serializer_class(self):\n");
        code.append("        if self.action == 'create':\n");
        code.append("            return UserRegistrationSerializer\n");
        code.append("        elif self.action == 'retrieve':\n");
        code.append("            return UserDetailSerializer\n");
        code.append("        return UserSerializer\n\n");

        code.append("    def get_permissions(self):\n");
        code.append("        if self.action == 'create':\n");
        code.append("            return [AllowAny()]\n");
        code.append("        return [IsAuthenticated()]\n\n");

        code.append("    @action(detail=False, methods=['get'], permission_classes=[IsAuthenticated])\n");
        code.append("    def me(self, request):\n");
        code.append("        serializer = UserDetailSerializer(request.user)\n");
        code.append("        return Response(serializer.data)\n\n");

        code.append("    @action(detail=False, methods=['post'], permission_classes=[IsAuthenticated])\n");
        code.append("    def change_password(self, request):\n");
        code.append("        user = request.user\n");
        code.append("        serializer = ChangePasswordSerializer(data=request.data)\n");
        code.append("        if serializer.is_valid():\n");
        code.append("            if user.check_password(serializer.data.get('old_password')):\n");
        code.append("                user.set_password(serializer.data.get('new_password'))\n");
        code.append("                user.save()\n");
        code.append("                return Response({'status': 'Password changed successfully'}, status=status.HTTP_200_OK)\n");
        code.append("            return Response({'old_password': ['Wrong password.']}, status=status.HTTP_400_BAD_REQUEST)\n");
        code.append("        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)\n\n");

        // Registration endpoint
        code.append("@api_view(['POST'])\n");
        code.append("@permission_classes([AllowAny])\n");
        code.append("def register_user(request):\n");
        code.append("    serializer = UserRegistrationSerializer(data=request.data)\n");
        code.append("    if serializer.is_valid():\n");
        code.append("        user = serializer.save()\n");
        code.append("        from rest_framework_simplejwt.tokens import RefreshToken\n");
        code.append("        refresh = RefreshToken.for_user(user)\n");
        code.append("        return Response({\n");
        code.append("            'user': UserSerializer(user).data,\n");
        code.append("            'refresh': str(refresh),\n");
        code.append("            'access': str(refresh.access_token),\n");
        code.append("        }, status=status.HTTP_201_CREATED)\n");
        code.append("    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)\n\n");

        // Logout endpoint
        code.append("@api_view(['POST'])\n");
        code.append("@permission_classes([IsAuthenticated])\n");
        code.append("def logout_user(request):\n");
        code.append("    try:\n");
        code.append("        refresh_token = request.data['refresh']\n");
        code.append("        token = RefreshToken(refresh_token)\n");
        code.append("        token.blacklist()\n");
        code.append("        return Response({'status': 'Logged out successfully'}, status=status.HTTP_200_OK)\n");
        code.append("    except Exception as e:\n");
        code.append("        return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)\n");

        return code.toString();
    }

    /**
     * Generates URL routing for authentication
     */
    public String generateAuthenticationURLs() {
        StringBuilder code = new StringBuilder();

        code.append("from django.urls import path, include\n");
        code.append("from rest_framework.routers import DefaultRouter\n");
        code.append("from rest_framework_simplejwt.views import (\n");
        code.append("    TokenObtainPairView,\n");
        code.append("    TokenRefreshView,\n");
        code.append("    TokenVerifyView,\n");
        code.append(")\n");
        code.append("from .views import CustomTokenObtainPairView, UserViewSet, register_user, logout_user\n\n");

        code.append("router = DefaultRouter()\n");
        code.append("router.register(r'users', UserViewSet, basename='user')\n\n");

        code.append("urlpatterns = [\n");
        code.append("    path('', include(router.urls)),\n");
        code.append("    path('auth/token/', CustomTokenObtainPairView.as_view(), name='token_obtain_pair'),\n");
        code.append("    path('auth/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),\n");
        code.append("    path('auth/token/verify/', TokenVerifyView.as_view(), name='token_verify'),\n");
        code.append("    path('auth/register/', register_user, name='register'),\n");
        code.append("    path('auth/logout/', logout_user, name='logout'),\n");
        code.append("]\n");

        return code.toString();
    }

    /**
     * Generates complete authentication configuration guide
     */
    public String generateAuthenticationGuide() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("JWT Authentication Configuration Guide\n");
        for (int i = 0; i < 60; i++) {
            code.append("=");
        }
        code.append("\n\n");

        code.append("1. INSTALLATION\n");
        code.append("   pip install djangorestframework-simplejwt\n\n");

        code.append("2. SETTINGS.PY CONFIGURATION\n");
        code.append("   - Add 'rest_framework_simplejwt' to INSTALLED_APPS\n");
        code.append("   - Configure SIMPLE_JWT settings\n");
        code.append("   - Configure REST_FRAMEWORK authentication\n\n");

        code.append("3. USER REGISTRATION\n");
        code.append("   POST /api/auth/register/\n");
        code.append("   {\n");
        code.append("       'username': 'user',\n");
        code.append("       'email': 'user@example.com',\n");
        code.append("       'password': 'securepass123',\n");
        code.append("       'password_confirm': 'securepass123'\n");
        code.append("   }\n\n");

        code.append("4. GET TOKENS\n");
        code.append("   POST /api/auth/token/\n");
        code.append("   {\n");
        code.append("       'username': 'user',\n");
        code.append("       'password': 'securepass123'\n");
        code.append("   }\n");
        code.append("   Response: {'access': '...', 'refresh': '...'}\n\n");

        code.append("5. REFRESH TOKEN\n");
        code.append("   POST /api/auth/token/refresh/\n");
        code.append("   {\n");
        code.append("       'refresh': 'refresh_token_here'\n");
        code.append("   }\n\n");

        code.append("6. USE ACCESS TOKEN\n");
        code.append("   GET /api/protected-endpoint/\n");
        code.append("   Authorization: Bearer <access_token>\n\n");

        code.append("7. PERMISSION CLASSES\n");
        code.append("   - IsAuthenticated: Requires authentication\n");
        code.append("   - IsAdmin: Requires staff user\n");
        code.append("   - IsSuperUser: Requires superuser\n");
        code.append("   - IsOwner: Only owner can access\n");
        code.append("   - IsOwnerOrReadOnly: Owner edit, others read\n\n");

        code.append("8. LOGOUT\n");
        code.append("   POST /api/auth/logout/\n");
        code.append("   {\n");
        code.append("       'refresh': 'refresh_token_here'\n");
        code.append("   }\n");
        code.append("\"\"\"\n");

        return code.toString();
    }

    /**
     * Generates ChangePasswordSerializer
     */
    public String generateChangePasswordSerializer() {
        StringBuilder code = new StringBuilder();

        code.append("class ChangePasswordSerializer(serializers.Serializer):\n");
        code.append("    old_password = serializers.CharField(required=True, write_only=True)\n");
        code.append("    new_password = serializers.CharField(required=True, write_only=True, min_length=8)\n");
        code.append("    new_password_confirm = serializers.CharField(required=True, write_only=True, min_length=8)\n\n");

        code.append("    def validate(self, data):\n");
        code.append("        if data.get('new_password') != data.get('new_password_confirm'):\n");
        code.append("            raise serializers.ValidationError({'new_password': 'Passwords do not match.'})\n");
        code.append("        if data.get('old_password') == data.get('new_password'):\n");
        code.append("            raise serializers.ValidationError({'new_password': 'New password must be different from old password.'})\n");
        code.append("        return data\n");

        return code.toString();
    }
}
