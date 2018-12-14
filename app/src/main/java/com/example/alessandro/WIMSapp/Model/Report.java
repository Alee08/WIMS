package com.example.alessandro.WIMSapp.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Report implements Serializable{
    private String key, TextBox;
    private String Urgency;
    private String Email;
    private double Latitude, Longitude;

    public Report(){

    }

    public String getTextBox() {
        return TextBox;
    }

    public void setTextBox(String textBox) {
        TextBox = textBox;
    }

    public String getUrgency() {
        return Urgency;
    }

    public void setUrgency(String urgency) {
        Urgency = urgency;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Report(String key, String textBox, String urgency, String email, double lat, double longitude) {
        this.key = key;
        TextBox = textBox;
        Urgency = urgency;
        Email = email;
        Latitude = lat;
        Longitude = longitude;

    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
