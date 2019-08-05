package classess;


//Imports
import java.sql.Connection;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//Begin Subclass Mail
public class Mail{

 public String sendEmail(String email) {
        final String username = "TurnBackAssistance@gmail.com";
        final String userpassword = "Tbsupport1";
        String password;
        //create an instance of the connectivity class
        Connectivity connection = new Connectivity();
        Connection dbConnection;
       
        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, userpassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("turnbackassistance@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Password Recovery");
            message.setText("Dear TB user,"
                    + "\n\n Your password is: " + connection.returnPassword(email));
            
            Transport.send(message);

            System.out.println("Email Sent");

        } catch (MessagingException e) {
            e.printStackTrace();
       
        }
        return null;
 }
 
} //End Subclass Mail
