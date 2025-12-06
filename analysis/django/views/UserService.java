from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from .models import User
from .serializers import UserSerializer
from .services import UserService

from .enums import UserStatus

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    service = UserService()

    def create(self, request):
        try:
            instance = self.service.create_user(request.data)
            serializer = self.get_serializer(instance)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        except ValueError as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

    def update(self, request, pk=None):
        try:
            instance = self.service.update_user(pk, request.data)
            serializer = self.get_serializer(instance)
            return Response(serializer.data)
        except ValueError as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def suspend(self, request, pk=None):
        try:
            instance = self.service.suspend_user(pk)
            serializer = self.get_serializer(instance)
            return Response(serializer.data)
        except ValueError as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def activate(self, request, pk=None):
        try:
            instance = self.service.activate_user(pk)
            serializer = self.get_serializer(instance)
            return Response(serializer.data)
        except ValueError as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)



from django.core.exceptions import ValidationError
from django.shortcuts import get_object_or_404
from .models import User
from .serializers import UserSerializer

from .enums import UserStatus

class UserService:

    def create_user(self, data):
        serializer = UserSerializer(data=data)
        if serializer.is_valid():
            self._validate_user_data(serializer.validated_data)
            return serializer.save()
        else:
            raise ValueError(serializer.errors)

    def update_user(self, pk, data):
        instance = get_object_or_404(User, pk=pk)
        serializer = UserSerializer(instance, data=data, partial=True)
        if serializer.is_valid():
            self._validate_user_data(serializer.validated_data)
            return serializer.save()
        else:
            raise ValueError(serializer.errors)

    def suspend_user(self, pk):
        instance = get_object_or_404(User, pk=pk)
        if instance.status == UserStatus.SUSPENDED:
            raise ValueError('User is already suspended')
        instance.status = UserStatus.SUSPENDED
        instance.save()
        return instance

    def activate_user(self, pk):
        instance = get_object_or_404(User, pk=pk)
        if instance.status == UserStatus.ACTIVE:
            raise ValueError('User is already active')
        instance.status = UserStatus.ACTIVE
        instance.save()
        return instance

    def _validate_user_data(self, data):
        pass
