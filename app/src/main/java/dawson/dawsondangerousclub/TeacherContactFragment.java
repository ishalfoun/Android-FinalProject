package dawson.dawsondangerousclub;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teacher.Teacher;

public class TeacherContactFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_teacher_contact, container, false);
        Teacher teacher = getArguments().getParcelable("teacher");
        TextView fullName = v.findViewById(R.id.nameTextView);
        TextView email = v.findViewById(R.id.emailTextView);
        TextView office = v.findViewById(R.id.officeTextView);
        TextView local = v.findViewById(R.id.localTextView);
        TextView website = v.findViewById(R.id.websiteTextView);
        TextView bio = v.findViewById(R.id.bioTextView);
        fullName.setText(teacher.getFull_name());
        email.setText(teacher.getEmail());
        office.setText(teacher.getOffice());
        local.setText(teacher.getLocal());
        website.setText(teacher.getWebsite());
        bio.setText(teacher.getBio());
        return inflater.inflate(R.layout.activity_teacher_contact, container, false);
    }
}
