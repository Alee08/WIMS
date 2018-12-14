package com.example.alessandro.WIMSapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alessandro.WIMSapp.Model.GarbageCollector;
import com.example.alessandro.WIMSapp.Model.Report;
import com.example.alessandro.WIMSapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser user;
    private TextView t;
    LinkedList<GarbageCollector> garbageList = SharingValues.getGarbageCollectors();
    ArrayList<Report> reportList = SharingValues.getReportList();
    private boolean created = false;
    private NotificationChannel channel;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private Intent intentNotification;
    private PendingIntent pi;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    Thread GARBAGE_THREAD;
    private String position = "";
    private boolean inserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        System.out.println("precreazione");
        construct();
        for (GarbageCollector c : garbageList) {

            System.out.println(c.getName() + ", " + c.getValue());
        }
        database = FirebaseDatabase.getInstance();
        if (!created) {
            createChannel("GARBAGE_CHANNEL", "CHANNEL FOR FULL GARBAGE COLLECTOR NOTIFICATION");
            createNotification();
        }

        ValueEventListener valEvent = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myRef = database.getReference("Rooms");
                Iterable<DataSnapshot> value = dataSnapshot.child("Rooms").getChildren();
                Iterator it = value.iterator();
                //System.out.println("pre iteratore");
                try {
                    while (it.hasNext()) {
                        DataSnapshot data = (DataSnapshot) it.next();
                        GarbageCollector garbage = new GarbageCollector(data.getKey(), (long) data.child("empty").getValue());
                        garbage.setLatitude((double) data.child("latitude").getValue());
                        garbage.setLongitude((double) data.child("longitude").getValue());
                        garbage.setValue((long) data.child("distance").getValue());

                        int i = 0;

                        for (GarbageCollector g : garbageList) {
                            if (g.getName().equals(garbage.getName())) {
                                inserted = true;
                                garbage = g;
                                if (g.getValue() != garbage.getValue()) {
                                    g.setValue(garbage.getValue());
                                }

                            }
                            //System.out.println(g.getName()+", "+g.getValue()+",  " +g.getFullPercentage()*100+"% full");
                        }
                        if (!inserted) {
                            garbageList.add(garbage);
                            inserted = false;
                        }


                        if (garbage.getValue() > garbage.getEmptyValue() / 3) {
                            garbage.setNotificated(false);
                        }

                        if (garbage.getValue() >= 0 && garbage.getValue() <= garbage.getEmptyValue() / 3) {
                            if (!garbage.getNotificated()) {
                                position = garbage.getPosition();
                                createNotification("Warning!", "the garbage collector " + garbage.getName() + " is full!", R.drawable.trash);
                                mNotificationManager.notify(0, mBuilder.build());
                                System.out.println(garbage.getNotificated());
                                garbage.setNotificated(true);

                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//REPORT prendo cordinate
                myRef = database.getReference("reports");
                Iterable<DataSnapshot> value2 = dataSnapshot.child("reports").getChildren();
                Iterator it2 = value2.iterator();
                try {
                    while (it2.hasNext()) {
                        DataSnapshot data = (DataSnapshot) it2.next();
                        Report report = new Report();
                        report.setKey(data.getKey());
                        report.setLatitude((Double) data.child("latitude").getValue());
                        report.setLongitude((Double) data.child("longitude").getValue());
                        report.setEmail((String) data.child("email").getValue());
                        report.setTextBox((String) data.child("textBox").getValue());
                        report.setUrgency((String) data.child("urgency").getValue());
                        inserted = false;
                        for (Report r : reportList)
                            if (r.getKey().equals(report.getKey()))
                                inserted = true;
                        if (!inserted) reportList.add(report);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//fine report
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        //TODO aggiorna in automatico il db aggiundendo dei report quando non deve
        GARBAGE_THREAD = new Thread(new DB_Thread(channel, mNotificationManager, mBuilder, pi, valEvent));
        GARBAGE_THREAD.start();
        System.out.println("thread partito");
        created = true;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i = new Intent(MenuActivity.this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(MenuActivity.this, MapsActivity.class);
            startActivity(i);
        } else if (id == R.id.info) {
            Intent i = new Intent(MenuActivity.this, AboutActivity.class);
            startActivity(i);
        } else if (id == R.id.out) {
            SharingValues.setLogOut(true);
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.report) {
            Intent i = new Intent(MenuActivity.this, ReportingActivity.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void createChannel(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        channel = new NotificationChannel("01", title, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(content);
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }


    public void createNotification() {


        System.out.println("ENTRATO NEL BUILDER");
        mBuilder = new NotificationCompat.Builder(this, channel.getId());
        intentNotification = new Intent(getApplicationContext(), MapsActivity.class);
        pi = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);


    }

    public void createNotification(String title, String text, int Icon) {


        mBuilder.setSmallIcon(Icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        mBuilder.setContentIntent(pi);


    }



public void construct(){

        GarbageCollector garbage2= new GarbageCollector("Room-A2",20);
        garbage2.setPosition(41.892773, 12.504529);
        garbage2.setValue(13.0);
        garbageList.add(garbage2);
        GarbageCollector garbage3= new GarbageCollector("Room-A3",20);
        garbage3.setPosition(41.895353, 12.500699);
        garbage3.setValue(9);
        garbageList.add(garbage3);
        GarbageCollector garbage4= new GarbageCollector("Room-A4",20);
        garbage4.setPosition(41.891448,12.499240);
        garbage4.setValue(18);
        garbageList.add(garbage4);
        GarbageCollector garbage5= new GarbageCollector("Room-A5",20);
        garbage5.setPosition(41.888836,12.494938);
        garbage5.setValue(2);
        garbageList.add(garbage5);
        GarbageCollector garbage6= new GarbageCollector("Room-A6",20);
        garbage6.setPosition(41.878971,12.503019);
        garbage6.setValue(8);
        garbageList.add(garbage6);
        GarbageCollector garbage7= new GarbageCollector("Room-A7",20);
        garbage7.setPosition(41.876638,12.507407);
        garbage7.setValue(3);
        garbageList.add(garbage7);
        GarbageCollector garbage8= new GarbageCollector("Room-B2",20);
        garbage8.setPosition(41.881040,12.509867);
        garbage8.setValue(5);
        garbageList.add(garbage8);
    }
}
