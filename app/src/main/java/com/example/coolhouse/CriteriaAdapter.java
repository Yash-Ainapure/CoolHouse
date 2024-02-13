package com.example.coolhouse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder>{
    private List<String> criteriaList;
    private Context context;

    public CriteriaAdapter(Context context,List<String> criteriaList) {
        this.context = context;
        this.criteriaList = criteriaList;
    }

    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View criteriaView = inflater.inflate(R.layout.item_criteria, parent, false);

        // Return a new holder instance
        return new CriteriaViewHolder(criteriaView);
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        // Get the data model based on position
        String criteria = criteriaList.get(position);

        // Set item views based on your views and data model
        holder.criteriaTextView.setText(criteria);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity to display products of the selected criteria
                Intent intent = new Intent(context, Product_Details.class);
                intent.putExtra("criteria", criteria);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return criteriaList.size();
    }

    // View holder class
    public static class CriteriaViewHolder extends RecyclerView.ViewHolder {
        public TextView criteriaTextView;

        public CriteriaViewHolder(View itemView) {
            super(itemView);

            // Your view should match the layout item_criteria.xml
            criteriaTextView = itemView.findViewById(R.id.criteriaTextView);
        }
    }
}
