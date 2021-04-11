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
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.studentcollab.Activities.FeedActivity;
import com.studentcollab.Activities.LoginActivity;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Variables;
import com.studentcollab.R;


public class SettingsFragment extends Fragment {

    private Activity activity;
    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;
    private Context context;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final View logOutView = view.findViewById(R.id.layout_log_out);


        context = getContext();
        activity = getActivity();

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(activity);

        logOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.start();
                mAuth.signOut();
                Variables.user = null;
                Intent intent = new Intent(context, LoginActivity.class);
                loadingDialog.dismiss();
                startActivity(intent);
                activity.finish();
            }
        });





        return view;
    }


}
