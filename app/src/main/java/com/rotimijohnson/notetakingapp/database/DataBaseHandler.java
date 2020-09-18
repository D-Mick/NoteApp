package com.rotimijohnson.notetakingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rotimijohnson.notetakingapp.model.Note;
import com.rotimijohnson.notetakingapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NoteTakingApp";
    private static final String TABLE_NAME = "NoteTaking";

    // Coloumn Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String username = "username";
    private static final String password = "password";
    private static final String fullname = "fullname";

    // Coloumn Combinations
    private static final String[] COLS_ID_TITLE_NOTE = new String[] {KEY_ID,KEY_TITLE,KEY_NOTE};


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+", "
                + KEY_TITLE + " TEXT"+ ", "
                + KEY_NOTE + " TEXT" + ", "
                + username + " TEXT" + ", "
                + password + " TEXT" + ", "
                + fullname + " TEXT "
                + ")";

        Log.d(TAG,CREATE_NOTES_TABLE);

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        Log.d(TAG,DROP_TABLE);

        db.execSQL(DROP_TABLE);

    }

    //CRUD OPERATIONS

    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_NOTE, note.getNote());


        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        db.close();

        Log.d(TAG,"Get Note Result "+ c.getString(0)+","+c.getString(1)+","+c.getString(2));
        Note note = new Note(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(2));
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,null,null,null,null,null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                noteList.add(note);

            }while (cursor.moveToNext());


        }
        db.close();
        return noteList;

    }

    public int getNotesCount(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE,null,null,null,null,null);
        return cursor.getCount();
    }

    public void deleteGrocery(int id ){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateNote(Note note){
        SQLiteDatabase db =this.getReadableDatabase();

        ContentValues values =new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_NOTE, note.getNote());

        return  db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
    }


    //inserting into database
    public boolean insert(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long ins = db.insert("NoteTaking", null,values);
        if (ins == -1) return false;
        else return true;
    }

    //checking if email exists
    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from NoteTaking where username=?", new String[]{username});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    //checking the email and password during login
    public Boolean usernamePassword(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from NoteTaking where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0 )return true;
        else return false;
    }


    public  void deleteAllItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{KEY_ID});
        db.close();
    }

    //new database instructions
    public boolean create(User user){
        boolean result = true;
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(username, user.getUsername());
            contentValues.put(password, user.getPassword());
            contentValues.put(fullname, user.getFullname());

            result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues) > 0;
        }catch (Exception e){
            result = false;
        }
        return result;
    }

    public User login(String username,String password){
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from NoteTaking where username = ? and password = ?",
                    new String[]{username,password});
            if (cursor.moveToFirst()){
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullname(cursor.getString(3));
            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }


    public User checkUn(String username){
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from NoteTaking where username = ?",
                    new String[]{username});
            if (cursor.moveToFirst()){
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setFullname(cursor.getString(3));
            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }

    public boolean update(User user){
        boolean result = true;
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(username, user.getUsername());
            contentValues.put(password, user.getPassword());

            result = sqLiteDatabase.update(TABLE_NAME,contentValues,KEY_ID + " = ?",
                    new String[] {String.valueOf(user.getId())}) > 0;
        }catch (Exception e){
            result = false;
        }
        return result;
    }

    public User find(int id){
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from NoteTaking where username = ?",
                    new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()){
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
            }
        }catch (Exception e){
            user = null;
        }
        return user;
    }
}
