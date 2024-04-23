package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBController {
    public static void main(String[] args) {

        DBController dbController = new DBController();

        dbController.addAccount("Maja", "Solheim", "Maja@hotmail.com", "MaSol", "lols");
        //dbController.removeAccount("Maja@hotmail.com");
        System.out.println(dbController.doesAccountExist("Maja@hotmail.com"));
        dbController.readTable("tAccount");
    }

    private void readTable(String tableName) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL query to select everything from the specified table
        String query = "SELECT * FROM " + tableName;

        try {
            // Create a prepared statement with the query
            PreparedStatement statement = connection.prepareStatement(query);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                // Assuming the structure of your table, adjust column names accordingly
                String fName = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                String email = resultSet.getString("email");
                String uName = resultSet.getString("uName");
                String password = resultSet.getString("password");
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error reading table: " + e.getMessage());
        }

        // Close the connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAccount(String fName, String lName, String email, String uName, String password) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL INSERT statement
        String insertQuery = "INSERT INTO tAccount (fName, lName, email, uName, password)" +
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

    private void removeAccount(String email) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL DELETE statement
        String deleteQuery = "DELETE FROM tAccount WHERE email = ?";

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

    private boolean doesAccountExist(String email) {
        // Obtain an instance of DBConnection
        DBConnection controller = DBConnection.getInstance();
        // Request a connection to the database
        Connection connection = controller.getConnection();

        // Prepare the SQL SELECT statement
        String selectQuery = "SELECT COUNT(*) FROM tAccount WHERE email = ?";

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


}
