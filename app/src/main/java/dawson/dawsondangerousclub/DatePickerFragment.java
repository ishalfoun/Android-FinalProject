package dawson.dawsondangerousclub;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {

    private TextView tv;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        if(savedInstanceState == null){
            // Get back arguments
            if(getArguments() != null) {
                tv = (TextView) getActivity().findViewById(getArguments().getInt("textview", 0));
            }
        }

DatePickerDialog d = new DatePickerDialog(getActivity(),   (DatePickerDialog.OnDateSetListener)
        getActivity(), year, month, day);

        return d;

    }


}