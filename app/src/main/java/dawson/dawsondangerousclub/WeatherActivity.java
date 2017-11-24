package dawson.dawsondangerousclub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    EditText cityInput;
    Spinner countrySelector;
    TextView forecastDisplay;
    ArrayList<String> countries;

    String selectedCity ="montreal";
    String countryCode ="ca";

    private static final int NETIOBUFFER = 1024;
    private static final String API_KEY = "818cbaf6cb7daa55c791ff656317de47";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityInput = (EditText) findViewById(R.id.cityET);
        countrySelector = (Spinner) findViewById(R.id.countrySpinner);
        forecastDisplay = (TextView)findViewById(R.id.forecastTV);

        getCountriesFromFile();

        //attch countres to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, countries);
        countrySelector.setAdapter(adapter);

        //set defaults
        countrySelector.setSelection(39);

        // first check to see if we can get on the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            new getForecast().execute();

        } else {
            forecastDisplay.setText("No network connection available.");
        }




    }

    /**
     * Readd json file with all the countries and their ISO 3166 country codes . Puts them into an arraylist.
     */
    public void getCountriesFromFile(){

        countries = new ArrayList<String>();

        try {
            InputStream is = getResources().openRawResource(R.raw.country_and_codes);
            String jsonTxt = IOUtils.toString(is, "UTF-8");

            JSONArray json = new JSONArray(jsonTxt);

            for(int country=0; country < 249; country++ ){

                JSONObject countryData = (JSONObject) json.get(country);
                String countryCode = countryData.getString("Code");
                String countryName = countryData.getString("Name");
                countries.add(countryCode + "|" + countryName);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This Async task gets the forecast via an open weather API.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getForecast extends AsyncTask<Nullable, Void, String> {

        @Override
        protected String doInBackground(Nullable... nullables) {
            try {

                String url = "http://api.openweathermap.org/data/2.5/forecast?q=montreal,CA&appid=818cbaf6cb7daa55c791ff656317de47";


                //String url = WEATHER_URL + "q=" + selectedCity + "," + countryCode + "&appid=" + API_KEY;

                return fetchForecastJSON(url);

            } catch (IOException e) {

                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            if(result == "Invalid request."){

            }else {
                //temperatureTextView.setText(result);
                //Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
                Log.i("json", result);
                forecastDisplay.setText(result);
            }


        }

    }

    /**
     * GET request to the weather API, returns JSON.
     *
     * @param myurl
     * @return weather JSON
     * @throws IOException
     */
    private String fetchForecastJSON(String myurl) throws IOException {
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
                return "Invalid request.";

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
            Log.i("json", weatherDataJson);
            JSONObject jsonObj = new JSONObject(weatherDataJson);
            //JSONArray forecastList = jsonObj.getJSONArray("list");

            //for(int temp=0; temp < 40; temp+=9){
            //   JSONObject tempObj = (JSONObject) forecastList.get(temp);
            //    String day = tempObj.getString("dt_txt");
           //     Log.i("day", day);


            //}

            //double tempKelvin = Double.parseDouble(jsonObj.getJSONObject("main").getString("temp"));
            //double tempCelsius = tempKelvin - KELVIN;
            //currentTemperature = Integer.toString((int) Math.round(tempCelsius)) + "°C";

        } catch (JSONException e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }
        return weatherDataJson;
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
