import java.util.Scanner;
//to cater for tha sql classes
//Connection, PreparedStatement, ResultSet, and SQLException are DB ops
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


class Login_1 extends db_connect_1{

    Scanner sc;
    String ur,pass;

    //constructor of login class that takes up parameter of input
    Login_1 (Scanner sc){
        this.sc=sc;
    }
    public void insertData(){
        System.out.println("User_name: ");
        ur=sc.nextLine();

        System.out.println("Password: ");
        pass= sc.nextLine();
        //validation();
        saveToDatabase();
    }
    //i now use m() to be called
    
     /* 
    public boolean validateCredentials(String username, String password) {
        this.ur = username;
        this.pass = password;

        return validation();
    }
    //The error "missing return statement" indicates that the validation method in the Login_ class is not handling all possible control paths. Specifically, the try block handles SQL exceptions, but the compiler expects a return statement outside the try block to handle cases where an exception is not thrown.
 //You need to add a return statement at the end of the validation method to cover all paths
    public boolean validation(){
        //my database validation logic
        //since connect() is static u call it by class name instead of instance
        try(Connection conn = db_connect_1.connect()){
            //The SQL statement SELECT * FROM users WHERE username = ? AND password = ? is used to retrieve records from the users table where the username and password match the provided parameters.
            String sql= "SELECT * FROM results WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ur);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();



            if(rs.next()){
                System.out.println("Login Successful");
            }else{System.out.println("Wrong username or password");}
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;}*/

    public void saveToDatabase() {
        Connection connection = db_connect_1.connect();
        if (connection != null) {
            String sql = "INSERT INTO results (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ur);
                statement.setString(2, pass);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }
            } catch (SQLException e) {
                System.err.println("Error inserting user: " + e.getMessage());
            } finally {
                db_connect_1.closeConnection(connection);
            }
        }
    }


    public static void main(String[] args){
        Scanner sc =new Scanner(System.in);
        Login_1 l =new Login_1(sc);
        l.insertData();
        sc.close();
    }
    
    
    
}

