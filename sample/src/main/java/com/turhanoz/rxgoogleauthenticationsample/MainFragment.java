package com.turhanoz.rxgoogleauthenticationsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turhanoz.android.rxgoogleauthentication.AuthCallback;
import com.turhanoz.android.rxgoogleauthentication.AuthObserver;
import com.turhanoz.android.rxgoogleauthentication.AuthSubscription;
import com.turhanoz.android.rxgoogleauthentication.AuthToken;

public class MainFragment extends Fragment implements View.OnClickListener, AuthCallback {
    MainFragmentUiController uiController;
    AuthSubscription subscription;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        uiController = new MainFragmentUiController(getActivity(), rootView);
        uiController.getTokenButton.setOnClickListener(this);

        return rootView;
    }

    private void initAuthSubscription() {
        subscription = new AuthSubscription()
                .setEmail(uiController.getSelectedAccount())
                .setScope(uiController.getSelectedScope())
                .setActivity(getActivity())
                .setCallback(this);
    }

    private void triggerAuthSubscription() {
        subscription.buildAndSubscribe();
    }

    private void cancelPreviousSubscription() {
        if (subscription != null) {
            subscription.cancelPreviousSubscription();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == AuthObserver.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == getActivity().RESULT_OK) {
            uiController.setResult("GoogleAuthException, trying again...");
            // Receiving a result that follows a GoogleAuthException, try auth again
            cancelPreviousSubscription();
            triggerAuthSubscription();
        } else if (resultCode == getActivity().RESULT_CANCELED) {
            uiController.setResult("cancel");
        }
    }

    @Override
    public void onTokenReceived(AuthToken token) {
        uiController.setResult(token.toString());
    }

    @Override
    public void onError(Throwable e) {
        uiController.setResult(e.toString());
    }


    @Override
    public void onClick(View view) {
        cancelPreviousSubscription();
        initAuthSubscription();
        triggerAuthSubscription();
    }
}
