CREATE SCHEMA IF NOT EXISTS testweb CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
USE testweb;

CREATE TABLE IF NOT EXISTS usuarios (
    ID INT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL, -- Hash de BCrypt tiene 60 caracteres
    PRIMARY KEY(ID)
);
