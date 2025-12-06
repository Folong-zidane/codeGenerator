from sqlalchemy import Column, Integer, String, DateTime, Enum
from sqlalchemy.ext.declarative import declarative_base
from datetime import datetime
from enum import Enum as PyEnum

class UserStatus(PyEnum):
    ACTIVE = "ACTIVE"
    SUSPENDED = "SUSPENDED"

Base = declarative_base()

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, autoincrement=True)
    username = Column(String)
    status = Column(Enum(UserStatus), default=UserStatus.ACTIVE)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    def suspend(self):
        if self.status != UserStatus.ACTIVE:
            raise ValueError(f'Cannot suspend user in state: {self.status}')
        self.status = UserStatus.SUSPENDED

    def activate(self):
        if self.status != UserStatus.SUSPENDED:
            raise ValueError(f'Cannot activate user in state: {self.status}')
        self.status = UserStatus.ACTIVE
