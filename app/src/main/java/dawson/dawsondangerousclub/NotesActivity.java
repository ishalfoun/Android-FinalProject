package dawson.dawsondangerousclub;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import notes.Note;
import notes.NotesDatabaseHelper;
import utilities.ListViewNoteAdapter;

public class NotesActivity extends OptionsMenu {
    private NotesDatabaseHelper database;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        database = new NotesDatabaseHelper(this);
        loadList();
    }


    private void loadList() {
        Cursor notes = database.getNotes();
        noteList = new ArrayList<>();
        if (notes.getCount() > 0) {
            while (notes.moveToNext()) {
                noteList.add(new Note(notes.getInt(0), notes.getString(1)));
            }
        }
        ListViewNoteAdapter adapter = new ListViewNoteAdapter(this, noteList);
        ListView list = (ListView) this.findViewById(R.id.notesListView);
        list.setAdapter(adapter);
    }

    public void onDelete() {
        loadList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }
}
