# Android RxGoogleAuthentication
[![Build Status](https://travis-ci.org/TurhanOz/RxGoogleAuthentication.svg?branch=master)](https://travis-ci.org/TurhanOz/RxGoogleAuthentication)
[![Maven Central](https://img.shields.io/badge/maven--central-0.0.1-blue.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22rxgoogleauthentication%22)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxGoogleAuthentication-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/2746)
[![Stories in Ready](https://badge.waffle.io/TurhanOz/RxGoogleAuthentication.png?label=ready&title=Ready)](https://waffle.io/TurhanOz/RxGoogleAuthentication)

A simple android library that lets you easily get an authentication token for the Google Apis.

This library has been developed using RxJava. It also integrates relevant unit tests and a sample application.

### Motivation
If you are an android developer, chances are that you will, one day or another, use one of the Google Rest Apis.
Most of the Google Rest Apis require authentication. So you'll start reading the [documentation](https://developers.google.com/android/guides/http-auth) in order to understand how to get authenticated.
The more I looked to that documentation, the more I was sceptical about the sample code provided in it. Indeed, the token fetched is done through a [AsyncTask](http://jdam.cd/async-android/). As a matter of fact, notifying the Ui is done is done though runOnUiThread() in case of Exception...
Lot's of 'stuff' I dislike. And no code quality given with the snipets...

So I decided to create this library, using RxJava and providing clean Unit Tests.

## Usage

### From Maven Central

Library releases are available on Maven Central; you can add dependencies as follow :

**Gradle**

```groovy
compile 'com.turhanoz.android.rxgoogleauthentication:0.0.1@aar'
```
**Maven**

```xml
<dependency>
  <groupId>com.turhanoz.android</groupId>
  <artifactId>rxgoogleauthentication</artifactId>
  <version>0.0.1</version>
  <type>aar</type>
</dependency>
```

### Supported Android SDK

You can use this library for apps starting from android 2.3.3 (gingerbread /API 10) to android 6 (marshmallow / API 23)

```
minSdkVersion 10
targetSdkVersion 23
```

### Usage

```java
//trigger a token request by using this builder:
private void fetchToken(){
    new AuthSubscription()
        .setEmail("email")
        .setScope("scope")
        .setActivity(getActivity())
        .setCallback(this)
        .buildAndSubscribe();
}

//get token through this callback
public interface AuthCallback {
    public void onTokenReceived(AuthToken token);
    public void onError(Throwable e);
}

//relevant exceptions are handled silently by the library
//such as GooglePlayServicesAvailabilityException and UserRecoverableAuthException
//in case of another kind of exceptions, you can handle it on the OnActivityResult callback
//in your fragment or activity
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if ((requestCode == AuthObserver.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
        && resultCode == getActivity().RESULT_OK) {
            // Receiving a result that follows a GoogleAuthException, try auth again
            fetchToken();
        } else if (resultCode == getActivity().RESULT_CANCELED) {
            //notify ui
        }
    }
```

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
