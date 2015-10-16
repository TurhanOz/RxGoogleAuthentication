package com.turhanoz.rxgoogleauthentication;

import android.accounts.Account;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.turhanoz.rxgoogleauthentication.shadows.ShadowGoogleAuthUtil;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CustomRunnerWithMoreShadows.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowGoogleAuthUtil.class})
public class AuthObservableTest {
    Context context;
    TestSubscriber<AuthToken> testSubscriber;
    String scope = "someScope";
    AuthObservable sut;


    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
        testSubscriber = new TestSubscriber<>();
        sut = new AuthObservable();
    }

    @Test
    public void shouldGetTokenFromEmail() throws Exception {
        String email = "someEmail";
        String tokenFromGoogleAuth = GoogleAuthUtil.getToken(context, email, scope);
        AuthToken expectedToken = new AuthToken(tokenFromGoogleAuth, scope);

        sut.getToken(context, email, scope).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(expectedToken));
        testSubscriber.assertCompleted();
    }

    @Test
    public void shouldGetTokenFromAccount() throws Exception {
        Account mockAccount = mock(Account.class);
        String tokenFromGoogleAuth = GoogleAuthUtil.getToken(context, mockAccount, scope);
        AuthToken expectedToken = new AuthToken(tokenFromGoogleAuth, scope);

        sut.getToken(context, mockAccount, scope).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(expectedToken));
        testSubscriber.assertCompleted();
    }

    @Test
    @Ignore //static method mocking...
    public void shouldNotifyOnError() throws Exception {
        Exception e = new Exception();
        when(GoogleAuthUtil.getToken(context, "email", scope)).thenThrow(e);

        sut.getToken(context, "email", scope).subscribe(testSubscriber);

        testSubscriber.assertError(e);
    }
}