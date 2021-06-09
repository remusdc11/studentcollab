package com.studentcollab.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.studentcollab.Fragments.ProjectDetailsFragment;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.Variables;
import com.studentcollab.Models.Review;
import com.studentcollab.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ReviewAdapter extends FirestoreRecyclerAdapter<Review, ReviewAdapter.ReviewHolder> {

    private View itemView;
    private Context context;
    private FragmentManager fragmentManager;

    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options, Context context) {
        super(options);
        this.context = context;
        this.fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        itemView = inflater.inflate(R.layout.adapter_review, parent, false);

        return new ReviewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewHolder holder, int position, @NonNull final Review model) {

        holder.title.setText(model.getTitle());
        holder.ratingBar.setRating(model.getRating());
        holder.review.setText(model.getReview());
        holder.date.setText(context.getString(R.string.adapter_review_date, new SimpleDateFormat("dd.MM.yyyy").format(new Timestamp(model.getDate()))));

        holder.viewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectDetailsFragment projectDetailsFragment = new ProjectDetailsFragment();
                Bundle args = new Bundle();
                args.putString("projectId", model.getProjectId());
                projectDetailsFragment.setArguments(args);
                Methods.addFragment(fragmentManager, projectDetailsFragment, Variables.FRAGMENT_PROJECT);
            }
        });
    }

    static class ReviewHolder extends RecyclerView.ViewHolder {

        TextView title, review, date;
        RatingBar ratingBar;
        View viewProject;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapter_review_TextView_title);
            review = itemView.findViewById(R.id.adapter_review_TextView_review);
            ratingBar = itemView.findViewById(R.id.adapter_review_rating);
            date = itemView.findViewById(R.id.adapter_review_TextView_date);
            viewProject = itemView.findViewById(R.id.adapter_review_view_project);
        }
    }
}

/*public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private View itemView;
    private Context context;
    private FragmentManager fragmentManager;
    private List<Review> reviewsList;

    public ReviewAdapter(List<Review> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
        this.fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        itemView = inflater.inflate(R.layout.adapter_review, parent, false);

        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review model = this.reviewsList.get(position);
        holder.title.setText(model.getTitle());
        holder.ratingBar.setRating(model.getRating());
        holder.review.setText(model.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    static class ReviewHolder extends RecyclerView.ViewHolder {

        TextView title, review;
        RatingBar ratingBar;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.adapter_review_TextView_title);
            review = itemView.findViewById(R.id.adapter_review_TextView_review);
            ratingBar = itemView.findViewById(R.id.adapter_review_rating);
        }
    }
}*/

