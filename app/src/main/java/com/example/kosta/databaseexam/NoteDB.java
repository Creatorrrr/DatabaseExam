package com.example.kosta.databaseexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by kosta on 2017-04-24.
 */

public class NoteDB {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    private final Context context;

    private NoteDBHelper dbHelper;
    private SQLiteDatabase myDB;

    public NoteDB(Context context) {
        this.context = context;
    }

    public NoteDB open() {
        dbHelper = NoteDBHelper.getInstance(context);
        myDB = dbHelper.getWritableDatabase();
        return this;
    }

    public long createNote(String title, String body) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_BODY, body);

        return myDB.insert(NoteDBHelper.DATABASE_TABLE, null, values);
    }

    public Cursor fetchAllNotes() {
        return myDB.query(
                NoteDBHelper.DATABASE_TABLE,
                new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY},
                null, null, null, null, null);
    }

    public Cursor fetchNote(long rowId) {
        Cursor cursor = myDB.query(
                NoteDBHelper.DATABASE_TABLE,
                new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY},
                KEY_ROWID + "=" + rowId,
                null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor fetchNotesByTitle(String title) {
        Cursor cursor = myDB.query(
                NoteDBHelper.DATABASE_TABLE,
                new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY},
                KEY_TITLE + " LIKE '%" + title + "%'",
                null,
                null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor fetchNotesByBody(String body) {
        Cursor cursor = myDB.query(
                NoteDBHelper.DATABASE_TABLE,
                new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY},
                KEY_BODY + " LIKE '%" + body + "%'",
                null,
                null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean updateNote(long rowId, String title, String body) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_BODY, body);

        return myDB.update(
                NoteDBHelper.DATABASE_TABLE,
                values,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteNote(long rowId) {
        return myDB.delete(
                NoteDBHelper.DATABASE_TABLE,
                KEY_ROWID + "=" + rowId, null) > 0;
    }
}
