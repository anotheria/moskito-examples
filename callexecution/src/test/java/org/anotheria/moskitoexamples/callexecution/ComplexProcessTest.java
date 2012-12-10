package org.anotheria.moskitoexamples.callexecution;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 10.12.12 12:13
 */
public class ComplexProcessTest {
	@Test
	public void methodWhichIsPartiallyMonitoredTest() throws Exception{
		ComplexProcess process =  new ComplexProcess();
		for (int i=0; i<10; i++){
			process.methodWhichIsPartiallyMonitored();
		}

		//now we check
		ServiceStats stats = ((OnDemandStatsProducer<ServiceStats>)(new ProducerRegistryAPIFactory().createProducerRegistryAPI().getProducer("complexprocess"))).getStats("methodWhichIsPartiallyMonitored");
		assertNotNull(stats);
		assertEquals(10, stats.getTotalRequests());
	}

	@Test public void methodWithMultipleComplexSubprocessesTest()throws Exception{
		ComplexProcess process =  new ComplexProcess();
		for (int i=0; i<10; i++){
			process.methodWithMultipleComplexSubprocesses();
		}

		OnDemandStatsProducer<ServiceStats> producer = (OnDemandStatsProducer<ServiceStats>)(new ProducerRegistryAPIFactory().createProducerRegistryAPI().getProducer("complexprocess"));
		//now we check
		ServiceStats phase1 = producer.getStats("phase1");
		ServiceStats phase2 = producer.getStats("phase2");
		ServiceStats phase3 = producer.getStats("phase3");
		assertNotNull(phase1);
		assertNotNull(phase2);
		assertNotNull(phase3);

		assertEquals(10, phase1.getTotalRequests());

		//called thrice per request
		assertEquals(30, phase2.getTotalRequests());
		//called twice per request
		assertEquals(20, phase3.getTotalRequests());
	}

	@BeforeClass
	public static void setup(){
		//disable builtin producers
		System.setProperty("JUNITTEST", Boolean.TRUE.toString());
	}

	@After
	public void cleanup(){
		ProducerRegistryFactory.reset();
	}
}


