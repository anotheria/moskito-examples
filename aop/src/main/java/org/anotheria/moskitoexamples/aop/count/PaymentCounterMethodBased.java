package org.anotheria.moskitoexamples.aop.count;

import net.anotheria.moskito.aop.annotation.Count;

/**
 * Example of method based counters, only annotated methods are counted.
 *
 * @author lrosenberg
 * @since 19.11.12 09:15
 */
public class PaymentCounterMethodBased {

	public void pay(String method){
		//do some important payment stuff

		//count up
		if (method.equals("ec")){
			ec();
		}
		if (method.equals("cc")){
			cc();
		}
		if (method.equals("paypal")){
			paypal();
		}
	}

	@Count
	private void ec(){

	}

	@Count
	private void cc(){

	}

	@Count
	private void paypal(){

	}
}
