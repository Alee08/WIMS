package com.example.alessandro.WIMSapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

import com.example.alessandro.WIMSapp.Model.GarbageCollector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class DB_Thread extends Application implements Runnable {
    LinkedList<GarbageCollector> list=SharingValues.getGarbageCollectors();
    private String position="";
    private NotificationChannel channel;
    private NotificationManager mNotificationManager ;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent pendingIntent;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private ValueEventListener val;
    public DB_Thread(NotificationChannel chan, NotificationManager man, NotificationCompat.Builder build, PendingIntent pending, ValueEventListener valEvent) {

        channel=chan;
        mNotificationManager=man;
        mBuilder=build;
        pendingIntent=pending;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        val=valEvent;
    }


    public void run(){

        while (!Thread.currentThread().isInterrupted()) {
            //System.out.println("in thread prima on data change");
            myRef.addValueEventListener(val);
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        }



    public void stop(){

        Thread.currentThread().interrupt();
    }


    public void createNotification(String title, String text,int Icon){


        mBuilder.setSmallIcon(Icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        mBuilder.setContentIntent(pendingIntent);


    }

}
