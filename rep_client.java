
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Logger;

class rep_client{

    private static final Logger logger = Logger.getLogger(rep_client.class.getName());

    public static void main (String[] args) {
        Socket socket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        BufferedReader br = null;


        try {
            socket = new Socket("127.0.0.1", 8000);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        br =new BufferedReader(new InputStreamReader(System.in));
        
       // Prompt for username and password
            // Prompt for username and password
       System.out.print("Username: ");
       String username = br.readLine();
       dos.writeUTF(username);
       
       System.out.print("Password: ");
       String password = br.readLine();
       dos.writeUTF(password);
       // Receive login response from the server
       String loginResponse = dis.readUTF();
       System.out.println(loginResponse);

       if ("Login Successful".equals(loginResponse)) {
           System.out.print("Representative: ");
           String msg = br.readLine();
           dos.writeUTF(msg);
        
    
        //try
        
    //''
    
    //''
    
        while(!msg.equals("exit")){
    
                System.out.print("Server :"+dis.readUTF());
                msg=br.readLine();
               dos.writeUTF(msg);
    
        }}else {
            System.out.println("Login Failed. Exiting...");
        }  } catch (Exception e) {
            logger.severe("Exception occurred: " + e.getMessage());
        } finally {  try {  if (dis != null) dis.close();
            if (dos != null) dos.close();
            if (br != null) br.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            logger.severe("Error closing resources: " + e.getMessage());
        }
        

    }
    
}
}