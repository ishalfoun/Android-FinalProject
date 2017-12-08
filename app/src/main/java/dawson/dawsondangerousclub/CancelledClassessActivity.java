package dawson.dawsondangerousclub;


import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dawson.classes.Entry;
import dawson.classes.FeedParser;
import dawson.fragments.ClassDetailFragment;
import dawson.fragments.ClassMenuFragment;

public class CancelledClassessActivity extends OptionsMenu implements ClassMenuFragment.OnItemSelectedListener{
	
	ArrayList<Entry> entries;
    final static String MYTAG = "MYTAG";
	
    //RSS Feed URL
    private final String RSS_FEED_URL = "https://www.dawsoncollege.qc.ca/wp-content/external-includes/cancellations/feed.xml";
    TextView errorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_classess);

        errorTv = (TextView) findViewById(R.id.errors);

        // first check to see if we can get on the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadXmlTask().execute(RSS_FEED_URL);
        } else {
            errorTv.setText("No network connection available.");
        }
        displayEntries(savedInstanceState);
    }

    private void displayEntries(Bundle savedInstanceState)
    {
        if (entries == null) {
            entries = new ArrayList<>();
            entries.add(new Entry());
        }
        //Log.d(MYTAG,  "enter displayentries 1st entry:"+entries.get(0).title);
        ClassMenuFragment menuFragment = new ClassMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putParcelableArrayList("entries", entries);
        menuFragment.setArguments(args);

        if (savedInstanceState == null) {
            ft.add(R.id.flContainer, menuFragment);
            ft.commit();
        } else {
            ft.replace(R.id.flContainer, menuFragment);
            ft.commit();
        }

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
            displayEntries(null);
        }

        // Uploads XML from stackoverflow.com, parses it, and combines it with
        // HTML markup. Returns HTML string.
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;

            // Instantiate the parser
            FeedParser rssFeedParser = new FeedParser();
            entries = null;
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

            Log.d(MYTAG, "size: " + entries.size());

            return null;
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





