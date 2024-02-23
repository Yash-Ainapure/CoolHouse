package com.example.coolhouse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder> implements SelectedProductsSingleton.SelectedProductsChangeListener{
    private List<String> criteriaList;
    private List<String> imageList;
    private Context context;

    public CriteriaAdapter(Context context,List<String> criteriaList,List<String> imageList) {
        this.context = context;
        this.criteriaList = criteriaList;
        this.imageList = imageList;
        SelectedProductsSingleton.getInstance().addSelectedProductsChangeListener(this);

        Log.d("Data Size", "criteriaList size: " + criteriaList.size());
        Log.d("Data Size", "imageList size: " + imageList.size());

    }
    @Override
    public void onSelectedProductsChanged() {
        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View criteriaView = inflater.inflate(R.layout.item_criteria, parent, false);
        return new CriteriaViewHolder(criteriaView);
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        String criteria = criteriaList.get(position);


//        String img= imageList.get(position);
//        Picasso.get().load(img).into(holder.criteriaImageView);

        if (position >= 0 && position < imageList.size()) {
            String img = imageList.get(position);
            Picasso.get().load(img).into(holder.criteriaImageView);
        } else {
            Log.e("IndexError", "Invalid position: " + position);
        }


        holder.criteriaTextView.setText(criteria);

        int selectedProductsCount = getSelectedProductsCount(criteria);
        holder.selectedProductsCountTextView.setText("Selected: " + selectedProductsCount);

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
        public TextView selectedProductsCountTextView;
        public ImageView criteriaImageView;




        public CriteriaViewHolder(View itemView) {
            super(itemView);

            // Your view should match the layout item_criteria.xml
            criteriaImageView=itemView.findViewById(R.id.criteriaImageView);
            criteriaTextView = itemView.findViewById(R.id.criteriaTextView);

            selectedProductsCountTextView = itemView.findViewById(R.id.selectedProductsCountTextView);

        }
    }
    private int getSelectedProductsCount(String criteria) {
        List<Product> selectedProducts = SelectedProductsSingleton.getInstance().getSelectedProducts(criteria);
        return selectedProducts.size();
    }
}
