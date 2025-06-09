package com.company.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

import com.company.App;
import com.company.pojo.Employee;
import com.company.util.ConstantUtil;
import com.company.util.DialogUtil;
import com.company.util.ExcelUtil;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;

public class MainController {

    private BorderPane root;

    private SearchController searchController;

    public MainController() {
        searchController = new SearchController();
        root = new BorderPane();

        root.setCenter(buildMainContent());
        root.setTop(buildMenuBar());
        root.setLeft(searchController.buildSearchbar());

        loadInitialData();
    }

    private void loadInitialData() {
        App.employees.setAll(App.employeeService.getAllEmployees());
        App.employeeTable.sort();
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("文件");
        MenuItem exportItem = new MenuItem("导出 Excel");
        MenuItem importItem = new MenuItem("导入 Excel");
        MenuItem exitItem = new MenuItem("退出系统");
        exportItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        importItem.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        exportItem.setOnAction(e -> {
            File file = DialogUtil.fileSavePicker();
            if (file != null) {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        ExcelUtil.exportToExcel(App.employeeTable, file);
                        return null;
                    }
                };
                task.setOnFailed(ev -> task.getException().printStackTrace());
                new Thread(task).start();
            }
        });

        importItem.setOnAction(e -> {
            File file = DialogUtil.fileOpenPicker();
            if (file != null) {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        ObservableList<Employee> list = ExcelUtil.importFromExcel(file);
                        for (Employee employee : list) {
                            App.employeeService.addEmployee(employee);
                        }
                        // UI 更新需在FX线程
                        Platform.runLater(() -> refresh());
                        return null;
                    }
                };
                task.setOnFailed(ev -> task.getException().printStackTrace());
                new Thread(task).start();
            }
        });

        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(exportItem, importItem, exitItem);

        Menu helpMenu = new Menu("帮助");
        MenuItem aboutItem = new MenuItem("关于");

        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("关于");
            alert.setHeaderText("企业人员管理系统");
            alert.setContentText("版本 1.0\n作者: 王添成，王宇雄");
            alert.showAndWait();
        });
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private BorderPane buildMainContent() {
        TableColumn<Employee, Integer> idCol = createTableColumn("ID", "id");
        TableColumn<Employee, String> nameCol = createTableColumn("姓名", "name");
        TableColumn<Employee, String> positionCol = createTableColumn("职位", "position");
        TableColumn<Employee, LocalDate> birthdayCol = createTableColumn("出生日期", "birthday");
        TableColumn<Employee, String> sexCol = createTableColumn("性别", "sex");
        TableColumn<Employee, Integer> deptIdCol = createTableColumn("部门", "deptId");
        deptIdCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }

                setText(item > ConstantUtil.deptList.size() || item <= 0 ? "未知部门"
                        : ConstantUtil.deptList.get(item - 1));
            }
        });
        TableColumn<Employee, Double> salaryCol = createTableColumn("工资", "salary");
        TableColumn<Employee, String> phoneCol = createTableColumn("电话号码", "phoneNumber");
        TableColumn<Employee, Void> actionCol = createActionColumn();

        App.employeeTable.getColumns().addAll(
                idCol, nameCol, positionCol, birthdayCol, sexCol, deptIdCol, salaryCol, phoneCol, actionCol);
        App.employeeTable.setItems(App.employees);

        BorderPane content = new BorderPane();
        content.setCenter(App.employeeTable);
        content.setBottom(buildActionButtons());
        content.setPadding(new Insets(10));
        return content;
    }

    private <T> TableColumn<Employee, T> createTableColumn(String title, String property) {
        TableColumn<Employee, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private TableColumn<Employee, Void> createActionColumn() {
        TableColumn<Employee, Void> actionCol = new TableColumn<>("操作");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("编辑");
            private final Button deleteButton = new Button("删除");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                buttons.setAlignment(Pos.CENTER);
                editButton.setOnAction(e -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
        return actionCol;
    }

    private HBox buildActionButtons() {
        JFXButton addButton = new JFXButton("添加员工");
        JFXButton refreshButton = new JFXButton("刷新数据");

        addButton.setOnAction(e -> showAddEmployeeDialog());
        refreshButton.setOnAction(e -> refresh());

        HBox buttonBar = new HBox(10, addButton, refreshButton);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));
        return buttonBar;
    }

    private void showAddEmployeeDialog() {
        Employee newEmployee = new Employee();
        newEmployee.setId(App.employeeService.getNextId());

        Optional<Employee> result = DialogUtil.showEmployeeDialog(newEmployee);
        result.ifPresent(employee -> {
            System.out.println(employee);
            App.employeeService.addEmployee(employee);
            refresh();
        });
    }

    private void handleEdit(Employee employee) {
        Optional<Employee> result = DialogUtil.showEmployeeDialog(employee);
        result.ifPresent(updatedEmployee -> updateEmployee(updatedEmployee));
    }

    private void updateEmployee(Employee employee) {
        App.employeeService.modifyEmployee(employee);
        refresh();
    }

    private void handleDelete(Employee employee) {
        App.employeeService.deleteEmployee(employee.getId());
        refresh();
    }

    private void refresh() {
        App.employees.setAll(App.employeeService.getAllEmployees());
        App.employeeTable.sort();
        App.employeeTable.refresh();
    }

    public BorderPane getView() {
        return root;
    }
}