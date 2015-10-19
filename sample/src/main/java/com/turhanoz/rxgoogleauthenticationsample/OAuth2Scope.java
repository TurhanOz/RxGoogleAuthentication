package com.turhanoz.rxgoogleauthenticationsample;

import java.util.HashMap;

//https://developers.google.com/identity/protocols/googlescopes
public class OAuth2Scope extends HashMap<String, String> {
    public static final String AUTH2 = "oauth2:";
    public static final String PROXIMITY_BEACON_API_SCOPE = AUTH2 + "https://www.googleapis.com/auth/userlocation.beacon.registry";
    public static final String DRIVE_API_SCOPE = AUTH2 + "https://www.googleapis.com/auth/drive";
    public static final String GMAIL_API_SCOPE = AUTH2 + "https://www.googleapis.com/auth/gmail.readonly";
    public static final String BOOK_API_SCOPE = AUTH2 + "https://www.googleapis.com/auth/books";

    public OAuth2Scope() {
        this.put("PROXIMITY BEACON API", PROXIMITY_BEACON_API_SCOPE);
        this.put("DRIVE API SCOPE", DRIVE_API_SCOPE);
        this.put("GMAIL API SCOPE", GMAIL_API_SCOPE);
        this.put("BOOK API SCOPE", BOOK_API_SCOPE);
    }
}
