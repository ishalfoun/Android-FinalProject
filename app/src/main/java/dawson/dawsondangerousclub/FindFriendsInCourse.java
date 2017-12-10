package dawson.dawsondangerousclub;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying the friends in a certain course, retrieved through the API
 * @author Theo
 */
public class FindFriendsInCourse extends AppCompatActivity {
	private final String TAG = "FindFriendsInCourse";

    ArrayList<String> friends;
    ArrayList<String> friendsOnlyNames;
    ArrayList<String> friendsEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_in_course);

        friends = getIntent().getStringArrayListExtra("friends");
        friendsEmails = getIntent().getStringArrayListExtra("friendsEmails");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.activity_find_friends_in_course, R.id.FriendTxtView, friends);

        ListView lvItems = new ListView(this);
        setContentView(lvItems);
        lvItems.setAdapter(adapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, friendsEmails.get(i));
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello your class got cancelled, let's study together");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }
}
