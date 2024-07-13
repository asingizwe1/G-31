import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Base64;




public class s_r {
    public static String hashPassword(String password, String salt) {
        byte[] saltedPassword = (password + salt).getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            saltedPassword = md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return Base64.getEncoder().encodeToString(saltedPassword);
    }

    // Method to generate a salt for hashing
    public static String generateSalt() {
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[16];
        rnd.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }


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
                switch (req[1]) {
                    case "pupil":
                        //model db class
                        //
                        if (Model.checkPupilLogin(username, password)) {
                            //out.println("Login successful");
                            out.println("Greetings " + username + ", welcome to the math quizz for gurs,Algebrate us.");
                            out.println("Considered commands:\nviewChallenges\nattemptChallenge <challenge_no>");
                            out.println("");//wenever u send empty string promps
                           /*
                            String salt = Model.retrieveSaltByUsername(username);// Retrieve salt for the username from database
                                    String hashedPassword = hashPassword(password, salt);
                            */
                            while(true) {
                                String clientRequest = in.readLine();
                                System.out.println(username + " sent: " + clientRequest + " command");
                                String[] actions = clientRequest.trim().split(" ");//we r now logged in
                                switch (actions[0]) {
                                    //Challenges available after login
                                    case "viewChallenges":
                                        pupil_detail.viewChallenges(out);
                                        out.println();
                                        break;

                                    case "attemptChallenge":
                                        //call appropriate method
                                        out.println();
                                        break;
                                    case "logout":
                                        out.println("logging out...");
                                        out.println();
                                        break outerlogin;
                                }
                            }
                        } else {
                            out.println("Invalid username or password");
                            out.println("");
                            break;
                        }

                    case "rep":
                        if (Model.checkSRLogin(username, password)) {
                            out.println(" Login verified: " + username + " ");
                            out.println("              -------------------                    ");
                            out.println("Considered commands:\nviewApplicants\nconfirmApplicant <y>(yes)/<n>(no) username");
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



}/*
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