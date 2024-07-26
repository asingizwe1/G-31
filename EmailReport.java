import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Properties;

public class EmailReport {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/your_database";
        String dbUser = "your_db_user";
        String dbPassword = "your_db_password";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT a.challenge_id, aq.question_id, q.question_text, an.correct_answer " +
                         "FROM attempted_questions aq " +
                         "JOIN attempts a ON aq.attempt_id = a.attempt_id " +
                         "JOIN challenges c ON a.challenge_id = c.challenge_id " +
                         "JOIN questions q ON aq.question_id = q.question_id " +
                         "JOIN answers an ON q.question_id = an.question_id " +
                         "ORDER BY a.challenge_id, aq.question_id";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ByteArrayOutputStream pdfOutputStream = generatePDF(resultSet);
            sendEmailWithPDF("participant@example.com", pdfOutputStream);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ByteArrayOutputStream generatePDF(ResultSet resultSet) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            while (resultSet.next()) {
                int challengeId = resultSet.getInt("challenge_id");
                int questionId = resultSet.getInt("question_id");
                String questionText = resultSet.getString("question_text");
                String correctAnswer = resultSet.getString("correct_answer");

                document.add(new Paragraph("Challenge ID: " + challengeId));
                document.add(new Paragraph("Question ID: " + questionId));
                document.add(new Paragraph("Question Text: " + questionText));
                document.add(new Paragraph("Correct Answer: " + correctAnswer));
                document.add(new Paragraph("\n"));
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }

    private static void sendEmailWithPDF(String recipientEmail, ByteArrayOutputStream pdfOutputStream) {
        final String username = "your_email@example.com";
        final String password = "your_email_password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your_email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Challenge Report");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please find attached the report for the challenge.");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new javax.activation.DataHandler(
                    new javax.mail.util.ByteArrayDataSource(pdfOutputStream.toByteArray(), "application/pdf")));
            messageBodyPart.setFileName("ChallengeReport.pdf");
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
