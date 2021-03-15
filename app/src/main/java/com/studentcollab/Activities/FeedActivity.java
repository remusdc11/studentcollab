package com.studentcollab.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.studentcollab.R;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        View rootLayout = findViewById(R.id.root_layout);

        Snackbar.make(rootLayout, R.string.feed_activity_welcome, Snackbar.LENGTH_LONG).show();

    }
}
