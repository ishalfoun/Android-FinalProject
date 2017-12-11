package dawson.classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dawson.classes.Note;
import dawson.classes.NotesDatabaseHelper;
import dawson.dawsondangerousclub.ItemNoteActivity;
import dawson.dawsondangerousclub.NotesActivity;
import dawson.dawsondangerousclub.R;

/**
 * Adapter used for notes.
 * @author Jacob
 */

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Note> elements;

    public NotesAdapter(Context context, List<Note> elements) {
        this.context = context;
        this.elements = elements;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.listelement_notes,null);
        TextView textView = (TextView)rowView.findViewById(R.id.noteListTextView);
        textView.setText(elements.get(i).getNoteShort());
        ImageButton imageBttn =  (ImageButton) rowView.findViewById(R.id.add_noteImgBttn);
        //imageBttn.setId(elements.get(i).getId());
        final int ID = elements.get(i).getId();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemNoteActivity.class);
                intent.putExtra("note",ID);
                context.startActivity(intent);
            }
        });
        imageBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesDatabaseHelper database = new NotesDatabaseHelper(context);
                database.deleteNote(ID);
                NotesActivity activity = (NotesActivity)context;
                activity.onDelete();
            }
        });
        return rowView;
    }
}
