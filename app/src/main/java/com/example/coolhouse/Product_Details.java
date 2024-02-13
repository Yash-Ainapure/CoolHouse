package com.example.coolhouse;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.coolhouse.ProductList;
import com.example.coolhouse.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class Product_Details extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private List<Product> productList;
    String selectedCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        selectedCriteria = getIntent().getStringExtra("criteria");
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products").child(selectedCriteria);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList,selectedCriteria);

        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        SelectedProductsSingleton.getInstance().loadSelectedProducts(selectedCriteria,productList);
        fetchProducts(productsRef);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Reload products when the activity is resumed
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

//    private void generateBill() {
//        List<Product> selectedProducts = SelectedProductsSingleton.getInstance().getSelectedProducts();
//
//        // Calculate the total amount based on selected products
//        double totalAmount = 0.0;
//        for (Product product : selectedProducts) {
//            totalAmount += product.price;
//        }
//
//    }


//    private void clearSelection() {
//        for (Product product : productList) {
//            product.isSelected = false;
//        }
//        SelectedProductsSingleton.getInstance().clearSelectedProducts();
//        productAdapter.notifyDataSetChanged();
//    }


}






