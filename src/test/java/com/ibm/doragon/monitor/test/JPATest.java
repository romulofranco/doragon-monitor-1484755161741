package com.ibm.doragon.monitor.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;

/**
 * 
 */

/**
 * @author romulofranco
 *
 */
public class JPATest {

	private static final String PERSISTENCE_UNIT_NAME = "DoragonMonitorJPA";
	private EntityManagerFactory factory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();
		TypedQuery<Target> query = em.createQuery("SELECT t from Target t", Target.class);
		boolean createNewEntries = (query.getResultList().size() == 0);

		if (createNewEntries) {
			org.junit.Assert.assertTrue(query.getResultList().size() == 0);
			Target target = new Target();
			target.setName("Test");
			target.setAddress("http");
			target.setAccessType(Target.ACCESS_TYPE_HTTP);
			target.setActive("1");
			em.persist(target);

			Monitor monitor = new Monitor();
			monitor.setDateTime(new Timestamp(System.currentTimeMillis()));
			monitor.setTargetId(target);
			monitor.setPerformance(200);
			monitor.setStatus(200);
			em.persist(monitor);
		}
		em.close();
	}

	@Test
	public void checkTarget() {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Target> query = em.createQuery("SELECT t from Target t", Target.class);
		assertTrue(query.getResultList().size() > 0);
		em.close();
	}
	
	@Test
	public void checkMonitor() {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<Monitor> query = em.createQuery("SELECT m from Monitor m", Monitor.class);
		assertTrue(query.getResultList().size() > 0);
		em.close();
	}

}
