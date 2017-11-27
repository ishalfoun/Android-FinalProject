package dawson.dawsondangerousclub;

import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int NETIOBUFFER = 1024;
    private static final int LOCATION_REQUEST = 1340;
    private static final String API_KEY = "818cbaf6cb7daa55c791ff656317de47";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String TAG = "MainActivity";
    private static final double KELVIN = 273.15;

    GPSTracker gps;
    double latitude;
    double longitude;
    TextView temperatureTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = (TextView) findViewById(R.id.tempTV);

        //force location permission
        locationPermissionRequest();

        // create class object
        gps = new GPSTracker(MainActivity.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        Log.i(TAG, "Lat: " + latitude + "Long: " + longitude);

        // \n is for new line
        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


        // check if GPS enabled
        if (gps.canGetLocation()) {

            // first check to see if we can get on the network
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // invoke the AsyncTask to do the dirtywork.
                new getTemperature().execute();
                // text is set in DownloadWebpageText().onPostExecute()
                //temperatureTextView.setText("Network connected.");
            } else {
                temperatureTextView.setText("No network connection available.");
            }

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    /**
     * Fires an intent according to activity requested.
     *
     * @param view
     */
    public void openActivity(View view) {

        switch (view.getId()) {

            case R.id.cancellationsImgBttn:
                startActivity(new Intent(MainActivity.this, CancelledClassessActivity.class));
                break;
            case R.id.teacherImgBttn:
                startActivity(new Intent(MainActivity.this, FindTeacherActivity.class));
                break;
            case R.id.calendarImgBttn:
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                break;
            case R.id.notesImgBttn:
                startActivity(new Intent(MainActivity.this, NotesActivity.class));
                break;
            case R.id.weatherImgBttn:
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                break;
            case R.id.academicCalendarImgBttn:
                startActivity(new Intent(MainActivity.this, AcademicCalendarActivity.class));
                break;
            case R.id.dawsonImgBttn:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/")));
                break;
            case R.id.teamImgBttn:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

        }


    }

    /**
     * If location permission is not already granted, this method demands for it.
     */
    private void locationPermissionRequest() {

        while (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        }


    }

    /**
     * Checks if desired feature is available.
     *
     * @param feature
     * @return
     */
    private boolean hasPermission(String feature) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, feature));
    }

    /**
     * This Async task gets the local temperature via an open weather API. It uses
     * the location provided from the OS. It receives JSON data which is deciphered
     * and displayed to the user.
     */
    private class getTemperature extends AsyncTask<Nullable, Void, String> {

        @Override
        protected String doInBackground(Nullable... nullables) {
            try {

                String url = WEATHER_URL + "lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;

                //Lat: 45.4888635 Long: -73.5877567 (dawson test)
                //String url = "http://api.openweathermap.org/data/2.5/weather?lat=45.4888635&lon=-73.5877567&appid=818cbaf6cb7daa55c791ff656317de47";

                return fetchTemperatureJSON(url);

            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            temperatureTextView.setText(result);

        }

    }

    /**
     * GET request to the weather API, returns JSON.
     *
     * @param myurl
     * @return weather JSON
     * @throws IOException
     */
    private String fetchTemperatureJSON(String myurl) throws IOException {
        InputStream is = null;

        HttpURLConnection conn = null;
        URL url = new URL(myurl);
        try {
            // create and open the connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();

            if (response != HttpURLConnection.HTTP_OK)
                return "Server returned: " + response + " aborting read.";

            // get the stream for the data from the website
            is = conn.getInputStream();
            // read the stream, returns String
            return convertInputStreamToString(is);

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
    }

    /**
     * Converts the input stream returned form the website into a readable JSON string.
     *
     * @param stream
     * @return JSON String
     * @throws IOException
     */
    public String convertInputStreamToString(InputStream stream) throws IOException {
        int bytesRead, totalRead = 0;
        byte[] buffer = new byte[NETIOBUFFER];
        String currentTemperature = "0";

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

        String weatherDataJson = byteArrayOutputStream.toString();

        try {

            JSONObject jsonObj = new JSONObject(weatherDataJson);
            double tempKelvin = Double.parseDouble(jsonObj.getJSONObject("main").getString("temp"));
            double tempCelsius = tempKelvin - KELVIN;
            currentTemperature = Integer.toString((int) Math.round(tempCelsius)) + "°C";

        } catch (JSONException e) {
            throw new IOException("JSON data invalid");
        }
        return currentTemperature;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //open About app activity
            case R.id.about:
                Intent openAbout = new Intent(getApplicationContext(),
                        AboutActivity.class);
                startActivity(openAbout);
                return true;
            //Launches Dawson Computer Science web page
            case R.id.dawson:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
                startActivity(i);
                return true;
            //Open last viewed quote
            case R.id.settings:
                //open settings activity
                Intent openSettings = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(openSettings);
                return true;
            default:
                return false;
        }

    }


}
