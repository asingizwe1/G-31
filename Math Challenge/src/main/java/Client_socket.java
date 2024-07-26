//Socket: Class for network communication.
//BufferedReader, IOException, InputStreamReader, PrintWriter: Classes for handling input/output streams and exceptions.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;


import java.util.UUID;
//various import statements for classes to be used.
class Client_socket {
    private static final Map<pupil_detail.Question, Long> timeSpentOnQuestions = new HashMap<>();
    private static final Set<Integer> completedChallenges = new HashSet<>();
    public static void main(String[] args) {
//        System.out.println("Client started..");
        // Try-with-resources statement to ensure the Socket resource is closed automatically.
        try(Socket soc = new Socket("127.0.0.1",2212);){
            // BufferedReader to read text from the input stream of the socket.
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));//Initializes a BufferedReader to read text from the input stream of the socket.This will handle incoming data from the server.
            // PrintWriter to write text to the output stream of the socket, with auto-flush enabled.
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);//Initializes a PrintWriter to write text to the output stream of the socket, with auto-flush enabled. This will handle outgoing data to the server.
//////////start////
            /////////////////
            // Call the runClient method to handle the client-server communication.
            runClient(out, in);//Calls the runClient method, passing the PrintWriter and BufferedReader for communication with the server.


        }catch(IOException e){ // Print any IOException that occurs during the socket connection or communication.
            System.out.println(e.getMessage());
        }

    }

    public static void runClient(PrintWriter out, BufferedReader in) throws IOException {
        // Declare variables for request and response strings.
        String request;//Declares a variable to hold the user input that will be sent to the server.
        String response;//Declares a variable to hold the server's response.
        Scanner scanner = new Scanner(System.in);
        // Display the menu with available commands
        showMenu();
        outer:do{
            // Prompt the user for input.
            System.out.print("MATH BOT>>");
            // Read the user's input (request) from the console.
            request = scanner.nextLine();
            // above: Send the request to the server via the PrintWriter.
            out.println(request);//Prints the server's response to the console.
            //above:  If the request is "done", exit the loop.
            if(request.equalsIgnoreCase("done")){
                break;
            }

            // Inner loop to read and print the server's responses.
            do {
                // Read a line of response from the server.
                response = in.readLine();
                //if(response==starting questions){new Thread(new Runnable{})}
                // Print the server's response to the console.
                System.out.println(response);//respose is from server

////start///////



                //////////////

                // If the server's response is "logging out...", break the outer loop to exit.
                if(response.equals("logging out...")){
                    break outer;//when ""
                }

                // Start handling challenge if the server sends the signal
                if (response.startsWith("Starting challenge")) {
                    int challengeNumber = Integer.parseInt(response.split(" ")[2]);
                    manageTimeAndAccessQuestions(challengeNumber, out, in);
                    while (true) {
                        System.out.println("Enter the challenge number to continue or 'done' to exit:");
                        String nextRequest = scanner.nextLine();

                        if (nextRequest.equalsIgnoreCase("done")) {
                           //////////////
                            break ;
                        }

                        try {
                            int newChallengeNumber = Integer.parseInt(nextRequest);
                            out.println(nextRequest); // Send the new challenge number to the server
                            manageTimeAndAccessQuestions(newChallengeNumber, out, in);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid command. Please enter a valid challenge number or 'done' to exit.");
                        }


                    }
                }
/////////////time to return form//////////

//String Anserfromclient=scanner.nextLine();
               // out.println(Anserfromclient);
            } while (!response.isEmpty());
            // Continue reading until an empty response is received.


        }while(!request.equalsIgnoreCase("done"));
        // Continue until the user enters "done".

        scanner.close();  // Close the scanner.
        // Recursive call to runClient to restart the client (this might cause a stack overflow if not handled properly).
        runClient(out,in);//Recursively calls runClient to restart the client (could cause stack overflow if not managed carefully).
    }

    public static void showMenu() {
        // Define a multiline string with instructions for the user.
        String instructionSet1 = """
                                            MATHEMATICS ONLINE SYSTEM
                Commands to use:
                >registerPupil username firstname lastname email password DateOfBirth school_reg_no imageFilePath -> for registering pupil.
                
                >registerRep username firstname lastname email password school_reg_no  -> for registering Representative.
                
                >login pupil / rep(representative) username password -> for logging into the system.

                >done -> to exit the system
            
                """;
        // Print the instruction set to the console.
        System.out.println(instructionSet1);

    }
