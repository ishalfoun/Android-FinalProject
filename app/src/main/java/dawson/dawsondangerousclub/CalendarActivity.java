package dawson.dawsondangerousclub;

import android.Manifest;
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
import java.util.Calendar;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class CalendarActivity extends AppCompatActivity {

    Button setStart;
    Button setEnd;
	private EditText title;
	private EditText description;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        title = (EditText) findViewById(R.id.titleET);
        description = (EditText) findViewById(R.id.descriptionET);
        setStart = (Button) findViewById(R.id.buttonStart);
        setEnd = (Button) findViewById(R.id.buttonEnd);

    }

    public void onClickStart(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }


    public void onClickEnd(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }

    public void handleAdd(View v) {

//        int hourstart = timePickerstart.getCurrentHour();
//        int minstart = timePickerstart.getCurrentMinute();
//        int hourend = timePickerend.getCurrentHour();
//        int minend = timePickerend.getCurrentMinute();

//        Log.d("MYTAG", "start="+hourstart+":"+minstart+" , end="+hourend+":"+minend);
		
//
//        long calID = 1;
//        long startMillis = 0;
//        long endMillis = 0;
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2012, 9, 14, 7, 30);
//        startMillis = beginTime.getTimeInMillis();
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2012, 9, 14, 8, 45);
//        endMillis = endTime.getTimeInMillis();
//
//        ContentResolver cr = getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(Events.DTSTART, startMillis);
//        values.put(Events.DTEND, endMillis);
//        values.put(Events.TITLE, title.getText());
//        values.put(Events.DESCRIPTION, description.getText());
//        values.put(Events.CALENDAR_ID, calID);
//        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Uri uri = cr.insert(Events.CONTENT_URI, values);
    }
}
