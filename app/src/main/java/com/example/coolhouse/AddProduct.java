package com.example.coolhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddProduct extends AppCompatActivity {

    private Spinner criteriaSpinner;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private Button addProductButton;

    private DatabaseReference productsRef;
    private String newCategoryName;
    private Spinner criteriaSpinner1;
    private Spinner productSpinner;
    private Button deleteProductButton;
    DatabaseReference criteriaRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //add product elements
        criteriaSpinner = findViewById(R.id.criteriaSpinner);
        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        addProductButton = findViewById(R.id.addProductButton);

        //delete product elements
        criteriaSpinner1 = findViewById(R.id.criteriaSpinner1);
        productSpinner = findViewById(R.id.productSpinner);
        deleteProductButton = findViewById(R.id.deleteProductButton);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        criteriaRef = FirebaseDatabase.getInstance().getReference().child("products");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        bottomNavigationView.setSelectedItemId(R.id.addProduct);
        MenuItem menuItem1 = menu.findItem(R.id.addProduct);
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("admin@gmail.com")){
            menuItem1.setVisible(true);
        }else{
            menuItem1.setVisible(false);
        }

        // Fetch bills from Firebase


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    finish();
                    startActivity(new Intent(AddProduct.this, HomePage.class));
                    return true;
                } else if (item.getItemId() == R.id.product_History) {
                    finish();
                    startActivity(new Intent(AddProduct.this, OrderHistory.class));
                    return true;
                } else if (item.getItemId() == R.id.logout) {
                    showLogoutConfirmationDialog();
                    return true;
                }
                // ... handle other items if needed
                return false;
            }
        });
        List<String> criteriaList = new ArrayList<>();
        List<String> criteriaList2 = new ArrayList<>();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                criteriaList.clear();

                for (DataSnapshot criteriaSnapshot : dataSnapshot.getChildren()) {
                    String criteria = criteriaSnapshot.getKey();
                    criteriaList.add(criteria);
                }
                criteriaList.add("Create New Category");

                ArrayAdapter<String> criteriaAdapter = new ArrayAdapter<>(AddProduct.this, R.layout.spinner_item, criteriaList);
                criteriaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                criteriaSpinner.setAdapter(criteriaAdapter);

                criteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedCriteria = criteriaList.get(position);
                        if (selectedCriteria.equals("Create New Category")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
                            builder.setTitle("Create New Category");

                            // Set up the input
                            final EditText input = new EditText(AddProduct.this);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);

                            // Set up the buttons
                            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    newCategoryName = input.getText().toString().trim();

                                    // Check if the input is not empty
                                    if (!newCategoryName.isEmpty()) {
                                        // TODO: Save the new category to the database or handle the creation logic

                                        // For simplicity, let's show a Toast
                                        Toast.makeText(AddProduct.this, "New Category Created: " + newCategoryName, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If the input is empty, show a Toast
                                        Toast.makeText(AddProduct.this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        } else {
                            // Handle the selection of existing criteria
                            // ...
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        criteriaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                criteriaList2.clear();


                for (DataSnapshot criteriaSnapshot : dataSnapshot.getChildren()) {
                    String criteria = criteriaSnapshot.getKey();
                    criteriaList2.add(criteria);
                }

                ArrayAdapter<String> criteriaAdapter = new ArrayAdapter<>(AddProduct.this, R.layout.spinner_item, criteriaList2);
                criteriaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                criteriaSpinner1.setAdapter(criteriaAdapter);

                criteriaSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedCriteria = criteriaList2.get(position);

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("products").child(selectedCriteria);
                        dbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> productList = new ArrayList<>();
                                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                    String product = productSnapshot.getKey();
                                    productList.add(product);
                                }
                                ArrayAdapter<String> productAdapter = new ArrayAdapter<>(AddProduct.this,R.layout.spinner_item, productList);
                                productAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                productSpinner.setAdapter(productAdapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        deleteProductButton.setOnClickListener(v->{
            String criteria = criteriaSpinner1.getSelectedItem().toString();
            String productName = productSpinner.getSelectedItem().toString();
            productsRef.child(criteria).child(productName).removeValue();
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(AddProduct.this, MainActivity.class));
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
    public void onAddProductClick(View view) {
        String criteria = criteriaSpinner.getSelectedItem().toString();
        String productName = productNameEditText.getText().toString().trim();
        String productPriceText = productPriceEditText.getText().toString().trim();

        if (productName.isEmpty() || productPriceText.isEmpty()) {
            Toast.makeText(this, "Please enter product name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        double productPrice = Integer.parseInt(productPriceText);

        // Now you have the criteria, product name, and product price
        // You can add this product to the Firebase Realtime Database
        addProductToDatabase(criteria, productName, productPrice);

        // Optionally, you can finish the activity or perform other actions
        finish();
    }

    private void addProductToDatabase(String criteria, String productName, double productPrice) {
        // Create a new Product object with the entered details
        Product product = new Product();
        product.name = productName;
        product.price = productPrice;

        if(criteria.equals("Create New Category")){
            productsRef.child(newCategoryName).child(productName).setValue(product);
            Toast.makeText(this, "demo success", Toast.LENGTH_SHORT).show();
        }else{
            productsRef.child(criteria).child(productName).setValue(product);
        }
        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
    }

}