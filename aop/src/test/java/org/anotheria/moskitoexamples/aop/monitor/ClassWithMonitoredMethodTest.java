package org.anotheria.moskitoexamples.aop.monitor;

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

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 27.11.12 08:34
 */
public class ClassWithMonitoredMethodTest {
	@Test
	public void test() throws OnDemandStatsProducerException {
		ClassWithMonitoredMethod testObject = new ClassWithMonitoredMethod();

		//now call all methods
		for (int i=0; i<100; i++){
			testObject.firstMethod();
			testObject.secondMethod();
		}

		//test is finished, now to ensure that the class is really monitored...
		//you don't have to do this, as your data will probably show up in webui or logs or whatever configured.
		IProducerRegistryAPI registryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		OnDemandStatsProducer<ServiceStats> producer = (OnDemandStatsProducer<ServiceStats>)registryAPI.getProducer("ClassWithMonitoredMethod");
		assertNotNull(producer);

		//the firstMethod is monitored and should therefor produce stats.
		ServiceStats firstMethodStats = producer.getStats("firstMethod");
		assertNotNull(firstMethodStats);
		assertEquals(100, firstMethodStats.getTotalRequests());

		//a way to prove that secondMethod is not count, is to count:
		assertEquals(2, producer.getStats().size());


		//the secondMethodStats is NOT monitored and should therefor produce stats.
		ServiceStats secondMethodStats = producer.getStats("secondMethod");
		//even not monitored, the stats object will be created on the fly.
		assertNotNull(secondMethodStats);
		//but there will be no data.
		assertEquals(0, secondMethodStats.getTotalRequests());

		//the cumulated stats should contain only firstmethod, therefore 100
		ServiceStats cumulatedStats = producer.getDefaultStats();
		assertNotNull(cumulatedStats);
		assertEquals(100, cumulatedStats.getTotalRequests());



	}

	@AfterClass
	public static void cleanup(){
		ProducerRegistryFactory.reset();
	}
	@BeforeClass
	public static void setup(){
		//disable builtin producers
		System.setProperty("JUNITTEST", Boolean.TRUE.toString());
	}


}
