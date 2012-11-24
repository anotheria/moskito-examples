package org.anotheria.moskitoexamples.aop.monitor;

import net.anotheria.moskito.aop.annotation.DontMonitor;
import net.anotheria.moskito.aop.annotation.Monitor;

/**
 * This is an example for moskito aop integration.
 * Please check https://confluence.opensource.anotheria.net/display/MSK/Integration+Guide for details.
 * @author lrosenberg
 */
@Monitor
public class MonitoredClass {
	public void firstMethod(){
		//do something
	}

	public void secondMethod(){
		//do something else
	}

	@DontMonitor
	public void doNotMonitorMe(){

	}
}
