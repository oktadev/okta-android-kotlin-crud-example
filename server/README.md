# Okta Kotlin Spring Boot

This is the server part of the Okta Kotlin CRUD Android example tutorial

## Setup

You need to enter in your Web Application details into `src/main/resource/application.properties`.
It should look something like this:

```
okta.oauth2.issuer=https://dev-628819.oktapreview.com/oauth2/default
okta.oauth2.clientId="CLIENT_ID"
okta.oauth2.clientSecret="CLIENT_SECRET"
```

Replace `CLIENT_ID` and `CLIENT_SECRET` with your applications values.

## Running

Simply run

```
gradlew bootRun
```

Or if you're on Linux/Mac:

```
./gradlew bootRun
```
