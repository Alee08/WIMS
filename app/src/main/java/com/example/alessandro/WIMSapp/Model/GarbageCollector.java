package com.example.alessandro.WIMSapp.Model;

import java.util.LinkedList;

public class GarbageCollector {


    private  String Name;
    private double Latitude,Longitude;
    private double Empty;
    private double Soil;
    private double Temperature;
    private String Gyro;
    private double value=17;
    private boolean notificated=false,tempNot=false,tilt=false;
    public GarbageCollector(String name,long empty){
        Name=name;
        Empty=(double)empty;

    }
    public double getTemperature(){

        return Temperature;
    }
    public boolean getTilt(){
        return tilt;
    }
    public void setTilt(Boolean b){tilt=b;}

    public boolean getTempHot(){
        return tempNot;
    }
    public void setTempHot(Boolean b){tempNot=b;}

    public void setTemperature(double temp){
        Temperature=temp;
    }

    public String getGyro(){
        return Gyro;
    }
    public void setGyro(String gyro){

        Gyro=gyro;
    }
    public  String getName(){

        return Name;
    }

    public  double getEmptyValue(){

        return Empty;
    }
    public  double getSoil(){

        return Soil;
    }



    public void setPosition(double latitude,double longitude){

        Latitude=latitude;
        Longitude=longitude;
    }
    public  String getPosition(){

        return "Latitude: "+Latitude+", Longitude: "+Longitude;
    }

    public  double getLatitude(){return Latitude;}
    public  double getLongitude(){return Longitude;}

    public  void setLatitude(double lat){Latitude=lat;}
    public  void setLongitude(double longitude){Longitude=longitude;}
    public void setValue(double val){
        value=val;

    }
    public double getFullPercentage(){

        if(value!=0 && value<Empty){

            return (Empty-value)/Empty;
        }
        else return 0;
    }
    public double getValue(){

        return value;
    }

    public void setNotificated(Boolean bol){notificated=bol;}

    public boolean getNotificated(){return notificated;}


}
