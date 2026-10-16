package com.codegym.dao;

import com.codegym.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    // ===== CẤU HÌNH KẾT NỐI =====
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "123456"; // ⚠️ Đổi thành password MySQL của bạn

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // ===== CÁC PHƯƠNG THỨC CRUD CƠ BẢN =====

    @Override
    public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, country) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());
            pstmt.executeUpdate();
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("country")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("country")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            rowDeleted = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        String sql = "UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());
            pstmt.setInt(4, user.getId());
            rowUpdated = pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }

    // ==========================================================
    // ⭐ PHƯƠNG THỨC CHÍNH CỦA BÀI — XỬ LÝ TRANSACTION
    // ==========================================================
    @Override
    public void addUserTransaction(User user, int[] permissionIds) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs = null;

        try {
            connection = getConnection();

            // 1. Tắt auto-commit để bắt đầu Transaction
            connection.setAutoCommit(false);

            // 2. Chèn User vào bảng users
            String insertUserSql = "INSERT INTO users (name, email, country) VALUES (?, ?, ?)";
            pstmtUser = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
            pstmtUser.setString(1, user.getName());
            pstmtUser.setString(2, user.getEmail());
            pstmtUser.setString(3, user.getCountry());
            pstmtUser.executeUpdate();

            // 3. Lấy ID vừa được sinh tự động
            rs = pstmtUser.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            // 4. Chèn permission vào bảng user_permission
            if (permissionIds != null && permissionIds.length > 0) {

                // ==========================================================
                // ⚠️⚠️⚠️ DÒNG CỐ TÌNH GÂY LỖI ĐỂ TEST ROLLBACK ⚠️⚠️⚠️
                // Bảng đúng là: user_permission
                // Đang cố tình viết sai thành: user_permissions (thêm chữ "s")
                // ==========================================================
                String insertPermissionSql = "INSERT INTO user_permissions (user_id, permission_id) VALUES (?, ?)";

                // ✅ KHI NỘP BÀI, SỬA LẠI DÒNG TRÊN THÀNH:
                // String insertPermissionSql = "INSERT INTO user_permission (user_id, permission_id) VALUES (?, ?)";

                pstmtAssignment = connection.prepareStatement(insertPermissionSql);

                for (int permissionId : permissionIds) {
                    pstmtAssignment.setInt(1, userId);
                    pstmtAssignment.setInt(2, permissionId);
                    pstmtAssignment.executeUpdate();
                }
            }

            // 5. Nếu mọi thứ thành công → Commit
            connection.commit();
            System.out.println("Transaction đã được commit thành công!");

        } catch (SQLException e) {
            // 6. Nếu có lỗi → Rollback toàn bộ
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Có lỗi xảy ra! Transaction đã bị rollback.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // 7. Dọn dẹp tài nguyên
            if (rs != null) rs.close();
            if (pstmtUser != null) pstmtUser.close();
            if (pstmtAssignment != null) pstmtAssignment.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}