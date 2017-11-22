package dawson.dawsondangerousclub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AcademicCalendarActivity extends AppCompatActivity {

    private WebView webView;
    Spinner spinner;
    RadioGroup radioGroup;

    public static final String FALL_2014 = "https://www.dawsoncollege.qc.ca/registrar/fall-2014-day-division/";
    public static final String FALL_2015 = "https://www.dawsoncollege.qc.ca/registrar/fall-2015-day-division/";
    public static final String FALL_2016 = "https://web.archive.org/web/20170330154343/https://www.dawsoncollege.qc.ca/registrar/fall-2016-day-division/";
    public static final String FALL_2017 = "https://www.dawsoncollege.qc.ca/registrar/fall-2017-day-division/";
    public static final String WINTER_2014 = "https://www.dawsoncollege.qc.ca/registrar/winter-2014-day-division/";
    public static final String WINTER_2015 = "https://www.dawsoncollege.qc.ca/registrar/winter-2015-day-division/";
    public static final String WINTER_2016 = "https://www.dawsoncollege.qc.ca/registrar/winter-2016-day-division/";
    public static final String WINTER_2017 = "https://www.dawsoncollege.qc.ca/registrar/winter-2017-day-division/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_calendar);

        webView = (WebView) findViewById(R.id.academicCalendarWV);
        spinner = (Spinner)findViewById(R.id.academicYrSpinner);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup) ;

        String[] items = new String[]{"2014", "2015", "2016", "2017"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        spinner.setAdapter(adapter);

        //set default values
        spinner.setSelection(3);
        radioGroup.check(R.id.fall);
        //webView.loadUrl(FALL_2017);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //webView.loadUrl("https://stackoverflow.com/questions/33725240/change-webview-url-on-button-tap");


                String season;
                Toast.makeText(getApplicationContext(), "Selection is " + position, Toast.LENGTH_LONG).show();


                //determine which season is selected
                if(radioGroup.getCheckedRadioButtonId()!=R.id.fall){
                    season = "fall";
                }else{
                    season = "winter";
                }
                //open webpage page based year selected
                switch(position){
                    //2014
                    case 0:
                        switch(season){
                            case "fall": webView.loadUrl(FALL_2014);
                                break;
                            case "winter": webView.loadUrl(WINTER_2014);
                                break;
                        }
                        //2015
                    case 1:
                        switch(season){
                            case "fall": webView.loadUrl(FALL_2015);
                                break;
                            case "winter": webView.loadUrl(WINTER_2015);
                                break;
                        }
                        //2016
                    case 2:
                        switch(season){
                            case "fall": webView.loadUrl(FALL_2016);
                                break;
                            case "winter": webView.loadUrl(WINTER_2016);
                                break;
                        }
                        //2017
                    case 3:
                        switch(season){
                            case "fall": webView.loadUrl(FALL_2017);
                                break;
                            case "winter": webView.loadUrl(WINTER_2017);
                                break;
                        }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        });


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
