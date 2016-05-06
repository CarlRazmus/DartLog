package com.fraz.dartlog.db;

import android.provider.BaseColumns;

public final class DartLogContract {


    protected static final String[] SQL_CREATE_ENTRIES = new String[]{
            "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                    PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                    PlayerEntry.COLUMN_NAME_PLAYER_NAME + " TEXT UNIQUE)",
            "CREATE TABLE " + MatchEntry.TABLE_NAME + " (" +
                    MatchEntry._ID + " INTEGER PRIMARY KEY," +
                    MatchEntry.COLUMN_NAME_DATE + " INTEGER," +
                    MatchEntry.COLUMN_NAME_GAME + " TEXT)",
            "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
                    ScoreEntry.COLUMN_NAME_MATCH_ID + " INTEGER," +
                    ScoreEntry.COLUMN_NAME_PLAYER_ID + " INTEGER," +
                    ScoreEntry.COLUMN_NAME_SCORE + " INTEGER," +
                    "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_MATCH_ID + ")" + " REFERENCES " +
                        MatchEntry.TABLE_NAME + "(" + MatchEntry._ID + ")," +
                    "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_PLAYER_ID + ")" + " REFERENCES " +
                        PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))"};

    protected static final String[] SQL_DELETE_ENTRIES = new String[]{
            "DROP TABLE IF EXISTS " + DartLogContract.PlayerEntry.TABLE_NAME,
            "DROP TABLE IF EXISTS " + DartLogContract.MatchEntry.TABLE_NAME,
            "DROP TABLE IF EXISTS " + DartLogContract.ScoreEntry.TABLE_NAME};


    public DartLogContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PlayerEntry implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME_PLAYER_NAME = "name";
    }

    public static abstract class MatchEntry implements BaseColumns {
        public static final String TABLE_NAME = "match";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_GAME = "game";
    }

    public static abstract class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "match_score";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_MATCH_ID = "match_id";
        public static final String COLUMN_NAME_SCORE = "score";
    }
}
