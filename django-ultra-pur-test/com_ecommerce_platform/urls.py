from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework.authtoken.views import obtain_auth_token

from .views import ProductViewSet

# Router configuration
router = DefaultRouter()
router.register(r'products', ProductViewSet)

urlpatterns = [
    # Authentication
    path('auth/token/', obtain_auth_token, name='api_token_auth'),

    # API endpoints
    path('', include(router.urls)),
]
