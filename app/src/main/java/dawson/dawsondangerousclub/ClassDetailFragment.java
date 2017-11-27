package dawson.dawsondangerousclub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dawson.dawsondangerousclub.R;


public class ClassDetailFragment extends Fragment {
    int position = 0;
	
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

        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.activity_class, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(savedInstanceState == null){
            // Get back arguments
            if(getArguments() != null) {
                position = getArguments().getInt("position", 0);
                entries = getArguments().getParcelableArrayList("entries");
            }
        }

        Log.d("MYTAG",  "got the entries in detailfrag: "+" "+ (entries != null ? entries.get(0).title : "empty"));

		  tvClassTitle = (TextView) view.findViewById(R.id.classTitle);
		  tvClassDescription = (TextView) view.findViewById(R.id.classDescription);
		  tvClassName = (TextView) view.findViewById(R.id.className);
		  tvClassTeacher = (TextView) view.findViewById(R.id.classTeacher);
		  tvClassNotes = (TextView) view.findViewById(R.id.classNotes);
		  tvClassPubDate = (TextView) view.findViewById(R.id.classPubDate);

        updateView();
		
    }

    // Activity is calling this to update view on Fragment
    public void updateView(){
        tvClassTitle.setText(getResources().getString(R.string.classTitle)+" "+entries.get(position).title);
//        tvClassDescription.setText(getResources().getString(R.string.classDescription)+" "+entries.get(position).description);
        tvClassName.setText(getResources().getString(R.string.className)+" "+entries.get(position).course);
        tvClassTeacher.setText(getResources().getString(R.string.classTeacher)+" "+entries.get(position).teacher);
        tvClassNotes.setText(getResources().getString(R.string.classNotes)+" "+entries.get(position).notes);
        tvClassPubDate.setText(getResources().getString(R.string.classPubDate)+" "+entries.get(position).pubDate);
    }
}