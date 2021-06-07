package com.studentcollab.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.studentcollab.Models.Review;
import com.studentcollab.R;

public class WriteReviewActivity extends AppCompatActivity {

    private String projectId, projectTitle, userId, userFullName;
    private TextInputEditText titleInput, reviewInput;
    private TextView sectionTitle, label;
    private View backButton, saveButton;
    private RatingBar ratingBar;
    private Review review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        Intent intent = getIntent();

        projectId = intent.getStringExtra("projectId");
        projectTitle = intent.getStringExtra("projectTitle");
        userId = intent.getStringExtra("userId");
        userFullName = intent.getStringExtra("userFullName");

        this.review = new Review(projectId, userId);

        backButton = findViewById(R.id.toolbar_simple_FrameLayout_back);
        sectionTitle = findViewById(R.id.toolbar_simple_TextView_section);
        label = findViewById(R.id.review_text_view_label);
        titleInput = findViewById(R.id.review_edit_text_layout_title);
        reviewInput = findViewById(R.id.review_edit_text_layout_project_review);
        saveButton = findViewById(R.id.review_button_save);

        sectionTitle.setText(R.string.review_title_new);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        label.setText(getString(R.string.review_label, userFullName, projectTitle));
    }
}
