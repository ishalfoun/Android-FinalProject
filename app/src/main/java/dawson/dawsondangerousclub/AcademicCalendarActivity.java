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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity for displaying the academic calendar from the dawson website.
 */
public class AcademicCalendarActivity extends OptionsMenu  {

    private final String TAG = "AcademicCalendarActivity";
    private WebView webView;
    Spinner spinner;
    RadioGroup radioGroup;
    boolean firstStart = true;

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
        spinner = (Spinner) findViewById(R.id.academicYrSpinner);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        String[] items = new String[]{"2014", "2015", "2016", "2017"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        spinner.setAdapter(adapter);

        //set default values
        spinner.setSelection(3);
        radioGroup.check(R.id.fall);
        webView.loadUrl(FALL_2017);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                if (firstStart) {
                    //default setup, do nothing
                    firstStart = false;
                } else {

                    //default season
                    boolean fallSeason = true;

                    //determine which season is selected
                    if (radioGroup.getCheckedRadioButtonId() == R.id.fall) {
                        fallSeason = true;
                    } else {
                        fallSeason = false;
                    }

                    //open web page page based year selected
                    switch (position) {
                        //2014
                        case 0:
                            if (fallSeason) {
                                webView.loadUrl(FALL_2014);
                            } else {
                                webView.loadUrl(WINTER_2014);
                            }
                            break;
                        //2015
                        case 1:
                            if (fallSeason) {
                                webView.loadUrl(FALL_2015);
                            } else {
                                webView.loadUrl(WINTER_2015);
                            }
                            break;
                        //2016
                        case 2:
                            if (fallSeason) {
                                webView.loadUrl(FALL_2016);
                            } else {
                                webView.loadUrl(WINTER_2016);
                            }
                            break;
                        //2017
                        case 3:
                            if (fallSeason) {
                                webView.loadUrl(FALL_2017);
                            } else {
                                webView.loadUrl(WINTER_2017);
                            }
                            break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String year = spinner.getSelectedItem().toString();

                //default season
                boolean fallSeason = true;

                if (checkedId == R.id.fall) {fallSeason = true;} else{fallSeason = false;}

                //open web page page based year selected
                switch (year) {
                    //2014
                    case "2014":
                        if (fallSeason) {
                            webView.loadUrl(FALL_2014);
                        } else {
                            webView.loadUrl(WINTER_2014);
                        }
                        break;
                    //2015
                    case "2015":
                        if (fallSeason) {
                            webView.loadUrl(FALL_2015);
                        } else {
                            webView.loadUrl(WINTER_2015);
                        }
                        break;
                    //2016
                    case "2016":
                        if (fallSeason) {
                            webView.loadUrl(FALL_2016);
                        } else {
                            webView.loadUrl(WINTER_2016);
                        }
                        break;
                    //2017
                    case "2017":
                        if (fallSeason) {
                            webView.loadUrl(FALL_2017);
                        } else {
                            webView.loadUrl(WINTER_2017);
                        }
                        break;
                }

            }


        });
    }


}
