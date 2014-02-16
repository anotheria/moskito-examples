package org.moskito.examples.ehcache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The whole Internet with tones of web-pages.
 *
 * @author Vladyslav Bezuhlyi
 */
public class Internet {

    private Map<String, Webpage> webpages = new HashMap<String, Webpage>();


    public Internet(List<Webpage> webpages) {
        for (Webpage webpage : webpages) {
            this.webpages.put(webpage.getUrl(), webpage);
        }
    }


    public Webpage returnWebpage(String url) {
        return webpages.get(url);
    }

    public void publishWebpage(Webpage webpage) {
        webpages.put(webpage.getUrl(), webpage);
    }

    public List<String> getAllUrls() {
        /* that's fantastic! */
        return new LinkedList<String>(webpages.keySet());
    }

}
