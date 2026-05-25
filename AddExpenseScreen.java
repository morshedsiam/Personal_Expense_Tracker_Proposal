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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddExpenseScreen {
    private Stage primaryStage;
    private String userId;
    private DashboardScreen dashboard;

    public AddExpenseScreen(Stage stage, String userId, DashboardScreen dashboard) {
        this.primaryStage = stage;
        this.userId = userId;
        this.dashboard = dashboard;
    }

    public void show() {
        // Title
        Label titleLabel = new Label("Add New Expense");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.TEAL);

        
        Label amountLabel = new Label("Amount ($):");
        amountLabel.setFont(Font.font("Arial", 14));
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        amountField.setPrefWidth(300);

        
        Label categoryLabel = new Label("Category:");
        categoryLabel.setFont(Font.font("Arial", 14));
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll(
            "Vehicle",
            "Shopping",
            "Food",
            "Electric Bill",
            "Health Insurance"
        );
        categoryCombo.setPromptText("Select category");
        categoryCombo.setStyle("-fx-font-size: 14px; -fx-padding: 4px;");
        categoryCombo.setPrefWidth(300);

        // Date picker
        Label dateLabel = new Label("Date:");
        dateLabel.setFont(Font.font("Arial", 14));
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setStyle("-fx-font-size: 14px;");
        datePicker.setPrefWidth(300);

        // Description field
        Label descLabel = new Label("Description (Optional):");
        descLabel.setFont(Font.font("Arial", 14));
        TextField descField = new TextField();
        descField.setPromptText("Enter description");
        descField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        descField.setPrefWidth(300);

        // Buttons
        Button saveButton = new Button("Save Expense");
        saveButton.setPrefWidth(150);
        saveButton.setStyle(
            "-fx-background-color: #008B8B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px;" +
            "-fx-cursor: hand;"
        );

        Button backButton = new Button("Back to Dashboard");
        backButton.setPrefWidth(150);
        backButton.setStyle(
            "-fx-background-color: #5F9EA0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px;" +
            "-fx-cursor: hand;"
        );

        HBox buttonsBox = new HBox(15, saveButton, backButton);
        buttonsBox.setAlignment(Pos.CENTER);

        // Save action
        saveButton.setOnAction(e -> {
            String amountText = amountField.getText().trim();
            String category = categoryCombo.getValue();
            LocalDate date = datePicker.getValue();
            String description = descField.getText().trim();

            // Validation
            if (amountText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter an amount!");
                return;
            }

            if (category == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a category!");
                return;
            }

            if (date == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a date!");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Amount must be greater than 0!");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number!");
                return;
            }

            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            JsonDataManager.addExpense(userId, category, amount, formattedDate, description);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Expense added successfully!");

            // Clear fields
            amountField.clear();
            categoryCombo.setValue(null);
            datePicker.setValue(LocalDate.now());
            descField.clear();
        });

        // Back action
        backButton.setOnAction(e -> {
            dashboard.refreshDashboard();
            dashboard.show();
        });

        // Layout
        VBox formBox = new VBox(8,
            amountLabel, amountField,
            categoryLabel, categoryCombo,
            dateLabel, datePicker,
            descLabel, descField,
            new Label(""), buttonsBox
        );
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(350);

        VBox root = new VBox(20, titleLabel, formBox);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F0FFFF;");

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

