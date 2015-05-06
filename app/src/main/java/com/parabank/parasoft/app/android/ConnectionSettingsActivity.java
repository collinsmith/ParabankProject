package com.parabank.parasoft.app.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parabank.parasoft.app.android.utils.Parabank;

import static com.parabank.parasoft.app.android.Constants.INTENT_PARABANK_URI;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_HOST;
import static com.parabank.parasoft.app.android.Constants.PREFS_PARABANK_PORT;

public class ConnectionSettingsActivity extends Activity implements View.OnClickListener {
    private EditText etHost;
    private EditText etPort;
    private ImageButton btnAcceptChanges;
    private ImageButton btnRejectChanges;


    private Uri parabankUri;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_settings_activity_layout);

        parabankUri = getIntent().getParcelableExtra(INTENT_PARABANK_URI);

        etHost = (EditText)findViewById(R.id.etHost);
        etHost.setText(parabankUri.getHost());
        etHost.selectAll();

        etPort = (EditText)findViewById(R.id.etPort);
        etPort.setText(Integer.toString(parabankUri.getPort()));

        btnAcceptChanges = (ImageButton)findViewById(R.id.btnAcceptChanges);
        btnAcceptChanges.setOnClickListener(this);
        btnAcceptChanges.setVisibility(View.VISIBLE);

        btnRejectChanges = (ImageButton)findViewById(R.id.btnRejectChanges);
        btnRejectChanges.setOnClickListener(this);
        btnRejectChanges.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etHost.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etPort.getWindowToken(), 0);
        switch(v.getId()) {
            case R.id.btnAcceptChanges:
                parabankUri = Parabank.updateUri(this, "http", etHost.getText().toString(), etPort.getText().toString());

                Intent i = new Intent();
                i.putExtra(INTENT_PARABANK_URI, parabankUri);
                setResult(Activity.RESULT_OK, i);
                finish();
                break;
            case R.id.btnRejectChanges:
                finish();
                break;
        }
    }
}
