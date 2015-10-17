package com.turhanoz.rxgoogleauthentication;

public class Utils {
    public static void checkArgumentNotNull(Object object, String qualifier) {
        if (object == null) {
            String message = "You need to set a non null ";
            throw new IllegalArgumentException(message + qualifier);
        }
    }
}
