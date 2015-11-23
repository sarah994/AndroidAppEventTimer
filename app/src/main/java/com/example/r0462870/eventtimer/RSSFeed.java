package com.example.r0462870.eventtimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by r0462870 on 9/11/2015.
 */
public class RSSFeed {
    private String name = null;
    private String eventTime = null;
    private ArrayList<RSSItem> items;

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public RSSFeed() {
        items = new ArrayList<RSSItem>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTimeMillis() {
        return eventTime;
    }

    /*public long getEventTimeMillis() {
        try {
            Date date = dateInFormat.parse(eventTime.trim());
            return date.getTime();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }*/

    public int addItem(RSSItem item) {
        items.add(item);
        return items.size();
    }

    public RSSItem getItem(int index) {
        return items.get(index);
    }

    public ArrayList<RSSItem> getAllItems() {
        return items;
    }
}
