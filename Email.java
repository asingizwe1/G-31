

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.*;

public class email {

    public  void sendEmailNotification(String schoolRegNo, String username, boolean accepted) {
        try (Connection connection = Model.createConnection()) {
            // Establishing a database connection using a method `Model.createConnection()`
            String sql = "SELECT email FROM pupil WHERE registration_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, schoolRegNo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieving email from the database result set
                String email = resultSet.getString("email");
                String subject;
                String body;
                // Determining email subject and body based on acceptance status
                if (accepted) {
                    subject = "Registration Confirmation";
                    body = "Dear " + username + ",\n\nYour registration has been confirmed for the competition.";
                } else {
                    subject = "Registration Rejection";
                    body = "Dear " + username + ",\n\nWe regret to inform you that your registration for the competition has been rejected.";
                }
                // Sending email notification
                sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String to, String subject, String body) {
        // Email sender credentials
        final String from = "mulindemusasizi@gmail.com";
        final String password = "sxam jhim wtox cafz";

        // Email server properties for Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Establishing email session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Creating email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            // Sending the email message
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendAcceptanceEmail(String to, String username) {
        // Sending an acceptance email with fixed subject and body
        String subject = "Application Accepted";
        String body = "Dear " + username + ",\n\nCongratulations! Your application has been accepted.";
        sendEmail(to, subject, body);
    }
}
