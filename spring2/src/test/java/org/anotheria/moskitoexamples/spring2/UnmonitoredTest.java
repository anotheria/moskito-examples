package org.anotheria.moskitoexamples.spring2;

import org.anotheria.moskitoexamples.spring2.a.AService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 14:33
 */
public class UnmonitoredTest {
	@Test
	public void testUnmonitoredCall(){
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"services.xml"});
		AService service = (AService)context.getBean("AService");
		assertEquals(2*100, service.echoMethodA(100));
	}
}
