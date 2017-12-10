package dawson.dawsondangerousclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;


/**
 * Activity for displaying the friends that are found through the API
 * @author Theo
 */
public class FindFriendsActivity extends OptionsMenu {
	  private final String TAG = "FindFriendsActivity";
    ListView friendsListView;
    TextView friendsTitleTV;
    ArrayList<String> friendNames;
    ArrayList<String> friendEmails;
    SharedPreferences prefs;
    String user_email, user_password;
    private static final String FIND_FRIENDS_URL = "https://dawsondangerousclub2.herokuapp.com/api/api/allfriends?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        friendsListView = (ListView) findViewById(R.id.friendsList);
        friendsTitleTV = (TextView) findViewById(R.id.findFriendsTitle);

        //retrieve logged in user data
        prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        user_email = prefs.getString("email", "theo@gmail.com");
        user_password = prefs.getString("pw", "dawson");

        //onItemClick find where the friend is at this time (or “Unknown Whereabouts”)  retrieve data via the API
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent showFriendWhereabouts = new Intent(getApplicationContext(),
                        WhereIsFriendActivity.class);
                showFriendWhereabouts.putExtra("friend_name",friendNames.get(position));
                showFriendWhereabouts.putExtra("friend_email", friendEmails.get(position));
                showFriendWhereabouts.putExtra("user_email",user_email);
                showFriendWhereabouts.putExtra("user_password", user_password);
                startActivity(showFriendWhereabouts);
            }
        });


        //create lists to hold the friends names and emails.
        friendNames = new ArrayList<String>();
        friendEmails = new ArrayList<String>();

        // showing all friends, data retrieved via the API
        new getFriendsAsync().execute();
    }

    /**
     * This Async task gets all the users friends via the API.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getFriendsAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url = FIND_FRIENDS_URL + "email=" + user_email + "&password=" + user_password;
                return fetchFriendsJSON(url);

            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase("Invalid request.")) {
                Toast.makeText(getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
            } else {

                String[] friendsArr = result.split(";");

                for (String friendStr : friendsArr) {
                    String [] nameAndEmail = friendStr.split("#");
                    friendNames.add(nameAndEmail[0]);
                    friendEmails.add(nameAndEmail[1]);
                }

                friendsTitleTV.setText(getResources().getString(R.string.my_friends));
                friendsListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.friends_list, R.id.friendTV, friendNames));
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
    private String fetchFriendsJSON(String aUrl) throws IOException {
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
