package cmpe275.order.service;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;

public class OrderAlgo {
	private java.sql.Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Time earliestPickupTime;
	private boolean checkEarliestPickupTime=false;
	
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

	public static void main(String a[]) {
		OrderAlgo obj = new OrderAlgo();
		try {

			Date dop = new Date(2016 - 1900, 4, 10);
			
			int chefId=1;
			boolean orderCreated=false;
			
			
			Time pickupTime2=new Time(21,0,0);
			int prepTime = 5;
			
			
			////////
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(pickupTime2);
			//System.out.println(calendar.getTime());
				
			////////
			
			System.out.println("earliest pickup time "+obj.earliestAvailableTimeSlot(dop, prepTime));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	//public void 
	
	
	
	
	public Time earliestAvailableTimeMulitpleItens(Date dop, int prepTime)
	{
		Time tempTime=new Time(0,0,0);
		//for(int i=0;i<quantity;i++)
		//{
			Time result=earliestAvailableTimeSlot(dop, prepTime);
			if(result==null)
				return null;
			else
				if(result.getTime()>tempTime.getTime())
			tempTime=result;
		//}
		return tempTime;
	}
	
 	public Time earliestAvailableTimeSlot(Date dop, int prepTime)
	{
		
		int chefId=1;
		boolean orderCreated=false;
		Time startTime,endTime;
		
		if(isToday(dop))
		{Calendar cal1 = Calendar.getInstance();
		//grace time of 1 minute
		cal1.add(Calendar.MINUTE, 1);
		startTime=new Time(cal1.getTimeInMillis());
		}
		else 
			startTime=new Time(5,0,0);
		
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.HOUR_OF_DAY, 21);
		cal2.set(Calendar.MINUTE,0);
		cal2.set(Calendar.SECOND,0);
		cal2.add(Calendar.MINUTE, -prepTime);
		
		endTime=new Time(cal2.getTimeInMillis());
		
		// OOBusiness hours
		if(startTime.getHours()>=21 && startTime.getMinutes()>=0 && startTime.getSeconds()>=0)
		{
			System.out.println("startTime: "+startTime);
			System.out.println("Out of Business hours");
			return null;
		}	

		//Handle for before business hours
		if(startTime.getHours()<5)
		{
			startTime.setHours(5);
			startTime.setMinutes(0);
			startTime.setSeconds(0);
			
		}
		
		checkEarliestPickupTime=true;
		
		//to keep same dates 1 Jan 1970
		Time startTime2=new Time(startTime.getHours(),startTime.getMinutes(),startTime.getSeconds());
		Time endTime2=new Time(endTime.getHours(),endTime.getMinutes(),endTime.getSeconds());
	
		
		if(startTime2.after(endTime2))
		{
			System.out.println(startTime2+" "+endTime2);
			System.out.println("start time after end time");
			return null;
		}
		
		System.out.println(startTime2+" "+endTime2);

		
		Time earliestPickupTime1=null,earliestPickupTime2=null,earliestPickupTime3=null;
		
			if(bookASlot(dop, prepTime, startTime2,endTime2,1))
			earliestPickupTime1=earliestPickupTime;
			System.out.println("earliestPickupTime1 "+earliestPickupTime1);
			
			
			if(bookASlot(dop, prepTime, startTime2,endTime2,2))
			 earliestPickupTime2=earliestPickupTime;
			System.out.println("earliestPickupTime2 "+earliestPickupTime2);

			if(bookASlot(dop, prepTime, startTime2,endTime2,3))
			earliestPickupTime3=earliestPickupTime;
			System.out.println("earliestPickupTime3 "+earliestPickupTime3);

			//no chef free
			if(earliestPickupTime1==null && earliestPickupTime2==null && earliestPickupTime3==null)
			{	
				System.out.println("No chef free");
				return null;
			}
			
			
			/*
			 * checks for null and retrieve earliest date
			 */
			if(earliestPickupTime1==null)
				earliestPickupTime=earliestPickupTime2;
			else 
			if(earliestPickupTime2==null)
				earliestPickupTime=earliestPickupTime1;
			else
			if(earliestPickupTime1.getTime()<earliestPickupTime2.getTime())
				earliestPickupTime=earliestPickupTime1;
				else 
					earliestPickupTime=earliestPickupTime2;
			
			if(earliestPickupTime==null)
				return earliestPickupTime3;
			
			if(earliestPickupTime3!=null && earliestPickupTime3.getTime()<earliestPickupTime.getTime())
				earliestPickupTime=earliestPickupTime3;
				
			
			///test not null times
				
		
		
		return earliestPickupTime;
		
		
	}

	

	
	
	
	public boolean userProvidedMultipleItems(Date dop, int prepTime,Time pickupTime)
	{
		boolean orderCreated=false;
		//for(int i=0;i<quantity;i++)
		//{	
				orderCreated=userProvidedTimeSlot(dop,prepTime,pickupTime);
				if(!orderCreated)
				return false;
		//}
		return true; 
		
	}
	
	public boolean userProvidedTimeSlot(Date dop, int prepTime,Time pickupTime)
	{
		int chefId=1;
		boolean orderCreated=false;
		Time startTime,endTime;
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(pickupTime);
		cal.add(Calendar.MINUTE, -prepTime);
		endTime=new Time(cal.getTimeInMillis());
		
		
//		/*
//		 *If end time is less than  6AM
//		 *
//		 */
//		if(endTime.getHours()<6)
//		{
//			System.out.println("End time before 6 AM");
//			return false;
//		}
		
		
		
		/*
		 * Today and Not enough time to prepare
		 */
		if(isToday(dop))
		{
		Calendar calET = Calendar.getInstance();
		calET.set(Calendar.HOUR_OF_DAY, endTime.getHours());
		calET.set(Calendar.MINUTE, endTime.getMinutes());
		calET.set(Calendar.SECOND, 0);
		Calendar tempcal = Calendar.getInstance();

		//System.out.println("calET "+calET.getTime()+ " tempcal: "+tempcal.getTime());
		//System.out.println("calET "+calET.getTime()+ " tempcal: "+tempcal);

//		int diff = (int) (calET.getTime().getTime() - tempcal.getTime()
//				.getTime()) / 60000;

		if (calET.before(tempcal))
		{
			System.out.println("Not enough time to prepare");
			return false;
		}
		}
		 
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endTime);
		cal2.add(Calendar.HOUR, -1);
		startTime=new Time(cal2.getTimeInMillis());

		System.out.println(startTime+" "+endTime);
		
		
		if(startTime.getHours()<5)
		{
			startTime.setHours(5);
			startTime.setMinutes(0);
			startTime.setSeconds(0);
			
		}
		System.out.println(startTime+" "+endTime);

		/*
		 * If current time is after start time,
		 * set start time to current time
		 */
		if(isToday(dop))
		{
			Calendar cal3 = Calendar.getInstance();
			cal3.set(Calendar.DAY_OF_MONTH,1);
			cal3.set(Calendar.MONTH,0);
			cal3.set(Calendar.YEAR,1970);
			//cal3.setTime(new Time(6,15,0));
			
			
			if(cal3.getTime().after(startTime))
			{
				startTime.setHours(cal3.getTime().getHours());
				startTime.setMinutes(cal3.getTime().getMinutes());
				startTime.setSeconds(cal3.getTime().getSeconds());

			}
			
		}
		
		System.out.println(startTime+" "+endTime);

		
		
		
		while(chefId<=3 && !orderCreated)
		{
			orderCreated=bookASlot(dop, prepTime, startTime,endTime,chefId);
			chefId++;
		}
		//System.out.println("order created "+orderCreated);
		return orderCreated;
	}
	
