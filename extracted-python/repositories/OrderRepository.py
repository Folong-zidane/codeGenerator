# Repository pattern implementation for Order
from sqlalchemy.orm import Session
from typing import List, Optional
from models.order import Order

class OrderRepository:
    def __init__(self, db: Session):
        self.db = db

    def find_all(self) -> List[Order]:
        return self.db.query(Order).all()

    def find_by_id(self, id: int) -> Optional[Order]:
        return self.db.query(Order).filter(Order.id == id).first()

    def save(self, entity: Order) -> Order:
        self.db.add(entity)
        self.db.commit()
        self.db.refresh(entity)
        return entity

    def delete_by_id(self, id: int) -> bool:
        entity = self.find_by_id(id)
        if entity:
            self.db.delete(entity)
            self.db.commit()
            return True
        return False
