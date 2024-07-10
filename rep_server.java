import java.util.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.logging.Logger;
import java.net.*;
import java.util.Scanner;
import java.io.*;


class rep_server extends Login_{
    private static final Logger logger = Logger.getLogger(rep_client.class.getName());
            //i create class for applicant
        static class Applicant {//i assign variables needed
            String name;
            String regNumber;
            boolean isActive; //for validity
            //then i make up a constructor for class 
            Applicant(String name, String regNumber, boolean isActive) {
                this.name = name;
                this.regNumber = regNumber;
                this.isActive = isActive;
            }
            //method to return the required details
            public String toString() {
                return "Name: " + name + ", RegNumber: " + regNumber + ", Active: " + isActive;
            }}
            //i create an arraylist for the applicants
            static List<Applicant> applicants = new ArrayList<>();
    
            static {
                applicants.add(new Applicant("Nakintu Imaan", "23/U/0909", false));
                applicants.add(new Applicant("Ssentaayi Sharif", "23/U/1909", false));
                // more to be added from database of applicants as needed
            }

    rep_server(Scanner sc) {
        super(sc); // Call superclass constructor with Scanner object
    }
        // Validate credentials method
        public boolean validateCredentials(String username, String password) {
            return super.validateCredentials(username, password);
        }
    
        public static void main (String[] args) {
            ServerSocket server = null;
                try {
             server = new ServerSocket(8000);


    while (true){
        System.out.println("server is ready to interact with representative");
        Socket socket=server.accept();
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

     // Read username and password
     String username = dis.readUTF();
     String password = dis.readUTF();
    
    

        // Validate credentials
        rep_server serverInstance = new rep_server(new Scanner(System.in));
        //static instance
                if (serverInstance.validateCredentials(username, password)) {
                    dos.writeUTF("Login Successful");
                } else {
                    dos.writeUTF("Login Failed");
                    socket.close();
                    continue;
                }
    
        String msg = dis.readUTF();
        System.out.println("Representative:" +msg);
    
        while(!msg.equals("exit")){
            //condition for if rep uses View applicants command
            if (msg.equals("ViewApplicants")) {
                for (Applicant applicant : applicants) {
                    //call the method toString
                    dos.writeUTF(applicant.toString() + "\n");
                    //if he uses confirm_yes or no
                } 
             } else if (msg.startsWith("confirm_yes_") || msg.startsWith("confirm_no_")) {
                    String[] parts = msg.split("_");
                    String command = parts[1];
                    String regNumber = parts[2];
                    //then i iterate with a for loop to check each pupil
                    //initially set to false

                    for (Applicant applicant : applicants) {
                        //this  block ensures it only breaks out of the loop after processing the current applicant.
                        if (applicant.regNumber.equals(regNumber)) {
                           //when a yes is used
                            if (command.equals("yes")) {
                                applicant.isActive = true;
                                dos.writeUTF("Applicant " + applicant.name + " confirmed. \n");
                          //when a no is used instead
                            } else if (command.equals("no")) {
                                applicants.remove(applicant);
                                dos.writeUTF("Applicant " + applicant.name + " rejected.\n");
                            }break;
                        }
                    }//when no correct command is used this is thrown up
                } else {
                    dos.writeUTF("Invalid command.\n");
                }


                msg=dis.readUTF();
                System.out.println("Representative: "+msg);
           } } } catch (IOException e) {
            System.err.println("Could not start server on port 8000: " + e.getMessage());
                    logger.severe("Error closing resources: " + e.getMessage());;
        } catch (Exception e) {
                    logger.severe("Error closing resources: " + e.getMessage());;
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
        }
    }}

        }
        

