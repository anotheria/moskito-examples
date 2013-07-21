package org.anotheria.moskitoexamples.spring2.a;

import org.anotheria.moskitoexamples.spring2.b.BService;
import org.anotheria.moskitoexamples.spring2.c.CService;
import org.anotheria.moskitoexamples.spring2.d.DService;
import org.springframework.beans.factory.annotation.Required;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 14:26
 */
public class AServiceImpl implements AService{

	private BService bService;

	private CService cService;

	private DService dService;

	@Override
	public long echoMethodA(long value) {
		dService.debugCall();
		return bService.echoServiceB(value)+cService.echo(value);
	}

	public BService getbService() {
		return bService;
	}

	@Required
	public void setbService(BService bService) {
		this.bService = bService;
	}

	public CService getcService() {
		return cService;
	}

	@Required
	public void setcService(CService cService) {
		this.cService = cService;
	}
	public DService getdService() {
		return dService;
	}

	@Required
	public void setdService(DService dService) {
		this.dService = dService;
	}


}

