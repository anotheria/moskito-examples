package org.moskito.examples.ehcache;

import net.anotheria.moskito.integration.ehcache.MonitoredEhcache;
import net.anotheria.moskito.integration.ehcache.PeriodicStatsUpdater;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;

/**
 * It is used to view pages on the Internet.
 * Imagine some browser you wouldn't use in real life.
 *
 * @author Vladyslav Bezuhlyi
 */
public class Browser {

    private static final Webpage WEBPAGE_404 = new Webpage("", "404 : page not found");

    private Internet internet;

    private Ehcache cache;


    public Browser(Internet internet) {
        this.internet = internet;
        /* initializing browser cache */
        cache = CacheManager.getInstance().getCache("browser-cache");
        /* making this cache to be monitored (statistics updates every 5 seconds) */
        cache = new MonitoredEhcache(cache, Statistics.STATISTICS_ACCURACY_GUARANTEED, 5*1000L);
    }


    public void renderWebpage(String url) {
        Webpage webpage = openWebpage(url);
//        System.out.println("Page URL:");
//        System.out.println(webpage.getUrl());
//        System.out.println("Page content:");
//        System.out.println(webpage.getContent());
    };

    private Webpage openWebpage(String url) {
        Webpage webpage = openWebpageFromCache(url);
        if (webpage == null) {
            return WEBPAGE_404;
        }

        return webpage;
    }

    private Webpage openWebpageFromCache(String url) {
        Element element = cache.get(url);
        if (element != null) {
            return (Webpage) element.getObjectValue();
        }

        Webpage webpage = internet.returnWebpage(url);
        if (webpage == null) {
            /* we don't caching the page if it doesn't exist */
            return null;
        }
        cache.put(new Element(webpage.getUrl(), webpage));

        return webpage;
    }

    public void exit() {
        /* stopping stats updates for all monitored caches */
        PeriodicStatsUpdater.cleanup();
    }

}
