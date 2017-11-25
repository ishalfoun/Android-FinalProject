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

public class NotesActivity extends AppCompatActivity {
    private NotesDatabaseHelper database;
    private List<Note> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        database = new NotesDatabaseHelper(this);
        loadList();
    }



    private void loadList(){
    Cursor notes = database.getNotes();
    noteList = new ArrayList<>();
    if (notes.getCount() > 0){
        while (notes.moveToNext()){
            noteList.add(new Note(notes.getInt(0),notes.getString(1)));
        }
    }
    ListViewNoteAdapter adapter = new ListViewNoteAdapter(this,noteList);
    ListView list = (ListView) this.findViewById(R.id.notesListView);
    list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNoteMenuItem:
                createNewNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewNote(){
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
}
