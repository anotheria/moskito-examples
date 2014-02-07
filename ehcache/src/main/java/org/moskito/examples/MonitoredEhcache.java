package org.moskito.examples;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.constructs.EhcacheDecoratorAdapter;

import java.util.TimerTask;

/**
 * Proxy for {@link net.sf.ehcache.Ehcache} instances that allows MoSKito to access Ehcache stats.
 *
 * @author Vladyslav Bezuhlyi
 *
 * @see org.moskito.examples.PeriodicStatsUpdater
 * @see net.anotheria.moskito.core.util.BuiltInMemoryProducer
 * @see net.sf.ehcache.terracotta.InternalEhcache
 */
public class MonitoredEhcache extends EhcacheDecoratorAdapter {

    private OnDemandStatsProducer<EhcacheStats> cacheProducer;


    public MonitoredEhcache(Ehcache instance) {
        this(instance, Statistics.STATISTICS_ACCURACY_NONE);
    }

    public MonitoredEhcache(Ehcache instance, long updatePeriod) {
        this(instance, Statistics.STATISTICS_ACCURACY_NONE, updatePeriod);
    }

    public MonitoredEhcache(Ehcache instance, int statisticsAccuracy) {
        this(instance, statisticsAccuracy, "Ehcache", "cache", 60*1000L);
    }

    public MonitoredEhcache(Ehcache instance, int statisticsAccuracy, long updatePeriod) {
        this(instance, statisticsAccuracy, "Ehcache", "cache", updatePeriod);
    }

    public MonitoredEhcache(Ehcache instance, int statisticsAccuracy, String category, String subsystem, long updatePeriod) {
        super(instance);
        underlyingCache.setStatisticsAccuracy(statisticsAccuracy);
        underlyingCache.setStatisticsEnabled(true);
        cacheProducer = new OnDemandStatsProducer<EhcacheStats>(underlyingCache.getName(), category, subsystem, new EhcacheStatsFactory());
        ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(cacheProducer);

        PeriodicStatsUpdater.addTask(new TimerTask() {
            @Override
            public void run() {
                updateStats();
            }
        }, updatePeriod);

    }


    private EhcacheStats getProducerStats() {
        try {
            return cacheProducer.getStats(underlyingCache.getName());
        } catch (OnDemandStatsProducerException e) {
            throw new IllegalStateException(e);
        }
    }

    private void updateStats() {
        EhcacheStats stats = getProducerStats();
        Statistics statistics = underlyingCache.getStatistics();

        stats.getHits().setValueAsLong(statistics.getCacheHits());
        stats.getMisses().setValueAsLong(statistics.getCacheMisses());
        stats.getElements().setValueAsLong(statistics.getObjectCount());

        stats.getInMemoryHits().setValueAsLong(statistics.getInMemoryHits());
        stats.getInMemoryMisses().setValueAsLong(statistics.getInMemoryMisses());
        stats.getInMemoryElements().setValueAsLong(statistics.getMemoryStoreObjectCount());

        stats.getOnDiskHits().setValueAsLong(statistics.getOnDiskHits());
        stats.getOnDiskMisses().setValueAsLong(statistics.getOnDiskMisses());
        stats.getOnDiskElements().setValueAsLong(statistics.getDiskStoreObjectCount());

        stats.getOffHeapHits().setValueAsLong(statistics.getOffHeapHits());
        stats.getOffHeapMisses().setValueAsLong(statistics.getOffHeapMisses());
        stats.getOffHeapElements().setValueAsLong(statistics.getOffHeapStoreObjectCount());
    }

}
