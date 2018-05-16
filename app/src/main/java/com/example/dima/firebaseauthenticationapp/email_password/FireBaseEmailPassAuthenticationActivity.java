package com.example.dima.firebaseauthenticationapp.email_password;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dima.firebaseauthenticationapp.R;
import com.example.dima.firebaseauthenticationapp.facebook.FireBaseFacebookPresenter;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class FireBaseEmailPassAuthenticationActivity extends AppCompatActivity implements EmailPasswordView{

    private static final String TAG = "EmailPassAuthentication";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private FirebaseAuth mAuth;
    FireBaseEmailPasswordPresenter fireBaseEmailPasswordPresenter;

    @BindView(R.id.mStatusTextView)
    TextView mStatusTextView;
    @BindView(R.id.mDetailTextView)
    TextView mDetailTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sign_out_and_disconnect)
    LinearLayout sign_out_and_disconnect;
    @BindView(R.id.mEmailPasswordIconImageView)

    ImageView mEmailPasswordIconImageView;
    @BindView(R.id.email_pass_sign_in_btn)
    Button mEmailPasSignInBtn;

    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout textInputLayoutEmail;
    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_email_pass_authentication);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        fireBaseEmailPasswordPresenter = new FireBaseEmailPasswordPresenter();
        fireBaseEmailPasswordPresenter.attachView(this);

       // fireBaseEmailPasswordPresenter.facebookSignOut();
       // setUserData();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void updateUser(FirebaseUser user) {

        progressBar.setVisibility(View.GONE);
        if (user != null) {
            mStatusTextView.setText("Google Email: "+user.getEmail()+"\n"+
                    "Full Name: "+user.getDisplayName());
            mDetailTextView.setText("Firebase User: "+user.getUid());

            mEmailPasSignInBtn.setVisibility(View.GONE);
            sign_out_and_disconnect.setVisibility(View.VISIBLE);
            textInputLayoutEmail.setVisibility(View.GONE);
            textInputLayoutPassword.setVisibility(View.GONE);
            editTextEmail.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);

            editTextEmail.setText("");
            editTextPassword.setText("");
            progressBar.setVisibility(View.GONE);


        } else {
            mStatusTextView.setText("Signed Out");
            mDetailTextView.setText(null);
            mEmailPasswordIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.logo_standard));

            mEmailPasSignInBtn.setVisibility(View.VISIBLE);
            sign_out_and_disconnect.setVisibility(View.GONE);

            textInputLayoutEmail.setVisibility(View.VISIBLE);
            textInputLayoutPassword.setVisibility(View.VISIBLE);
            editTextEmail.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.email_pass_sign_in_btn, R.id.sign_out_button})
    public void clickemailpassword (View view) {
        switch(view.getId())
        {
            case R.id.email_pass_sign_in_btn:

                progressBar.setVisibility(View.VISIBLE);

                hideKeyboard();
                String sEmail = textInputLayoutEmail.getEditText().getText().toString();
                String sPassword = textInputLayoutPassword.getEditText().getText().toString();

                if (!validateEmail(sEmail)) {
                    textInputLayoutEmail.setError("Not a valid email address!");
                } else if (!validatePassword(sPassword)) {
                    textInputLayoutPassword.setError("Not a valid password!");
                } else {
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    fireBaseEmailPasswordPresenter.emailPasswordCreateUser(sEmail, sPassword);

                    Toast.makeText(this, "Create new user!",
                            Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.sign_out_button:
                fireBaseEmailPasswordPresenter.emailPasswordSignOut();
                break;
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

}