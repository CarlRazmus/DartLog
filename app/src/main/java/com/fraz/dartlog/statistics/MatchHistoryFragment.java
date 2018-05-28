package com.fraz.dartlog.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;

import java.util.ArrayList;

import static com.fraz.dartlog.statistics.ProfileDetailFragment.ARG_ITEM_NAME;

public class MatchHistoryFragment extends Fragment {

    private String profileName;
    private ArrayList<GameData> playerGameData;
    private DartLogDatabaseHelper databaseHelper;
    private long lastLoadedMatchId = Long.MAX_VALUE;
    private boolean allLoaded = false;
    private int AMOUNT_ITEMS_TO_LOAD = 20;

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
            databaseHelper = DartLogDatabaseHelper.getInstance(getActivity());
            playerGameData = databaseHelper.getPlayerMatchData(profileName, lastLoadedMatchId, AMOUNT_ITEMS_TO_LOAD);
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
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            final MatchRecyclerViewAdapter recyclerViewAdapter = new MatchRecyclerViewAdapter(getContext(),
                    playerGameData, profileName);
            recyclerView.setAdapter(recyclerViewAdapter);

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (allLoaded)
                        return;
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (pastVisibleItems + visibleItemCount >= totalItemCount - 10) {
                        final int size = playerGameData.size();
                        ArrayList<GameData> newData = databaseHelper.getPlayerMatchData(profileName, lastLoadedMatchId, AMOUNT_ITEMS_TO_LOAD);
                        final int sizeNewData = newData.size();
                        if (sizeNewData < AMOUNT_ITEMS_TO_LOAD)
                            allLoaded = true;
                        playerGameData.addAll(newData);

                        recyclerView.post(new Runnable() {
                            public void run() {
                                recyclerViewAdapter.notifyItemRangeInserted(size, sizeNewData);
                            }
                        });
                        lastLoadedMatchId = databaseHelper.getLastLoadedMatchId();
                    }
                }
            });
        }
        return view;
    }
}
