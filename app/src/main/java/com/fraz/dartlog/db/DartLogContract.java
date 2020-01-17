package com.fraz.dartlog.db;

import android.provider.BaseColumns;

final class DartLogContract {


    static final String[] SQL_CREATE_ENTRIES = new String[]{
        "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                PlayerEntry._ID + " INTEGER PRIMARY KEY," +
                PlayerEntry.COLUMN_NAME_PLAYER_NAME + " TEXT UNIQUE)",
        "CREATE TABLE " + Match.TABLE_NAME + " (" +
                Match._ID + " INTEGER PRIMARY KEY," +
                Match.COLUMN_NAME_DATE + " INTEGER," +
                Match.COLUMN_NAME_WINNER_PLAYER_ID + " INTEGER," +
                Match.COLUMN_NAME_GAME_TYPE + " TEXT," +
                "FOREIGN KEY(" + Match.COLUMN_NAME_WINNER_PLAYER_ID + ")" +
                        " REFERENCES " + PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))",
        "CREATE TABLE " + X01Entry.TABLE_NAME + " (" +
                X01Entry._ID + " INTEGER PRIMARY KEY," +
                X01Entry.COLUMN_NAME_X + " INTEGER," +
                X01Entry.COLUMN_NAME_DOUBLE_OUT + " INTEGER," +
                X01Entry.COLUMN_NAME_MATCH_ID + " INTEGER," +
                "FOREIGN KEY(" + X01Entry.COLUMN_NAME_MATCH_ID + ")" +
                    " REFERENCES " + Match.TABLE_NAME + "(" + Match._ID + "))",
        "CREATE TABLE " + RandomEntry.TABLE_NAME + " (" +
                RandomEntry._ID + " INTEGER PRIMARY KEY," +
                RandomEntry.COLUMN_NAME_TURNS + " INTEGER," +
                RandomEntry.COLUMN_NAME_MATCH_ID + " INTEGER," +
                "FOREIGN KEY(" + RandomEntry.COLUMN_NAME_MATCH_ID + ")" +
                    " REFERENCES " + Match.TABLE_NAME + "(" + Match._ID + "))",
        "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
                ScoreEntry._ID + " INTEGER PRIMARY KEY, " +
                ScoreEntry.COLUMN_NAME_MATCH_ID + " INTEGER, " +
                ScoreEntry.COLUMN_NAME_PLAYER_ID + " INTEGER, " +
                ScoreEntry.COLUMN_NAME_SCORE + " INTEGER, " +
                "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_MATCH_ID + ") REFERENCES " +
                    Match.TABLE_NAME + "(" + Match._ID + ")," +
                "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_PLAYER_ID + ") REFERENCES " +
                    PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))"};

    static final String SQL_CREATE_STATISTIC_ENTRIES =
            "CREATE TABLE " + StatisticEntry.TABLE_NAME + " (" +
                    StatisticEntry._ID + " INTEGER PRIMARY KEY," +
                    StatisticEntry.COLUMN_NAME_PLAYER_ID + " INTEGER, " +
                    StatisticEntry.COLUMN_NAME_301_FEWEST_TURNS_MATCH_ID + " INTEGER," +
                    StatisticEntry.COLUMN_NAME_501_FEWEST_TURNS_MATCH_ID + " INTEGER," +
                    StatisticEntry.COLUMN_NAME_HIGHEST_CHECKOUT_MATCH_ID +" INTEGER," +
                    "FOREIGN KEY(" + StatisticEntry.COLUMN_NAME_301_FEWEST_TURNS_MATCH_ID +
                        ") REFERENCES " + Match.TABLE_NAME + "(" + Match._ID + ")," +
                    "FOREIGN KEY(" + StatisticEntry.COLUMN_NAME_501_FEWEST_TURNS_MATCH_ID +
                        ") REFERENCES " + Match.TABLE_NAME + "(" + Match._ID + ")," +
                    "FOREIGN KEY(" + StatisticEntry.COLUMN_NAME_HIGHEST_CHECKOUT_MATCH_ID +
                        ") REFERENCES " + Match.TABLE_NAME + "(" + Match._ID + ")," +
                    "FOREIGN KEY(" + ScoreEntry.COLUMN_NAME_PLAYER_ID + ") REFERENCES " +
                        PlayerEntry.TABLE_NAME + "(" + PlayerEntry._ID + "))";


    static final String[] SQL_DELETE_ENTRIES = new String[]{
        "DROP TABLE IF EXISTS " + DartLogContract.PlayerEntry.TABLE_NAME,
        "DROP TABLE IF EXISTS " + Match.TABLE_NAME,
        "DROP TABLE IF EXISTS " + X01Entry.TABLE_NAME,
        "DROP TABLE IF EXISTS " + RandomEntry.TABLE_NAME,
        "DROP TABLE IF EXISTS " + DartLogContract.ScoreEntry.TABLE_NAME};

    public DartLogContract() {}

    /* Inner class that defines the table contents */
    static abstract class PlayerEntry implements BaseColumns {
        static final String TABLE_NAME = "player";
        static final String COLUMN_NAME_ID = "_id";
        static final String COLUMN_NAME_PLAYER_NAME = "name";
    }

    /* Inner class that defines the table contents */
    static abstract class StatisticEntry implements BaseColumns {
        static final String TABLE_NAME = "statistic_type";
        static final String COLUMN_NAME_PLAYER_ID = "player_id";
        static final String COLUMN_NAME_301_FEWEST_TURNS_MATCH_ID = "fewest_turns_301";
        static final String COLUMN_NAME_501_FEWEST_TURNS_MATCH_ID = "fewest_turns_501";
        static final String COLUMN_NAME_HIGHEST_CHECKOUT_MATCH_ID = "highest_checkout";
    }

    static abstract class Match implements BaseColumns {
        static final String TABLE_NAME = "match";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_WINNER_PLAYER_ID = "winner_id";
        static final String COLUMN_NAME_GAME_TYPE = "game_type";
    }

    static abstract class X01Entry implements BaseColumns {
        static final String TABLE_NAME = "x01";
        static final String COLUMN_NAME_X = "x";
        static final String COLUMN_NAME_DOUBLE_OUT = "double_out";
        static final String COLUMN_NAME_MATCH_ID = "match_id";
    }

    static abstract class RandomEntry implements BaseColumns {
        static final String TABLE_NAME = "random";
        static final String COLUMN_NAME_TURNS = "turns";
        static final String COLUMN_NAME_MATCH_ID = "match_id";
    }

    static abstract class ScoreEntry implements BaseColumns {
        static final String TABLE_NAME = "match_score";
        static final String COLUMN_NAME_PLAYER_ID = "player_id";
        static final String COLUMN_NAME_MATCH_ID = "match_id";
        static final String COLUMN_NAME_SCORE = "score";
    }
}
