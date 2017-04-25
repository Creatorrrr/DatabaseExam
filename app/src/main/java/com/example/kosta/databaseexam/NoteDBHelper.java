package com.example.kosta.databaseexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kosta on 2017-04-24.
 */

public class NoteDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_TABLE = "notes";

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "CREATE TABLE notes(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "body TEXT NOT NULL);";

    private static NoteDBHelper ndb;

    private NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static NoteDBHelper getInstance(Context context) {
        if(ndb == null) {
            ndb = new NoteDBHelper(context);
        }
        return ndb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}
