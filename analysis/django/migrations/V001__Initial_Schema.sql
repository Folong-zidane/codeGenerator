from django.db import migrations, models
import uuid

class Migration(migrations.Migration):
    initial = True
    dependencies = []

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)),
                ('username', models.CharField(max_length=255)),
            ],
            options={
                'db_table': 'users',
                'ordering': ['-id'],
            },
        ),
    ]
