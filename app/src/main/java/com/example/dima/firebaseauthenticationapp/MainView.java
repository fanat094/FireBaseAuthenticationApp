package com.example.dima.firebaseauthenticationapp;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

public interface MainView {

    void onReadyActivityStartForResult(Intent intent, int i);

    void updateUser(FirebaseUser user);
}
