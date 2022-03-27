# Google API-Authentication library
[![Build Status](https://travis-ci.org/TurhanOz/RxGoogleAuthentication.svg?branch=master)](https://travis-ci.org/TurhanOz/RxGoogleAuthentication)
![Library version](https://img.shields.io/badge/library--version-2.0.0-blue.svg)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-EasyAuth-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2746)



> Fetching Google OAuth tokens was never easy

An android library through which you can easily fetch access tokens for Google APIs. Now assign the work of authentication to this library and concentrate on your main work.

![image](https://user-images.githubusercontent.com/65817230/158441396-489128d3-2499-4242-9581-4637637639cf.png)

## Features
- Easily get access and refresh tokens.
- Auto refreshing access token
- Auto validation, expiration, and fetching of tokens
- Smooth google sign-in flow with callbacks


## Implementation
Add it in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
in your module (app) build.gradle:
```groovy
	dependencies {
	      implementation 'com.github.ErrorxCode:RxGoogleAuthentication: 2.0.0'
	}
```

## Acknowledgement
- [Google oauth2](https://developers.google.com/identity/protocols/oauth2)
- [Mobile authentication](https://developers.google.com/identity/protocols/oauth2/native-app)
- [Google sing-in](https://developers.google.com/identity/sign-in/android/start-integrating)


## Usage
**First of all**, initialize the library passing your cloud credentials.
```java
EasyAuth easyAuth = new EasyAuth(CLIENT_ID,CLIENT_SECRET);
```
**Note:** Both of these must be of *Web application* credentials.
**Note:** All the methods run synchronously, put this in the background thread if needed.



### Automatic way
To automatically get access token, use `autoRefreshingAccessToken()` method.
```java
String accessToken = easyAuth.autoRefreshingAccessToken(this, API_SCOPE);
```
You also need to overwride `onActivityResult` and call `easyAuth.onActivityResult()` method in that.
```java

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyAuth.onActivityResult(resultCode, data, "https://your.redirect.url", new EasyAuth.LoginFlowCallback() {
            @Override
            public void onSuccess(@NonNull String accessToken, @NonNull String refreshToken, @NonNull String authorizationCode) {
                // Get your access token from here.
            }

            @Override
            public void onFailed(@NonNull Exception e) {
                // Check the exception
            }
        });
    }
```
For the first time, it will return null and starts the sign-in flow. You will receive your access token as a result of that sign-in flow.


### Manual way
If you already have an authorization code or refresh token, you can also manually fetch an access tokens from them.
```java
try {
    String refreshToken = easyAuth.getRefreshCode("AUTHORIZATION_CODE");  // Save this for future.
    String accessToken = easyAuth.getAccessToken(refreshToken);
} catch (Exception e) {
    e.printStackTrace();
}
```
**Note:** AUTHORIZATION_CODE can only be used once. So in this way, you have to save `refreshToken` on the device to get **access_token** directly.

If you don't have an authorization code, you can get one by authentication user with a google account.
```java
easyAuth.startLoginFlow(this,"API_SCOPE");
```
You also need to overwride `onActivityResult` and call `easyAuth.onActivityResult()` method in that.
```java

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyAuth.onActivityResult(resultCode, data, "https://your.redirect.url", new EasyAuth.LoginFlowCallback() {
            @Override
            public void onSuccess(@NonNull String accessToken, @NonNull String refreshToken, @NonNull String authorizationCode) {
                // Get your access token from here.
            }

            @Override
            public void onFailed(@NonNull Exception e) {
                // Check the exception
            }
        });
    }
```

### That's it
Yhe, Now it's easy to manage auth tokens. It's time to contribute now. You can add different scopes constant in this class.

License
-------

    Copyright 2015 Turhan OZ

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
