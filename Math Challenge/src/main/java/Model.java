import java.time.LocalDate;
import java.util.List;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Model {

    public static Connection createConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mathschallenges";
        String username="root";
        String password = "";

        return DriverManager.getConnection(url,username,password);
    }
    //Checks if a given school registration number exists in the School t able.
    //checking if a given school registration number is valid
    public static boolean checkRegNo(pupil_detail pupil){

        try(Connection con =Model.createConnection()){
            String sql ="SELECT 1 FROM schoolrep where School_reg_no = ?";

            PreparedStatement st =con.prepareStatement(sql);
            st.setString(1,pupil.getSchoolRegNo());
            ResultSet rs = st.executeQuery();//: Executes the query and retrieves the result.
            // Checks if the result set is empty. If it is, the school registration number does not exist.
            if(!rs.isBeforeFirst()){
                return false;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }


    public static boolean checkRepRegNo(SchoolRepresentative rep){

        try(Connection con =Model.createConnection()){
            String sql ="SELECT 1 FROM school where school_reg_No = ?";

            PreparedStatement st =con.prepareStatement(sql);
            st.setString(1,rep.getSchool_reg_no());
            ResultSet rs = st.executeQuery();//: Executes the query and retrieves the result.
            // Checks if the result set is empty. If it is, the school registration number does not exist.
            if(!rs.isBeforeFirst()){
                return false;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }


    public static void registerRepresentative(SchoolRepresentative rep) {
        String sql = "INSERT INTO verified_representative (username, name, email, password, school_reg_no) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = createConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, rep.getUsername());
            st.setString(2, rep.getName());
            st.setString(3, rep.getEmail());
            st.setString(4, rep.getPassword());
            st.setString(5, rep.getSchool_reg_no());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("School Representative registered successfully.");
            } else {
                System.out.println("Failed to register School Representative.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





    //checking if a given student is already registered basing on their name and date of birth
    public static boolean checkStudentRegistration(pupil_detail pupil){
        String sql = "SELECT 1 FROM pupil where name = ? and date_of_birth = ?";
        PreparedStatement st= null;
        ResultSet rs = null;
        boolean isRegistered = false;
        try(Connection con = Model.createConnection()){
            st = con.prepareStatement(sql);
            st.setString(1,pupil.getName());//: Sets the name parameter.
            st.setString(2,pupil.getDate_of_birth());
            rs = st.executeQuery();// Executes the query and retrieves the result.
            isRegistered= rs.isBeforeFirst();//Checks if the result set is not empty, indicating the student is already registered.
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return isRegistered;
    }


    //check if a username is taken
    public static boolean checkUsername(pupil_detail pupil){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM pupil where username = ?";
            PreparedStatement st = con.prepareStatement(sql);// Prepares the SQL statement.
            st.setString(1,pupil.getUsername());//Sets the username parameter.
            ResultSet rs = st.executeQuery();//Executes the insert operation.
            if(!rs.isBeforeFirst()){//Checks if the result set is empty, indicating the username is not taken.
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //add a pupil to the database
    public static void updatePupil(pupil_detail pupil){
        String sql = "insert into participant(name,username,email,password,DateOfBirth,school_reg_no) values(?,?,?,?,?,?)";
//: Inserts a rejected pupil into the rejected table.
        try(Connection con = Model.createConnection();) {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, pupil.getName());
            st.setString(2, pupil.getUsername());
            st.setString(3, pupil.getEmail());
            st.setString(4, pupil.getPassword());
            st.setString(5, pupil.getDate_of_birth());
            st.setString(6, pupil.getSchoolRegNo());
            st.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
/*
    public static void saveSubmissionsToDatabase(ArrayList<pupil_detail.Question> submissions) {
        String insertQuery = "INSERT INTO _Challenge_Submission (SubID, PupilID, ChID, Question_id, AnswerFromStudent, DateSubmitted) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            for (pupil_detail.Question question : submissions) {
                preparedStatement.setString(1, question.);
                preparedStatement.setInt(2, question.PupilID);
                preparedStatement.setInt(3, question.ChID);
                preparedStatement.setInt(4, question.Question_id);
                preparedStatement.setString(5, question.AnswerFromStudent);
                preparedStatement.setDate(6, java.sql.Date.valueOf(question.DateSubmitted));

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
    public static int checkForSubId(String column, String table) {
        Random random = new Random();
        int minimum = 10;
        int maximum = 1000000;

        while (true) {
            int subId = random.nextInt((maximum - minimum + 1)) + minimum;
            try (Connection connection = createConnection()) {
                String query = "SELECT COUNT(*) FROM " + table + " WHERE " + column + "=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, subId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    return subId;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //update rejected table if pupil is rejected
    public static void updateRejected(pupil_detail pupil){
        String sql = "insert into rejected(name,username,email,password,date_of_birth,school_reg_no) values(?,?,?,?,?,?)";

        try(Connection con = Model.createConnection();) {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, pupil.getName());
            st.setString(2, pupil.getUsername());
            st.setString(3, pupil.getEmail());
            st.setString(4, pupil.getPassword());
            st.setString(5, pupil.getDate_of_birth());
            st.setString(6, pupil.getSchoolRegNo());
            st.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    //////////////////start///////////////

//
/* Method to retrieve salt for a username
public static String retrieveSaltByUsername(String username) {
    String salt = null;

    try (Connection con = createConnection()) {
        String sql = "SELECT password FROM users WHERE username = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            salt = rs.getString("salt");
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    return salt;
}

    */

    //check if a supplied username and password match for a given pupil
    public static boolean checkPupilLogin(String username, String password){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM participant where username = ? and password = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,username);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            if(!rs.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //check if a supplied username and password match for a given school representative
    public static boolean checkSRLogin(String username, String password){
        try(Connection con = Model.createConnection()){
            String sql = "SELECT 1 FROM verified_representative where username = ? and pwd = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1,username);
            st.setString(2,password);
            ResultSet rs = st.executeQuery();
            if(!rs.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    ////////////////
    // Method to store image in the directory and update the database with the file path
    public static void storeImageInDirectory(String username, byte[] imageBytes) {
        // Create directory for storing images if it doesn't exist
        String directoryPath = "images";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Save the image bytes to a file
        String filePath = directoryPath + "/" + username + ".jpg";
        try {
            Files.write(Paths.get(filePath), imageBytes);
        } catch (IOException e) {
            System.err.println("Error saving image to directory: " + e.getMessage());
            return;
        }

        // Update the database with the image file path
        String sql = "UPDATE pupil SET image_path = ? WHERE username = ?";
        try (Connection con = createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, filePath);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating database with image path: " + e.getMessage());
        }
    }

    // Method to get challenge duration
    public static int getChallengeDuration(int challengeNumber) {
        String query = "SELECT duration FROM challenges WHERE Challengename = ?";
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, challengeNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("duration");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Method to get challenge details
    public static Challenge getChallengeDetails(int challengeNumber) {
        String query = "SELECT opening_date, closing_date FROM challenges WHERE Challengename = ?";
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, challengeNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Challenge(resultSet.getDate("opening_date"), resultSet.getDate("closing_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get questions for a challenge
    public static List<Question> getChallengeQuestions(int challengeNumber) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT Question_id, Question_text, Marks FROM questions WHERE challenge_number = ?";
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, challengeNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int questionID = resultSet.getInt("Question_id");
                String questionText = resultSet.getString("Question_text");
                int marks = resultSet.getInt("Marks");
                questions.add(new Question(questionID, questionText, marks));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Inner class to represent a challenge
    public static class Challenge {
        private final Date openingDate;
        private final Date closingDate;

        public Challenge(Date openingDate, Date closingDate) {
            this.openingDate = openingDate;
            this.closingDate = closingDate;
        }

        public Date getOpeningDate() {
            return openingDate;
        }

        public Date getClosingDate() {
            return closingDate;
        }
    }

    // Inner class to represent a question
    public static class Question {
        private final int id;
        private final String questionText;
        private final int marks;

        public Question(int id, String questionText, int marks) {
            this.id = id;
            this.questionText = questionText;
            this.marks = marks;
        }

        public int getId() {
            return id;
        }

        public String getQuestionText() {
            return questionText;
        }

        public int getMarks() {
            return marks;
        }
    }

    }
class Question {
    String AnswerFromDatabase;
    String AnswerFromStudent;
    String SubID;//m
    int PupilID;//m
    int ChID;//m
    String QnMarks;
    LocalDate DateSubmitted;
    String Challenge_finishedStatus;
    int Question_id;
    String Question_text;
    int Marks;//m

    public Question(int question_id, String question_text, int marks) {
        this.Question_id = question_id;
        this.Question_text = question_text;
        this.Marks = marks;
    }

    public String toString(){
        return AnswerFromDatabase+"\n"+AnswerFromStudent+"\n"+SubID+"\n"+ChID+"\n"+"\n"+DateSubmitted+"\n"+Question_id+"\n"+Question_text+"\n"+Marks;
    }
}


///////////////start//////////////
// Reads an image file and converts it to a byte array

