package com.fraz.dartlog;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.common.api.Status;

import static com.google.android.gms.common.api.CommonStatusCodes.SIGN_IN_REQUIRED;

public class MainActivity extends AppCompatActivity
        implements  GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "SignInActivity";
    private TextView loginStatusTextView;
    private SignInButton signInButton;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button playButton = (Button) findViewById(R.id.play_x01_button);
        Button profilesButton = (Button) findViewById(R.id.profiles_button);
        Button randomButton = (Button) findViewById(R.id.play_random_button);

        assert playButton != null;
        assert profilesButton != null;
        assert randomButton != null;

        playButton.setOnClickListener(this);
        profilesButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.x01_preferences, false);

        loginStatusTextView = (TextView) findViewById(R.id.login_status_textview);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(this);

        // Configure sign-in. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (pendingResult.isDone()) {
            // There's immediate result available.
            handleSignInResult(pendingResult.get());
        } else {
            // There's no immediate result ready.
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

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

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        loginStatusTextView.setText("User was logged out");
                        signInButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "SIGN IN RESULT: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            loginStatusTextView.setText("Logged in as \n" + acct.getDisplayName());
            signInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);

        } else {
            // Signed in unsuccessfully, show unauthenticated UI.
            if(result.getStatus().getStatusCode() != SIGN_IN_REQUIRED)
                loginStatusTextView.setText("Failed to log in with " +
                        "status code: " + result.getStatus().getStatusCode() + "\n");
        }
        loginStatusTextView.setVisibility(View.VISIBLE);

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
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
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
