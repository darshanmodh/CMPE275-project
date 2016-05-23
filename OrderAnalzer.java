package pack;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class OrderAnalzer implements Runnable {

	private java.sql.Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String email;
	private Time endTime;

	public void run() {

		while (true) {
			System.out.println("inside thread");
			try {
				// thread to sleep for 1000 milliseconds
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.SECOND, 0);

				// System.out.println("todays date "+new
				// Date(cal.getTime().getTime()).toLocaleString());
				// System.out.println("todays time "+new
				// java.sql.Time(cal.getTime()
				// .getTime()).toLocalTime());

				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connect = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/OrderManagementSystem?"
								+ "user=root&password=admin");

				preparedStatement = connect
						.prepareStatement("select * from  OrderManagementSystem.OrdersPlaced where startTime=? and prepDate=?");
				preparedStatement.setTime(1, new java.sql.Time(cal.getTime()
						.getTime()));
				preparedStatement.setDate(2, new Date(cal.getTime().getTime()));

				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					System.out.println("Sending email ");

					email = resultSet.getString("email");
					endTime = resultSet.getTime("endTime");
					sendMail();
				}

				Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}
	}

	public void sendMail() {
		String emailBody = "";
		String endEmailBody = "";
		emailBody = "<html> <head> <style> div.background { background: url(http://static.asiawebdirect.com/m/bangkok/portals/vietnam/homepage/da-nang/top10/top10-da-nang-dishes/pagePropertiesImage/vietnam-dishes.jpg) repeat; border: 2px solid black; }";
		emailBody += "div.transbox { margin: 30px; background-color: #ffffff; border: 1px solid black; opacity: 0.6; filter: alpha(opacity=60);} div.transbox p {margin: 5%;font-weight: bold; color: #000000;} </style></head>";
		emailBody += "<body> <div class='background'> <div class='transbox'>";
		emailBody += "\n <h3>Hello,</h3> \n";
		emailBody += "<p>Your order is being processed and will be ready for pickup at "
				+ endTime + "</p> \n\n\t";
		endEmailBody = "</div> </div> </body> </html>";

		String to = email;
		String from = "project.cmpe275@gmail.com";
		String password = "cmpe275zhang";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, password);
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject("Order status - Order Management System");
			message.setContent(emailBody
					+ "\n\n <p>Thanks,\n<br> Project CMPE275</p>"
					+ endEmailBody, "text/html");
			// message.setText(emailBody + verificationCode + "\n\n" +
			// "Please use it to verify your account.\n\n Thanks,\nProject CMPE275");
			Transport.send(message);
			System.out.println("email sent successfully...");
		} catch (MessagingException ex) {
			System.out.println("Exception : Sending Mail");
			ex.printStackTrace();
		}

	}

	public static void main(String args[]) throws Exception {
		Thread t = new Thread(new OrderAnalzer());
		// this will call run() function
		t.start();

	}

}
