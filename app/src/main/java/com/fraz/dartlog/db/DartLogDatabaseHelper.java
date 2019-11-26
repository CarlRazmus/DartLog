package com.fraz.dartlog.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseLongArray;

import com.fraz.dartlog.CheckoutChart;
import com.fraz.dartlog.game.AdditionScoreManager;
import com.fraz.dartlog.game.Game;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.game.random.Random;
import com.fraz.dartlog.game.x01.X01;
import com.fraz.dartlog.game.x01.X01PlayerData;
import com.fraz.dartlog.game.x01.X01ScoreManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class DartLogDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "DartLog.db";
    private CheckoutChart checkoutChart;

    private static DartLogDatabaseHelper dbHelperInstance = null;

    public static DartLogDatabaseHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
           dbHelperInstance = new DartLogDatabaseHelper(context.getApplicationContext());
           dbHelperInstance.setWriteAheadLoggingEnabled(true);
        }
        return dbHelperInstance;
    }

    private DartLogDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        checkoutChart = new CheckoutChart(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String createSql : DartLogContract.SQL_CREATE_ENTRIES) {
            db.execSQL(createSql);
        }
        db.execSQL(DartLogContract.SQL_CREATE_STATISTIC_ENTRIES );
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + DartLogContract.StatisticEntry.TABLE_NAME);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //resetDatabase();
        switch (oldVersion) {
            case 1:
                db.execSQL(DartLogContract.SQL_CREATE_STATISTIC_ENTRIES);
        }
    }

    private void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        for (String deleteSql : DartLogContract.SQL_DELETE_ENTRIES) {
            db.execSQL(deleteSql);
        }
        for (String createSql : DartLogContract.SQL_CREATE_ENTRIES) {
            db.execSQL(createSql);
        }
    }

    /**
     * Add a player to the database. Duplicated names is not allowed.
     *
     * @param name the name of the player to add.
     * @return the row ID of the newly inserted player, or -1 if the player could not be added.
     */
    public long addPlayer(String name) {
        SQLiteDatabase db = getWritableDatabase();
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
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> names;
        String[] projection = {
                DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME
        };

        names = new ArrayList<>();
        try (Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME, projection,
                null, null, null, null, null)) {
            while (c.moveToNext()) {
                names.add(c.getString(c.getColumnIndex(
                        DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME)));
            }
        }
        return names;
    }

    /**
     * Get the player ids of all the players.
     *
     * @return the player names of all the players in the database.
     */
    public ArrayList<Integer> getPlayerIds() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Integer> ids;
        String[] projection = {
                DartLogContract.PlayerEntry.COLUMN_NAME_ID
        };

        ids = new ArrayList<>();
        try (Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME, projection,
                null, null, null, null, null)) {
            Integer columnIndex = c.getColumnIndex(DartLogContract.PlayerEntry.COLUMN_NAME_ID);
            while (c.moveToNext()) {
                ids.add(c.getInt(columnIndex));
            }
        }
        return ids;
    }

    public HashMap<Long, GameData> getPlayerMatchDataById(String playerName) {
        HashMap<Long, GameData> gameDataById;

        long playerId = getPlayerId(playerName);
        ArrayList<Long> matchIds = getAllMatchIds(playerId);
        HashMap<Long, String> gameTypes = new HashMap<>();
        for(long matchId : matchIds)
            gameTypes.put(matchId, getGameType(matchId));

        gameDataById = new HashMap<>();
        for (long matchId : matchIds) {
            String gameType = gameTypes.get(matchId);

            switch (gameType){
                case "x01":
                    gameDataById.put(matchId, getX01GameData(matchId));
                    break;
                case "random":
                    gameDataById.put(matchId, getRandomGameData(matchId));
                    break;

            }
        }
        return gameDataById;
    }

    /**
     * Get all match data for the player with the given name.
     *
     * @param playerName The name of the player.
     * @return List of match data for the given player.
     */
    public ArrayList<GameData> getPlayerMatchData(String playerName) {
        return new ArrayList<>(getPlayerMatchDataById(playerName).values());
    }

    private long lastLoadedMatchId = Long.MAX_VALUE;

    /**
     *
     * @param playerName The name of the player.
     * @param startMatchId The starting match id in the database - OBS! the data is fetched backwards.
     * @param amount
     * @return List of match data for the given player.
     */
    public ArrayList<GameData> getPlayerMatchData(String playerName, long startMatchId, int amount) {
        ArrayList<GameData> gameData;

        long playerId = getPlayerId(playerName);
        ArrayList<Long> matchIds = getMatchIds(playerId, startMatchId, amount);

        if (matchIds.size() > 0)
            lastLoadedMatchId = matchIds.get(matchIds.size() - 1);
        HashMap<Long, String> gameTypes = new HashMap<>();
        for(long matchId : matchIds)
            gameTypes.put(matchId, getGameType(matchId));

        gameData = new ArrayList<>();
        for (long MatchId : matchIds) {
            String gameType = gameTypes.get(MatchId);

            switch (gameType){
                case "x01":
                    gameData.add(getX01GameData(MatchId));
                    break;
                case "random":
                    gameData.add(getRandomGameData(MatchId));
                    break;
            }
        }
        return gameData;
    }

    public int getNumberOfGamesPlayed(String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        long playerId = getPlayerId(playerName);
        try (Cursor c = db.query(true, DartLogContract.ScoreEntry.TABLE_NAME,
                new String[]{DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID},
                String.format(Locale.getDefault(), "%s = '%d'",
                        DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID, playerId),
                null, null, null, null, null)) {
            return c.getCount();
        }
    }

    public int getNumberOfGamesWon(String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        long playerId = getPlayerId(playerName);
        String query = "SELECT m._ID" +
                      "     FROM \"match\" m" +
                      "          join player p" +
                      "               on p._ID = m.winner_id" +
                      "     WHERE m.winner_id = ?;";

        try (Cursor c = db.rawQuery(query, new String[]{String.valueOf(playerId)})) {
            return c.getCount();
        }
    }

    private GameData getRandomGameData(long matchId) {
        HashMap<String, LinkedList<Integer>> matchScores = getMatchScores(matchId);

        try (Cursor c = getRandomMatchEntry(matchId)) {
            if (c.moveToFirst()) {
                String winnerName = c.getString(c.getColumnIndex("winner"));
                Calendar date = getDate(c);

                LinkedHashMap<String, PlayerData> playerData = new LinkedHashMap<>();
                for (Map.Entry<String, LinkedList<Integer>> playerEntry : matchScores.entrySet())
                {
                    AdditionScoreManager scoreManager = new AdditionScoreManager();
                    scoreManager.applyScores(playerEntry.getValue());
                    playerData.put(playerEntry.getKey(),
                            new PlayerData(playerEntry.getKey(), scoreManager));
                }

                return new GameData(new ArrayList<>(playerData.values()),
                        date, playerData.get(winnerName), "random");
            } else throw new IllegalArgumentException("Match not found");
        }
    }

    private GameData getX01GameData(long matchId) {
        HashMap<String, LinkedList<Integer>> matchScores = getMatchScores(matchId);

        try (Cursor c = getX01MatchEntry(matchId)) {
            if (c.moveToFirst()) {
                String winnerName = c.getString(c.getColumnIndex("winner"));
                Calendar date = getDate(c);
                int x = c.getInt(c.getColumnIndex(DartLogContract.X01Entry.COLUMN_NAME_X));
                int double_out =
                        c.getInt(c.getColumnIndex(DartLogContract.X01Entry.COLUMN_NAME_DOUBLE_OUT));

                LinkedHashMap<String, PlayerData> playerData = new LinkedHashMap<>();
                for (Map.Entry<String, LinkedList<Integer>> playerEntry : matchScores.entrySet())
                {
                    X01ScoreManager scoreManager = new X01ScoreManager(x);
                    scoreManager.setDoubleOutAttempts(double_out);
                    scoreManager.applyScores(playerEntry.getValue());
                    playerData.put(playerEntry.getKey(),
                                   new X01PlayerData(checkoutChart, playerEntry.getKey(), scoreManager));
                }

                return new GameData(new ArrayList<>(playerData.values()),
                        date, playerData.get(winnerName), "x01");
            } else throw new IllegalArgumentException("Match   "+ matchId + "   not found");
        }
    }

    private int[] x01GameTypes = {301, 401, 501, 601, 701, 801};
    private String generateX01MatchString(int winnerId){
        String matchDataString = "(";
        matchDataString += Calendar.getInstance().getTimeInMillis();
        matchDataString += ", " + winnerId;
        matchDataString += ", 'x01')";
        return matchDataString;
    }

    private String generateX01MatchScoresString(int matchId, int winnerId, int gameType, ArrayList<Integer> playerIds, java.util.Random rand) {
        int pointsLeft;
        int currentPlayerIdx = 0;
        int currentPlayer = playerIds.get(currentPlayerIdx);
        int score = rand.nextInt(181);
        String matchScoresString;
        HashMap<Integer, Integer> playerToPointsLeftMap = new HashMap<>();

        for(int i = 0; i < playerIds.size(); i++){
            playerToPointsLeftMap.put(playerIds.get(i), gameType);
        }

        //create the first element without a comma in the beginning.
        matchScoresString = "(" + matchId + ", " + currentPlayer + ", "+ score + ")";
        int newVal = playerToPointsLeftMap.get(currentPlayer) - score;
        playerToPointsLeftMap.put(currentPlayer, newVal);
        currentPlayerIdx += 1;

        while (playerToPointsLeftMap.get(winnerId) > 0){
            currentPlayer = playerIds.get(currentPlayerIdx);
            pointsLeft = playerToPointsLeftMap.get(currentPlayer);
            if(currentPlayer == winnerId){
                if (pointsLeft < 50)
                    score = pointsLeft;
                else {
                    score = rand.nextInt(181);
                    if (pointsLeft - score == 1 || pointsLeft - score < 0)
                        score = 0;
                }
            }
            else{
                score = rand.nextInt(50);
                if (pointsLeft - score <= 1)
                    score = 0;
            }
            matchScoresString += ", (" + matchId + ", " + currentPlayer + ", "+ score + ")";

            if (score > 0)
                playerToPointsLeftMap.put(currentPlayer, playerToPointsLeftMap.get(currentPlayer) - score);

            currentPlayerIdx += 1;
            if (currentPlayerIdx == playerIds.size())
                currentPlayerIdx = 0;
        }
        return matchScoresString;
    }

    @TargetApi(24)
    private String generateX01String(int gameType, int matchId){
        return "(" + String.valueOf(Math.floorDiv(gameType, 100)) + ", " + 5 + ", " + matchId + ")";
    }

    private void generateDatabaseX01MatchEntries(int nrEntries){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Integer> playerIds = getPlayerIds();
        java.util.Random rand = new java.util.Random();
        int matchId = getLatestMatchId() + 1;

        int winnerId = playerIds.get(rand.nextInt(playerIds.size()));
        int gameType = x01GameTypes[rand.nextInt(x01GameTypes.length)];

        String matchValues = generateX01MatchString(winnerId);
        String matchScoreValues = generateX01MatchScoresString(matchId, winnerId, gameType, playerIds, rand);
        String x01String = generateX01String(gameType, matchId);

        for (int i=1; i < nrEntries; i++){
            matchId += 1;
            winnerId = playerIds.get(rand.nextInt(playerIds.size()));
            gameType = x01GameTypes[rand.nextInt(x01GameTypes.length)];

            matchValues += ", " + generateX01MatchString(winnerId);
            matchScoreValues += ", " + generateX01MatchScoresString(matchId, winnerId, gameType, playerIds, rand);
            x01String += ", " + generateX01String(gameType, matchId);
        }

        String sqlMatchQuery =
                "INSERT INTO match (date, winner_id, game_type) " +
                        "VALUES " +
                        matchValues + ";";
        String sqlMatchScoreQuery =
                "INSERT INTO match_score (match_id, player_id, score) " +
                        "VALUES " +
                        matchScoreValues + ";";
        String sqlX01Query =
                "INSERT INTO x01 (x, double_out, match_id) " +
                        "VALUES " +
                        x01String + ";";

        db.execSQL(sqlMatchQuery);
        db.execSQL(sqlMatchScoreQuery);
        db.execSQL(sqlX01Query);
    }

    private int getLatestMatchId() {
        SQLiteDatabase db = getWritableDatabase();
        int matchId = 0;
        String sql = "SELECT max(m._id) as m_max FROM match m;";

        try (Cursor c = db.rawQuery(sql, null)) {
            while (c.moveToNext()) {
                matchId = c.getInt(c.getColumnIndex("m_max"));
            }
        }
        return matchId;
    }

    public void createStatisticViews(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Integer> playerIds = getPlayerIds();
        long start = SystemClock.currentThreadTimeMillis();
        for (int id : playerIds){
            String sqlDropStatisticsView = "DROP VIEW IF EXISTS [statistics_view_" + id + "];";
            String sqlCreateStatisticsView =
                    "CREATE VIEW statistics_view_" + id + " AS " +
                            "SELECT m._id as m_id, m.game_type, " +
                            "x.x, m.winner_id, ms.score as ms_score, ms._id as ms_id, " +
                            "count(ms.score) as ms_count, max(ms._id) as ms_max_id " +
                            "FROM x01 x " +
                            "INNER JOIN match m ON x.match_id == m_id " +
                            "INNER JOIN match_score ms ON x.match_id == ms.match_id " +
                            "WHERE m.winner_id == " + id + " AND m.winner_id == ms.player_id " +
                            "AND x.x IN (3, 4, 5) " +
                            "GROUP BY m.winner_id, x.x, m_id ";
            db.execSQL(sqlDropStatisticsView);
            db.execSQL(sqlCreateStatisticsView);
        }
        long end = SystemClock.currentThreadTimeMillis();
        Log.d("hej", "it took " + (end - start) + "ms");
    }

    public void updateStatisticViews(ArrayList<Integer> playerIds){
        SQLiteDatabase db = getWritableDatabase();
        long start = SystemClock.currentThreadTimeMillis();
        for (int id : playerIds){
            String sqlDropStatisticsView = "DROP VIEW IF EXISTS [statistics_view_" + id + "];";
            String sqlCreateStatisticsView =
                    "CREATE VIEW statistics_view_" + id + " AS " +
                            "SELECT m._id as m_id, m.game_type, " +
                            "x.x, m.winner_id, ms.score as ms_score, ms._id as ms_id, " +
                            "count(ms.score) as ms_count, max(ms._id) as ms_max_id " +
                            "FROM x01 x " +
                            "INNER JOIN match m ON x.match_id == m_id " +
                            "INNER JOIN match_score ms ON x.match_id == ms.match_id " +
                            "WHERE m.winner_id == " + id + " AND m.winner_id == ms.player_id " +
                            "AND x.x IN (3, 4, 5) " +
                            "GROUP BY m.winner_id, x.x, m_id ";
            db.execSQL(sqlDropStatisticsView);
            db.execSQL(sqlCreateStatisticsView);
        }
        long end = SystemClock.currentThreadTimeMillis();
        Log.d("hej", "it took " + (end - start) + "ms");
    }

    public void test(){
        SQLiteDatabase db = getWritableDatabase();
        String sqlCreateStatisticsView_1 =
                "CREATE VIEW IF NOT EXISTS statistics_view_1 AS " +
                        "SELECT m._id as m_id, m.game_type, " +
                        "x.x, m.winner_id, ms.score as ms_score, ms._id as ms_id, " +
                        "count(ms.score) as ms_count, max(ms._id) as ms_max_id " +
                        "FROM x01 x " +
                        "INNER JOIN match m ON x.match_id == m_id " +
                        "INNER JOIN match_score ms ON x.match_id == ms.match_id " +
                        "WHERE m.winner_id == 1 AND m.winner_id == ms.player_id " +
                        "AND x.x IN (3, 5) " +
                        "GROUP BY m.winner_id, x.x, m_id ";

        String sqlCreateStatisticsViewAll =
                "CREATE VIEW IF NOT EXISTS statistics_view_all AS " +
                        "SELECT m._id as m_id, m.game_type, " +
                        "x.x, m.winner_id, ms.score as ms_score, ms._id as ms_id, " +
                        "count(ms.score) as ms_count, max(ms._id) as ms_max_id " +
                        "FROM x01 x " +
                        "INNER JOIN match m ON x.match_id == m_id " +
                        "INNER JOIN match_score ms ON x.match_id == ms.match_id " +
                        "WHERE  m.winner_id == ms.player_id " +
                        "AND x.x IN (3, 5) " +
                        "GROUP BY m.winner_id, x.x, m_id ";

        String sqlGetX01MaxCheckoutQueryFast =
                "SELECT  x, m_id, winner_id, max(ms_score) as max_checkout  " +
                        "FROM statistics_view_all " +
                        "WHERE winner_id ==  1 " +
                        "GROUP BY winner_id " +
                        "ORDER BY winner_id; ";

        String sqlGetX01MaxCheckoutQuerySlow =
                "SELECT  x, m_id, winner_id, max(ms_score) as max_checkout  " +
                        "FROM statistics_view_1;";
        db.execSQL(sqlCreateStatisticsView_1);
        db.execSQL(sqlCreateStatisticsViewAll);
        runQueryAndPrintTable("SELECT count(_id) from match_score;");
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndPrintTable(sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQueryFast);
        //runQueryAndPrintTable("SELECT * FROM statistics_view_1");

        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndPrintTable(sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
        runQueryAndLogStatistics( sqlGetX01MaxCheckoutQuerySlow);
    }

    public GameData getHighestX01Checkout(long playerId){
        SQLiteDatabase db = getWritableDatabase();
        int highestCheckout = 0;
        int matchId = -1;
        GameData gameData = null;

        String sqlGetX01MaxCheckoutsQuery =
            "SELECT  x, m_id, winner_id, max(ms_score) as max_checkout  " +
                    "FROM statistics_view_" + playerId + " " +
                    "WHERE winner_id ==  " + playerId + " " +
                    "GROUP BY winner_id " +
                    "ORDER BY winner_id; ";

        try (Cursor c = db.rawQuery(sqlGetX01MaxCheckoutsQuery, null)) {
            while (c.moveToNext()) {
                 Integer checkout = c.getInt(c.getColumnIndex("max_checkout"));
                 if (checkout < highestCheckout) {
                     highestCheckout = checkout;
                     matchId = c.getInt(c.getColumnIndex("m_id"));
                 }
            }
        }
        if(highestCheckout > 0)
            gameData = getX01GameData(matchId);
        //runQueryAndPrintTable(sqlGetX01MaxCheckoutsQuery);
        return gameData;
    }

    public HashMap<String, Integer> getFewestTurns(long playerId){
        SQLiteDatabase db = getWritableDatabase();
        HashMap<String, Integer> fewestTurns = new HashMap<>();
        String sqlGetX01FewestTurnsQuery =
                "SELECT  x, m_id, winner_id, min(ms_count) as fewest_turns  " +
                        "FROM statistics_view_ " + playerId + " " +
                        "WHERE winner_id ==  " + playerId + " " +
                        "GROUP BY x, winner_id " +
                        "ORDER BY winner_id; ";

        try (Cursor c = db.rawQuery(sqlGetX01FewestTurnsQuery, null)) {
            while (c.moveToNext()) {
                String x01 = c.getString(c.getColumnIndex(DartLogContract.X01Entry.COLUMN_NAME_X));
                fewestTurns.put(x01, c.getInt(c.getColumnIndex("fewest_turns")));
            }
        }
        //runQueryAndPrintTable(sqlGetX01FewestTurnsQuery);
        return fewestTurns;
    }

    public void runQueryAndPrintTable(String sqlQuery){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> headers = new ArrayList<>();
        String baseFormatString = new String();
        int maxHeaderLen = 5;

        Cursor c = db.rawQuery(sqlQuery, null);

        for (int i=0; i< c.getColumnCount(); i++)
            maxHeaderLen = Math.max(maxHeaderLen, c.getColumnName(i).length());
        for (int i=0; i< c.getColumnCount(); i++) {
            headers.add(c.getColumnName(i));
            baseFormatString += "%" + maxHeaderLen + "s  |";
        }

        Log.d("hej", String.format(baseFormatString, headers.toArray()));
        while (c.moveToNext()) {
            ArrayList<String> values = new ArrayList<>();
            for (int i=0; i< c.getColumnCount(); i++){
                if(c.isNull(i))
                    values.add(null);
                else
                    values.add(c.getString(i));
            }
            Log.d("hej", String.format(baseFormatString, values.toArray()));
        }
        c.close();
    }

    private void runQueryAndLogStatistics(String sqlQuery){
        SQLiteDatabase db = getWritableDatabase();
        long startTime = System.currentTimeMillis();
        Cursor c = db.rawQuery(sqlQuery, null);

        while (c.moveToNext()) {   }

        long executionTime = System.currentTimeMillis() - startTime;
        Log.d("hej", "exekveringstid: " + executionTime + "ms");
        c.close();
    }

    @NonNull
    private Calendar getDate(Cursor c) {
        long dateInMillis = c.getLong(c.getColumnIndex(DartLogContract.Match.COLUMN_NAME_DATE));
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date;
    }

    public long getLastLoadedMatchId(){
        return lastLoadedMatchId;
    }

    /**
     * Add a match to the database. Date at time of the add is recorded as date of match.
     * All players scores are added.
     *
     * @param game The match to add.
     */
    public void addX01Match(X01 game) {
        long matchId = insertMatchEntry(game, "x01");
        insertX01Entry(game, matchId);
        insertScores(game, matchId);
    }

    /**
     * Add a match to the database. Date at time of the add is recorded as date of match.
     * All players scores are added.
     *
     * @param game The match to add.
     */
    public void addRandomMatch(Random game) {
        long matchId = insertMatchEntry(game, "random");
        insertRandomEntry(game, matchId);
        insertScores(game, matchId);
    }

    private void insertScores( Game game, long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        SparseLongArray playerIds = new SparseLongArray();
        SparseArray<Iterator<Integer>> playerScoreIterators = new SparseArray<>();

        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
            PlayerData player = game.getPlayer(i);
            long playerId = getPlayerId(player);
            playerIds.append(i, playerId);
            playerScoreIterators.append(i, player.getScoreHistory().iterator());
        }

        for (Integer i : game.getPlayOrder()) {
            int score = playerScoreIterators.get(i).next();
            ContentValues values = new ContentValues();
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_SCORE, score);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID, matchId);
            values.put(DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID, playerIds.get(i));
            db.insert(DartLogContract.ScoreEntry.TABLE_NAME, null, values);
        }
    }

    private HashMap<String, LinkedList<Integer>> getMatchScores(long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT p.name, s.score " +
                "          FROM match_score s " +
                "              join player p " +
                "                  on s.player_id = p._ID and s.match_id = ?;";

        HashMap<String, LinkedList<Integer>> playerScores = new HashMap<>();

        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(matchId)})) {
            while (c.moveToNext()) {
                int score = c.getInt(c.getColumnIndex(
                        DartLogContract.ScoreEntry.COLUMN_NAME_SCORE));
                String playerName = c.getString(c.getColumnIndex(
                        DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME));
                if (playerScores.containsKey(playerName))
                    playerScores.get(playerName).add(score);
                else {
                    LinkedList<Integer> scores = new LinkedList<>();
                    scores.add(score);
                    playerScores.put(playerName, scores);
                }
            }
        }
        return playerScores;
    }

    /**
     * Get the ids of all the matches the given player has played.
     *
     * @param playerId The id of the player.
     * @return List of match ids.
     */
    private ArrayList<Long> getAllMatchIds(long playerId) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Long> matchIds = new ArrayList<>();

        try (Cursor c = db.query(true, DartLogContract.ScoreEntry.TABLE_NAME,
                new String[]{DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID},
                String.format(Locale.getDefault(), "%s = '%d'",
                        DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID,
                        playerId),
                null, null, null, null, null)) {
            while (c.moveToNext()) {
                matchIds.add(c.getLong(c.getColumnIndex(
                        DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID)));
            }
        }
        return matchIds;
    }
    /**
     * Get the ids of the specified matches.
     *
     * @param playerId The id of the player.
     * @param startIndex
     * @param amount
     * @return List of match ids.
     */
    private ArrayList<Long> getMatchIds(long playerId, long startIndex, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Long> matchIds = new ArrayList<>();

        try (Cursor c = db.query(true, DartLogContract.ScoreEntry.TABLE_NAME,
                new String[]{DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID},
                String.format(Locale.getDefault(), "%s = '%d' AND %s < '%d'",
                        DartLogContract.ScoreEntry.COLUMN_NAME_PLAYER_ID, playerId,
                        DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID, startIndex),
                null,null, null,
                String.format(Locale.getDefault(), "%s DESC",
                        DartLogContract.ScoreEntry._ID),
                "0," + String.valueOf(amount))) {
            while (c.moveToNext()) {
                matchIds.add(c.getLong(c.getColumnIndex(
                        DartLogContract.ScoreEntry.COLUMN_NAME_MATCH_ID)));
            }
        }

        return matchIds;
    }

    /**
     * Get the game type of a match
     *
     * @param matchId The id of the match.
     * @return The game type of the game (x01, random).
     */
    private String getGameType(long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        String gameType = "";

        try (Cursor c = db.query(true, DartLogContract.Match.TABLE_NAME,
                new String[]{DartLogContract.Match.COLUMN_NAME_GAME_TYPE},
                String.format(Locale.getDefault(), "%s = '%d'",
                        DartLogContract.Match._ID,
                        matchId),
                null, null, null, null, null)) {
            if (c.moveToFirst()) {
                gameType = c.getString(c.getColumnIndex(
                        DartLogContract.Match.COLUMN_NAME_GAME_TYPE));
                while (c.moveToNext()) {
                    /* If more than one row of data is found we have encountered an error. */
                   throw new Error();
                }
            }
        }
        return gameType;
    }

    /**
     * Gets the ID of a player in the database. Adds a new player if id does not exist.
     *
     * @param player The player to find in the database or add if not existing.
     * @return The id of the player in the database.
     */
    private long getPlayerId(PlayerData player) {
        return getPlayerId(player.getPlayerName());
    }

    /**
     * Gets the ID of a player in the database. Adds a new player if id does not exist.
     *
     * @param playerName The name of the player to find in the database or add if not existing.
     * @return The id of the player in the database.
     */
    public long getPlayerId(String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        long playerId;
        try (Cursor c = db.query(DartLogContract.PlayerEntry.TABLE_NAME,
                new String[]{DartLogContract.PlayerEntry._ID},
                String.format("%s = '%s'", DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME,
                        playerName), null, null, null, null)) {
            if (c.getCount() == 0)
                playerId = addPlayer(playerName);
            else {
                c.moveToFirst();
                playerId = c.getLong(0);
            }
        }
        return playerId;
    }

    private long insertMatchEntry(Game game, String gameType){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues matchValues = new ContentValues();

        matchValues.put(DartLogContract.Match.COLUMN_NAME_DATE,
                game.getDate().getTimeInMillis());
        matchValues.put(DartLogContract.Match.COLUMN_NAME_GAME_TYPE, gameType);
        matchValues.put(DartLogContract.Match.COLUMN_NAME_WINNER_PLAYER_ID,
                getPlayerId(game.getWinner()));

        return db.insert(DartLogContract.Match.TABLE_NAME, null, matchValues);
    }

    private long insertX01Entry(X01 game, long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues matchValues = new ContentValues();
        matchValues.put(DartLogContract.X01Entry.COLUMN_NAME_X, game.getX());
        matchValues.put(DartLogContract.X01Entry.COLUMN_NAME_MATCH_ID, matchId);
        matchValues.put(DartLogContract.X01Entry.COLUMN_NAME_DOUBLE_OUT,
                game.getDoubleOutAttempts());
        return db.insert(DartLogContract.X01Entry.TABLE_NAME, null, matchValues);
    }

    private long insertRandomEntry(Random game, long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues matchValues = new ContentValues();
        matchValues.put(DartLogContract.RandomEntry.COLUMN_NAME_MATCH_ID, matchId);
        matchValues.put(DartLogContract.RandomEntry.COLUMN_NAME_TURNS, game.getNrOfTurns());
        return db.insert(DartLogContract.RandomEntry.TABLE_NAME, null, matchValues);
    }

    private Cursor getX01MatchEntry(long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT x.double_out, x.x, m.date, p.name as winner" +
                "     FROM x01 x" +
                "          join \"match\" m" +
                "               on m._ID = x.match_id" +
                "          join player p" +
                "               on p._ID = m.winner_id" +
                "     WHERE x.match_id = ?;";

        return db.rawQuery(sql, new String[]{String.valueOf(matchId)});
    }


    private Cursor getRandomMatchEntry(long matchId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT r.turns, m.date, p.name as winner" +
                "     FROM random r" +
                "          join \"match\" m" +
                "               on m._ID = r.match_id" +
                "          join player p" +
                "               on p._ID = m.winner_id" +
                "     WHERE r.match_id = ?;";

        return db.rawQuery(sql, new String[]{String.valueOf(matchId)});
    }

    public boolean playerExist(String playerName) {
        SQLiteDatabase db = getWritableDatabase();
        try (Cursor cc = db.query(DartLogContract.PlayerEntry.TABLE_NAME,
                new String[]{DartLogContract.PlayerEntry._ID},
                String.format("%s = '%s'", DartLogContract.PlayerEntry.COLUMN_NAME_PLAYER_NAME,
                        playerName), null, null, null, null)) {
            if (cc.getCount() == 1)
                return true;
        }
        catch(android.database.sqlite.SQLiteException e){
            return false;
        }
        return false;
    }
}
