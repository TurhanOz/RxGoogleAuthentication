package com.turhanoz.android.rxgoogleauthentication;

public class AuthToken {
    String token;
    String scope;

    public AuthToken(String token, String scope) {
        this.token = token;
        this.scope = scope;
    }

    public String getToken() {
        return token;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthToken authToken = (AuthToken) o;

        if (token != null ? !token.equals(authToken.token) : authToken.token != null) return false;
        return !(scope != null ? !scope.equals(authToken.scope) : authToken.scope != null);

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "token='" + token + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
