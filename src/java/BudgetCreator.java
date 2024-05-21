import java.time.LocalDate;
import java.time.YearMonth;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

class FinanceTracker {
    public Map<String, Double> fetchExpenditures() {
        Map<String, Double> expenditures = new HashMap<>();
        expenditures.put("Home", 210.50);
        expenditures.put("Bill", 72.61);
        expenditures.put("Shopping", 60.0);
        expenditures.put("Groceries", 110.0);
        expenditures.put("General", 50.0);
        expenditures.put("Eatout", 34.0);
        expenditures.put("Transport", 78.0);
        expenditures.put("Entertainment", 40.0);
        return expenditures;
    }
}

public class BudgetCreator {
    private Map<String, Double> budgetMaximums = new HashMap<>();
    private int weeksRemainingInMonth;
    private Map<String, double[]> lastThreeMonthsSpending = new HashMap<>();


    public BudgetCreator() {}

    public void setWeeksRemainingInMonth(int weeksRemainingInMonth) {
        this.weeksRemainingInMonth = weeksRemainingInMonth;
    }

    public void findWeeksRemaining() {
        LocalDate now = LocalDate.now();
        int daysInMonth = now.lengthOfMonth();
        int presentDay = now.getDayOfMonth();
        int daysLeftInMonth = daysInMonth - presentDay;
        setWeeksRemainingInMonth((int) Math.ceil(daysLeftInMonth / 7.0));
    }

    public double findBudgetLeftPercentage(String type, double expenditure) {
        double limit = budgetMaximums.getOrDefault(type, 0.0);
        double budgetLeft = findBudgetLeft(type, expenditure);
        return (budgetLeft / limit) * 100;
    }

    public double findBudgetLeft(String type, double expenditure) {
        double limit = budgetMaximums.getOrDefault(type, 0.0);
        return limit - expenditure;
    }

    public void addBudgetMaximum(String type, double limit) {
        budgetMaximums.put(type, limit);
    }

    public void fetchAndStorePastThreeMonthsSpending(String category, FinanceTracker financeTracker) {
        Map<String, Double> expenditures = financeTracker.fetchExpenditures();
        double[] categoryExpenditures = new double[3];
        categoryExpenditures[0] = expenditures.getOrDefault(category, 0.0);
        categoryExpenditures[1] = expenditures.getOrDefault(category, 0.0);
        categoryExpenditures[2] = expenditures.getOrDefault(category, 0.0);
        lastThreeMonthsSpending.put(category, categoryExpenditures);
    }

    public void displayPastThreeMonths(String category) {
        if (lastThreeMonthsSpending.containsKey(category)) {
            double[] expenditures = lastThreeMonthsSpending.get(category);
            YearMonth currentYearMonth = YearMonth.now();
            String[] shortMonths = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
            System.out.println("Past three months' spending for: " + category);
            for (int i = 0; i < 3; i++) {
                YearMonth yearMonth = currentYearMonth.minusMonths(i + 1);
                String monthName = shortMonths[yearMonth.getMonthValue() - 1];
                System.out.println(monthName + " " + yearMonth.getYear() + ": £" + expenditures[2 - i]);
            }
        } else {
            System.out.println("No data found for this category");
        }
    }

    public static void main(String[] args) {
        BudgetCreator budgetCreator = new BudgetCreator();
        Scanner scanner = new Scanner(System.in);
        FinanceTracker financeTracker = new FinanceTracker();

        System.out.println("Welcome to the budget creator!");

        String[] group = {
                "Home", "Bill", "Shopping", "Groceries",
                "General", "Eatout", "Transport", "Entertainment"
        };

        for (String category : group) {
            System.out.println("What is your budget for " + category + "?");
            double budget = scanner.nextDouble();
            budgetCreator.addBudgetMaximum(category, budget);
        }

        for (String category : group) {
            budgetCreator.fetchAndStorePastThreeMonthsSpending(category, financeTracker);
        }

        for (String category : group) {
            budgetCreator.displayPastThreeMonths(category);
        }

        budgetCreator.findWeeksRemaining();
        System.out.println("Weeks remaining in the month: " + budgetCreator.weeksRemainingInMonth);

        Map<String, Double> expenditures = financeTracker.fetchExpenditures();
        for (String category : group) {
            double expenditure = expenditures.getOrDefault(category, 0.0);
            double budgetLeftPercentage = budgetCreator.findBudgetLeftPercentage(category, expenditure);
            System.out.println("Budget left percentage for " + category + ": " + budgetLeftPercentage + "%");
        }

        scanner.close();
    }



}