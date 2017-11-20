package dawson.dawsondangerousclub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // remove about because we don't need it here
        menu.removeItem(R.id.about);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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

    public void displayAuthorDetails(View v) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getString(R.string.about_me_dialog));

        switch (v.getId()) {
            case R.id.issak:
                alertDialog.setMessage(getResources().getString(R.string.about_me_dialog));
                break;
            case R.id.jacob:
                alertDialog.setMessage(getResources().getString(R.string.about_me_dialog));
                break;
            case R.id.theo:
                alertDialog.setMessage(getResources().getString(R.string.about_me_dialog));
                break;
        }

        alertDialog.show();
    }

    public void openDawsonWebpage(View v){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        startActivity(i);
    }
}
