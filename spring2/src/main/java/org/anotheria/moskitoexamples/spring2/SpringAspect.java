package org.anotheria.moskitoexamples.spring2;

import net.anotheria.moskito.aop.aspect.AbstractMoskitoAspect;
import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TraceStep;
import net.anotheria.moskito.core.calltrace.TracedCall;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.predefined.ServiceStats;
import net.anotheria.moskito.core.predefined.ServiceStatsFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 16:17
 */
@Aspect
public class SpringAspect extends AbstractMoskitoAspect {

	@Pointcut("execution(* *.*(..)) && !within(org.anotheria.moskitoexamples.spring2.SpringAspect)")
	public void aroundEveryMethod() {}

	/**
	 * Factory constant is needed to prevent continuous reinstantiation of ServiceStatsFactory objects.
	 */
	private static final ServiceStatsFactory FACTORY = new ServiceStatsFactory();

	/*  */
	public Object doProfiling(ProceedingJoinPoint pjp) throws Throwable {
		String aProducerId = pjp.getTarget().getClass().getSimpleName();
		String aSubsystem = "default";
		String aCategory = "spring-aop";

		OnDemandStatsProducer<ServiceStats> producer = getProducer(pjp, aProducerId, aCategory, aSubsystem, false, FACTORY);
		String producerId = producer.getProducerId();

		String caseName = pjp.getSignature().getName();
		ServiceStats defaultStats = producer.getDefaultStats();
		ServiceStats methodStats = producer.getStats(caseName);

		final Object[] args = pjp.getArgs();
		final String method = pjp.getSignature().getName();
		defaultStats.addRequest();
		if (methodStats != null) {
			methodStats.addRequest();
		}
		TracedCall aRunningTrace = RunningTraceContainer.getCurrentlyTracedCall();
		TraceStep currentStep = null;
		CurrentlyTracedCall currentTrace = aRunningTrace.callTraced() ? (CurrentlyTracedCall) aRunningTrace : null;
		if (currentTrace != null) {
			StringBuilder call = new StringBuilder(producerId).append('.').append(method).append("(");
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					call.append(args[i]);
					if (i < args.length - 1) {
						call.append(", ");
					}
				}
			}
			call.append(")");
			currentStep = currentTrace.startStep(call.toString(), producer);
		}
		long startTime = System.nanoTime();
		Object ret = null;
		try {
			ret = pjp.proceed();
			return ret;
		} catch (InvocationTargetException e) {
			defaultStats.notifyError();
			if (methodStats != null) {
				methodStats.notifyError();
			}
			//System.out.println("exception of class: "+e.getCause()+" is thrown");
			if (currentStep != null) {
				currentStep.setAborted();
			}
			throw e.getCause();
		} catch (Throwable t) {
			defaultStats.notifyError();
			if (methodStats != null) {
				methodStats.notifyError();
			}
			if (currentStep != null) {
				currentStep.setAborted();
			}
			throw t;
		} finally {
			long exTime = System.nanoTime() - startTime;
			defaultStats.addExecutionTime(exTime);
			if (methodStats != null) {
				methodStats.addExecutionTime(exTime);
			}
			defaultStats.notifyRequestFinished();
			if (methodStats != null) {
				methodStats.notifyRequestFinished();
			}
			if (currentStep != null) {
				currentStep.setDuration(exTime);
				try {
					currentStep.appendToCall(" = " + ret);
				} catch (Throwable t) {
					currentStep.appendToCall(" = ERR: " + t.getMessage() + " (" + t.getClass() + ")");
				}
			}
			if (currentTrace != null) {
				currentTrace.endStep();
			}
		}
	}

}
