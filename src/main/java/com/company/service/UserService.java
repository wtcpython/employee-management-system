package com.company.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.company.pojo.User;
import com.company.util.DatabaseUtil;
import com.company.util.PasswordUtil;


public class UserService {

    // 登录验证：从数据库取用户信息，验证密码
    public boolean login(String name, String password) {
        String sql = "SELECT salt, hash FROM Users WHERE name = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String salt = rs.getString("salt");
                String hash = rs.getString("hash");
                User user = new User(name, salt, hash);
                return PasswordUtil.verifyPassword(password, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String name, String password) {
        if (userExists(name)) return false;

        try {
            User user = PasswordUtil.createUser(name, password);

            String sql = "INSERT INTO Users (name, salt, hash) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, user.getName());
                ps.setString(2, user.getSalt());
                ps.setString(3, user.getHash());

                return ps.executeUpdate() == 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean userExists(String name) {
        String sql = "SELECT 1 FROM Users WHERE name = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
