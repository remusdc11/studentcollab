package com.studentcollab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentcollab.Activities.FeedActivity;
import com.studentcollab.Activities.SearchActivity;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.TagsCompleteAdapter;
import com.studentcollab.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

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
    private TextView sectionTitle;
    private RecyclerView recyclerView;

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

       return rootView;
    }



}
