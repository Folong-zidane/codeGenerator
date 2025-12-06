from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from models.order import Order
from services.order_service import OrderService
from database import get_db

router = APIRouter(prefix='/api/orders', tags=['orders'])

@router.get('/', response_model=List[Order])
def get_all_orders(db: Session = Depends(get_db)):
    service = OrderService(db)
    return service.get_all()

@router.get('/{id}', response_model=Order)
def get_order(id: int, db: Session = Depends(get_db)):
    service = OrderService(db)
    entity = service.get_by_id(id)
    if not entity:
        raise HTTPException(status_code=404, detail='Order not found')
    return entity

@router.post('/', response_model=Order, status_code=201)
def create_order(entity: Order, db: Session = Depends(get_db)):
    service = OrderService(db)
    return service.create(entity)

@router.delete('/{id}', status_code=204)
def delete_order(id: int, db: Session = Depends(get_db)):
    service = OrderService(db)
    if not service.delete(id):
        raise HTTPException(status_code=404, detail='Order not found')

@router.patch('/{id}/suspend', response_model=Order)
def suspend_order(id: int, db: Session = Depends(get_db)):
    service = OrderService(db)
    try:
        return service.suspend_order(id)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.patch('/{id}/activate', response_model=Order)
def activate_order(id: int, db: Session = Depends(get_db)):
    service = OrderService(db)
    try:
        return service.activate_order(id)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
