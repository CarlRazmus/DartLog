package com.fraz.dartlog.statistics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;
import com.fraz.dartlog.databinding.ActivityProfileListBinding;
import com.fraz.dartlog.databinding.ProfileListItemBinding;
import com.fraz.dartlog.util.BindingRecyclerViewHolder;
import com.fraz.dartlog.util.EventObserver;
import com.fraz.dartlog.view.AddPlayerDialogFragment;
import com.fraz.dartlog.viewmodel.ProfileListViewModel;

import java.util.ArrayList;

/**
 * An activity representing a list of Profiles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProfileDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProfileListActivity extends MenuBackground {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPaneMode;
    RecyclerView recyclerView;
    ProfileListViewModel profileListViewModel;

    public ProfileListActivity(){
        super(R.layout.activity_profile_list);
        setParentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myToolbar.setTitle(getTitle());


        recyclerView = findViewById(R.id.profile_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        if (findViewById(R.id.profile_detail_container) != null) {
            twoPaneMode = true;
        }

        profileListViewModel = ViewModelProviders.of(this).get(ProfileListViewModel.class);
        ActivityProfileListBinding binding = ActivityProfileListBinding.bind(findViewById(R.id.profile_list_root));
        binding.setViewModel(profileListViewModel);

        observeViewModel(profileListViewModel);
    }


    public void observeViewModel(ProfileListViewModel viewModel)
    {
        viewModel.getProfilesObservable().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> profiles) {
                if (profiles != null)
                {
                    ProfilesRecyclerViewAdapter adapter = (ProfilesRecyclerViewAdapter) recyclerView.getAdapter();
                    if(adapter != null)
                    {
                        adapter.updateProfiles(profiles);
                    }
                }
            }
        });
        viewModel.getAddPlayerClickEvent().observe(this, new EventObserver<String>() {
            @Override
            public void onEventUnhandled(String content) {
                new AddPlayerDialogFragment().show(getSupportFragmentManager(),
                        "AddPlayerDialogFragment");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ProfilesRecyclerViewAdapter());
    }

    public class ProfilesRecyclerViewAdapter
            extends RecyclerView.Adapter<BindingRecyclerViewHolder> {

        private ArrayList<String> profiles;

        void updateProfiles(ArrayList<String> profiles) {
            this.profiles = profiles;
            notifyDataSetChanged();
        }

        @Override
        @NonNull
        public BindingRecyclerViewHolder<ProfileListItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ProfileListItemBinding binding = ProfileListItemBinding.inflate(
                    layoutInflater, parent, false);
            return new BindingRecyclerViewHolder<>(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull final BindingRecyclerViewHolder holder, int position) {
            final ProfileListItemBinding binding = (ProfileListItemBinding) holder.getBinding();
            binding.setName(profiles.get(position));
            // Setup view change on click
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPaneMode) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ProfileDetailFragment.ARG_ITEM_NAME, binding.getName());
                        ProfileDetailFragment fragment = new ProfileDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.profile_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProfileDetailActivity.class);
                        intent.putExtra(ProfileDetailFragment.ARG_ITEM_NAME, binding.getName());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

    }
}
