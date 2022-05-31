package com.fraz.dartlog.statistics;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.R;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

public class MatchHistoryFragment extends Fragment {
    private MatchRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MatchHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_history_list, container, false);

        if (view instanceof RecyclerView) {
            ProfileViewModel profileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);

            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);

            recyclerViewAdapter = new MatchRecyclerViewAdapter(getContext(), profileViewModel.getMatchHistory(), profileViewModel.getProfileName());
            profileViewModel.getMatchHistoryLoaded().observe(getViewLifecycleOwner(), new EventObserver<Integer>() {
                @Override
                public void onEventUnhandled(Integer content) {
                    int itemCount = recyclerViewAdapter.getItemCount();
                    recyclerViewAdapter.notifyItemRangeRemoved(itemCount - content, content);
                    recyclerViewAdapter.notifyItemRangeInserted(itemCount - content, content);
                }
            });
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        return view;
    }
}
