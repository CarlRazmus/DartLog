package com.fraz.dartlog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fraz.dartlog.db.DartLogDatabaseHelper;
import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
        this.myToolbar = (Toolbar) findViewById(R.id.toolbar);

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
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_home).withIcon(R.drawable.ic_home_white_24dp);
        PrimaryDrawerItem gameX01Item = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.x01).withIcon(R.drawable.dart_board_400px);
        SecondaryDrawerItem gamesHeader = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.games);
        PrimaryDrawerItem gameRandomItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.random).withIcon(R.drawable.dart_board_400px);
        PrimaryDrawerItem profilesItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profiles).withIcon(R.drawable.ic_group_white_24dp);
        PrimaryDrawerItem addProfileItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.add_profile).withIcon(R.drawable.ic_person_add);
        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.settings).withIcon(R.drawable.ic_settings_white_24dp);
        PrimaryDrawerItem aboutItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.about).withIcon(R.drawable.ic_help_white_24dp);

        // Create an empty AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(parentActivity)
                .withHeaderBackground(R.drawable.profile_background)
                .build();

        navigationDrawer = new DrawerBuilder()
                .withActivity(parentActivity)
                .withToolbar(myToolbar)
                /*.withAccountHeader(
                        headerResult
                )*/
                .addDrawerItems(
                        homeItem,
                        new DividerDrawerItem(),
                        gamesHeader,
                        gameX01Item,
                        gameRandomItem,
                        new DividerDrawerItem(),
                        profilesItem,
                        addProfileItem,
                        new DividerDrawerItem(),
                        settingsItem,
                        aboutItem
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

    private final int HOME = 0;
    private final int X01 = 3;
    private final int RANDOM = 4;
    private final int PROFILES = 6;
    private final int ADD_PROFILE = 7;
    private final int SETTINGS = 9;
    private final int ABOUT = 10;

    private void itemClickedEvent(int position) {
        switch (position){
            case(HOME):
                openHomeActivity();
                break;
            case(X01):
                startGameActivity("x01");
                break;
            case(RANDOM):
                startGameActivity("random");
                break;
            case(PROFILES):
                openFriendsActivity();
                break;
            case(ADD_PROFILE):
                openAddPlayerDialog();
                break;
            case(SETTINGS):
                openSettingsActivity();
                break;
            case(ABOUT):
                openAboutActivity();
                break;
            default:
                throw new Error("Non defined item clicked");
        }
    }

    private void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        openActivity(AppSettingsActivity.class.getName(), intent);
    }


    private void openActivity(String className, Intent intent)
    {
        if(!parentActivity.getClass().getName().equals(className))
            startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, AppSettingsActivity.class);
        openActivity(AppSettingsActivity.class.getName(), intent);
    }

    private void openAddPlayerDialog() {
        new AddPlayerDialogFragment().show(getSupportFragmentManager(),
                "AddPlayerDialogFragment");
    }

    private void startGameActivity(String gameType) {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("gameType", gameType);
        openActivity(SetupActivity.class.getName(), intent);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openActivity(MainActivity.class.getName(), intent);
    }

    private void openFriendsActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        openActivity(ProfileListActivity.class.getName(), intent);
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
                                (EditText) getDialog().findViewById(R.id.add_player_edit_text);
                        String name = profileNameEditText.getText().toString();
                        DartLogDatabaseHelper dbHelper = DartLogDatabaseHelper.getInstance(getContext());
                        if(!dbHelper.playerExist(name)) {
                            if (dbHelper.addPlayer(name) != -1) {
                                Util.addPlayer(name, getContext());

                                    if(getActivity().getClass().getName().equals(ProfileListActivity.class.getName()))
                                    {
                                        ((ProfileListActivity)getActivity()).updateProfileList();
                                    }
                                }
                            }
                            else if(!Util.loadProfileNames(getContext()).contains(name))
                                Util.addPlayer(name, getContext());
                        }
                    });
            return builder.create();
        }
    }
}
