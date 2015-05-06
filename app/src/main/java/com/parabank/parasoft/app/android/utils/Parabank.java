package com.parabank.parasoft.app.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.parabank.parasoft.app.android.R;

import java.net.URI;
import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_HOST;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_PORT;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_PROTOCOL;

public class Parabank {
    private Parabank() {
        //...
    }

    public static Uri getUpdatedUri(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_PARABANK, MODE_PRIVATE);
        String protocol = preferences.getString(PREFS_PARABANK_PROTOCOL, context.getResources().getString(R.string.example_protocol));
        String host = preferences.getString(PREFS_PARABANK_HOST, context.getResources().getString(R.string.example_host));
        String port = preferences.getString(PREFS_PARABANK_PORT, context.getResources().getString(R.string.example_port));

        return new Uri.Builder()
                .scheme(protocol)
                .encodedAuthority(host + ":" + port)
                .appendPath("parabank")
                .appendPath("services")
                .appendPath("bank")
                .build();
    }

    public static Uri updateUri(Context context, String protocol, String host, String port) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_PARABANK, MODE_PRIVATE);
        preferences.edit()
                .putString(PREFS_PARABANK_PROTOCOL, protocol)
                .putString(PREFS_PARABANK_HOST, host)
                .putString(PREFS_PARABANK_PORT, port)
                .apply();

        return getUpdatedUri(context);
    }
}
