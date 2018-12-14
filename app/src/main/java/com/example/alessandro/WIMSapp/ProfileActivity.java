package com.example.alessandro.WIMSapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView fullname;
    private TextView email;
    private EditText first,last;
    String last_name,first_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        first=(EditText) findViewById(R.id.First);
        last=(EditText) findViewById(R.id.Last);
        email=(TextView) findViewById(R.id.email_inserted);
        email.setText(SharingValues.getCurrentUser().getEmail());

        fullname=(TextView) findViewById(R.id.name_inserted);
        fullname.setText(SharingValues.getName());
        visibility();

    }

    private void visibility(){
        if(!fullname.getText().equals("")){
            findViewById(R.id.insertion_elements).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button){
            if(validateForm()){

                SharingValues.setFullName(first_name+" "+last_name);
                Intent i= new Intent(ProfileActivity.this,ProfileActivity.class);
                startActivity(i);
            }


        }
    }
    private boolean validateForm() {
        boolean valid = true;

        first_name = first.getText().toString();
        if (TextUtils.isEmpty(first_name)) {
            first.setError("Required.");
            valid = false;
        } else {
            first.setError(null);
        }

        last_name = last.getText().toString();
        if (TextUtils.isEmpty(last_name)) {
            last.setError("Required.");
            valid = false;
        } else {
            last.setError(null);
        }

        return valid;
    }
}
