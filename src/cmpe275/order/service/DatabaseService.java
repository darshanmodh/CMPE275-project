package cmpe275.order.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
		MenuItem mi = entityManager.find(MenuItem.class, id);
		entityManager.remove(mi);
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
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
