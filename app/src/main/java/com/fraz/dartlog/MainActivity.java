package com.fraz.dartlog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fraz.dartlog.game.setup.SetupActivity;
import com.fraz.dartlog.statistics.ProfileListActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import static com.google.android.gms.common.api.CommonStatusCodes.SIGN_IN_REQUIRED;

public class MainActivity extends MenuBackground implements  GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {



    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "SignInActivity";
    private Dialog logInDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this, R.layout.activity_main);
        Button playButton = (Button) findViewById(R.id.play_x01_button);
        Button profilesButton = (Button) findViewById(R.id.profiles_button);
        Button randomButton = (Button) findViewById(R.id.play_random_button);
        logInDialog = new Dialog(this);

        assert playButton != null;
        assert profilesButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        logInDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.exit(1);
            }
        });

        Button logOutButton = (Button) findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.x01_preferences, false);

        // Configure sign-in. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        User.setGoogleApiClient(mGoogleApiClient);

        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (pendingResult.isDone()) {
            // There's immediate result available.
            handleSignInResult(pendingResult.get());
            initializeAndPopulateNavigationDrawer();
        } else {
            final Bundle bundle = savedInstanceState;
            // There's no immediate result ready.
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                    initializeAndPopulateNavigationDrawer();
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "SIGN IN RESULT: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("SIGN_IN", "Successful Sign in");
            Log.d("SIGN_IN", "name: " + acct.getDisplayName());
            Log.d("SIGN_IN", "email: " + acct.getEmail());
            User.setFullName(acct.getDisplayName());
            User.setEmail(acct.getEmail());
            if(logInDialog.isShowing())
                logInDialog.dismiss();

        } else {
            // Sign in unsuccessful, show log in UI.
            if(result.getStatus().getStatusCode() != SIGN_IN_REQUIRED)
                Log.d("SIGN_IN", "Failed to log in with status code: " + result.getStatus().getStatusCode());
            if(!logInDialog.isShowing())
                openLogInDialog();
        }
    }

    private void openLogInDialog() {
        logInDialog.show();
        logInDialog.setTitle("Log in");
        logInDialog.setContentView(R.layout.login_dialog);
        logInDialog.setCancelable(true);

        SignInButton signInButton = (SignInButton)logInDialog.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
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
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("CONNECTION STATUS:", "CONNECTION FAILED");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.play_x01_button:
                startPlayersActivity("x01");
                break;
            case R.id.profiles_button:
                startProfileActivity();
                break;
            case R.id.play_random_button:
                startPlayersActivity("random");
                break;
        }
    }

    private void startPlayersActivity(String gameType) {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("gameType", gameType);
        startActivity(intent);
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileListActivity.class);
        startActivity(intent);
    }
}
