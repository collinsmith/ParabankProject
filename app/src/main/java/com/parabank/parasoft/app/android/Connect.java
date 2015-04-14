package com.parabank.parasoft.app.android;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


/**
 * Created by bmcmullin on 4/13/15.
 */
public class Connect {

    //Was originally protected
    public String accountInfoUrl (String host, String port, String accountNum) {
        String url = "http://" + host + ":" + port + "/parabank/services/bank/customers/" +
                accountNum + "/accounts?_type=json";

        return url;
    }

    //Was originally protected
    public String loginUrl (String host, String port, String uname, String password) {
        String url = "http://" + host + ":" + port +"/parabank/services/bank/login/" + uname + "/"
                + password + "?_type=json";

        return url;
    }

    //Originally returned VOID and protected
    public String executeGet(String url)
    {

        //For the result
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                result = EntityUtils.toString(response.getEntity());
                
            }
            else
                result = "\n Please verify connection settings";
        } catch (ClientProtocolException e) {
            result = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            result = e.getMessage();
            e.printStackTrace();
        }

        return result;
    }
}
