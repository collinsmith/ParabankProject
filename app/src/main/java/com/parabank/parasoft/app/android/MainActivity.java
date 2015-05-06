package com.parabank.parasoft.app.android;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parabank.parasoft.app.android.adts.Account;
import com.parabank.parasoft.app.android.adts.User;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.parabank.parasoft.app.android.Constants.INTENT_PARABANK_URI;
import static com.parabank.parasoft.app.android.Constants.INTENT_USER;

public class MainActivity extends Activity implements View.OnClickListener {
    static final int RESULT_EDIT_ACCOUNT_INFO = 0x000000001;

    private User user;
    private Uri parabankUri;
    private Uri accountsInfoUri;

    private TextView tvFullName;
    private TextView tvAddress;
    private LinearLayout llProgressBar;
    private ImageButton btnLogout;
    private ImageButton btnEditAccountInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        user = getIntent().getParcelableExtra(INTENT_USER);
        parabankUri = getIntent().getParcelableExtra(INTENT_PARABANK_URI);
        accountsInfoUri = parabankUri.buildUpon()
                .appendPath("customers")
                .appendPath(Long.toString(user.getCustomer().getId()))
                .appendPath("accounts")
                .appendQueryParameter("_type", "json")
                .build();

        tvFullName = (TextView)findViewById(R.id.tvFullName);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);

        btnLogout = (ImageButton)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnLogout.setVisibility(View.VISIBLE);

        btnEditAccountInfo = (ImageButton)findViewById(R.id.btnEditAccountInfo);
        btnEditAccountInfo.setOnClickListener(this);
        btnEditAccountInfo.setVisibility(View.VISIBLE);

        loadCustomerInfo();
        loadAccounts();
    }

    private void loadCustomerInfo() {
        tvFullName.setText(user.getCustomer().getFullName());
        tvAddress.setText(user.getCustomer().getAddress().getAddress());
    }

    private void loadAccounts() {
        llProgressBar.setVisibility(View.VISIBLE);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(accountsInfoUri.toString(), new GetAccountsResponseHandler(this, user, llProgressBar));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnLogout:
                finish();
                break;
            case R.id.btnEditAccountInfo:
                Intent i = new Intent(this, EditAccountInfoActivity.class);
                i.putExtra(INTENT_USER, user);
                i.putExtra(INTENT_PARABANK_URI, parabankUri);
                startActivityForResult(i, RESULT_EDIT_ACCOUNT_INFO);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_EDIT_ACCOUNT_INFO:
                switch (resultCode) {
                    case RESULT_CANCELED:
                        break;
                    case RESULT_OK:
                        user = data.getParcelableExtra(INTENT_USER);
                        loadCustomerInfo();
                        break;
                }

                break;
        }
    }
}
