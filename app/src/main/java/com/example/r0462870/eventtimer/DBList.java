package com.example.r0462870.eventtimer;

/**
 * Created by Sarah on 26/12/2015.
 */
public class DBList {
    private String name;
    private int id;

    public DBList()  {}

    public DBList(String name){
        this.name = name;
    }

    public DBList(String name, int id){
        this.name = name;
        this.id = id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
