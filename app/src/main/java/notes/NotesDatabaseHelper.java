package notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by 1537385 on 11/20/2017.
 */

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private final String DATABASE_NAME = "notes.db";
    private final String TABLE_NAME =  "notes";
    private final int VERSION = 1;


    public NotesDatabaseHelper(Context context){
        super(context, "notes.db",null ,1);
    }

    public boolean insertNote(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("NOTE",note);
        if (db.insert(TABLE_NAME,null,content) == -1){
            return false;
        }
        return true;
    }

    public void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM notes WHERE ID = ?";
        SQLiteStatement stmnt = db.compileStatement(sql);
        stmnt.bindString(1,id + "");
        stmnt.execute();
    }

    public Cursor getNotes(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME,null);
        return cursor;
    }

    public Note getNoteById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " WHERE ID = " + id,null);
        cursor.moveToNext();
        return new Note(cursor.getInt(0),cursor.getString(1));
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists notes (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT NOT NULL DEFAULT ' ')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
