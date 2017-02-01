package com.ibm.doragon.monitor.backend.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;

public class DoragonMonitorService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(DoragonMonitorService.class);

	private static DoragonMonitorService INSTANCE;

	private EntityManagerFactory emFactory;

	private DoragonMonitorService() {
		emFactory = Persistence.createEntityManagerFactory("DoragonMonitorJPA");
	}

	public synchronized static DoragonMonitorService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DoragonMonitorService();
		}
		return INSTANCE;
	}

	public EntityManager getEntityManager() {
		return emFactory.createEntityManager();
	}

	public void close() {
		emFactory.close();
	}

	public List<Target> getAllTargets() {

		EntityManager em = this.getEntityManager();
		TypedQuery<Target> q = em.createQuery("select t from Target t", Target.class);
		List<Target> targets = q.getResultList();
		em.close();
		return targets;
	}

	public boolean insertTarget(Target target) {

		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		em.persist(target);
		em.getTransaction().commit();
		em.close();

		return true;
	}

	public Target findTarget(Long id) {
		EntityManager em = this.getEntityManager();
		Target target = em.find(Target.class, id);
		em.close();
		if (target != null)
			return target;
		else
			return null;
	}

	public Target findTarget(String targetName) {
		EntityManager em = this.getEntityManager();
		TypedQuery<Target> query = em.createQuery("select t from Target t where t.name = :name", Target.class);
		query.setParameter("name", targetName);

		Target target = query.getSingleResult();

		if (target != null)
			return target;
		else
			return null;
	}

	public boolean insertMonitor(Monitor monitor) {
		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		em.persist(monitor);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	public boolean insertMonitors(List<Monitor> monitors) {
		EntityManager em = this.getEntityManager();
		em.getTransaction().begin();
		
		for (Monitor monitor : monitors) {			
			em.persist(monitor);			
		}
		
		em.getTransaction().commit();

		em.close();
		return true;

	}

	/**
	 * Wrapper to convert a Array of monitor data in a Monitor entities Data:
	 * 
	 * @param data[0]
	 *            = Target ID
	 * @param data[1]
	 *            = Date/Time YYYY-MM-dd HH:mm:ss
	 * @param data[2]
	 *            = Status (0 Failure, 1 OK)
	 * @param data[3]
	 *            = Performance (Integer)
	 * 
	 * @param monitors
	 * @return
	 */
	public List<Monitor> wrapperMonitor(String[] monitors) {
		List<Monitor> lstMonitor = new ArrayList<Monitor>();

		for (String preMonitor : monitors) {
			String[] data = preMonitor.split(",");
			Monitor monitor = new Monitor();

			// TargetID
			if (!data[0].isEmpty()) {
				monitor.setTargetId(this.findTarget(Long.parseLong(data[0])));
			} else {
				continue;
			}

			if (monitor.getTargetId() == null) {
				logger.info("wrapperMonitor - Target " + data[0] + " not found on the database");
				continue;
			}

			// DateTime
			if (!data[1].isEmpty()) {
				monitor.setDateTime(this.convertStringToDate(data[1]));
			} else {
				continue;
			}

			// Status
			if (!data[2].isEmpty()) {
				monitor.setStatus(Integer.parseInt(data[2]));
			}

			// performance
			if (!data[3].isEmpty()) {
				monitor.setPerformance(Integer.parseInt(data[3]));
			}
			lstMonitor.add(monitor);

		}

		return lstMonitor;
	}

	public Date convertStringToDate(String strDate) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	public List<Monitor> getMonitorNotSent() {
		EntityManager em = this.getEntityManager();
		TypedQuery<Monitor> query = em.createQuery("select m from Monitor m where m.sent = null or m.sent = 0",
				Monitor.class);
		List<Monitor> monitors = query.getResultList();
		return monitors;
	}

	public String convertDateToString(Timestamp date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String dateString = dateFormat.format(date);
		System.out.println(dateString);
		return dateString;
	}

}