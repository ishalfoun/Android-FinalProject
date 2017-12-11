package dawson.dawsondangerousclub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 *  Activity displayed when selecting 'About' from the options menu
 *  Displays information about the app and the developers.
 *  @author Isaak, Theodore 
 */
public class AboutActivity extends OptionsMenu {
	private final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.removeItem(R.id.about);
        return true;
    }

    public void displayAuthorDetails(View v) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getString(R.string.about_me_dialog));

        switch (v.getId()) {
            case R.id.issak:
                alertDialog.setMessage(getResources().getString(R.string.about_me_issak));
                break;
            case R.id.jacob:
                alertDialog.setMessage(getResources().getString(R.string.about_me_jacob));
                break;
            case R.id.theo:
                alertDialog.setMessage(getResources().getString(R.string.about_me_theo));
                break;
        }

        alertDialog.show();
    }

    public void openDawsonWebPage(View v){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        startActivity(i);
    }
}
