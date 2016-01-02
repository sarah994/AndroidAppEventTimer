package com.example.r0462870.eventtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EventTimerActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener{
    private RSSFeed feed;
    private FileIO io;
    private DBEventList db;
    StringBuilder sb = new StringBuilder();

    private TextView titleTextView;
    private ListView itemsListView;

    private int positionFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_timer);
        io = new FileIO(getApplicationContext());

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

        itemsListView.setOnItemClickListener(this);

        new DownloadFeed().execute();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventTimerActivity.this.updateDisplay();
                            }
                        });
                    }
                }
                catch (InterruptedException e) {

                }
            }
        };
        t.start();
    }

    class DownloadFeed extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params){
            io.downloadFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.d("Event timer", "Feed Downloaded");
            new ReadFeed().execute();
        }
    }

    class ReadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            feed = io.readFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Log.d("Event timer", "Feed read");
            EventTimerActivity.this.uploadAllDataToDatabase();
        }
    }

    public void uploadAllDataToDatabase() {
        db = new DBEventList(this);
        ArrayList<RSSItem> items = feed.getAllItems();

        for (RSSItem item : items) {
            DBEvent event = new DBEvent(1, item.getName(), item.getNr(), item.getEventTime(), item.getWaypoint(), item.getLocation(), item.getPre(), item.getPreLocation(), item.getPreWaypoint());
            long insertId = db.insertEvent(event);
            if(insertId > 0)
            {
                sb.append("Row inserted! Insert id: " +insertId + "\n");
            }
        }
    }

    public void updateDisplay(){
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // get the items for the feed
        //ArrayList<RSSItem> items = feed.getAllItems();
        ArrayList<DBEvent> events = db.getEvents("events");

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();


        /*for (RSSItem item : items) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nr", item.getNr());
            map.put("eventTime", item.getEventTimeFormatted());
            map.put("name", item.getName());
            data.add(map);
        }*/
        for (DBEvent event : events) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nr", event.getNr());
            map.put("eventTime", event.getEventTime());
            map.put("name", event.getName());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"eventTime", "name"};
        int[] to = {R.id.eventTimeTextView, R.id.nameTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("Event Timer", "Feed displayed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        RSSItem item = feed.getItem(positionFirst+position);

        // create an intent
        Intent intent = new Intent(this, EventTimerItem_Activity.class);

        intent.putExtra("eventTime", item.getEventTime());
        intent.putExtra("name", item.getName());
        intent.putExtra("waypoint", item.getWaypoint());
        intent.putExtra("location", item.getLocation());
        intent.putExtra("pre", item.getPre());
        intent.putExtra("preLocation", item.getPreLocation());
        intent.putExtra("preWaypoint", item.getPreWaypoint());

        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            //Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
