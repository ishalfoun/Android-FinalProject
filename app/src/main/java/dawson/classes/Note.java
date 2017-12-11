package dawson.classes;

/**
 * Bean used for Notes.
 * @author Jacob
 */
public class Note {
    private int id;
    private String note;

    public Note(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteShort(){
        if (note.length() > 25){
            return note.substring(0,24);
        }
        return note;
    }
}
