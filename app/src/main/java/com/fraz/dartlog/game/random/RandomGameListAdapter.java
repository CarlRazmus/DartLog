package com.fraz.dartlog.game.random;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameListAdapter;
import com.fraz.dartlog.game.PlayerData;
import com.fraz.dartlog.game.x01.X01GameListAdapter;

import java.util.LinkedList;

/**
 * Created by CarlR on 09/10/2016.
 */
public class RandomGameListAdapter extends GameListAdapter<GameListAdapter.ViewHolder> {

    private Random game;
    private Activity context;


    public RandomGameListAdapter(Activity context, Random game) {
        super(game);
        this.context = context;
        this.game = game;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.game_player_list_item, parent, false);
        return new GameListAdapter.ViewHolder(listItem);
    }
}
