package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls database operations for managing users, accounts, and transactions.
 * This class provides methods to add, remove, and check users, as well as add accounts and payments to the database.
 */
public class DBController {
    /**
     * Temporary main method for testing database operations.
     * @param args Command line arguments.
     */
    public static void main(String[] args) throws SQLException {
        DBController dbController = new DBController();
        System.out.println(dbController.selectAccounts(1));
        //dbController.executeSQL("INSERT INTO tUser (fName, lName, email, uName, password)" +
                //" VALUES ('Daisy', 'Jones', 'DJon@gmail.com', 'Daisy', 'the6Rule')");


//        dbController.addUser("Sophie", "Gellar", "Soph@gmail.com", "SuperSophie", "haha");
//        dbController.removeUser("Maja@hotmail.com");
        List<Map<String, Object>> tableData = dbController.readTable("tUser");
        System.out.println(tableData);
//        dbController.addPayment(4, 20.00, "2024-05-07", "ALDI", "Todays dinner", "GROCERIES", "NA");
//        dbController.addAccount(12345678, "Savings", 1, 1000.0, "savings account");
    }

    /**
     * Reads all records from any specified table and returns them.
     * @param tableName The name of the table to read from.
     * @return A list of maps, where each map represents a row with column names as keys and column values as values.
     * @throws SQLException If an SQL error occurs.
     */
    public List<Map<String, Object>> readTable(String tableName) throws SQLException {
        DBConnection controller = DBConnection.getInstance();


        Connection connection = controller.getConnection();


        String query = "SELECT * FROM " + tableName;
        List<Map<String, Object>> tableData = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Getting metadata to dynamically read column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Processing each row of the database table
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                tableData.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error reading table: " + e.getMessage());
            throw e; // rethrow the exception after logging it
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e; // rethrow the exception after logging it
            }
        }

        return tableData;
    }

    /**
     * Adds a new user to the database.
     * @param fName User's first name.
     * @param lName User's last name.
     * @param email User's email.
     * @param uName Username.
     * @param password User's password.
     */
    private void addUser(String fName, String lName, String email, String uName, String password) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL INSERT statement
        String insertQuery = "INSERT INTO tUser (fName, lName, email, uName, password)" +
                " VALUES (?, ?, ?, ?, ?)";

        try {
            // Create a prepared statement with the insert query
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameter values for the insert statement
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, uName);
            preparedStatement.setString(5, password);

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("Account added successfully.");
            } else {
                System.out.println("Failed to add account.");
            }

            // Close the prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error adding account: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a user from the database by email.
     * @param email The email of the user to be removed.
     */
    private void removeUser(String email) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL DELETE statement
        String deleteQuery = "DELETE FROM tUser WHERE email = ?";

        try {
            // Create a prepared statement with the insert query
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

            // Set the parameter value for the email
            preparedStatement.setString(1, email);

            // Execute the DELETE statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete account.");
            }

            // Close the prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user exists in the database by their email.
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    private boolean doesUserExist(String email) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL SELECT statement
        String selectQuery = "SELECT COUNT(*) FROM tUser WHERE email = ?";

        try {
            // Create a prepared statement with the select query
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            // Set the parameter value for the email
            preparedStatement.setString(1, email);

            // Execute the SELECT statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Get the count of rows returned
            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                return rowCount > 0; // If rowCount > 0, account exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking account existence: " + e.getMessage());
        } finally {
            // Close the connection
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Default to false if an error occurred or no rows were returned
        return false;
    }

    /**
     * Adds a payment transaction to the database.
     * @param accountNumber The accountNumber associated with the payment.
     * @param amount The amount of the payment.
     * @param date The date of the transaction in the format "YYYY-MM-DD".
     * @param place The location where the transaction occurred.
     * @param purchase Description of the purchase.
     * @param transaction_type The type of transaction (e.g., GROCERIES, UTILITIES).
     * @param billing_type The billing type (e.g., NA, RECURRING).
     */
    public void addPayment(int accountNumber, double amount, String date, String place, String purchase, String transaction_type, String billing_type) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL INSERT statement
        String insertQuery = "INSERT INTO tPayment (accountNumber, amount, date, place, purchase, transaction_type, billing_type)" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        // Convert String date to java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);

        try {
            // Create a prepared statement with the insert query
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameter values for the insert statement
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setString(4, place);
            preparedStatement.setString(5, purchase);
            preparedStatement.setString(6, transaction_type);
            preparedStatement.setString(7, billing_type);

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("Payment added successfully.");
            } else {
                System.out.println("Failed to add payment.");
            }

            // Close the prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an account record to the database.
     * @param accountNumber The account number, which is unique for each account.
     * @param type The type of the account (e.g., Savings, Checking).
     * @param ownerid The user ID of the account owner.
     * @param balance The initial balance of the account, defaulting to 0 if not specified.
     * @param name The name or description of the account.
     */
    //TODO: add more constraint(accountNumber set amount of numbers)
    private void addAccount(int accountNumber, String type, int ownerid, double balance, String name) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL INSERT statement
        String insertQuery = "INSERT INTO tAccount (accountNumber, type, ownerid, balance, name)" +
                " VALUES (?, ?, ?, ?, ?)";

        try {
            // Create a prepared statement with the insert query
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Set parameter values for the insert statement
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, ownerid);
            preparedStatement.setDouble(4, balance);
            preparedStatement.setString(5, name);

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                System.out.println("Account added successfully.");
            } else {
                System.out.println("Failed to add account.");
            }

            // Close the prepared statement
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Error adding account: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves all account records for a given user ID from the tAccount table.
     *
     * @param userid The ID of the user whose accounts are to be retrieved.
     * @return A list of maps, where each map represents a row in the result set, with column names as keys and column values as values.
     * @throws SQLException If a database access error occurs or the SQL statement is invalid.
     */
    public List<Map<String, Object>> selectAccounts(int userid) throws SQLException {
        DBConnection controller = DBConnection.getInstance();
        Connection connection = controller.getConnection();
        String query = "SELECT * FROM tAccount WHERE ownerid = " + userid;
        List<Map<String, Object>> tableData = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Getting metadata to dynamically read column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Processing each row of the database table
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                tableData.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw e; // rethrow the exception after logging it
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e; // rethrow the exception after logging it
            }
        }

        return tableData;
    }


    /**
     * Executes the provided SQL query and returns the result set as a list of maps.
     * Each map represents a row in the result set, with column names as keys and column values as values.
     * The method can handle SELECT statements that return results.
     *
     * @param sql The SQL statement to be executed.
     * @return A list of maps, where each map represents a row in the result set, with column names as keys and column values as values.
     * @throws SQLException If a database access error occurs or the SQL statement is invalid.
     */
    public List<Map<String, Object>> executeSQL(String sql) throws SQLException {
        DBConnection controller = DBConnection.getInstance();
        Connection connection = controller.getConnection();
        List<Map<String, Object>> tableData = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            if (statement.execute()) { // True if the first result is a ResultSet object
                ResultSet resultSet = statement.getResultSet();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Processing each row of the database table
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    tableData.add(row);
                }

                resultSet.close();
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw e; // rethrow the exception after logging it
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e; // rethrow the exception after logging it
            }
        }

        return tableData;
    }
}