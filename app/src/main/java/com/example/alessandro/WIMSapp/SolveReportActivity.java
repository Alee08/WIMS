package com.example.alessandro.WIMSapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alessandro.WIMSapp.Model.Report;
import com.example.alessandro.WIMSapp.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SolveReportActivity extends AppCompatActivity implements View.OnClickListener {
    Report report;
    TextView priorityView, messageView;
    Button button;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_report);
        //retrieve the reports and add them to the view
        report = (Report) getIntent().getSerializableExtra("report");
        priorityView = (TextView)findViewById(R.id.priority);
        messageView = (TextView)findViewById(R.id.message_view);
        button= (Button)findViewById(R.id.solved);
        button.setOnClickListener(this);
        priorityView.setText(report.getUrgency());
        messageView.setText(report.getTextBox());
        //Retrieve the database
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onClick(View view) {
        DatabaseReference ref = database.getReference("reports/").child(report.getKey());
        ref.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ArrayList<Report> list = SharingValues.getReportList();
                        for (Report r : list)
                            if (r.getKey().equals(report.getKey()))
                                list.remove(r);
                        Intent i = new Intent(SolveReportActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        System.out.println("Cancellato");
                    }
                });
    }
}
