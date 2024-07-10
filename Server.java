// By placing all the initialization and communication logic within the constructor, you encapsulate all the related operations in one place. This makes the code more organized and easier to understand. 
import java.net.*;
import java.io.*;
import java.sql.*; // Import for SQL classes
import java.util.Scanner;

public class Server extends Login_1{
    private Socket socket =null;
    private BufferedReader in =null;
    private BufferedWriter out = null;
    //instead of output data stream i'll have the server socket
    private ServerSocket server =null;
    //create constructor with parameter Port
    //this constructor initialises server on a specified port
    //additions below
    Server(int port, Scanner scanner){
        super(scanner); // Pass scanner to Login_1 constructor if needed
        try{//create new object of server socket
        //server pocket initialisation
            server= new ServerSocket(port);
            //creates new serversocket object that listens for client connections on specified port
            System.out.println("server has started");
             System.out.println("Waiting for the client...."); 
             //after connecting the client and it prints connected.. then..

            socket= server.accept();
            //waits for client to connect. When client connects, it returns a Socket object to communicate with client
            System.out.println("Client accepted");//client connected
            //take input from client socket, from getinput stream
            //we proceed to read data from client......
            in= new BufferedReader(new InputStreamReader(socket.getInputStream()));//DataInputStream(new BufferedInputStream(socket.getInputStream())) ;
            //output stream
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//DataOutputStream(socket.getOutputStream());
            // the above initialises a DataInputStream to read data from the client through an input stream
             // Send output to the client socket


            //it will then read the string line
            String line="";
            //a while loop is used to read messages from client until the client sends the string "exit"
            while((line = in.readLine()) != null && !line.equals("exit")){//try block contains code that may throw an exception
               
                //use try to wrap code that you want to monitor for exceptions
                //Unicode Transformation Format-8- format that uses 1 to 4 bytes. when coding english 1 byte is used per character
                // translate any unicode character to matching unique binary string
                    //this reads UTF-8 Encoded string from input stream
                    System.out.println("Client : "+line);//prints message received from client.
                 // Split the input and process it
                 //(" "). This means that the method will look for spaces in the string and use them to determine where to split the string.
                    //The line.split(" ") method in Java is used to split a string into an array of substrings based on a given delimiter. In this case, the delimiter is a single space (" ")
                    String[] parts = line.split(" ");// [> sign] The server should ignore any extra parts beyond the required five fields.
                    for (int i = 0; i < parts.length; i++) {
                        System.out.println("Part " + i + ": " + parts[i]); // Debug statement
                    }
                    if (parts.length >= 7) {
                        String username = parts[0];
                        String firstname = parts[1];
                        String lastname = parts[2];
                        String emailAddress = parts[3];
                        String dateOfBirth = parts[4];
                        String school_registration_number = parts[5];
                        String password = parts[6];
                        insertUser(username, firstname, lastname, emailAddress, dateOfBirth, school_registration_number, password);
                    
                        /*condition from my login class
                        if(validateCredentials(username, firstname, lastname, emailAddress, dateOfBirth, school_registration_number, password)){
                            out.write("Login success. ");
                        }else{out.write("wrong details. ");}*/

                         // Print the results
                        System.out.println("Username: " + username);
                        System.out.println("First Name: " + firstname);
                        System.out.println("Last Name: " + lastname);
                        System.out.println("Email Address: " + emailAddress);
                        System.out.println("Date of Birth: " + dateOfBirth);
                        System.out.println("school_registration_number: " + school_registration_number);
                        
                        out.write("Data received and inserted.");
                    out.newLine();
                    out.flush();
                        //new addition
                       /* out.write("Received: " + line);
                        //
                         /send a response back to the client
                        out.write("Received: " + line);
                        out.newLine();
                        out.flush();*/
                    }else {
                        System.out.println("Invalid input format.");
                    out.write("Invalid input format. Please provide data in the correct format.");
                        //ensures that the data is sent out to the server without delay.
                        out.newLine();
                        out.flush();
                    }
                }// we than proceed to close the connection
                //catch block handles the exception if one is thrown
                // you can have multiple catch blocks
               
                System.out.println("closing connection");// shows that the server is closing the connection
                //close out input stream
                socket.close();//closes socket connection with client
                in.close();//closes input stream
                out.close();
            }catch(IOException i){System.out.println(i);}
        //entry point of program which creates a new server object initialising it to listen on port 
       
    }

 public static void main(String[] args){
            //initialise server with port number
            Scanner scanner =new Scanner(System.in);
       Server server = new Server(8000,scanner);
    }
    //detailing on inputing the data
    public static void insertUser(String username, String firstname, String lastname, String emailAddress, String dateOfBirth, String schoolRegistrationNumber, String password) {
        Connection connection = db_connect_1.connect();
        if (connection != null) {
            String sql = "INSERT INTO results (username, firstname, lastname, emailAddress, dateOfBirth, schoolRegistrationNumber, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, firstname);
                statement.setString(3, lastname);
                statement.setString(4, emailAddress);
                statement.setString(5, dateOfBirth);
                statement.setString(6, schoolRegistrationNumber);
                statement.setString(7, password);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }
            } catch (SQLException e) {
                System.err.println("Error inserting user: " + e.getMessage());
            } finally {
                closeConnection(connection);
            }}}}
        
    
 