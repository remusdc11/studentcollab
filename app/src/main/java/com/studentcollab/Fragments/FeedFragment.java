package com.studentcollab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studentcollab.Activities.SearchActivity;
import com.studentcollab.Adapters.ProjectAdapterAdvanced;
import com.studentcollab.Globals.CustomRecyclerView;
import com.studentcollab.Globals.CustomSwipeToRefresh;
import com.studentcollab.Models.Project;
import com.studentcollab.R;

import java.util.ArrayList;


public class FeedFragment extends Fragment {

    private CustomRecyclerView recyclerView;
    //private CustomSwipeToRefresh swipeToRefresh;
    private ArrayList<Project> projects = new ArrayList<>();
    private Context context;
    private Activity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference projectsRef = db.collection("projects");
    private ProjectAdapterAdvanced adapter;
    private FragmentManager fragmentManager;
    private View searchButton;

    public FeedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        context = this.getContext();
        activity = this.getActivity();
        recyclerView = rootView.findViewById(R.id.fragment_home_recycler);
        //swipeToRefresh = rootView.findViewById(R.id.fragment_home_swipe_refresh);
        searchButton = rootView.findViewById(R.id.toolbar_feed_FrameLayout_search);
        fragmentManager = getParentFragmentManager();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }
        });

        setUpRecyclerView();


        return rootView;
    }

    private void setUpRecyclerView() {
        Query query = projectsRef.orderBy("startDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class)
                .build();

        adapter = new ProjectAdapterAdvanced(options, context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
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
