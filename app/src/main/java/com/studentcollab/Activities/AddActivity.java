package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.TagsCompleteAdapter;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.ProjectStatus;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private TextInputEditText titleInput, descriptionInput, noUsersInput, startDateInput, endDateInput;

    private String startDate = null;
    private long startDateLong = 0;
    private String endDate = null;
    private long endDateLong = 0;
    private Context context;
    private FirebaseFirestore db;
    private ArrayList<String> allTags = new ArrayList<String>();
    private ArrayList<Chip> chips = new ArrayList<>();
    private ChipGroup chipGroup;
    private ScrollView scrollView;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        context = AddActivity.this;
        final TextView title = findViewById(R.id.toolbar_simple_TextView_section);
        final View backButton = findViewById(R.id.toolbar_simple_FrameLayout_back);
        scrollView = findViewById(R.id.add_scroll_view);
        titleInput = findViewById(R.id.add_edit_text_layout_project_title);
        descriptionInput = findViewById(R.id.add_edit_text_layout_project_description);
        noUsersInput = findViewById(R.id.add_edit_text_layout_project_no_users);
        startDateInput = findViewById(R.id.add_edit_text_layout_project_start_date);
        endDateInput = findViewById(R.id.add_edit_text_layout_project_end_date);
        chipGroup = findViewById(R.id.add_chip_group);

        final MaterialAutoCompleteTextView tagsAutoComplete = findViewById(R.id.add_auto_complete_tags);
        final TextInputLayout tagsInputLayout = findViewById(R.id.add_input_layout_tags);
        Button createButton = findViewById(R.id.add_create_button);

        title.setText(R.string.add_title);

        db = FirebaseFirestore.getInstance();

        loadingDialog = new LoadingDialog(AddActivity.this);
        messageDialog = new MessageDialog(AddActivity.this, getString(R.string.add_title_error));


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

                            //return filterResults;
                        } else {
                            Log.d("TagsAdapter", "Error getting documents: ", task.getException());
                        }
                    }
                });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.onBackPressed();
            }
        });

        ArrayAdapter<String> tagsAdapter = new TagsCompleteAdapter(this, android.R.layout.simple_list_item_1, allTags);
        tagsAutoComplete.setAdapter(tagsAdapter);


        final MaterialDatePicker startPicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(this.getString(R.string.add_select_start_date))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        startPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onPositiveButtonClick(Object selection) {
                startDateLong = Long.parseLong(selection.toString());

                if(startDateLong < (new Timestamp(System.currentTimeMillis())).getTime() || (endDateLong > 0 &&  startDateLong > endDateLong)) {
                    messageDialog.setMessage(getString(R.string.add_start_date_error));
                    messageDialog.show();
                    startDateLong = 0;
                    startDateInput.setText("");
                }
                else {
                    startDate = new SimpleDateFormat("dd/MM/yyyy").format(new Timestamp(startDateLong));
                    startDateInput.setText(startDate);
                }

            }
        });

        final MaterialDatePicker endPicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(this.getString(R.string.add_select_start_date))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        endPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                endDateLong = Long.parseLong(selection.toString());
                if(endDateLong < startDateLong || endDateLong  < (new Timestamp(System.currentTimeMillis())).getTime()) {
                    messageDialog.setMessage(getString(R.string.add_end_date_error));
                    messageDialog.show();
                    endDateLong = 0;
                    endDateInput.setText("");
                }
                else {
                    endDate = new SimpleDateFormat("dd/MM/yyyy").format(new Timestamp(endDateLong));
                    endDateInput.setText(endDate);
                }
            }
        });

        startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker.show(getSupportFragmentManager(), "startPicker");
            }
        });

        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPicker.show(getSupportFragmentManager(), "endPicker");
            }
        });

        tagsInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = tagsAutoComplete.getText().toString().toLowerCase();
                tagsAutoComplete.setText("");
                //tagsAutoComplete.clearFocus();
                addTag(tag);
            }
        });

        /*tagsInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            }
        });*/

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideSoftKeyboard(AddActivity.this);
                ValidateAndSendProject();
            }
        });

    }

    private void addTag(String tag) {
        if(tag.isEmpty())
            return ;
        for(int i = 0; i < chips.size(); i++) {
            if(chips.get(i).getText().toString().compareTo(tag) == 0)
                return ;
        }
        Chip chip = new Chip(AddActivity.this);

        chip.setText(tag);
        chip.setOnClickListener(chipOnClickListener);
        chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorPrimary));
        chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
        chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
        //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
        chip.setCloseIconVisible(true);

        chips.add(chip);
        chipGroup.addView(chip);
    }

    private void removeTag(Chip chip) {
        for(int i = 0; i < chips.size(); i++) {
            if(chips.get(i) == chip) {
                chips.remove(i);
                chipGroup.removeView(chip);
                break;
            }
        }
    }

    private View.OnClickListener chipOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeTag((Chip) v);
        }
    };

    private void ValidateAndSendProject() {


        boolean validData = true;
        String title, description;
        int noUsers = 0;
        title = titleInput.getText().toString();
        description = descriptionInput.getText().toString();
        try {
            noUsers = Integer.parseInt(noUsersInput.getText().toString());

        }
        catch (Exception e) {
            //noUsers = 0;
        }

        if(title.isEmpty() || title.length() < 3) {
            messageDialog.setMessage(getString(R.string.add_title_error));
            messageDialog.show();
            return ;
        }

        if(description.isEmpty() || description.length() < 10) {
            messageDialog.setMessage(getString(R.string.add_description_error));
            messageDialog.show();
            return ;
        }

        if(noUsers < 2) {
            messageDialog.setMessage(getString(R.string.add_no_users_error));
            messageDialog.show();
            return ;
        }

        if(startDateLong == 0) {
            messageDialog.setMessage(getString(R.string.add_start_date_mandatory));
            messageDialog.show();
            return ;
        }

        if(endDateLong == 0) {
            messageDialog.setMessage(getString(R.string.add_end_date_mandatory));
            messageDialog.show();
            return ;
        }

        if(chips.size() < 1) {
            messageDialog.setMessage(getString(R.string.add_tags_error));
            messageDialog.show();
            return ;
        }


        Project project = new Project(title, description, noUsers, startDateLong, endDateLong, ProjectStatus.NEW);

        //find new tags and insert them in the tags collection

        //create project => care at how you store tags in the project.
        /*loadingDialog.start();
        loadingDialog.dismiss();*/



    }
}
