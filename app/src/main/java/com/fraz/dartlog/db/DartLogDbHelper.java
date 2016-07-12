package com.fraz.dartlog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fraz.dartlog.game.X01;
import com.fraz.dartlog.game.X01PlayerData;

import java.util.ArrayList;
import java.util.Calendar;

public class DartLogDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DartLog.db";

    public DartLogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String createSql : DartLogContract.SQL_CREATE_ENTRIES) {
            db.execSQL(createSql);
        }

        initializePlayers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase();
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        for (String deleteSql : DartLogContract.SQL_DELETE_ENTRIES) {
            db.execSQL(deleteSql);
        }
        for (String createSql : DartLogContract.SQL_CREATE_ENTRIES) {
            db.execSQL(createSql);
        }

        addPlayer("Filip");
        addPlayer("Razmus");
        addPlayer("Jonathan");
        addPlayer("Martin");
    }

    /**
     * Add a player to the database. Duplicated names is not allowed.
     *
     * @param name the name of the player to add.
     * @return the row ID of the newly inserted player, or -1 if the player could not be added.
     */
    public long addPlayer(String name) {
        SQLiteDatabase db = getWritableDatabase();
        return addPlayer(name, db);
    }

    /**
     * Add a player to the database. Duplicated names is not allowed.
     *
     * @param name the name of the player to add.
     * @param db   the database which the name shall be added to.
     * @return the row ID of the newly inserted player, or -1 if the player could not be added.
     */
    private long addPlayer(String name, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME, name);
        return db.insert(DartLogContract.PlayerEntry.TABLE_NAME, null, values);
    }

    /**
     * Add a match to the database. Date at time of the add is recorded as date of match.
     * All players scores are added.
     *
     * @param match The match to add.
     */
    public void addX01Match(X01 match) {
        SQLiteDatabase db = getWritableDatabase();
        Calendar c = Calendar.getInstance();
        long matchId = InsertMatchEntry(db, "X01", c.getTimeInMillis());

        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
            InsertPlayerScores(db, match.getPlayer(i), matchId);
        }
    }

    private void InsertPlayerScores(SQLiteDatabase db, X01PlayerData player, long matchId) {
        long playerId = getPlayerId(db, player);

        for (int score : player.getScoreHistory()) {
            ContentValues values = new ContentValues();
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_SCORE, score);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID, matchId);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID, playerId);
            db.insert(DartLogContract.ScoreEntry.TABLE_NAME, null, values);
        }
    }

    /**
     * Gets the ID of a player in the database. Adds a new player if id does not exist.
     *
     * @param db     The database.
     * @param player The player to find in the database or add if not existing.
     * @return The id of the player in the database.
     */
    private long getPlayerId(SQLiteDatabase db, X01PlayerData player) {
        long playerId;
        String name = player.getPlayerName();
        Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME,
                new String[]{DartLogContract.PlayerEntry._ID},
                String.format("%s = '%s'", DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME,
                        name), null, null, null, null);
        if (c.getCount() == 0)
            playerId = addPlayer(name);
        else {
            c.moveToFirst();
            playerId = c.getLong(0);
        }
        c.close();
        return playerId;
    }

    private long InsertMatchEntry(SQLiteDatabase db, String gameType, Long timeInMillis) {
        ContentValues matchValues = new ContentValues();
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_DATE, timeInMillis);
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_GAME, gameType);
        return db.insert(DartLogContract.MatchEntry.TABLE_NAME, null, matchValues);
    }

    /**
     * Get the player names of all the players.
     *
     * @return the player names of all the players in the database.
     */
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

    private void initializePlayers(SQLiteDatabase db) {
        ArrayList<String> playersNames = new ArrayList<>();

        playersNames.add("Razmus");
        playersNames.add("Filip");
        playersNames.add("Jonathan");
        playersNames.add("Martin");
        playersNames.add("Erik");
        playersNames.add("Fredrik");
        playersNames.add("Stefan");
        playersNames.add("Maria");
        playersNames.add("Gustav");

        for (String name : playersNames) {
            addPlayer(name, db);
        }
    }
}
