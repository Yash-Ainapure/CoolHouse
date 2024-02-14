package com.example.coolhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

        // Fetch bills from Firebase
        fetchBillsFromFirebase();
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