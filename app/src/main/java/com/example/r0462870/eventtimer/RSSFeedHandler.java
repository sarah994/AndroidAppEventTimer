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
        if (qName.equals("item")) {
            item = new RSSItem();
        }else if (qName.equals("name")) {
            isName = true;
        }
        else if (qName.equals("eventTime")) {
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
        else if (qName.equals("preLocation")) {
            isPreLocation = true;
        }
        else if (qName.equals("preWaypoint")) {
            isPreWaypoint = true;
        }
    }

     @Override
     public void endElement(String namespaceURI, String localName, String qName) throws SAXException{
         if (qName.equals("item")) {
             feed.addItem(item);
             return;
         }
     }

    public void characters(char ch[], int start, int length) throws SAXException
    {
        String s = new String(ch, start, length);
        if (isName) {
            if (feedNameHasBeenRead == false) {
                feed.setName(s);
                feedNameHasBeenRead = true;
            }
            else {
                item.setName(s);
            }
            isName = false;
        }
        else if (isEventTime) {
            if (feedEventTimeHasBeenRead == false) {
                feed.setEventTime(s);
                feedEventTimeHasBeenRead = true;
            }
            else {
                item.setEventTime(s);
            }
            isEventTime = false;
        }
        else if (isWaypoint) {
            item.setWaypoint(s);
            isWaypoint = false;
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
            item.setPreWaypoint(s);
            isPreWaypoint = false;
        }
    }
}
