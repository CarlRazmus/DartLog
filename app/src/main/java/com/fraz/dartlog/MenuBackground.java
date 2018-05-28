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
import android.util.Log;
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
        PrimaryDrawerItem gameX01Item = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.x01).withIcon(R.drawable.ic_home_white_24dp);
        SecondaryDrawerItem gamesHeader = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.games);
        PrimaryDrawerItem gameRandomItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.random).withIcon(R.drawable.ic_home_white_24dp);
        PrimaryDrawerItem profilesItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profiles).withIcon(R.drawable.ic_group_white_24dp);
        PrimaryDrawerItem addProfileItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.add_profile).withIcon(R.drawable.ic_person_add);
        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.settings).withIcon(R.drawable.ic_settings_white_24dp);

        // Create an empty AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(parentActivity)
                .withHeaderBackground(R.drawable.profile_background)
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
                        gamesHeader,
                        gameX01Item,
                        gameRandomItem,
                        new DividerDrawerItem(),
                        profilesItem,
                        addProfileItem,
                        new DividerDrawerItem(),
                        settingsItem
                )
                .withFooterDivider(true)
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
    private final int X01 = 4;
    private final int RANDOM = 5;
    private final int PROFILES = 7;
    private final int ADD_PROFILE = 8;
    private final int SETTINGS = 10;

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
            default:
                throw new Error("Non defined item clicked");
        }
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, AppSettingsActivity.class);
        startActivity(intent);
    }

    private void openAddPlayerDialog() {
        new AddPlayerDialogFragment().show(getSupportFragmentManager(),
                "AddPlayerDialogFragment");
    }

    private void startGameActivity(String gameType) {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("gameType", gameType);
        startActivity(intent);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openFriendsActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }

    public static class AddPlayerDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.GreenButtonAlertDialog);

            builder.setTitle("Add new profile").setView(R.layout.dialog_add_player)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditText profileNameEditText =
                                    (EditText) getDialog().findViewById(R.id.add_player_edit_text);
                            String name = profileNameEditText.getText().toString();
                            DartLogDatabaseHelper dbHelper = new DartLogDatabaseHelper(getContext());
                            if(!dbHelper.playerExist(name)) {
                                if (dbHelper.addPlayer(name) != -1) {
                                    Util.addPlayer(name, getContext());

                                    if(getActivity().getLocalClassName().equals(ProfileListActivity.class.getName()))
                                    {
                                        ((ProfileListActivity)getActivity()).updateProfileList();
                                    }
                                }
                            }
                            else if(!Util.loadProfileNames(getContext()).contains(name))
                                Util.addPlayer(name, getContext());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
