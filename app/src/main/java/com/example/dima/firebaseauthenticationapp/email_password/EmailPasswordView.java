package com.example.dima.firebaseauthenticationapp.email_password;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface EmailPasswordView {

    void updateUser(FirebaseUser user);
}
