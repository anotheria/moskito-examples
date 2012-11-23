package org.anotheria.moskitoexamples.aop;

import net.anotheria.moskito.aop.annotation.Monitor;

/**
 * This is an example for moskito aop integration.
 * Please check https://confluence.opensource.anotheria.net/display/MSK/Integration+Guide for details.
 * @author lrosenberg
 */
public class ClassWithMonitoredMethod {
	@Monitor
	public void firstMethod(){
		//do something
	}

	public void secondMethod(){
		//do something else
	}
}
