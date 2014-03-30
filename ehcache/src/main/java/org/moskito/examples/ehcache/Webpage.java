package org.moskito.examples.ehcache;

import java.io.Serializable;

/**
 * Just an ordinary web-page. Implements {@link java.io.Serializable} in order to be available for caching.
 *
 * @author Vladyslav Bezuhlyi
 */
public class Webpage implements Serializable {

    private final String url;

    private final String content;


    public Webpage(String url, String content) {
        this.url = url;
        this.content = content;
    }


    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Webpage)) return false;

        Webpage webpage = (Webpage) o;
        if (content != null ? !content.equals(webpage.content) : webpage.content != null) return false;
        if (url != null ? !url.equals(webpage.url) : webpage.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Webpage{" +
                "content='" + content + '\'' +
                ",  url=" + url +
                '}';
    }

}
