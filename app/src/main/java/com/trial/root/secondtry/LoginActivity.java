package com.trial.root.secondtry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.BoolRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.trial.root.secondtry.outbound.AuthTask;

public class LoginActivity extends AppCompatActivity {


    private EditText mPasswordView;
    private EditText mUsernameView;
    private Button mLoginButtonView;

    private AuthTask authTask;

    private ProgressBar progressBar;

    private String loginPreferences = "LOGIN_PREFERENCES";

    private static final int PERMISSION_REQUEST_CODE = 1;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView = (EditText) findViewById(R.id.username);
        mLoginButtonView = (Button) findViewById(R.id.loginButton);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences(loginPreferences, Context.MODE_PRIVATE);

        String session_key = sharedPreferences.getString("session_key","");
        if(!session_key.isEmpty()){
            //go to next act
            Log.e("TEST",session_key);
            Intent accountIntent = new Intent(this, AccountActivity.class);
            startActivity(accountIntent);
        }
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        if (result != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.INTERNET},PERMISSION_REQUEST_CODE);
        }

        mLoginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                authTask = new AuthTask(mUsernameView.getText().toString(),mPasswordView.getText().toString());
                authTask.execute(getApplicationContext());
                try{
                    if(authTask.get()){
                        progressBar.setVisibility(View.GONE);
                        Intent accountIntent = new Intent(getApplicationContext(), AccountActivity.class);
                        startActivity(accountIntent);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}
