import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

class BudgetItem {
    private String name;
    private double amount;

    public BudgetItem(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}

class Budget {
    private List<BudgetItem> incomes;
    private List<BudgetItem> expenses;

    public Budget() {
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public void addIncome(String name, double amount) {
        incomes.add(new BudgetItem(name, amount));
    }

    public void addExpense(String name, double amount) {
        expenses.add(new BudgetItem(name, amount));
    }

    public double calculateTotalIncome() {
        double totalIncome = 0;
        for (BudgetItem item : incomes) {
            totalIncome += item.getAmount();
        }
        return totalIncome;
    }

    public double calculateTotalExpenses() {
        double totalExpenses = 0;
        for (BudgetItem item : expenses) {
            totalExpenses += item.getAmount();
        }
        return totalExpenses;
    }

    public double calculateAvailableFunds() {
        return calculateTotalIncome() - calculateTotalExpenses();
    }

    public void displayBudgetSummary() {
        System.out.println("----- Budget Summary -----");
        System.out.println("Total Income: $" + calculateTotalIncome());
        System.out.println("Total Expenses: $" + calculateTotalExpenses());
        System.out.println("Available Funds: $" + calculateAvailableFunds());
    }
}


public class BudgetCreatorApp {
    private static final String SALT = BCrypt.gensalt(); // Generate a salt for bcrypt hashing

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Budget budget = new Budget();


        System.out.println("Welcome to the Budget Creator App!");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String hashedPassword = BCrypt.hashpw(password, SALT);


        if (username.equals("user") && BCrypt.checkpw(password, hashedPassword)) {
            System.out.println("Authentication successful!");


            System.out.println("\nEnter income details:");
            System.out.print("Name: ");
            String incomeName = scanner.nextLine();
            System.out.print("Amount: $");
            double incomeAmount = scanner.nextDouble();
            budget.addIncome(incomeName, incomeAmount);


            System.out.println("\nEnter expense details:");
            System.out.print("Name: ");
            scanner.nextLine(); // Clear scanner buffer
            String expenseName = scanner.nextLine();
            System.out.print("Amount: $");
            double expenseAmount = scanner.nextDouble();
            budget.addExpense(expenseName, expenseAmount);

            budget.displayBudgetSummary();
        } else {
            System.out.println("Authentication failed. Invalid username or password.");
        }
    }
}
