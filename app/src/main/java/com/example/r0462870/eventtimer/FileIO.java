package com.example.r0462870.eventtimer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileIO {
    private final String URL_STRING = "https://raw.githubusercontent.com/sarah994/TimerDataXml/master/boss_timers.xml";
    private final String FILENAME = "boss_data.xml";
    private Context context = null;

    public FileIO (Context context) {
        this.context = context;
    }

    public void downloadFile() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            try {
                URL url = new URL(URL_STRING);

                InputStream in = url.openStream();

                FileOutputStream out =
                        context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                while (bytesRead != -1) {
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                Log.e("Event timer", e.toString());
            }
        }
    }

    public RSSFeed readFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            FileInputStream in = context.openFileInput(FILENAME);

            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        }
        catch (Exception e) {
            Log.e("Event timer", e.toString());
            return null;
        }
    }
}
