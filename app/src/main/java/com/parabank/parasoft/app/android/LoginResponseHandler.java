package com.parabank.parasoft.app.android;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parabank.parasoft.app.android.adts.Customer;
import com.parabank.parasoft.app.android.adts.User;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

class LoginResponseHandler extends JsonHttpResponseHandler {
    private final LoginActivity context;
    private final String username;
    private final String password;
    private final ProgressDialog loadingDialog;

    public LoginResponseHandler(LoginActivity context, String username, String password, ProgressDialog loadingDialog) {
        this.context = context;
        this.username = username;
        this.password = password;
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
        if (!loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.dismiss();

        try {
            JSONObject obj = jsonObject.getJSONObject("customer");
            context.login(new User(username, password, new Customer(obj)));
        } catch (JSONException e) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
            errorDialog.setTitle(R.string.dialog_login_error_title);
            errorDialog.setMessage(e.getMessage());
            errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //...
                }
            });
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        onFailure(statusCode, headers, throwable.getMessage(), throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (!loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.dismiss();

        AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
        errorDialog.setTitle(R.string.dialog_login_error_title);
        if (statusCode == 0) {
            errorDialog.setMessage(responseString.isEmpty() ? throwable.getMessage() : responseString);
        } else {
            errorDialog.setMessage(String.format("%d - %s", statusCode, responseString.isEmpty() ? throwable.getMessage() : responseString));
        }

        errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //...
            }
        });

        errorDialog.show();
    }
}
