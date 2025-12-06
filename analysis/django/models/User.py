from django.db import models
from django.core.validators import MinValueValidator, MaxValueValidator
import uuid

from .enums import UserStatus

class User(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    username = models.CharField(max_length=255)

    class Meta:
        db_table = 'users'
        ordering = ['-id']

    def __str__(self):
        return f"User({self.id})"

    def can_suspend(self):
        return self.status == UserStatus.ACTIVE

    def can_activate(self):
        return self.status in [UserStatus.SUSPENDED, UserStatus.INACTIVE]
