package com.fraz.dartlog.statistics;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;

public class MatchRecyclerViewAdapter extends RecyclerView.Adapter<MatchRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private ArrayList<GameData> gameData;
    private String playerName;

    MatchRecyclerViewAdapter(Context context, ArrayList<GameData> gameData, String playerName) {
        this.context = context;
        this.playerName = playerName;

        this.gameData = gameData;
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
                MatchPagerFragment pagerFragment = MatchPagerFragment.newInstance(holder.getAdapterPosition());
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
