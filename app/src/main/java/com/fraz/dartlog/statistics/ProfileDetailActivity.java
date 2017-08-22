package com.fraz.dartlog.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fraz.dartlog.MenuBackground;
import com.fraz.dartlog.R;

/**
 * An activity representing a single Profile detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProfileListActivity}.
 */
public class ProfileDetailActivity extends MenuBackground {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_profile_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_detail_view_pager);
        viewPager.setAdapter(new ProfileDetailFragmentPagerAdapter(getSupportFragmentManager(),
                getIntent().getStringExtra(ProfileDetailFragment.ARG_ITEM_NAME)));
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
        return super.onOptionsItemSelected(item);
    }
}
