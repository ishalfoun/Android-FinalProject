package dawson.dawsondangerousclub;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.Calendar;

public class WhoIsFreeActivity extends AppCompatActivity {

    ArrayList<String> daysOffTheWeek;
    Spinner daySelector;
    ListView friendLV;
    Calendar currentTime;
    Calendar startTime;
    Calendar endTime;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private static final String BREAK_FRIENDS_URL = "https://dawsondangerousclub2.herokuapp.com/api/api/breakfriends?";
    private static final String TAG = "WhoIsFreeActivity";
    final SimpleDateFormat apiTimeFormat = new SimpleDateFormat("HHmm");
    final SimpleDateFormat uiTimeFormat = new SimpleDateFormat("h:mm a");
    SharedPreferences prefs;
    String user_email, user_password;

    ArrayList<String> friendNames;
    ArrayList<String> friendEmails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_is_free);

        daySelector = (Spinner)findViewById(R.id.daySpinner);
        friendLV =(ListView)findViewById(R.id.friendLV);
        tvStartTime = (TextView) findViewById(R.id.startTimeTV);
        tvEndTime = (TextView) findViewById(R.id.endTimeTV);

        //retrieve logged in user data
        prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        user_email = prefs.getString("email", "theo@gmail.com");
        user_password = prefs.getString("pw", "dawson");

        daysOffTheWeek = new ArrayList<String>();
        daysOffTheWeek.add(getResources().getString(R.string.monday));
        daysOffTheWeek.add(getResources().getString(R.string.tuesday));
        daysOffTheWeek.add(getResources().getString(R.string.wednesday));
        daysOffTheWeek.add(getResources().getString(R.string.thursday));
        daysOffTheWeek.add(getResources().getString(R.string.friday));

        //attach countries to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, daysOffTheWeek);
        daySelector.setAdapter(adapter);

        //set defaults
        daySelector.setSelection(0);
        currentTime = Calendar.getInstance();
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();

        //create lists to hold the friends names and emails.
        friendNames = new ArrayList<String>();
        friendEmails = new ArrayList<String>();

        //onItemClick find where the friend is at this time (or “Unknown Whereabouts”)  retrieve data via the API
        friendLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { friendEmails.get(position) });
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    public void onClickStartTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, startTimeListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false);
        dialog.show();
    }

    public void onClickEndTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, endTimeListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false);
        dialog.show();
    }

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            tvStartTime.setText(uiTimeFormat.format(startTime.getTime()));

        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            endTime.set(Calendar.HOUR_OF_DAY, hour);
            endTime.set(Calendar.MINUTE, minute);
            tvEndTime.setText(uiTimeFormat.format(endTime.getTime()));
        }
    };

    /**
     * Get friends on break during specified time, lanches Async Task
     * @param view
     */
    public void getBreakFriends(View view){
        new getBreakFriends().execute();
    }

    /**
     * This Async task gets the friends that are on break betwwen a 10am-5pm.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getBreakFriends extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //String url = "https://dawsondangerousclub2.herokuapp.com/api/api/breakfriends?email=theo@gmail.com&password=dawson&day=1&start=1000&end=1300";
                String url = BREAK_FRIENDS_URL + "email=" + user_email + "&password=" + user_password + "&day=" + (daySelector.getSelectedItemPosition()+1) + "&start=" + apiTimeFormat.format(startTime.getTime()) + "&end="+apiTimeFormat.format(endTime.getTime());
                Log.d(TAG, "Url: "+ url);
                return fetchBreakFriendsJSON(url);

            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Invalid request.") || result.equalsIgnoreCase("Unable to retrieve web page. URL may be invalid.")) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_warning), Toast.LENGTH_LONG).show();
            } else {

                String[] friendsArr = result.split(";");

                for (int x =0; x<=(friendsArr.length-1); x++) {
                    String [] nameAndEmail = friendsArr[x].split("#");
                    friendNames.add(nameAndEmail[0]);
                    friendEmails.add(nameAndEmail[1]);
                }

                friendLV.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.break_friends_list, R.id.breakFriend, friendNames));
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
    private String fetchBreakFriendsJSON(String aUrl) throws IOException {
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
        Log.i("friendData", friendDataJson);
        //reset string builder
        sb.setLength(0);

        try {

            //parse json text
            JSONArray friends = new JSONArray(friendDataJson);
            for (int friendPosition = 0; friendPosition < friends.length(); friendPosition++) {
                JSONObject friend = friends.getJSONObject(friendPosition);
                String friendName = friend.getString("name");
                String friendEmail = friend.getString("email");
                sb.append(friendName + "#" + friendEmail + ";");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }
        return sb.toString();
    }



}
