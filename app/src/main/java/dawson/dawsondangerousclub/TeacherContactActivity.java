package dawson.dawsondangerousclub;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import dawson.classes.Teacher;

public class TeacherContactActivity extends OptionsMenu{
    private final String TAG = "TeacherContactActivity";
    private Teacher teacher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_contact);
        this.teacher = getIntent().getExtras().getParcelable("teacher");
        Log.i(TAG,teacher.teacherInfo());
        TextView fullName = (TextView)findViewById(R.id.nameTextView);
        TextView email = (TextView)findViewById(R.id.emailTextView);
        TextView office = (TextView)findViewById(R.id.officeTextView);
        TextView local = (TextView)findViewById(R.id.localTextView);
        TextView website = (TextView)findViewById(R.id.websiteTextView);
        TextView bio = (TextView)findViewById(R.id.bioTextView);
        fullName.setText(teacher.getFull_name());
        email.setText(teacher.getEmail());
        office.setText(teacher.getOffice());
        local.setText(teacher.getLocal());
        website.setText(teacher.getWebsite());
        bio.setText(teacher.getBio());
    }
}
