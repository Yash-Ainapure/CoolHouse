package com.example.coolhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CriteriaAdapter criteriaAdapter;
    private List<String> criteriaList;
    private DatabaseReference productsRef;
    Button getBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getBill=findViewById(R.id.getbill);
        recyclerView = findViewById(R.id.recyclerView);
        criteriaList = new ArrayList<>();
        criteriaAdapter = new CriteriaAdapter(this, criteriaList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(criteriaAdapter);

        // Assuming "products" is your root node in the database
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        fetchProductCriteria();

        getBill.setOnClickListener(v->{
            Log.d("sleeee","displaying selected products");
            for (String criteria : criteriaList) {
                List<Product> selectedProducts = SelectedProductsSingleton.getInstance().getSelectedProducts(criteria);
                Log.d("SelectedProducts", "Criteria: " + criteria);
                for (Product product : selectedProducts) {
                    Log.d("SelectedProducts", "  Selected Product: " + product.name);
                }
            }
        });

    }

    private void fetchProductCriteria() {
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot criteriaSnapshot : dataSnapshot.getChildren()) {
                    String criteria = criteriaSnapshot.getKey();
                    criteriaList.add(criteria);
                }
                criteriaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}