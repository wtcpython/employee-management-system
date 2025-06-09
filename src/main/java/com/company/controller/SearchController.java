package com.company.controller;

import com.company.App;
import com.company.util.ConstantUtil;
import com.company.util.FormatterUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SearchController {
    private TextField numberField = buildNumberField();
    private JFXComboBox<String> deptBox = buildDeptBox();
    private JFXComboBox<String> genderBox = buildGenderBox();

    public SearchController() {
    }

    public VBox buildSearchbar() {
        Label welcomeLabel = new Label("欢迎：" + App.currentUser.getName());

        JFXComboBox<String> searchBox = new JFXComboBox<>();
        searchBox.setItems(ConstantUtil.searchList);
        searchBox.setPromptText("请选择查询方式");
        searchBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateContainer(observable, oldValue, newValue);
                });

        hideElement();

        JFXButton searchButton = new JFXButton("搜索");
        searchButton.setOnAction(e -> {
            if (searchBox.getSelectionModel().getSelectedIndex() >= 0) {
                executeSearch(searchBox.getValue());
            }
        });

        VBox searchBar = new VBox(10, welcomeLabel, searchBox, numberField, deptBox, genderBox, searchButton);
        searchBar.setPadding(new Insets(10));
        return searchBar;
    }

    private void hideElement() {
        numberField.setVisible(false);
        numberField.setManaged(false);
        deptBox.setVisible(false);
        deptBox.setManaged(false);
        genderBox.setVisible(false);
        genderBox.setManaged(false);
    }

    // 编号查询框
    private TextField buildNumberField() {
        TextField field = new TextField();
        FormatterUtil.setDoubleFormatter(field);
        field.setPromptText("请输入要查询的编号：");
        return field;
    }

    // 部门选择框
    private JFXComboBox<String> buildDeptBox() {
        JFXComboBox<String> box = new JFXComboBox<>();
        box.setItems(ConstantUtil.deptList);
        box.setPromptText("选择部门");
        return box;
    }

    // 性别选择框
    private JFXComboBox<String> buildGenderBox() {
        JFXComboBox<String> box = new JFXComboBox<>();
        box.setItems(FXCollections.observableArrayList("男", "女"));
        box.setPromptText("选择性别");
        return box;
    }

    private void updateContainer(ObservableValue<? extends Number> observable, Number oldValue,
            Number newValue) {
        hideElement();
        switch (newValue.intValue()) {
            case 0:
                numberField.setVisible(true);
                numberField.setManaged(true);
                break;
            case 1:
                genderBox.setVisible(true);
                genderBox.setManaged(true);
                break;
            case 2:
                deptBox.setVisible(true);
                deptBox.setManaged(true);
                break;
            default:
                break;
        }
    }

    private void executeSearch(String type) {
        // ObservableList<Employee> employees = FXCollections.observableArrayList();
        switch (type) {
            case "按编号查询":
                App.employees.setAll(
                        App.employeeService.getEmployeesByID(Integer.parseInt(numberField.getText())));
                break;
            case "按性别查询":
                App.employees
                        .setAll(App.employeeService.getEmployeesByGender(genderBox.getValue()));
                break;
            case "按部门查询":
                Integer index = ConstantUtil.deptList.indexOf(deptBox.getValue()) + 1;
                if (index > 0) {
                    App.employees.setAll(App.employeeService.getEmployeesDeptId(index));
                }
            default:
                break;
        }
        App.employeeTable.setItems(App.employees);
    }

}
