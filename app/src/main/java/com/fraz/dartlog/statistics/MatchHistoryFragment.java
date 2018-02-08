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
            DartLogDatabaseHelper databaseHelper = new DartLogDatabaseHelper(getActivity());
            playerGameData = databaseHelper.getPlayerMatchData(profileName, lastLoadedMatchId, 10);
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
            recyclerView.setAdapter(
                    new MatchRecyclerViewAdapter(getContext(), playerGameData, profileName));
        }
        return view;
    }
}
