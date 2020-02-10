package com.fraz.dartlog.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.game.GameData;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class MatchPagerFragment extends Fragment {

    public static final String ARG_ITEM_POSITION = "ARG_POSITION";
    private int position;
    private ProfileViewModel profileViewModel;

    public static MatchPagerFragment newInstance(int position) {
        MatchPagerFragment matchPagerFragment = new MatchPagerFragment();
        Bundle args = new Bundle();
        args.putInt(MatchPagerFragment.ARG_ITEM_POSITION, position);
        matchPagerFragment.setArguments(args);
        return matchPagerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null)
        {
            position = arguments.getInt(ARG_ITEM_POSITION, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewPager view = (ViewPager) inflater.inflate(R.layout.activity_match_pager, container, false);

        view.addOnPageChangeListener(new OnPageChangeListener());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() == null) return;

        ViewPager pager = (ViewPager) getView();

        profileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        ArrayList<GameData> playerGameData = profileViewModel.getMatchHistory();

        final MatchPagerAdapter adapter = new MatchPagerAdapter(getFragmentManager(), playerGameData);
        pager.setAdapter(adapter);
        profileViewModel.getMatchHistoryLoaded().observe(this, new EventObserver<Integer>() {
            @Override
            public void onEventUnhandled(Integer content) {
                adapter.notifyDataSetChanged();
            }
        });

        UpdateToolbar(position, pager);
        pager.setCurrentItem(position);
    }

    private void UpdateToolbar(int position, View view) {
        if (view != null)
        {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(
                        String.format(Locale.getDefault(),
                                "%s | Match %d",
                                profileViewModel.getProfileName(),
                                position + 1));
            }
        }
    }

    class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener
    {
        @Override
        public void onPageSelected(int position) {
            UpdateToolbar(position, getView());
        }
    }
}
