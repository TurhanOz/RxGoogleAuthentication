package com.turhanoz.rxgoogleauthentication;

import android.app.Activity;
import android.app.Dialog;

import com.google.android.gms.common.GooglePlayServicesUtil;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.gms.ShadowGooglePlayServicesUtil;

import static org.mockito.Mockito.mock;

@Implements(GooglePlayServicesUtil.class)
public class EnhancedShadowGooglePlayServicesUtil extends ShadowGooglePlayServicesUtil {
    static Dialog mockDialog = mock(Dialog.class);

    @Implementation
    public static Dialog getErrorDialog(int errorCode, Activity activity, int requestCode) {
        return mockDialog;
    }
}
