-- Generated database migration
-- Package: com.test

CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

