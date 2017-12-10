package dawson.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

import dawson.classes.Entry;
import dawson.dawsondangerousclub.FindFriendsInCourse;
import dawson.dawsondangerousclub.FindTeacherActivity;
import dawson.dawsondangerousclub.R;


public class ClassDetailFragment extends Fragment {
    int position = 0;
	final static String MYTAG = "MYTAG";
    TextView tvClassTitle;
    TextView tvClassDescription;
    TextView tvClassName;
    TextView tvClassTeacher;
    TextView tvClassNotes;
    TextView tvClassPubDate;
    ArrayList<Entry> entries;
    ArrayList<String> friends;
    ArrayList<String> friendsEmails;

    private static final String COURSE_FRIENDS_URL = "https://dawsondangerousclub2.herokuapp.com/api/api/coursefriends?";
    private static final String TAG = "ClassDetailFragment";
    SharedPreferences prefs;
    String user_email, user_password, courseName, section;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve logged in user data
        prefs = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        user_email = prefs.getString("email", "theo@gmail.com");
        user_password = prefs.getString("pw", "dawson");
        friends = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_class, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(savedInstanceState == null){
            if(getArguments() != null) {
                position = getArguments().getInt("position", 0);
                entries = getArguments().getParcelableArrayList("entries");
            }
        }

        Log.d(MYTAG,  "got the entries in detailfrag: "+" "+ (entries != null ? entries.get(0).title : "empty"));

        tvClassTitle = (TextView) view.findViewById(R.id.classTitle);
        tvClassTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String[] courseSplit = ((String)tvClassTitle.getText()).split(" ");
                courseName = courseSplit[2];
                section = courseSplit[3];

                Log.d(MYTAG,"long click: "+courseName+" "+section);

                new getFriendsInCourse().execute();

                return false;
            }
        });

		tvClassDescription = (TextView) view.findViewById(R.id.classDescription);
		tvClassName = (TextView) view.findViewById(R.id.className);
		tvClassTeacher = (TextView) view.findViewById(R.id.classTeacher);
        tvClassTeacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), FindTeacherActivity.class);
                    String[] name = ((String)tvClassTeacher.getText()).split(" ");
                    i.putExtra("firstname", name[1]);
                    i.putExtra("lastname", name[2]);
                    startActivity(i);
                }
            });
		tvClassNotes = (TextView) view.findViewById(R.id.classNotes);
		tvClassPubDate = (TextView) view.findViewById(R.id.classPubDate);

        updateView();
    }

    // Activity is calling this to update view on Fragment
    public void updateView(){
        if (entries != null) {
            tvClassTitle.setText(getResources().getString(R.string.classTitle) + " " + entries.get(position).title);
//        tvClassDescription.setText(getResources().getString(R.string.classDescription)+" "+entries.get(position).description);
            tvClassName.setText(getResources().getString(R.string.className) + " " + entries.get(position).course);
            tvClassTeacher.setText(getResources().getString(R.string.classTeacher) + " " + entries.get(position).teacher);
            tvClassNotes.setText(getResources().getString(R.string.classNotes) + " " + entries.get(position).notes);
            tvClassPubDate.setText(getResources().getString(R.string.classPubDate) + " " + entries.get(position).pubDate);
        }
    }

    /**
     * This Async task gets the friends that are in the specified course.
     * It receives JSON data which is deciphered and displayed to the user.
     */
    private class getFriendsInCourse extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //String url = "https://dawsondangerousclub2.herokuapp.com/api/api/breakfriends?email=theo@gmail.com&password=dawson&day=1&start=1000&end=1300";
                String url = COURSE_FRIENDS_URL + "email=" + user_email + "&password=" + user_password + "&section=" + section + "&coursename=" + courseName;
                Log.d(TAG, "Url: " + url);
                return fetchFriendsInCourseJSON(url);

            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {

            Log.i(TAG, "Result: " + result);

            if (result.equalsIgnoreCase("Invalid request.") || result.equalsIgnoreCase("Unable to retrieve web page. URL may be invalid.")) {
                Toast.makeText(getContext(), getResources().getString(R.string.input_warning), Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("empty")) {

                Toast.makeText(getContext(), getResources().getString(R.string.no_friends), Toast.LENGTH_LONG).show();

            } else {

                String[] friendsArr = result.split(";");

                for (int x = 0; x <= (friendsArr.length - 1); x++) {
                    String[] nameAndEmail = friendsArr[x].split("#");
                    friends.add(nameAndEmail[0]);
                    friendsEmails.add(nameAndEmail[1]);
                }

                Intent intent = new Intent(getContext(), FindFriendsInCourse.class);
                intent.putStringArrayListExtra("friends", friends);
                intent.putStringArrayListExtra("friendsEmails", friendsEmails);
                startActivity(intent);


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
    private String fetchFriendsInCourseJSON(String aUrl) throws IOException {
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

            JSONArray friends = new JSONArray(friendDataJson);

            if (friends.length() == 0) {
                sb.append("empty");
            } else {
                //parse json text
                for (int friendPosition = 0; friendPosition < friends.length(); friendPosition++) {
                    JSONObject friend = friends.getJSONObject(friendPosition);
                    String friendName = friend.getString("name");
                    String friendEmail = friend.getString("email");
                    sb.append(friendName + "#" + friendEmail + ";");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("JSON data invalid");
        }
        return sb.toString();
    }

}