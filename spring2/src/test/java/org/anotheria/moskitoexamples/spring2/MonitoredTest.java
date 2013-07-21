package org.anotheria.moskitoexamples.spring2;

import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.NoSuchProducerException;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import org.anotheria.moskitoexamples.spring2.a.AService;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 16:03
 */
public class MonitoredTest {
	@Test
	public void testMonitoredCall(){
		BasicConfigurator.configure();
		System.setProperty("JUNITTEST", "" + Boolean.TRUE);
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"services-aop.xml"});
		AService service = (AService)context.getBean("AService");
		assertEquals(2*100, service.echoMethodA(100));

		//check that the call is traced
		IProducerRegistryAPI registryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
		IStatsProducer<ServiceStats> producerB = registryAPI.getProducer("BServiceImpl");
		assertEquals(1, producerB.getStats().get(0).getTotalRequests());

		IStatsProducer<ServiceStats> producerC = registryAPI.getProducer("CServiceImpl");
		assertEquals(1, producerC.getStats().get(0).getTotalRequests());

		try{
			IStatsProducer<ServiceStats> producerD = registryAPI.getProducer("DServiceImpl");
			fail("Exception expected");
		}catch(NoSuchProducerException expectedException){}
	}
}
