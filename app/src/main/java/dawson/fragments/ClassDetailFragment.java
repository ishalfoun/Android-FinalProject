package dawson.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                String course = courseSplit[2];
                String section = courseSplit[3];
                Log.d(MYTAG,"long click: "+course+" "+section);


                //INSERT API CODE HERE FOR FRIENDS IN THIS COURSE
                //
                //get friends:
                ArrayList<String> friends = new ArrayList<>();
                friends.add("friend1;email1");
                friends.add("friend2;email2");
                //
                //

                //show friends:
                if (friends.size()>0)
                {
                    Intent intent = new Intent(view.getContext(), FindFriendsInCourse.class);
                    intent.putStringArrayListExtra("friends", friends);
                    startActivity(intent);
                }
                else  //if nothing found:
                {
                    AlertDialog.Builder alertNoFriends = new AlertDialog.Builder(getActivity());
                    alertNoFriends.setMessage("No one available")
                            .setTitle("Friends in this course");
                    alertNoFriends.create().show();
                }
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
}