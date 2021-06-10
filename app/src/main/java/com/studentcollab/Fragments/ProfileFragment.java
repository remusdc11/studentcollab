package com.studentcollab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.studentcollab.Activities.LoginActivity;
import com.studentcollab.Adapters.ProjectAdapter;
import com.studentcollab.Globals.ConfirmationDialog;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.University;
import com.studentcollab.Models.User;
import com.studentcollab.Models.UserProjectDTO;
import com.studentcollab.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ProfileFragment extends Fragment {

    private boolean ownProfile = false;
    private User user;
    private String userId;
    private University university;
    private View backButton, logoutButton, ratingButton;
    private TextView usernameTextView, universityTextView, createdProjectsTextView, completedProjectsTextView, ratingTextView, sectionTitle;
    private NestedScrollView scrollView;
    private RecyclerView projectsRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private Context context;
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private ArrayList<Project> projects = new ArrayList<>();
    private ConfirmationDialog confirmationDialog;

    private CollectionReference projectsRef = db.collection("projects");

    private ProjectAdapter adapter;

    private int readyToLoad = 0;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        activity = (AppCompatActivity) getActivity();

        loadingDialog = new LoadingDialog(activity);
        messageDialog = new MessageDialog(activity, "");

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


        fragmentManager = getActivity().getSupportFragmentManager();

        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        backButton = rootView.findViewById(R.id.toolbar_profile_FrameLayout_back);
        sectionTitle = rootView.findViewById(R.id.toolbar_profile_TextView_section);
        logoutButton = rootView.findViewById(R.id.toolbar_profile_button_logout);
        usernameTextView = rootView.findViewById(R.id.fragment_profile_TextView_username);
        universityTextView = rootView.findViewById(R.id.fragment_profile_TextView_university);
        createdProjectsTextView = rootView.findViewById(R.id.fragment_profile_textView_created_number);
        completedProjectsTextView = rootView.findViewById(R.id.fragment_profile_textView_completed_number);
        ratingTextView = rootView.findViewById(R.id.fragment_profile_TextView_rating_number);
        ratingButton = rootView.findViewById(R.id.fragment_profile_LinearLayout_rating);
        scrollView = rootView.findViewById(R.id.fragment_profile_ScrollView);
        projectsRecyclerView = rootView.findViewById(R.id.fragment_profile_recycler);

        //scrollView.setScrollContainer(false);
        //scrollView.setFillViewport(false);
        //projectsRecyclerView.setNestedScrollingEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        sectionTitle.setText(R.string.fragment_profile_section_title);

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                args.putString("userFullName", user.getFullName());
                reviewsFragment.setArguments(args);
                Methods.addFragment(fragmentManager, reviewsFragment, Variables.FRAMGMENT_REVIEWS);
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

        loadProfile();

        return rootView;
    }

    private void loadProfile() {

        this.readyToLoad++;

        if (this.readyToLoad < 2) {
            return ;
        }
        else this.readyToLoad = 0;

        loadingDialog.start();
        projects.clear();

        db.collection("users")
                .whereEqualTo("userId", this.userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            List<DocumentSnapshot> documents = snapshot.getDocuments();

                            if (documents.size() > 0) {
                                ProfileFragment.this.user = documents.get(0).toObject(User.class);
                            }
                            else {
                                user = Variables.user;
                            }
                            populateUserFields();
                        }
                    }
                });

        db.collection("projects")
                .whereEqualTo("ownerId", this.userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            List<DocumentSnapshot> documents = snapshot.getDocuments();
                            int value = documents.size();
                            createdProjectsTextView.setText(String.valueOf(value));
                        }
                    }
                });

        db.collection("projects")
                .whereArrayContains("teamMembers", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            List<DocumentSnapshot> documents = snapshot.getDocuments();
                            int value = documents.size();
                            completedProjectsTextView.setText(String.valueOf(value));
                        }
                    }
                });

        populateProjectsList();

//        db.collection("projects").whereArrayContains("teamMembers", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot snapshot = task.getResult();
//                            List<DocumentSnapshot> documents = snapshot.getDocuments();
//
//                            for (DocumentSnapshot document : documents) {
//                                projects.add(document.toObject(Project.class));
//                            }
//
//                            populateProjectsList();
//                        }
//                    }
//                });
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

    private void populateUserFields() {
        usernameTextView.setText(this.user.getFirstName() + " " + this.user.getLastName());
        ratingTextView.setText(String.valueOf(this.user.computeUserRating()));

        db.collection("universities").
                document(user.getUniversityDocumentId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        assert documentSnapshot != null;
                        university = documentSnapshot.toObject(University.class);
                        university.setDocumentId(documentSnapshot.getId());
                        universityTextView.setText(university.getName());
                        loadingDialog.dismiss();
                    }
                });

    }

    private void populateProjectsList() {
        Query query = projectsRef
                .whereArrayContains("teamMembers", userId)
                .orderBy("startDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class)
                .build();

        adapter = new ProjectAdapter(options, context);

        projectsRecyclerView.setHasFixedSize(true);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        projectsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
