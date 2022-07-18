package com.bochkarev.restbackend.tools;

import org.apache.commons.lang3.RandomStringUtils;

public class Tools {
    public static String getRandomString(final int length, final boolean useLetters, final boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
