# Repository pattern implementation for User
from sqlalchemy.orm import Session
from typing import List, Optional
from models.user import User

class UserRepository:
    def __init__(self, db: Session):
        self.db = db

    def find_all(self) -> List[User]:
        return self.db.query(User).all()

    def find_by_id(self, id: int) -> Optional[User]:
        return self.db.query(User).filter(User.id == id).first()

    def save(self, entity: User) -> User:
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
