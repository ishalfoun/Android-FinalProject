package dawson.dawsondangerousclub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import dawson.dawsondangerousclub.ClassAdapter;
import dawson.dawsondangerousclub.CancelledClassessActivity;
import dawson.dawsondangerousclub.R;

public class ClassMenuFragment extends Fragment {

    ClassAdapter adapter;
    String [] listClassTitle;
    String [] listClassDescription;
    String [] listClassName;
    String [] listClassTeacher;
    String [] listClassNotes;
    String [] listClassPubDate;
    private OnItemSelectedListener listener;

    ArrayList<Entry> entries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            // Get back arguments
            if(getArguments() != null) {
                entries = getArguments().getParcelableArrayList("entries");
            }
        }
        //listClassTitle = entries.toArray(new String[entries.size()]);

        Log.d("MYTAG",  "got the entries in menufrag: "+ (entries != null ? entries.get(0).title : "empty"));

//		//TODO: need to insert array of classes here
//		listClassTitle =  getResources().getStringArray(R.array.classList);
//		listClassDescription =  getResources().getStringArray(R.array.classList);
//		listClassName =  getResources().getStringArray(R.array.classList);
//		listClassTeacher =  getResources().getStringArray(R.array.classList);
//		listClassNotes =  getResources().getStringArray(R.array.classList);
//		listClassPubDate =  getResources().getStringArray(R.array.classList);
//
//        adapter = new ClassAdapter(getContext(), listClassTitle, listClassDescription,
//						listClassName, listClassTeacher, listClassNotes,
//						listClassPubDate, listener);
        adapter = new ClassAdapter(getContext(), entries, listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_class_menu, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ListView lvItems = (ListView) view.findViewById(R.id.listView);
        lvItems.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ClassMenuFragment.OnItemSelectedListener");
        }
    }

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onClassItemSelected(int position);
    }
}