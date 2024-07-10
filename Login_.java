import java.util.Scanner;
//to cater for tha sql classes
//Connection, PreparedStatement, ResultSet, and SQLException are DB ops
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


 class Login_ extends db_connect_{
     Scanner sc;
     String ur, pass;
     private static final Logger logger = Logger.getLogger(db_connect_.class.getName());

     // Constructor of Login_ class that takes a Scanner object
     Login_(Scanner sc) {
         this.sc = sc;
     }

     // Method to insert user input data (username and password)
     public void insertData() {
         System.out.println("Username: ");
         ur = sc.nextLine();

         System.out.println("Password: ");
         pass = sc.nextLine();

         // Validate credentials after obtaining input
         validation();
     }

     // Method to validate user credentials against the database
     public boolean validateCredentials(String username, String password) {
         this.ur = username;
         this.pass = password;
         return validation();
     }

     // Method to perform validation and return true if login is successful
     public boolean validation() {
         try (Connection conn = db_connect_.connect()) {
             String sql = "SELECT * FROM results WHERE username = ? AND password = ?";
             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, ur);
                 pstmt.setString(2, pass);
                 try (ResultSet rs = pstmt.executeQuery()) {
                     if (rs.next()) {
                         System.out.println("Login Successful");
                         return true;
                     } else {
                         System.out.println("Wrong username or password");
                         return false;
                     }
                 }
             }
         } catch (SQLException e) {
             logger.severe("SQL Exception occurred: " + e.getMessage());
             return false;
         }
     }

     public static void main(String[] args) {

         Scanner sc = new Scanner(System.in);
         try (sc) {
             Login_ l = new Login_(sc);
             l.insertData();
         } catch (Exception e) {
             logger.severe("Exception occurred during login: " + e.getMessage());
         }
     }}

