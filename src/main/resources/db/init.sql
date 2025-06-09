-- Create a new database called 'Company'
-- Connect to the 'master' database to run this snippet
USE master
GO
-- Create the new database if it does not exist already
IF NOT EXISTS (
    SELECT name
FROM sys.databases
WHERE name = N'Company'
)
CREATE DATABASE Company
GO

USE Company
GO
DROP TABLE employees
CREATE TABLE employees
(
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    position NVARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    sex NVARCHAR(10) CHECK (sex IN ('男', '女')) NOT NULL,
    deptId INT DEFAULT 0,
    salary DECIMAL(10,2) CHECK (salary >= 0) DEFAULT 0.00,
    phoneNumber NVARCHAR(20),
    CONSTRAINT chk_birthday CHECK (birthday <= GETDATE())
);

CREATE INDEX idx_dept ON employees(deptId);

SELECT MAX(id) AS max_id FROM employees;

CREATE TABLE Users (
    name VARCHAR(255) PRIMARY KEY,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL
);
