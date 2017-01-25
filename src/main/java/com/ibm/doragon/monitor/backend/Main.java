package com.ibm.doragon.monitor.backend;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.ibm.doragon.monitor.backend.data.Availability;
import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "DoragonMonitorJPA";
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		EntityManager em = factory.createEntityManager();
		//
		// create new todo
		em.getTransaction().begin();
		Target target = new Target();
		target.setName("This is a test");
		target.setAddress("This is a test");
		target.setAccessType("1");
		target.setActive("1");
		em.persist(target);
		em.getTransaction().commit();

		// read the existing entries and write to console
		Query q = em.createQuery("select t from Target t");
		List<Target> targetList = q.getResultList();
		for (Target t : targetList) {
			System.out.println(t);
		}
		System.out.println("Size: " + targetList.size());
		//
		
		Target tem = em.find(Target.class, target.getId());
		System.out.println("Target by Find " + tem);
		
		TypedQuery<Target> tq = em.createQuery("select t from Target t where t.id = :targetid", Target.class);
		tq.setParameter("targetid", 3);
		if (tq.getResultList().size() > 0) {
			Target tg = tq.getSingleResult();
			System.out.println("TypedQuery " + tg);
		}

		em.getTransaction().begin();
		Monitor monitor = new Monitor();
		monitor.setTargetId(target);
		monitor.setDateTime(new Timestamp(System.currentTimeMillis()));
		monitor.setStatus(0);
		monitor.setPerformance(200);
		em.persist(monitor);
		em.getTransaction().commit();
		em.close();
		System.out.println(monitor);
	}
}