package org.anotheria.moskitoexamples.dynamicproxy.service;

import net.anotheria.moskito.core.dynamic.MoskitoInvokationProxy;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.ProxyUtils;
import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.predefined.ServiceStatsCallHandler;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.NoSuchProducerException;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 25.11.12 18:57
 */
public class SimpleServiceTest {
	@Test
	public void testWithoutMonitoring(){
		SimpleService service = new SimpleServiceImpl();
		for (int i=0; i<10; i++){
			service.doSomethingMethod();
		}

		//in this case we don't have anything in the Registry.
		IProducerRegistryAPI registry = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		try{
			registry.getProducer("SimpleService");
			registry.getProducer("SimpleService-1");
			fail("There should be no producer in the factory");
		}catch(NoSuchProducerException e){}

	}

	@Test
	public void testWithMonitoringViaProxyUtils() throws Exception{
		SimpleService service = ProxyUtils.createServiceInstance(new SimpleServiceImpl(), "default", SimpleService.class);
		for (int i=0; i<10; i++){
			service.doSomethingMethod();
		}

		//in this case we don't have anything in the Registry.
		IProducerRegistryAPI registry = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		OnDemandStatsProducer<ServiceStats> producer = (OnDemandStatsProducer<ServiceStats>)registry.getProducer("SimpleService-1");
		assertNotNull(producer);
		ServiceStats methodStats = producer.getStats("doSomethingMethod");
		assertEquals(10, methodStats.getTotalRequests());

	}

	@Test
	public void testWithMonitoringViaInvocationProxy() throws Exception{
		SimpleService unmonitoredInstance = new SimpleServiceImpl();
		MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(
				unmonitoredInstance,
				new ServiceStatsCallHandler(),
				new ServiceStatsFactory(),
				"SimpleService",
				"service",
				"test-sub-system",
				SimpleService.class
				);
		SimpleService monitoredInstance = (SimpleService)proxy.createProxy();

		for (int i=0; i<10; i++){
			monitoredInstance.doSomethingMethod();
		}

		//in this case we don't have anything in the Registry.
		IProducerRegistryAPI registry = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		OnDemandStatsProducer<ServiceStats> producer = (OnDemandStatsProducer<ServiceStats>)registry.getProducer("SimpleService");
		assertNotNull(producer);
		ServiceStats methodStats = producer.getStats("doSomethingMethod");
		assertEquals(10, methodStats.getTotalRequests());

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
