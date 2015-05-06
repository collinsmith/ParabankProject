package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.parabank.parasoft.app.android.adts.Address;
import com.parabank.parasoft.app.android.adts.Customer;
import com.parabank.parasoft.app.android.adts.Setting;
import com.parabank.parasoft.app.android.adts.User;

import java.util.ArrayList;
import java.util.List;

import static com.parabank.parasoft.app.android.Constants.INTENT_PARABANK_URI;
import static com.parabank.parasoft.app.android.Constants.INTENT_USER;

public class EditCustomerInfoActivity extends Activity implements View.OnClickListener {
    private User originalUser;
    private List<Setting> settings;
    private SettingsAdapter settingsAdapter;

    private ImageButton btnRejectChanges;
    private ImageButton btnAcceptChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_info_activity_layout);

        originalUser = getIntent().getParcelableExtra(INTENT_USER);

        settings = new ArrayList<>(10);
        settings.add(new Setting("username", R.string.username, 0, InputType.TYPE_TEXT_VARIATION_NORMAL, originalUser.getUsername()));
        settings.add(new Setting("password", R.string.password, R.string.unchanged, InputType.TYPE_TEXT_VARIATION_NORMAL, ""));

        settings.add(new Setting("firstName", R.string.firstName, 0, InputType.TYPE_TEXT_VARIATION_PERSON_NAME, originalUser.getCustomer().getFirstName()));
        settings.add(new Setting("lastName", R.string.lastName, 0, InputType.TYPE_TEXT_VARIATION_PERSON_NAME, originalUser.getCustomer().getLastName()));
        settings.add(new Setting("phoneNumber", R.string.phoneNumber, 0, InputType.TYPE_CLASS_PHONE, originalUser.getCustomer().getPhoneNumber()));
        settings.add(new Setting("ssn", R.string.ssn, 0, InputType.TYPE_CLASS_NUMBER, originalUser.getCustomer().getSsn()));

        settings.add(new Setting("street", R.string.street, 0, InputType.TYPE_TEXT_VARIATION_NORMAL, originalUser.getCustomer().getAddress().getStreet()));
        settings.add(new Setting("city", R.string.city, 0, InputType.TYPE_TEXT_VARIATION_NORMAL, originalUser.getCustomer().getAddress().getCity()));
        settings.add(new Setting("state", R.string.state, 0, InputType.TYPE_TEXT_VARIATION_NORMAL, originalUser.getCustomer().getAddress().getState()));
        settings.add(new Setting("zipCode", R.string.zipCode, 0, InputType.TYPE_CLASS_NUMBER, originalUser.getCustomer().getAddress().getZipCode()));

        settingsAdapter = new SettingsAdapter(this, originalUser, settings);
        ListView lvSettings = (ListView)findViewById(R.id.lvSettings);
        lvSettings.setAdapter(settingsAdapter);

        btnRejectChanges = (ImageButton)findViewById(R.id.btnRejectChanges);
        btnRejectChanges.setOnClickListener(this);
        btnRejectChanges.setVisibility(View.VISIBLE);

        btnAcceptChanges = (ImageButton)findViewById(R.id.btnAcceptChanges);
        btnAcceptChanges.setOnClickListener(this);
        btnAcceptChanges.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnRejectChanges:
                settingsAdapter.hideInputs();
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnAcceptChanges:
                settingsAdapter.hideInputs();
                User newUser = new User(
                        settingsAdapter.getValue("username"),
                        settingsAdapter.getValue("password").isEmpty() ? originalUser.getPassword() : settingsAdapter.getValue("password"),
                        new Customer(
                                originalUser.getCustomer(),
                                settingsAdapter.getValue("firstName"),
                                settingsAdapter.getValue("lastName"),
                                new Address(
                                        originalUser.getCustomer().getAddress(),
                                        settingsAdapter.getValue("street"),
                                        settingsAdapter.getValue("city"),
                                        settingsAdapter.getValue("state"),
                                        settingsAdapter.getValue("zipCode")
                                ),
                                settingsAdapter.getValue("phoneNumber"),
                                settingsAdapter.getValue("ssn")
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
