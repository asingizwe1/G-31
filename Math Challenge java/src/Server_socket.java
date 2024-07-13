import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
class Server_socket {
    public static void main(String[] args) {
        // Create a thread pool using cached thread pool for handling multiple client connections concurrently.
        ExecutorService executorService= Executors.newCachedThreadPool();
        // Try-with-resources to ensure the ServerSocket is closed automatically.
        try(ServerSocket ss =new ServerSocket(2212)){
            System.out.println("Waiting for Client connection..");
            // Continuously accept client connections.
            while(true) {
                // Accept a client connection.
                Socket soc = ss.accept();
                System.out.println("Connection established");
                // Set a socket timeout of 900,000 milliseconds (15 minutes).
                soc.setSoTimeout(900000);
                // Submit a new task to handle the client request using a separate thread.
                executorService.submit(()->handleClientRequest(soc));


            }
        }catch(IOException e){
            // Print any IOException that occurs during the server socket operation.
            System.out.println(e.getMessage());
        }
    }

    public static void handleClientRequest(Socket socket){
        // Try-with-resources to ensure the socket, BufferedReader, and PrintWriter are closed automatically.
        try(socket;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ){
            // Continuously read and process client requests.
            while (true) {
                // Read a client request from the input stream.
                String clientRequest = in.readLine();//receives request

                System.out.println("Server received: " + clientRequest + " command");
                // Split the client request into parts based on spaces.
                String[] req = clientRequest.trim().split(" ");

                // Switch-case to handle different commands.
                switch(req[0]){
                    // Call the register method in the Pupil class to handle registration.
                    case "register":
                        pupil_detail.register(req, out);
                        break;
                    case "login":
                        // // Call the login method in the Pupil class to handle login.
                        s_r.login(req,out,in);// static call to class with that method
                        break;
                    default:
                        // If the command is not recognized, print and send an error message.
                        System.out.println("Command not recognized");
                        out.println("Command not recognized");
                        out.println();
                        break;
                }

            }
        }catch(IOException e){
            // Print any IOException that occurs during client communication.
            System.out.println(e.getMessage());
        }

    }



}
