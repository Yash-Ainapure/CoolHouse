package com.example.coolhouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private List<Product> productList;
    private Context context;
    private String criteria;

    public ProductAdapter(Context context,List<Product> productList,String criteria) {
        this.context = context;
        this.productList = productList;
        this.criteria = criteria;
        SelectedProductsSingleton.getInstance().loadSelectedProducts(criteria,productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        return new ProductViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the data model based on position
        Product product = productList.get(position);

        // Set item views based on your views and data model
        holder.nameTextView.setText(product.name);
        holder.priceTextView.setText(String.valueOf(product.price));
        holder.selectionCheckBox.setChecked(product.isSelected);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.isSelected = !product.isSelected;

                notifyDataSetChanged();

                SelectedProductsSingleton.getInstance().saveSelectedProducts(criteria,productList);
            }
        });
        holder.selectionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.isSelected = !product.isSelected;

                notifyDataSetChanged();

                SelectedProductsSingleton.getInstance().saveSelectedProducts(criteria,productList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // View holder class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public CheckBox selectionCheckBox;


        public ProductViewHolder(View itemView) {
            super(itemView);

            // Your view should match the layout item_product.xml
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            selectionCheckBox = itemView.findViewById(R.id.selectionCheckBox);

        }
    }
}
