package com.fraz.dartlog.statistics;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPlayerDialogFragment().show(getSupportFragmentManager(),
                        "AddPlayerDialogFragment");
            }
        });

        recyclerView = findViewById(R.id.profile_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.profile_detail_container) != null) {
            twoPaneMode = true;
        }

        profileListViewModel = ViewModelProviders.of(this).get(ProfileListViewModel.class);
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
            extends RecyclerView.Adapter<ProfilesRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> profiles;

        void updateProfiles(ArrayList<String> profiles) {
            this.profiles = profiles;
            notifyDataSetChanged();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mItem = profiles.get(position);

            TextView profileNameView = holder.mView.findViewById(R.id.profile_name);
            profileNameView.setText(holder.mItem);

            // Setup view change on click
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPaneMode) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ProfileDetailFragment.ARG_ITEM_NAME, holder.mItem);
                        ProfileDetailFragment fragment = new ProfileDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.profile_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProfileDetailActivity.class);
                        intent.putExtra(ProfileDetailFragment.ARG_ITEM_NAME, holder.mItem);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return profiles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            String mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
            }
        }
    }

    public static class AddPlayerDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add new profile").setView(R.layout.dialog_add_player)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText profileNameEditText =
                                    getDialog().findViewById(R.id.add_player_edit_text);
                            String name = profileNameEditText.getText().toString();
                            ProfileListViewModel profileListViewModel = ViewModelProviders.of(getActivity()).get(ProfileListViewModel.class);
                            profileListViewModel.AddProfile(name);
                        }
                    });
            return builder.create();
        }
    }
}
