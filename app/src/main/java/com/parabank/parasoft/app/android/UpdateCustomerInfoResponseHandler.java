package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parabank.parasoft.app.android.adts.User;

import org.apache.http.Header;
import org.json.JSONObject;

import static com.parabank.parasoft.app.android.Constants.INTENT_USER;

class UpdateCustomerInfoResponseHandler extends JsonHttpResponseHandler {
    private final Activity parent;
    private final User newUser;
    private final ProgressDialog loadingDialog;

    public UpdateCustomerInfoResponseHandler(Activity parent, User newUser, ProgressDialog loadingDialog) {
        this.parent = parent;
        this.newUser = newUser;
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
        if (!loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.dismiss();

        Intent i = new Intent();
        i.putExtra(INTENT_USER, newUser);
        parent.setResult(Activity.RESULT_OK, i);
        parent.finish();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        onFailure(statusCode, headers, throwable.getMessage(), throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (statusCode == 200) {
            onSuccess(statusCode, headers, responseString);
            return;
        }

        if (!loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.dismiss();

        AlertDialog.Builder errorDialog = new AlertDialog.Builder(parent);
        errorDialog.setTitle(R.string.dialog_update_account_info_failed_title);
        errorDialog.setMessage(String.format("%d - %s", statusCode, responseString.isEmpty() ? throwable.getMessage() : responseString));
        errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //...
            }
        });

        errorDialog.show();
    }
}
