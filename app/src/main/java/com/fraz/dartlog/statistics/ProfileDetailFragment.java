package com.fraz.dartlog.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDbHelper;
import com.fraz.dartlog.game.PlayerData;

import java.util.ArrayList;

/**
 * A fragment representing a single Profile detail screen.
 * This fragment is either contained in a {@link ProfileListActivity}
 * in two-pane mode (on tablets) or a {@link ProfileDetailActivity}
 * on handsets.
 */
public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_NAME = "item_name";

    /**
     * The content this fragment is presenting.
     */
    private ArrayList<PlayerData> playerData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DartLogDbHelper dartLogDbHelper = new DartLogDbHelper(getActivity());

        if (getArguments().containsKey(ARG_ITEM_NAME)) {
            String name = getArguments().getString(ARG_ITEM_NAME);
            playerData = dartLogDbHelper.getPlayerMatchData(name);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail, container, false);

        if (playerData != null) {
            ((TextView) rootView.findViewById(R.id.profile_detail_games_played))
                    .setText(Integer.toString(playerData.size()));
        }

        return rootView;
    }
}
