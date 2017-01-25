package com.ibm.doragon.monitor.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;
import com.ibm.doragon.monitor.backend.service.DoragonMonitorService;

@ApplicationPath("rest")
@Path("collector")
public class MonitorCollectorService extends Application {

	final static Logger logger = Logger.getLogger(MonitorCollectorService.class);

	@GET
	@Path("getArray")
	@Produces("application/json")
	public List<String> getSampleData(@QueryParam("value") String value) {
		return Arrays.asList(new String[] { "one", "two", "three", "four", value });
	}

	@GET
	@Path("getString")
	@Produces("application/json")
	public String getString() {
		return "Test";
	}

	@GET
	@Path("getAllTargets")
	@Produces("application/json")
	public List<Target> getAllTargets() {

		if (logger.isDebugEnabled()) {
			logger.debug("getAllTargets");
		}

		DoragonMonitorService service = DoragonMonitorService.getInstance();
		return service.getAllTargets();
	}

	@POST
	@Path("/insertTarget")
	@Consumes("application/json")
	public Response insertTarget(Target target) {

		if (logger.isDebugEnabled()) {
			logger.debug("insertTarget: " + target);
		}

		/**
		 * For testing use:
		 * http://localhost:9888/DoragonMonitor/rest/collector/insertTarget JSON
		 * Object: {"name":"Maximo
		 * DEV","address":"http://local","accessType":"1","active":"1"} Method
		 * POST
		 */

		try {
			if (target == null) {
				return Response.status(200).entity("Target is invalid").build();
			}
			DoragonMonitorService service = DoragonMonitorService.getInstance();
			service.insertTarget(target);
			return Response.status(200).entity("Target successfully inserted on Database.").build();
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@POST
	@Path("/insertDetailedMonitor")
	@Consumes("application/json")
	public Response insertMonitor(Monitor monitor, boolean detailed) {
		try {
			DoragonMonitorService service = DoragonMonitorService.getInstance();
			service.insertMonitor(monitor);
			return Response.status(200).entity("Monitor successfully inserted on Database.").build();
		} catch (Exception e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Path("/insertMonitor")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMonitor(@FormParam("targetId") String targetId,
			@FormParam("availability") String availability, @FormParam("performance") String performance) {

		/**
		 * For testing use:
		 * 
		 */

		if (logger.isDebugEnabled()) {
			logger.debug("insertMonitor: " + targetId + " / " + availability + " / " + performance);
		}

		try {
			DoragonMonitorService service = DoragonMonitorService.getInstance();
			Target target = service.findTarget(Long.parseLong(targetId));
			if (target == null)
				return Response.status(405).entity("Target was not found.").build();
			else {
				Monitor monitor = new Monitor();
				monitor.setTargetId(target);
				monitor.setDateTime(new Timestamp(System.currentTimeMillis()));

				if (availability.equals("1"))
					monitor.setStatus(1);
				else
					monitor.setStatus(0);

				monitor.setPerformance(Integer.parseInt(performance));

				service.insertMonitor(monitor);
				return Response.status(200).entity("Monitor successfully inserted on Database - " + monitor.toString())
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(500);
		}
	}

	
	@POST
	@Path("/monitors")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertMonitors(String[] monitors) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("insertMonitors: " + monitors);
		}
		
		try {
			DoragonMonitorService service = DoragonMonitorService.getInstance();
			List<Monitor> monitorWrapped = service.wrapperMonitor(monitors);
			
			if (monitorWrapped.size() > 0) {
				service.insertMonitors(monitorWrapped);
				return Response.status(200).entity("Monitor successfully inserted on Database.").build();
			} else {
				return Response.status(200).entity("Invalid data").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(500);
		}
	}
}