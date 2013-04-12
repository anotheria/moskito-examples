package org.anotheria.moskitoexamples.spring2;

import org.anotheria.moskitoexamples.spring2.a.AService;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

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
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"services-aop.xml"});
		AService service = (AService)context.getBean("AService");
		assertEquals(100, service.echoMethodA(100));

	}
}
