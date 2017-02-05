package com.fraz.dartlog.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;

public class MatchFragment extends Fragment {
    public static final String ARG_GAME_DATA = "game_data";

    public static MatchFragment newInstance(GameData game) {
        MatchFragment matchFragment = new MatchFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_GAME_DATA, game);
        matchFragment.setArguments(args);

        return matchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout layout =
                (LinearLayout) inflater.inflate(R.layout.fragment_match, container, false);
        Bundle args = getArguments();
        MatchTableView matchTable = (MatchTableView) layout.findViewById(R.id.match_table);
        matchTable.setGame((GameData) args.getSerializable(ARG_GAME_DATA));
        return layout;
    }
}
