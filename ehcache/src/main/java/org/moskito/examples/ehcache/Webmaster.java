package org.moskito.examples.ehcache;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This guy/girl is obsessed with creating and publishing new web-pages all over the Internet.
 *
 * @author Vladyslav Bezuhlyi
 */
public class Webmaster {

    public Webpage produceWebpage() {
        return new Webpage(randomUrl(), randomContent());
    }

    private String randomUrl() {
        return "http://moskito.org/"+randomString(50);
    }

    private String randomContent() {
        return randomString(250);
    }

    private String randomString(int length) {
        Random random = new SecureRandom();
        char[] string = new char[length];
        char[] charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        for (int i = 0; i < string.length; i++) {
            int randomCharIndex = random.nextInt(charSet.length);
            string[i] = charSet[randomCharIndex];
        }
        return new String(string);
    }

}
