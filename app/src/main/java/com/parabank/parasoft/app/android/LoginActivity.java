package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.parabank.parasoft.app.android.adts.User;
import com.parabank.parasoft.app.android.utils.Parabank;

import static com.parabank.parasoft.app.android.Constants.INTENT_PARABANK_URI;
import static com.parabank.parasoft.app.android.Constants.INTENT_USER;

public class LoginActivity extends Activity implements View.OnClickListener {
    static final int RESULT_EDIT_CONNECTION_SETTINGS = 0x000000001;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ImageButton btnConnectionSettings;

    private Uri parabankUri;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        parabankUri = Parabank.getUpdatedUri(this);

        etUsername = (EditText)findViewById(R.id.etUsername);

        etPassword = (EditText)findViewById(R.id.etPassword);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnConnectionSettings = (ImageButton)findViewById(R.id.btnConnectionSettings);
        btnConnectionSettings.setOnClickListener(this);
        btnConnectionSettings.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        String username = etUsername.getText().toString();
        switch(v.getId()) {
            case R.id.btnLogin:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                String password = etPassword.getText().toString();
                attemptLogin(username, password);
                break;
            case R.id.btnConnectionSettings:
                editConnectionSettings();
                break;
        }
    }

    private void attemptLogin(final String username, final String password) {
        final ProgressDialog loadingDialog = new ProgressDialog(this);
        //loadingDialog.setTitle(getString(R.string.dialog_login_load_title));
        loadingDialog.setMessage(getString(R.string.dialog_login_load));
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(true);
        loadingDialog.show();

        Uri loginUri = parabankUri.buildUpon()
                .appendPath("login")
                .appendPath(username)
                .appendPath(password)
                .appendQueryParameter("_type", "json")
                .build();

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(loginUri.toString(), new LoginResponseHandler(this, username, password, loadingDialog));
    }

    private void editConnectionSettings() {
        Intent i = new Intent(this, ConnectionSettingsActivity.class);
        i.putExtra(INTENT_PARABANK_URI, parabankUri);
        startActivityForResult(i, RESULT_EDIT_CONNECTION_SETTINGS);
    }

    void login(User user) {
        String welcomeMessage = getString(R.string.login_welcome_back, user.getCustomer().getFirstName());
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(INTENT_USER, user);
        i.putExtra(INTENT_PARABANK_URI, parabankUri);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_EDIT_CONNECTION_SETTINGS:
                if (resultCode != Activity.RESULT_OK) {
                    break;
                }

                parabankUri = data.getParcelableExtra(INTENT_PARABANK_URI);
                break;
            default:
                break;
        }
    }
}
