package dawson.dawsondangerousclub;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WhoIsFreeActivity extends AppCompatActivity {

    ArrayList<String> daysOffTheWeek;
    Spinner daySelector;
    ListView friendLV;
    Calendar currentTime;
    Calendar startTime;
    Calendar endTime;
    private TextView tvStartTime;
    private TextView tvEndTime;
    //final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    final SimpleDateFormat apiTimeFormat = new SimpleDateFormat("HHmm");
    final SimpleDateFormat uiTimeFormat = new SimpleDateFormat("h:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_is_free);

        daySelector = (Spinner)findViewById(R.id.daySpinner);
        friendLV =(ListView)findViewById(R.id.friendLV);
        tvStartTime = (TextView) findViewById(R.id.startTimeTV);
        tvEndTime = (TextView) findViewById(R.id.endTimeTV);


        daysOffTheWeek = new ArrayList<String>();
        daysOffTheWeek.add(getResources().getString(R.string.monday));
        daysOffTheWeek.add(getResources().getString(R.string.tuesday));
        daysOffTheWeek.add(getResources().getString(R.string.wednesday));
        daysOffTheWeek.add(getResources().getString(R.string.thursday));
        daysOffTheWeek.add(getResources().getString(R.string.friday));

        //attach countries to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, daysOffTheWeek);
        daySelector.setAdapter(adapter);

        //set defaults
        daySelector.setSelection(0);

        currentTime = Calendar.getInstance();
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
    }




    public void onClickStartTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, startTimeListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false);
        dialog.show();
    }

    public void onClickEndTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, endTimeListener, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false);
        dialog.show();
    }

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            tvStartTime.setText(uiTimeFormat.format(startTime.getTime()));
            //Toast.makeText(getApplicationContext(), apiTimeFormat.format(startTime.getTime()), Toast.LENGTH_LONG).show();

        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            endTime.set(Calendar.HOUR_OF_DAY, hour);
            endTime.set(Calendar.MINUTE, minute);
            tvEndTime.setText(uiTimeFormat.format(endTime.getTime()));
            //Toast.makeText(getApplicationContext(), apiTimeFormat.format(endTime.getTime()), Toast.LENGTH_LONG).show();
        }
    };


}
