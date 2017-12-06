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

public class FindFriendsInCourse extends AppCompatActivity {

    ArrayList<String> friends;
    ArrayList<String> friendsOnlyNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_in_course);

        friends = getIntent().getStringArrayListExtra("friends");

        friendsOnlyNames = new ArrayList<String>();
        for (String friend : friends) {
            friendsOnlyNames.add(friend.split(";")[0]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.activity_find_friends_in_course, R.id.FriendTxtView, friendsOnlyNames);

        ListView lvItems = new ListView(this);
        setContentView(lvItems);
        lvItems.setAdapter(adapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, friends.get(i).split(";")[1]);
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.about_app_title);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello your class got cancelled, let's study together");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }
}