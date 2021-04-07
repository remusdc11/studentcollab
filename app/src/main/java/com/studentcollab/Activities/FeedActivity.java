package com.studentcollab.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Variables;
import com.studentcollab.R;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //View rootLayout = findViewById(R.id.root_layout);

        View container = findViewById(R.id.container);

        Snackbar.make(container, R.string.feed_activity_welcome, Snackbar.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(FeedActivity.this);


        View logOut = findViewById(R.id.button_log_out);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.start();
                mAuth.signOut();
                Variables.user = null;
                Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
                loadingDialog.dismiss();
                startActivity(intent);
                finish();
            }
        });




    }
}
