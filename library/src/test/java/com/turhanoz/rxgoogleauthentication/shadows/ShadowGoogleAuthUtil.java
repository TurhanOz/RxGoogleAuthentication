package com.turhanoz.rxgoogleauthentication.shadows;

import android.accounts.Account;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.IOException;

/**
 * Shadow for {@link com.google.android.gms.auth.GoogleAuthUtil}.
 */
@Implements(GoogleAuthUtil.class)
public class ShadowGoogleAuthUtil{

    @Implementation
    @Deprecated
    public static String getToken(Context context, String accountName, String scope) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return "token";
    }

    @Implementation
    public static String getToken(Context context, Account account, String scope) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return "token";
    }
}
