package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parabank.parasoft.app.android.adts.Address;
import com.parabank.parasoft.app.android.adts.Customer;
import com.parabank.parasoft.app.android.adts.User;

import org.apache.http.Header;
import org.json.JSONObject;

import static com.parabank.parasoft.app.android.Constants.INTENT_PARABANK_URI;
import static com.parabank.parasoft.app.android.Constants.INTENT_USER;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_HOST;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_PORT;

public class EditAccountInfoActivity extends Activity implements View.OnClickListener {
    private User originalUser;

    private EditText etUsername;
    private EditText etPassword;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNumber;
    private EditText etSSN;

    private EditText etStreet;
    private EditText etCity;
    private EditText etState;
    private EditText etZipCode;

    private ImageButton btnRejectChanges;
    private ImageButton btnAcceptChanges;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_info_activity_layout);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);

        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        etSSN = (EditText)findViewById(R.id.etSSN);

        etStreet = (EditText)findViewById(R.id.etStreet);
        etCity = (EditText)findViewById(R.id.etCity);
        etState = (EditText)findViewById(R.id.etState);
        etZipCode = (EditText)findViewById(R.id.etZipCode);

        btnRejectChanges = (ImageButton)findViewById(R.id.btnRejectChanges);
        btnRejectChanges.setOnClickListener(this);
        btnRejectChanges.setVisibility(View.VISIBLE);

        btnAcceptChanges = (ImageButton)findViewById(R.id.btnAcceptChanges);
        btnAcceptChanges.setOnClickListener(this);
        btnAcceptChanges.setVisibility(View.VISIBLE);

        originalUser = getIntent().getParcelableExtra(INTENT_USER);

        etUsername.setText(originalUser.getUsername());
        //etPassword.setText(originalUser.getPassword());

        etFirstName.setText(originalUser.getCustomer().getFirstName());
        etLastName.setText(originalUser.getCustomer().getLastName());
        etPhoneNumber.setText(originalUser.getCustomer().getPhoneNumber());
        etSSN.setText(originalUser.getCustomer().getSsn());

        etStreet.setText(originalUser.getCustomer().getAddress().getStreet());
        etCity.setText(originalUser.getCustomer().getAddress().getCity());
        etState.setText(originalUser.getCustomer().getAddress().getState());
        etZipCode.setText(originalUser.getCustomer().getAddress().getZipCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etLastName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etPhoneNumber.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etSSN.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etStreet.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etState.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etZipCode.getWindowToken(), 0);

        switch(v.getId()) {
            case R.id.btnRejectChanges:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnAcceptChanges:
                User newUser = new User(
                    etUsername.getText().toString(),
                    etPassword.getText().toString().isEmpty() ? originalUser.getPassword() : etPassword.getText().toString(),
                    new Customer(
                        originalUser.getCustomer(),
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        new Address(
                            originalUser.getCustomer().getAddress(),
                            etStreet.getText().toString(),
                            etCity.getText().toString(),
                            etState.getText().toString(),
                            etZipCode.getText().toString()
                        ),
                        etPhoneNumber.getText().toString(),
                        etSSN.getText().toString()
                    )
                );

                updateAccountInfo(newUser);
                break;
        }
    }

    private void updateAccountInfo(final User newUser) {
        final ProgressDialog loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getString(R.string.updating_contact_info));
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        Uri updateAccountInfoUri = ((Uri)getIntent().getParcelableExtra(INTENT_PARABANK_URI)).buildUpon()
                .appendPath("customers")
                .appendPath("update")
                .appendPath(Long.toString(newUser.getCustomer().getId()))
                .appendPath(newUser.getCustomer().getFirstName())
                .appendPath(newUser.getCustomer().getLastName())
                .appendPath(newUser.getCustomer().getAddress().getStreet())
                .appendPath(newUser.getCustomer().getAddress().getCity())
                .appendPath(newUser.getCustomer().getAddress().getState())
                .appendPath(newUser.getCustomer().getAddress().getZipCode())
                .appendPath(newUser.getCustomer().getPhoneNumber())
                .appendPath(newUser.getCustomer().getSsn())
                .appendPath(newUser.getUsername())
                .appendPath(newUser.getPassword())
                .build();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(updateAccountInfoUri.toString(), new UpdateCustomerInfoResponseHandler(this, newUser, loadingDialog));
    }
}
