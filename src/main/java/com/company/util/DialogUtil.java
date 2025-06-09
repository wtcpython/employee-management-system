package com.company.util;

import java.io.File;
import java.util.Optional;

import com.company.pojo.Employee;
import com.jfoenix.controls.JFXComboBox;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DialogUtil {
    public static Optional<Employee> showEmployeeDialog(Employee employee) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.initOwner(null);
        dialog.setTitle("员工信息录入");
        dialog.setHeaderText("请填写员工基本信息");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // 初始化表单控件
        TextField nameField = new TextField();
        TextField positionField = new TextField();
        JFXComboBox<String> genderBox = new JFXComboBox<>();
        genderBox.setPromptText("选择性别");
        DatePicker birthPicker = new DatePicker();
        JFXComboBox<String> deptIdField = new JFXComboBox<>();
        deptIdField.setItems(ConstantUtil.deptList);
        deptIdField.setPromptText("选择部门");
        TextField salaryField = new TextField();
        FormatterUtil.setDoubleFormatter(salaryField);
        TextField phoneNumberField = new TextField();
        FormatterUtil.setPhoneNumberFormatter(phoneNumberField);

        genderBox.setItems(ConstantUtil.sexs);
        birthPicker.setPromptText("选择出生日期");

        nameField.setText(employee.getName());
        positionField.setText(employee.getPosition());
        genderBox.setValue(employee.getSex());
        deptIdField.getSelectionModel().select(employee.getDeptId() - 1);
        birthPicker.setValue(employee.getBirthday());
        salaryField.setText(String.valueOf(employee.getSalary()));
        phoneNumberField.setText(employee.getPhoneNumber());

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(
                nameField.textProperty().isEmpty()
                        .or(positionField.textProperty().isEmpty())
                        .or(genderBox.valueProperty().isNull())
                        .or(birthPicker.valueProperty().isNull())
                        .or(deptIdField.valueProperty().isNull())
                        .or(salaryField.textProperty().isEmpty())
                        .or(phoneNumberField.textProperty().isEmpty()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("姓名:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("职位:"), 0, 1);
        grid.add(positionField, 1, 1);
        grid.add(new Label("性别:"), 0, 2);
        grid.add(genderBox, 1, 2);
        grid.add(new Label("出生日期:"), 0, 3);
        grid.add(birthPicker, 1, 3);
        grid.add(new Label("所属部门编号:"), 0, 4);
        grid.add(deptIdField, 1, 4);
        grid.add(new Label("工资:"), 0, 5);
        grid.add(salaryField, 1, 5);
        grid.add(new Label("电话号码:"), 0, 6);
        grid.add(phoneNumberField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Employee(
                        employee.getId(),
                        // dao.getNextId(),
                        nameField.getText(),
                        positionField.getText(),
                        birthPicker.getValue(),
                        genderBox.getValue(),
                        deptIdField.getSelectionModel().getSelectedIndex() + 1,
                        Double.parseDouble(salaryField.getText()),
                        phoneNumberField.getText());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    public static File fileSavePicker() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel 文件", "*.xlsx"));
        fileChooser.setInitialFileName("export.xlsx");
        File file = fileChooser.showSaveDialog(new Stage());
        return file;
    }

    public static File fileOpenPicker() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel 文件", "*.xlsx"));
        fileChooser.setInitialFileName("export.xlsx");
        File file = fileChooser.showOpenDialog(new Stage());
        return file;
    }

    public static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
