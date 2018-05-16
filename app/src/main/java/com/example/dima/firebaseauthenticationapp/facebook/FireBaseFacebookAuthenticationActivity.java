package com.example.dima.firebaseauthenticationapp.facebook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dima.firebaseauthenticationapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FireBaseFacebookAuthenticationActivity extends AppCompatActivity implements FacebookView {

    private CallbackManager mCallbackManager;
    private static final String TAG = "FacebookAuthentication";

    private FirebaseAuth mAuth;

    FireBaseFacebookPresenter fireBaseFacebookPresenter;

    @BindView(R.id.mStatusTextView)
    TextView mStatusTextView;
    @BindView(R.id.mDetailTextView)
    TextView mDetailTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sign_out_and_disconnect)
    LinearLayout sign_out_and_disconnect;
    @BindView(R.id.mFacebookIconImageView)
    ImageView mFacebookIconImageView;
    @BindView(R.id.facebook_button_sign)
    Button mFacebookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_facebook_authentication);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        fireBaseFacebookPresenter = new FireBaseFacebookPresenter();
        fireBaseFacebookPresenter.attachView(this);

        fireBaseFacebookPresenter.facebookSignOut();


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            updateUser(currentUser);
        }
    }


    @Override
    public void onReadyActivityStartForResult(Intent intent, int i) {
        startActivityForResult(intent, i);
    }

    @OnClick({R.id.facebook_button_sign, R.id.sign_out_button})
    public void clickbind(View view) {

        switch (view.getId()) {
            case R.id.facebook_button_sign:

                //progressBar.setVisibility(View.VISIBLE);

                LoginManager.getInstance().logInWithReadPermissions(FireBaseFacebookAuthenticationActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        fireBaseFacebookPresenter.handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });
                break;

            case R.id.sign_out_button:
                fireBaseFacebookPresenter.facebookSignOut();
                break;
        }
    }


    @Override
    public void updateUser(FirebaseUser user) {

        if (user != null) {
            mStatusTextView.setText("Email: "+user.getEmail()+"\n"+
                    "Full Name: "+user.getDisplayName());
            mDetailTextView.setText("Firebase User: "+user.getUid());

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(mFacebookIconImageView);

            mFacebookBtn.setVisibility(View.GONE);
            sign_out_and_disconnect.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);

        } else {
            mStatusTextView.setText("Signed Out");
            mDetailTextView.setText(null);
            mFacebookIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.logo_standard));

            mFacebookBtn.setVisibility(View.VISIBLE);
            sign_out_and_disconnect.setVisibility(View.GONE);
        }

    }
}
