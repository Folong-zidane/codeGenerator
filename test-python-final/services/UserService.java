from sqlalchemy.orm import Session
from typing import List, Optional
from models.user import User

class UserService:
    def __init__(self, db: Session):
        self.db = db

    def get_all(self) -> List[User]:
        return self.db.query(User).all()

    def get_by_id(self, id: int) -> Optional[User]:
        return self.db.query(User).filter(User.id == id).first()

    def create(self, entity: User) -> User:
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

    def suspend_user(self, id: int) -> User:
        entity = self.get_by_id(id)
        if not entity:
            raise ValueError(f'User not found with id: {id}')
        entity.suspend()
        self.db.commit()
        return entity

    def activate_user(self, id: int) -> User:
        entity = self.get_by_id(id)
        if not entity:
            raise ValueError(f'User not found with id: {id}')
        entity.activate()
        self.db.commit()
        return entity
