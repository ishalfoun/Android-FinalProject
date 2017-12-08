package dawson.dawsondangerousclub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends OptionsMenu {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText pw;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firstName = (EditText) findViewById(R.id.firstNameET);
        lastName = (EditText) findViewById(R.id.lastNameET);
        email = (EditText) findViewById(R.id.emailET);
        pw = (EditText) findViewById(R.id.pwET);

        prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        firstName.setText(prefs.getString("firstname", "Jaya"));
        lastName.setText(prefs.getString("lastname", "Patricia"));
        email.setText(prefs.getString("email", "theo@gmail.com"));
        pw.setText(prefs.getString("pw", "dawson"));
    }

    public void onSubmit(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (email.getText().toString().matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"))//
        {
            if (pw.getText().toString().length()>5) {
                prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("firstname", firstName.getText().toString());
                editor.putString("lastname", lastName.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("pw", pw.getText().toString());
                editor.putString("stamp", Calendar.getInstance().getTime().toString());
                editor.commit();
                this.finish();
            }
            else
            {
                builder.setMessage(R.string.settings_pw_short)
                        .setTitle(R.string.settings_title);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else
        {
            builder.setMessage(R.string.settings_invalid_email)
                    .setTitle(R.string.settings_title);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    //launch a dialog to confirm or discard
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.settings_message)
                .setTitle(R.string.settings_title);

        builder.setPositiveButton(R.string.settings_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onSubmit(null);
            }
        });
        builder.setNegativeButton(R.string.settings_discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.removeItem(R.id.settings);
        return true;
    }

}
