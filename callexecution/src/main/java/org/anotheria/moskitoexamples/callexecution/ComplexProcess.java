package org.anotheria.moskitoexamples.callexecution;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import net.anotheria.moskito.core.producers.CallExecution;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;

/**
 * This class illustrates usage of moskito in a complex process.
 */
public class ComplexProcess{

	private OnDemandStatsProducer<ServiceStats> producer;

	public ComplexProcess(){
		producer = new OnDemandStatsProducer<ServiceStats>("complexprocess", "category", "subsystem", ServiceStatsFactory.DEFAULT_INSTANCE);
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(producer);
	}

	public void methodWhichIsPartiallyMonitored(){
		//doing something
		//... bla boo bla

		//here comes monitored part
		try{
			CallExecution execution = producer.getStats("methodWhichIsPartiallyMonitored").createCallExecution();
			execution.startExecution();
			//now we are doing something extremely important...
			execution.finishExecution();

		}catch(OnDemandStatsProducerException e){
			//react somehow, preferably smarter than the following line:
			throw new AssertionError("This should never happen in the example environment");
		}

		//doing something else
		// boo bla boo
	}

	public void methodWithMultipleComplexSubprocesses(){
		//doing something
		//... bla boo bla

		//here comes the beginning of our complex process
		try{
			CallExecution execution = producer.getStats("phase1").createCallExecution();
			execution.startExecution();
			//now we are doing something extremely important...
			execution.finishExecution();

			//now we are doing phase 2. For whatever reasons we have to do it in a loop or something.
			for (int i=0; i<3; i++){
				execution = producer.getStats("phase2").createCallExecution();
				execution.startExecution();
				//now we are doing something extremely important...
				execution.finishExecution();
			}
			//no we do something else, until we finally have to do the last phase twice...

			execution = producer.getStats("phase3").createCallExecution();
			execution.startExecution();
			//now we are doing something extremely important...
			execution.finishExecution();
			execution = producer.getStats("phase3").createCallExecution();
			execution.startExecution();
			//now we are doing something extremely important...
			execution.finishExecution();

			//now we are all set


		}catch(OnDemandStatsProducerException e){
			//react somehow, preferably smarter than the following line:
			throw new AssertionError("This should never happen in the example environment");
		}

		//doing something else
		// boo bla boo


	}
}