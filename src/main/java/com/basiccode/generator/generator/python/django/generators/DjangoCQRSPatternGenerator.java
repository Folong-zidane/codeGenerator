package com.basiccode.generator.generator.python.django.generators;

import java.util.*;

/**
 * DjangoCQRSPatternGenerator - Phase 3
 * CQRS (Command Query Responsibility Segregation) Pattern Implementation
 *
 * Generates:
 * - Command classes for write operations
 * - Query classes for read operations
 * - Command handlers
 * - Query handlers
 * - Event bus for async operations
 * - Repository pattern implementation
 * - CQRS view models
 *
 * @version 1.0
 */
public class DjangoCQRSPatternGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoCQRSPatternGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates abstract Command base class
     */
    public String generateCommandBase() {
        StringBuilder code = new StringBuilder();

        code.append("from abc import ABC, abstractmethod\n");
        code.append("from typing import Any, Optional\n");
        code.append("from dataclasses import dataclass\n");
        code.append("from datetime import datetime\n\n");

        code.append("@dataclass\n");
        code.append("class Command(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Abstract base class for all commands\n");
        code.append("    Commands represent intent to change system state\n");
        code.append("    \"\"\"\n");
        code.append("    command_id: str\n");
        code.append("    aggregate_id: str\n");
        code.append("    timestamp: datetime\n");
        code.append("    user_id: Optional[str] = None\n\n");

        code.append("    @abstractmethod\n");
        code.append("    def execute(self):\n");
        code.append("        \"\"\"Execute the command\"\"\"\n");
        code.append("        pass\n\n");

        code.append("    def validate(self) -> bool:\n");
        code.append("        \"\"\"Validate command data\"\"\"\n");
        code.append("        return (\n");
        code.append("            self.command_id is not None and\n");
        code.append("            self.aggregate_id is not None and\n");
        code.append("            self.timestamp is not None\n");
        code.append("        )\n");

        return code.toString();
    }

    /**
     * Generates abstract Query base class
     */
    public String generateQueryBase() {
        StringBuilder code = new StringBuilder();

        code.append("from abc import ABC, abstractmethod\n");
        code.append("from dataclasses import dataclass\n");
        code.append("from typing import List, Optional, Any\n\n");

        code.append("@dataclass\n");
        code.append("class Query(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Abstract base class for all queries\n");
        code.append("    Queries represent intent to retrieve data\n");
        code.append("    \"\"\"\n");
        code.append("    query_id: str\n");
        code.append("    user_id: Optional[str] = None\n\n");

        code.append("    @abstractmethod\n");
        code.append("    def validate(self) -> bool:\n");
        code.append("        \"\"\"Validate query parameters\"\"\"\n");
        code.append("        pass\n\n");

        code.append("@dataclass\n");
        code.append("class QueryResult:\n");
        code.append("    \"\"\"\n");
        code.append("    Result wrapper for query responses\n");
        code.append("    \"\"\"\n");
        code.append("    success: bool\n");
        code.append("    data: Optional[Any] = None\n");
        code.append("    error: Optional[str] = None\n");
        code.append("    count: Optional[int] = None\n");

        return code.toString();
    }

    /**
     * Generates command and query handlers
     */
    public String generateHandlers(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("from typing import Type, Dict, Callable\n");
        code.append("from abc import ABC, abstractmethod\n");
        code.append("import logging\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("class CommandHandler(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Abstract base for all command handlers\n");
        code.append("    \"\"\"\n");
        code.append("    @abstractmethod\n");
        code.append("    def handle(self, command: Command) -> Any:\n");
        code.append("        pass\n\n");

        code.append("class ").append(modelName).append("CommandHandler(CommandHandler):\n");
        code.append("    \"\"\"\n");
        code.append("    Handler for ").append(modelName).append(" commands\n");
        code.append("    \"\"\"\n\n");

        code.append("    def create_").append(formatFieldName(modelName)).append("(self, command) -> Any:\n");
        code.append("        \"\"\"Create ").append(modelName).append(" command\"\"\"\n");
        code.append("        logger.info(f'Creating ").append(modelName).append("', extra={'command_id': command.command_id})\n");
        code.append("        # Implementation\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        return ").append(modelName).append(".objects.create(**command.data)\n\n");

        code.append("    def update_").append(formatFieldName(modelName)).append("(self, command) -> Any:\n");
        code.append("        \"\"\"Update ").append(modelName).append(" command\"\"\"\n");
        code.append("        logger.info(f'Updating ").append(modelName).append("', extra={'command_id': command.command_id})\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        obj = ").append(modelName).append(".objects.get(pk=command.aggregate_id)\n");
        code.append("        for key, value in command.data.items():\n");
        code.append("            setattr(obj, key, value)\n");
        code.append("        obj.save()\n");
        code.append("        return obj\n\n");

        code.append("    def delete_").append(formatFieldName(modelName)).append("(self, command) -> bool:\n");
        code.append("        \"\"\"Delete ").append(modelName).append(" command\"\"\"\n");
        code.append("        logger.info(f'Deleting ").append(modelName).append("', extra={'command_id': command.command_id})\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        obj = ").append(modelName).append(".objects.get(pk=command.aggregate_id)\n");
        code.append("        obj.delete()\n");
        code.append("        return True\n\n");

        code.append("    def handle(self, command: Command) -> Any:\n");
        code.append("        \"\"\"Route command to appropriate handler\"\"\"\n");
        code.append("        handler_map = {\n");
        code.append("            'Create").append(modelName).append("': self.create_").append(formatFieldName(modelName)).append(",\n");
        code.append("            'Update").append(modelName).append("': self.update_").append(formatFieldName(modelName)).append(",\n");
        code.append("            'Delete").append(modelName).append("': self.delete_").append(formatFieldName(modelName)).append(",\n");
        code.append("        }\n");
        code.append("        handler = handler_map.get(command.__class__.__name__)\n");
        code.append("        if handler:\n");
        code.append("            return handler(command)\n");
        code.append("        raise ValueError(f'Unknown command: {command.__class__.__name__}')\n\n");

        code.append("class QueryHandler(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Abstract base for all query handlers\n");
        code.append("    \"\"\"\n");
        code.append("    @abstractmethod\n");
        code.append("    def handle(self, query: Query) -> QueryResult:\n");
        code.append("        pass\n\n");

        code.append("class ").append(modelName).append("QueryHandler(QueryHandler):\n");
        code.append("    \"\"\"\n");
        code.append("    Handler for ").append(modelName).append(" queries\n");
        code.append("    \"\"\"\n\n");

        code.append("    def get_by_id(self, query) -> QueryResult:\n");
        code.append("        \"\"\"Get ").append(modelName).append(" by ID\"\"\"\n");
        code.append("        try:\n");
        code.append("            from .models import ").append(modelName).append("\n");
        code.append("            obj = ").append(modelName).append(".objects.get(pk=query.aggregate_id)\n");
        code.append("            return QueryResult(success=True, data=obj)\n");
        code.append("        except ").append(modelName).append(".DoesNotExist:\n");
        code.append("            return QueryResult(success=False, error='Not found')\n\n");

        code.append("    def get_all(self, query) -> QueryResult:\n");
        code.append("        \"\"\"Get all ").append(modelName).append("\"\"\"\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        objs = list(").append(modelName).append(".objects.all())\n");
        code.append("        return QueryResult(success=True, data=objs, count=len(objs))\n\n");

        code.append("    def handle(self, query: Query) -> QueryResult:\n");
        code.append("        \"\"\"Route query to appropriate handler\"\"\"\n");
        code.append("        query_type = query.__class__.__name__\n");
        code.append("        if 'GetById' in query_type:\n");
        code.append("            return self.get_by_id(query)\n");
        code.append("        elif 'GetAll' in query_type:\n");
        code.append("            return self.get_all(query)\n");
        code.append("        raise ValueError(f'Unknown query: {query_type}')\n");

        return code.toString();
    }

    /**
     * Generates command and query dispatcher
     */
    public String generateDispatcher() {
        StringBuilder code = new StringBuilder();

        code.append("from typing import Type, Dict, Any\n");
        code.append("import logging\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("class CommandDispatcher:\n");
        code.append("    \"\"\"\n");
        code.append("    Central dispatcher for command handling\n");
        code.append("    \"\"\"\n");
        code.append("    _handlers: Dict[Type, CommandHandler] = {}\n\n");

        code.append("    @classmethod\n");
        code.append("    def register_handler(cls, command_type: Type, handler: CommandHandler):\n");
        code.append("        \"\"\"Register a command handler\"\"\"\n");
        code.append("        cls._handlers[command_type] = handler\n");
        code.append("        logger.info(f'Registered handler for {command_type.__name__}')\n\n");

        code.append("    @classmethod\n");
        code.append("    def execute(cls, command: Command) -> Any:\n");
        code.append("        \"\"\"Execute a command\"\"\"\n");
        code.append("        if not command.validate():\n");
        code.append("            raise ValueError('Invalid command')\n\n");
        code.append("        handler = cls._handlers.get(type(command))\n");
        code.append("        if not handler:\n");
        code.append("            raise ValueError(f'No handler for {type(command).__name__}')\n\n");
        code.append("        return handler.handle(command)\n\n");

        code.append("class QueryDispatcher:\n");
        code.append("    \"\"\"\n");
        code.append("    Central dispatcher for query handling\n");
        code.append("    \"\"\"\n");
        code.append("    _handlers: Dict[Type, QueryHandler] = {}\n\n");

        code.append("    @classmethod\n");
        code.append("    def register_handler(cls, query_type: Type, handler: QueryHandler):\n");
        code.append("        \"\"\"Register a query handler\"\"\"\n");
        code.append("        cls._handlers[query_type] = handler\n");
        code.append("        logger.info(f'Registered handler for {query_type.__name__}')\n\n");

        code.append("    @classmethod\n");
        code.append("    def query(cls, query: Query) -> QueryResult:\n");
        code.append("        \"\"\"Execute a query\"\"\"\n");
        code.append("        if not query.validate():\n");
        code.append("            raise ValueError('Invalid query')\n\n");
        code.append("        handler = cls._handlers.get(type(query))\n");
        code.append("        if not handler:\n");
        code.append("            raise ValueError(f'No handler for {type(query).__name__}')\n\n");
        code.append("        return handler.handle(query)\n");

        return code.toString();
    }

    /**
     * Generates event classes for event sourcing
     */
    public String generateEventClasses(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("from dataclasses import dataclass\n");
        code.append("from datetime import datetime\n");
        code.append("from typing import Optional, Any\n");
        code.append("from enum import Enum\n\n");

        code.append("class EventType(Enum):\n");
        code.append("    CREATED = 'created'\n");
        code.append("    UPDATED = 'updated'\n");
        code.append("    DELETED = 'deleted'\n");
        code.append("    RESTORED = 'restored'\n\n");

        code.append("@dataclass\n");
        code.append("class DomainEvent:\n");
        code.append("    \"\"\"\n");
        code.append("    Base domain event class\n");
        code.append("    \"\"\"\n");
        code.append("    event_id: str\n");
        code.append("    aggregate_id: str\n");
        code.append("    event_type: EventType\n");
        code.append("    timestamp: datetime\n");
        code.append("    user_id: Optional[str] = None\n");
        code.append("    data: Optional[Any] = None\n");
        code.append("    version: int = 1\n\n");

        code.append("@dataclass\n");
        code.append("class ").append(modelName).append("CreatedEvent(DomainEvent):\n");
        code.append("    \"\"\"Event when ").append(modelName).append(" is created\"\"\"\n");
        code.append("    pass\n\n");

        code.append("@dataclass\n");
        code.append("class ").append(modelName).append("UpdatedEvent(DomainEvent):\n");
        code.append("    \"\"\"Event when ").append(modelName).append(" is updated\"\"\"\n");
        code.append("    pass\n\n");

        code.append("@dataclass\n");
        code.append("class ").append(modelName).append("DeletedEvent(DomainEvent):\n");
        code.append("    \"\"\"Event when ").append(modelName).append(" is deleted\"\"\"\n");
        code.append("    pass\n");

        return code.toString();
    }

    /**
     * Generates event bus for async operations
     */
    public String generateEventBus() {
        StringBuilder code = new StringBuilder();

        code.append("from typing import List, Callable, Dict, Type\n");
        code.append("import logging\n");
        code.append("from abc import ABC, abstractmethod\n");
        code.append("import asyncio\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("class EventSubscriber(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Abstract base for event subscribers\n");
        code.append("    \"\"\"\n");
        code.append("    @abstractmethod\n");
        code.append("    async def handle(self, event: DomainEvent):\n");
        code.append("        pass\n\n");

        code.append("class EventBus:\n");
        code.append("    \"\"\"\n");
        code.append("    Central event bus for publishing domain events\n");
        code.append("    \"\"\"\n");
        code.append("    _subscribers: Dict[Type, List[EventSubscriber]] = {}\n");
        code.append("    _event_store: List[DomainEvent] = []\n\n");

        code.append("    @classmethod\n");
        code.append("    def subscribe(cls, event_type: Type, subscriber: EventSubscriber):\n");
        code.append("        \"\"\"Subscribe to event type\"\"\"\n");
        code.append("        if event_type not in cls._subscribers:\n");
        code.append("            cls._subscribers[event_type] = []\n");
        code.append("        cls._subscribers[event_type].append(subscriber)\n");
        code.append("        logger.info(f'Subscribed {subscriber.__class__.__name__} to {event_type.__name__}')\n\n");

        code.append("    @classmethod\n");
        code.append("    async def publish(cls, event: DomainEvent):\n");
        code.append("        \"\"\"Publish event to all subscribers\"\"\"\n");
        code.append("        # Store event\n");
        code.append("        cls._event_store.append(event)\n");
        code.append("        logger.info(f'Publishing event: {event.__class__.__name__}')\n\n");
        code.append("        # Notify subscribers\n");
        code.append("        subscribers = cls._subscribers.get(type(event), [])\n");
        code.append("        tasks = [subscriber.handle(event) for subscriber in subscribers]\n");
        code.append("        if tasks:\n");
        code.append("            await asyncio.gather(*tasks)\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_events(cls, aggregate_id: str) -> List[DomainEvent]:\n");
        code.append("        \"\"\"Get all events for an aggregate\"\"\"\n");
        code.append("        return [e for e in cls._event_store if e.aggregate_id == aggregate_id]\n");

        return code.toString();
    }

    /**
     * Generates repository pattern implementation
     */
    public String generateRepository(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("from abc import ABC, abstractmethod\n");
        code.append("from typing import List, Optional, Any\n\n");

        code.append("class IRepository(ABC):\n");
        code.append("    \"\"\"\n");
        code.append("    Interface for data repository\n");
        code.append("    \"\"\"\n");
        code.append("    @abstractmethod\n");
        code.append("    def add(self, entity: Any) -> Any:\n");
        code.append("        pass\n\n");
        code.append("    @abstractmethod\n");
        code.append("    def get(self, entity_id: Any) -> Optional[Any]:\n");
        code.append("        pass\n\n");
        code.append("    @abstractmethod\n");
        code.append("    def get_all(self) -> List[Any]:\n");
        code.append("        pass\n\n");
        code.append("    @abstractmethod\n");
        code.append("    def update(self, entity: Any) -> Any:\n");
        code.append("        pass\n\n");
        code.append("    @abstractmethod\n");
        code.append("    def remove(self, entity_id: Any) -> bool:\n");
        code.append("        pass\n\n");

        code.append("class ").append(modelName).append("Repository(IRepository):\n");
        code.append("    \"\"\"\n");
        code.append("    Repository for ").append(modelName).append(" entities\n");
        code.append("    \"\"\"\n");
        code.append("    def __init__(self):\n");
        code.append("        from .models import ").append(modelName).append("\n");
        code.append("        self.model = ").append(modelName).append("\n\n");

        code.append("    def add(self, entity: Any) -> Any:\n");
        code.append("        entity.save()\n");
        code.append("        return entity\n\n");

        code.append("    def get(self, entity_id: Any) -> Optional[Any]:\n");
        code.append("        return self.model.objects.filter(pk=entity_id).first()\n\n");

        code.append("    def get_all(self) -> List[Any]:\n");
        code.append("        return list(self.model.objects.all())\n\n");

        code.append("    def update(self, entity: Any) -> Any:\n");
        code.append("        entity.save()\n");
        code.append("        return entity\n\n");

        code.append("    def remove(self, entity_id: Any) -> bool:\n");
        code.append("        result = self.model.objects.filter(pk=entity_id).delete()\n");
        code.append("        return result[0] > 0\n");

        return code.toString();
    }

    /**
     * Generates complete CQRS setup guide
     */
    public String generateCQRSGuide() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("CQRS Pattern Implementation Guide\n");
        for(int i = 0; i < 60; i++) code.append("=");
        code.append("\n\n");

        code.append("1. COMMAND PATTERN\n");
        code.append("   - Represents intent to change system state\n");
        code.append("   - Immutable objects\n");
        code.append("   - Contains all data needed for operation\n");
        code.append("   - Examples: CreateUserCommand, UpdateProfileCommand\n\n");

        code.append("2. QUERY PATTERN\n");
        code.append("   - Represents intent to retrieve data\n");
        code.append("   - Read-only operations\n");
        code.append("   - Returns QueryResult\n");
        code.append("   - Examples: GetUserByIdQuery, ListAllUsersQuery\n\n");

        code.append("3. COMMAND DISPATCHER\n");
        code.append("   - Routes commands to handlers\n");
        code.append("   - Ensures validation\n");
        code.append("   - Handles errors and exceptions\n\n");

        code.append("4. QUERY DISPATCHER\n");
        code.append("   - Routes queries to handlers\n");
        code.append("   - Manages read models\n");
        code.append("   - Optimizes for query performance\n\n");

        code.append("5. EVENT SOURCING\n");
        code.append("   - Store all changes as events\n");
        code.append("   - Build state from event history\n");
        code.append("   - Audit trail included\n");
        code.append("   - Time travel debugging\n\n");

        code.append("6. BENEFITS\n");
        code.append("   - Scalability: Read/write models separate\n");
        code.append("   - Performance: Optimize each path independently\n");
        code.append("   - Reliability: Event sourcing provides audit trail\n");
        code.append("   - Testability: Commands/queries are independent\n\n");

        code.append("7. IMPLEMENTATION STEPS\n");
        code.append("   1. Define commands\n");
        code.append("   2. Implement command handlers\n");
        code.append("   3. Define queries\n");
        code.append("   4. Implement query handlers\n");
        code.append("   5. Set up dispatchers\n");
        code.append("   6. Integrate with views\n");
        code.append("   7. Add event sourcing\n");
        code.append("\"\"\"\n");

        return code.toString();
    }

    /**
     * Formats field name (snake_case)
     */
    private String formatFieldName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
