package com.example.dima.firebaseauthenticationapp.email_password;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.dima.firebaseauthenticationapp.facebook.FacebookView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseEmailPasswordPresenter {

    EmailPasswordView emailPasswordView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "FPres";

    public void attachView(EmailPasswordView emailPasswordView) {
        this.emailPasswordView = emailPasswordView;
    }

    public void emailPasswordCreateUser(String email, String  password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            emailPasswordView.updateUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            emailPasswordView.updateUser(null);
                        }

                        // ...
                    }
                });
    }

    public void emailPasswordSignIn(String email, String  password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                           // updateUI(null);
                        }

                        // ...
                    }
                });

    }


    public void emailPasswordSignOut(){

        mAuth.signOut();
        LoginManager.getInstance().logOut();
        emailPasswordView.updateUser(null);
    }
}