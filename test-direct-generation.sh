#!/bin/bash

echo "🔧 Test de génération directe des fichiers Python"

# Créer le dossier de sortie
mkdir -p generated/python-test

# Générer un fichier Python simple
cat > generated/python-test/user.py << 'EOF'
from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class User(Base):
    __tablename__ = 'users'
    
    id = Column(Integer, primary_key=True)
    name = Column(String(255), nullable=False)
    email = Column(String(255), unique=True, nullable=False)
    
    def __repr__(self):
        return f"<User(id={self.id}, name='{self.name}', email='{self.email}')>"
EOF

# Générer un contrôleur FastAPI
cat > generated/python-test/user_controller.py << 'EOF'
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from .user import User
from .database import get_db

router = APIRouter(prefix='/api/users', tags=['users'])

@router.get('/', response_model=List[User])
def get_all_users(db: Session = Depends(get_db)):
    return db.query(User).all()

@router.get('/{id}', response_model=User)
def get_user(id: int, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.id == id).first()
    if not user:
        raise HTTPException(status_code=404, detail='User not found')
    return user

@router.post('/', response_model=User, status_code=201)
def create_user(user: User, db: Session = Depends(get_db)):
    db.add(user)
    db.commit()
    db.refresh(user)
    return user
EOF

# Générer un README
cat > generated/python-test/README.md << 'EOF'
# Python FastAPI Project

## Generated Files

- `user.py` - SQLAlchemy User model
- `user_controller.py` - FastAPI router for User endpoints

## Usage

```bash
pip install fastapi sqlalchemy
uvicorn main:app --reload
```

## Endpoints

- GET /api/users/ - List all users
- GET /api/users/{id} - Get user by ID
- POST /api/users/ - Create new user
EOF

echo "✅ Fichiers Python générés dans: generated/python-test/"
ls -la generated/python-test/