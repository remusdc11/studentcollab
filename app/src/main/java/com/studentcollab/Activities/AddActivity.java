package com.studentcollab.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AddActivity extends AppCompatActivity {

    private String startDate = null;
    private long startDateLong = 0;
    private String endDate = null;
    private long endDateLong = 0;
    private Context context;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        context = this.getApplicationContext();

        final TextInputEditText startDateInput = findViewById(R.id.add_edit_text_layout_project_start_date);
        final TextInputEditText endDateInput = findViewById(R.id.add_edit_text_layout_project_end_date);
        final TextView title = findViewById(R.id.toolbar_simple_TextView_section);
        final View backButton = findViewById(R.id.toolbar_simple_FrameLayout_back);
        final ChipGroup chipGroup = findViewById(R.id.add_chip_group);
        final MaterialAutoCompleteTextView tagsAutoComplete = findViewById(R.id.add_auto_complete_tags);
        title.setText(R.string.add_title);

        db = FirebaseFirestore.getInstance();


        Chip chip = new Chip(this);

        chip.setText("Test chip");
        chipGroup.addView(chip);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.onBackPressed();
            }
        });

        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        tagsAutoComplete.setAdapter(tagsAdapter);

        final MessageDialog messageDialog = new MessageDialog(AddActivity.this, getString(R.string.add_end_date_error));


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

                if(startDateLong < (new Timestamp(System.currentTimeMillis())).getTime()) {
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
    }
}
