# Okta Kotlin Android CRUD app

This is the Android Studio source for the client side of the Okta Kotlin Android CRUD tutorial

## Setup

You need to fill out `app/src/main/res/raw/okta_app_auth_config.json` with your Okta Application details.
It should look like this:

```
{
  "client_id": "{CLIENT_ID}",
  "redirect_uri": "com.oktapreview.dev-628819:/callback",
  "scopes": [
    "openid",
    "profile",
    "offline_access"
  ],
  "issuer_uri": "https://dev-628819.oktapreview.com/oauth2/default"
}
```

Note you also need to have the *server* setup and running locally for this app to work.

## Running

You should be able to run this on an emulator just by pressing Play in Android Studio.