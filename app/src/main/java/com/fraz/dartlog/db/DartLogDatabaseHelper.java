package com.fraz.dartlog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.game.X01;
import com.fraz.dartlog.game.X01PlayerData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

public class DartLogDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DartLog.db";

    public DartLogDatabaseHelper(Context context) {
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

        initializePlayers(db);
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

    /**
     * Get all match data for the player with the given name.
     *
     * @param playerName The name of the player.
     * @return List of match data for the given player.
     */
    public ArrayList<PlayerData> getPlayerMatchData(String playerName) {
        SQLiteDatabase db = getReadableDatabase();

        long playerId = getPlayerId(db, playerName);
        ArrayList<Long> matchIds = getMatchIds(db, playerId);
        ArrayList<PlayerData> playerData = new ArrayList<>();
        for (long matchId : matchIds) {
            LinkedList<Integer> playerScores = getPlayerScores(db, playerId, matchId);
            playerData.add(new X01PlayerData(playerName, playerScores));
        }
        return playerData;
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
        long matchId = insertX01MatchEntry(db, match, c.getTimeInMillis());

        for (int i = 0; i < match.getNumberOfPlayers(); ++i) {
            insertPlayerScores(db, match.getPlayer(i), matchId);
        }
    }

    private void insertPlayerScores(SQLiteDatabase db, X01PlayerData player, long matchId) {
        long playerId = getPlayerId(db, player);

        for (int score : player.getScoreHistory()) {
            ContentValues values = new ContentValues();
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_SCORE, score);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID, matchId);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID, playerId);
            db.insert(DartLogContract.ScoreEntry.TABLE_NAME, null, values);
        }
    }

    private LinkedList<Integer> getPlayerScores(SQLiteDatabase db, long playerId, long matchId) {
        String[] projection = {
                DartLogContract.ScoreEntry.COLUMN_NAME_SCORE
        };
        String selection = String.format(Locale.getDefault(), "%s = '%d' AND %s = '%d'",
                DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID,
                matchId,
                DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID,
                playerId);
        Cursor c = db.query(DartLogContract.ScoreEntry.TABLE_NAME, projection,
                selection, null, null, null, null);

        LinkedList<Integer> scores = new LinkedList<>();
        try {
            while (c.moveToNext()) {
                scores.add(c.getInt(c.getColumnIndex(
                        DartLogContract.ScoreEntry.COLUMN_NAME_SCORE)));
            }
        } finally {
            c.close();
        }
        return scores;
    }

    /**
     * Get the ids of all the matches the given player has played.
     *
     * @param playerId The id of the player.
     * @return List of match ids.
     */
    private ArrayList<Long> getMatchIds(SQLiteDatabase db, long playerId) {
        Cursor c = db.query(true, DartLogContract.ScoreEntry.TABLE_NAME,
                new String[]{DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID},
                String.format(Locale.getDefault(), "%s = '%d'",
                        DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID,
                        playerId),
                null, null, null, null, null);
        ArrayList<Long> matchIds = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                matchIds.add(c.getLong(c.getColumnIndex(
                        DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID)));
            }
        } finally {
            c.close();
        }
        return matchIds;
    }

    /**
     * Gets the ID of a player in the database. Adds a new player if id does not exist.
     *
     * @param db     The database.
     * @param player The player to find in the database or add if not existing.
     * @return The id of the player in the database.
     */
    private long getPlayerId(SQLiteDatabase db, X01PlayerData player) {
        return getPlayerId(db, player.getPlayerName());
    }

    /**
     * Gets the ID of a player in the database. Adds a new player if id does not exist.
     *
     * @param db         The database.
     * @param playerName The name of the player to find in the database or add if not existing.
     * @return The id of the player in the database.
     */
    private long getPlayerId(SQLiteDatabase db, String playerName) {
        long playerId;
        Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME,
                new String[]{DartLogContract.PlayerEntry._ID},
                String.format("%s = '%s'", DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME,
                        playerName), null, null, null, null);
        if (c.getCount() == 0)
            playerId = addPlayer(playerName);
        else {
            c.moveToFirst();
            playerId = c.getLong(0);
        }
        c.close();
        return playerId;
    }

    private long insertX01MatchEntry(SQLiteDatabase db, X01 match, Long timeInMillis) {
        ContentValues matchValues = new ContentValues();
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_DATE, timeInMillis);
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_GAME, "X01");
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_STARTING_PLAYER_ID,
                getPlayerId(db, match.getStartingPlayer()));
        matchValues.put(DartLogContract.MatchEntry.COLUMN_NAME_WINNER_PLAYER_ID,
                getPlayerId(db, match.getWinner()));
        return db.insert(DartLogContract.MatchEntry.TABLE_NAME, null, matchValues);
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
