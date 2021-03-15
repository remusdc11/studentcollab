package com.studentcollab.Activities;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.R;
import com.studentcollab.Models.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean validEmail = false;
    private boolean validPassword = false;
    private LoadingDialog loadingDialog;
    private View rootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button singUpButton = findViewById(R.id.sign_up);
        final Context context = this.getApplicationContext();

        rootLayout = findViewById(R.id.root_layout);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Only for testing reasons
        if(currentUser != null)
            mAuth.signOut();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Methods.setGlobalUser(currentUser);
            Intent intent = new Intent(context, FeedActivity.class);
            startActivity(intent);
            finish();
        }

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = usernameEditText.getText().toString();
                if(!Methods.validateEmail(value)) {
                    usernameEditText.setError(context.getResources().getString(R.string.login_activity_invalid_email));
                    validEmail = false;
                    loginButton.setEnabled(false);
                }
                else {
                    usernameEditText.setError(null);
                    validEmail = true;
                    if(validPassword)
                        loginButton.setEnabled(true);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = passwordEditText.getText().toString();
                if(!Methods.validatePassword(value)) {
                    passwordEditText.setError(context.getResources().getString(R.string.login_activity_invalid_password));
                    validPassword = false;
                    loginButton.setEnabled(false);
                }
                else {
                    passwordEditText.setError(null);
                    validPassword = true;
                    if(validEmail)
                        loginButton.setEnabled(true);
                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(LoginActivity.this);
                loadingDialog.startLoadingDialog();
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(),  passwordEditText.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loadingDialog.dismissLoadingDialog();
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Methods.setGlobalUser(user);
                                    Intent intent = new Intent(context, FeedActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Snackbar.make(rootLayout, R.string.login_activity_wrong_credentials, Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
