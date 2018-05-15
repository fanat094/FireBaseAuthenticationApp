package com.example.dima.firebaseauthenticationapp;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FireBaseGoogleSigInPresenter {

    private static final int RC_SIGN_IN = 9001;

    GoogleInView view;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void attachView(GoogleInView view) {
        this.view = view;
    }

    public void signIn(Intent signInIntent, FirebaseAuth mAuth,
                       GoogleApiClient mGoogleApiClient) {

        signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        view.onReadyActivityStartForResult(signInIntent, RC_SIGN_IN);
    }


    public void signOut(GoogleApiClient mGoogleApiClient) {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        view.updateUser(null);
                    }
                });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.updateUser(user);
                        } else {

                        }
                    }
                });
    }

}
