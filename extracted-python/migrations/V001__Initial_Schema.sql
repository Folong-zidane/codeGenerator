# Database migration for ecommerce
from alembic import op
import sqlalchemy as sa

def upgrade():
    # Create User table
    op.create_table('users',
        sa.Column('id', sa.Integer(), primary_key=True, autoincrement=True),
        sa.Column('status', sa.String(50), default='ACTIVE'),
        sa.Column('created_at', sa.DateTime(), default=sa.func.now()),
        sa.Column('updated_at', sa.DateTime(), default=sa.func.now(), onupdate=sa.func.now())
    )

    # Create Order table
    op.create_table('orders',
        sa.Column('id', sa.Integer(), primary_key=True, autoincrement=True),
        sa.Column('status', sa.String(50), default='ACTIVE'),
        sa.Column('created_at', sa.DateTime(), default=sa.func.now()),
        sa.Column('updated_at', sa.DateTime(), default=sa.func.now(), onupdate=sa.func.now())
    )

def downgrade():
    op.drop_table('users')
    op.drop_table('orders')
