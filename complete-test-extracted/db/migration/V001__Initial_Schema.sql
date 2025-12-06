-- Generated database migration
-- Package: com.test.complete

CREATE TABLE orders (
    id VARCHAR(255) PRIMARY KEY AUTO_INCREMENT,
    customerEmail VARCHAR(255),
    total DECIMAL(10,2),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

