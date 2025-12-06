from sqlalchemy.orm import Session
from typing import List, Optional
from models.order import Order

class OrderService:
    def __init__(self, db: Session):
        self.db = db

    def get_all(self) -> List[Order]:
        return self.db.query(Order).all()

    def get_by_id(self, id: int) -> Optional[Order]:
        return self.db.query(Order).filter(Order.id == id).first()

    def create(self, entity: Order) -> Order:
        self.db.add(entity)
        self.db.commit()
        self.db.refresh(entity)
        return entity

    def delete(self, id: int) -> bool:
        entity = self.get_by_id(id)
        if entity:
            self.db.delete(entity)
            self.db.commit()
            return True
        return False

    def suspend_order(self, id: int) -> Order:
        entity = self.get_by_id(id)
        if not entity:
            raise ValueError(f'Order not found with id: {id}')
        entity.suspend()
        self.db.commit()
        return entity

    def activate_order(self, id: int) -> Order:
        entity = self.get_by_id(id)
        if not entity:
            raise ValueError(f'Order not found with id: {id}')
        entity.activate()
        self.db.commit()
        return entity
