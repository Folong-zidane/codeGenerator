package com.basiccode.generator.generator.python.django.generators;

import java.util.*;

/**
 * DjangoEventSourcingGenerator - Phase 3
 * Event Sourcing implementation for audit trails and temporal queries
 *
 * Generates:
 * - Event store models
 * - Event streaming
 * - Aggregate reconstruction
 * - Event snapshots
 * - Temporal queries
 * - Audit trail views
 * - Event replay mechanisms
 *
 * @version 1.0
 */
public class DjangoEventSourcingGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoEventSourcingGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates Event Store model for Django
     */
    public String generateEventStoreModel() {
        StringBuilder code = new StringBuilder();

        code.append("import uuid\n");
        code.append("import json\n");
        code.append("from django.db import models\n");
        code.append("from django.utils import timezone\n");
        code.append("from enum import Enum\n\n");

        code.append("class EventType(models.TextChoices):\n");
        code.append("    CREATED = 'created', 'Created'\n");
        code.append("    UPDATED = 'updated', 'Updated'\n");
        code.append("    DELETED = 'deleted', 'Deleted'\n");
        code.append("    RESTORED = 'restored', 'Restored'\n");
        code.append("    STATE_CHANGED = 'state_changed', 'State Changed'\n\n");

        code.append("class EventStore(models.Model):\n");
        code.append("    \"\"\"\n");
        code.append("    Event Store for all domain events\n");
        code.append("    Immutable log of all changes\n");
        code.append("    \"\"\"\n");
        code.append("    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)\n");
        code.append("    aggregate_id = models.UUIDField(db_index=True)\n");
        code.append("    aggregate_type = models.CharField(max_length=255)\n");
        code.append("    event_type = models.CharField(\n");
        code.append("        max_length=50,\n");
        code.append("        choices=EventType.choices,\n");
        code.append("        db_index=True,\n");
        code.append("    )\n");
        code.append("    event_data = models.JSONField()\n");
        code.append("    metadata = models.JSONField(default=dict)\n");
        code.append("    version = models.IntegerField(db_index=True)\n");
        code.append("    timestamp = models.DateTimeField(auto_now_add=True, db_index=True)\n");
        code.append("    user_id = models.CharField(max_length=255, null=True, blank=True)\n");
        code.append("    ip_address = models.GenericIPAddressField(null=True, blank=True)\n");
        code.append("    user_agent = models.TextField(blank=True)\n");
        code.append("    correlation_id = models.UUIDField(null=True, blank=True, db_index=True)\n\n");

        code.append("    class Meta:\n");
        code.append("        db_table = 'event_store'\n");
        code.append("        ordering = ['version', 'timestamp']\n");
        code.append("        verbose_name = 'Event Store'\n");
        code.append("        verbose_name_plural = 'Event Stores'\n");
        code.append("        unique_together = [['aggregate_id', 'version']]\n");
        code.append("        indexes = [\n");
        code.append("            models.Index(fields=['aggregate_id', 'version']),\n");
        code.append("            models.Index(fields=['aggregate_type', 'timestamp']),\n");
        code.append("            models.Index(fields=['event_type', 'timestamp']),\n");
        code.append("            models.Index(fields=['correlation_id']),\n");
        code.append("        ]\n\n");

        code.append("    def __str__(self):\n");
        code.append("        return f'{self.aggregate_type}:{self.aggregate_id} - {self.event_type}'\n\n");

        code.append("    @classmethod\n");
        code.append("    def append_event(cls, aggregate_id, aggregate_type, event_type, event_data, metadata=None, user_id=None):\n");
        code.append("        \"\"\"Append new event to store\"\"\"\n");
        code.append("        # Get next version\n");
        code.append("        last_version = cls.objects.filter(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            aggregate_type=aggregate_type,\n");
        code.append("        ).values_list('version', flat=True).order_by('-version').first() or 0\n\n");

        code.append("        return cls.objects.create(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            aggregate_type=aggregate_type,\n");
        code.append("            event_type=event_type,\n");
        code.append("            event_data=event_data,\n");
        code.append("            metadata=metadata or {},\n");
        code.append("            version=last_version + 1,\n");
        code.append("            user_id=user_id,\n");
        code.append("        )\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_events_for_aggregate(cls, aggregate_id, from_version=0):\n");
        code.append("        \"\"\"Get all events for an aggregate after specified version\"\"\"\n");
        code.append("        return cls.objects.filter(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            version__gt=from_version,\n");
        code.append("        ).order_by('version')\n");

        return code.toString();
    }

    /**
     * Generates Event Snapshot model
     */
    public String generateEventSnapshotModel() {
        StringBuilder code = new StringBuilder();

        code.append("import uuid\n");
        code.append("from django.db import models\n\n");

        code.append("class EventSnapshot(models.Model):\n");
        code.append("    \"\"\"\n");
        code.append("    Snapshots of aggregate state at specific versions\n");
        code.append("    Used to optimize event sourcing reconstruction\n");
        code.append("    \"\"\"\n");
        code.append("    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)\n");
        code.append("    aggregate_id = models.UUIDField(unique=True, db_index=True)\n");
        code.append("    aggregate_type = models.CharField(max_length=255)\n");
        code.append("    snapshot_data = models.JSONField()\n");
        code.append("    version = models.IntegerField()\n");
        code.append("    timestamp = models.DateTimeField(auto_now=True)\n");
        code.append("    compressed = models.BooleanField(default=False)\n\n");

        code.append("    class Meta:\n");
        code.append("        db_table = 'event_snapshot'\n");
        code.append("        verbose_name = 'Event Snapshot'\n");
        code.append("        verbose_name_plural = 'Event Snapshots'\n");
        code.append("        ordering = ['-version']\n\n");

        code.append("    def __str__(self):\n");
        code.append("        return f'{self.aggregate_type}:{self.aggregate_id} v{self.version}'\n\n");

        code.append("    @classmethod\n");
        code.append("    def get_latest_snapshot(cls, aggregate_id):\n");
        code.append("        \"\"\"Get latest snapshot for aggregate\"\"\"\n");
        code.append("        return cls.objects.filter(aggregate_id=aggregate_id).first()\n\n");

        code.append("    @classmethod\n");
        code.append("    def create_snapshot(cls, aggregate_id, aggregate_type, snapshot_data, version):\n");
        code.append("        \"\"\"Create or update snapshot\"\"\"\n");
        code.append("        snapshot, created = cls.objects.update_or_create(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            defaults={\n");
        code.append("                'aggregate_type': aggregate_type,\n");
        code.append("                'snapshot_data': snapshot_data,\n");
        code.append("                'version': version,\n");
        code.append("            }\n");
        code.append("        )\n");
        code.append("        return snapshot\n");

        return code.toString();
    }

    /**
     * Generates aggregate reconstruction logic
     */
    public String generateAggregateReconstruction(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("from typing import Dict, Any, Optional\n");
        code.append("from datetime import datetime\n\n");

        code.append("class Aggregate:\n");
        code.append("    \"\"\"\n");
        code.append("    Base Aggregate class for event sourcing\n");
        code.append("    \"\"\"\n");
        code.append("    def __init__(self, aggregate_id: str):\n");
        code.append("        self.aggregate_id = aggregate_id\n");
        code.append("        self.version = 0\n");
        code.append("        self.changes = []\n\n");

        code.append("    def apply_event(self, event: Dict[str, Any]):\n");
        code.append("        \"\"\"Apply event to aggregate state\"\"\"\n");
        code.append("        event_type = event.get('event_type')\n");
        code.append("        if event_type == 'created':\n");
        code.append("            self._on_created(event)\n");
        code.append("        elif event_type == 'updated':\n");
        code.append("            self._on_updated(event)\n");
        code.append("        elif event_type == 'deleted':\n");
        code.append("            self._on_deleted(event)\n");
        code.append("        self.version = event.get('version', self.version + 1)\n\n");

        code.append("    def _on_created(self, event: Dict[str, Any]):\n");
        code.append("        pass\n\n");

        code.append("    def _on_updated(self, event: Dict[str, Any]):\n");
        code.append("        pass\n\n");

        code.append("    def _on_deleted(self, event: Dict[str, Any]):\n");
        code.append("        pass\n\n");

        code.append("class ").append(modelName).append("Aggregate(Aggregate):\n");
        code.append("    \"\"\"\n");
        code.append("    Aggregate for ").append(modelName).append("\n");
        code.append("    \"\"\"\n");
        code.append("    def __init__(self, aggregate_id: str):\n");
        code.append("        super().__init__(aggregate_id)\n");
        code.append("        self.state: Dict[str, Any] = {}\n\n");

        code.append("    @classmethod\n");
        code.append("    def from_history(cls, aggregate_id: str, events):\n");
        code.append("        \"\"\"Reconstruct aggregate from event history\"\"\"\n");
        code.append("        aggregate = cls(aggregate_id)\n");
        code.append("        for event in events:\n");
        code.append("            aggregate.apply_event(event)\n");
        code.append("        return aggregate\n\n");

        code.append("    @classmethod\n");
        code.append("    def from_snapshot(cls, aggregate_id: str, snapshot: Dict, remaining_events):\n");
        code.append("        \"\"\"Reconstruct aggregate from snapshot + remaining events\"\"\"\n");
        code.append("        aggregate = cls(aggregate_id)\n");
        code.append("        aggregate.state = snapshot.get('state', {})\n");
        code.append("        aggregate.version = snapshot.get('version', 0)\n");
        code.append("        # Apply remaining events since snapshot\n");
        code.append("        for event in remaining_events:\n");
        code.append("            aggregate.apply_event(event)\n");
        code.append("        return aggregate\n\n");

        code.append("    def _on_created(self, event: Dict[str, Any]):\n");
        code.append("        self.state = event.get('data', {})\n\n");

        code.append("    def _on_updated(self, event: Dict[str, Any]):\n");
        code.append("        self.state.update(event.get('data', {}))\n\n");

        code.append("    def _on_deleted(self, event: Dict[str, Any]):\n");
        code.append("        self.state['deleted_at'] = event.get('timestamp')\n\n");

        code.append("    def get_state(self) -> Dict[str, Any]:\n");
        code.append("        \"\"\"Get current state\"\"\"\n");
        code.append("        return self.state.copy()\n");

        return code.toString();
    }

    /**
     * Generates temporal query support
     */
    public String generateTemporalQueries() {
        StringBuilder code = new StringBuilder();

        code.append("from datetime import datetime\n");
        code.append("from typing import Any, Optional\n\n");

        code.append("class TemporalQuery:\n");
        code.append("    \"\"\"\n");
        code.append("    Support for querying data as of a specific point in time\n");
        code.append("    \"\"\"\n\n");

        code.append("    @staticmethod\n");
        code.append("    def get_aggregate_at_time(aggregate_id: str, timestamp: datetime):\n");
        code.append("        \"\"\"Get aggregate state as it was at specific time\"\"\"\n");
        code.append("        from .models import EventStore\n");
        code.append("        events = EventStore.objects.filter(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            timestamp__lte=timestamp,\n");
        code.append("        ).order_by('version')\n");
        code.append("        # Reconstruct from events\n");
        code.append("        state = {}\n");
        code.append("        for event in events:\n");
        code.append("            if event.event_type == 'created':\n");
        code.append("                state = event.event_data\n");
        code.append("            elif event.event_type == 'updated':\n");
        code.append("                state.update(event.event_data)\n");
        code.append("        return state\n\n");

        code.append("    @staticmethod\n");
        code.append("    def get_changes_between(aggregate_id: str, start_time: datetime, end_time: datetime):\n");
        code.append("        \"\"\"Get all changes for aggregate between two times\"\"\"\n");
        code.append("        from .models import EventStore\n");
        code.append("        return EventStore.objects.filter(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            timestamp__gte=start_time,\n");
        code.append("            timestamp__lte=end_time,\n");
        code.append("        ).order_by('timestamp').values('event_type', 'event_data', 'timestamp', 'user_id')\n\n");

        code.append("    @staticmethod\n");
        code.append("    def get_state_history(aggregate_id: str):\n");
        code.append("        \"\"\"Get complete state history with snapshots\"\"\"\n");
        code.append("        from .models import EventStore, EventSnapshot\n");
        code.append("        snapshot = EventSnapshot.objects.filter(aggregate_id=aggregate_id).first()\n");
        code.append("        \n");
        code.append("        history = []\n");
        code.append("        \n");
        code.append("        if snapshot:\n");
        code.append("            history.append({\n");
        code.append("                'type': 'snapshot',\n");
        code.append("                'version': snapshot.version,\n");
        code.append("                'timestamp': snapshot.timestamp,\n");
        code.append("                'data': snapshot.snapshot_data,\n");
        code.append("            })\n");
        code.append("            start_version = snapshot.version\n");
        code.append("        else:\n");
        code.append("            start_version = 0\n");
        code.append("        \n");
        code.append("        events = EventStore.objects.filter(\n");
        code.append("            aggregate_id=aggregate_id,\n");
        code.append("            version__gt=start_version,\n");
        code.append("        ).order_by('version')\n");
        code.append("        \n");
        code.append("        for event in events:\n");
        code.append("            history.append({\n");
        code.append("                'type': 'event',\n");
        code.append("                'version': event.version,\n");
        code.append("                'timestamp': event.timestamp,\n");
        code.append("                'event_type': event.event_type,\n");
        code.append("                'data': event.event_data,\n");
        code.append("                'user_id': event.user_id,\n");
        code.append("            })\n");
        code.append("        \n");
        code.append("        return history\n");

        return code.toString();
    }

    /**
     * Generates event replay mechanism
     */
    public String generateEventReplay() {
        StringBuilder code = new StringBuilder();

        code.append("import logging\n");
        code.append("from datetime import datetime\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("class EventReplay:\n");
        code.append("    \"\"\"\n");
        code.append("    Event replay mechanism for rebuilding read models\n");
        code.append("    \"\"\"\n\n");

        code.append("    @staticmethod\n");
        code.append("    def replay_all_events():\n");
        code.append("        \"\"\"Replay all events to rebuild state\"\"\"\n");
        code.append("        from .models import EventStore\n");
        code.append("        logger.info('Starting full event replay')\n");
        code.append("        \n");
        code.append("        events = EventStore.objects.all().order_by('timestamp')\n");
        code.append("        total = events.count()\n");
        code.append("        processed = 0\n");
        code.append("        \n");
        code.append("        for event in events:\n");
        code.append("            # Process event\n");
        code.append("            try:\n");
        code.append("                # Apply event to read model\n");
        code.append("                processed += 1\n");
        code.append("                if processed % 100 == 0:\n");
        code.append("                    logger.info(f'Processed {processed}/{total} events')\n");
        code.append("            except Exception as e:\n");
        code.append("                logger.error(f'Error replaying event {event.id}: {e}')\n");
        code.append("        \n");
        code.append("        logger.info(f'Event replay completed: {processed} events')\n\n");

        code.append("    @staticmethod\n");
        code.append("    def replay_since(from_version: int):\n");
        code.append("        \"\"\"Replay events since specific version\"\"\"\n");
        code.append("        from .models import EventStore\n");
        code.append("        logger.info(f'Replaying events since version {from_version}')\n");
        code.append("        \n");
        code.append("        events = EventStore.objects.filter(version__gte=from_version).order_by('version')\n");
        code.append("        for event in events:\n");
        code.append("            # Apply event\n");
        code.append("            pass\n\n");

        code.append("    @staticmethod\n");
        code.append("    def replay_aggregate(aggregate_id: str):\n");
        code.append("        \"\"\"Replay all events for specific aggregate\"\"\"\n");
        code.append("        from .models import EventStore\n");
        code.append("        logger.info(f'Replaying aggregate {aggregate_id}')\n");
        code.append("        \n");
        code.append("        events = EventStore.objects.filter(aggregate_id=aggregate_id).order_by('version')\n");
        code.append("        for event in events:\n");
        code.append("            # Apply event\n");
        code.append("            pass\n");

        return code.toString();
    }

    /**
     * Generates audit trail views
     */
    public String generateAuditTrailViews() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework import viewsets, filters\n");
        code.append("from rest_framework.decorators import action\n");
        code.append("from rest_framework.response import Response\n");
        code.append("from rest_framework.permissions import IsAuthenticated\n");
        code.append("from .models import EventStore\n");
        code.append("from django.utils.dateparse import parse_datetime\n\n");

        code.append("class AuditTrailViewSet(viewsets.ReadOnlyModelViewSet):\n");
        code.append("    \"\"\"\n");
        code.append("    ViewSet for audit trail (event history)\n");
        code.append("    \"\"\"\n");
        code.append("    queryset = EventStore.objects.all()\n");
        code.append("    permission_classes = [IsAuthenticated]\n");
        code.append("    filter_backends = [filters.OrderingFilter, filters.SearchFilter]\n");
        code.append("    ordering = ['-timestamp']\n");
        code.append("    search_fields = ['aggregate_id', 'aggregate_type', 'event_type']\n\n");

        code.append("    def get_queryset(self):\n");
        code.append("        queryset = super().get_queryset()\n");
        code.append("        # Filter by aggregate_id if provided\n");
        code.append("        aggregate_id = self.request.query_params.get('aggregate_id')\n");
        code.append("        if aggregate_id:\n");
        code.append("            queryset = queryset.filter(aggregate_id=aggregate_id)\n");
        code.append("        # Filter by date range\n");
        code.append("        start_date = self.request.query_params.get('start_date')\n");
        code.append("        if start_date:\n");
        code.append("            queryset = queryset.filter(timestamp__gte=parse_datetime(start_date))\n");
        code.append("        end_date = self.request.query_params.get('end_date')\n");
        code.append("        if end_date:\n");
        code.append("            queryset = queryset.filter(timestamp__lte=parse_datetime(end_date))\n");
        code.append("        return queryset\n\n");

        code.append("    @action(detail=False, methods=['get'])\n");
        code.append("    def aggregate_history(self, request):\n");
        code.append("        \"\"\"Get history for specific aggregate\"\"\"\n");
        code.append("        aggregate_id = request.query_params.get('id')\n");
        code.append("        if not aggregate_id:\n");
        code.append("            return Response({'error': 'aggregate_id required'}, status=400)\n");
        code.append("        \n");
        code.append("        events = self.get_queryset().filter(aggregate_id=aggregate_id)\n");
        code.append("        return Response({\n");
        code.append("            'aggregate_id': aggregate_id,\n");
        code.append("            'total_events': events.count(),\n");
        code.append("            'events': [{\n");
        code.append("                'version': e.version,\n");
        code.append("                'event_type': e.event_type,\n");
        code.append("                'timestamp': e.timestamp,\n");
        code.append("                'user_id': e.user_id,\n");
        code.append("                'data': e.event_data,\n");
        code.append("            } for e in events]\n");
        code.append("        })\n");

        return code.toString();
    }

    /**
     * Generates event sourcing setup guide
     */
    public String generateEventSourcingGuide() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("Event Sourcing Implementation Guide\n");
        for (int i = 0; i < 60; i++) {
            code.append("=");
        }
        code.append("\n\n");

        code.append("1. CONCEPTS\n");
        code.append("   - Event Store: Immutable log of all domain events\n");
        code.append("   - Aggregate: Object that maintains consistency boundaries\n");
        code.append("   - Event Sourcing: Store state changes as events\n");
        code.append("   - Snapshot: Cached state at specific version\n\n");

        code.append("2. BENEFITS\n");
        code.append("   - Complete audit trail\n");
        code.append("   - Time travel: Query data as of any point\n");
        code.append("   - Debugging: Replay events to understand state\n");
        code.append("   - Performance: Snapshot reduces replay time\n");
        code.append("   - Scalability: Read models separate from write model\n\n");

        code.append("3. EVENT STORE STRUCTURE\n");
        code.append("   - aggregate_id: Which entity changed\n");
        code.append("   - event_type: What changed\n");
        code.append("   - event_data: Details of change\n");
        code.append("   - version: Sequence number\n");
        code.append("   - timestamp: When it happened\n");
        code.append("   - user_id: Who made the change\n\n");

        code.append("4. AGGREGATE RECONSTRUCTION\n");
        code.append("   1. Load latest snapshot (if exists)\n");
        code.append("   2. Apply events since snapshot\n");
        code.append("   3. Current state = reconstructed state\n\n");

        code.append("5. TEMPORAL QUERIES\n");
        code.append("   - Get state at any past time\n");
        code.append("   - Track changes over time\n");
        code.append("   - Generate historical reports\n\n");

        code.append("6. EVENT REPLAY\n");
        code.append("   - Rebuild entire read model\n");
        code.append("   - Fix bugs in event handlers\n");
        code.append("   - Migrate to new storage\n\n");

        code.append("7. BEST PRACTICES\n");
        code.append("   - Keep events immutable\n");
        code.append("   - Use UUIDs for correlation\n");
        code.append("   - Archive old events\n");
        code.append("   - Monitor event store size\n");
        code.append("   - Take snapshots regularly\n");
        code.append("   - Version your event schema\n");
        code.append("\"\"\"\n");

        return code.toString();
    }
}
