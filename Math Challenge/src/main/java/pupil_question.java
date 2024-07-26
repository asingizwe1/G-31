import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class pupil_question {
    private static final int NUM_QUESTIONS = 5; // Adjust as needed
    private static final long TOTAL_CHALLENGE_TIME = 300000; // Total time for the challenge in milliseconds (5 minutes)
   // Initialize timeUp flag

    public static int retrieveQuestion(PrintWriter printWriter, BufferedReader br) {
        List<QuestionAnswerPair> questionsAndAnswers = readQuestionsAndAnswersFromDatabase();
        Collections.shuffle(questionsAndAnswers);
        int score = 0; // Initialize score for this attempt
        AtomicBoolean timeUp = new AtomicBoolean(false); // Initialize timeUp flag
        long totalElapsedTime = 0; // Initialize total elapsed time for the challenge


        try {
            for (int i = 0; i < NUM_QUESTIONS; i++) {
                QuestionAnswerPair qa = questionsAndAnswers.get(i);
                printWriter.println("Question " + (i + 1) + ": " + qa.getQuestion());
                printWriter.println("Questions remaining: " + (NUM_QUESTIONS - i - 1));
                printWriter.flush();


                // Reset the timer flag
                timeUp.set(false);
                // Timer initialization
                final long timeLimit = 60000; // 60 seconds in milliseconds
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + timeLimit;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        timeUp.set(true);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, timeLimit); // Schedule the task to run after 60 seconds

                // Create a separate thread to handle timer display
                Thread timerThread = new Thread(() -> {
                    while (!timeUp.get()) {
                        long elapsed = System.currentTimeMillis() - startTime;
                        long remaining = timeLimit - elapsed;
                        if (remaining <= 0) {
                            timeUp.set(true);
                            remaining = 0;
                        }
                        printWriter.print("\rTime remaining: " + remaining / 1000.0 + " seconds"); // Print on the same line
                        printWriter.flush();
                        try {
                            Thread.sleep(100); // Sleep for 100 milliseconds
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                timerThread.start();


                String userAnswer = "";
                if (!timeUp.get()) {
                    userAnswer = br.readLine();
                }
                timer.cancel();
                timerThread.interrupt(); // Stop the timer thread
                printWriter.println(); // Move to the next line after timer stops
                printWriter.flush();

                // Update total elapsed time for the challenge
                totalElapsedTime += System.currentTimeMillis() - startTime;

                // Calculate and display remaining time for the entire challenge
                long totalRemainingTime = TOTAL_CHALLENGE_TIME - totalElapsedTime;
                if (totalRemainingTime < 0) {
                    totalRemainingTime = 0;
                }
                printWriter.println("Total time remaining for the challenge: " + totalRemainingTime / 60000.0 + " minutes");
                printWriter.println("Number of remaining attempts: " + (NUM_QUESTIONS - i - 1));
                printWriter.flush();


                // Check the user's answer and update score
                if (userAnswer.equalsIgnoreCase("-")) {
                    printWriter.println("No marks awarded.");
                } else if (userAnswer.equalsIgnoreCase(qa.getAnswer())) {
                    score += 3;
                    printWriter.println("Correct! +3 marks");
                } else {
                    score -= 3;
                    printWriter.println("Incorrect! -3 marks");
                }
                printWriter.flush();
                timeUp.set(false);
            }

            // Display final results
            printWriter.println("Final score: " + score);
            printWriter.println("Correct answers:");
            for (int i = 0; i < NUM_QUESTIONS; i++) {
                QuestionAnswerPair qa = questionsAndAnswers.get(i);
                printWriter.println("Question " + (i + 1) + ": " + qa.getQuestion() + " - " + qa.getAnswer());
            }
            printWriter.flush();

            // Return the final score
            return score;

        } catch (Exception e) {
            e.printStackTrace();
            // Return -1 to indicate an error occurred
            return -1;
        }
    }

    public static List<QuestionAnswerPair> readQuestionsAndAnswersFromDatabase() {
        List<QuestionAnswerPair> questionsAndAnswers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement questionStatement = null;
        ResultSet questionResultSet = null;

        try {
            connection = Model.createConnection();
            String questionQuery = "SELECT Question_id, Question_text, (SELECT Answer_text FROM answer WHERE Question_id = question.Question_id LIMIT 1) as Answer_text FROM question ORDER BY RAND() LIMIT ?";
            questionStatement = connection.prepareStatement(questionQuery);
            questionStatement.setInt(1, NUM_QUESTIONS);
            questionResultSet = questionStatement.executeQuery();

            while (questionResultSet.next()) {
                String questionText = questionResultSet.getString("Question_text");
                String correctAnswer = questionResultSet.getString("Answer_text");
                questionsAndAnswers.add(new QuestionAnswerPair(questionText, correctAnswer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (questionResultSet != null) questionResultSet.close();
                if (questionStatement != null) questionStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questionsAndAnswers;
    }

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
