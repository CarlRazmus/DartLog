package com.fraz.dartlog.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;
import com.fraz.dartlog.view.ProfileFragment;
import com.fraz.dartlog.viewmodel.ProfileViewModel;

/**
 * An activity representing a single Profile detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProfileListActivity}.
 */
public class ProfileDetailActivity extends MenuBackground {

    private String profileName;
    ProfileViewModel profileViewModel;

    public ProfileDetailActivity(){
        super(R.layout.activity_profile_detail);
        setParentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileName = getIntent().getStringExtra(ProfileDetailFragment.ARG_ITEM_NAME);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.setProfile(profileName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.profile_fragment_container, new ProfileFragment());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ProfileListActivity.class));
            return true;
        }

        if(id ==  R.id.action_settings)
            showDeleteUserDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteUserDialog(){
        new AlertDialog.Builder(this)
            .setTitle("Delete user")
            .setMessage("Do you really want to delete the user? This will not remove database data.")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    profileViewModel.deleteProfile();
                    startUpdatedProfileListActivity();
                }})
            .setNegativeButton(android.R.string.no, null).show();
    }

    private void startUpdatedProfileListActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
