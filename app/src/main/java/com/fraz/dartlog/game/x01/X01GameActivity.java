package com.fraz.dartlog.game.x01;

import android.app.DialogFragment;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ActivityX01GameBinding;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.viewmodel.X01GameViewModel;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;


public class X01GameActivity extends AppCompatActivity implements
        OnBackPressedDialogFragment.OnBackPressedDialogListener {

    private X01GameListAdapter gameListAdapter;
    private X01GameViewModel viewModel;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            socket = IO.socket("http://192.168.28.157:5000");
            socket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        viewModel = ViewModelProviders.of(this).get(X01GameViewModel.class);
        ActivityX01GameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_x01_game);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setGame(viewModel.getGameObservable());

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));

        viewModel.initGame(savedInstanceState, getIntent());
        gameListAdapter = new X01GameListAdapter(viewModel, this);

        initNumPadView();
        initListView();

        observeViewModel();

        hideSystemBars();
    }

    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) return;

        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", viewModel.getGame());
    }

    public void observeViewModel()
    {
        viewModel.getGameObservable().observe(this, new Observer<X01>() {
            @Override
            public void onChanged(@Nullable X01 x01) {
                sendGameDataToServer();
                gameListAdapter.notifyDataSetChanged();
                scrollToPlayerInList();
            }
        });
        viewModel.getCompleteMatchEvent().observe(this, new EventObserver<String>() {
            @Override
            public void onEventUnhandled(String content) {
                completeMatch();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                viewModel.undo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    private void sendGameDataToServer()
    {
        final String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String winner = "None";
        if (viewModel.getGame().isGameOver())
            winner = viewModel.getGame().getWinner().getPlayerName();

        PlayerData player1 = viewModel.getGame().getPlayer(0);
        PlayerData player2 = viewModel.getGame().getPlayer(1);
        Map player1data = new HashMap();
        Map player2data = new HashMap();
        player1data.put("name", player1.getPlayerName());
        player2data.put("name", player2.getPlayerName());
        LinkedList player1list = (LinkedList)player1.getTotalScoreHistory().clone();
        LinkedList player2list = (LinkedList)player2.getTotalScoreHistory().clone();

        player1list.add(player1.getScore());
        player2list.add(player2.getScore());
        player1data.put("matchdata", player1list);
        player2data.put("matchdata", player2list);

        Map match = new HashMap();
        match.put("winner", winner);
        match.put("player1", player1data);
        match.put("player2", player2data);

        Map data = new HashMap();
        data.put("device_id", android_id);
        data.put("match", match);

        socket.emit("matchdata-update-from-android", new JSONObject(data));
    }

    private void initListView() {
        RecyclerView myListView = findViewById(R.id.play_players_listView);
        assert myListView != null;
        myListView.setAdapter(gameListAdapter);
    }

    private void scrollToPlayerInList() {
        RecyclerView playersListView = findViewById(R.id.play_players_listView);
        assert playersListView != null;
        playersListView.smoothScrollToPosition(viewModel.getGame().getCurrentPlayerIdx());
    }

    private void initNumPadView() {
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad), 180);
        numPadHandler.setListener(new InputEventListener() {
            @Override
            public void enter(int score) {
                viewModel.submitScore(score);
            }
        });
    }

    private void completeMatch() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
        onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}
