package org.anotheria.moskitoexamples.aop;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


/**
 * This is an example test for moskito aop integration.
 * Please check https://confluence.opensource.anotheria.net/display/MSK/Integration+Guide for details.
 */
public class MonitoredClassTest{
	@Test
	public void test() throws OnDemandStatsProducerException {
		MonitoredClass testObject = new MonitoredClass();

		//now call all methods
		for (int i=0; i<100; i++){
			testObject.firstMethod();
			testObject.secondMethod();
			testObject.doNotMonitorMe();
		}

		//test is finished, now to ensure that the class is really monitored...
		//you don't have to do this, as your data will probably show up in webui or logs or whatever configured.
		IProducerRegistryAPI registryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		OnDemandStatsProducer<ServiceStats> producer = (OnDemandStatsProducer<ServiceStats>)registryAPI.getProducer("MonitoredClass");
		assertNotNull(producer);

		ServiceStats firstMethodStats = producer.getStats("firstMethod");
		assertNotNull(firstMethodStats);
		assertEquals(100, firstMethodStats.getTotalRequests());

		ServiceStats secondMethodStats = producer.getStats("secondMethod");
		assertNotNull(secondMethodStats);
		assertEquals(100, secondMethodStats.getTotalRequests());

		//the cumulated stats should contain both
		ServiceStats cumulatedStats = producer.getDefaultStats();
		assertNotNull(cumulatedStats);
		assertEquals(200, cumulatedStats.getTotalRequests());


		//now ensure that the doNotMonitorMe is not being monitored.
		//we can't call getStats("doNotMonitorMe") directly, because onDemandStatsProducer would create it on the fly, instead we iterate ober exising stats
		for (ServiceStats stats : producer.getStats()){
			if (stats.getName().equals("doNotMonitorMe"))
				fail("Found doNotMonitorMe stats!");
		}

		//another way to prove is, is to count:
		assertEquals(3, producer.getStats().size());

	}

	@BeforeClass
	public static void setup(){
		//disable builtin producers
		System.setProperty("JUNITTEST", Boolean.TRUE.toString());
	}

	@AfterClass
	public static void cleanup(){
		ProducerRegistryFactory.reset();
	}
}