package com.parabank.parasoft.app.android;

import android.net.Uri;

/**
 * Interface which defines constant variables used throughout the app.
 *
 * @author csmith@parasoft.com
 */
public interface Constants {
    /**
     * String representation for the name of the preferences object storing
     * the Parabank connection settings.
     */
    String PREFS_PARABANK = "parabankConnectionSettings";

    /**
     * String representation for the key within the Parabank preferences
     * object which stores the protocol for the HTTP requests.
     */
    @Deprecated
    String PREFS_PARABANK_PROTOCOL = "protocol";

    /**
     * String representation for the key within the Parabank preferences
     * object which stores the host for the HTTP requests.
     */
    String PREFS_PARABANK_HOST = "host";

    /**
     * String representation for the key within the Parabank preferences
     * object which stores the port for the HTTP requests.
     */
    String PREFS_PARABANK_PORT = "port";

    /**
     * String representation for key within the {@link android.content.Intent}
     * object which stores the {@link com.parabank.parasoft.app.android.adts.User}
     * object data.
     */
    String INTENT_USER = "user";
    String INTENT_PARABANK_URI = "parabankConnection";
}
