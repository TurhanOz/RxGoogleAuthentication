package com.turhanoz.rxgoogleauthentication;

public interface AuthCallback {
    public void onTokenReceived(AuthToken token);
    public void onError(Throwable e);
}
