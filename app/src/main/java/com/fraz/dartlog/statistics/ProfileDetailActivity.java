package com.fraz.dartlog.statistics;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fraz.dartlog.MainActivity;
import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;
import com.fraz.dartlog.Util;
import com.fraz.dartlog.game.setup.SetupActivity;

/**
 * An activity representing a single Profile detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProfileListActivity}.
 */
public class ProfileDetailActivity extends MenuBackground {

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_profile_detail);

        playerName = getIntent().getStringExtra(ProfileDetailFragment.ARG_ITEM_NAME);

        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_detail_view_pager);
        viewPager.setAdapter(new ProfileDetailFragmentPagerAdapter(getSupportFragmentManager(),
                playerName));
    }    @Override

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
                        Util.removePlayer(playerName, getApplicationContext());
                        startUpdatedProfileListActivity();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void startUpdatedProfileListActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }
}
