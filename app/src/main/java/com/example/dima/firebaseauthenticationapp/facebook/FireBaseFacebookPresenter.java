package com.example.dima.firebaseauthenticationapp.facebook;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.dima.firebaseauthenticationapp.GoogleInView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseFacebookPresenter {

    FacebookView facebookView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "FPres";

    public void attachView(FacebookView facebookView) {
        this.facebookView = facebookView;
    }

    public void handleFacebookAccessToken(AccessToken token) {

        Log.e(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            facebookView.updateUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            facebookView.updateUser(null);
                        }
                    }
                });
    }

    public void facebookSignOut(){

        mAuth.signOut();
        LoginManager.getInstance().logOut();
        facebookView.updateUser(null);
    }
}