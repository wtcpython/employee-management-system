package com.company;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

import com.company.controller.LoginController;
import com.company.pojo.Employee;
import com.company.pojo.User;
import com.company.service.EmployeeService;

/**
 * JavaFX App
 */
public class App extends Application {

    public static TableView<Employee> employeeTable = new TableView<>();
    public static EmployeeService employeeService = new EmployeeService();
    public static ObservableList<Employee> employees = FXCollections.observableArrayList();
    public static User currentUser = new User();

    @Override
    public void start(Stage stage) throws IOException {
        LoginController loginController = new LoginController(stage);
        Scene scene = new Scene(new StackPane(loginController.getView()));
        stage.setScene(scene);
        stage.setTitle("企业人员管理系统");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}