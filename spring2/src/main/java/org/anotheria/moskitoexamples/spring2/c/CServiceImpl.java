package org.anotheria.moskitoexamples.spring2.c;

import net.anotheria.moskito.aop.annotation.Monitor;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.04.13 17:14
 */
@Monitor
public class CServiceImpl implements CService {
	@Override
	public long echo(long value) {
		return value;
	}
}
