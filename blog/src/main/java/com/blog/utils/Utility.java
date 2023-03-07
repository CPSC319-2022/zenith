package com.blog.utils;

import java.time.Clock;

public class Utility {
    static final Clock UTC = Clock.systemUTC();

    /**
     * Returns the current time in UTC as a string.
     *
     * @return  String
     */
    public static String getCurrentTime() {
        return UTC.instant().toString();
    }
}
