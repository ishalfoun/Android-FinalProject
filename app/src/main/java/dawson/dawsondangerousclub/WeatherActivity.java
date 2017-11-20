package dawson.dawsondangerousclub;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    // GPSTracker class
    GPSTracker gps;
    TextView temperatureTextView;
    private static final int NETIOBUFFER = 1024;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temperatureTextView = (TextView) findViewById(R.id.tempTV);

        // create class object
        gps = new GPSTracker(WeatherActivity.this);
        // create class object
        gps = new GPSTracker(WeatherActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
             latitude = gps.getLatitude();
             longitude = gps.getLongitude();


            // first check to see if we can get on the network
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // invoke the AsyncTask to do the dirtywork.
                new DownloadWebpageText().execute();
                // text is set in DownloadWebpageText().onPostExecute()
            } else {
                temperatureTextView.setText("No network connection available.");
            }


            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }


    private class DownloadWebpageText extends AsyncTask<Nullable, Void, String> {


        @Override
        protected String doInBackground(Nullable... nullables) {
// params comes from the execute() call: params[0] is the url.
            try {


                String url = "api.openweathermap.org/data/2.5/weather?"+"lat="+latitude+"&lon="+longitude;

                return downloadUrl(url);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }        }

        protected void onPostExecute(String result) {
            //temperatureTextView.setText(result);



        }

    } // AsyncTask DownloadWebpageText()

	/*
     * Given a URL, establishes an HttpUrlConnection and retrieves the web page
	 * content as a InputStream, which it returns as a string.
	 */

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        HttpURLConnection conn = null;
        URL url = new URL(myurl);
        try {
            // create and open the connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            // specifies whether this connection allows receiving data
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            int response = conn.getResponseCode();

			/*
			 *  check the status code HTTP_OK = 200 anything else we didn't get what
			 *  we want in the data.
			 */
            if (response != HttpURLConnection.HTTP_OK)
                return "Server returned: " + response + " aborting read.";

            // get the stream for the data from the website
            is = conn.getInputStream();
            // read the stream, returns String
            return readIt(is);

        } catch (IOException e) {
            throw e;
        } finally {
			/*
			 * Make sure that the InputStream is closed after the app is
			 * finished using it.
			 * Make sure the connection is closed after the app is finished using it.
			 */

            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
                if (conn != null)
                    try {
                        conn.disconnect();
                    } catch (IllegalStateException ignore) {
                    }
            }
        }
    } // downloadUrl()

    public String readIt(InputStream stream) throws IOException,
            UnsupportedEncodingException {
        int bytesRead, totalRead = 0;
        byte[] buffer = new byte[NETIOBUFFER];

        // for data from the server
        BufferedInputStream bufferedInStream = new BufferedInputStream(stream);
        // to collect data in our output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream writer = new DataOutputStream(byteArrayOutputStream);

        // read the stream until end
        while ((bytesRead = bufferedInStream.read(buffer)) != -1) {
            writer.write(buffer);
            totalRead += bytesRead;
        }
        writer.flush();
        return new String(byteArrayOutputStream.toString());
    } // readIt()


}
