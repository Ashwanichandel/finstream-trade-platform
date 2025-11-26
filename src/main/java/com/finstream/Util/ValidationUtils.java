package com.finstream.Util;


public class ValidationUtils {

    public static boolean isValidOrderType(String s) {
        return "BUY".equalsIgnoreCase(s) || "SELL".equalsIgnoreCase(s);
    }
}
