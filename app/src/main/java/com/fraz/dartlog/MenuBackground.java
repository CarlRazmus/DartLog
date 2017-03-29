package com.fraz.dartlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fraz.dartlog.statistics.ProfileDetailActivity;
import com.fraz.dartlog.statistics.ProfileDetailFragment;
import com.fraz.dartlog.statistics.ProfileListActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

    protected void initializeAndPopulateNavigationDrawer(){
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_home).withIcon(R.drawable.ic_home_white_24dp).withIconColorRes(R.color.md_red_700);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_friends).withIcon(R.drawable.ic_group_white_24dp).withIconColorRes(R.color.md_red_700);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_statistics).withIcon(R.drawable.ic_poll_white_24dp);

        PrimaryDrawerItem logOutItem = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.logOut);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(parentActivity)
                .withHeaderBackground(R.drawable.profile_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(User.getFullName()).withEmail(User.getEmail()).withIcon(getResources().getDrawable(R.drawable.ic_account_circle_white_36dp))
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
                .addStickyDrawerItems(
                        logOutItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        itemClickedEvent(position);
                        navigationDrawer.closeDrawer();
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
    }

    private final int HOME = 1;
    private final int STATISTICS = 3;
    private final int FRIENDS = 4;
    private final int SIGNOUT = -1;

    private void itemClickedEvent(int position) {
        switch (position){
            case(HOME):
                openHomeActivity();
                break;
            case(STATISTICS):
                openStatisticsActivity();
                break;
            case(FRIENDS):
                openFriendsActivity();
                break;
            case(SIGNOUT):
                signOut();
                break;
            default:
                throw new Error("Non defined item clicked: " + position);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(User.getmGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess()) {
                            User.clearAllData();
                            openHomeActivity();
                        }
                    }
                });
    }

    private void openStatisticsActivity() {
        //TODO This depends on whom are logged in. Change this later.
/*        Intent intent = new Intent(this, ProfileDetailActivity.class);
        intent.putExtra(ProfileDetailFragment.ARG_ITEM_NAME, "Filip");

        this.startActivity(intent);*/
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openFriendsActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }
}
