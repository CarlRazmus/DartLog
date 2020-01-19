package com.fraz.dartlog;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends MenuBackground implements View.OnClickListener {

    private DartLogDatabaseHelper dbHelper;

    public MainActivity() {
        super(R.layout.activity_main);
        setParentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util util = new Util();
        util.updateDbStatistics();

        Button playButton = findViewById(R.id.play_x01_button);
        Button profilesButton = findViewById(R.id.profiles_button);
        Button randomButton = findViewById(R.id.play_random_button);
        dbHelper = DartLogDatabaseHelper.getInstance();

        assert playButton != null;
        assert profilesButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.x01_preferences, false);
        randomButton.setOnClickListener(this);

        checkIfSharedPlayersPreferencesExists();

        //Initialize checkout chart now since it is expensive to load.
        CheckoutChart.initCheckoutMap();
    }

    private void checkIfSharedPlayersPreferencesExists() {
        ArrayList<String> playerNames = Util.loadProfileNames();
        /* If no preference file exist -> create an empty list, or get the player names from
         * the database (if the database contains any names) */
        if(playerNames == null || playerNames.isEmpty()) {
            if(dbHelper.getPlayers().size() > 0)
                Util.saveProfileNames(dbHelper.getPlayers());
            else
                Util.saveProfileNames(new ArrayList<String>());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play_x01_button:
                startPlayersActivity("x01");
                break;
            case R.id.profiles_button:
                startProfileActivity();
                break;
            case R.id.play_random_button:
                startPlayersActivity("random");
                break;
        }
    }

    private void startPlayersActivity(String gameType) {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("gameType", gameType);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
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
}
