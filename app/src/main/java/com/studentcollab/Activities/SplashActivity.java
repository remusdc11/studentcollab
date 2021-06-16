package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.User;
import com.studentcollab.R;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private Animation logoAnimation;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Context context = getApplicationContext();

        logo = findViewById(R.id.activity_splash_logo);
        logoAnimation = AnimationUtils.loadAnimation(context, R.anim.splash_animation);

        logo.startAnimation(logoAnimation);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Methods.setGlobalUser(currentUser);

            //get user details and check if acc is initialized
            db.collection("users")
                    .whereEqualTo("userId", Variables.user.getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                                if(documents.size() > 0)
                                {
                                    Variables.user = documents.get(0).toObject(User.class);
                                    Variables.user.setDocumentId(documents.get(0).getId());
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
                                //Log.d("aaa", String.valueOf(documents.size()));

                            } else {
                                //Log.d("aaa", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
