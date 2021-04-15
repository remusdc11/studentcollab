package com.studentcollab.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AddActivity extends AppCompatActivity {

    private String startDate = null;
    private long startDateLong = 0;
    private String endDate = null;
    private long endDateLong = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final TextInputEditText startDateInput = findViewById(R.id.add_edit_text_layout_project_start_date);
        final TextInputEditText endDateInput = findViewById(R.id.add_edit_text_layout_project_end_date);


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
                startDate = new SimpleDateFormat("dd/MM/yyyy").format(new Timestamp(startDateLong));
                startDateInput.setText(startDate);
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
                if(endDateLong < startDateLong) {
                    messageDialog.show();
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
