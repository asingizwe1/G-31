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
            String sql = "SELECT email FROM pupil WHERE registration_number = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, schoolRegNo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String subject;
                String body;
                if (accepted) {
                    subject = "Registration Confirmation";
                    body = "Dear " + username + ",\n\nYour registration has been confirmed for the competition.";
                } else {
                    subject = "Registration Rejection";
                    body = "Dear " + username + ",\n\nWe regret to inform you that your registration for the competition has been rejected.";
                }
                sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String to, String subject, String body) {
        final String from = "mulindemusasizi@gmail.com";
        final String password = "sxam jhim wtox cafz";

        Properties properties = new Properties();//For Gmail, it would be smtp.gmail.com.
        properties.put("mail.smtp.host", "smtp.gmail.com");//smtp.example.com with the actual SMTP server of your email provider.
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void sendAcceptanceEmail(String to, String username) {
        String subject = "Application Accepted";
        String body = "Dear " + username + ",\n\nCongratulations! Your application has been accepted.";
        sendEmail(to, subject, body);
    }

    ///////start///////////


    public void sendRepresentativeRegistrationEmail(String schoolRegNo, String representativeUsername, boolean accepted) {
        try (Connection connection = Model.createConnection()) {
            String sql = "SELECT email FROM schoolrep_ WHERE school_reg_no = ? AND username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, schoolRegNo);
            statement.setString(2, representativeUsername);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String subject = "Representative Registration Confirmation";
                String body = "Dear " + representativeUsername + ",\n\nThank you for registering as a school representative. Your registration has been successfully completed.";
                sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
/*Explanation:
sendRepresentativeRegistrationEmail Method: Fetches the email address of the representative from the schoolrep table based on the provided schoolRegNo and representativeUsername. It then sends a registration confirmation email to that address.
Database Query: Uses a PreparedStatement to safely execute the SQL query and fetch the representative's email.
Email Sending: Utilizes the existing sendEmail method to handle the email sending process.
Make sure the database schema and field names match the ones used in the SQL query. Adjust the query if necessary to fit your actual database structure.
*/

/////////////////////


