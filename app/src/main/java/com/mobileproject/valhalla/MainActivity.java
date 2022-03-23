package com.mobileproject.valhalla;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import com.mobileproject.valhalla.utils.GoogleClient;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    // this activity provides a sign in functionality, so it needs a GoogleClient object
    GoogleClient googleClient;

    // views needed from the xml file
    TextView welcomeStatement;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove top action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Make sure to run the application only in portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // get the components from the view
        fab = findViewById(R.id.fab);
        welcomeStatement = findViewById(R.id.welcomeStatement);

        // initiate the googleClient object with an instance of this activity
        googleClient = new GoogleClient(this);


        //check if user is authenticated
        if (googleClient.isSignedIn()) {
            // if user is authenticated do the following

            // 1. change the welcome statement from
                // "Welcome"
                // to
                // "Welcome, username"
            welcomeStatement.setText("Welcome, " + googleClient.getUsername());

            // 2. change the floating action button image from default (login) icon
                // to profile icon
            fab.setImageResource(R.drawable.profile);

            // 3. set the floating action button to direct to profile page
            fab.setOnClickListener(v -> directToProfilePage());

        } else {
            // if user is not authenticated do the following

            // 1. set the floating action button to login using google
            fab.setOnClickListener(v -> loginWithGoogle());

        }

    }

    // show the google login intent using the googleClient object
    public void loginWithGoogle() {
        Intent signInIntent = googleClient.getGoogleSignInClient().getSignInIntent();

        // start the login activity and wait for the result
        // NOTE: the result will be resolved in "onActivityResult" override method below
        startActivityForResult(signInIntent, googleClient.SIGNIN_CODE);
    }

    // resolve the result from the login intent to sign in the user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // result returned from launching the Intent from getGoogleSignInClient.getSignInIntent();
        if (requestCode == googleClient.SIGNIN_CODE) {
            // save the results in a Task object to resolve it in GoogleSignInAccount object
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // after google login is successful, authenticate with firebase using the ID token
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "login failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void firebaseAuthWithGoogle(String idToken) {
        // using the ID token, create a GoogleAuthProvider object to get the user credentials
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        // use the credential to authenticate the user with firebase
        googleClient.getFirebaseAuth().signInWithCredential(credential)
                // after the authenticate is complete
                .addOnCompleteListener(this, task -> {

                    // check id the auth is success or not
                    if (task.isSuccessful()) {
                        // login success, update UI with the signed-in user's information
                        Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // a function to direct the user to the profile page
    private void directToProfilePage() {
        // since the profile page do not need any data from this page
        // the intent is empty and the profile page will start immediately
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // a function to direct the user to the leaderboard page
    private void directToLeaderboardPage(String gameName){
        // since the profile page need only the game name from this page
        // the intent will save the game name and start the leaderboard page
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("GAMENAME", gameName);
        startActivity(intent);
    }

    // function to toast if user try to enter a game without logging in
    private boolean canEnterGame() {
        // check if the user is not signed in
        if (!googleClient.isSignedIn()) {
            // toast the user and return false
            Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return false;
        }
        // if signed in: return true
        return true;
    }

    // a set of functions to start each game using the directToLeaderboardPage() function
    public void startGameCOD(View view) {
        // if user tries to enter this game without authentication
        if (!canEnterGame()) return;
        directToLeaderboardPage("cod");
    }

    public void startGameVALO(View view) {
        // if user tries to enter this game without authentication
        if (!canEnterGame()) return;
        directToLeaderboardPage("valo");
    }

    public void startGameFORT(View view) {
        // if user tries to enter this game without authentication
        if (!canEnterGame()) return;
        directToLeaderboardPage("fort");
    }

    public void startGameOW(View view) {
        // if user tries to enter this game without authentication
        if (!canEnterGame()) return;
        directToLeaderboardPage("ow");
    }
}