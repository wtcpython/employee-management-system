package com.company.service;

import com.company.pojo.Employee;
import com.company.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    // 添加员工
    public int addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, position, birthday, sex, deptId, salary, phoneNumber) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setEmployeeParameters(pstmt, employee);
            int affectedRows = pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // 返回生成的ID
                }
            }
            return affectedRows;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 修改员工信息
    public boolean modifyEmployee(Employee employee) {
        String sql = "UPDATE employees SET name=?, position=?, birthday=?, sex=?, "
                + "deptId=?, salary=?, phoneNumber=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setEmployeeParameters(pstmt, employee);
            pstmt.setInt(8, employee.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            ;
            return false;
        }
    }

    // 删除员工
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 按编号查询员工信息
    public List<Employee> getEmployeesByID(Integer id) {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            Employee emp = new Employee();
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // 提取数据
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setPosition(rs.getString("position"));
                emp.setBirthday(rs.getDate("birthday").toLocalDate());
                emp.setSex(rs.getString("sex"));
                emp.setDeptId(rs.getInt("deptId"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setPhoneNumber(rs.getString("phoneNumber"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    // 按性别查询
    public List<Employee> getEmployeesByGender(String sex) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE sex = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sex);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setPosition(rs.getString("position"));
                emp.setBirthday(rs.getDate("birthday").toLocalDate());
                emp.setSex(rs.getString("sex"));
                emp.setDeptId(rs.getInt("deptId"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setPhoneNumber(rs.getString("phoneNumber"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // 按部门查询
    public List<Employee> getEmployeesDeptId(Integer deptId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE deptId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deptId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setPosition(rs.getString("position"));
                emp.setBirthday(rs.getDate("birthday").toLocalDate());
                emp.setSex(rs.getString("sex"));
                emp.setDeptId(rs.getInt("deptId"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setPhoneNumber(rs.getString("phoneNumber"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // 获取所有员工
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // 获取下一个可用ID（基于当前最大值+1）
    public int getNextId() {
        String sql = "SELECT MAX(id) AS max_id FROM employees";

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt("max_id") + 1 : 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 公共参数设置方法
    private void setEmployeeParameters(PreparedStatement pstmt, Employee employee)
            throws SQLException {
        pstmt.setString(1, employee.getName());
        pstmt.setString(2, employee.getPosition());
        pstmt.setDate(3, Date.valueOf(employee.getBirthday()));
        pstmt.setString(4, employee.getSex());
        pstmt.setInt(5, employee.getDeptId());
        pstmt.setDouble(6, employee.getSalary());
        pstmt.setString(7, employee.getPhoneNumber());
    }

    // 结果集映射方法
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setPosition(rs.getString("position"));

        Date birthday = rs.getDate("birthday");
        emp.setBirthday(birthday != null ? birthday.toLocalDate() : null);

        emp.setSex(rs.getString("sex"));
        emp.setDeptId(rs.getInt("deptId"));
        emp.setSalary(rs.getDouble("salary"));
        emp.setPhoneNumber(rs.getString("phoneNumber"));
        return emp;
    }
}