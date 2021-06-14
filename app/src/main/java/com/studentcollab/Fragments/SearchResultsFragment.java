package com.studentcollab.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Adapters.ProjectAdapterAdvanced;
import com.studentcollab.Adapters.ProjectAdapterBasic;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Models.Project;
import com.studentcollab.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {

    private AppCompatActivity activity;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private ArrayList<String> tags;
    private ArrayList<Chip> chips = new ArrayList<>();
    private ChipGroup chipGroup;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private View backButton;
    private TextView sectionTitle, noResults;
    private RecyclerView recyclerView;
    private CollectionReference projectsRef = db.collection("projects");
    private ProjectAdapterBasic adapter;


    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getContext();
        activity = (AppCompatActivity) this.getActivity();
        loadingDialog = new LoadingDialog(activity);
        messageDialog = new MessageDialog(activity, getString(R.string.add_title_error));
        Bundle arguments = getArguments();
        if (arguments != null)
            tags = getArguments().getStringArrayList("tags");
        else
            tags = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

       backButton = rootView.findViewById(R.id.toolbar_simple_FrameLayout_back);
       sectionTitle = rootView.findViewById(R.id.toolbar_simple_TextView_section);
       chipGroup = rootView.findViewById(R.id.fragment_search_results_chip_group);
       recyclerView = rootView.findViewById(R.id.fragment_search_results_recycler);
       noResults = rootView.findViewById(R.id.fragment_search_results_no_results);

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activity.onBackPressed();
           }
       });

       sectionTitle.setText(R.string.fragment_search_results_section_title);

       for (String tag : tags) {
           Chip chip = new Chip(context);
           chip.setText(tag);
           //chip.setOnClickListener(chipOnClickListener);
           chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorAccent));
           chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
           chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
           //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
           //chip.setCloseIconVisible(true);

           chips.add(chip);
           chipGroup.addView(chip);
       }
       setUpRecyclerView();
       return rootView;
    }

    private void setUpRecyclerView() {
        loadingDialog.start();
        //Query query = projectsRef.orderBy("startDate", Query.Direction.DESCENDING);
        Query query = projectsRef.whereArrayContains("tags", tags.get(0));

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult() != null) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();

                    List<Project> projects = new ArrayList<>();
                    for (DocumentSnapshot doc : docs) {
                        projects.add(doc.toObject(Project.class));
                    }
                    applyRemainingFilters(projects);

                    adapter = new ProjectAdapterBasic(projects, context);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);

                    if (projects.size() == 0)
                        noResults.setVisibility(View.VISIBLE);
                }

                loadingDialog.dismiss();
            }
        });

        /*for (String tag : tags) {
            if (!tag.isEmpty()) {
                //query = query.whereIn("tags", tags);
                //query = query.whereEqualTo("tags." + tag, true);
            }
        }


        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class)
                .build();

        adapter = new ProjectAdapterAdvanced(options, context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);*/
    }

    private void applyRemainingFilters(List<Project> projects) {
        for (int i = 1; i < tags.size(); i++) {
            for (int j = 0; j < projects.size(); j++) {
                List<String> currentTags = projects.get(j).getTags();
                boolean contains = false;
                for (String currentTag : currentTags) {
                    if (currentTag.compareTo(tags.get(i)) == 0) {
                        contains = true;
                        break;
                    }
                }
                if (!contains)
                    projects.remove(j);
                /*if (!projects.get(j).getTags().contains(tags.get(i)))
                    projects.remove(j);*/
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
}
