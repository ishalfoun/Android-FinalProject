package dawson.dawsondangerousclub;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_in_course);

        friends = getIntent().getStringArrayListExtra("friends");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.activity_find_friends_in_course, R.id.FriendTxtView, friends);

        ListView lvItems = new ListView(this);
        setContentView(lvItems);
        lvItems.setAdapter(adapter);
    }
}