/////////start///////////
    //////////////////


    public static int getChallengeDuration(int challengeNumber) {

        if (completedChallenges.contains(challengeNumber)) {
            System.out.println("You have already completed this challenge. Please select another challenge.");
            System.out.flush();

        }

        if (completedChallenges.size() >= 3) {
            System.out.println("You have reached the maximum number of challenges. Please try again later.");
            System.out.flush();

        }

        int duration = 0;
        try (Connection connection = Model.createConnection()) {
            String query = "SELECT duration FROM challenges WHERE Challengename = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, challengeNumber);
            ResultSet resultSet = statement.executeQuery();

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
    // Define the Submissionquestions ArrayList to store submissions
    private static ArrayList<pupil_detail.Question> Submissionquestions = new ArrayList<>();



    public static void manageTimeAndAccessQuestions(int challengeNumber, PrintWriter out, BufferedReader in) {
        try (Connection connection = Model.createConnection()) {
            // Time Management Logic
            String query = "SELECT opening_date, closing_date FROM challenges WHERE Challengename = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, challengeNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Date openingDate = resultSet.getDate("opening_date");
                Date closingDate = resultSet.getDate("closing_date");
                Date currentDate = new Date(System.currentTimeMillis());

                out.println("Opening Date: " + openingDate);
                out.println("Closing Date: " + closingDate);
                out.println("Current Date: " + currentDate);
///
                if (currentDate.before(openingDate)) {
                    out.println("The challenge has not been opened yet. It will open on: " + openingDate);
                    out.flush();
                    System.exit(0);
                } else if (currentDate.after(closingDate)) {
                    out.println("The challenge has been closed. It closed on: " + closingDate);
                    out.flush();
                    System.exit(0);
                } else {
                    out.println("The challenge is currently open. You may proceed.");
                }
            } else {
                out.println("Challenge not found.");
                out.flush();
                System.exit(0);
            }

            // Question Accessing Logic
            ArrayList<pupil_detail.Question> questions = new ArrayList<>();
            Scanner scanner = new Scanner(System.in);
            HashMap<pupil_detail.Question, String> studentAnswers = new HashMap<>();

            long startTime = System.currentTimeMillis();
            int duration = getChallengeDuration(challengeNumber);
            long halfTime = duration * 30_000L; // Convert minutes to milliseconds
            long endTime = startTime + duration * 60_000L; // Convert minutes to milliseconds


            boolean hasWarnedHalfTime = false; // Flag to check if 50% warning has been shown

            String questionQuery = "SELECT q.Question_id, q.Question_text, q.Marks " +
                    "FROM question q " +
                    "WHERE q.challenge_number = ?";
            PreparedStatement questionStatement = connection.prepareStatement(questionQuery);
            questionStatement.setInt(1, challengeNumber);
            ResultSet questionResultSet = questionStatement.executeQuery();

            while (questionResultSet.next()) {
                int questionID = questionResultSet.getInt("Question_id");
                String questionText = questionResultSet.getString("Question_text");
                String marksString = questionResultSet.getString("Marks"); // Retrieve marks as String

                int marks = 0;
                try {
                    // Extract numeric part from marksString
                    marks = Integer.parseInt(marksString.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing marks: " + marksString);
                }

                questions.add(new pupil_detail.Question(questionID, questionText, marks));

                // Create a question object
                pupil_detail.Question question = new pupil_detail.Question(questionID, questionText, marks);
                questions.add(question);

                // Fetch the correct answer for this question
                String correctAnswerQuery = "SELECT Answer_text FROM answer WHERE Qn_id = ?";
                PreparedStatement correctAnswerStatement = connection.prepareStatement(correctAnswerQuery);
                correctAnswerStatement.setInt(1, questionID); // Use Question_id to fetch the answer
                ResultSet correctAnswerResultSet = correctAnswerStatement.executeQuery();

                String correctAnswer = "";
                if (correctAnswerResultSet.next()) {
                    correctAnswer = correctAnswerResultSet.getString("Answer_text");
                }


            }

            Collections.shuffle(questions);
            int maxQuestions = 10; // Define the maximum number of questions to show

            while (true) {
                long currentTime = System.currentTimeMillis();

                if (currentTime >= endTime) {
                    System.out.println("\u001B[33mYou have used 100% of your time.\u001B[0m"); // Yellow
                    out.flush();
                    break;
                } else if (currentTime >= startTime + halfTime && !hasWarnedHalfTime) {
                    System.out.println("\u001B[31mYou have used 50% of your time!\u001B[0m"); // Red
                    out.flush();
                    hasWarnedHalfTime = true; // Set flag to true so the warning isn't shown again
                }

                if (questions.isEmpty()) {
                    System.out.println("No more questions available for this challenge.");
                    out.flush();
                    break;
                }
                // Calculate remaining time in minutes
                long remainingTimeMillis = endTime - currentTime;
                int remainingMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);

                // Display the number of remaining questions and remaining time
                System.out.println("Remaining questions: " + questions.size());
                System.out.println("Remaining time: " + remainingMinutes + " minutes");
                out.flush();
                pupil_detail.Question question = questions.remove(0);

                // Display the question along with its marks
                //send a signal: starting questions



                System.out.println("\u001B[34mQuestion: " + question.Question_text + " (Marks: " + question.Marks + ")\u001B[0m"); // Blue
                System.out.print("Your answer: ");
                String studentAnswer = scanner.nextLine();

                // Collect student answers
                studentAnswers.put(question, studentAnswer);
/////////start////////
                // Record time spent on the current question


                System.out.println("Enter the command 'nextQuestion' to fetch another question, or 'endChallenge' to finish:");
                String nextCommand = scanner.nextLine();
                if (nextCommand.equalsIgnoreCase("endChallenge")) {
                    System.out.println("Challenge ended.");
                    completedChallenges.add(challengeNumber);
                    break;
                } else if (!nextCommand.equalsIgnoreCase("nextQuestion")) {
                    System.out.println("Invalid command. Please enter 'nextQuestion' to fetch another question or 'endChallenge' to finish.");
                }
            }

            // After the challenge ends, you can implement the answer comparison here
            int totalScore = 0;
            int correctAnswers = 0;
            int incorrectAnswers = 0;

            for (Map.Entry<pupil_detail.Question, String> entry : studentAnswers.entrySet()) {
                pupil_detail.Question question = entry.getKey();
                String studentAnswer = entry.getValue();

                // Fetch correct answer from the database
                String correctAnswerQuery = "SELECT Answer_text FROM answer WHERE Qn_id = ?";
                PreparedStatement correctAnswerStatement = connection.prepareStatement(correctAnswerQuery);
                correctAnswerStatement.setInt(1, question.Question_id);
                ResultSet correctAnswerResultSet = correctAnswerStatement.executeQuery();
                String correctAnswer = "";
                if (correctAnswerResultSet.next()) {
                    correctAnswer = correctAnswerResultSet.getString("Answer_text");
                }

                // Scoring logic
                if (studentAnswer.equals("-")) {
                    // No penalty for "not sure" answer
                    continue;
                } else if (studentAnswer.equals(correctAnswer)) {
                    totalScore += question.Marks;
                    correctAnswers++;
                } else {
                    totalScore -= 3; // Deduct 3 marks for incorrect answer
                    incorrectAnswers++;
                }
            }

            System.out.println("Challenge completed.");
            System.out.println("Total Score: " + totalScore);
            System.out.println("Correct Answers: " + correctAnswers);
            System.out.println("Incorrect Answers: " + incorrectAnswers);
            System.out.println("Challenges completed: " + completedChallenges.size()); // Debugging line





            //still debugging number of challenges
            System.out.println("Challenges completed: " + completedChallenges.size()); // Debugging line
            // After the challenge ends, you can implement the answer comparison here
            if (completedChallenges.size() >= 3) {
                printReport();
                out.println("You have " + (3 - completedChallenges.size()) + " more challenge(s) available.");
            } else {
                out.println("You have completed all available challenges.");
            }
            out.flush();
            // After the challenge ends, you can implement the answer comparison here
            // Model.saveSubmissionsToDatabase(new ArrayList<>(studentAnswers.keySet()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void printReport() {
        System.out.println("Challenge Report:");
        for (Map.Entry<pupil_detail.Question, Long> entry : timeSpentOnQuestions.entrySet()) {
            pupil_detail.Question question = entry.getKey();
            long timeSpent = entry.getValue();
            long timeSpentInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeSpent);
            System.out.println("Question ID: " + question.Question_id);
            System.out.println("Time Spent: " + timeSpentInSeconds + " seconds");

        }
    }
}


