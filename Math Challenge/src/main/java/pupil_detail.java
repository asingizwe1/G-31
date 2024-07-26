import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
/*question imports
package com.demo.ExcelProject;import java.io.File;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

 * */


import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class pupil_detail {
    //initialise pupil attributes
    private int participantId;
    private String name;
    private String username;
    private String email;
    private String password;
    private String date_of_birth;
    private String schoolRegNo;
    private String image;
////////////////start//////////
private static String loggedInUsername;
    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }



    //default constructor .Default constructor: Initializes an empty Pupil object.
    public pupil_detail() {
    }

    //constructor.Parameterized constructor: Initializes a Pupil object with the given parameters.
    public pupil_detail(String name, String username, String email, String password, String date_of_birth, String schoolRegNo, String image) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.schoolRegNo = schoolRegNo;
        this.image = image;
    }
    //Setters and Getters: Methods to set and get the values of the private fields.
    //Setters
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public void setSchoolRegNo(String schoolRegNo) {
        this.schoolRegNo = schoolRegNo;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Getters
    public int getParticipantId() {
        return participantId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getSchoolRegNo() {
        return schoolRegNo;
    }

    public String getImage() {
        return image;
    }

    //Register new interested participant

    //gets printwriter in server to communicate directly to client
    public static void register(String[] req, PrintWriter out){
        if (req.length!=9){
            out.println("Missing parameters");
            out.println("");// helps break out of inner loop
        }else {
            String username = req[1];
            String name = req[2] +" "+ req[3];
            String email = req[4];
            String password = req[5];
            String dateOfBirth = req[6];
            String schoolRegNo = req[7];
            String imageFile = req[8];
            pupil_detail pupil = new pupil_detail(name, username, email, password, dateOfBirth, schoolRegNo, imageFile);
            System.out.println("Registering pupil: " + username); // Debug statement
            if (!Model.checkRegNo(pupil)) {
                out.println("School not registered, please contact the system administrator to register your school first");
                out.println("");
            } //else if (Model.checkUsername(pupil)) { issue on bug concerning existing user name in file
               // out.println("Username already taken,try another one");
               // out.println("");}
            else if (Model.checkStudentRegistration(pupil)) {
                out.println("Student already registered, please login to attempt challenges");
                out.println("");
            } else {
                pupil_detail.addPupilToFile(pupil);/////////////start////////////
                //email.sendEmailNotification(schoolRegNo, username); // Corrected call to send email to school representative
                out.println("Wait for confirmation email from the system administrator");
                out.println("");
            }
        }
    }

    // Method to access and manage questions
    public static void questionAccessing(int challengeNumber, int duration, PrintWriter printWriter, BufferedReader br) {
        ArrayList<Question> questions = new ArrayList<>();
        HashMap<Question, String> studentAnswers = new HashMap<>();

        long startTime = System.currentTimeMillis();
        long halfTime = duration * 30_000L; // Convert minutes to milliseconds
        long endTime = startTime + duration * 60_000L; // Convert minutes to milliseconds

        boolean hasWarnedHalfTime = false; // Flag to check if 50% warning has been shown

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "")) {
            String query = "SELECT q.Question_id, q.Question_text, q.Marks " +
                    "FROM question q " +
                    "WHERE q.challenge_number = " + challengeNumber;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int questionID = resultSet.getInt("Question_id");
                String questionText = resultSet.getString("Question_text");
                String marksString = resultSet.getString("Marks"); // Retrieve marks as String

                int marks = 0;
                try {
                    // Extract numeric part from marksString
                    marks = Integer.parseInt(marksString.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing marks: " + marksString);
                }

                questions.add(new Question(questionID, questionText, marks));
            }

            Collections.shuffle(questions);

            for (Question question : questions) {
                long currentTime = System.currentTimeMillis();

                if (currentTime >= endTime) {
                    printWriter.println("\u001B[33mYou have used 100% of your time.\u001B[0m"); // Yellow
                    break;
                } else if (currentTime >= startTime + halfTime && !hasWarnedHalfTime) {
                    printWriter.println("\u001B[31mYou have used 50% of your time!\u001B[0m"); // Red
                    hasWarnedHalfTime = true;
                }

                printWriter.println("Question: " + question.getQuestionText() + " (Marks: " + question.getMarks() + ")");
                printWriter.print("Your answer: ");
                printWriter.flush();

                String studentAnswer = br.readLine();
                studentAnswers.put(question, studentAnswer);

                printWriter.println("Enter 'nextQuestion' to fetch another question or 'endChallenge' to finish:");
                printWriter.flush();
                String command = br.readLine();

                if (command.equalsIgnoreCase("endChallenge")) {
                    printWriter.println("Challenge ended.");
                    printWriter.flush();
                    break;
                } else if (!command.equalsIgnoreCase("nextQuestion")) {
                    printWriter.println("Invalid command. Please enter 'nextQuestion' to fetch another question or 'endChallenge' to finish.");
                    printWriter.flush();
                }
            }

            // Process answers and store final marks here
            // Example: storeFinalMarks(username, calculateMarks(studentAnswers));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    //Allows logged in participant to view open challenges
    public static void attemptChallenge(PrintWriter printWriter, BufferedReader br) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "");
            System.out.println("Debug: Database connection established.");
            // Prompt student to enter the command 'viewChallenges'
            printWriter.println("Enter the command 'viewChallenges':");
            printWriter.flush();
            String command = br.readLine();
            System.out.println("Command received: '" + command + "'");
            if (command.equalsIgnoreCase("viewChallenges")) {
                // View available challenges
                String viewChallengesQuery = "SELECT Challengename FROM challenges";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(viewChallengesQuery);

                printWriter.println("Available challenges:");
                while (resultSet.next()) {
                    int challengeName = resultSet.getInt("Challengename");
                    printWriter.println("Challenge: " + challengeName);
                    System.out.println("Debug: Challenge retrieved: " + challengeName);
                }
                printWriter.flush();

                // Prompt student to enter a challenge name
                printWriter.println("Enter the challenge name you want to attempt:");
                printWriter.flush();
                int challengeName = Integer.parseInt(br.readLine());
                System.out.println("Debug: Challenge name entered: " + challengeName);


                // Check the date constraints and get the duration
                String timeManagementQuery = "SELECT opening_date, closing_date FROM challenges WHERE Challengename = " + challengeName;
                ResultSet rsTime = statement.executeQuery(timeManagementQuery);

                if (rsTime.next()) {
                    Date openingDate = rsTime.getDate("opening_date");
                    Date closingDate = rsTime.getDate("closing_date");
                    Date currentDate = new Date(System.currentTimeMillis());

                    printWriter.println("Opening Date: " + openingDate);
                    printWriter.println("Closing Date: " + closingDate);
                    printWriter.println("Current Date: " + currentDate);
                    printWriter.flush();
                    System.out.println("Debug: Dates - Opening: " + openingDate + ", Closing: " + closingDate + ", Current: " + currentDate);

                    if (currentDate.before(openingDate)) {
                        printWriter.println("The challenge has not been opened yet. It will open on: " + openingDate);
                        printWriter.flush();
                        System.out.println("Debug: Challenge not yet opened. It will open on: " + openingDate);
                        return;
                    } else if (currentDate.after(closingDate)) {
                        printWriter.println("The challenge has been closed.");
                        printWriter.flush();
                        System.out.println("Debug: Challenge has been closed.");
                        return;
                    }

                    // Get challenge duration
                    String durationQuery = "SELECT duration FROM challenges WHERE Challengename = " + challengeName;
                    ResultSet rsDuration = statement.executeQuery(durationQuery);

                    int duration = 0;
                    if (rsDuration.next()) {
                        duration = rsDuration.getInt("duration"); // Duration in minutes
                    }

                    if (duration > 0) {
                        printWriter.println("Challenge duration: " + duration + " minutes");
                        printWriter.flush();

                        // Call the questionAccessing method
                        questionAccessing(challengeName, duration, printWriter, br);
                        System.out.println("Debug: questionAccessing method called.");
                        // Prompt student to start attempting the challenge
                        printWriter.println("Enter the command 'attemptChallenge " + challengeName + "' to start attempting the challenge:");
                        printWriter.flush();
                        String attemptCommand = br.readLine();
                        System.out.println("Debug: Challenge duration: " + duration + " minutes");

                        if (attemptCommand.equalsIgnoreCase("attemptChallenge " + challengeName)) {
                            // Fetch questions for the challenge
                            String questionsQuery = "SELECT * FROM questions WHERE challenge_id = " + challengeName;
                            Statement questionStmt = connection.createStatement();
                            ResultSet questionRs = questionStmt.executeQuery(questionsQuery);

                            // Simulate question answering process
                            Map<Question, String> studentAnswers = new HashMap<>();
                            long startTime = System.currentTimeMillis();
                            long endTime = startTime + TimeUnit.MINUTES.toMillis(duration);

                            while (System.currentTimeMillis() < endTime && questionRs.next()) {
                                String questionText = questionRs.getString("question_text");
                                int marks = questionRs.getInt("marks");
                                printWriter.println("Question: " + questionText + " (Marks: " + marks + ")");
                                printWriter.println("Your answer: ");
                                printWriter.flush();
                                System.out.println("Debug: Displaying question: " + questionText + " (Marks: " + marks + ")");


                                String studentAnswer = br.readLine();
                                studentAnswers.put(new Question(questionRs.getInt("question_id"), questionText, marks), studentAnswer);

                                printWriter.println("Enter the command 'nextQuestion' to fetch another question, or 'endChallenge' to finish:");
                                printWriter.flush();
                                String nextCommand = br.readLine();
                                System.out.println("Debug: Next command received: '" + nextCommand + "'");


                                if (nextCommand.equalsIgnoreCase("endChallenge")) {
                                    printWriter.println("Challenge ended.");
                                    printWriter.flush();
                                    break;
                                } else if (!nextCommand.equalsIgnoreCase("nextQuestion")) {
                                    printWriter.println("Invalid command. Please enter 'nextQuestion' to fetch another question or 'endChallenge' to finish.");
                                    printWriter.flush();
                                }
                            }

                            // After challenge ends, you can implement the answer comparison here

                        } else {
                            printWriter.println("Invalid command. Please enter 'attemptChallenge " + challengeName + "' to start attempting the challenge.");
                            printWriter.flush();
                        }
                    } else {
                        printWriter.println("Invalid challenge or duration.");
                        printWriter.flush();


                    }
                } else {
                    printWriter.println("Challenge not found.");
                    printWriter.flush();
                    System.out.println("Debug: Challenge not found for ID: " + challengeName);
                }

                connection.close();
                System.out.println("Debug: Database connection closed.");
            } else {
                printWriter.println("Invalid command. Please enter 'viewChallenges' to view available challenges.");
                printWriter.flush();
                System.out.println("Debug: Invalid command. Command received: " + command);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Debug: Exception caught - " + e.getMessage());
        }
    }
    static class Question {
        int Question_id;
        String Question_text;
        int Marks;


        // Constructor for full question details
        public Question(int id, String questionText, int marks) {
            this.Question_id = id;
            this.Question_text = questionText;
            this.Marks = marks;
        }

        // Getters
        public String getQuestionText() {
            return Question_text;
        }

        public int getMarks() {
            return Marks;
        }

        @Override
        public String toString() {
            return "Question ID: " + Question_id + ", Question: " + Question_text + ", Marks: " + Marks;
        }
    }
