package com.studentcollab.Activities;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Models.University;
import com.studentcollab.Models.User;
import com.studentcollab.R;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean validEmail = false;
    private boolean validPassword = false;
    private LoadingDialog loadingDialog;
    private View rootLayout;
    private final int signUpRequestCode = 1;
    private  FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(1048576)
                .build();
        db.setFirestoreSettings(settings);*/


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button singUpButton = findViewById(R.id.sign_up);
        final Context context = this.getApplicationContext();

        rootLayout = findViewById(R.id.root_layout);
        loadingDialog = new LoadingDialog(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();
/*
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser != null){
            Methods.setGlobalUser(currentUser);

            loadingDialog.start();
            //get user details and check if acc is initialized
            db.collection("users")
                    .whereEqualTo("userId", Variables.user.getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            loadingDialog.dismiss();
                            if (task.isSuccessful()) {
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                                if(documents.size() > 0)
                                {
                                    Variables.user = documents.get(0).toObject(User.class);
                                    if(documents.get(0).getBoolean("initialized") != true) {
                                        //Log.d("aaa", Variables.user.getDocumentId());
                                        Variables.user.setInitialized(false);
                                        Intent intent = new Intent(context, OnboardingActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Variables.user.setInitialized(true);
                                        Intent intent = new Intent(context, FeedActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                                Log.d("aaa", String.valueOf(documents.size()));

                            } else {
                                Log.d("aaa", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }*/

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
                /*if(!Methods.validatePassword(value)) {
                    passwordEditText.setError(context.getResources().getString(R.string.login_activity_invalid_password));
                    validPassword = false;
                    loginButton.setEnabled(false);
                }
                else {*/
                    passwordEditText.setError(null);
                    validPassword = true;
                    if(validEmail)
                        loginButton.setEnabled(true);
                //}
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(LoginActivity.this);
                loadingDialog.start();
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(),  passwordEditText.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loadingDialog.dismiss();
                                //Log.d("aaa", Objects.requireNonNull(task.getException().getMessage()));
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Methods.setGlobalUser(user);

                                    db.collection("users")
                                            .whereEqualTo("userId", Variables.user.getUserId())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {
                                                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                                        //Log.d("documents length", String.valueOf(documents.size()));

                                                        if(documents.size() > 0)
                                                        {
                                                            Variables.user = documents.get(0).toObject(User.class);
                                                            Variables.user.setDocumentId(documents.get(0).getId());
                                                            if(documents.get(0).getBoolean("initialized") != true) {
                                                                //Log.d("aaa", Variables.user.getDocumentId());
                                                                Intent intent = new Intent(context, OnboardingActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Intent intent = new Intent(context, FeedActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }
                                                    } else {
                                                        //Log.d("aaa", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });



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
                startActivityForResult(intent, signUpRequestCode);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == signUpRequestCode) {
            if(resultCode == Activity.RESULT_OK){
                boolean result = data.getBooleanExtra("result", false);
                if(result)
                    Snackbar.make(rootLayout, R.string.sign_up_activity_successful, Snackbar.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }//onActivityResult

}
