package com.fraz.dartlog.statistics;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fraz.dartlog.R;
import com.fraz.dartlog.db.DartLogDatabaseHelper;

import java.util.ArrayList;

/**
 * An activity representing a list of Profiles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProfileDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProfileListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPlayerDialogFragment().show(getSupportFragmentManager(),
                        "AddPlayerDialogFragment");
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.profile_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.profile_detail_container) != null) {
            twoPaneMode = true;
        }
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
        recyclerView.setAdapter(new ProfilesRecyclerViewAdapter(this));
    }

    public class ProfilesRecyclerViewAdapter
            extends RecyclerView.Adapter<ProfilesRecyclerViewAdapter.ViewHolder> {

        private Context context;
        private ArrayList<String> profiles;

        public ProfilesRecyclerViewAdapter(Context context) {
            this.context = context;
            updateDataSetFromDatabase();
        }

        public void updateDataSetFromDatabase() {
            this.profiles = new DartLogDatabaseHelper(context).getPlayers();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = profiles.get(position);

            TextView profileNameView = (TextView) holder.mView.findViewById(R.id.profile_name);
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
            public final View mView;
            public String mItem;

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
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.RedButtonAlertDialog);

            builder.setTitle("Add new profile").setView(R.layout.dialog_add_player)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText profileNameEditText =
                                    (EditText) getDialog().findViewById(R.id.add_player_edit_text);
                            String name = profileNameEditText.getText().toString();
                            DartLogDatabaseHelper dbHelper = new DartLogDatabaseHelper(getContext());
                            dbHelper.addPlayer(name);
                            getProfilesAdapter().updateDataSetFromDatabase();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        private ProfilesRecyclerViewAdapter getProfilesAdapter()
        {
            return ((ProfilesRecyclerViewAdapter) ((RecyclerView) getActivity()
                        .findViewById(R.id.profile_list)).getAdapter());
        }
    }
}
