package com.fraz.dartlog.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;

import static com.fraz.dartlog.statistics.ProfileDetailFragment.ARG_ITEM_NAME;

public class MatchHistoryFragment extends Fragment {

    private String profileName;
    private ArrayList<GameData> playerGameData;
    private DartLogDatabaseHelper databaseHelper;
    private long lastLoadedMatchId = -1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchHistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_NAME)) {
            profileName = getArguments().getString(ARG_ITEM_NAME);
            databaseHelper = new DartLogDatabaseHelper(getActivity());
            playerGameData = databaseHelper.getPlayerMatchData(profileName, lastLoadedMatchId, 2);
            lastLoadedMatchId = databaseHelper.getLastLoadedMatchId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_history_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final MatchRecyclerViewAdapter recyclerViewAdapter = new MatchRecyclerViewAdapter(getContext(),
                    playerGameData, profileName);
            recyclerView.setAdapter(recyclerViewAdapter);

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(1)) {
                        int size = playerGameData.size();
                        ArrayList<GameData> newData = databaseHelper.getPlayerMatchData(profileName, lastLoadedMatchId, 2);
                        int sizeNewData = newData.size();
                        playerGameData.addAll(newData);
                        recyclerViewAdapter.notifyItemRangeInserted(size, sizeNewData);
                        lastLoadedMatchId = databaseHelper.getLastLoadedMatchId();
                        Log.d("lastLoadedMatchId", String.valueOf(lastLoadedMatchId));
                    }
                }
            });
        }
        return view;
    }
}
