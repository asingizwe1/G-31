import java.io.ObjectOutputStream;
import java.sql.Connection;


import java.beans.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class s_r {
    ///start////
    public static void viewChallenges(PrintWriter out) {
        try (Connection connection = Model.createConnection()) {
            String query = "SELECT Challengename FROM challenges";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            out.println("Available challenges:");
            while (resultSet.next()) {
                String challengeName = resultSet.getString("Challengename");
                out.println("Challenge: " + challengeName);
            }out.flush(); // Ensure the output is sent to the client
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Vendor Error: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    // Method to get the duration of the challenge

//////////start/////////
    //////stop////////
    // TimeManagement method

    ////end////

    //Login participant move to login class
    public static void login(String[] req, PrintWriter out,BufferedReader in) throws IOException {
        if (req.length != 4) {
            out.println("Missing parameters");
            out.println("");// when client receices empt string it should break out
        }else {
            String username = req[2];
            String password = req[3];
            outerlogin:
            {//case change
                 while (true) {

                     switch (req[1]) {

                    case "pupil":
                        //model db class
                        //

                           /*
                            String salt = Model.retrieveSaltByUsername(username);// Retrieve salt for the username from database
                                    String hashedPassword = hashPassword(password, salt);
                            */

                            if (Model.checkPupilLogin(username, password)) {
                                out.println("Greetings " + username + ", welcome to the math quiz for gurus, Algebrate us.");
                                out.println(" USE COMMAND <viewChallenges> to view available challenges. ");
                                out.println(" USE COMMAND <attemptChallenge> <ChallengeNumber> to view available challenges. ");
                                out.println(""); // whenever you send empty string prompts

                                while (true) {
                                    String clientRequest = in.readLine();
                                    System.out.println(username + " sent: " + clientRequest + " command");
                                    String[] actions = clientRequest.trim().split(" ");
                                    switch (actions[0]) {
                                        case "viewChallenges":
                                           s_r.viewChallenges(out);
                                            out.println();
                                            out.flush();
                                            break;
                                        case "attemptChallenge":
                                            out.println("Starting challenge " + actions[1]);
                                            out.flush();
                                            break;
                                        case "logout":
                                            out.println("logging out...");
                                            out.println();
                                            out.flush();
                                            break outerlogin;
                                        default:
                                            out.println("Invalid command");
                                            out.println();
                                            out.flush();
                                            break;
                                    }
                                }
                            } else {
                                out.println("Invalid username or password");
                                out.println("");}
                            break;
                            case "rep":
                                if (Model.checkSRLogin(username, password)) {
                                    out.println(" Login verified: " + username + " ");
                                    out.println("              -------------------                    ");
                                    out.println("Considered commands:\nviewApplicants\nconfirmApplicant yes / no username");
                                    out.println("");

                                    while (true) {
                                        String clientRequest = in.readLine();
                                        System.out.println(username + " sent: " + clientRequest + " command");
                                        String[] actions = clientRequest.trim().split(" ");
                                        switch (actions[0]) {
                                            case "viewApplicants":
                                                SchoolRepresentative.viewApplicants("applicants", out);
                                                out.println();
                                                break;
                                            case "confirmApplicant":
                                                SchoolRepresentative.confirmApplicant(actions[2], actions[1], out);
                                                out.println();
                                                break;
                                            case "logout":
                                                out.println("logging out...");
                                                out.println();
                                                break outerlogin;
                                            default:
                                                out.println("Invalid command");
                                                out.println();
                                                break;
                                        }
                                    }
                                } else {
                                    out.println("Invalid username or password");
                                    out.println("");
                                    break;
                                }
                            default:
                                out.println("Invalid login type");
                                out.println("");
                                break;
                        }
                }

                }
            }
            }
        }




/*
    public static void attemptChallenge(PrintWriter PrintWriter, BufferedReader br, String username){
        String challengeNumber=req[1]
                int participationId=Model.getPupilId(username);
        LocalDateTime startTime;
        int score=0;

        String sql = "SELECT _ from _ where cahllenge_no = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(l.req[1];);
        ResultSet rs = stmt.executeQuery();
        if(rs.isBeforeFirst()){
            while(true){
                printWriter.println("getting questions...");
                startTime= LocalDateTime.now();
                score=  Question.retrieveQuestion(printWriter,br,Integer.parseInt(ChallengeNumber),participantId,startTime);
                endTime= LocalDateTime.now();
                printWriter.println("finished trial");
                printWriter.println("Total marks"+ score);
                printWriter.println("_____");
                //update the attempted challenge table
                Model.recordChallenge(Integer.parseInt(challengeNumber),participantId,startTime,endTime,score);
                //allow the pupil two more redos after the first attempt
                if(redo<3){
                    printWriter.println("try it again (y/n)"){
                        printWriter.println();
                        String redo=br.readLine();
                        if(redo.equalsIgnoreCase("y")){
                            redo++;
                        }else{
                            printWriter.println("you have depleted your redos.")
                        printWriter.println();
                            break;
                        }
                    }
                }else{
                    printWriter.println("Unable to locate the challenge.");
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }catch(IOException e){
                throw new RuntimeException;
            }
        }



    }



    //security reasons for password
    static class hashed_password {
        public static passwordhash(String password, String salt) {
            byte[] salted_password = password.getBytes();
            try {
                MessageDigest md = MessageDigest.getInstance("");
                md.update(salt.getBytes());
                salted_password = md.digest(password.getBytes());
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
        }
        return Base64.getEncoder().

        encodeToString(salted_password);
    }

    //generate a salt for hashing
    public static String generateSalt(){
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);



    }
}
*/