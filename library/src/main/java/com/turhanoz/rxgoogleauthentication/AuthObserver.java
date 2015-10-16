package com.turhanoz.rxgoogleauthentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import rx.Observer;

public class AuthObserver implements Observer<AuthToken> {
    public static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 10042;
    Activity activity;
    AuthCallback callback;

    public AuthObserver(Activity activity, AuthCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof GooglePlayServicesAvailabilityException) {
            // The Google Play services APK is old, disabled, or not present.
            // Show a dialog created by Google Play services that allows
            // the user to update the APK
            int statusCode = ((GooglePlayServicesAvailabilityException) e)
                    .getConnectionStatusCode();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                    activity,
                    REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
            dialog.show();
        } else if (e instanceof UserRecoverableAuthException) {
            // Unable to authenticate, such as when the user has not yet granted
            // the app access to the account, but the user can fix this.
            // Forward the user to an activity in Google Play services.
            Intent intent = ((UserRecoverableAuthException) e).getIntent();
            activity.startActivityForResult(intent,
                    REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
        } else {
            callback.onError(e);
        }
    }

    @Override
    public void onNext(AuthToken token) {
        callback.onTokenReceived(token);
    }
}
