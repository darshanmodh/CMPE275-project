package cmpe275.order.service;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.eclipse.persistence.jpa.jpql.parser.AbstractExpression;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import cmpe275.order.model.MenuItem;
import cmpe275.order.model.OrderDetail;
import cmpe275.order.model.OrdersPlaced;
import cmpe275.order.model.Popular;
import cmpe275.order.model.ShoppingCart;
import cmpe275.order.model.User;

public class DatabaseService {

	EntityManagerFactory entityManagerFactory;
	EntityManager entityManager;

	public DatabaseService() {
		entityManagerFactory = Persistence.createEntityManagerFactory("CMPE275-Project");
		entityManager = entityManagerFactory.createEntityManager();
	}

	public void addItem(MenuItem menu) throws SQLException {
		System.out.println("len_add = " + menu.getPicture());
		entityManager.getTransaction().begin();
		entityManager.persist(menu);
		entityManager.getTransaction().commit();
	}

	public void deleteItem(int id) {
		// May be needs to be changed for handling already placed order
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("update MenuItem m set m.isEnabled=0 where m.menuId=" + id + "");
		query.executeUpdate();
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}

	public void updatePicture(int itemId, MenuItem menu) throws SQLException {
		// System.out.println("len = "+menu.getPicture().length());
		Query query = entityManager
				.createQuery("update MenuItem m SET m.picture='" + menu.getPicture() + "' WHERE m.menuId=:itemId");
		query.setParameter("itemId", itemId);
		query.executeUpdate();
	}

	public void updateMenuItem(int itemId, MenuItem menu) throws SQLException {
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("update MenuItem m SET m.name='" + menu.getName() + "', m.category='"
				+ menu.getCategory() + "', m.unitPrice='" + menu.getUnitPrice() + "', m.calories='" + menu.getCalories()
				+ "', m.prepTime='" + menu.getPrepTime() + "' WHERE m.menuId=:itemId");
		query.setParameter("itemId", itemId);
		query.executeUpdate();
		// updatePicture(itemId, menu);
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}

	public void enableItem(int id) {
		// May be needs to be changed for handling already placed order
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("update MenuItem m set m.isEnabled=1 where m.menuId=" + id + "");
		query.executeUpdate();
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}

	public List<MenuItem> viewAllItems() {
		Query query = entityManager.createQuery(
				"select m.menuId,m.name,m.category,m.unitPrice,m.calories,m.prepTime from MenuItem m where m.isEnabled=1");
		@SuppressWarnings("unchecked")
		List<Object[]> menu = query.getResultList();
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		for (Object[] m : menu) {
			MenuItem mi = new MenuItem();
			mi.setMenuId((int) m[0]);
			mi.setName((String) m[1]);
			mi.setCategory((String) m[2]);
			mi.setUnitPrice((float) m[3]);
			mi.setCalories((float) m[4]);
			mi.setPrepTime((int) m[5]);
			menuList.add(mi);
		}
		// System.out.println(menuList.get(0).getName());
		return menuList;
	}

	public List<MenuItem> getItemsByCategory(String category) {
		Query query = entityManager.createQuery(
				"select m.menuId,m.name,m.category,m.unitPrice,m.calories from MenuItem m where m.isEnabled=1 AND m.category=:category");
		query.setParameter("category", category);
		@SuppressWarnings("unchecked")
		List<Object[]> menu = query.getResultList();
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		for (Object[] m : menu) {
			MenuItem mi = new MenuItem();
			mi.setMenuId((int) m[0]);
			mi.setName((String) m[1]);
			mi.setCategory((String) m[2]);
			mi.setUnitPrice((float) m[3]);
			mi.setCalories((float) m[4]);
			menuList.add(mi);
		}
		return menuList;
	}

	public List<MenuItem> viewDisabledItems() {
		Query query = entityManager
				.createQuery("select m.menuId,m.name,m.category from MenuItem m where m.isEnabled=0");
		@SuppressWarnings("unchecked")
		List<Object[]> menu = query.getResultList();
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		for (Object[] m : menu) {
			MenuItem mi = new MenuItem();
			mi.setMenuId((int) m[0]);
			mi.setName((String) m[1]);
			mi.setCategory((String) m[2]);
			menuList.add(mi);
		}
		// System.out.println(menuList.get(0).getName());
		return menuList;
	}

	public MenuItem viewItem(int menuId) {
		Query query = entityManager.createQuery(
				"select m.menuId,m.name,m.category,m.unitPrice,m.calories,m.prepTime from MenuItem m where m.menuId=:menuId");
		query.setParameter("menuId", menuId);
		@SuppressWarnings("unchecked")
		List<Object[]> menu = query.getResultList();
		MenuItem mi = new MenuItem();
		for (Object[] m : menu) {
			mi.setMenuId((int) m[0]);
			mi.setName((String) m[1]);
			mi.setCategory((String) m[2]);
			mi.setUnitPrice((float) m[3]);
			mi.setCalories((float) m[4]);
			mi.setPrepTime((int) m[5]);
		}
		return mi;
	}

