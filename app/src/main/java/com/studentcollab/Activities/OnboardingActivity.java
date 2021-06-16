package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.CustomArrayAdapter;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.University;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnboardingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<University> universitiesList = new ArrayList<University>();
    private Spinner universitySpinner;
    private ArrayAdapter spinnerAdapter;
    private TextView sectionTitle;

    private University selectedUniversity = null;
    private boolean validFirstName = false;
    private boolean validLastName = false;
    private boolean validUniversity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onoarding);

         db = FirebaseFirestore.getInstance();

        final EditText firstNameEditText = findViewById(R.id.edit_text_first_name);
        final EditText lastNameEditText = findViewById(R.id.edit_text_last_name);
        final View nextButton = findViewById(R.id.toolbar_next_button_next);
        final View backButton = findViewById(R.id.toolbar_next_button_back);
        backButton.setVisibility(View.GONE);
        universitySpinner = findViewById(R.id.spinner_university);
        sectionTitle = findViewById(R.id.toolbar_next_TextView_section);
        final Context context = this.getApplicationContext();

        //nextButton.setEnabled(false);

        final MessageDialog messageDialog = new MessageDialog(OnboardingActivity.this, getString(R.string.activity_onboarding_invalid_data));
        final LoadingDialog loadingDialog = new LoadingDialog(OnboardingActivity.this);

        sectionTitle.setText(R.string.activity_onboarding_title);


         spinnerAdapter = new CustomArrayAdapter(context, universitiesList);


        //Get Universities
        loadingDialog.start();
        db.collection("universities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                universitiesList.add(new University(document.getId(), document.getString("name")));
                            }
                            onDataReady();
                        } else {
                            Log.d("Onboarding", "Error getting documents: ", task.getException());
                        }
                    }
                });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify then send data
                if(!firstNameEditText.getText().toString().isEmpty() &&
                !lastNameEditText.getText().toString().isEmpty() &&
                selectedUniversity != null) {
                    //SEND DATA
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("firstName", firstNameEditText.getText().toString());
//
//                    data.put("universityDocumentId", selectedUniversity.getDocumentId());
//                    data.put("initialized", true);
                    Variables.user.setFirstName(firstNameEditText.getText().toString());
                    Variables.user.setLastName(lastNameEditText.getText().toString());
                    Variables.user.setUniversityDocumentId(selectedUniversity.getDocumentId());
                    Variables.user.setInitialized(true);
                    Variables.user.setJoinDate(new Timestamp(System.currentTimeMillis()).getTime());


                    loadingDialog.start();
                    db.collection("users").document(Variables.user.getDocumentId())
                            .set(Variables.user)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadingDialog.dismiss();

                                    Log.d("Onboarding", "DocumentSnapshot successfully written!");

                                    Intent intent = new Intent(OnboardingActivity.this, FeedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingDialog.dismiss();
                                    Log.w("Onboarding", "Error writing document", e);
                                }
                            });


                }
                else {
                    //show error dialog
                    messageDialog.show();
                }
            }
        });
    }

    private void onDataReady() {
        universitySpinner.setAdapter(spinnerAdapter);

        universitySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {
                        selectedUniversity = (University) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                        Log.d("spinner", "NOTHING SELECTED");
                    }
                });
    }

}