	public boolean bookASlot(Date dop, int prepTime, Time st, Time et,int chefId) {
		int diff = 0;
		try {
			System.out.println();
			System.out.println("inside bookASlot, chefId: "+chefId);
			System.out.println("Preparation time "+prepTime);

			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/OrderManagementSystem?"
							+ "user=root&password=a");

			// Statements allow to issue SQL queries to the database
			//statement = connect.createStatement();

			/*
			 * Check if an order is placed overlapping or surrounding the timeslot
			 * 
			 */
			preparedStatement = connect
					.prepareStatement("select * from OrderManagementSystem.OrdersPlaced where chefId=?"
							+ " and prepDate=?"
							+ " and startTime<= ? and endTime >?");
		
			preparedStatement.setInt(1, chefId);
			preparedStatement.setDate(2, dop);
			preparedStatement.setTime(3, st);
			preparedStatement.setTime(4, et);

			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				System.out.println("overlapping order ");
				return false;
			}
			
			
			
			
			
			preparedStatement = connect
					.prepareStatement("select * from OrderManagementSystem.OrdersPlaced where chefId=?"
							+ " and prepDate=?"
							+ " and ((startTime between ? and  ?) or (endTime between ? and ?)) order by startTime"); //test endtime addtion
		
			preparedStatement.setInt(1, chefId);
			preparedStatement.setDate(2, dop);
			preparedStatement.setTime(3, st);
			preparedStatement.setTime(4, et);
			preparedStatement.setTime(5, st);
			preparedStatement.setTime(6, et);
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
				System.out.println();
							
				
				/*
				 * before first order check
				 * 
				 */
				diff = (int) ((startTime.getTime() - st.getTime()) / 60000); // minutes
				System.out.println("time before first order " + diff
						+ " minutes");
				if (prepTime <= diff) {
					
					System.out.println("Time slot avaliable");
					Calendar cal = Calendar.getInstance();
					cal.setTime(st);
					cal.add(Calendar.MINUTE, prepTime);

					preparedStatement = connect
							.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
					// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
					// Parameters start with 1
					preparedStatement.setInt(1, chefId);
					preparedStatement.setDate(2, prepDate);
					preparedStatement.setTime(3, st);
					preparedStatement.setTime(4, new java.sql.Time(cal
							.getTime().getTime()));

					earliestPickupTime=new java.sql.Time(cal
							.getTime().getTime());
					if(!checkEarliestPickupTime)
					{
						preparedStatement.executeUpdate();
					}
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

					if (prepTime <= diff) {
						System.out.println("Time slot avaliable");
						Calendar cal = Calendar.getInstance();
						cal.setTime(prevEndtime);
						cal.add(Calendar.MINUTE, prepTime);

						preparedStatement = connect
								.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
						// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
						// Parameters start with 1
						preparedStatement.setInt(1, chefId);
						preparedStatement.setDate(2, prepDate);
						preparedStatement.setTime(3, prevEndtime);
						preparedStatement.setTime(4, new java.sql.Time(cal
								.getTime().getTime()));

						earliestPickupTime=new java.sql.Time(cal
								.getTime().getTime());
						if(!checkEarliestPickupTime)
						{
						preparedStatement.executeUpdate();
						}
						// System.out.println("Order created");
						close();
						return true;
					}
					prevEndtime = endTime;
				}
				//check for timeslot in between orders over
				

