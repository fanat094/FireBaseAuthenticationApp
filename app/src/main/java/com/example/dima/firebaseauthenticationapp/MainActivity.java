package com.example.dima.firebaseauthenticationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dima.firebaseauthenticationapp.facebook.FireBaseFacebookAuthenticationActivity;
import com.example.dima.firebaseauthenticationapp.email_password.FireBaseEmailPassAuthenticationActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener, GoogleInView {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.mStatusTextView) TextView mStatusTextView;
    @BindView(R.id.mDetailTextView) TextView mDetailTextView;
    @BindView(R.id.sign_in_button) SignInButton sign_in_button;
    @BindView(R.id.sign_out_button) Button sign_out_button;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.sign_out_and_disconnect) LinearLayout sign_out_and_disconnect;
    @BindView(R.id.mGoogleIconImageView) ImageView mGoogleIconImageView;

    FireBaseGoogleSigInPresenter fireBaseGoogleSigInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constr);

        fireBaseGoogleSigInPresenter = new FireBaseGoogleSigInPresenter();
        fireBaseGoogleSigInPresenter.attachView(this);

        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick({R.id.sign_in_button, R.id.sign_out_button})
    public void clickbind (View view) {
        switch(view.getId())
        {
           case R.id.sign_in_button:
               Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
               fireBaseGoogleSigInPresenter.signIn(signInIntent, mAuth, mGoogleApiClient);
               progressBar.setVisibility(View.VISIBLE);
               break;

           case R.id.sign_out_button:
               fireBaseGoogleSigInPresenter.signOut(mGoogleApiClient);
               break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReadyActivityStartForResult(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    @Override
    public void updateUser(FirebaseUser user) {

        if (user != null) {
            mStatusTextView.setText("Google Email: "+user.getEmail()+"\n"+
                    "Full Name: "+user.getDisplayName());
            mDetailTextView.setText("Firebase User: "+user.getUid());

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(mGoogleIconImageView);

            sign_in_button.setVisibility(View.GONE);
            sign_out_and_disconnect.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            mStatusTextView.setText("Signed Out");
            mDetailTextView.setText(null);
            mGoogleIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.logo_standard));

            sign_in_button.setVisibility(View.VISIBLE);
            sign_out_and_disconnect.setVisibility(View.GONE);
        }
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseGoogleSigInPresenter.firebaseAuthWithGoogle(account);
            } else {
                updateUser(null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.firebasefacebookitem:

                Intent firebasefacebookintent = new Intent(this, FireBaseFacebookAuthenticationActivity.class);
                startActivity(firebasefacebookintent);
                return true;

            case R.id.emailpasswordfacebookitem:

                Intent firebaseemailpasswordintent = new Intent(this, FireBaseEmailPassAuthenticationActivity.class);
                startActivity(firebaseemailpasswordintent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