///////////////start////////////////
// Method to store final marks in the database
public static void storeFinalMarks(String username, int marks) {
    try (Connection conn = Model.createConnection()) {
        String sql = "INSERT INTO results (username, marks) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setInt(2, marks);
        pstmt.executeUpdate();
        System.out.println("Final marks stored successfully for user: " + username);
    } catch (SQLException e) {
        System.out.println("Error storing final marks: " + e.getMessage());
    }
}


    //Allows logged in participant to view open challenges
    public static void viewChallenges(PrintWriter printWriter){
        String chal=null;
        try(Connection conn = Model.createConnection();){

            String sql = "SELECT Challenge_number, challenge_name from challenge";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                chal=rs.getString("Challenge_number")+"."+rs.getString("challenge_name");
                printWriter.println(chal);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        };
    }

    /*

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void attemptChallenge(){
package com.demo.ExcelProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadInvoices {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String JDBC_USERNAME = "your_username";
    private static final String JDBC_PASSWORD = "your_password";
    private static final int NUM_QUESTIONS = 10;
//you first clarify challangeNum
//The PrintWriter is used to send output to the client such as error messages
//The BufferedReader is used to read input from the client.This allows the server to receive data sent by the client
//This array is used to pass multiple pieces of information to the method.
     public static void attemptChallenge(PrintWriter printWriter, BufferedReader br, String[] req){

        try(Connection conn = Model.createConnection();){

            String sql = "SELECT challengeNum from challenge";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String tempID = rs.getString("ChallengeID");
                if(req[1].equals(tempID)){
                    Question.retrieveQuestion(printWriter, br);
                }
                else{
                    String error = "ChallengeID not recognised";
                    printWriter.println(error);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        };
///////////////////////////////////

    public static void main(String[] args) {
        List<QuestionAnswerPair> questionsAndAnswers = readQuestionsAndAnswersFromDatabase();
        // Now questionsAndAnswers can be used for further processing or storing in a database
    }

    public static List<QuestionAnswerPair> readQuestionsAndAnswersFromDatabase() {
        List<QuestionAnswerPair> questionsAndAnswers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement questionStatement = null;
        PreparedStatement answerStatement = null;
        ResultSet questionResultSet = null;
        ResultSet answerResultSet = null;

        try {
            // Establish connection
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

            // Prepare statement to fetch questions
            String questionQuery = "SELECT question_id, question_text FROM questions ORDER BY RAND() LIMIT ?";
            questionStatement = connection.prepareStatement(questionQuery);
            questionStatement.setInt(1, NUM_QUESTIONS);
            questionResultSet = questionStatement.executeQuery();

            // Iterate through questions
            while (questionResultSet.next()) {
                int questionId = questionResultSet.getInt("question_id");
                String questionText = questionResultSet.getString("question_text");

                // Prepare statement to fetch answer for each question
                String answerQuery = "SELECT answer_text FROM answers WHERE question_id = ?";
                answerStatement = connection.prepareStatement(answerQuery);
                answerStatement.setInt(1, questionId);
                answerResultSet = answerStatement.executeQuery();

                // Assuming there's only one correct answer per question
                if (answerResultSet.next()) {
                    String correctAnswer = answerResultSet.getString("answer_text");
                    questionsAndAnswers.add(new QuestionAnswerPair(questionText, correctAnswer));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close JDBC objects in finally block
            try {
                if (answerResultSet != null) {
                    answerResultSet.close();
                }
                if (answerStatement != null) {
                    answerStatement.close();
                }
                if (questionResultSet != null) {
                    questionResultSet.close();
                }
                if (questionStatement != null) {
                    questionStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return questionsAndAnswers;
    }

    // Class to hold question and answer pair
    static class QuestionAnswerPair {
        private final String question;
        private final String answer;

        public QuestionAnswerPair(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}

}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//will work later
    //Allows user to attempt a challenge they are interested in
    public static void attemptChallenge(PrintWriter printWriter, BufferedReader br, String[] req){

        try(Connection conn = Model.createConnection();){

            String sql = "SELECT ChallengeID from Challenge";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                String tempID = rs.getString("ChallengeID");
                if(req[1].equals(tempID)){
                    Question.retrieveQuestion(printWriter, br);
                }
                else{
                    String error = "ChallengeID not recognised";
                    printWriter.println(error);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        };
    }*/

    //check if reg no supplied is in the database

    //to add a pupil to a file

    public static void addPupilToFile(pupil_detail pupil){
        //from applicants to pupils.txt......
        //Filewriter is a class in java.io package used to write character files.
        //buffered writer is a wrapper around Writer that provides buffering for improved efficiency
        //buffering can increase performance when writing large amouts of data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("applicants.txt",true));
        )// BufferedWriter: Wraps the FileWriter in a BufferedWriter for efficient writing.
        //FileWriter: Creates a FileWriter object to write to the file.
        {
            writer.write(pupil.getName() + " " + pupil.getUsername() + " " + pupil.getEmail() + " " + pupil.getPassword() + " " + pupil.getDate_of_birth() + " " + pupil.getSchoolRegNo() + " " + pupil.getImage() + "\n");
//Writing to File: Writes the pupil's details to the file.
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    //put all applicants into an arraylist
    public static ArrayList<pupil_detail> addToArrayList(String username){
      //reads pupil details from applicants.txt and adds them to an ArrayList:
        ArrayList<pupil_detail> applicants = new ArrayList<>();
        //Creates an ArrayList to hold pupil_detail objects.
        String school = "applicants";
        String filename = school + ".txt";
        //BufferedReader: Reads the file line by line using a BufferedReader.
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
//Splits each line into parts and creates a pupil_detail object for each line.
            while ((line = br.readLine()) != null){
                String[] parts = line.trim().split(" ");
                pupil_detail pupil = new pupil_detail();
//Adding to List: Adds each pupil_detail object to the ArrayList.
                pupil.setName(parts[0]+ " " + parts[1]);
                pupil.setUsername(parts[2]);
                pupil.setEmail(parts[3]);
                pupil.setPassword(parts[4]);
                pupil.setDate_of_birth(parts[5]);
                pupil.setSchoolRegNo(parts[6]);
                pupil.setImage(parts[7]);
                applicants.add(pupil);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return applicants;
    }

    //to delete a pupil from the file after being confirmed or rejected
    public static void deleteFromFile(String username){
      //The deleteFromFile method deletes a pupil from the applicants.txt file:
        ArrayList<pupil_detail> applicants = addToArrayList(username);
        // Reads all pupils into an ArrayList using the addToArrayList method.
        String school = "applicants";
        String filename = school + ".txt";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename));) {
            for (pupil_detail pupil : applicants) {

                if (!pupil.getUsername().equals(username)) {
                    bw.write(pupil.getName() + " " + pupil.getUsername() + " " + pupil.getEmail() + " " + pupil.getPassword() + " " + pupil.getDate_of_birth() + " " + pupil.getSchoolRegNo() + " " + pupil.getImage() + "\n");
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    //to string
    public String toString() {
        return "Pupil{" +
                "participantId=" + participantId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", schoolRegNo='" + schoolRegNo + '\'' +
                ", image='" + image + '\'' +
                '}';
    }


}

////////start///////
////////start//////

