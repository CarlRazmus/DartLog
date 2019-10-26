package com.fraz.dartlog.game.random;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameListAdapter;

/**
 * Created by CarlR on 09/10/2016.
 */
public class RandomGameListAdapter extends GameListAdapter<GameListAdapter.ViewHolder> {

    public RandomGameListAdapter(Random game) {
        super(game);
    }

    @NonNull
    @Override
    public com.fraz.dartlog.game.GameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_player_list_item, parent, false);
        return new GameListAdapter.ViewHolder(listItem);
    }
}
