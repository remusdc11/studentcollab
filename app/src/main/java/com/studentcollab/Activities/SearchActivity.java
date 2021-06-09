package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.TagsCompleteAdapter;
import com.studentcollab.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = getApplicationContext();
        loadingDialog = new LoadingDialog(this);
        messageDialog = new MessageDialog(this, getString(R.string.add_title_error));

        backButton = findViewById(R.id.toolbar_search_FrameLayout_back);
        searchButton = findViewById(R.id.toolbar_search_FrameLayout_search);
        searchEditText = findViewById(R.id.fragment_search_EditText);
        chipGroup = findViewById(R.id.fragment_search_chip_group);
        listViewTags = findViewById(R.id.fragment_search_ListView_tags);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    }

    private void showTagsList() {
        tagsAdapter = new TagsCompleteAdapter(context, android.R.layout.simple_list_item_1, allTags);
        listViewTags.setAdapter(tagsAdapter);
        listViewTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> filteredItems = ((TagsCompleteAdapter) tagsAdapter).getFilteredItems();
                ArrayList<String> allItems = ((TagsCompleteAdapter) tagsAdapter).getAllItems();
                String tag = filteredItems.get(position);
                //String tag = allTags.get(position) != null ? allTags.get(position) : "fail";

                Chip chip = new Chip(SearchActivity.this);
                chip.setText(tag);
                chip.setOnClickListener(chipOnClickListener);
                chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorAccent));
                chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
                chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
                chip.setCloseIconVisible(true);

                chips.add(chip);
                chipGroup.addView(chip);
                filteredItems.remove(tag);
                allItems.remove(tag);
                tagsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void removeTag(Chip chip) {
        if (chips.remove(chip)) {
            chipGroup.removeView(chip);
            ArrayList<String> allItems = ((TagsCompleteAdapter) tagsAdapter).getAllItems();
            allItems.add(chip.getText().toString());
            //allTags.add(chip.getText().toString());
            tagsAdapter.notifyDataSetChanged();
        }
    }

    private View.OnClickListener chipOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeTag((Chip) v);
        }
    };
}
