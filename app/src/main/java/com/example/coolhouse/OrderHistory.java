package com.example.coolhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private List<Bill> billList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.billRecyclerView);
        billList = new ArrayList<>();
        billAdapter = new BillAdapter(this, billList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(billAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem1 = menu.findItem(R.id.addProduct);
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("admin@gmail.com")){
            menuItem1.setVisible(true);
        }else{
            menuItem1.setVisible(false);
        }

        // Fetch bills from Firebase
        fetchBillsFromFirebase();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    finish();
                    startActivity(new Intent(OrderHistory.this, HomePage.class));
                    return true;
                } else if (item.getItemId() == R.id.addProduct) {
                    finish();
                    startActivity(new Intent(OrderHistory.this, AddProduct.class));
                    return true;
                }
                // ... handle other items if needed
                return false;
            }
        });
    }

    private void fetchBillsFromFirebase() {
        DatabaseReference billsRef = FirebaseDatabase.getInstance().getReference().child("bills");

        billsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Bill> billList = new ArrayList<>();

                for (DataSnapshot billSnapshot : dataSnapshot.getChildren()) {
                    Bill bill = billSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        billList.add(bill);
                    }
                }

                BillAdapter billAdapter = new BillAdapter(OrderHistory.this, billList);

                // Set the data to the adapter
                billAdapter.setBillList(billList);

                // Set the adapter to your RecyclerView
                recyclerView.setAdapter(billAdapter);

                // Notify the adapter that the data has changed
                billAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}