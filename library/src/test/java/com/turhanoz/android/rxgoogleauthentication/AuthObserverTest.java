package com.turhanoz.android.rxgoogleauthentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.turhanoz.android.rxgoogleauthentication.shadows.EnhancedShadowGooglePlayServicesUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {EnhancedShadowGooglePlayServicesUtil.class})
public class AuthObserverTest {
    AuthCallback mockCallback;
    Activity activity;
    AuthObserver sut;

    @Before
    public void setUp() throws Exception {
        activity = mock(Activity.class);
        mockCallback = mock(AuthCallback.class);
        sut = new AuthObserver(activity, mockCallback);
    }

    @Test
    public void onNextShouldNotifyCallback() throws Exception {
        AuthToken mockToken = mock(AuthToken.class);

        sut.onNext(mockToken);

        verify(mockCallback).onTokenReceived(mockToken);
    }

    @Test
    public void onErrorShouldNotifyCallbackForOnlyUnhandledErrors() throws Exception {
        Exception exception = mock(Exception.class);

        sut.onError(exception);

        verify(mockCallback).onError(exception);
    }

    @Test
    public void userRecoverableAuthExceptionShouldNotNotifyCallback() throws Exception {
        Exception exception = mock(UserRecoverableAuthException.class);

        sut.onError(exception);

        verify(mockCallback, never()).onError(exception);
    }

    @Test
    public void userRecoverableAuthExceptionShouldBeResolved() throws Exception {
        Intent mockIntent = mock(Intent.class);
        UserRecoverableAuthException stubException = mock(UserRecoverableAuthException.class);
        when(stubException.getIntent()).thenReturn(mockIntent);

        sut.onError(stubException);

        verify(activity).startActivityForResult(mockIntent,
                sut.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
    }

    @Test
    public void googlePlayServicesAvailabilityExceptionShouldNotNotifyCallback() throws Exception {
        Exception exception = mock(GooglePlayServicesAvailabilityException.class);

        sut.onError(exception);

        verify(mockCallback, never()).onError(exception);
    }

    @Test
    public void googlePlayServicesAvailabilityExceptionShouldBeResolved() throws Exception {
        EnhancedShadowGooglePlayServicesUtil.mockDialog = mock(Dialog.class);
        GooglePlayServicesAvailabilityException stubException = mock(GooglePlayServicesAvailabilityException.class);

        sut.onError(stubException);

        verify(EnhancedShadowGooglePlayServicesUtil.mockDialog).show();
    }
}