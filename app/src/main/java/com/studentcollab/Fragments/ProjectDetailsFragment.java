package com.studentcollab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Adapters.ProjectAdapter;
import com.studentcollab.Adapters.TeamMemberAdapter;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.MessageDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.UserProjectDTO;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProjectDetailsFragment extends Fragment {

    private String projectId;
    private View backButton;
    private TextView toolbarTitle, title, startDate, endDate, status, description, teamMembersNumber;
    private ChipGroup chipGroup;
    private ListView membersListView;
    private Project project = new Project();
    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;
    private MessageDialog messageDialog;
    private Context context;
    private AppCompatActivity activity;
    private FragmentManager fragmentManager;
    private ArrayList<UserProjectDTO> teamMembers = new ArrayList<>();

    public ProjectDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        activity = (AppCompatActivity) getActivity();
        assert activity != null;
        fragmentManager = activity.getSupportFragmentManager();

        loadingDialog = new LoadingDialog(activity);
        messageDialog = new MessageDialog(activity, "");

        db = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        assert args != null;
        projectId = args.getString("projectId", "");

        if (projectId.equals("")) {
            activity.onBackPressed();
            //fragmentManager.popBackStack();
        }
        else {
           loadProject();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_project_details, container, false);

        backButton = rootView.findViewById(R.id.toolbar_simple_FrameLayout_back);
        toolbarTitle = rootView.findViewById(R.id.toolbar_simple_TextView_section);

        toolbarTitle.setText(R.string.fragment_project_title);

        title = rootView.findViewById(R.id.adapter_project_TextView_title);
        startDate = rootView.findViewById(R.id.adapter_project_TextView_start_date);
        endDate = rootView.findViewById(R.id.adapter_project_TextView_end_date);
        status = rootView.findViewById(R.id.adapter_project_TextView_status);
        description = rootView.findViewById(R.id.adapter_project_TextView_description);
        teamMembersNumber = rootView.findViewById(R.id.project_details_team_members_number);
        chipGroup = rootView.findViewById(R.id.adapter_project_chips);
        membersListView = rootView.findViewById(R.id.fragment_project_list_team_members);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });


        //idTv.setText(projectId);

        return rootView;
    }

    private void loadProject() {
        loadingDialog.start();

        db.collection("projects").document(projectId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                project = documentSnapshot.toObject(Project.class);
                populateViews();
                loadingDialog.dismiss();
            }
        });
    }

    private void populateViews() {
        title.setText(project.getTitle());
        startDate.setText(context.getString(R.string.adapter_project_start_date, new SimpleDateFormat("dd.MM.yyyy").format(new Timestamp(project.getStartDate()))));
        endDate.setText(context.getString(R.string.adapter_project_start_date, new SimpleDateFormat("dd.MM.yyyy").format(new Timestamp(project.getEndDate()))));
        status.setText(context.getString(R.string.fragment_project_status, project.getStatus().toString()));
        description.setText(project.getDescription());
        teamMembersNumber.setText(context.getString(R.string.fragment_project_team_members_number, project.getNumberOfCurrentUsers(), project.getNumberOfUsers()));

        ArrayList<String> tags = project.getTags();
        if (tags != null) {
            for(int i = 0; i < tags.size(); i++) {
                Chip chip = new Chip(context);

                chip.setText(tags.get(i));
                //chip.setOnClickListener(chipOnClickListener);
                chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorPrimary));
                chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
                //chip.setCloseIconVisible(true);

                chipGroup.addView(chip);
            }
        }
        getTeamMembers();
    }

    private int count;
    private void getTeamMembers() {
        count = 0;

        loadingDialog.start();
        teamMembers.clear();
        db.collection("users").whereIn("userId",project.getTeamMembers()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserProjectDTO user = document.toObject(UserProjectDTO.class);
                        user.setUserAccepted(true);
                        if (user.getUserId().compareTo(project.getOwnerId()) == 0)
                            user.setOwner(true);
                        teamMembers.add(user);
                    }
                }
                if(++count > 1) {
                    loadingDialog.dismiss();
                    loadMembersList();
                }
            }
        });

        if(!project.getPendingMembers().isEmpty()) {
            db.collection("users").whereIn("userId", project.getPendingMembers()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserProjectDTO user = document.toObject(UserProjectDTO.class);
                            user.setUserAccepted(false);
                            teamMembers.add(user);
                        }
                    }
                    if(++count > 1) {
                        loadingDialog.dismiss();
                        loadMembersList();
                    }
                }
            });
        }
        else {
            count++;
        }

    }

    private void loadMembersList() {
        TeamMemberAdapter adapter = new TeamMemberAdapter(context, teamMembers, project);

        membersListView.setAdapter(adapter);
        Methods.getListViewSize(membersListView, context);
    }
}
