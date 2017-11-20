package dawson.dawsondangerousclub;

import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //open About app activity
            case R.id.about:
                Intent openAbout = new Intent(getApplicationContext(),
                        AboutActivity.class);
                startActivity(openAbout);
                return true;
            //Launches Dawson Computer Science web page
            case R.id.dawson:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
                startActivity(i);
                return true;
            //Open last viewed quote
            case R.id.settings:
                //open settings activity
                Intent openSettings = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(openSettings);
                return true;
            default:
                return false;
        }

    }

}
