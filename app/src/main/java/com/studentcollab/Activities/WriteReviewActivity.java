package com.studentcollab.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.ConfirmationDialog;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.Review;
import com.studentcollab.Models.User;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.util.List;

public class WriteReviewActivity extends AppCompatActivity {

    private String projectId, projectTitle, userId, userFullName, documentId = null;
    private TextInputEditText titleInput, reviewInput;
    private TextView sectionTitle, label;
    private View backButton, saveButton, deleteButton;
    private RatingBar ratingBar;
    private Review review;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private ConfirmationDialog confirmationDialog;
    private String title, reviewText;
    private int rating;
    private boolean reviewExists = false;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        Intent intent = getIntent();

        projectId = intent.getStringExtra("projectId");
        projectTitle = intent.getStringExtra("projectTitle");
        userId = intent.getStringExtra("userId");
        userFullName = intent.getStringExtra("userFullName");

        this.review = new Review(projectId, userId, projectTitle);
        loadingDialog = new LoadingDialog(WriteReviewActivity.this);
        messageDialog = new MessageDialog(WriteReviewActivity.this, getString(R.string.add_title_error));

        backButton = findViewById(R.id.toolbar_review_FrameLayout_back);
        sectionTitle = findViewById(R.id.toolbar_review_TextView_section);
        deleteButton = findViewById(R.id.toolbar_review_button_delete);
        label = findViewById(R.id.review_text_view_label);
        titleInput = findViewById(R.id.review_edit_text_layout_title);
        reviewInput = findViewById(R.id.review_edit_text_layout_project_review);
        ratingBar = findViewById(R.id.review_rating_bar);
        saveButton = findViewById(R.id.review_button_save);

        sectionTitle.setText(R.string.review_title_new);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        label.setText(getString(R.string.review_label, userFullName, projectTitle));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fieldsError = validateFields();
                if(fieldsError == null) {
                    saveReview();
                }
                else {
                    messageDialog.setMessage(fieldsError);
                    messageDialog.show();
                }
            }
        });

        getData();
    }

    private int loadingCount;
    private void getData () {
        loadingCount = 0;
        loadingDialog.start();

        db.collection("users").whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingCount++;
                if (loadingCount == 2)
                    loadingDialog.dismiss();

                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        List<DocumentSnapshot> docs = task.getResult().getDocuments();

                        if (docs.size() > 0) {
                            user = docs.get(0).toObject(User.class);
                        }
                    }
                }
            }
        });

        db.collection("reviews").whereEqualTo("userId", userId).whereEqualTo("projectId", projectId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingCount++;
                if (loadingCount == 2)
                    loadingDialog.dismiss();

                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        List<DocumentSnapshot> docs = task.getResult().getDocuments();
                        if(docs.size() > 0) {
                            review = docs.get(0).toObject(Review.class);
                            reviewExists = true;
                            documentId = docs.get(0).getId();
                            populateViews();
                        }
                    }
                }
            }
        });
    }

    private void populateViews() {
        titleInput.setText(review.getTitle());
        reviewInput.setText(review.getReview());
        ratingBar.setRating(review.getRating());
        sectionTitle.setText(R.string.review_title_edit);
        deleteButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog = new ConfirmationDialog(WriteReviewActivity.this, getString(R.string.review_delete_confirmation), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       confirmationDialog.dismiss();
                       deleteReview();
                    }
                });
                confirmationDialog.show();
            }
        });
    }

    private void deleteReview() {
        Methods.hideSoftKeyboard(WriteReviewActivity.this);
        loadingDialog.start();
        user.setReviews(user.getReviews() - 1);
        user.setScore(user.getScore() - review.getRating());

        db.collection("users").document(user.getDocumentId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("reviews").document(documentId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismiss();

                        if (task.isSuccessful()) {
                            WriteReviewActivity.this.finish();
                        }
                    }
                });
            }
        });
    }

    private String validateFields() {
        Methods.hideSoftKeyboard(WriteReviewActivity.this);
        title = titleInput.getText().toString();
        reviewText = reviewInput.getText().toString();
        rating = (int)ratingBar.getRating();

        if (title.isEmpty() || title.length() < 3) {
            return getString(R.string.review_title_error);
        }

        if (reviewText.isEmpty() || reviewText.length() < 10)
            return getString(R.string.review_review_error);

        if (rating < 1 || rating > 5) {
            return getString(R.string.review_rating_error);
        }

        return null;
    }

    private void saveReview() {
        int oldRating = review.getRating();
        loadingDialog.start();
        review.setTitle(title);
        review.setReview(reviewText);
        review.setRating(rating);
        review.setDate((new Timestamp(System.currentTimeMillis())).getTime());

        //Update if already exists
        if (reviewExists) {
            user.setScore(user.getScore() - oldRating + review.getRating());

            db.collection("users").document(user.getDocumentId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    db.collection("reviews").document(documentId).set(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingDialog.dismiss();

                            if (task.isSuccessful()) {
                                messageDialog.setCustomDismissAction(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        messageDialog.dismiss();
                                        WriteReviewActivity.this.finish();
                                    }
                                });
                                messageDialog.setMessage(getString(R.string.review_save_successful));
                                messageDialog.show();
                            }
                        }
                    });
                }
            });


        }
        else {
            user.setScore(user.getScore() + review.getRating());
            user.setReviews(user.getReviews() + 1);
            db.collection("users").document(user.getDocumentId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    db.collection("reviews").add(review).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            loadingDialog.dismiss();
                            if (task.isSuccessful()) {

                                messageDialog.setCustomDismissAction(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        messageDialog.dismiss();
                                        WriteReviewActivity.this.finish();
                                    }
                                });
                                messageDialog.setMessage(getString(R.string.review_save_successful));
                                messageDialog.show();
                            }
                        }
                    });
                }
            });

        }
    }
}
