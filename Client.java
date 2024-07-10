import java.net.*; //because i want a network connection thats why i'm importing that
import java.io.*;//because i want input an output streams

public class Client{//initialise my input and output streams 
//initialise all variables to null ensures youve defined the state before any operations performed and indicates that these variables have not been assigned a valid object

 
    private Socket socket =null;
    
    private BufferedReader in = null; // Change DataInputStream to BufferedReader
    private BufferedWriter out = null;//private DataOutputStream out =null;
 
    // you then create ip address for the ip address and the port number
    Client(String address, int port){
        // my first step is to then establish a connection
        // in order to do this i will create object of Socket and pass parameters address and port
        try{// create object of socket and pass address and port parameters.
            socket= new Socket(address,port);
            //as  it does that you have to print connected
            System.out.println("connected");
            
            //you then need to take input from the terminal
            //prompt user to enter in put and reading input from the console
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//instead of using scanner class
             System.out.println("Input in format :(username firstname lastname emailAddress date_of_birth school_registration_number password )");
            String  Detail= br.readLine();
             // Ensure detail is not null or empty before proceeding
          if (Detail == null || Detail.trim().isEmpty()) {
                System.out.println("Invalid input. Please provide details in the correct format.");
                Detail = "default_user default_firstname default_lastname default_email 01-Jan-2000 school_registration_number";
            }
           
              // Ensure detail is not null or empty before proceeding

            //then also sends output to the socket
            //this sends data to the server
            out= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//new DataOutputStream(socket.getOutputStream());
            //sending collected data to server
            //ensure there is not prefix before detail or the format will be unknown
            out.write( Detail);//take note write UTF may write a string that doesnt exit hence error
            out.newLine(); // Ensures the message is terminated with a newline
            //ensures that the data is sent out to the server without delay.
            out.flush();
//you then prepare to read messages from server
 in =new BufferedReader(new InputStreamReader(socket.getInputStream()));//DataInputStream cannot be converted to buffered reader so they are incompatible formats
            //Sting to read message from input
            String line = "";
            //its meant to keep reading till "exit is input"
            while (!line.equals("exit")){
                try{
                    line=in.readLine();
                   System.out.println(line);
                }
                catch(IOException i)
                {System.out.println(i);}
            }
            
        //close the connection
        br.close();
        in.close();
        out.close();
        socket.close();
    }catch(IOException i){
        System.out.println(i);
    }
}
public static void main (String args[]){
    // connect to server at localhost and port 8000
    Client client = new Client("127.0.0.1",8000);
}
}