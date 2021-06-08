package com.studentcollab.Adapters;

import android.content.Context;
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
import com.google.android.material.chip.ChipGroup;
import com.studentcollab.Models.Review;
import com.studentcollab.R;

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

        itemView = inflater.inflate(R.layout.adaptar_review, parent, false);

        return new ReviewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewHolder holder, int position, @NonNull final Review model) {

        holder.title.setText(model.getTitle());
        holder.ratingBar.setRating(model.getRating());
        holder.review.setText(model.getReview());
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
}