	public Blob getImage(int menuId) throws SerialException, SQLException {
		Query query = entityManager.createQuery("select m.picture from MenuItem m where m.menuId=" + menuId + "");
		MenuItem menu = new MenuItem();
		menu.setPicture(new SerialBlob((byte[]) query.getSingleResult()));
		return menu.getPicture();
	}

	public void addUser(User user) {
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
	}

	public String getVerificationCode(String emailId) {
		String verificationCode = "";
		Query query = entityManager.createQuery("select u from User u where u.email=:emailId");
		query.setParameter("emailId", emailId);
		try {
			User user = (User) query.getSingleResult();
			verificationCode = user.getVerificationCode();
		} catch (Exception e) {
			System.out.println("Exception : getVerificationCode()");
			e.printStackTrace();
		}
		return verificationCode;
	}

	public String getPassword(String emailId) {
		String password = "";
		Query query = entityManager.createQuery("select u from User u where u.email=:emailId");
		query.setParameter("emailId", emailId);
		try {
			User user = (User) query.getSingleResult();
			password = user.getPassword();
		} catch (Exception e) {
			System.out.println("Exception : getPassword()");
			e.printStackTrace();
		}
		return password;
	}

	public boolean isVerified(String emailId) {
		boolean isVerified = false;
		Query query = entityManager.createQuery("select u from User u where u.email=:emailId");
		query.setParameter("emailId", emailId);
		try {
			User user = (User) query.getSingleResult();
			isVerified = user.isVerified();
		} catch (Exception e) {
			System.out.println("Exception : isVerified()");
			e.printStackTrace();
		}
		return isVerified;
	}

	public boolean makeUserVerified(String emailId) {
		boolean verifyComplete = false;
		entityManager.getTransaction().begin();
		Query query = entityManager
				.createQuery("update User u SET u.isVerified = '" + true + "' WHERE u.email=:emailId");
		query.setParameter("emailId", emailId);
		try {
			query.executeUpdate();
			verifyComplete = true;
		} catch (Exception e) {
			System.out.println("Exception : makeUserVerified()");
			return false;
		}
		entityManager.getTransaction().commit();
		return verifyComplete;
	}

	public char isAdmin(String emailId) {
		char isAdmin = '\0';
		Query query = entityManager.createQuery("select u from User u where u.email=:emailId");
		query.setParameter("emailId", emailId);
		try {
			User user = (User) query.getSingleResult();
			isAdmin = user.getRole();
		} catch (Exception e) {
			System.out.println("Exception : isVerified()");
			e.printStackTrace();
		}
		return isAdmin;
	}

	public List getCustomerOrderDetails(Date startDate, Date endDate, int sort) {

		// List<String> resultSet = new ArrayList<String>();
		String str = "";
		if (sort == 0) {
			str = "select op from OrdersPlaced op where op.prepDate between :startDate and :endDate";
		} else if (sort == 1) {
			str = "select op from OrdersPlaced op where op.prepDate between :startDate and :endDate order by op.orderTime";
		} else {
			str = "select op from OrdersPlaced op where op.prepDate between :startDate and :endDate order by op.prepDate, op.startTime ASC";
		}
		Query query = entityManager.createQuery(str);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		Date today;
		List<OrdersPlaced> resultSet = query.getResultList();

		System.out.println(" Result Set ");
		List<OrdersPlaced> resultToDisplay = new ArrayList<OrdersPlaced>();
		for (OrdersPlaced m : resultSet) {
			Calendar cal = Calendar.getInstance();
			today = new Date(cal.getTime().getYear(), cal.getTime().getMonth(), cal.getTime().getDate());
			System.out.println(today.toLocaleString() + " " + m.getPrepDate().toLocaleString());

			System.out.println("orderTime" + m.getOrderTime());
			if (m.getPrepDate().before(today))
				m.setStatus("Completed");

			else if (m.getPrepDate().after(today))
				m.setStatus("Not yet started");
			else { // today
					// System.out.println("today");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.YEAR, 1970);
				// System.out.println(cal.getTime().toLocaleString()+"
				// "+m.getStartTime().toLocaleString()+"
				// "+m.getEndTime().toLocaleString());
				if (cal.getTime().before(m.getStartTime()))
					m.setStatus("Not yet started");
				else if (cal.getTime().after(m.getEndTime()))
					m.setStatus("Completed");
				else
					m.setStatus("In Progress");

			}
			resultToDisplay.add(m);
		}
		// for (OrdersPlaced m: resultSet)
		// {
		// System.out.println("Printng results "+m.getStatus());
		// }

		return resultToDisplay;
	}

