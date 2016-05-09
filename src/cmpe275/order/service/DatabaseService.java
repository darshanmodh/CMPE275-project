package cmpe275.order.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import cmpe275.order.model.MenuItem;
import cmpe275.order.model.User;

public class DatabaseService {

	EntityManagerFactory entityManagerFactory;
	EntityManager entityManager;

	public DatabaseService() {
		entityManagerFactory = Persistence.createEntityManagerFactory("CMPE275-Project");
		entityManager = entityManagerFactory.createEntityManager();
	}

	public void addItem(MenuItem menu) {
		entityManager.getTransaction().begin();
		entityManager.persist(menu);
		entityManager.getTransaction().commit();
	}

	public void deleteItem(int id) {
		// May be needs to be changed for handling already placed order
		entityManager.getTransaction().begin();
		Query query = entityManager.createQuery("update MenuItem m set m.isEnabled=0 where m.menuId="+id+"");
		query.executeUpdate();
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	public List<MenuItem> viewAllItems() {
		Query query = entityManager.createQuery("select m.menuId,m.name,m.category,m.unitPrice,m.calories from MenuItem m where m.isEnabled=1");
		@SuppressWarnings("unchecked")
		List<Object[]> menu =  query.getResultList();
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		for (Object[] m: menu) {
			MenuItem mi = new MenuItem();
			mi.setMenuId((int) m[0]);
			mi.setName((String) m[1]);
			mi.setCategory((String) m[2]);
			mi.setUnitPrice((float) m[3]);
			mi.setCalories((float) m[4]);
			menuList.add(mi);
		}
		//System.out.println(menuList.get(0).getName());
		return menuList;
	}
	
	public MenuItem viewItem(int menuId) {
		Query query = entityManager.createQuery("select m from MenuItem m where m.menuId="+menuId+"");
		MenuItem menu = (MenuItem) query.getSingleResult();
		return menu;
	}

	public Blob getImage(int menuId) throws SerialException, SQLException {
		Query query = entityManager.createQuery("select m.picture from MenuItem m where m.menuId="+menuId+"");
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
		Query query = entityManager.createQuery("update User u SET u.isVerified = '" + true + "' WHERE u.email=:emailId");
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
	
}
