package com.example.r0462870.eventtimer;

/**
 * Created by Sarah on 26/12/2015.
 */
public class DBEvent {
    private int eventID;
    private int listID;
    private String name;
    private String nr;
    private String eventTime;
    private String waypoint;
    private String location;
    private String pre;
    private String preLocation;
    private String preWaypoint;

    public DBEvent(){
        name = "";
        nr = "";
        eventTime = "";
        waypoint = "";
        location = "";
        pre = "";
        preLocation = "";
        preWaypoint = "";
    }

    public DBEvent(int listID, String name, String nr, String eventTime, String waypoint, String location, String pre, String preLocation, String preWaypoint){
        this.listID = listID;
        this.name = name;
        this.nr = nr;
        this.eventTime = eventTime;
        this.waypoint = waypoint;
        this.location = location;
        this.pre = pre;
        this.preLocation = preLocation;
        this.preWaypoint = preWaypoint;
    }

    public DBEvent(int eventID, int listID, String name, String nr, String eventTime, String waypoint, String location, String pre, String preLocation, String preWaypoint){
        this.eventID = eventID;
        this.listID = listID;
        this.name = name;
        this.nr = nr;
        this.eventTime = eventTime;
        this.waypoint = waypoint;
        this.location = location;
        this.pre = pre;
        this.preLocation = preLocation;
        this.preWaypoint = preWaypoint;
    }

    public void setId(int id){ eventID = id; }
    public int getId(){ return eventID; }

    public void setName(String name){ this.name = name; }
    public String getName(){ return name; }

    public void setListId(int id){ listID = id; }
    public int getListId(){ return listID; }

    public void setNr(String nr) { this.nr = nr;}
    public String getNr(){ return nr;}

    public void setEventTime(String eventTime) { this.eventTime = eventTime;}
    public String getEventTime(){ return eventTime;}

    public void setWaypoint(String waypoint) { this.waypoint = waypoint;}
    public String getWaypoint(){ return waypoint;}

    public void setLocation(String location) { this.location = location;}
    public String getLocation(){ return location;}

    public void setPre(String pre) { this.pre = pre;}
    public String getPre(){ return pre;}

    public void setPreLocation(String preLocation) { this.preLocation = preLocation;}
    public String getPreLocation(){ return preLocation;}

    public void setPreWaypoint(String preWaypoint) { this.preWaypoint = preWaypoint;}
    public String getPreWaypoint(){ return preWaypoint;}
}
