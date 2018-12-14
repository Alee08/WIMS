package com.example.alessandro.WIMSapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.alessandro.WIMSapp.SharingValues.setCurrentUser;

public class MainActivitySigIn extends AppCompatActivity implements View.OnClickListener {

    private Button mButton;
    private ProgressBar progressBar;
    public FirebaseAuth mAuth;
    private static final String TAG = "MainActivitySigIn";
    private final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_sig_in);
        mAuth = FirebaseAuth.getInstance();

        mButton=(Button) findViewById(R.id.login_register);
        findViewById(R.id.login_register).setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SharingValues.setCurrentUser(currentUser);
            Intent intent = new Intent(MainActivitySigIn.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_register){
            Intent intent = new Intent(MainActivitySigIn.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
