package org.moskito.examples;

import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.AbstractStats;
import net.anotheria.moskito.core.stats.StatValue;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.stats.impl.StatValueFactory;

/**
 * Container with Ehcache related stats.
 * Field set of this class could be extended to cover the whole Ehcache statistics.
 *
 * @author Vladyslav Bezuhlyi
 *
 * @see net.anotheria.moskito.core.producers.AbstractStats
 * @see net.sf.ehcache.Statistics
 */
public class EhcacheStats extends AbstractStats {

    private StatValue hits;
    private StatValue inMemoryHits;
    private StatValue offHeapHits;
    private StatValue onDiskHits;
    private StatValue misses;
    private StatValue inMemoryMisses;
    private StatValue offHeapMisses;
    private StatValue onDiskMisses;
    private StatValue elements;
    private StatValue inMemoryElements;
    private StatValue offHeapElements;
    private StatValue onDiskElements;


    public EhcacheStats(String name) {
        super(name);
        this.hits = newLongStatValue("hits");
        this.inMemoryHits = newLongStatValue("inMemoryHits");
        this.offHeapHits = newLongStatValue("offHeapHits");
        this.onDiskHits = newLongStatValue("onDiskHits");
        this.misses = newLongStatValue("misses");
        this.inMemoryMisses = newLongStatValue("inMemoryMisses");
        this.offHeapMisses = newLongStatValue("offHeapMisses");
        this.onDiskMisses = newLongStatValue("onDiskMisses");
        this.elements = newLongStatValue("elements");
        this.inMemoryElements = newLongStatValue("inMemoryElements");
        this.offHeapElements = newLongStatValue("offHeapElements");
        this.onDiskElements = newLongStatValue("onDiskElements");
    }

    private StatValue newLongStatValue(String valueName) {
        return StatValueFactory.createStatValue(0L, valueName, Constants.getDefaultIntervals());
    }


    public StatValue getHits() {
        return hits;
    }

    public StatValue getInMemoryHits() {
        return inMemoryHits;
    }

    public StatValue getOffHeapHits() {
        return offHeapHits;
    }

    public StatValue getOnDiskHits() {
        return onDiskHits;
    }

    public StatValue getMisses() {
        return misses;
    }

    public StatValue getInMemoryMisses() {
        return inMemoryMisses;
    }

    public StatValue getOffHeapMisses() {
        return offHeapMisses;
    }

    public StatValue getOnDiskMisses() {
        return onDiskMisses;
    }

    public StatValue getElements() {
        return elements;
    }

    public StatValue getInMemoryElements() {
        return inMemoryElements;
    }

    public StatValue getOffHeapElements() {
        return offHeapElements;
    }

    public StatValue getOnDiskElements() {
        return onDiskElements;
    }

    @Override
    public String toStatsString(String aIntervalName, TimeUnit unit) {
        return "EhcacheStats{" +
                " hits=" + hits.getValueAsString(aIntervalName) +
                ",  inMemoryHits=" + inMemoryHits.getValueAsString(aIntervalName) +
                ",  offHeapHits=" + offHeapHits.getValueAsString(aIntervalName) +
                ",  onDiskHits=" + onDiskHits.getValueAsString(aIntervalName) +
                ",  misses=" + misses.getValueAsString(aIntervalName) +
                ",  inMemoryMisses=" + inMemoryMisses.getValueAsString(aIntervalName) +
                ",  offHeapMisses=" + offHeapMisses.getValueAsString(aIntervalName) +
                ",  onDiskMisses=" + onDiskMisses.getValueAsString(aIntervalName) +
                ",  elements=" + elements.getValueAsString(aIntervalName) +
                ",  inMemoryElements=" + inMemoryElements.getValueAsString(aIntervalName) +
                ",  offHeapElements=" + offHeapElements.getValueAsString(aIntervalName) +
                ",  onDiskElements=" + onDiskElements.getValueAsString(aIntervalName) +
                '}';
    }

}
