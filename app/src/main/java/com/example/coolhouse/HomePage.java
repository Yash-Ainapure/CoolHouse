package com.example.coolhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
    private TextView selectedProductsCountTextView;
    private Button clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getBill=findViewById(R.id.getbill);
        selectedProductsCountTextView = findViewById(R.id.selectedProductsCountTextView);
        recyclerView = findViewById(R.id.recyclerView);
        criteriaList = new ArrayList<>();
        criteriaAdapter = new CriteriaAdapter(this, criteriaList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(criteriaAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem1 = menu.findItem(R.id.addProduct);
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("admin@gmail.com")){
            menuItem1.setVisible(true);
        }else{
            menuItem1.setVisible(false);
        }



        clearButton = findViewById(R.id.clearSelection);
        clearButton.setOnClickListener(v->{
            SelectedProductsSingleton.getInstance().clearAllSelectedProducts();
        });

        // Assuming "products" is your root node in the database
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        fetchProductCriteria();
        setupSelectedProductsCountListener();
        getBill.setOnClickListener(v->{
            Intent intent = new Intent(HomePage.this, GenerateBillActivity.class);
            startActivity(intent);
        });


        //bottom navigation sidebar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.product_History) {
                    finish();
                    startActivity(new Intent(HomePage.this, OrderHistory.class));
                    return true;
                } else if (item.getItemId() == R.id.addProduct) {
                    finish();
                    startActivity(new Intent(HomePage.this, AddProduct.class));
                    return true;
                }
                else if (item.getItemId() == R.id.logout) {
                    showLogoutConfirmationDialog();
                    return true;
                }
                return false;
            }
        });


    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to logout?");

        // Add buttons for positive (yes) and negative (no) actions
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Yes", perform logout
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "No", do nothing or handle accordingly
            }
        });

        // Create and show the dialog
        builder.create().show();
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

    private void setupSelectedProductsCountListener() {
        // Listen for changes in the selected products and update the count
        SelectedProductsSingleton.getInstance().addSelectedProductsChangeListener(new SelectedProductsSingleton.SelectedProductsChangeListener() {
            @Override
            public void onSelectedProductsChanged() {
                updateSelectedProductsCount();
            }
        });
    }

    private void updateSelectedProductsCount() {
        // Update the TextView on the main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Retrieve or calculate selected products count
                int selectedProductsCount = calculateSelectedProductsCount();
                selectedProductsCountTextView.setText("Selected Products Count: " + selectedProductsCount);
            }
        });
    }

    private int calculateSelectedProductsCount() {
        // Example: Retrieve selected products count for all criteria
        int totalSelectedProductsCount = 0;
        for (String criteria : criteriaList) {
            List<Product> selectedProducts = SelectedProductsSingleton.getInstance().getSelectedProducts(criteria);
            totalSelectedProductsCount += selectedProducts.size();
        }
        return totalSelectedProductsCount;
    }


}