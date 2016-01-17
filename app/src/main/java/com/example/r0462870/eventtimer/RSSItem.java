package com.example.r0462870.eventtimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by r0462870 on 9/11/2015.
 */
public class RSSItem {
    private String name = null;
    private String nr = null;
    private String eventTime = null;
    private String waypoint = null;
    private String location = null;
    private String pre = null;
    private String preLocation = null;
    private String preWaypoint = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfTimeZone = new SimpleDateFormat("HH:mm z");

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    public String getNr() {return nr; }
    public void setNr(String nr) { this.nr = nr; }

    public String getEventTime() {
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = sdf.parse(eventTime);
            return sdfTimeZone.format(myDate);
        }
        catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getWaypoint() { return waypoint; }
    public void setWaypoint(String waypoint) { this.waypoint = waypoint; }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getPre() {
        return pre;
    }
    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getPreLocation() {
        return preLocation;
    }
    public void setPreLocation(String preLocation) {
        this.preLocation = preLocation;
    }

    public String getPreWaypoint() {
        return preWaypoint;
    }
    public void setPreWaypoint(String preWaypoint) { this.preWaypoint = preWaypoint; }
}
