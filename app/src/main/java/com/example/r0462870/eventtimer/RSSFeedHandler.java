package com.example.r0462870.eventtimer;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * Created by r0462870 on 9/11/2015.
 */
public class RSSFeedHandler extends DefaultHandler{
    private RSSFeed feed;
    private RSSItem item;

    private boolean feedNameHasBeenRead = false;
    private boolean feedEventTimeHasBeenRead = false;

    private boolean isName = false;
    private boolean isEventTime = false;
    private boolean isWaypoint = false;
    private boolean isLocation = false;
    private boolean isPre = false;
    private boolean isPreLocation = false;
    private boolean isPreWaypoint = false;

    private String s ="";

    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException{
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void endDocument() throws SAXException{}

    @Override
    public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException {
        if (qName.equals("boss")) {
            item = new RSSItem();
        }else if (qName.equals("name")) {
            isName = true;
        }
        else if (qName.equals("utc")) {
            isEventTime = true;
        }
        else if (qName.equals("waypoint")) {
            isWaypoint = true;
        }
        else if (qName.equals("location")) {
            isLocation = true;
        }
        else if (qName.equals("pre")) {
            isPre = true;
        }
        else if (qName.equals("pre_location")) {
            isPreLocation = true;
        }
        else if (qName.equals("pre_waypoint")) {
            isPreWaypoint = true;
        }
    }

     @Override
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException{
         if (qName.equals("boss")) {
             feed.addItem(item);
             return;
         }
     }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
        if(s.equals("[")||s.equals("[&")){
            s = s + new String(ch, start, length);
        }
        else {
            s = new String(ch, start, length);
        }
        if (isName) {
            item.setName(s);
            isName = false;
        }
        else if (isEventTime) {
            item.setEventTime(s);
            isEventTime = false;
        }
        else if (isWaypoint) {
            if(!s.equals("[")&&!s.equals("[&")) {
                item.setWaypoint(s);
                isWaypoint = false;
            }
        }
        else if (isLocation) {
            item.setLocation(s);
            isLocation = false;
        }
        else if (isPre) {
            item.setPre(s);
            isPre = false;
        }
        else if (isPreLocation) {
            item.setPreLocation(s);
            isPreLocation = false;
        }
        else if (isPreWaypoint) {
            if(!s.equals("[")&&!s.equals("[&")) {
                item.setPreWaypoint(s);
                isPreWaypoint = false;
            }
        }
    }
}
