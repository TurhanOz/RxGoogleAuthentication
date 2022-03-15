package com.xcoder.easyauth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.concurrent.CancellationException;

/**
 * A smart easy-2-use library for maintaining google oauth2 access-tokens that are used for authenticating google apis.
 * Every method of this class runs synchronously.
 */
public class EasyAuth {
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private String refreshToken;
    private SharedPreferences pref;

    public EasyAuth(@NonNull String clientId, @NonNull String clientSecret) {
        CLIENT_ID = clientId;
        CLIENT_SECRET = clientSecret;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    /**
     * Gets a new access token from the refresh token.
     *
     * @return A new access token or throws if failed to get.
     * @throws Exception - If the response is not successful. This exception will have most of the error details.
     */
    public String getAccessToken(@NonNull String refreshToken) throws Exception {
        String query = String.format("client_id=%1$s&" + "client_secret=%2$s&" + "refresh_token=%3$s&" + "grant_type=refresh_token", CLIENT_ID, CLIENT_SECRET, refreshToken);
        ANResponse<JSONObject> response = AndroidNetworking.post("https://oauth2.googleapis.com/token?" + query).build().executeForJSONObject();
        if (response.isSuccess())
            return response.getResult().optString("access_token");
        else {
            try {
                if (InetAddress.getByName("google.com").isReachable(10000))
                    throw new Exception(response.getError().getErrorBody());
                else
                    throw new ConnectException("Can't connect to google servers. Is your internet on ?");
            } catch (IOException e) {
                throw new ConnectException("Can't connect to google servers. Is your internet on ?");
            }
        }
    }


    /**
     * Fetches refresh code for refreshing access token in future. By default, this uses the library credentials.
     * If they don't work, set your own google API credentials statically. like {@code EasyAuth.CLIENT_ID = "xxxxxxxxxxx"}.
     * This method runs synchronously.
     *
     * @param authorizationCode Authorizing code (which you get when user allows consent of your API/application) for getting access/refresh token.
     *                          This code can only be used once. Calling this method more then 1 time with same code will return null.
     * @return A refresh code that can be used to exchange access token in future. You must save this on device.
     * @throws Exception if failed to get refresh token from the server. This exception will have most of the error details.
     */
    public String getRefreshCode(@NonNull String authorizationCode) throws Exception {
        String query = String.format("client_id=%1$s&" + "client_secret=%2$s&" + "code=%3$s&" + "redirect_uri=%4$s&" + "grant_type=authorization_code", CLIENT_ID, CLIENT_SECRET, authorizationCode, "https://github.com/ErrorxCode/EasyInsta/issues");
        ANResponse<JSONObject> response = AndroidNetworking.post("https://oauth2.googleapis.com/token?" + query).build().executeForJSONObject();
        if (response.isSuccess())
            return response.getResult().optString("refresh_token");
        else {
            try {
                if (InetAddress.getByName("google.com").isReachable(10000))
                    throw new Exception(response.getError().getErrorBody());
                else
                    throw new ConnectException("Can't connect to google servers. Is your internet on ?");
            } catch (IOException e) {
                throw new ConnectException("Can't connect to google servers. Is your internet on ?");
            }
        }
    }


    /**
     * A fully automated authorization flow that will automatically do everything from sign-in to refreshing token to get the access token.
     * This method will take care of token expiration,validation and authorization. To make this work, you have to override {@code onActivityResult()}
     * method passing it to this class {@link #onActivityResult(int, Intent, String, LoginFlowCallback)}. Calling this method first time will start up the login flow
     * and returns null. Access token will be fetched by {@link #onActivityResult(int, Intent, String, LoginFlowCallback)} in this case or
     * otherwise everytime it will directly return you the access code. It will only log-in when user has previously not signed in to google
     * (using {@link #startLoginFlow(Activity, String)}) or if the access was revoked for the application.
     *
     * @param context The activity which is requesting the token.
     * @param scope   Scope for the token.
     * @return Access-token that can be used for authentication google APis, null if failed to get.
     */
    public String autoRefreshingAccessToken(@NonNull Activity context, @NonNull String scope) {
        pref = context.getSharedPreferences("tokens", 0);
        refreshToken = pref.getString("refresh_token", "");
        try {
            if (refreshToken.isEmpty()) {
                startLoginFlow(context, scope);
                return null;
            } else
                return getAccessToken(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Starts google sign-in flow. This will prompt the user to select google account to perform login.
     * Once user is logged in, this will fetch the access and refresh token based on the scope provided.
     * override {@code onActivityResult()} of the activity and forward it to this class {@link #onActivityResult(int, Intent, String, LoginFlowCallback)}.
     */
    public void startLoginFlow(@NonNull Activity activity, @NonNull String scope) {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder()
                .requestEmail()
                .requestServerAuthCode(CLIENT_ID)
                .requestScopes(new Scope(scope))
                .build();
        Intent intent = GoogleSignIn.getClient(activity, options).getSignInIntent();
        pref = activity.getSharedPreferences("tokens", 0);
        activity.startActivityForResult(intent, 0);
    }


    /**
     * Exchanges authorization token with access token. Once user chooses account and allows permissions (popup is closed)
     * {@code onActivityResult()} is fired. You must call this in that {@code onActivityResult()} method. If this method is not called,
     * access token would not be fetched and nothing will happen.
     *
     * @param resultCode  The activity result code.
     * @param data        Activity result data
     * @param redirectUrl Your redirectUrl which is configured at the time of creating Oauth2 credentials.
     * @param callback    A callback which will be fired when the sign-in flow is completed.
     */
    public void onActivityResult(int resultCode, Intent data, @NonNull String redirectUrl, @NonNull LoginFlowCallback callback) {
        if (resultCode == Activity.RESULT_OK && pref != null) {
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String code = task.getResult().getServerAuthCode();
                    ANResponse<JSONObject> response = AndroidNetworking.post("https://oauth2.googleapis.com/token")
                            .addBodyParameter("code", code)
                            .addBodyParameter("client_id", CLIENT_ID)
                            .addBodyParameter("client_secret", CLIENT_SECRET)
                            .addBodyParameter("grant_type", "authorization_code")
                            .addBodyParameter("redirect_uri", redirectUrl).build().executeForJSONObject();

                    if (response.isSuccess()) {
                        String token = response.getResult().optString("refresh_token");
                        pref.edit().putString("refresh_token", token).apply();
                        callback.onSuccess(response.getResult().optString("access_token"), token, code);
                    } else {
                        callback.onFailed(new Exception(response.getError().getErrorBody()));
                    }
                } else
                    callback.onFailed(task.getException());
            });
        } else {
            try {
                if (InetAddress.getByName("google.com").isReachable(10000))
                    callback.onFailed(new CancellationException("Sign-in flow cancelled or permission not granted"));
                else
                    callback.onFailed(new ConnectException("Can't connect to google servers. Is your internet on ?"));
            } catch (IOException e) {
                callback.onFailed(new ConnectException("Can't connect to google servers. Is your internet on ?"));
            }
        }
    }


    /**
     * Callback interface for sign-in flow.
     */
    public interface LoginFlowCallback {
        void onSuccess(@NonNull String accessToken, @NonNull String refreshToken, @NonNull String authorizationCode);

        void onFailed(@NonNull Exception e);
    }
}
