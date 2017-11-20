package dawson.dawsondangerousclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchCancellations(){
        Intent cancellations = new Intent(MainActivity.this,CancelledClassesActivity.class);
        startActivity(cancellations);
    }

    public void launchTeachers(){

    }

    public void launchCalendar(){

    }

    public void launchNotes(){

    }

    public void launchWeather(){

    }

    public void launchAcademicCalendar(){

    }

    public void launchAbout(){

    }

    public void launchDawson(){

    }

}
