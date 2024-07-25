import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ChallengeManagement {

    public static void attemptChallenge() {
        ChallengeManagement cm = new ChallengeManagement();
        Scanner scanner = new Scanner(System.in);

        // Prompt student to enter the command 'viewChallenges'
        System.out.println("Enter the command 'viewChallenges':");
        String command = scanner.nextLine();

        if (command.equalsIgnoreCase("viewChallenges")) {
            cm.viewChallenges();
            // Prompt student to enter a challenge name
            System.out.println("Enter the challenge name you want to attempt:");
            int challengeName = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check the date constraints and get the duration
            cm.timeManagement(challengeName); // Check timing constraints

            int duration = cm.getChallengeDuration(challengeName);
            if (duration > 0) {
                // Prompt student to start attempting the challenge
                System.out.println("Enter the command 'attemptChallenge " + challengeName + "' to start attempting the challenge:");
                String attemptCommand = scanner.nextLine();

                if (attemptCommand.equalsIgnoreCase("attemptChallenge " + challengeName)) {
                    cm.questionAccessing(challengeName, duration);
                } else {
                    System.out.println("Invalid command. Please enter 'attemptChallenge " + challengeName + "' to start attempting the challenge.");
                }
            } else {
                System.out.println("Invalid challenge or duration.");
            }
        } else {
            System.out.println("Invalid command. Please enter 'viewChallenges' to view available challenges.");
        }

        scanner.close();
    }

    // Method to display available challenges
    public static String viewChallenges() {
        String challengeName="";
        String challenges="";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "");
            String query = "SELECT Challengename FROM challenges";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //System.out.println("Available challenges:");
            while (resultSet.next()) {
                challengeName =    String.valueOf( resultSet.getInt("Challengename"));
                challenges+="Challange: "+challengeName+"\n";
                //System.out.println("Challenge: " + challengeName);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Vendor Error: " + e.getErrorCode());
            e.printStackTrace();
        }
        return challenges;
    }

    // Method to get the duration of the challenge
    public int getChallengeDuration(int challengeName) {
        int duration = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "");
            String query = "SELECT duration FROM challenges WHERE Challengename = " + challengeName;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                duration = resultSet.getInt("duration"); // Duration in minutes
            } else {
                System.out.println("Challenge not found or has no duration.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return duration;
    }

    // TimeManagement method
    public void timeManagement(int challengeName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "");

            String query = "SELECT opening_date, closing_date FROM challenges WHERE Challengename = " + challengeName;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Date openingDate = resultSet.getDate("opening_date");
                Date closingDate = resultSet.getDate("closing_date");
                Date currentDate = new Date(System.currentTimeMillis());

                System.out.println("Opening Date: " + openingDate);
                System.out.println("Closing Date: " + closingDate);
                System.out.println("Current Date: " + currentDate);

                if (currentDate.before(openingDate)) {
                    System.out.println("The challenge has not been opened yet. It will open on: " + openingDate);
                    System.exit(0);
                } else if (currentDate.after(closingDate)) {
                    System.out.println("The challenge has been closed. It closed on: " + closingDate);
                    System.exit(0);
                } else {
                    System.out.println("The challenge is currently open. You may proceed.");
                }
            } else {
                System.out.println("Challenge not found.");
                System.exit(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void questionAccessing(int challengeNumber, int duration) {
        ArrayList<Question> questions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        HashMap<Question, String> studentAnswers = new HashMap<>();

        long startTime = System.currentTimeMillis();
        long halfTime = duration * 30_000L; // Convert minutes to milliseconds
        long endTime = startTime + duration * 60_000L; // Convert minutes to milliseconds

        boolean hasWarnedHalfTime = false; // Flag to check if 50% warning has been shown

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mathschallenge", "root", "");

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

            while (true) {
                long currentTime = System.currentTimeMillis();

                if (currentTime >= endTime) {
                    System.out.println("\u001B[33mYou have used 100% of your time.\u001B[0m"); // Yellow
                    break;
                } else if (currentTime >= startTime + halfTime && !hasWarnedHalfTime) {
                    System.out.println("\u001B[31mYou have used 50% of your time!\u001B[0m"); // Red
                    hasWarnedHalfTime = true; // Set flag to true so the warning isn't shown again
                }

                if (questions.isEmpty()) {
                    System.out.println("No more questions available for this challenge.");
                    break;
                }

                // Calculate remaining time in minutes
                long remainingTimeMillis = endTime - currentTime;
                int remainingMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);

                // Display the number of remaining questions and remaining time
                System.out.println("Remaining questions: " + questions.size());
                System.out.println("Remaining time: " + remainingMinutes + " minutes");

                Question question = questions.remove(0);

                // Display the question along with its marks
                System.out.println("\u001B[34mQuestion: " + question.Question_text + " (Marks: " + question.Marks + ")\u001B[0m"); // Blue
                System.out.print("Your answer: ");
                String studentAnswer = scanner.nextLine();

                // Collect student answers
                studentAnswers.put(question, studentAnswer);

                System.out.println("Enter the command 'nextQuestion' to fetch another question, or 'endChallenge' to finish:");
                String nextCommand = scanner.nextLine();
                if (nextCommand.equalsIgnoreCase("endChallenge")) {
                    System.out.println("Challenge ended.");
                    break;
                } else if (!nextCommand.equalsIgnoreCase("nextQuestion")) {
                    System.out.println("Invalid command. Please enter 'nextQuestion' to fetch another question or 'endChallenge' to finish.");
                }
            }

            // After the challenge ends, you can implement the answer comparison here

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

// Assuming the Question class is defined like this

