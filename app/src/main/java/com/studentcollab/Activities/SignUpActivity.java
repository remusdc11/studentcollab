package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean validEmail = false;
    private boolean validPassword = false;
    private boolean passwordsMatch = false;
    private Button signUpButton;
    private LoadingDialog loadingDialog;
    private View rootLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        rootLayout = findViewById(R.id.root_layout);
        loadingDialog = new LoadingDialog(SignUpActivity.this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText passwordVerificationEditText = findViewById(R.id.password_verification);
        signUpButton = findViewById(R.id.sign_up);
        signUpButton.setEnabled(false);
        final Context context = this.getApplicationContext();

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
                }
                else {
                    usernameEditText.setError(null);
                    validEmail = true;
                }
                updateButtonClickability();
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
                }
                else {
                    passwordEditText.setError(null);
                    validPassword = true;
                }
                String passwordCheck = passwordVerificationEditText.getText().toString();

                if(!value.equals(passwordCheck)) {
                    passwordVerificationEditText.setError(context.getResources().getString(R.string.sign_up_activity_password_verification_error));
                    passwordsMatch = false;
                }
                else {
                    passwordVerificationEditText.setError(null);
                    passwordsMatch = true;
                }

                updateButtonClickability();
            }
        });

        passwordVerificationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = passwordVerificationEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(!value.equals(password)) {
                    passwordVerificationEditText.setError(context.getResources().getString(R.string.sign_up_activity_password_verification_error));
                    passwordsMatch = false;
                }
                else {
                    passwordVerificationEditText.setError(null);
                    passwordsMatch = true;
                }
                updateButtonClickability();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(SignUpActivity.this);
                loadingDialog.start();
                mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(),  passwordEditText.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d("aaa", task.getResult().toString());

                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    //Methods.setGlobalUser(user);
                                    //Intent intent = new Intent(context, FeedActivity.class);
                                    //startActivity(intent);

                                    final Map<String, Object> user = new HashMap<>();
                                    user.put("id", firebaseUser.getUid());
                                    user.put("email", firebaseUser.getEmail());
                                    user.put("initialized", false);

                                    db.collection("users").add(user)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    loadingDialog.dismiss();
                                                    //Log.d("aaa", user.toString());
                                                    mAuth.signOut();
                                                    Intent returnIntent = new Intent();
                                                    returnIntent.putExtra("result", true);
                                                    setResult(Activity.RESULT_OK, returnIntent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("aaa", "Error adding document", e);
                                                }
                                            });
                                } else {
                                    Snackbar.make(rootLayout, context.getResources().getString(R.string.sign_up_activity_email_exists), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    void updateButtonClickability() {
        if(this.validEmail && this.validPassword && this.passwordsMatch)
            signUpButton.setEnabled(true);
        else
            signUpButton.setEnabled(false);
    }
}
