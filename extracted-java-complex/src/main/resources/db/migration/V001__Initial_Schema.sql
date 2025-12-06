-- ============================================
-- Flyway Migration: V001__Initial_Schema.sql
-- ============================================
-- Description: Create initial database schema
-- Date: 2025-12-02 03:55:04
-- Version: 1.0.0
-- ============================================

-- Set charset for database compatibility
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET COLLATION_CONNECTION = utf8mb4_unicode_ci;

-- ============================================
-- Table: entity (Entity)
-- ============================================
CREATE TABLE IF NOT EXISTS entity (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    createdAt TIMESTAMP COMMENT 'createdAt field',
    updatedAt TIMESTAMP COMMENT 'updatedAt field',
    Long VARCHAR(255) COMMENT 'Long field',
    LocalDateTime VARCHAR(255) COMMENT 'LocalDateTime field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Entity entity table';

CREATE INDEX IF NOT EXISTS idx_entity_created_at ON entity (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_entity_status ON entity (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: user (User)
-- ============================================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    username VARCHAR(255) NOT NULL UNIQUE COMMENT 'username field',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'email field',
    passwordHash VARCHAR(255) NOT NULL COMMENT 'passwordHash field',
    role VARCHAR(255) COMMENT 'role field',
    lastLogin TIMESTAMP COMMENT 'lastLogin field',
    active VARCHAR(255) COMMENT 'active field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    void VARCHAR(255) COMMENT 'void field',
    List~Order~ VARCHAR(255) COMMENT 'List~Order~ field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User entity table';

CREATE INDEX IF NOT EXISTS idx_user_created_at ON user (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_user_status ON user (status) COMMENT 'Index for status filtering';
CREATE INDEX IF NOT EXISTS idx_user_email ON user (email) COMMENT 'Index for email lookups';
CREATE INDEX IF NOT EXISTS idx_user_username ON user (username) COMMENT 'Index for username lookups';

-- ============================================
-- Table: order (Order)
-- ============================================
CREATE TABLE IF NOT EXISTS order (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    orderNumber VARCHAR(255) NOT NULL COMMENT 'orderNumber field',
    status VARCHAR(255) COMMENT 'status field',
    orderDate TIMESTAMP COMMENT 'orderDate field',
    totalAmount VARCHAR(255) COMMENT 'totalAmount field',
    shippingAddress VARCHAR(255) NOT NULL COMMENT 'shippingAddress field',
    BigDecimal VARCHAR(255) COMMENT 'BigDecimal field',
    void VARCHAR(255) COMMENT 'void field',
    List~OrderItem~ VARCHAR(255) COMMENT 'List~OrderItem~ field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Order entity table';

CREATE INDEX IF NOT EXISTS idx_order_created_at ON order (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_order_status ON order (status) COMMENT 'Index for status filtering';
CREATE INDEX IF NOT EXISTS idx_order_status_created_at ON order (status, created_at) COMMENT 'Composite index for filtering by status and date';

-- ============================================
-- Table: product (Product)
-- ============================================
CREATE TABLE IF NOT EXISTS product (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    name VARCHAR(255) NOT NULL COMMENT 'name field',
    description VARCHAR(255) NOT NULL COMMENT 'description field',
    price VARCHAR(255) COMMENT 'price field',
    quantity VARCHAR(255) COMMENT 'quantity field',
    sku VARCHAR(255) NOT NULL COMMENT 'sku field',
    category VARCHAR(255) COMMENT 'category field',
    reviews VARCHAR(255) COMMENT 'reviews field',
    void VARCHAR(255) COMMENT 'void field',
    void VARCHAR(255) COMMENT 'void field',
    Double VARCHAR(255) COMMENT 'Double field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product entity table';

CREATE INDEX IF NOT EXISTS idx_product_created_at ON product (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_product_status ON product (status) COMMENT 'Index for status filtering';
CREATE FULLTEXT INDEX IF NOT EXISTS idx_product_name ON product (name) COMMENT 'Fulltext index for name search';

-- ============================================
-- Table: order_item (OrderItem)
-- ============================================
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    quantity VARCHAR(255) COMMENT 'quantity field',
    unitPrice VARCHAR(255) COMMENT 'unitPrice field',
    lineTotal VARCHAR(255) COMMENT 'lineTotal field',
    BigDecimal VARCHAR(255) COMMENT 'BigDecimal field',
    Product VARCHAR(255) COMMENT 'Product field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OrderItem entity table';

CREATE INDEX IF NOT EXISTS idx_order_item_created_at ON order_item (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_order_item_status ON order_item (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: review (Review)
-- ============================================
CREATE TABLE IF NOT EXISTS review (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    rating VARCHAR(255) COMMENT 'rating field',
    comment VARCHAR(255) NOT NULL COMMENT 'comment field',
    author VARCHAR(255) COMMENT 'author field',
    helpfulCount VARCHAR(255) COMMENT 'helpfulCount field',
    int VARCHAR(255) COMMENT 'int field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Review entity table';

CREATE INDEX IF NOT EXISTS idx_review_created_at ON review (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_review_status ON review (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: payment (Payment)
-- ============================================
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    boolean VARCHAR(255) COMMENT 'boolean field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    PaymentStatus VARCHAR(255) COMMENT 'PaymentStatus field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Payment entity table';

CREATE INDEX IF NOT EXISTS idx_payment_created_at ON payment (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_payment_status ON payment (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: credit_card_payment (CreditCardPayment)
-- ============================================
CREATE TABLE IF NOT EXISTS credit_card_payment (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    cardNumber VARCHAR(255) NOT NULL COMMENT 'cardNumber field',
    expiryDate VARCHAR(255) NOT NULL COMMENT 'expiryDate field',
    cvv VARCHAR(255) NOT NULL COMMENT 'cvv field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CreditCardPayment entity table';

CREATE INDEX IF NOT EXISTS idx_credit_card_payment_created_at ON credit_card_payment (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_credit_card_payment_status ON credit_card_payment (status) COMMENT 'Index for status filtering';

-- ============================================
-- Table: pay_pal_payment (PayPalPayment)
-- ============================================
CREATE TABLE IF NOT EXISTS pay_pal_payment (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'email field',
    transactionId VARCHAR(255) NOT NULL COMMENT 'transactionId field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    boolean VARCHAR(255) COMMENT 'boolean field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PayPalPayment entity table';

CREATE INDEX IF NOT EXISTS idx_pay_pal_payment_created_at ON pay_pal_payment (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_pay_pal_payment_status ON pay_pal_payment (status) COMMENT 'Index for status filtering';
CREATE INDEX IF NOT EXISTS idx_pay_pal_payment_email ON pay_pal_payment (email) COMMENT 'Index for email lookups';

-- ============================================
-- Table: inventory (Inventory)
-- ============================================
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary Key',
    Product~ products VARCHAR(255) COMMENT 'Product~ products field',
    void VARCHAR(255) COMMENT 'void field',
    void VARCHAR(255) COMMENT 'void field',
    Product VARCHAR(255) COMMENT 'Product field',
    quantity) void VARCHAR(255) COMMENT 'quantity) void field',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Entity status (ACTIVE, INACTIVE, SUSPENDED)',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    version BIGINT NOT NULL DEFAULT 0 COMMENT 'Optimistic locking version'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Inventory entity table';

CREATE INDEX IF NOT EXISTS idx_inventory_created_at ON inventory (created_at) COMMENT 'Index for time-based queries';
CREATE INDEX IF NOT EXISTS idx_inventory_status ON inventory (status) COMMENT 'Index for status filtering';

