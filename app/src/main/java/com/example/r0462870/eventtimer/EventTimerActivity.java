package com.example.r0462870.eventtimer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EventTimerActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener{
    private RSSFeed feed;
    private FileIO io;
    private DBEventList db;
    private StringBuilder sb = new StringBuilder();
    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    private ArrayList<HashMap<String, String>> data;

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

        titleTextView.setText("Laden.....");

        new DownloadFeed().execute();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
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
            EventTimerActivity.this.loadDatabase();
        }
    }

    public void loadDatabase() {
        db = new DBEventList(this);
        ArrayList<RSSItem> items = feed.getAllItems();

        for (RSSItem item : items) {
            DBEvent event = new DBEvent(1, item.getName(), item.getNr(), item.getEventTime(),
                    item.getWaypoint(), item.getLocation(), item.getPre(), item.getPreLocation(), item.getPreWaypoint());
            long insertId = db.insertEvent(event);
            if(insertId > 0)
            {
                sb.append("Row inserted! Insert id: " +insertId + "\n");
            }
        }

        ArrayList<DBEvent> events = db.getEvents("events");
        data = new ArrayList<HashMap<String, String>>();
        for (DBEvent event : events) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nr", event.getNr());
            map.put("eventTime", event.getEventTime());
            map.put("name", event.getName());
            data.add(map);
        }
    }

    public void updateDisplay(){
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }
        else{
            titleTextView.setText("");
        }

        boolean nextEvent = false;
        int eventNr = 0;
        String utcNow = sdf.format(date);

        while(nextEvent == false)
        {
            int eventTimeInt = convertStringDateToInt(data.get(eventNr).get("eventTime"));
            if(eventTimeInt > convertStringDateToInt(utcNow))
            {
                nextEvent = true;
                positionFirst = eventNr;
            }
            else{
                nextEvent = false;
                eventNr++;
            }
        }

        ArrayList<HashMap<String, String>> filteredData = new ArrayList<HashMap<String, String>>();
        int positie = positionFirst;
        int i = 0;
        for(int j = 0; j<7;j++ ){
            if(positie > 95){
                positie = 0;
                i = 0;
            }
            HashMap<String, String> map = new HashMap<String, String>();
            String formattedTime = data.get(positie+i).get("eventTime");
            map.put("eventTimeFormatted", getEventTimeFormatted(formattedTime));
            map.put("name", data.get(positie+i).get("name"));
            filteredData.add(map);
            i++;
        }

        // create the resource, from, and to variables

        int resource = 0;
        if(getScreenOrientation() == true){
            resource = R.layout.listview_item;
        }
        else{
            resource = R.layout.listview_item_land;
        }
        String[] from = {"eventTimeFormatted", "name"};
        int[] to = {R.id.eventTimeTextView, R.id.nameTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, filteredData, resource, from, to);
        itemsListView.setAdapter(adapter);

        if(filteredData.get(0).get("eventTimeFormatted") == "recreate"){
            finish();
            startActivity(getIntent());
        }

        Log.d("Event Timer", "Feed displayed");
    }

    public int convertStringDateToInt(String date){
        String[] dateSplit = date.split(":");
        String dateWithTimeZone = dateSplit[0]+dateSplit[1];
        String[] dateTimeZoneSplit = dateWithTimeZone.split(" ");
        int result;
        if(Integer.parseInt(dateTimeZoneSplit[0])==0){
            result= 60;
        }
        else{
            result = Integer.parseInt(dateTimeZoneSplit[0]);
        }
        return result;
    }

    public String getEventTimeFormatted(String eventTime) {
        try {
            Date dateUpdated = new Date();
            Date myDate = sdf.parse(eventTime);
            String utcEvent = sdf2.format(myDate);
            String[] eventSplit = utcEvent.split(":");
            int eventHour = Integer.parseInt(eventSplit[0]);
            int eventMin = Integer.parseInt(eventSplit[1]);

            String localTime = sdf.format(dateUpdated);
            String[] localSplit = localTime.split(":");
            int localHour = Integer.parseInt(localSplit[0]);
            int localMin = Integer.parseInt(localSplit[1]);

            int hour = 0;
            int min = 0;
            //enkele voorwaarden
            if(eventMin == 0){eventMin = 60; eventHour-=1;}
            if(eventMin < localMin){
                eventHour-=1;
                min= (60-localMin) + eventMin;
            }
            else{
                min = eventMin - localMin;
            }
            if(eventHour<localHour){
                eventHour = 24 + eventHour;
            }

            hour = eventHour - localHour;

            //extra regels voor timer
            if(hour==0 && min == 0){
                return "event up";
            }
            else if(hour < 0 || hour > 3){
                return "recreate";
            }
            else{
                return Integer.toString(hour) +"h "+Integer.toString(min)+"m";
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        if(getOrient.getRotation() == Surface.ROTATION_90 || getOrient.getRotation() == Surface.ROTATION_270)
        {
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        //error controle
        int startPositie = positionFirst;
        int positieNr = position;
        if(startPositie+positieNr>95){
            int positieSom = positionFirst + positieNr;
            startPositie = positieSom - 95;
        }
        else{
            startPositie = positionFirst + position;
        }
        // get the item at the specified position
        RSSItem item = feed.getItem(startPositie);

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
