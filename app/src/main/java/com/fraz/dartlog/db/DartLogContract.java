package com.fraz.dartlog.db;

import android.provider.BaseColumns;

final class DartLogContract {


    static final String[] SQL_CREATE_ENTRIES = new String[]{
        "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                PlayerEntry.COLUMN_NAME_PLAYER_NAME + " TEXT UNIQUE)",
        "CREATE TABLE " + X01Entry.TABLE_NAME + " (" +
                X01Entry._ID + " INTEGER PRIMARY KEY," +
                X01Entry.COLUMN_NAME_DATE + " INTEGER," +
                X01Entry.COLUMN_NAME_X + " INTEGER," +
                X01Entry.COLUMN_NAME_WINNER_PLAYER_ID + " INTEGER," +
                "FOREIGN KEY(" + X01Entry.COLUMN_NAME_X + ")" +
                    " REFERENCES " + PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + ")," +
                "FOREIGN KEY(" + X01Entry.COLUMN_NAME_WINNER_PLAYER_ID + ")" +
                    " REFERENCES " + PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))",
        "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                ScoreEntry.COLUMN_NAME_MATCH_ID + " INTEGER," +
                ScoreEntry.COLUMN_NAME_PLAYER_ID + " INTEGER," +
                ScoreEntry.COLUMN_NAME_SCORE + " INTEGER," +
                "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_MATCH_ID + ")" + " REFERENCES " +
                    X01Entry.TABLE_NAME + "(" + X01Entry._ID + ")," +
                "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_PLAYER_ID + ")" + " REFERENCES " +
                    PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))"};

    static final String[] SQL_DELETE_ENTRIES = new String[]{
        "DROP TABLE IF EXISTS " + DartLogContract.PlayerEntry.TABLE_NAME,
        "DROP TABLE IF EXISTS " + X01Entry.TABLE_NAME,
        "DROP TABLE IF EXISTS " + DartLogContract.ScoreEntry.TABLE_NAME};


    public DartLogContract() {}

    /* Inner class that defines the table contents */
    static abstract class PlayerEntry implements BaseColumns {
        static final String TABLE_NAME = "player";
        static final String COLUMN_NAME_PLAYER_NAME = "name";
    }

    static abstract class X01Entry implements BaseColumns {
        static final String TABLE_NAME = "x01";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_WINNER_PLAYER_ID = "winner_id";
        static final String COLUMN_NAME_X = "x";
    }

    static abstract class ScoreEntry implements BaseColumns {
        static final String TABLE_NAME = "match_score";
        static final String COLUMN_NAME_PLAYER_ID = "player_id";
        static final String COLUMN_NAME_MATCH_ID = "match_id";
        static final String COLUMN_NAME_SCORE = "score";
    }
}
