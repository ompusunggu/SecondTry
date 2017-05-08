package com.trial.root.secondtry.outbound;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.trial.root.secondtry.LoginActivity;
import com.trial.root.secondtry.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 08/05/2017.
 */
public class AuthTask extends AsyncTask<Context, Void, Boolean> {

    private final String mUsername;
    private final String mPassword;

    private ProgressBar progressBar;

    private final String loginPreferences = "LOGIN_PREFERENCES";

    public AuthTask(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Context... params) {
        // TODO: attempt authentication against a network service.
        Context context = params[0];
        SharedPreferences sharedPreferences = context.getSharedPreferences(loginPreferences, Context.MODE_PRIVATE);

        try {
            // Simulate network access.
            String URL = "http://assistant-api.16mb.com/api/login";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(URL);

            List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
            urlParameters.add(new BasicNameValuePair("username", mUsername));
            urlParameters.add(new BasicNameValuePair("password", mPassword));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            JSONObject resultData = temp1.getJSONObject("resultData");

            if(sharedPreferences.edit().putString("session_key",resultData.getString("session_key")).commit() == true){
                Log.e("GOOD","berhasil simpan ke sharedPreferences");
            }

        } catch (Exception e) {
            Log.e("LOGIN ERROR", e.getLocalizedMessage());
            return false;
        }

        return true;
    }
}
