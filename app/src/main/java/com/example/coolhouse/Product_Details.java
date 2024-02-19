package com.example.coolhouse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product_Details extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> searchProductList;

    String selectedCriteria;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        searchEditText = findViewById(R.id.searchEditText);
        selectedCriteria = getIntent().getStringExtra("criteria");
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products").child(selectedCriteria);

        productList = new ArrayList<>();
        searchProductList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList,selectedCriteria);

        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        SelectedProductsSingleton.getInstance().loadSelectedProducts(selectedCriteria,productList);
        fetchProducts(productsRef);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Called to notify you that characters within `start` and `start + before` are about to be replaced with new text with length `count`.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Called to notify you that somewhere within `start` and `start + before`, the text has been replaced with new text with length `count`.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Called to notify you that the characters within `editable` have changed.
                String newText = editable.toString();
                //fetchProducts(newText);
                searchProducts(newText);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Save selections when the activity is destroyed
        String selectedCriteria = getIntent().getStringExtra("criteria");

        SelectedProductsSingleton.getInstance().saveSelectedProducts(selectedCriteria,productList);
    }

    private void fetchProducts(DatabaseReference productsRef) {
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                SelectedProductsSingleton.getInstance().loadSelectedProducts(selectedCriteria,productList);
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    private void searchProducts(String query) {
        if (query.isEmpty()) {
            productAdapter.updateList(productList);
        } else {
            String searchQuery = query.toString().toLowerCase().trim();
            List<Product> filteredList = new ArrayList<>();

            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(searchQuery)) {
                    filteredList.add(product);
                }
            }
            productAdapter.updateList(filteredList);

        }
    }


    private boolean isProductMatch(Product product, String query) {
        return product.getName().toLowerCase().contains(query.toLowerCase());
    }


}






