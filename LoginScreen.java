package personalexpensetracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {
    private Stage primaryStage;
    private String currentUserId;

    public LoginScreen(Stage stage) {
        this.primaryStage = stage;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void show() {
        // Title label
        Label titleLabel = new Label("Personal Expense Tracker");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.TEAL);

        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.GRAY);

       
        Label userLabel = new Label("Username:");
        userLabel.setFont(Font.font("Arial", 14));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(250);
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

       
        Label passLabel = new Label("Password:");
        passLabel.setFont(Font.font("Arial", 14));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(250);
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(250);
        loginButton.setStyle(
            "-fx-background-color: #008B8B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 10px;" +
            "-fx-cursor: hand;"
        );

       
        Hyperlink registerLink = new Hyperlink("New user? Register here");
        registerLink.setTextFill(Color.TEAL);
        registerLink.setStyle("-fx-font-size: 12px;");

        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields!");
                return;
            }

            String userId = JsonDataManager.loginUser(username, password);
            if (userId != null) {
                currentUserId = userId;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome " + username + "!");
                DashboardScreen dashboard = new DashboardScreen(primaryStage, currentUserId);
                dashboard.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password!");
            }
        });

       
        registerLink.setOnAction(e -> {
            showRegisterScreen();
        });

       
        VBox formBox = new VBox(10, userLabel, usernameField, passLabel, passwordField, loginButton, registerLink);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        VBox root = new VBox(20, titleLabel, subtitleLabel, formBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F0FFFF;");

        Scene scene = new Scene(root, 450, 450);
        primaryStage.setTitle("Personal Expense Tracker - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showRegisterScreen() {
        Label titleLabel = new Label("Create New Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.TEAL);

        Label userLabel = new Label("Username:");
        userLabel.setFont(Font.font("Arial", 14));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label passLabel = new Label("Password:");
        passLabel.setFont(Font.font("Arial", 14));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label confirmLabel = new Label("Confirm Password:");
        confirmLabel.setFont(Font.font("Arial", 14));
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Re-enter password");
        confirmField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(250);
        registerButton.setStyle(
            "-fx-background-color: #008B8B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-padding: 10px;" +
            "-fx-cursor: hand;"
        );

        Button backButton = new Button("Back to Login");
        backButton.setPrefWidth(250);
        backButton.setStyle(
            "-fx-background-color: #5F9EA0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8px;" +
            "-fx-cursor: hand;"
        );

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirm = confirmField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields!");
                return;
            }

            if (!password.equals(confirm)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                return;
            }

            if (JsonDataManager.registerUser(username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created! Please login.");
                show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Username already exists!");
            }
        });

        backButton.setOnAction(e -> {
            show();
        });

        VBox formBox = new VBox(10, userLabel, usernameField, passLabel, passwordField, confirmLabel, confirmField, registerButton, backButton);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        VBox root = new VBox(20, titleLabel, formBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F0FFFF;");

        Scene scene = new Scene(root, 450, 500);
        primaryStage.setScene(scene);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
