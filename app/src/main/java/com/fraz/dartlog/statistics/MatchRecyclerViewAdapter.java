package com.fraz.dartlog.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GameData> gameData;
    private String playerName;

    public MatchRecyclerViewAdapter(Context context, ArrayList<GameData> gameData, String playerName) {
        this.context = context;
        this.gameData = gameData;
        this.playerName = playerName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MatchItemView view = new MatchItemView(parent.getContext());
        return new ViewHolder(view);    
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        GameData game = gameData.get(position);

        ((MatchItemView) holder.itemView).setGame(game, playerName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                MatchPagerFragment pagerFragment = MatchPagerFragment.newInstance(playerName, holder.getAdapterPosition(), gameData.size());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.profile_fragment_container, pagerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return gameData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
        }
    }
}
