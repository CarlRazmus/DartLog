package com.fraz.dartlog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by filip on 2016-05-05.
 */
public class DartLogDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DartLog.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DartLogContract.PlayerEntry.TABLE_NAME + " (" +
                    DartLogContract.PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                    DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME + " TEXT )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DartLogContract.PlayerEntry.TABLE_NAME;

    public DartLogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);

        addPlayer("Filip");
        addPlayer("Razmus");
        addPlayer("Jonathan");
        addPlayer("Martin");
    }

    public void addPlayer(String name) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME, name);
        db.insert(DartLogContract.PlayerEntry.TABLE_NAME, null, values);
    }

    public void addPlayers(ArrayList<String> names) {
        for (String name : names) {
            addPlayer(name);
        }
    }

    public ArrayList<String> getPlayers() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME
        };
        Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME, projection,
                null, null, null, null, null);

        ArrayList<String> names = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                names.add(c.getString(c.getColumnIndex(
                        DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME)));
            }
        } finally {
            c.close();
        }
        return names;
    }
}
