CREATE SCHEMA IF NOT EXISTS testweb CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
USE testweb;

CREATE TABLE IF NOT EXISTS usuarios (
    ID INT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL, -- 60 caracteres para almacenar hash BCrypt
    PRIMARY KEY(ID)
);