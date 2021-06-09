package com.studentcollab.Globals;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.studentcollab.Models.University;

import java.util.ArrayList;

public class TagsCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> tags;
    private ArrayList<String> copy;
    private FirebaseFirestore db;

    public TagsCompleteAdapter(Context context, int textViewResourceId, ArrayList<String> tags) {
        super(context, textViewResourceId);
        //this.tags = tags;
        this.copy = tags;
        this.tags = tags;
        //this.db = FirebaseFirestore.getInstance();
    }

    public ArrayList<String> getFilteredItems() {
        return this.tags;
    }

    public ArrayList<String> getAllItems() {
        return this.copy;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public String getItem(int index) {
        return tags.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                tags = new ArrayList<>(copy);
                if(constraint != null) {
                    String stringConstraint = constraint.toString();
                    for (int i = 0; i < tags.size(); i++) {
                        if(!tags.get(i).contains(stringConstraint)) {
                            tags.remove(i);
                            i--;
                        }
                    }

                    // Now assign the values and count to the FilterResults object
                    filterResults.values = tags;
                    filterResults.count = tags.size();

                    /*db.collection("tags")
                            .whereGreaterThanOrEqualTo("tag", constraint.toString())
                            //.whereLessThanOrEqualTo("tag", constraint.toString() + '\uf8ff')
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //loadingDialog.dismiss();
                                        tags.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            tags.add(document.getString("tag"));
                                        }

                                        //return filterResults;
                                    } else {
                                        Log.d("TagsAdapter", "Error getting documents: ", task.getException());
                                    }
                                }
                            });*/


                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}