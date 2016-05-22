package cmpe275.order.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class JdbcDatabaseService {
	
	private Connection connect;
	private Statement stmt;
	public boolean deleteOrder(int orderId) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/OrderManagementSystem?"
							+ "user=root&password=admin");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stmt = connect.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stmt.executeUpdate("DELETE FROM OrderDetail  WHERE orderId=" + orderId + "");
			
			System.out.println("done!");
			try {
				stmt.executeUpdate("DELETE FROM OrdersPlaced  WHERE orderId=" + orderId + "");
				System.out.println("done!");
				return true;
			} catch (Exception e) {
				System.out.println("done!Exception");
				 System.out.println(e.getMessage());
				return false;
			}
		} catch (Exception e) {
			System.out.println("done!Exception");
			return false;
		}

	}

}
