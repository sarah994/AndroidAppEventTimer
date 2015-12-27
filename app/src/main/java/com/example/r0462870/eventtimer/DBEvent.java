package com.example.r0462870.eventtimer;

/**
 * Created by Sarah on 26/12/2015.
 */
public class DBEvent {
    private int eventID;
    private int listID;
    private String theme;
    private String name;

    public static final String LIGHT = "light";
    public static final String DARK = "dark";

    public DBEvent(){
        theme = LIGHT;
    }

    public DBEvent(int listID, String name){
        this.listID = listID;
        this.name = name;
    }

    public DBEvent(int eventID, int listID, String name, String theme){
        this.eventID = eventID;
        this.listID = listID;
        this.theme = theme;
        this.name = name;
    }

    public void setId(int id){
        eventID = id;
    }

    public int getId(){
        return eventID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getname(){ return name; }

    public void setListId(int id){
        listID = id;
    }

    public int getListId(){
        return listID;
    }

    public void setTheme(String theme){
        this.theme = theme;
    }

    public String getTheme(){
        return theme;
    }
}