	public boolean deleteOrderDetail() {
		try {
			entityManager.getTransaction().begin();
			Query query1 = entityManager.createQuery("DELETE FROM OrderDetail o");
			query1.executeUpdate();
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void deleteOrders() {
		boolean bool = new DatabaseService().deleteOrderDetail();
		if (bool) {
			entityManager.getTransaction().begin();
			Query query = entityManager.createQuery("Delete from OrdersPlaced op");
			query.executeUpdate();
			entityManager.getTransaction().commit();
		}
	}

	public List<OrdersPlaced> getMyOrders(String email) {
		Query query = entityManager.createQuery("Select o from OrdersPlaced o where o.email='" + email + "'");
		@SuppressWarnings("unchecked")
		List<OrdersPlaced> resultSet = query.getResultList();

		Date today;
		List<OrdersPlaced> resultToDisplay = new ArrayList<OrdersPlaced>();
		for (OrdersPlaced m : resultSet) {
			Calendar cal = Calendar.getInstance();
			today = new Date(cal.getTime().getYear(), cal.getTime().getMonth(), cal.getTime().getDate());
			System.out.println(today.toLocaleString() + " " + m.getPrepDate().toLocaleString());

			if (m.getPrepDate().before(today))
				m.setStatus("Completed");

			else if (m.getPrepDate().after(today))
				m.setStatus("Not yet started");
			else { // today
					// System.out.println("today");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.YEAR, 1970);
				// System.out.println(cal.getTime().toLocaleString()+"
				// "+m.getStartTime().toLocaleString()+"
				// "+m.getEndTime().toLocaleString());
				if (cal.getTime().before(m.getStartTime()))
					m.setStatus("Not yet started");
				else if (cal.getTime().after(m.getEndTime()))
					m.setStatus("Completed");
				else
					m.setStatus("In Progress");

			}
			resultToDisplay.add(m);
		}
		// for (OrdersPlaced m: resultSet)
		// {
		// System.out.println("Printng results "+m.getStatus());
		// }

		return resultToDisplay;

	}

	public int insertOrders(OrdersPlaced order) {
		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();
		return order.getOrderId();

	}

	public boolean deleteOrder(int orderId) {
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("DELETE FROM OrderDetail o where o.foodOrder.orderId=" + orderId + "");
		query.executeUpdate();
		OrdersPlaced ordersPlaced = entityManager.find(OrdersPlaced.class, orderId);
		if (ordersPlaced != null) {
			entityManager.remove(ordersPlaced);
		}
		entityManager.getTransaction().commit();
		return true;
	}

	public void get(String email, HashMap cart, int orderId) {
		System.out.println("orderId" + orderId);
		Set set = cart.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			Query query = entityManager
					.createQuery("Select m.menuId from MenuItem m where m.name='" + me.getKey() + "'");
			int menuId = (int) query.getSingleResult();
			OrderDetail orderDetail = new OrderDetail();
			OrdersPlaced foodOrder = new OrdersPlaced();
			MenuItem menu = new MenuItem();
			entityManager.getTransaction().begin();
			orderDetail.setFoodOrder(foodOrder);
			orderDetail.setMenuItem(menu);
			orderDetail.getMenuItem().setMenuId(menuId);
			orderDetail.getFoodOrder().setOrderId(orderId);
			orderDetail.setQuantity((int) me.getValue());
			entityManager.persist(orderDetail);
			entityManager.getTransaction().commit();
		}

	}

	public List<ShoppingCart> getOrder(int orderId) {
		Query query = entityManager.createQuery("select m.name,o.quantity from OrderDetail o "
				+ "JOIN MenuItem m on o.menuItem.menuId = m.menuId where o.foodOrder.orderId=" + orderId + "");
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		System.out.println(list.size());
		List<ShoppingCart> arr = new ArrayList<ShoppingCart>();
		for (Object[] p : list) {
			ShoppingCart s = new ShoppingCart();
			s.setMenuName(p[0].toString());
			s.setQuantity((int) p[1]);
			arr.add(s);
		}
		return arr;
	}

	public List<Popular> getPopular(Date startDate, Date endDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.SECOND, 0);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endDate);
		cal2.set(Calendar.SECOND, 0);
		// System.out.println(new java.sql.Timestamp(endDate.getTime()));
		Query query = entityManager.createQuery("select m.menuId,m.name,m.category,count(m.menuId) from"
				+ " MenuItem m JOIN OrderDetail od on m.menuId = od.menuItem.menuId "
				+ "WHERE od.foodOrder.orderId IN (SELECT op.orderId FROM OrdersPlaced op JOIN OrderDetail od on "
				+ "od.foodOrder.orderId = op.orderId WHERE op.orderTime BETWEEN '"
				+ new java.sql.Timestamp(startDate.getTime()) + "' AND '" + new java.sql.Timestamp(endDate.getTime())
				+ "')" + "GROUP BY m.category,m.menuId " + "ORDER BY count(m.menuId) DESC");
		System.out.println(query);

		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();

		List<Popular> popList = new ArrayList<>();
		for (Object[] p : list) {
			Popular pop = new Popular();
			// System.out.println(p[0]+""+p[1]+" "+p[2] );
			pop.setMenuId((int) p[0]);
			pop.setName((String) p[1]);
			pop.setCategory((String) p[2]);
			pop.setCount((long) p[3]);
			popList.add(pop);
		}
		return popList;
	}

	public float getPrice(String menuName) {
		Query query = entityManager.createQuery("select m.unitPrice from MenuItem m where m.name='" + menuName + "'");
		float price = (float) query.getSingleResult();
		return price;
	}
	public long getMenuCount()
	{
		Query query = entityManager.createQuery("select count(m) from MenuItem m");
		return (long) query.getSingleResult();
		
	}
}
