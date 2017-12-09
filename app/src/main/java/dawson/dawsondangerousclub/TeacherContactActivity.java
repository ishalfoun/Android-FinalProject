package dawson.dawsondangerousclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    public void sendEmail(View view){
        if (!teacher.getEmail().equals("")) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{teacher.getEmail()});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            try {
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(TeacherContactActivity.this, getString(R.string.no_mail_client), Toast.LENGTH_SHORT);
            }
        }
    }
}
