package com.fraz.dartlog;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.util.Log;

import com.fraz.dartlog.db.DartLogDatabaseHelper;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class GenerateTestData {

    private int[] x01GameTypes = {301, 401, 501, 601, 701, 801};
    private DartLogDatabaseHelper dbHelper;

    public GenerateTestData() {
        dbHelper = DartLogDatabaseHelper.getInstance(null);
    }

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
        ArrayList<Integer> playerIds = dbHelper.getPlayerIds();
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

        dbHelper.getWritableDatabase().execSQL(sqlMatchQuery);
        dbHelper.getWritableDatabase().execSQL(sqlMatchScoreQuery);
        dbHelper.getWritableDatabase().execSQL(sqlX01Query);
    }

    private int getLatestMatchId() {
        int matchId = 0;
        String sql = "SELECT max(m._id) as m_max FROM [match] m;";

        try (Cursor c = dbHelper.getWritableDatabase().rawQuery(sql, null)) {
            while (c.moveToNext()) {
                matchId = c.getInt(c.getColumnIndex("m_max"));
            }
        }
        return matchId;
    }


    public void test(){
        String sqlCreateStatisticsView_1 =
                "CREATE VIEW IF NOT EXISTS statistics_view_1 AS " +
                        "SELECT m._id as m_id, m.game_type, " +
                        "x.x, m.winner_id, ms.score as ms_score, ms._id as ms_id, " +
                        "count(ms.score) as ms_count, max(ms._id) as ms_max_id " +
                        "FROM x01 x " +
                        "INNER JOIN [match] m ON x.match_id == m_id " +
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
                        "INNER JOIN [match] m ON x.match_id == m_id " +
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
        dbHelper.getWritableDatabase().execSQL(sqlCreateStatisticsView_1);
        dbHelper.getWritableDatabase().execSQL(sqlCreateStatisticsViewAll);
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

    private void runQueryAndPrintTable(String sqlQuery){
        ArrayList<String> headers = new ArrayList<>();
        String baseFormatString = new String();
        int maxHeaderLen = 5;

        Cursor c = dbHelper.getWritableDatabase().rawQuery(sqlQuery, null);

        for (int i=0; i< c.getColumnCount(); i++)
            maxHeaderLen = Math.max(maxHeaderLen, c.getColumnName(i).length());
        for (int i=0; i< c.getColumnCount(); i++) {
            headers.add(c.getColumnName(i));
            baseFormatString += "%" + maxHeaderLen + "s  |";
        }

        Log.d("DartLogDebug", String.format(baseFormatString, headers.toArray()));
        while (c.moveToNext()) {
            ArrayList<String> values = new ArrayList<>();
            for (int i=0; i< c.getColumnCount(); i++){
                if(c.isNull(i))
                    values.add(null);
                else
                    values.add(c.getString(i));
            }
            Log.d("DartLogDebug", String.format(baseFormatString, values.toArray()));
        }
        c.close();
    }

    private void runQueryAndLogStatistics(String sqlQuery){
        long startTime = System.currentTimeMillis();
        Cursor c = dbHelper.getWritableDatabase().rawQuery(sqlQuery, null);

        while (c.moveToNext()) {   }

        long executionTime = System.currentTimeMillis() - startTime;
        Log.d("DartLogDebug", "exekveringstid: " + executionTime + "ms");
        c.close();
    }
}
