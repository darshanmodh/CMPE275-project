package cmpe275.order.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailService {
	
	public boolean sendMail(String emailId) {
		String verificationCode = "";
		String emailBody = "\n <h3>Hello " + emailId + ",</h3> \n";
		emailBody += "<p>Your verification code for Order management system is as following:</p> \n\n\t";
		
		DatabaseService databaseService = new DatabaseService();
		verificationCode = databaseService.getVerificationCode(emailId);
		
		String to = emailId;
		String from = "project.cmpe275@gmail.com";
		String password = "cmpe275zhang";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication( from, password); 
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Verification code - Order Management System");
			message.setContent(emailBody + "<b><i>" + verificationCode + "</b></i>" + "\n\n" + "<p>Please use it to verify your account.</p>\n\n Thanks,\nProject CMPE275", "text/html" );
			//message.setText(emailBody + verificationCode + "\n\n" + "Please use it to verify your account.\n\n Thanks,\nProject CMPE275");
			Transport.send(message);
			System.out.println("email sent successfully...");
			return true;
		} catch (MessagingException ex) {
			System.out.println("Exception : Sending Mail");
			ex.printStackTrace();
		}
		return false;
	}

}
