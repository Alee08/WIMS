package com.example.alessandro.WIMSapp;

import com.example.alessandro.WIMSapp.Model.GarbageCollector;
import com.example.alessandro.WIMSapp.Model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;

public class SharingValues {

    private static String FullName="";
    private static boolean logout=false;
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static LinkedList<GarbageCollector> CollectorList= new LinkedList<>();
    private static ArrayList<Report> ReportList= new ArrayList<>();



    public static ArrayList<Report> getReportList() {
        return ReportList;
    }

    public static void setReportList(ArrayList<Report> reportList) {
        ReportList = reportList;
    }


    public static void setFullName(String name){

        FullName=name;
    }
    public static String getName(){
        return FullName;
    }
    public static void setCurrentUser(FirebaseUser u){

        user=u;
    }


    public static FirebaseAuth getCurrentUserAuth(){

        return auth;
    }


public static void setLogOut(Boolean b){

        logout=b;
}

public static boolean getLogOut(){

        return logout;
    }
    public static void setCurrentUserAuth(FirebaseAuth u){

        auth=u;
    }


    public static FirebaseUser getCurrentUser(){

        return user;
    }
public static LinkedList<GarbageCollector> getGarbageCollectors(){


        return CollectorList;
}
    public static void setGarbageCollectors(LinkedList<GarbageCollector> l){
    CollectorList=l;
    }

}
