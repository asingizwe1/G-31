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
        ExecutorService executorService= Executors.newCachedThreadPool();//It helps efficiently manage multiple client connections by allowing threads to be reused.
        // Try-with-resources to ensure the ServerSocket is closed automatically.
        try(ServerSocket ss =new ServerSocket(2212)){//Creates a new socket connection to the server at IP address 127.0.0.1 (localhost) on port 2212.
            System.out.println("Waiting for Client connection..");
            // Continuously accept client connections.
            while(true) {
                // Accept a client connection.
                Socket soc = ss.accept();//Accepts an incoming client connection and creates a new Socket object to communicate with the client.
                System.out.println("Connection established");
                // Set a socket timeout of 900,000 milliseconds (15 minutes).
                soc.setSoTimeout(900000);
                //Submits a new task to the executor service. The task is defined as a lambda expression () -> handleClientRequest(soc), which calls the handleClientRequest method with the Socket object (soc) as an argument.
                executorService.submit(()->handleClientRequest(soc));//This allows the server to handle each client connection in a separate thread, enabling concurrent processing of multiple clients.


            }
        }catch(IOException e){
            // Print any IOException that occurs during the server socket operation.
            System.out.println(e.getMessage());
        }
    }
//Defines a method to handle client requests, taking a Socket object as an argument.
    public static void handleClientRequest(Socket socket){
        // below is a Try-with-resources to ensure that the Socket, BufferedReader, and PrintWriter are closed automatically after the try block is executed.
        try(socket;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ){
            // Continuously read and process client requests.
            while (true) {
                // Read a client request from the input stream.
                String clientRequest = in.readLine();//Reads a line of text sent by the client from the BufferedReader

                System.out.println("Server received: " + clientRequest + " command");
                // Split the client request into parts based on spaces.
                String[] req = clientRequest.trim().split(" ");

                // Switch-case to handle different commands.
                switch(req[0]){
                    // Call the register method in the Pupil class to handle registration.
                    case "registerPupil":
                        pupil_detail.register(req, out);
                        break;
                    case "registerRep":
                      //rep logic....
                        SchoolRepresentative.registerRep_(req, out);
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

