package com.turhanoz.android.rxgoogleauthentication;

import android.accounts.Account;
import android.content.Context;

import com.google.android.gms.auth.GoogleAuthUtil;

import rx.Observable;
import rx.Subscriber;

public class AuthObservable {
    @Deprecated
    public Observable<AuthToken> getToken(final Context context, final String email, final String scope) {
        return Observable.create(new Observable.OnSubscribe<AuthToken>() {
            @Override
            public void call(Subscriber<? super AuthToken> observer) {
                try {
                    String token = GoogleAuthUtil.getToken(context, email, scope);
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(new AuthToken(token, scope));
                        observer.onCompleted();
                    }
                }
                catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    public Observable<AuthToken> getToken(final Context context, final Account account, final String scope) {
        return Observable.create(new Observable.OnSubscribe<AuthToken>() {
            @Override
            public void call(Subscriber<? super AuthToken> observer) {
                try {
                    String token = GoogleAuthUtil.getToken(context, account, scope);
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(new AuthToken(token, scope));
                        observer.onCompleted();
                    }
                }
                catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }
}
