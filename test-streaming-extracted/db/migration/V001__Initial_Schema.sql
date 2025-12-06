-- Generated database migration
-- Package: com.test.fixed

CREATE TABLE products (
    id VARCHAR(255) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

