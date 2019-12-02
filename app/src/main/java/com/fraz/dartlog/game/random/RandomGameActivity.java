package com.fraz.dartlog.game.random;

import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.OnBackPressedDialogFragment;
import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ActivityRandomGameBinding;
import com.fraz.dartlog.game.InputEventListener;
import com.fraz.dartlog.game.NumPadHandler;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.viewmodel.RandomGameViewModel;

public class RandomGameActivity extends AppCompatActivity implements
        OnBackPressedDialogFragment.OnBackPressedDialogListener {

    private RandomGameListAdapter gameListAdapter;
    private RandomGameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(RandomGameViewModel.class);
        ActivityRandomGameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_random_game);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setGame(viewModel.getGameObservable());

        setSupportActionBar((Toolbar) findViewById(R.id.game_toolbar));

        viewModel.initGame(savedInstanceState, getIntent());
        gameListAdapter = new RandomGameListAdapter(viewModel);

        initListView();
        initNumPadView();

        observeViewModel();
    }

    @Override
    public void onBackPressed() {
        DialogFragment onBackPressedDialogFragment = new OnBackPressedDialogFragment();
        onBackPressedDialogFragment.show(getFragmentManager(), "OnBackPressedDialogFragment");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("randomGame", viewModel.getGame());
    }

    public void observeViewModel()
    {
        viewModel.getGameObservable().observe(this, new Observer<Random>() {
            @Override
            public void onChanged(@Nullable Random random) {
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
        NumPadHandler numPadHandler = new NumPadHandler((ViewGroup) findViewById(R.id.num_pad), 9);
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
    public void onDialogPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing.
    }
}
