package com.fraz.dartlog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by CarlR on 25/03/2017.
 */

public class MenuBackground extends AppCompatActivity {

    Drawer navigationDrawer;
    protected Toolbar myToolbar;
    Activity parentActivity;

    protected void onCreate(Bundle savedInstanceState, Activity parentActivity, int parentView) {
        super.onCreate(savedInstanceState);
        setContentView(parentView);

        this.parentActivity = parentActivity;
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        initializeAndPopulateNavigationDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parentActivity activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void initializeAndPopulateNavigationDrawer(){
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_home).withIcon(R.drawable.ic_home_white_24dp).withIconColorRes(R.color.md_red_700);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_friends).withIcon(R.drawable.ic_group_white_24dp).withIconColorRes(R.color.md_red_700);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_statistics).withIcon(R.drawable.ic_poll_white_24dp);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(parentActivity)
                .withHeaderBackground(R.drawable.profile_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("").withEmail("").withIcon(getResources().getDrawable(R.drawable.ic_account_circle_white_36dp))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        navigationDrawer = new DrawerBuilder()
                .withActivity(parentActivity)
                .withToolbar(myToolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.background_transparent))
                .withAccountHeader(
                        headerResult
                )
                .addDrawerItems(
                        homeItem,
                        new DividerDrawerItem(),
                        item2,
                        item1
                )
                .withFooterDivider(true)
             /*   .addStickyDrawerItems(
                        item4,
                        item3,
                        item5
                )*/
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        navigationDrawer.closeDrawer();
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
    }
}
