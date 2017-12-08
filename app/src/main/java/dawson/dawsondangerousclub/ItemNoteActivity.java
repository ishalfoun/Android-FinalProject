package dawson.dawsondangerousclub;

import android.os.Bundle;
import android.widget.TextView;

import dawson.classes.NotesDatabaseHelper;

public class ItemNoteActivity extends OptionsMenu {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_note);
        NotesDatabaseHelper database = new NotesDatabaseHelper(this);
        int note = getIntent().getExtras().getInt("note");
        TextView textView = (TextView)findViewById(R.id.noteFullTextView);
        textView.setText(database.getNoteById(note).getNote());
    }
}
