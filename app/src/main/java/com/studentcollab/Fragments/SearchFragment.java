/*
package com.studentcollab.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Activities.AddActivity;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.TagsCompleteAdapter;
import com.studentcollab.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private AppCompatActivity activity;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private ArrayList<String> allTags = new ArrayList<String>();
    private ArrayList<Chip> chips = new ArrayList<>();
    private ChipGroup chipGroup;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private View backButton, searchButton;
    private EditText searchEditText;
    private ListView listViewTags;
    private ArrayAdapter<String> tagsAdapter = null;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getContext();
        activity = (AppCompatActivity) this.getActivity();
        loadingDialog = new LoadingDialog(activity);
        messageDialog = new MessageDialog(activity, getString(R.string.add_title_error));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_search, container, false);

       backButton = rootView.findViewById(R.id.toolbar_search_FrameLayout_back);
       searchButton = rootView.findViewById(R.id.toolbar_search_FrameLayout_search);
       searchEditText = rootView.findViewById(R.id.fragment_search_EditText);
       chipGroup = rootView.findViewById(R.id.fragment_search_chip_group);
       listViewTags = rootView.findViewById(R.id.fragment_search_ListView_tags);

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activity.onBackPressed();
           }
       });

       searchButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // open search results fragment
           }
       });

       searchEditText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               if (tagsAdapter != null)
                   tagsAdapter.getFilter().filter(searchEditText.getText().toString());
           }
       });

        loadingDialog.start();
        db.collection("tags")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loadingDialog.dismiss();
                        if (task.isSuccessful()) {
                            allTags.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allTags.add(document.getString("tag"));
                            }
                            showTagsList();
                        } else {
                            Log.d("TagsAdapter", "Error getting documents: ", task.getException());
                        }
                    }
                });

       return rootView;
    }

    private void showTagsList() {
        tagsAdapter = new TagsCompleteAdapter(context, android.R.layout.simple_list_item_1, allTags);
        listViewTags.setAdapter(tagsAdapter);
    }
}
*/
