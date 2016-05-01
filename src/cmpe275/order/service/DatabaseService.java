package cmpe275.order.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import cmpe275.order.model.MenuItem;

public class DatabaseService {

	EntityManagerFactory entityManagerFactory;
	EntityManager entityManager;
	
	DatabaseService() {
		entityManagerFactory = Persistence.createEntityManagerFactory("CMPE275-Project");
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	public void addItem(MenuItem menu) {
		entityManager.getTransaction().begin();
		entityManager.persist(menu);
		entityManager.getTransaction().commit();
	}
}
