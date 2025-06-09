package com.company.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConstantUtil {
    // Database
    public static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Company;encrypt=true;trustServerCertificate=true;";
    public static final String USER = "sa";
    public static final String PASSWORD = "sqlserver";

    // Sex
    public static final ObservableList<String> sexs = FXCollections.observableArrayList("男", "女");

    public static final ObservableList<String> searchList = FXCollections.observableArrayList(
            "按编号查询", "按性别查询", "按部门查询");
    public static final ObservableList<String> deptList = FXCollections.observableArrayList("生产部", "销售部", "财务部");
}
