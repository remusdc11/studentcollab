package com.studentcollab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentcollab.Activities.LoginActivity;
import com.studentcollab.Globals.ConfirmationDialog;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.User;
import com.studentcollab.Models.UserProjectDTO;
import com.studentcollab.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private boolean ownProfile = false;
    private User user;
    private String userId;
    private View backButton, logoutButton, ratingButton;
    private TextView usernameTextView, universityTextView, createdProjectsTextView, completedProjectsTextView, ratingTextView;
    private RecyclerView projectsRecyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private Context context;
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private ArrayList<Project> projects = new ArrayList<>();
    private ConfirmationDialog confirmationDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        activity = (AppCompatActivity) getActivity();
        assert activity != null;
        fragmentManager = activity.getSupportFragmentManager();

        loadingDialog = new LoadingDialog(activity);
        messageDialog = new MessageDialog(activity, "");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Bundle args = getArguments();
        assert args != null;
        userId = args.getString("userId", Variables.user.getUserId());

        if (userId.equals(Variables.user.getUserId())) {
            this.ownProfile = true;
        }

        loadProfile();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        backButton = rootView.findViewById(R.id.toolbar_profile_FrameLayout_back);
        logoutButton = rootView.findViewById(R.id.toolbar_profile_button_logout);
        usernameTextView = rootView.findViewById(R.id.fragment_profile_TextView_username);
        universityTextView = rootView.findViewById(R.id.fragment_profile_TextView_university);
        createdProjectsTextView = rootView.findViewById(R.id.fragment_profile_textView_created_number);
        completedProjectsTextView = rootView.findViewById(R.id.fragment_profile_textView_completed_number);
        ratingTextView = rootView.findViewById(R.id.fragment_profile_TextView_rating_number);
        ratingButton = rootView.findViewById(R.id.fragment_profile_LinearLayout_rating);
        projectsRecyclerView = rootView.findViewById(R.id.fragment_profile_recycler);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        if (!ownProfile) {
            logoutButton.setVisibility(View.GONE);
        }
        else {
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmationDialog  = new ConfirmationDialog(activity, getString(R.string.fragment_profile_logout_confirmation),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmationDialog.dismiss();
                                    loadingDialog.start();
                                    mAuth.signOut();
                                    Variables.user = null;
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    loadingDialog.dismiss();
                                    startActivity(intent);
                                    activity.finish();
                                }
                            }
                    );
                    confirmationDialog.show();
                }
            });
        }

        return rootView;
    }

    private void loadProfile() {
        //loadingDialog.start();
        projects.clear();
        /*teamMembers.clear();
        if (chipGroup != null)
            chipGroup.removeAllViews();

        db.collection("projects").document(projectId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                project = documentSnapshot.toObject(Project.class);
                populateViews();
                //loadingDialog.dismiss();
            }
        });*/
    }
}
