package dawson.dawsondangerousclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import dawson.classes.Teacher;

/**
 * Activity for displaying the teacher
 * @author Jacob
 */
public class TeacherContactActivity extends OptionsMenu{
    private final String TAG = "TeacherContactActivity";
    private Teacher teacher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_contact);
        //Extract teacher from parcelable
        this.teacher = getIntent().getExtras().getParcelable("teacher");
        Log.i(TAG,teacher.teacherInfo());

        //Get Elements from XML document by id;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.teacherContactLinearLayout);
        TextView fullName = (TextView)findViewById(R.id.nameTextView);
        TextView email = (TextView)findViewById(R.id.emailTextView);
        TextView office = (TextView)findViewById(R.id.officeTextView);
        TextView local = (TextView)findViewById(R.id.localTextView);
        TextView website = (TextView)findViewById(R.id.websiteTextView);
        TextView bio = (TextView)findViewById(R.id.bioTextView);
        fullName.setText(teacher.getFull_name());

        //set text or remove views if they have no content found from the database it will remove the element
        if (teacher.getEmail().equals("")){
            linearLayout.removeView(email);
        }
        else {
            email.setText(email.getText() + ": " + teacher.getEmail());
        }
        if (teacher.getOffice().equals("")){
            linearLayout.removeView(office);
        }
        else {
            office.setText(office.getText() + ": " + teacher.getOffice());
        }
        if (teacher.getLocal().equals("")){
            linearLayout.removeView(local);
        }
        else{
            local.setText(local.getText() + ": " + teacher.getLocal());
        }
        if (teacher.getWebsite().equals("")){
            linearLayout.removeView(website);
        }
        {
            website.setText(website.getText() + ": " + teacher.getWebsite());
        }
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

    public void makeCall(View view){
        if (!teacher.getLocal().equals("")){
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
            phoneIntent.setData(Uri.parse("tel:5149318731%27" + teacher.getLocal()));
            if(phoneIntent.resolveActivity(getPackageManager())!= null){
                startActivity(phoneIntent);
            }
            else{
                Toast.makeText(TeacherContactActivity.this, getString(R.string.no_phone), Toast.LENGTH_SHORT);
            }
        }
    }
}
