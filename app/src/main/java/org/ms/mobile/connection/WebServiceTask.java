package org.ms.mobile.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;

import org.ms.mobile.MSM;
import org.ms.mobile.sp.SPConstants;
import org.ms.mobile.ui.util.ProgressDialogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Hubert on 2017-04-03.
 */

public class WebServiceTask extends AsyncTask<String, Void, Boolean> {

    private String uri;
    private String result;
    private Context context;
    private OnResult action;
    private SharedPreferences sp;
    private boolean showProgressDialog = false;

    public WebServiceTask(String uri, Context context, OnResult action) {
        this.context = context;
        this.action = action;
        this.uri = uri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sp = PreferenceManager.getDefaultSharedPreferences(MSM.getAppContext());
        if (showProgressDialog)
            ProgressDialogUtil.show(context);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(ConnectionConstants.HOST + uri);
            URLConnection connection = url.openConnection();
            urlConnection = (HttpURLConnection) connection;
            urlConnection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + getCredBase64());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(true);
            if (params != null && params.length > 0) {
                connection.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                out.write(params[0]);
                out.flush();
                out.close();
            }
            connection.connect();
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
            urlConnection.disconnect();
            result = builder.toString();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
            return false;
        }
    }

    private String getCredBase64() {
        String userLogin = sp.getString(SPConstants.USER_LOGIN, null);
        String userPassword = sp.getString(SPConstants.USER_PASSWORD, null);
        if (userLogin == null || userPassword == null) {
            userLogin = ConnectionConstants.NO_USER_LOGIN;
            userPassword = ConnectionConstants.NO_USER_PASSWORD_HASH;
        }
        String credentials = userLogin + ":" + userPassword;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP).replace("\n", "");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (showProgressDialog)
            ProgressDialogUtil.dismiss();
        if (aBoolean)
            action.onSuccess(result);
        else
            action.onFailure(result);
    }

    public WebServiceTask showProgressDialog() {
        showProgressDialog = true;
        return this;
    }
}
