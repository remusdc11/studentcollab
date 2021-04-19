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
    private ArrayList<String> mData;
    private FirebaseFirestore db;

    public TagsCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<String>();
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>

                   /* db.collection("tags").document(constraint).get()
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

                    StyleFetcher fetcher = new StyleFetcher();
                    try {
                        mData = fetcher.retrieveResults(constraint.toString());
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }*/
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
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