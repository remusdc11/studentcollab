package com.studentcollab.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.studentcollab.Fragments.ProjectDetailsFragment;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Project;

import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProjectAdapterAdvanced extends FirestoreRecyclerAdapter<Project, ProjectAdapterAdvanced.ProjectsHolder> {

    //private ArrayList<Project> projects;
    private View itemView;
    //private ProjectsHolder holder;
    private Context context;
    private FragmentManager fragmentManager;


    public ProjectAdapterAdvanced(@NonNull FirestoreRecyclerOptions<Project> options, Context context) {
        super(options);
        this.context = context;
        this.fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }


//    public ProjectAdapterAdvanced(Context context, ArrayList<Project> projects) {
//        super();
//        this.projects = projects;
//    }

    @NonNull
    @Override
    public ProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        itemView = inflater.inflate(R.layout.adapter_project, parent, false);

        return new ProjectsHolder(itemView);
    }


    @Override
    protected void onBindViewHolder(@NonNull ProjectsHolder holder, int position, @NonNull final Project model) {

        holder.title.setText(model.getTitle());
        holder.startDate.setText(context.getString(R.string.adapter_project_start_date, new SimpleDateFormat("dd.MM.yyyy").format(new Timestamp(model.getStartDate()))));
        holder.endDate.setText(context.getString(R.string.adapter_project_end_date, new SimpleDateFormat("dd.MM.yyyy").format(new Timestamp(model.getEndDate()))));

        String teamMembers = context.getString(R.string.adapter_project_number_users, model.getNumberOfCurrentUsers(), model.getNumberOfUsers());
        holder.numberUsers.setText(teamMembers);


        holder.chipGroup.removeAllViews();
        ArrayList<String> tags = model.getTags();
        final int maxShownChips = 4;
        if (tags != null) {
            for (int i = 0; i < tags.size() && i < maxShownChips; i++)
            {
                Chip chip = new Chip(context);

                chip.setText(tags.get(i));
                //chip.setOnClickListener(chipOnClickListener);
                chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorAccent));
                chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
                //chip.setCloseIconVisible(true);

                holder.chipGroup.addView(chip);
            }

            if (tags.size() > maxShownChips) {
                Chip chip = new Chip(context);

                chip.setText("...");
                //chip.setOnClickListener(chipOnClickListener);
                chip.setChipBackgroundColor(AppCompatResources.getColorStateList(context, R.color.colorAccent));
                chip.setTextColor(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIconTint(AppCompatResources.getColorStateList(context, R.color.text_light));
                //chip.setCloseIcon(AppCompatResources.getDrawable(context, R.drawable.cancel_white));
                //chip.setCloseIconVisible(true);

                holder.chipGroup.addView(chip);
            }
        }


        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectDetailsFragment projectDetailsFragment = new ProjectDetailsFragment();
                Bundle args = new Bundle();
                args.putString("projectId", model.getDocumentId());
                projectDetailsFragment.setArguments(args);
                Methods.addFragment(fragmentManager, projectDetailsFragment, Variables.FRAGMENT_PROJECT);
            }
        });

        /*
        final Project currentProject = projects.get(position);
*/
        //final  ProjectsHolder projectsHolder = ((ProjectsHolder) holder);

        //int requiredMembers = model.getNumberOfUsers() - model.getTeamMembers().size();

        //holder.projectIdTextView.setText(String.valueOf(model.getNumberOfRequiredUsers()));
    }

    static class ProjectsHolder extends RecyclerView.ViewHolder {

        //public TextView projectIdTextView;
        ChipGroup chipGroup;
        TextView title, startDate, endDate, numberUsers;
        View detailsButton;

        ProjectsHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapter_project_TextView_title);
            startDate = itemView.findViewById(R.id.adapter_project_TextView_start_date);
            endDate = itemView.findViewById(R.id.adapter_project_TextView_end_date);
            numberUsers = itemView.findViewById(R.id.adapter_project_TextView_number_users);
            chipGroup = itemView.findViewById(R.id.adapter_project_chips);
            detailsButton = itemView.findViewById(R.id.adapter_project_details);

            //projectIdTextView = itemView.findViewById(R.id.adapter_project_id);
        }
    }
}
