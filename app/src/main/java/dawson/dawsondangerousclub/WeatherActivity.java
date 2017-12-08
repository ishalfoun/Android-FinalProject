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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherActivity extends OptionsMenu {

    EditText cityInput;
    Spinner countrySelector;
    ArrayList<String> countries;
    ListView forecastListView;
    LinearLayout mainLayout;
    InputMethodManager keyboard;
    TextView uvTV;

    String selectedCity = "montreal";
    String countryCode = "ca";
    String latitude;
    String longitude;

    private static final String API_KEY = "818cbaf6cb7daa55c791ff656317de47";
    private static final String WEATHER_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?";
    private static final String WEATHER_UV_URL = "http://api.openweathermap.org/data/2.5/uvi/forecast?";
    private static final double KELVIN = 273.15;

    List<String> forecastDetails = new ArrayList<String>();
    List<Integer> forecastImages = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityInput = (EditText) findViewById(R.id.cityET);
        countrySelector = (Spinner) findViewById(R.id.countrySpinner);
        uvTV = (TextView) findViewById(R.id.uvTV);
        mainLayout = (LinearLayout) findViewById(R.id.weatherWindow);
        forecastListView = (ListView) findViewById(R.id.forecastLV);
        keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        getCountriesFromFile();

        //attach countries to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, countries);
        countrySelector.setAdapter(adapter);

        //set defaults
        countrySelector.setSelection(39);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        forecastListView.setDivider(null);

        //get default montreal forecast
        getForecast();


    }

    /**
     * Launches async task to get forecast, checks for network connectivity first.
     */
    public void getForecast() {
        // first check to see if we can get on the network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new getForecastAsync().execute();

        } else {
            Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_LONG).show();

        }
        //hide keyboard
        keyboard.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    /**
     * Button onClick() to get forecast.
     *
     * @param view
     */
    public void findForecast(View view) {
        selectedCity = cityInput.getText().toString().trim();
        countryCode = countrySelector.getSelectedItem().toString().split("\\|")[0].trim();
        getForecast();
    }

    /**
     * Read json file with all the countries and their ISO 3166 country codes . Puts them into an array list.
     */
    public void getCountriesFromFile() {

        countries = new ArrayList<String>();

        try {
            InputStream is = getResources().openRawResource(R.raw.country_and_codes);
            String jsonTxt = IOUtils.toString(is, "UTF-8");

            JSONArray json = new JSONArray(jsonTxt);

            for (int country = 0; country < 249; country++) {

                JSONObject countryData = (JSONObject) json.get(country);
                String countryCode = countryData.getString("Code");
                String countryName = countryData.getString("Name");
                countries.add(countryCode + "|" + countryName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the image resource id according to the image code.
     *
     * @param imageCode internal weather description code
     * @return image resource id
     */
    private int getForecastImage(String imageCode) {

        int imageResourceId = 0;

        switch (imageCode) {
            case "01d":
                imageResourceId = R.drawable.w01d;
                break;
            case "01n":
                imageResourceId = R.drawable.w01n;
                break;
            case "02d":
                imageResourceId = R.drawable.w02d;
                break;
            case "02n":
                imageResourceId = R.drawable.w02n;
                break;
            case "03d":
                imageResourceId = R.drawable.w03d;
                break;
            case "03n":
                imageResourceId = R.drawable.w03n;
                break;
            case "04d":
                imageResourceId = R.drawable.w04d;
                break;
            case "04n":
                imageResourceId = R.drawable.w04n;
                break;
            case "09d":
                imageResourceId = R.drawable.w09d;
                break;
            case "09n":
                imageResourceId = R.drawable.w09n;
                break;
            case "10d":
                imageResourceId = R.drawable.w10d;
                break;
            case "10n":
                imageResourceId = R.drawable.w10n;
                break;
            case "11d":
                imageResourceId = R.drawable.w11d;
                break;
            case "11n":
                imageResourceId = R.drawable.w11n;
                break;
            case "13d":
                imageResourceId = R.drawable.w13d;
                break;
            case "13n":
                imageResourceId = R.drawable.w13n;
                break;
            case "50d":
                imageResourceId = R.drawable.w50d;
                break;
            case "50n":
                imageResourceId = R.drawable.w50n;
                break;

        }

        return imageResourceId;
    }

    /**
     * This Async task gets the forecast via an open weather API.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getForecastAsync extends AsyncTask<Nullable, Void, String> {

        @Override
        protected String doInBackground(Nullable... nullables) {
            try {

                //String url = "http://api.openweathermap.org/data/2.5/forecast?q=montreal,CA&appid=818cbaf6cb7daa55c791ff656317de47";

                String url = WEATHER_FORECAST_URL + "q=" + selectedCity + "," + countryCode + "&appid=" + API_KEY;

                return fetchForecastJSON(url);

            } catch (IOException e) {

                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Invalid request.")) {
                Toast.makeText(getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
            } else {
                //empty any previous saved forecasts
                forecastDetails.clear();
                forecastImages.clear();

                String[] forecastAndLatAndLon = result.split("#");
                Log.i("latandlong", forecastAndLatAndLon[0]);
                String[] latAndLon = forecastAndLatAndLon[0].split("\\?");
                latitude = latAndLon[0];
                longitude = latAndLon[1];


                String[] allForecasts = forecastAndLatAndLon[1].split("\\|");
                //Log.i("line numbers", Integer.toString(allForecasts.length));

                for (String forecast : allForecasts) {
                    // Log.i("line", forecast );

                    String[] forecastAndImgArr = forecast.split("@");
                    // Log.i("line", forecastAndImgArr[0] );
                    forecastDetails.add(forecastAndImgArr[0]);
                    forecastImages.add(getForecastImage(forecastAndImgArr[1].trim()));

                    //Log.i("description", forecastAndImgArr[0] );
                    //Log.i("image", forecastAndImgArr[1] );

                }

                //populate listView with forecast data
                forecastListView.setAdapter(new forecastAdapter(forecastDetails.toArray(new String[0]), toIntArray(forecastImages)));

                //get UV index for location
                new getUVIndexAsync().execute();

            }


        }

    }

    /**
     * GET request to the weather API, returns JSON.
     *
     * @param aUrl api get request url
     * @return weather JSON
     * @throws IOException in case the url is incorrect
     */
    private String fetchForecastJSON(String aUrl) throws IOException {
        InputStream is = null;

        HttpURLConnection conn = null;
        URL url = new URL(aUrl);
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
     * @param stream data from api
     * @return JSON String
     * @throws IOException in case json is invalid
     */
    public String convertInputStreamToString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                stream, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        //get json string
        String weatherDataJson = sb.toString();

        //reset string builder
        sb.setLength(0);

        try {

            //parse json text
            JSONObject jsonObj = new JSONObject(weatherDataJson);

            //retrieve the number of forecasts "cnt"(count) provided by the API
            int forecasts = jsonObj.getInt("cnt");
            //retrieve longitude and longitude
            String latitude = Double.toString(jsonObj.getJSONObject("city").getJSONObject("coord").getDouble("lat"));
            String longitude = Double.toString(jsonObj.getJSONObject("city").getJSONObject("coord").getDouble("lon"));
            sb.append(latitude + "?" + longitude + "#");

            //get the all the forecasts
            JSONArray forecastList = jsonObj.getJSONArray("list");

            //iterate through each forecast and retrieve relevant data
            for (int forecast = 0; forecast < forecasts; forecast++) {

                JSONObject weatherObj = (JSONObject) forecastList.get(forecast);
                JSONObject weatherTemperature = weatherObj.getJSONObject("main");
                JSONArray weatherDetails = weatherObj.getJSONArray("weather");

                String forecastDate = weatherObj.getString("dt_txt");
                String description = weatherDetails.getJSONObject(0).getString("description");
                String icon = weatherDetails.getJSONObject(0).getString("icon");
                String temperature = Integer.toString((int) Math.round(weatherTemperature.getDouble("temp") - KELVIN)) + "°C";

                description = description.substring(0, 1).toUpperCase() + description.substring(1);

                //format found in json
                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parsedDate = apiFormat.parse(forecastDate);
                //desired format
                SimpleDateFormat appFormat = new SimpleDateFormat("EEEE MMMM dd ha");

                //Log.i("forecast", appFormat.format(parsedDate)+ " " + description + " " +  temperature + " @"+ icon);

                // amalgamate data to be deciphered in async task post execute, "^" separates each weather forecast
                sb.append(appFormat.format(parsedDate) + " " + description + " " + temperature + " @" + icon + "|");

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }
        return sb.toString();
    }

    /**
     * This Async task gets the uv forecast via an open weather API.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getUVIndexAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... location) {
            try {

                String url = WEATHER_UV_URL + "appid=" + API_KEY + "&lat=" + latitude + "&lon=" + longitude;

                return fetchUVIndexJSON(url);

            } catch (IOException e) {

                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Invalid request.")) {
                Toast.makeText(getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
            } else {
                uvTV.setText(result);
            }


        }

    }

    /**
     * GET request to the weather API, returns JSON.
     *
     * @param aUrl
     * @return weather JSON
     * @throws IOException
     */
    private String fetchUVIndexJSON(String aUrl) throws IOException {
        InputStream is = null;

        HttpURLConnection conn = null;
        URL url = new URL(aUrl);
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
            return convertInputStreamUVIndexToString(is);

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
    public String convertInputStreamUVIndexToString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                stream, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        //get json string
        String uvDataJson = sb.toString();
        Log.i("uvdata", uvDataJson);
        //reset string builder
        sb.setLength(0);

        try {

            //parse json text
            JSONArray uvForecasts = new JSONArray(uvDataJson);

            for (int uvForecast = 0; uvForecast < uvForecasts.length(); uvForecast++) {
                JSONObject forecast = uvForecasts.getJSONObject(uvForecast);
                int timestamp = forecast.getInt("date");
                double uvIndex = forecast.getInt("value");
                String intensity = "LOW";

                Date d = new Date((long) timestamp * 1000);
                DateFormat f = new SimpleDateFormat("EEEE MMMM dd");
                f.format(d);

                if (uvIndex > 2 && uvIndex < 6) {
                    intensity = "MODERATE";
                } else if (uvIndex >= 6 && uvIndex < 8) {
                    intensity = "HIGH";
                } else if (uvIndex >= 8 && uvIndex < 11) {
                    intensity = "VERY HIGH";
                }
                if (uvIndex >= 11) {
                    intensity = "EXTREME";
                }


                sb.append(f.format(d) + "---" + uvIndex + " " + intensity + " \n");

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }
        return sb.toString();
    }

    class forecastAdapter extends BaseAdapter {
        String[] text;
        int[] image;

        forecastAdapter() {
            text = null;
            image = null;
        }

        public forecastAdapter(String[] texts, int[] images) {
            text = texts;
            image = images;

        }

        public int getCount() {
            return text.length;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.forecast_list, parent, false);

            TextView textV;
            ImageView i1;

            textV = (TextView) row.findViewById(R.id.forecastText);
            i1 = (ImageView) row.findViewById(R.id.forecastImage);
            textV.setText(text[position]);
            i1.setImageResource(image[position]);

            return (row);
        }
    }

    public int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }


}
