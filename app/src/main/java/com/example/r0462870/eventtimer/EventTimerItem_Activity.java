package com.example.r0462870.eventtimer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by r0462870 on 23/11/2015.
 */
public class EventTimerItem_Activity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView eventTimeTextView = (TextView) findViewById(R.id.eventTimeTextView);
        TextView waypointTextView = (TextView) findViewById(R.id.waypointTextView);
        TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
        TextView preTextView = (TextView) findViewById(R.id.preTextView);
        TextView preLocationTextView = (TextView) findViewById(R.id.preLocationTextView);
        TextView preWaypointTextView = (TextView) findViewById(R.id.preWaypointTextView);

        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        String eventTime = intent.getStringExtra("eventTime");
        String name = intent.getStringExtra("name");
        String waypoint = intent.getStringExtra("waypoint");
        String location = intent.getStringExtra("location");
        String pre = intent.getStringExtra("pre");
        String preLocation = intent.getStringExtra("preLocation");
        String preWaypoint = intent.getStringExtra("preWaypoint");

        // display data on the widgets
        eventTimeTextView.setText(eventTime);
        nameTextView.setText(name);
        waypointTextView.setText(waypoint);
        locationTextView.setText(location);
        preTextView.setText(pre);
        preLocationTextView.setText(preLocation);
        preWaypointTextView.setText(preWaypoint);
    }

    @Override
    public void onClick(View v) {
        // get the intent
        Intent intent = getIntent();
    }

}
