package com.ibm.doragon.monitor.test;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.ibm.doragon.monitor.backend.data.Availability;
import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "DoragonMonitorJPA";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		// read the existing entries and write to console
		Query q = em.createQuery("select t from Target t");
		List<Target> targetList = q.getResultList();
		for (Target target : targetList) {
			System.out.println(target);
		}
		System.out.println("Size: " + targetList.size());

		// create new target
		em.getTransaction().begin();
		Target target = new Target();
		target.setName("This is a test");
		target.setAddress("This is a test");
		em.persist(target);
		em.getTransaction().commit();

		em.getTransaction().begin();
		Monitor monitor = new Monitor();
		monitor.setStatus(1);
		em.persist(monitor);
		em.getTransaction().commit();
		em.close();
	}
}