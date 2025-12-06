from rest_framework import serializers
from .models import User

from .enums import UserStatus

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'
        read_only_fields = ('id',)

    def validate(self, data):
        # Custom validation logic
        return data

    def validate_status(self, value):
        if value not in [choice[0] for choice in UserStatus.choices]:
            raise serializers.ValidationError('Invalid status')
        return value
