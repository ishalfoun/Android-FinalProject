package dawson.dawsondangerousclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dawson.classes.Teacher;

public class FindTeacherActivity extends AppCompatActivity {
    private EditText firstNameTextView;
    private EditText lastNameTextView;
    private CheckBox exactCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_teacher);
        firstNameTextView = (EditText)findViewById(R.id.firstNameEditText);
        lastNameTextView = (EditText)findViewById(R.id.lastNameEditText);
        exactCheckBox = (CheckBox)findViewById(R.id.exactCheckBox);

        if (savedInstanceState == null)
        {
            String firstname = getIntent().getStringExtra("firstname");
            String lastname = getIntent().getStringExtra("lastname");
            firstNameTextView.setText(firstname);
            lastNameTextView.setText(lastname);
        }
    }

    public void findTeacher(View view) {
        String firstName = firstNameTextView.getText().toString();
        String lastName = lastNameTextView.getText().toString();
        boolean exact = exactCheckBox.isChecked();
        Query foundTeachers;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        //IF NO INPUTS
        if ((firstName.equals("") || firstName.equals(getString(R.string.firstname))) && (lastName.equals("") || lastName.equals(getString(R.string.lastname)))) {
            Toast.makeText(FindTeacherActivity.this, R.string.no_teachers_input, Toast.LENGTH_SHORT).show();
        } else {
            //SEARCH BY FIRST NAME
            if ((lastName.equals("") || lastName.equals(getString(R.string.lastname)))) {
                foundTeachers = rootRef.orderByChild("first_name").equalTo(firstName);
            }
            ////SEARCH BY LAST NAME
            else if(firstName.equals("") || firstName.equals(getString(R.string.lastname))){
                foundTeachers = rootRef.orderByChild("last_name").equalTo(lastName);
            }
            //SEARCH BY FULL NAME
            else {
                foundTeachers = rootRef.orderByChild("full_name").equalTo(firstName + " " + lastName);
            }
            foundTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<Teacher> teachers = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            teachers.add(snapshot.getValue(Teacher.class));
                        }
                        if (teachers.size() > 1) {
                        Intent intent = new Intent(FindTeacherActivity.this, ChoseTeacherActivity.class);
                        intent.putParcelableArrayListExtra("teachers",(ArrayList)teachers);
                        startActivity(intent);
                        } else {
                            Intent intent = new Intent(FindTeacherActivity.this, TeacherContactActivity.class);
                            intent.putExtra("teacher",teachers.get(0));
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(FindTeacherActivity.this, R.string.no_teacher_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}