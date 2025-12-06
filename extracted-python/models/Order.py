from sqlalchemy import Column, Integer, String, DateTime, Enum
from sqlalchemy.ext.declarative import declarative_base
from datetime import datetime
from enum import Enum as PyEnum

class OrderStatus(PyEnum):
    ACTIVE = "ACTIVE"
    SUSPENDED = "SUSPENDED"

Base = declarative_base()

class Order(Base):
    __tablename__ = 'orders'

    status = Column(Enum(OrderStatus), default=OrderStatus.ACTIVE)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    def suspend(self):
        if self.status != OrderStatus.ACTIVE:
            raise ValueError(f'Cannot suspend user in state: {self.status}')
        self.status = OrderStatus.SUSPENDED

    def activate(self):
        if self.status != OrderStatus.SUSPENDED:
            raise ValueError(f'Cannot activate user in state: {self.status}')
        self.status = OrderStatus.ACTIVE
