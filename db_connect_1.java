//they provide necessay classes and interfaces to connect to the databases and handle SQL exceptions
//connection, drivermanager, sqlexceprion
import java.sql.*;
import java.util.*;

class db_connect_1{    //DB url, username and password stroing constants
    //we dont specify the table we only do so when it comes to the sql command
    private static final String URL="jdbc:mysql://localhost:3306/testuser";
    private static final String USER="root";
    private static final String PASSWORD="";

 //the below method to establish connection to data base
//and also to connect from database
//connect() prints a success message 
//it also establishes a message to the database using DriverManager .getConnection
 public static Connection connect(){
    //u have to initialise the connection object to null
    Connection connection = null;
    try{
        //ur object tries to create a connection using DMC() method
        //changed method below
        connection=DriverManager.getConnection(URL,USER,PASSWORD);
        //incase connection is established this message comes
        System.out.println("connection established"); 
    }//attains any sqlexception that is gotten
     catch(SQLException e){
       //when an exception ours this is what is passed
        System.err.println("Error closing connection: "+e.getMessage()); 
      } return connection;}//this returns an established connection or null if connection failed
    
    //the method is to close the connection to the database
   public static void closeConnection(Connection connection) {
        if (connection != null)//being not null means connection was successfully established
        {
            try {
                connection.close();
                System.out.println("Connection closed successfully.");
            } //incase of anysqlexception during closing
            catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

  //the main method is to test the connection
    public static void main(String[] args){
        //i then call the connect method to establish connection sttored in connection variable
        Connection connection=connect();
//CC() - to close my conne
 closeConnection(connection) ;
    }
}
    

