from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from models.user import User
from services.user_service import UserService
from database import get_db

router = APIRouter(prefix='/api/users', tags=['users'])

@router.get('/', response_model=List[User])
def get_all_users(db: Session = Depends(get_db)):
    service = UserService(db)
    return service.get_all()

@router.get('/{id}', response_model=User)
def get_user(id: int, db: Session = Depends(get_db)):
    service = UserService(db)
    entity = service.get_by_id(id)
    if not entity:
        raise HTTPException(status_code=404, detail='User not found')
    return entity

@router.post('/', response_model=User, status_code=201)
def create_user(entity: User, db: Session = Depends(get_db)):
    service = UserService(db)
    return service.create(entity)

@router.delete('/{id}', status_code=204)
def delete_user(id: int, db: Session = Depends(get_db)):
    service = UserService(db)
    if not service.delete(id):
        raise HTTPException(status_code=404, detail='User not found')

@router.patch('/{id}/suspend', response_model=User)
def suspend_user(id: int, db: Session = Depends(get_db)):
    service = UserService(db)
    try:
        return service.suspend_user(id)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.patch('/{id}/activate', response_model=User)
def activate_user(id: int, db: Session = Depends(get_db)):
    service = UserService(db)
    try:
        return service.activate_user(id)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
