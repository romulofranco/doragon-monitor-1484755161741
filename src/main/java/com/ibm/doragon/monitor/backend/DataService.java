package com.ibm.doragon.monitor.backend;

import java.io.Serializable;
import java.util.List;

import com.ibm.doragon.monitor.backend.data.Monitor;
import com.ibm.doragon.monitor.backend.data.Target;

public abstract class DataService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract List<Target> getAllTargets();

	public abstract boolean insertTarget(Target target);

	public abstract boolean insertMonitor(Monitor monitor);
	
	public abstract boolean insertMonitors(List<Monitor> monitors);
	
	public abstract Target findTarget(Long id);
	

}
