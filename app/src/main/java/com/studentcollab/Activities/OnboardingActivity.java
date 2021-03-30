package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Globals.CustomArrayAdapter;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Models.University;
import com.studentcollab.R;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;
    private ArrayList<University> universitiesList = new ArrayList<University>();
    private University selectedUniversity = null;
    private Spinner universitySpinner;


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
        final Context context = this.getApplicationContext();

        nextButton.setEnabled(false);

        loadingDialog = new LoadingDialog(OnboardingActivity.this);


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
                        } else {
                            Log.d("aaa", "Error getting documents: ", task.getException());
                        }
                    }
                });

        ArrayAdapter spinnerAdapter = new CustomArrayAdapter(context, universitiesList);
        universitySpinner.setAdapter(spinnerAdapter);

        universitySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {

                        // It returns the clicked item.
                        University clickedItem = (University)
                                parent.getItemAtPosition(position);
                        String name = clickedItem.getName();
                        Toast.makeText(OnboardingActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify then send data
            }
        });

//        Map<String, Object> user = new HashMap<>();
//        user.put("id", Variables.user.getUserId());
//        user.put("email", Variables.user.getUserEmail());
//
//
//        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Variables.user.setDocumentId(documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w("aaa", "Error adding document", e);
//            }
//        });

    }

}
