package org.anotheria.moskitoexamples.spring2.a;

import org.anotheria.moskitoexamples.spring2.b.BService;
import org.springframework.beans.factory.annotation.Required;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 14:26
 */
public class AServiceImpl implements AService{

	private BService bService;

	@Override
	public long echoMethodA(long value) {
		return bService.echoServiceB(value);
	}

	public BService getbService() {
		return bService;
	}

	@Required
	public void setbService(BService bService) {
		this.bService = bService;
	}


}