				/*
				 * check for timeslot after last order 
				 * 
				 */
				
				//diff = (int) (et.getTime() - prevEndtime.getTime()) / 60000;

				//System.out.println("time left till ET " + diff+ " minutes");
				if (prevEndtime.getTime() <= et.getTime()) {
					System.out.println("time remaining after last order");
					Calendar cal = Calendar.getInstance();
					cal.setTime(prevEndtime);
					cal.add(Calendar.MINUTE, prepTime);

					preparedStatement = connect
							.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");
					// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
					// Parameters start with 1
					preparedStatement.setInt(1, chefId);
					preparedStatement.setDate(2, prepDate);
					preparedStatement.setTime(3, prevEndtime);
					preparedStatement.setTime(4, new java.sql.Time(cal
							.getTime().getTime()));

					earliestPickupTime=new java.sql.Time(cal
							.getTime().getTime());
					if(!checkEarliestPickupTime)
					{
						preparedStatement.executeUpdate();
					}// System.out.println("Order created");
					close();
					return true;
				}
				return false; 
			
			}

			/*
			 * if order is today and no orders against chef 
			 */
			if (isToday(dop)) {

					//test
					Calendar tempCal = Calendar.getInstance();
					tempCal.setTime(st);
					tempCal.add(Calendar.MINUTE, prepTime);
					//Calendar cal = Calendar.getInstance();

					preparedStatement = connect
							.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");

					preparedStatement.setInt(1, chefId);
					preparedStatement.setDate(2, dop);
					
					preparedStatement.setTime(3, st);
					preparedStatement.setTime(4, new java.sql.Time(tempCal.getTime()
							.getTime()));

					earliestPickupTime=new java.sql.Time(tempCal.getTime()
							.getTime());
					if(!checkEarliestPickupTime)
					{
						preparedStatement.executeUpdate();
					}
					close();
					return true;
					
				
				
			}
			
			/*
			 * if not today and no orders against chef
			 * 
			 */
			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(st);
			tempCal.add(Calendar.MINUTE, prepTime);

			preparedStatement = connect
					.prepareStatement("insert into  OrderManagementSystem.OrdersPlaced (chefId,prepDate,startTime,endTime) values (?, ?, ?, ?)");

			preparedStatement.setInt(1, chefId);
			preparedStatement.setDate(2, dop);
			
			preparedStatement.setTime(3, st);
			preparedStatement.setTime(4, new java.sql.Time(tempCal.getTime()
					.getTime()));

			earliestPickupTime=new java.sql.Time(tempCal.getTime()
					.getTime());
			if(!checkEarliestPickupTime)
			{
				preparedStatement.executeUpdate();
			}
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
