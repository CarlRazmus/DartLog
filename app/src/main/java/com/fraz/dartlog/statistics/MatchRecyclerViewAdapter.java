package com.fraz.dartlog.statistics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;
import java.util.Collections;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GameData> gameData;
    private String playerName;

    public MatchRecyclerViewAdapter(Context context, ArrayList<GameData> gameData, String playerName) {
        this.context = context;
        this.gameData = gameData;
        this.playerName = playerName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MatchItemView view = new MatchItemView(context);
        return new ViewHolder(view);    
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GameData game = gameData.get(position);

        ((MatchItemView) holder.itemView).setGame(game, playerName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MatchPagerActivity.class);
                intent.putExtra(MatchPagerActivity.ARG_ITEM_NAME, playerName);
                intent.putExtra(MatchPagerActivity.ARG_ITEM_POSITION, holder.getAdapterPosition());
                intent.putExtra(MatchPagerActivity.ARG_MATCHES, gameData.size());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return gameData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
}
