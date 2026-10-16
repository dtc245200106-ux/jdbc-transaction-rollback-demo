package com.codegym.dao;

import com.codegym.model.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    // Các phương thức CRUD cơ bản
    void insertUser(User user) throws SQLException;
    User selectUser(int id);
    List<User> selectAllUsers();
    boolean deleteUser(int id);
    boolean updateUser(User user);

    // Phương thức xử lý Transaction
    void addUserTransaction(User user, int[] permissionIds) throws SQLException;
}