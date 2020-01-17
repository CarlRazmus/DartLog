package com.fraz.dartlog.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ProfileDetailBinding;
import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.model.Profile;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

import java.util.Locale;

/**
 * A fragment representing a single Profile detail screen.
 * This fragment is either contained in a {@link ProfileListActivity}
 * in two-pane mode (on tablets) or a {@link ProfileDetailActivity}
 * on handsets.
 */
public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_NAME = "item_name";

    ProfileViewModel profileViewModel;

    /**
     * Name of the profile this fragment is presenting.
     */
    private String profileName;
    private Profile profile;

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
            profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
            profileViewModel.setProfile(profileName);
            profile = profileViewModel.getProfile();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProfileDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.profile_detail, container, false);
        binding.setViewModel(profileViewModel);
        View rootView = binding.getRoot();

        Toolbar toolbar =
                getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(profileName);
        }

        initFewestTurnsGames((LinearLayout) rootView.findViewById(R.id.fewest_turns_linear_layout));
        initHighestOutGame((LinearLayout) rootView.findViewById(R.id.highest_out_linear_layout));
        return rootView;
    }

    private void initFewestTurnsGames(LinearLayout linearLayout) {
        if (profile.getFewestTurns301Game() != null)
            addFewestTurnsView(profile.getFewestTurns301Game(), linearLayout);
        if (profile.getFewestTurns501Game() != null)
            addFewestTurnsView(profile.getFewestTurns501Game(), linearLayout);
    }

    private void initHighestOutGame(LinearLayout linearLayout) {
        if (profile.getHighestCheckoutGame() != null)
            addCheckoutView(profile.getHighestCheckoutGame(), linearLayout);
    }

    private void addCheckoutView(final GameData game, LinearLayout linearLayout) {
        MatchItemView matchItemView = new MatchItemView(getContext());
        matchItemView.setStatToShow(MatchItemView.Stat.CHECKOUT);
        addGameView(matchItemView, game, linearLayout);
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
                        "overview_game");
            }
        });

        linearLayout.addView(matchItemView);
    }
}
