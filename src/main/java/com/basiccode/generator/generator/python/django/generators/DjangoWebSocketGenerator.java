package com.basiccode.generator.generator.python.django.generators;

import java.util.*;

/**
 * DjangoWebSocketGenerator - Phase 3
 * WebSocket support for real-time API features
 *
 * Generates:
 * - WebSocket consumers
 * - Real-time data streaming
 * - Channel groups for broadcasting
 * - WebSocket authentication
 * - Message routing
 * - Connection management
 * - Real-time notifications
 *
 * @version 1.0
 */
public class DjangoWebSocketGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoWebSocketGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates Django Channels installation and configuration
     */
    public String generateChannelsConfiguration() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== Django Channels Configuration =====\n\n");

        code.append("# 1. INSTALLATION\n");
        code.append("# pip install channels channels-redis\n\n");

        code.append("# 2. settings.py configuration\n");
        code.append("INSTALLED_APPS = [\n");
        code.append("    'daphne',  # ASGI server\n");
        code.append("    'channels',\n");
        code.append("    'rest_framework',\n");
        code.append("    'app',\n");
        code.append("]\n\n");

        code.append("# ASGI Application\n");
        code.append("ASGI_APPLICATION = 'project.asgi.application'\n\n");

        code.append("# Channel Layers Configuration\n");
        code.append("CHANNEL_LAYERS = {\n");
        code.append("    'default': {\n");
        code.append("        'BACKEND': 'channels_redis.core.RedisChannelLayer',\n");
        code.append("        'CONFIG': {\n");
        code.append("            'hosts': [('localhost', 6379)],\n");
        code.append("            'capacity': 15000,\n");
        code.append("            'expiry': 10,\n");
        code.append("        },\n");
        code.append("    },\n");
        code.append("}\n\n");

        code.append("# 3. WSGI/ASGI\n");
        code.append("# Use 'daphne' as server:\n");
        code.append("# daphne -b 0.0.0.0 -p 8000 project.asgi:application\n");

        return code.toString();
    }

    /**
     * Generates ASGI configuration
     */
    public String generateASGIConfiguration() {
        StringBuilder code = new StringBuilder();

        code.append("import os\n");
        code.append("from django.core.asgi import get_asgi_application\n");
        code.append("from channels.routing import ProtocolTypeRouter, URLRouter\n");
        code.append("from channels.auth import AuthMiddlewareStack\n");
        code.append("from django.urls import path\n\n");

        code.append("os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'project.settings')\n\n");

        code.append("django_asgi_app = get_asgi_application()\n\n");

        code.append("from app import consumers\n\n");

        code.append("websocket_urlpatterns = [\n");
        code.append("    path('ws/chat/<str:room_name>/', consumers.ChatConsumer.as_asgi()),\n");
        code.append("    path('ws/notifications/', consumers.NotificationConsumer.as_asgi()),\n");
        code.append("    path('ws/data-stream/', consumers.DataStreamConsumer.as_asgi()),\n");
        code.append("]\n\n");

        code.append("application = ProtocolTypeRouter({\n");
        code.append("    'http': django_asgi_app,\n");
        code.append("    'websocket': AuthMiddlewareStack(\n");
        code.append("        URLRouter(\n");
        code.append("            websocket_urlpatterns\n");
        code.append("        )\n");
        code.append("    ),\n");
        code.append("})\n");

        return code.toString();
    }

    /**
     * Generates WebSocket consumers
     */
    public String generateWebSocketConsumers(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("import json\n");
        code.append("from channels.generic.websocket import AsyncWebsocketConsumer\n");
        code.append("from channels.db import database_sync_to_async\n");
        code.append("import logging\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        // Base consumer
        code.append("class BaseConsumer(AsyncWebsocketConsumer):\n");
        code.append("    \"\"\"\n");
        code.append("    Base WebSocket consumer with common functionality\n");
        code.append("    \"\"\"\n");
        code.append("    async def connect(self):\n");
        code.append("        \"\"\"Called when WebSocket connects\"\"\"\n");
        code.append("        await self.accept()\n");
        code.append("        logger.info(f'WebSocket connected: {self.channel_name}')\n\n");

        code.append("    async def disconnect(self, close_code):\n");
        code.append("        \"\"\"Called when WebSocket disconnects\"\"\"\n");
        code.append("        await self.close()\n");
        code.append("        logger.info(f'WebSocket disconnected: {self.channel_name}')\n\n");

        code.append("    async def receive(self, text_data):\n");
        code.append("        \"\"\"Called when message received\"\"\"\n");
        code.append("        try:\n");
        code.append("            data = json.loads(text_data)\n");
        code.append("            await self.handle_message(data)\n");
        code.append("        except json.JSONDecodeError:\n");
        code.append("            await self.send_error('Invalid JSON')\n\n");

        code.append("    async def send_error(self, message):\n");
        code.append("        \"\"\"Send error message to client\"\"\"\n");
        code.append("        await self.send(text_data=json.dumps({\n");
        code.append("            'type': 'error',\n");
        code.append("            'message': message,\n");
        code.append("        }))\n\n");

        code.append("    async def send_success(self, data):\n");
        code.append("        \"\"\"Send success message to client\"\"\"\n");
        code.append("        await self.send(text_data=json.dumps({\n");
        code.append("            'type': 'success',\n");
        code.append("            'data': data,\n");
        code.append("        }))\n\n");

        code.append("    async def handle_message(self, data):\n");
        code.append("        \"\"\"Override in subclasses\"\"\"\n");
        code.append("        pass\n\n");

        // Model specific consumer
        code.append("class ").append(modelName).append("Consumer(BaseConsumer):\n");
        code.append("    \"\"\"\n");
        code.append("    WebSocket consumer for ").append(modelName).append(" real-time updates\n");
        code.append("    \"\"\"\n");
        code.append("    async def connect(self):\n");
        code.append("        self.room_name = self.scope['url_route']['kwargs'].get('room_name', 'default')\n");
        code.append("        self.room_group_name = f'").append(modelName.toLowerCase()).append("_{self.room_name}'\n\n");
        code.append("        # Join room group\n");
        code.append("        await self.channel_layer.group_add(\n");
        code.append("            self.room_group_name,\n");
        code.append("            self.channel_name\n");
        code.append("        )\n");
        code.append("        await super().connect()\n\n");

        code.append("    async def disconnect(self, close_code):\n");
        code.append("        # Leave room group\n");
        code.append("        await self.channel_layer.group_discard(\n");
        code.append("            self.room_group_name,\n");
        code.append("            self.channel_name\n");
        code.append("        )\n");
        code.append("        await super().disconnect(close_code)\n\n");

        code.append("    async def handle_message(self, data):\n");
        code.append("        \"\"\"Handle incoming messages\"\"\"\n");
        code.append("        action = data.get('action')\n");
        code.append("        if action == 'subscribe':\n");
        code.append("            await self.handle_subscribe(data)\n");
        code.append("        elif action == 'update':\n");
        code.append("            await self.handle_update(data)\n");
        code.append("        elif action == 'delete':\n");
        code.append("            await self.handle_delete(data)\n\n");

        code.append("    async def handle_subscribe(self, data):\n");
        code.append("        \"\"\"Handle subscription to updates\"\"\"\n");
        code.append("        obj_id = data.get('id')\n");
        code.append("        obj = await self.get_object(obj_id)\n");
        code.append("        if obj:\n");
        code.append("            await self.send_success({\n");
        code.append("                'id': obj_id,\n");
        code.append("                'data': await self.serialize_object(obj),\n");
        code.append("            })\n\n");

        code.append("    async def handle_update(self, data):\n");
        code.append("        \"\"\"Handle object update\"\"\"\n");
        code.append("        obj_id = data.get('id')\n");
        code.append("        await self.update_object(obj_id, data.get('data', {}))\n");
        code.append("        # Broadcast to group\n");
        code.append("        await self.channel_layer.group_send(\n");
        code.append("            self.room_group_name,\n");
        code.append("            {\n");
        code.append("                'type': '").append(modelName.toLowerCase()).append("_update',\n");
        code.append("                'id': obj_id,\n");
        code.append("                'data': data.get('data', {}),\n");
        code.append("            }\n");
        code.append("        )\n\n");

        code.append("    async def handle_delete(self, data):\n");
        code.append("        \"\"\"Handle object deletion\"\"\"\n");
        code.append("        obj_id = data.get('id')\n");
        code.append("        await self.delete_object(obj_id)\n");
        code.append("        await self.channel_layer.group_send(\n");
        code.append("            self.room_group_name,\n");
        code.append("            {\n");
        code.append("                'type': '").append(modelName.toLowerCase()).append("_delete',\n");
        code.append("                'id': obj_id,\n");
        code.append("            }\n");
        code.append("        )\n\n");

        code.append("    async def ").append(modelName.toLowerCase()).append("_update(self, event):\n");
        code.append("        \"\"\"Receive update from group\"\"\"\n");
        code.append("        await self.send(text_data=json.dumps({\n");
        code.append("            'type': 'update',\n");
        code.append("            'id': event['id'],\n");
        code.append("            'data': event['data'],\n");
        code.append("        }))\n\n");

        code.append("    async def ").append(modelName.toLowerCase()).append("_delete(self, event):\n");
        code.append("        \"\"\"Receive delete from group\"\"\"\n");
        code.append("        await self.send(text_data=json.dumps({\n");
        code.append("            'type': 'delete',\n");
        code.append("            'id': event['id'],\n");
        code.append("        }))\n\n");

        code.append("    @database_sync_to_async\n");
        code.append("    def get_object(self, obj_id):\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        return ").append(modelName).append(".objects.filter(pk=obj_id).first()\n\n");

        code.append("    @database_sync_to_async\n");
        code.append("    def serialize_object(self, obj):\n");
        code.append("        from .serializers import ").append(modelName).append("DetailSerializer\n");
        code.append("        return ").append(modelName).append("DetailSerializer(obj).data\n\n");

        code.append("    @database_sync_to_async\n");
        code.append("    def update_object(self, obj_id, data):\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        obj = ").append(modelName).append(".objects.get(pk=obj_id)\n");
        code.append("        for key, value in data.items():\n");
        code.append("            setattr(obj, key, value)\n");
        code.append("        obj.save()\n");
        code.append("        return obj\n\n");

        code.append("    @database_sync_to_async\n");
        code.append("    def delete_object(self, obj_id):\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        ").append(modelName).append(".objects.filter(pk=obj_id).delete()\n");

        return code.toString();
    }

    /**
     * Generates notification consumer
     */
    public String generateNotificationConsumer() {
        StringBuilder code = new StringBuilder();

        code.append("class NotificationConsumer(BaseConsumer):\n");
        code.append("    \"\"\"\n");
        code.append("    Consumer for real-time notifications\n");
        code.append("    \"\"\"\n");
        code.append("    async def connect(self):\n");
        code.append("        user_id = self.scope['user'].id if self.scope['user'].is_authenticated else None\n");
        code.append("        if not user_id:\n");
        code.append("            await self.close()\n");
        code.append("            return\n\n");
        code.append("        self.user_id = user_id\n");
        code.append("        self.notification_group = f'notifications_{user_id}'\n");
        code.append("        await self.channel_layer.group_add(\n");
        code.append("            self.notification_group,\n");
        code.append("            self.channel_name\n");
        code.append("        )\n");
        code.append("        await super().connect()\n\n");

        code.append("    async def notification_message(self, event):\n");
        code.append("        \"\"\"Receive notification from group\"\"\"\n");
        code.append("        await self.send(text_data=json.dumps({\n");
        code.append("            'type': 'notification',\n");
        code.append("            'title': event.get('title'),\n");
        code.append("            'message': event.get('message'),\n");
        code.append("            'level': event.get('level', 'info'),\n");
        code.append("            'timestamp': str(event.get('timestamp')),\n");
        code.append("        }))\n\n");

        code.append("    async def handle_message(self, data):\n");
        code.append("        action = data.get('action')\n");
        code.append("        if action == 'mark_read':\n");
        code.append("            await self.mark_notification_read(data.get('notification_id'))\n");

        return code.toString();
    }

    /**
     * Generates client-side WebSocket connection guide
     */
    public String generateWebSocketClientGuide() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("WebSocket Client Implementation Guide\n");
        for(int i = 0; i < 60; i++) code.append("=");
        code.append("\n\n");

        code.append("JAVASCRIPT/TYPESCRIPT CLIENT\n\n");

        code.append("class WebSocketClient {\n");
        code.append("    constructor(url, onMessage, onError) {\n");
        code.append("        this.url = url;\n");
        code.append("        this.onMessage = onMessage;\n");
        code.append("        this.onError = onError;\n");
        code.append("        this.ws = null;\n");
        code.append("    }\n\n");

        code.append("    connect() {\n");
        code.append("        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';\n");
        code.append("        this.ws = new WebSocket(`${protocol}//${window.location.host}${this.url}`);\n\n");

        code.append("        this.ws.onopen = () => console.log('Connected');\n");
        code.append("        this.ws.onmessage = (event) => {\n");
        code.append("            const data = JSON.parse(event.data);\n");
        code.append("            this.onMessage(data);\n");
        code.append("        };\n");
        code.append("        this.ws.onerror = (error) => {\n");
        code.append("            console.error('WebSocket error:', error);\n");
        code.append("            this.onError(error);\n");
        code.append("        };\n");
        code.append("    }\n\n");

        code.append("    send(message) {\n");
        code.append("        if (this.ws.readyState === WebSocket.OPEN) {\n");
        code.append("            this.ws.send(JSON.stringify(message));\n");
        code.append("        }\n");
        code.append("    }\n\n");

        code.append("    disconnect() {\n");
        code.append("        if (this.ws) {\n");
        code.append("            this.ws.close();\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n\n");

        code.append("// Usage\n");
        code.append("const client = new WebSocketClient(\n");
        code.append("    '/ws/notifications/',\n");
        code.append("    (data) => console.log('Message:', data),\n");
        code.append("    (error) => console.error('Error:', error)\n");
        code.append(");\n");
        code.append("client.connect();\n\n");

        code.append("// Send message\n");
        code.append("client.send({\n");
        code.append("    action: 'subscribe',\n");
        code.append("    id: 123\n");
        code.append("});\n\n");

        code.append("TESTING WEBSOCKETS\n");
        code.append("1. Use wscat: npm install -g wscat\n");
        code.append("2. Connect: wscat -c ws://localhost:8000/ws/notifications/\n");
        code.append("3. Send JSON messages\n\n");

        code.append("PRODUCTION DEPLOYMENT\n");
        code.append("1. Use Daphne as ASGI server\n");
        code.append("2. Configure Redis for channel layer\n");
        code.append("3. Set up load balancer with sticky sessions\n");
        code.append("4. Monitor WebSocket connections\n");
        code.append("5. Implement heartbeat/ping-pong\n");
        code.append("\"\"\"\n");

        return code.toString();
    }

    /**
     * Generates WebSocket authentication middleware
     */
    public String generateWebSocketAuth() {
        StringBuilder code = new StringBuilder();

        code.append("from channels.db import database_sync_to_async\n");
        code.append("from django.contrib.auth.models import AnonymousUser\n");
        code.append("import json\n\n");

        code.append("@database_sync_to_async\n");
        code.append("def get_user(user_id):\n");
        code.append("    from django.contrib.auth.models import User\n");
        code.append("    try:\n");
        code.append("        return User.objects.get(id=user_id)\n");
        code.append("    except User.DoesNotExist:\n");
        code.append("        return AnonymousUser()\n\n");

        code.append("class TokenAuthMiddleware:\n");
        code.append("    \"\"\"\n");
        code.append("    Middleware to authenticate WebSocket connections via token\n");
        code.append("    \"\"\"\n");
        code.append("    def __init__(self, inner):\n");
        code.append("        self.inner = inner\n\n");

        code.append("    async def __call__(self, scope, receive, send):\n");
        code.append("        # Extract token from query string\n");
        code.append("        query_string = scope.get('query_string', b'').decode()\n");
        code.append("        token = None\n");
        code.append("        if 'token=' in query_string:\n");
        code.append("            token = query_string.split('token=')[1]\n\n");

        code.append("        if token:\n");
        code.append("            # Verify JWT token\n");
        code.append("            from rest_framework_simplejwt.tokens import UntypedToken\n");
        code.append("            try:\n");
        code.append("                untyped_token = UntypedToken(token)\n");
        code.append("                user_id = untyped_token['user_id']\n");
        code.append("                scope['user'] = await get_user(user_id)\n");
        code.append("            except Exception:\n");
        code.append("                scope['user'] = AnonymousUser()\n");
        code.append("        else:\n");
        code.append("            scope['user'] = AnonymousUser()\n\n");

        code.append("        return await self.inner(scope, receive, send)\n");

        return code.toString();
    }

    /**
     * Generates broadcast helper functions
     */
    public String generateBroadcastHelpers() {
        StringBuilder code = new StringBuilder();

        code.append("from channels.layers import get_channel_layer\n");
        code.append("import asyncio\n\n");

        code.append("async def broadcast_to_user(user_id, message):\n");
        code.append("    \"\"\"\n");
        code.append("    Broadcast message to specific user's WebSocket connections\n");
        code.append("    \"\"\"\n");
        code.append("    channel_layer = get_channel_layer()\n");
        code.append("    await channel_layer.group_send(\n");
        code.append("        f'notifications_{user_id}',\n");
        code.append("        {\n");
        code.append("            'type': 'notification_message',\n");
        code.append("            'title': message.get('title'),\n");
        code.append("            'message': message.get('message'),\n");
        code.append("            'level': message.get('level', 'info'),\n");
        code.append("            'timestamp': str(datetime.now()),\n");
        code.append("        }\n");
        code.append("    )\n\n");

        code.append("async def broadcast_to_group(group_name, message):\n");
        code.append("    \"\"\"\n");
        code.append("    Broadcast message to all users in a group\n");
        code.append("    \"\"\"\n");
        code.append("    channel_layer = get_channel_layer()\n");
        code.append("    await channel_layer.group_send(\n");
        code.append("        group_name,\n");
        code.append("        {\n");
        code.append("            'type': 'group_message',\n");
        code.append("            'message': message,\n");
        code.append("        }\n");
        code.append("    )\n\n");

        code.append("async def broadcast_to_all(message):\n");
        code.append("    \"\"\"\n");
        code.append("    Broadcast message to all connected users\n");
        code.append("    \"\"\"\n");
        code.append("    channel_layer = get_channel_layer()\n");
        code.append("    await channel_layer.group_send(\n");
        code.append("        'broadcast',\n");
        code.append("        {\n");
        code.append("            'type': 'broadcast_message',\n");
        code.append("            'message': message,\n");
        code.append("        }\n");
        code.append("    )\n");

        return code.toString();
    }
}
