import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

//The beginning of the budget creator class
/*This class is mainly for calculating the remaining budget for each month
and how many weeks are left in the month*/
public class BudgetCreator {
//Using a 'Map' to store the maximum budget for each of the categories
private Map<String, Double> budgetMaximums = new HashMap<>();
private int weeksRemainingInMonth;
private static final int WEEKS_LEFT_PER_MONTH_DEFAULT = 4;
public BudgetCreator() {



//Setting how many weeks are left in the month with a method
public void setWeeksRemainingInMonth(int weeksRemainingInMonth)

{

this.weeksRemainingInMonth = weeksRemainingInMonth;

}


/*Solving the number of weeks remaining in the month
Using the java.util.Calendar import to find the current day
*/
public void findWeeksRemaining()

{
//Working out which week in the month it is
//This way the user doesn't need to input the week manually

LocalDate now = LocalDate.now();
int daysInMonth = now.lengthOfMonth(); 
int presentDay = now.getDayOfMonth(); 
int daysLeftInMonth = daysInMonth - presentDay;
weeksRemainingInMonth = (int) Math.ceil(daysLeftInMonth / 7.0);

}

//Finding the percentage of the budget that is remaining for a specific category

public double findBudgetLeftPercentage(String type, double expenditure) {
double limit = budgetMaximums.getOrDefault(type, 0.0);
double budgetLeft = findBudgetLeft(type, expenditure);
return (budgetLeft / limit) * 100;

}

//Finding the amount of money that is left in the budget for a specicific category
public double findBudgetLeft(String type, double expenditure)
{

double limit = budgetMaximums.getOrDefault(type, 0.0);
//Subtracting the 'expenditure' from the 'limit'
//This will calculate how much of the budget is left over
double budgetLeft = limit - expenditure;
return budgetLeft;

}

//Adding a budget limit for the chosen category
public void addBudgetMaximum(String type, double limit) {
budgetMaximums.put(type, limit);

}

//Running the budget creator
//The main method
public static void main(String[] args) {
BudgetCreator budgetCreator = new BudgetCreator();
Scanner scanner = new Scanner(System.in);

/*Listing each of the categories for the budget.
I left out 'health' since that is a bill users' may not budget for.
The categories are from the 'financeReportGenerator' class.*/

String[] group =

{"Home",
"Bill",
"Shopping",
"Groceries",
"General",
"Eatout",
"Transport",
"Entertainment"

};
//Welcome message outside of loop so it isn't repeated everytime
System.out.println("Welcome to the budget creator!");
//This is where the users' can enter their budget for each category
for (String category : group) {
   
System.out.println("What is your budget for " + category + "?");
double budget = scanner.nextDouble();
budgetCreator.addBudgetMaximum(category, budget);
}

/*Test data
Different amounts spent for each category
If no data is obtained for the money spent for each category then this test data will be used
This way there is still data provided regardless
*/
Map<String, Double> expenditure = new HashMap<String, Double>() {{
put("Home", 210.50);
put("Bill", 72.61);
put("Shopping", 60.0);
put("Groceries", 110.0);
put("General", 50.0);
put("Eatout", 34.0);
put("Transport", 78.0);
put("Entertainment", 40.0);

}};

//Finding the amount of weeks left in the month
budgetCreator.findWeeksRemaining();

/*Finding the budget remaining and the percentage of the budget that is left
Next the results are printed to the console
This is done for each of the eight categories
*/
//Using a for loop
for (String category : group) {
double thisMonthsExpenditure = expenditure.getOrDefault(category, 0.0);
double budgetLeft = budgetCreator.findBudgetLeft(category, thisMonthsExpenditure);
double budgetLeftPercentage = budgetCreator.findBudgetLeftPercentage(category, thisMonthsExpenditure);
//Displaying how much of the budget is left in pounds and in percentage
System.out.println("You currently have £" + budgetLeft + " of your " + category + " budget left to last you " + budgetCreator.weeksRemainingInMonth + " weeks.");
System.out.println("You currently have " + budgetLeftPercentage + "% left of your " + category + " budget.");
}

//The end of the scanner
scanner.close(); 
} } }