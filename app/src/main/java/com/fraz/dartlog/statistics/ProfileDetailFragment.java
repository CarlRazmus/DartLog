package com.fraz.dartlog.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ProfileDetailBinding;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

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

        EventObserver<String> observer = new EventObserver<String>() {
            @Override
            public void onEventUnhandled(String content) {
                Profile profile = profileViewModel.getProfile().getValue();

                LinearLayout turnsLayout = rootView.findViewById(R.id.fewest_turns_linear_layout);
                turnsLayout.removeAllViews();
                addFewestTurnsGames(profile.getFewestTurns301Game(), turnsLayout);
                addFewestTurnsGames(profile.getFewestTurns501Game(), turnsLayout);

                LinearLayout checkoutLayout = rootView.findViewById(R.id.highest_out_linear_layout);
                checkoutLayout.removeAllViews();
                addHighestOutGame(profile.getHighestCheckoutGame(), checkoutLayout);
            }
        };
        profileViewModel.getHighScoresLoaded().observe(getViewLifecycleOwner(), observer);
        return rootView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    private void addFewestTurnsGames(GameData game, ViewGroup layout) {
        if (game != null)
            addFewestTurnsView(game, layout);
    }

    private void addHighestOutGame(GameData game, ViewGroup layout ) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.CHECKOUT);
        addGameView(matchItemView, game, layout);
    }

    private void addFewestTurnsView(final GameData game, ViewGroup linearLayout) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.TURNS);
        addGameView(matchItemView, game, linearLayout);
    }

    private void addGameView(MatchItemView matchItemView, final GameData game, ViewGroup layout) {
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

        layout.addView(matchItemView);
    }
}
