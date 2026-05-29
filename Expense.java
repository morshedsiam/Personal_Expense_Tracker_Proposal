package personalexpensetracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
    private final SimpleStringProperty expenseId;
    private final SimpleStringProperty userId;
    private final SimpleStringProperty category;
    private final SimpleDoubleProperty amount;
    private final SimpleStringProperty date;
    private final SimpleStringProperty description;

    Expense(String expenseId, String userId, String category, double amount, String date, String description) {
        this.expenseId = new SimpleStringProperty(expenseId);
        this.userId = new SimpleStringProperty(userId);
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
    }

    public String getExpenseId() {
        return expenseId.get();
    }

    public String getUserId() {
        return userId.get();
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
