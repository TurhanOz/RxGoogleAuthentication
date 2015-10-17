package com.turhanoz.android.rxgoogleauthentication;

import android.accounts.Account;
import android.app.Activity;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.turhanoz.android.rxgoogleauthentication.Utils.checkArgumentNotNull;

public class AuthSubscription {
    Subscription subscription;
    AuthCallback callback;
    String email;
    String scope;
    Account account;
    Activity activity;
    AuthObservable authObservable;

    public AuthSubscription() {
        authObservable = new AuthObservable();
    }

    public AuthSubscription setEmail(String email) {
        this.email = email;
        return this;
    }

    public AuthSubscription setAccount(Account account) {
        this.account = account;
        return this;
    }

    public AuthSubscription setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public AuthSubscription setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public AuthSubscription setCallback(AuthCallback callback) {
        this.callback = callback;
        return this;
    }

    public Subscription buildAndSubscribe() {
        checkArgumentsConsistency();
        Observable<AuthToken> observable = buildObservable();

        subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AuthObserver(activity, callback));

        return subscription;
    }

    private void checkArgumentsConsistency() {
        checkArgumentNotNull(scope, "scope");
        checkArgumentNotNull(activity, "Activity");
        checkArgumentNotNull(callback, "AuthCallback");

        if (this.email == null && this.account == null) {
            checkArgumentNotNull(null, "Account or Email");
        }
    }

    private Observable<AuthToken> buildObservable() {
        if (isAccountSet()) {
            return authObservable.getToken(activity, account, scope);
        } else {
            return authObservable.getToken(activity, email, scope);
        }
    }

    private boolean isAccountSet() {
        return account != null;
    }

    public void cancelPreviousSubscription() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = null;
    }
}
