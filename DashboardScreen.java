package personalexpensetracker;

import javafx.collections.ObservableList;
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

public class DashboardScreen {
    private Stage primaryStage;
    private String userId;
    private Label incomeValueLabel;
    private Label expenseValueLabel;
    private Label balanceValueLabel;
    private TableView<Expense> recentTable;

    public DashboardScreen(Stage stage, String userId) {
        this.primaryStage = stage;
        this.userId = userId;
    }

    public void show() {
        // Top title bar
        Label titleLabel = new Label("Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.WHITE);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
            "-fx-background-color: #FF6B6B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6px 15px;" +
            "-fx-cursor: hand;"
        );
        logoutButton.setOnAction(e -> {
            LoginScreen login = new LoginScreen(primaryStage);
            login.show();
        });

        HBox topBar = new HBox(titleLabel, logoutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(20);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #008B8B;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

       
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        double monthlyIncome = JsonDataManager.getMonthlyIncome(userId, currentMonth);
        double totalExpenses = JsonDataManager.getTotalExpenses(userId);
        double remainingBalance = monthlyIncome - totalExpenses;

      
        VBox incomeCard = createSummaryCard("Monthly Income", monthlyIncome, "#20B2AA", "$");
        incomeValueLabel = (Label) incomeCard.getChildren().get(1);
        Button setIncomeButton = new Button("Set Income");
        setIncomeButton.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #20B2AA;" +
            "-fx-font-size: 11px;" +
            "-fx-padding: 4px 10px;" +
            "-fx-cursor: hand;"
        );
        setIncomeButton.setOnAction(e -> showSetIncomeDialog(currentMonth));
        incomeCard.getChildren().add(setIncomeButton);

        
        VBox expenseCard = createSummaryCard("Total Expenses", totalExpenses, "#FF6B6B", "$");
        expenseValueLabel = (Label) expenseCard.getChildren().get(1);

       
        VBox balanceCard = createSummaryCard("Remaining Balance", remainingBalance, "#4ECDC4", "$");
        balanceValueLabel = (Label) balanceCard.getChildren().get(1);

        HBox cardsBox = new HBox(20, incomeCard, expenseCard, balanceCard);
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setPadding(new Insets(20));

        // Action Buttons
        Button addExpenseButton = new Button("+ Add New Expense");
        addExpenseButton.setStyle(
            "-fx-background-color: #008B8B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px 25px;" +
            "-fx-cursor: hand;"
        );
        addExpenseButton.setOnAction(e -> {
            AddExpenseScreen addScreen = new AddExpenseScreen(primaryStage, userId, this);
            addScreen.show();
        });

        Button summaryButton = new Button("View Summary Report");
        summaryButton.setStyle(
            "-fx-background-color: #5F9EA0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px 25px;" +
            "-fx-cursor: hand;"
        );
        summaryButton.setOnAction(e -> {
            SummaryScreen summary = new SummaryScreen(primaryStage, userId, this);
            summary.show();
        });

        HBox buttonsBox = new HBox(15, addExpenseButton, summaryButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10));

        
        Label recentLabel = new Label("Recent Expenses");
        recentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        recentLabel.setTextFill(Color.DARKSLATEGRAY);

        recentTable = new TableView<>();
        recentTable.setPrefHeight(250);
        recentTable.setStyle("-fx-font-size: 13px;");

        TableColumn<Expense, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));
        catCol.setPrefWidth(150);

        TableColumn<Expense, String> amtCol = new TableColumn<>("Amount");
        amtCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty("$" + String.format("%.2f", cellData.getValue().getAmount())));
        amtCol.setPrefWidth(120);

        TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate()));
        dateCol.setPrefWidth(120);

        TableColumn<Expense, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        descCol.setPrefWidth(200);

        recentTable.getColumns().addAll(catCol, amtCol, dateCol, descCol);
        loadRecentExpenses();

        VBox tableBox = new VBox(10, recentLabel, recentTable);
        tableBox.setPadding(new Insets(0, 20, 20, 20));

        // Main layout
        VBox mainContent = new VBox(10, cardsBox, buttonsBox, tableBox);
        VBox root = new VBox(topBar, mainContent);
        root.setStyle("-fx-background-color: #F5F5F5;");

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Personal Expense Tracker - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox createSummaryCard(String title, double value, String color, String prefix) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.WHITE);

        Label valueLabel = new Label(prefix + String.format("%.2f", value));
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.WHITE);

        VBox card = new VBox(8, titleLabel, valueLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 10px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        return card;
    }

    private void showSetIncomeDialog(String month) {
        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Set Monthly Income");
        dialog.setHeaderText("Set your income for " + month);

        ButtonType setButton = new ButtonType("Set", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(setButton, ButtonType.CANCEL);

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        VBox content = new VBox(10, new Label("Amount ($):"), amountField);
        content.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == setButton) {
                try {
                    return Double.parseDouble(amountField.getText().trim());
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number!");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(amount -> {
            JsonDataManager.setMonthlyIncome(userId, amount, month);
            refreshDashboard();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Monthly income set to $" + String.format("%.2f", amount));
        });
    }

    public void refreshDashboard() {
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        double monthlyIncome = JsonDataManager.getMonthlyIncome(userId, currentMonth);
        double totalExpenses = JsonDataManager.getTotalExpenses(userId);
        double remainingBalance = monthlyIncome - totalExpenses;

        incomeValueLabel.setText("$" + String.format("%.2f", monthlyIncome));
        expenseValueLabel.setText("$" + String.format("%.2f", totalExpenses));
        balanceValueLabel.setText("$" + String.format("%.2f", remainingBalance));

        loadRecentExpenses();
    }

    private void loadRecentExpenses() {
        ObservableList<Expense> expenses = JsonDataManager.getExpenses(userId);
        recentTable.setItems(expenses);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
