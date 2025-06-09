package com.company.controller;

import com.company.App;
import com.company.service.UserService;
import com.company.util.DialogUtil;
import com.jfoenix.controls.JFXButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginController {

    private BorderPane root;

    private UserService userService;
    private StackPane stackPane;
    private VBox loginContent;
    private VBox registerContent;

    public LoginController(Stage stage) {
        userService = new UserService();
        root = new BorderPane();
        root.setPrefSize(400, 580);
        root.setPadding(new Insets(10));

        stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(stackPane);

        buildLoginContent(stage);
        buildRegisterContent(stage);

        stackPane.getChildren().add(loginContent); // 默认显示登录界面
    }

    // 构建登录内容
    public void buildLoginContent(Stage stage) {
        Image backgroundImage = new Image(getClass().getResource("/img/background.png").toExternalForm());
        Background background = new Background(new javafx.scene.layout.BackgroundImage(backgroundImage,
                javafx.scene.layout.BackgroundRepeat.NO_REPEAT, javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                javafx.scene.layout.BackgroundPosition.CENTER, null));
        root.setBackground(background);

        TextField nameField = new TextField();
        nameField.setPromptText("昵称");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("密码");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("昵称:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.setAlignment(Pos.CENTER);

        JFXButton loginButton = new JFXButton("登录");
        loginButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText();

            if (name.isEmpty() || password.isEmpty()) {
                DialogUtil.showAlert("请输入昵称和密码", Alert.AlertType.WARNING);
                return;
            }

            if (userService.login(name, password)) {
                DialogUtil.showAlert("登录成功", Alert.AlertType.INFORMATION);
                App.currentUser.setName(name);
                showMainController(stage);
            } else {
                DialogUtil.showAlert("登录失败，昵称或密码错误", Alert.AlertType.ERROR);
                nameField.clear();
                passwordField.clear();
            }
        });

        JFXButton registerButton = new JFXButton("注册");
        registerButton.setOnAction(e -> switchToRegister());

        HBox buttonBox = new HBox(10, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);

        loginContent = new VBox(10, grid, buttonBox);
        loginContent.setAlignment(Pos.CENTER);
    }

    // 构建注册内容
    public void buildRegisterContent(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("请输入昵称");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("请确认密码");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("昵称:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("确认密码:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.setAlignment(Pos.CENTER);

        JFXButton registerButton = new JFXButton("注册");
        registerButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                DialogUtil.showAlert("请输入所有字段", Alert.AlertType.WARNING);
                return;
            }

            if (!password.equals(confirmPassword)) {
                DialogUtil.showAlert("密码和确认密码不匹配", Alert.AlertType.WARNING);
                return;
            }

            if (userService.register(name, password)) {
                DialogUtil.showAlert("注册成功", Alert.AlertType.INFORMATION);
                App.currentUser.setName(name);
                showMainController(stage);
            } else {
                DialogUtil.showAlert("注册失败，昵称已存在", Alert.AlertType.ERROR);
                nameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
            }
        });

        JFXButton loginButton = new JFXButton("返回登录");
        loginButton.setOnAction(e -> switchToLogin());

        HBox buttonBox = new HBox(10, registerButton, loginButton);
        buttonBox.setAlignment(Pos.CENTER);

        registerContent = new VBox(10, grid, buttonBox);
        registerContent.setAlignment(Pos.CENTER);
    }

    // 切换到登录界面
    private void switchToLogin() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(loginContent);
    }

    // 切换到注册界面
    private void switchToRegister() {
        stackPane.getChildren().clear();
        stackPane.getChildren().add(registerContent);
    }

    // 展示主界面
    public static void showMainController(Stage stage) {
        MainController controller = new MainController();
        Scene scene = new Scene(controller.getView());
        stage.setScene(scene);
    }

    public BorderPane getView() {
        return root;
    }
}
