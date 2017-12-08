package dawson.dawsondangerousclub;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dawson.classes.NotesAdapter;
import dawson.classes.Note;
import dawson.classes.NotesDatabaseHelper;

public class NotesActivity extends OptionsMenu {
    private NotesDatabaseHelper database;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        database = new NotesDatabaseHelper(this);
        loadList();

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewNote();
            }
        });

    }

    private void loadList() {
        Cursor notes = database.getNotes();
        noteList = new ArrayList<>();
        if (notes.getCount() > 0) {
            while (notes.moveToNext()) {
                noteList.add(new Note(notes.getInt(0), notes.getString(1)));
            }
        }
        NotesAdapter adapter = new NotesAdapter(this, noteList);
        ListView list = (ListView) this.findViewById(R.id.notesListView);
        list.setAdapter(adapter);
    }

    public void createNewNote(){
        final EditText editText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_note_title)
                .setView(editText)
                .setPositiveButton(R.string.create_note, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(database.insertNote(String.valueOf(editText.getText()))){
                            Toast.makeText(NotesActivity.this,R.string.note_added,Toast.LENGTH_SHORT).show();
                            loadList();
                        }
                        else{
                            Toast.makeText(NotesActivity.this,R.string.note_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.show();
    }

    public void onDelete(){
        loadList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }
}
