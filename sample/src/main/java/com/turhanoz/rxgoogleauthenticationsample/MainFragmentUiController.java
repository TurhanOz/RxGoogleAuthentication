package com.turhanoz.rxgoogleauthenticationsample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Set;

public class MainFragmentUiController{
     Activity activity;
    View rootView;
    MaterialBetterSpinner accountSpinner;
    MaterialBetterSpinner scopeSpinner;
    Button getTokenButton;
    TextView resultTextView;

    OAuth2Scope scope;


    public MainFragmentUiController(Activity activity, View rootView) {
        this.rootView = rootView;
        this.activity = activity;

        initButtonAndResult(rootView);
        initAccountSpinner(rootView);
        initScopeSpinner(rootView);
    }

    private void initButtonAndResult(View rootView) {
        getTokenButton = (Button) rootView.findViewById(R.id.b_getToken);
        resultTextView = (TextView) rootView.findViewById(R.id.tv_result);
    }


    private void initAccountSpinner(View rootView) {
        ArrayAdapter<String> adapter = new ArrayAdapter(activity,
                android.R.layout.simple_dropdown_item_1line, getAccountsEmail());
        accountSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.s_account);
        accountSpinner.setAdapter(adapter);
    }

    private ArrayList<String> getAccountsEmail() {
        AccountManager accountManager = AccountManager.get(activity);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        ArrayList<String> emails = new ArrayList<>();
        for (Account account : accounts) {
            emails.add(account.name);
        }
        return emails;
    }

    private void initScopeSpinner(View rootView) {
        scope = new OAuth2Scope();
        Set<String> keys = scope.keySet();
        ArrayAdapter<String> adapter = new ArrayAdapter(activity,
                android.R.layout.simple_dropdown_item_1line, keys.toArray(new String[keys.size()]));
        scopeSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.s_scope);
        scopeSpinner.setAdapter(adapter);
    }

    public TextView getResultTextView() {
        return resultTextView;
    }

    public Button getTokenButton() {
        return getTokenButton;
    }

    public String getSelectedAccount(){
        return accountSpinner.getText().toString();
    }

    public String getSelectedScope(){
        return scope.get(scopeSpinner.getText().toString());
    }

    public void setResult(String result){
        resultTextView.setText(result);
    }

}
