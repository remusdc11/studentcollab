package com.studentcollab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Adapters.ReviewAdapter;
import com.studentcollab.Globals.CustomRecyclerView;
import com.studentcollab.Globals.CustomSwipeToRefresh;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Review;
import com.studentcollab.R;

import java.util.ArrayList;
import java.util.List;


public class ReviewsFragment extends Fragment {

    private String userId, userFullName;
    private RecyclerView recyclerView;
    private CustomSwipeToRefresh swipeToRefresh;
    private Context context;
    private Activity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reviewsRef = db.collection("reviews");
    private ReviewAdapter adapter;
    private FragmentManager fragmentManager;
    private View backButton;
    private TextView sectionLabel;
    private LoadingDialog loadingDialog;
    private List<Review> reviewsList = new ArrayList<>();

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        context = this.getContext();
        activity = this.getActivity();

        loadingDialog = new LoadingDialog(activity);

        //loadingDialog.start();

        Bundle args = getArguments();
        assert args != null;
        userId = args.getString("userId", Variables.user.getUserId());
        userFullName = args.getString("userFullName", "");

        recyclerView = rootView.findViewById(R.id.fragment_reviews_recycler);
        sectionLabel = rootView.findViewById(R.id.toolbar_simple_TextView_section);
        backButton = rootView.findViewById(R.id.toolbar_simple_FrameLayout_back);

        if (!userFullName.isEmpty())
        sectionLabel.setText(getString(R.string.fragment_reviews_title, userFullName));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        fragmentManager = getParentFragmentManager();

       /* db.collection("reviews")
                .whereEqualTo("userId", userId)
                //.orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                loadingDialog.dismiss();

                if(task.isSuccessful() && task.getResult() != null) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    reviewsList.clear();
                    for (DocumentSnapshot doc : docs) {
                        reviewsList.add(doc.toObject(Review.class));
                    }
                    setUpRecyclerView();
                }
            }
        });*/


        setUpRecyclerView();

        return rootView;
    }

    private void setUpRecyclerView() {
        Query query = reviewsRef
                .whereEqualTo("userId", userId);
                //.orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Review> options = new FirestoreRecyclerOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .build();

        adapter = new ReviewAdapter(options, context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        /*adapter = new ReviewAdapter(reviewsList, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);*/

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
