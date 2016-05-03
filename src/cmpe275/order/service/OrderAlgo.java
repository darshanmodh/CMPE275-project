package cmpe275.order.service;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderAlgo {
	private java.sql.Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String chefId = resultSet.getString("chefId");
			Date prepDate = resultSet.getDate("prepDate");
			Time startTime = resultSet.getTime("startTime");
			Time endTime = resultSet.getTime("endTime");
			System.out.println("chefId: " + chefId);
			System.out.println("prepDate: " + prepDate);
			System.out.println("startTime: " + startTime);
			System.out.println("endTime: " + endTime);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public static void main1(String a[]) {
		OrderAlgo obj = new OrderAlgo();
		try {

			Calendar cal = Calendar.getInstance();

			// obj.readDataBase();
			String startTime = "06:50:00";
			String endTime = "07:51:00";
			int prepTime = 59;
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			Date start = null;
			// try {
			// date = sdf.parse(myTime);
			Date dop = new Date(2016 - 1900, 4, 3);
			
			int chefId=1;
			boolean orderCreated=false;
			while(chefId<=3 && !orderCreated)
			{
				orderCreated=obj.bookASlot(dop, 1, new java.sql.Time(6, 51, 0),
							new java.sql.Time(6, 54, 0),chefId);
				chefId++;
			}
			System.out.println("order created "+orderCreated);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean bookASlot(Date dop, int pt, Time st, Time et,int chefId) {
		int diff = 0;
		try {
			System.out.println("inside bookASlot");
			System.out.println("Preparation time "+pt);

			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/OrderManagementSystem?"
							+ "user=root&password=a");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();

			preparedStatement = connect
					.prepareStatement("select * from OrderManagementSystem.OrdersPlaced where chefId="
							+ chefId
							+ " and prepDate=?"
							+ " and startTime>=? and startTime<=? order by startTime");
			preparedStatement.setDate(1, dop);
			preparedStatement.setTime(2, st);
			preparedStatement.setTime(3, et);

			resultSet = preparedStatement.executeQuery();

			// writeResultSet(resultSet);
			Time prevEndtime;
			Time nextStartTime;

			if (resultSet.next()) {
				Date prepDate = resultSet.getDate("prepDate");
				Time startTime = resultSet.getTime("startTime");
				Time endTime = resultSet.getTime("endTime");

				System.out.println("chefId: " + resultSet.getString("chefId"));
				System.out.println("prepDate: " + prepDate);
				System.out.println("startTime: " + startTime);
				System.out.println("endTime: " + endTime);
				
				/*
				 * before first order check
				 * 
				 */
				diff = (int) ((startTime.getTime() - st.getTime()) / 60000); // minutes
				System.out.println("time before first order " + diff
						+ " minutes");
				if (pt <= diff) {
					
					System.out.println("Time slot avaliable");
					Calendar cal = Calendar.getInstance();
					cal.setTime(st);
					cal.add(Calendar.MINUTE, pt);

					preparedStatement = connect
							.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
					// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
					// Parameters start with 1
					preparedStatement.setInt(1, chefId);
					preparedStatement.setDate(2, prepDate);
					preparedStatement.setTime(3, st);
					preparedStatement.setTime(4, new java.sql.Time(cal
							.getTime().getTime()));

					preparedStatement.executeUpdate();
					// System.out.println("Order created");
					close();
					return true;
					
					
				}
				//before first order check over
				
				/*
				 * check for timeslot in between orders
				 * 
				 */
				prevEndtime = resultSet.getTime("endTime");

				while (resultSet.next()) {
					

					prepDate = resultSet.getDate("prepDate");
					startTime = resultSet.getTime("startTime");
					endTime = resultSet.getTime("endTime");

					System.out.println("chefId: " + resultSet.getString("chefId"));
					System.out.println("prepDate: " + prepDate);
					System.out.println("startTime: " + startTime);
					System.out.println("endTime: " + endTime);

					nextStartTime = startTime;
					diff = (int) ((nextStartTime.getTime() - prevEndtime
							.getTime()) / 60000); // minutes
					System.out.println("diff " + diff + " minutes");
					System.out.println();

					if (pt <= diff) {
						System.out.println("Time slot avaliable");
						Calendar cal = Calendar.getInstance();
						cal.setTime(prevEndtime);
						cal.add(Calendar.MINUTE, pt);

						preparedStatement = connect
								.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
						// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
						// Parameters start with 1
						preparedStatement.setInt(1, chefId);
						preparedStatement.setDate(2, prepDate);
						preparedStatement.setTime(3, prevEndtime);
						preparedStatement.setTime(4, new java.sql.Time(cal
								.getTime().getTime()));

						preparedStatement.executeUpdate();
						// System.out.println("Order created");
						close();
						return true;
					}
					prevEndtime = endTime;
				}
				//check for timeslot in between orders over
				

				/*
				 * check for timeslot after last order till ET
				 * 
				 */
				diff = (int) (et.getTime() - prevEndtime.getTime()) / 60000;

				System.out.println("time left till closing " + diff
						+ " minutes");
				if (diff >= pt) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(prevEndtime);
					cal.add(Calendar.MINUTE, pt);

					preparedStatement = connect
							.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
					// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
					// Parameters start with 1
					preparedStatement.setInt(1, chefId);
					preparedStatement.setDate(2, prepDate);
					preparedStatement.setTime(3, prevEndtime);
					preparedStatement.setTime(4, new java.sql.Time(cal
							.getTime().getTime()));

					preparedStatement.executeUpdate();
					// System.out.println("Order created");
					close();
					return true;
				}
				return false; 
			
			}

			/*
			 * if order is today and no orders against chef return false if
			 * insufficient time
			 */
			if (isToday(dop)) {

				Calendar calET = Calendar.getInstance();
				calET.set(Calendar.HOUR_OF_DAY, et.getHours());
				calET.set(Calendar.MINUTE, et.getMinutes());
				Calendar cal = Calendar.getInstance();

				diff = (int) (calET.getTime().getTime() - cal.getTime()
						.getTime()) / 60000;
				System.out.println("time remaining till pickup " + diff + " minutes");

				if (diff < pt)
					return false;
				
			}
			
			/*
			 * If today and there is sufficient time or if not today and no
			 * orders against chef
			 */
			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(et);
			tempCal.add(Calendar.MINUTE, pt);

			preparedStatement = connect
					.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");

			preparedStatement.setInt(1, chefId);
			preparedStatement.setDate(2, dop);
			preparedStatement.setTime(3, et);
			preparedStatement.setTime(4, new java.sql.Time(tempCal.getTime()
					.getTime()));

			preparedStatement.executeUpdate();
			close();
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			close();

			return false;

		}

	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * <p>
	 * Checks if two calendars represent the same day ignoring time.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal2
				.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * <p>
	 * Checks if a date is today.
	 * </p>
	 * 
	 * @param date
	 *            the date, not altered, not null.
	 * @return true if the date is today.
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 */
	public static boolean isToday(Date date) {
		return isSameDay(date, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
	}

}
