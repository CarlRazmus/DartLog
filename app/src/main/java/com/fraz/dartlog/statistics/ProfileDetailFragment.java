package com.fraz.dartlog.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ProfileDetailBinding;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

import java.util.HashMap;
import java.util.Locale;

/**
 * A fragment representing a single Profile detail screen.
 * This fragment is either contained in a {@link ProfileListActivity}
 * in two-pane mode (on tablets) or a {@link ProfileDetailActivity}
 * on handsets.
 */
public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_NAME = "item_name";

    private View rootView;
    private String profileName;
    private int unique_id = 0;
    ProfileViewModel profileViewModel;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_NAME)) {
            profileName = getArguments().getString(ARG_ITEM_NAME);
            profileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProfileDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.profile_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(profileViewModel);
        rootView = binding.getRoot();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null)
            toolbar.setTitle(profileName);

        return rootView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    /*private void addSummaryData() {
        LinearLayout profileLayout = rootView.findViewById(R.id.profile_detail_linear_layout);
        profileLayout.removeViewAt(0);
        View summaryView = getLayoutInflater().inflate(R.layout.profile_match_summary, null);
        ((TextView) summaryView.findViewById(R.id.profile_detail_games_played))
                .setText(String.format(Locale.getDefault(), "%d", gamesPlayed));

        ((TextView) summaryView.findViewById(R.id.profile_detail_games_won))
                .setText(String.format(Locale.getDefault(), "%d", gamesWon));
        profileLayout.addView(summaryView, 0);
    }

    private void addFewestTurnsGames() {
        LinearLayout fewestTurnsLayout = rootView.findViewById(R.id.profile_detail_fewest_turns_container);
        if (fewestTurns301Game != null)
            addFewestTurnsView(fewestTurns301Game, fewestTurnsLayout);
        if (fewestTurns501Game != null)
            addFewestTurnsView(fewestTurns501Game, fewestTurnsLayout);
    }

    private void addHighestOutGame( ) {
        LinearLayout linearLayout= rootView.findViewById(R.id.profile_detail_highest_out_container);
        if (highestX01CheckoutGame != null){
            MatchItemView matchItemView = new MatchItemView(getContext());
            matchItemView.setStatToShow(MatchItemView.Stat.CHECKOUT);
            addGameView(matchItemView, highestX01CheckoutGame, linearLayout);
        }
    }

    private void addFewestTurnsView(final GameData game, LinearLayout linearLayout) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.TURNS);
        addGameView(matchItemView, game, linearLayout);
    }

    private void addGameView(MatchItemView matchItemView, final GameData game, LinearLayout linearLayout) {
        matchItemView.setVisibility(View.VISIBLE);
        matchItemView.setGame(game, profileName);
        matchItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchFragment.newInstance(game).show(getFragmentManager(),
                        "game_overview_" + String.valueOf(unique_id));
                unique_id += 1;
            }
        });

        linearLayout.addView(matchItemView);
    }*/
}
