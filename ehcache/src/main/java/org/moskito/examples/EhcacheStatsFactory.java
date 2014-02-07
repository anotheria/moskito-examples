package org.moskito.examples;

import net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory;

/**
 * Factory for {@link org.moskito.examples.EhcacheStats}.
 *
 * @author Vladyslav Bezuhlyi
 *
 * @see net.anotheria.moskito.core.dynamic.IOnDemandStatsFactory
 * @see org.moskito.examples.EhcacheStats
 */
public class EhcacheStatsFactory implements IOnDemandStatsFactory<EhcacheStats> {

    @Override
    public EhcacheStats createStatsObject(String name) {
        return new EhcacheStats(name);
    }

}
