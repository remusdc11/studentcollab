package com.studentcollab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.errorprone.annotations.Var;
import com.studentcollab.Activities.WriteReviewActivity;
import com.studentcollab.Fragments.ProjectDetailsFragment;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Project;
import com.studentcollab.Models.ProjectStatus;
import com.studentcollab.Models.UserProjectDTO;
import com.studentcollab.R;

import java.util.ArrayList;

public class TeamMemberAdapter extends ArrayAdapter<UserProjectDTO> {

    private Context context;
    private ArrayList<UserProjectDTO> teamMembers;
    private Project project;
    private ProjectDetailsFragment parentFragment;

    public TeamMemberAdapter(@NonNull Context context, ArrayList<UserProjectDTO> teamMembers, Project project, ProjectDetailsFragment parentFragment) {
        super(context, R.layout.adapter_team_member, teamMembers);
        this.context = context;
        this.teamMembers = teamMembers;
        this.project = project;
        this.parentFragment = parentFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null) {
            rootView = LayoutInflater.from(context).inflate(R.layout.adapter_team_member, parent, false);
        }

        final UserProjectDTO teamMember = teamMembers.get(position);

        if (teamMember != null) {

            View memberView = rootView.findViewById(R.id.adapter_team_member_View_user);
            ImageView userProfileImage = rootView.findViewById(R.id.adapter_team_member_ImageView_user);
            TextView userName = rootView.findViewById(R.id.adapter_team_member_username);
            TextView pending = rootView.findViewById(R.id.adapter_team_member_pending);
            View confirmButton = rootView.findViewById(R.id.adapter_team_member_confirm);
            View cancelButton = rootView.findViewById(R.id.adapter_team_member_cancel);
            View reviewButton = rootView.findViewById(R.id.adapter_team_member_review);

            userName.setText(teamMember.getFirstName() + " " + teamMember.getLastName());

            if (teamMember.isOwner()) {
                pending.setText(R.string.adapter_team_member_owner);
            }
            else {
                if (!teamMember.isUserAccepted())
                    pending.setText(R.string.adapter_team_member_pending);
                else
                    pending.setVisibility(View.GONE);
            }


            if (project.getStatus() == ProjectStatus.NEW) {
                confirmButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                reviewButton.setVisibility(View.GONE);

                if (Variables.user.getUserId().compareTo(project.getOwnerId()) == 0)
                {
                    if(teamMember.isUserAccepted()) {
                        confirmButton.setVisibility(View.GONE);
                    }
                    else {
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TeamMemberAdapter.this.parentFragment.acceptMember(teamMember.getUserId());

                            }
                        });
                    }

                    if(teamMember.getUserId().compareTo(project.getOwnerId()) == 0) {
                        cancelButton.setVisibility(View.GONE);
                    }
                    else {
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (teamMember.isUserAccepted())
                                    TeamMemberAdapter.this.parentFragment.removeMember(teamMember.getUserId(), teamMember.getFirstName() + " " + teamMember.getLastName());
                                else
                                    TeamMemberAdapter.this.parentFragment.declinePendingRequest(teamMember.getUserId(), teamMember.getFirstName() + " " + teamMember.getLastName());
                            }
                        });
                    }
                }
                else
                {
                    cancelButton.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                }
            }
            else{
                if (project.getStatus() == ProjectStatus.STARTED) {
                    confirmButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    reviewButton.setVisibility(View.GONE);
                }
                else {
                    //ENDED
                    confirmButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    if (project.getOwnerId().compareTo(Variables.user.getUserId()) == 0 && teamMember.getUserId().compareTo(project.getOwnerId()) != 0) {
                        reviewButton.setVisibility(View.VISIBLE);
                        reviewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //open review activity/fragment
                                Intent intent = new Intent(context, WriteReviewActivity.class);
                                intent.putExtra("projectId", project.getDocumentId());
                                intent.putExtra("projectTitle", project.getTitle());
                                intent.putExtra("userId", teamMember.getUserId());
                                intent.putExtra("userFullName", teamMember.getFirstName() + " " + teamMember.getLastName());
                                context.startActivity(intent);
                            }
                        });
                    }
                    else {
                        reviewButton.setVisibility(View.GONE);
                    }
                }
            }

        }
        return rootView;
    }
}
