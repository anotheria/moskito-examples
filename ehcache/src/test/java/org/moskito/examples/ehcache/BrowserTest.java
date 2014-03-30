package org.moskito.examples.ehcache;

import net.anotheria.moskito.core.dynamic.OnDemandStatsProducer;
import net.anotheria.moskito.core.dynamic.OnDemandStatsProducerException;
import net.anotheria.moskito.core.registry.IProducerRegistryAPI;
import net.anotheria.moskito.core.registry.ProducerRegistryAPIFactory;
import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.integration.ehcache.EhcacheStats;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test to demonstrate MoSKito-Ehcache integration example in work.
 *
 * @author Vladyslav Bezuhlyi
 */
public class BrowserTest {

    private static Browser browser;
    private static Internet internet;
    private static Webmaster webmaster;

    @BeforeClass
    public static void startup() {
        webmaster = new Webmaster();
        List<Webpage> webpages = new LinkedList<Webpage>();
        for (int i = 1; i <= 100 ; i++) {
            webpages.add(webmaster.produceWebpage());
        }
        /* hundred pages, that was just the beginning of the Internet */
        internet = new Internet(webpages);
        /* saying the browser: "You work with this Internet" */
        browser = new Browser(internet);
    }

    @Test
    public void openManyWebpages() throws InterruptedException, OnDemandStatsProducerException {
        /* lets open every Internet web-page 100 times */
        for (int i = 1; i <= 100; i++) {
            List<String> urls = internet.getAllUrls();
            for (String url : urls) {
                browser.renderWebpage(url);
            }
        }


        /* waiting some time for next stats update */
        Thread.sleep(5500);

        /* if there is no MoSKito WebUI embedded in your application, stats can be accessed in a way like this */
        IProducerRegistryAPI registryAPI = new ProducerRegistryAPIFactory().createProducerRegistryAPI();
        OnDemandStatsProducer<EhcacheStats> producer = (OnDemandStatsProducer<EhcacheStats>) registryAPI.getProducer("browser-cache");
        EhcacheStats stats =  producer.getStats("cumulated");
        System.out.println(stats.toStatsString(null, TimeUnit.MILLISECONDS));

        /* first 100 accesses will not result in hits because we don't have this pages in cache yet */
        assertEquals(9900, stats.getHits().getValueAsLong());
    }

    @AfterClass
    public static void cleanup() {
        /* notice the insides of the last thing we should do */
        browser.exit();
    }

}
