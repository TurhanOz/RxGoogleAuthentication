package com.turhanoz.rxgoogleauthentication;

import android.accounts.Account;
import android.app.Activity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Subscription;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AuthSubscriptionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    Activity mockActivity;
    AuthCallback mockCallback;
    Account mockAccount;
    AuthSubscription sut;

    @Before
    public void setUp() throws Exception {
        mockActivity = mock(Activity.class);
        mockCallback = mock(AuthCallback.class);
        mockAccount = mock(Account.class);
        sut = new AuthSubscription();
        stubAuthObservable();
        buildAuthSubscription();
    }

    private void stubAuthObservable() {
        sut.authObservable = mock(AuthObservable.class);
        when(sut.authObservable.getToken(mockActivity, mockAccount, "scope")).thenReturn(mock(Observable.class));
        when(sut.authObservable.getToken(mockActivity, "email", "scope")).thenReturn(mock(Observable.class));

    }

    private void buildAuthSubscription() {
        sut.setActivity(mockActivity)
                .setCallback(mockCallback)
                .setAccount(mockAccount)
                .setEmail("email")
                .setScope("scope");
    }

    @Test
    public void shouldThrowExceptionIfScopeMissing() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You need to set a non null scope");

        sut.setScope(null).buildAndSubscribe();
    }

    @Test
    public void shouldThrowExceptionIfActivityMissing() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You need to set a non null Activity");

        sut.setActivity(null).buildAndSubscribe();
    }

    @Test
    public void shouldThrowExceptionIfCallbackMissing() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You need to set a non null AuthCallback");

        sut.setCallback(null).buildAndSubscribe();
    }

    @Test
    public void shouldThrowExceptionIfEmailOrAccountMissing() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("You need to set a non null Account or Email");

        sut.setEmail(null).setAccount(null).buildAndSubscribe();
    }

    @Test
    public void shouldCollaborateWithAuthObservable() throws Exception {
        sut.setAccount(null).buildAndSubscribe();
        verify(sut.authObservable).getToken(mockActivity, "email", "scope");

        sut.setEmail(null).setAccount(mockAccount).buildAndSubscribe();
        verify(sut.authObservable).getToken(mockActivity, mockAccount, "scope");
    }

    @Test
    public void shouldCancelSubscription() throws Exception {
        Subscription mockSubscription = mock(Subscription.class);
        sut.subscription = mockSubscription;

        sut.cancelPreviousSubscription();

        verify(mockSubscription).unsubscribe();
        assertNull(sut.subscription);
    }

    @Test
    public void shouldCreateNewSubscription() throws Exception {
        Subscription mockSubscription = mock(Subscription.class);
        sut.subscription = mockSubscription;

        sut.buildAndSubscribe();

        assertNotSame(sut.subscription, mockSubscription);

    }
}