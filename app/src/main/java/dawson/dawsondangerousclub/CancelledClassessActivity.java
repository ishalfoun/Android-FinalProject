package dawson.dawsondangerousclub;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import dawson.dawsondangerousclub.ClassDetailFragment;
import dawson.dawsondangerousclub.ClassMenuFragment;

public class CancelledClassessActivity extends AppCompatActivity implements ClassMenuFragment.OnItemSelectedListener{
	
	ArrayList<Entry> entries;
	
    //RSS Feed URL
    private final String RSS_FEED_URL = "https://www.dawsoncollege.qc.ca/wp-content/external-includes/cancellations/feed.xml";
    TextView errorTv;

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    public static String sPref = null;

//    myAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_classess);

        //errorTv = (TextView) findViewById(R.id.errors);

        // first check to see if we can get on the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadXmlTask().execute(RSS_FEED_URL);
        } else {
            errorTv.setText("No network connection available.");
        }

        Log.d("MYTAG",  "reached end of oncreate main");
//        displayEntries();
    }
    private void displayEntries()
    {

//        List<String>  listofOptions = (List<String>) Arrays.asList(options);
//        then you can user constructoru of an arraylist to instantiate with predefined values.
//
//            ArrayList<String> arrlistofOptions = new ArrayList<String>(list);
//
        if (entries == null) {
            entries = new ArrayList<>();
            entries.add(new Entry());
        }

            Log.d("MYTAG",  "enter displayentries 1st entry:"+entries.get(0).title);
        ClassMenuFragment menuFragment = new ClassMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putParcelableArrayList("entries", entries);
        menuFragment.setArguments(args);

        //if (savedInstanceState == null) {
            ft.add(R.id.flContainer, menuFragment);
            ft.commit();
//        } else {
//            ft.replace(R.id.flContainer, menuFragment);
//            ft.commit();
//        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ClassDetailFragment secondFragment = new ClassDetailFragment();
            args = new Bundle();
            args.putInt("position", 0);
            args.putParcelableArrayList("entries", entries);
            secondFragment.setArguments(args);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.add(R.id.flContainer2, secondFragment);
            ft2.commit();
        }

    }
	
	
	
	
    @Override
    public void onClassItemSelected(int position) {
        ClassDetailFragment detailFragment = new ClassDetailFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelableArrayList("entries", entries);
        detailFragment.setArguments(args);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer2, detailFragment)
                    //.addToBackStack(null)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


	
	
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                //return "trace: "+e.pri() + " "+getResources().getString(R.string.xml_error);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //setContentView(R.layout.activity_cancelled_classess);
            // Displays the HTML string in the UI via a WebView
//            WebView myWebView = (WebView) findViewById(R.id.webview);
//            myWebView.loadData(result, "text/html", null);


            displayEntries();
        }

        // Uploads XML from stackoverflow.com, parses it, and combines it with
        // HTML markup. Returns HTML string.
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;

            // Instantiate the parser
            FeedParser rssFeedParser = new FeedParser();
            entries = null;
            String title = null;
            String description = null;
            String course = null;
            String teacher = null;
            String notes = null;
            String pubDate = null;

            Calendar rightNow = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

            StringBuilder htmlString = new StringBuilder();
            htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
            htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                    formatter.format(rightNow.getTime()) + "</em>");

            try {
                stream = downloadUrl(urlString);
                entries = (ArrayList<Entry>) rssFeedParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            Log.d("MYTAG", "size: " + entries.size());

//            myAdapter adapter;
            int mCurPosition = -1;



            // FeedParser returns a List (called "entries") of Entry objects.
            // Each Entry object represents a single item in the XML feed.
            // This section processes the entries list to combine each entry with HTML markup.
            // Each entry is displayed in the UI as a link that includes the description.
            for (Entry entry : entries) {
                htmlString.append("<p><a href='");
                htmlString.append(entry.title);
                htmlString.append("'>" + entry.title + "</a></p>");
                // If the user set the preference to include summary text,
                // adds it to the display.
                    htmlString.append(entry.description);
            }
            return htmlString.toString();
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }

	
}





