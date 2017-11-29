package dawson.dawsondangerousclub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teacher.Teacher;

public class ChoseTeacherActivity extends AppCompatActivity {
    private List<Teacher> teachers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_teacher);
        if (getIntent().hasExtra("teachers")) {
            teachers = getIntent().getExtras().getParcelableArrayList("teachers");
        }
        loadList();
    }

    private void loadList(){
    List<String> fullnames = new ArrayList<>();
    for (Teacher t : teachers){
        fullnames.add(t.getFull_name());
    }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,fullnames);
        ListView listView = this.findViewById(R.id.teacherListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickTeacher);
        adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener onClickTeacher = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            TeacherContactFragment teacherContact = new TeacherContactFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("teacher",teachers.get(position));
//            teacherContact.setArguments(bundle);
//            transaction.add(R.id.teacherListFragment,teacherContact);
//            transaction.addToBackStack(null);
//            transaction.commit();
        }
    };
}
