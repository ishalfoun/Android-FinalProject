package dawson.dawsondangerousclub;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.app.DialogFragment;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CalendarActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvStartDate;
    private TextView tvEndDate;

    Calendar currentTime;
    Calendar startTime;
    Calendar endTime;
    boolean is24HourFormat;
    private TextView tvErrors;

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            startTime.set(Calendar.YEAR, year);
            startTime.set(Calendar.MONTH, month);
            startTime.set(Calendar.DAY_OF_MONTH, day);
            updateUI();
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            endTime.set(Calendar.YEAR, year);
            endTime.set(Calendar.MONTH, month);
            endTime.set(Calendar.DAY_OF_MONTH, day);
            updateUI();
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            startTime.set(Calendar.HOUR, hour);
            startTime.set(Calendar.MINUTE, minute);
            updateUI();
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            endTime.set(Calendar.HOUR, hour);
            endTime.set(Calendar.MINUTE, minute);
            updateUI();
        }
    };

    public void updateUI() {
        tvStartTime.setText(startTime.get(Calendar.HOUR) + ":" + startTime.get(Calendar.MINUTE));
        tvStartDate.setText(startTime.get(Calendar.DAY_OF_MONTH) + "/" + startTime.get(Calendar.MONTH) + "/" + startTime.get(Calendar.YEAR));
        tvEndTime.setText(endTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.MINUTE));
        tvEndDate.setText(endTime.get(Calendar.DAY_OF_MONTH) + "/" + endTime.get(Calendar.MONTH) + "/" + endTime.get(Calendar.YEAR));
    }
    public void clearUI()
    {
        tvStartTime.setText("");
        tvStartDate.setText("");
        tvEndTime.setText("");
        tvEndDate.setText("");
        title.setText("");
        description.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        title = (EditText) findViewById(R.id.titleET);
        description = (EditText) findViewById(R.id.descriptionET);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvErrors = (TextView) findViewById(R.id.errors);


        currentTime = Calendar.getInstance();
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();

        updateUI();
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        is24HourFormat= dateFormat.is24HourFormat(this);
    }

    public void onClickStartDate(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, startDateListener, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void onClickEndDate(View v) {
        DatePickerDialog dialog = new DatePickerDialog(this, endDateListener,  currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void onClickStartTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, startTimeListener, currentTime.get(Calendar.HOUR), currentTime.get(Calendar.MINUTE), is24HourFormat);
        dialog.show();
    }

    public void onClickEndTime(View v) {
        TimePickerDialog dialog = new TimePickerDialog(this, endTimeListener, currentTime.get(Calendar.HOUR), currentTime.get(Calendar.MINUTE), is24HourFormat);
        dialog.show();
    }


    public void onSubmit(View v) {

        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE));
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE));
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, title.getText().toString());
        values.put(Events.DESCRIPTION, description.getText().toString());
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            tvErrors.setText("You do not have permission to add events to the calendar");
            return;
        }

        Uri uri = cr.insert(Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        tvErrors.setText("Event Added Successfully. Id="+eventID);
        clearUI();
    }

}
