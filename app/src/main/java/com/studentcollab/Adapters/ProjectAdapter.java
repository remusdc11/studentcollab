package com.studentcollab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.studentcollab.Models.Project;

import com.studentcollab.R;

public class ProjectAdapter extends FirestoreRecyclerAdapter<Project, ProjectAdapter.ProjectsHolder> {

    //private ArrayList<Project> projects;
    private View itemView;
    //private ProjectsHolder holder;


    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options) {
        super(options);
    }


//    public ProjectAdapter(Context context, ArrayList<Project> projects) {
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
    protected void onBindViewHolder(@NonNull ProjectsHolder holder, int position, @NonNull Project model) {
        /*
        final Project currentProject = projects.get(position);
*/
        //final  ProjectsHolder projectsHolder = ((ProjectsHolder) holder);

        holder.projectIdTextView.setText(model.getDescription());
    }





    public static class ProjectsHolder extends RecyclerView.ViewHolder {

        public TextView projectIdTextView;

        public ProjectsHolder(@NonNull View itemView) {
            super(itemView);

            projectIdTextView = itemView.findViewById(R.id.adapter_project_id);


        }
    }
}
