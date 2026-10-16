-- ==========================================
-- Schema cho bài thực hành JDBC Transaction
-- ==========================================
USE demo;

-- Bảng users
CREATE TABLE IF NOT EXISTS users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL,
    country VARCHAR(120) NOT NULL,
    PRIMARY KEY (id)
);

-- Bảng permission
CREATE TABLE IF NOT EXISTS permission (
    id INT(11) PRIMARY KEY,
    name VARCHAR(50)
);

-- Bảng trung gian user_permission
CREATE TABLE IF NOT EXISTS user_permission (
    user_id INT(11),
    permission_id INT(11),
    PRIMARY KEY(user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

-- Dữ liệu mẫu cho permission
INSERT IGNORE INTO permission(id, name) VALUES (1, 'add');
INSERT IGNORE INTO permission(id, name) VALUES (2, 'edit');
INSERT IGNORE INTO permission(id, name) VALUES (3, 'delete');
INSERT IGNORE INTO permission(id, name) VALUES (4, 'view');