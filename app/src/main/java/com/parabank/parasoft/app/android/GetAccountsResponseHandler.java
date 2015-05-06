package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parabank.parasoft.app.android.adts.Account;
import com.parabank.parasoft.app.android.adts.User;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetAccountsResponseHandler extends JsonHttpResponseHandler {
    private final Activity parent;
    private final User user;
    private final View progressBar;

    public GetAccountsResponseHandler(Activity parent, User user, View progressBar) {
        this.parent = parent;
        this.user = user;
        this.progressBar = progressBar;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        List<Account> accounts = new ArrayList<>();

        try {
            JSONObject obj;
            JSONArray jsonArray = response.getJSONArray("account");
            for (int i = 0; i < jsonArray.length(); i++) {
                obj = jsonArray.getJSONObject(i);
                accounts.add(new Account(obj));
            }
        } catch (JSONException e) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(parent);
            errorDialog.setMessage(e.getMessage());
            errorDialog.setPositiveButton(R.string.global_action_okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //...
                }
            });
        }

        AccountsAdapter adapter = new AccountsAdapter(parent, user, accounts);
        ListView lvAccountsList = (ListView)parent.findViewById(R.id.lvQueryResults);
        if (adapter.isEmpty()) {
            LinearLayout llEmptyList = (LinearLayout)parent.findViewById(R.id.llEmptyList);
            llEmptyList.setVisibility(View.VISIBLE);
        } else {
            lvAccountsList.setAdapter(adapter);
        }

        progressBar.setVisibility(View.GONE);
    }
}
