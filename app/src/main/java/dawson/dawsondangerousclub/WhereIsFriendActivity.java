package dawson.dawsondangerousclub;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WhereIsFriendActivity extends OptionsMenu {

    private static final String WHERE_IS_FRIENDS_URL = "https://dawsondangerousclub2.herokuapp.com/api/api/whereisfriend?";
    private static final String TAG = "WhereIsFriendActivity";
    String friend_email, friend_name, user_email, user_password, time;
    TextView friendDataTV;
    boolean isWeekend = false;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_where_is_friend);

        friendDataTV = (TextView)findViewById(R.id.friend_data_tv);


        friend_name = getIntent().getExtras().getString("friend_name");
        friend_email = getIntent().getExtras().getString("friend_email");
        user_email = getIntent().getExtras().getString("user_email");
        user_password = getIntent().getExtras().getString("user_password");

        getTime();

        if(!isWeekend) {
            new getFriendWhereAboutsAsync().execute();
        }else{
            friendDataTV.setText(getResources().getString(R.string.weekend_text));
        }

    }

    /**
     * Resolves current time and day of week required by the API.
     */
    private void getTime(){
        //get time now in 24hr format
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        time = sdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        //get dayof week, weekends are not includes
        switch (day) {
            case Calendar.SUNDAY:
                isWeekend = true;
                break;
            case Calendar.MONDAY:
                day=1;
                break;
            case Calendar.TUESDAY:
                day=2;
                break;
            case Calendar.WEDNESDAY:
                day=3;
                break;
            case Calendar.THURSDAY:
                day=4;
                break;
            case Calendar.FRIDAY:
                day=5;
                break;
            case Calendar.SATURDAY:
                isWeekend = true;
                break;
        }
    }

    /**
     * This Async task gets the whereabouts of the friend selected.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getFriendWhereAboutsAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //String url = "https://dawsondangerousclub2.herokuapp.com/api/api/whereisfriend?email=theo@gmail.com&password=dawson&day=5&time=1200&friendemail=assunta.toy@example.net";
                String url = WHERE_IS_FRIENDS_URL + "email=" + user_email + "&password=" + user_password + "&friendemail=" + friend_email + "&time=" + time + "&day="+day;
                Log.d(TAG, "Url: "+ url);
                return fetchFriendWhereAboutsJSON(url);

            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {


            if (result.equalsIgnoreCase("Invalid request.")) {
                Toast.makeText(getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("free")) {
                // if the friend is free, notify user
                friendDataTV.setText(friend_name + " " + getResources().getString(R.string.is_free));
            }else{
                // else, display all the data of the friends whereabouts
                friendDataTV.setText(friend_name + "\n" + result);
            }

        }

    }

    /**
     * GET request to the dawson API, returns JSON.
     *
     * @param aUrl
     * @return weather JSON
     * @throws IOException
     */
    private String fetchFriendWhereAboutsJSON(String aUrl) throws IOException {
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
     * @param stream
     * @return JSON String
     * @throws IOException
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
        String friendDataJson = sb.toString();
        String buildStr;
        Log.i("friendData", friendDataJson);
        //reset string builder
        sb.setLength(0);

        try {
            //parse json text
            JSONObject whereAboutsObj = new JSONObject(friendDataJson);

            if(whereAboutsObj.length()== 0){
                sb.append("free");
            }else{
                String teacher = whereAboutsObj.getString("teacher");
                String section = whereAboutsObj.getString("section");
                String title = whereAboutsObj.getString("title");
                String class_code = whereAboutsObj.getString("class");

                sb.append(title + "\n with " + teacher + "\nSection: " + section + "\n" + class_code);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }

        return sb.toString();
    }

}
