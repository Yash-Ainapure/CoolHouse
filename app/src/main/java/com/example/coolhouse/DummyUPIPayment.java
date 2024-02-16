package com.example.coolhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DummyUPIPayment extends AppCompatActivity {

    TextView totalAmountTextView;
    Button payButton,One,Two,Three,Four,Five,Six,Seven,Eight,Nine,Zero;
    ImageButton Clrbtn;
    EditText upiPasswordEditText;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private final StringBuilder passwordBuilder = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_upipayment);

        totalAmountTextView = findViewById(R.id.totalAmount);
        upiPasswordEditText = findViewById(R.id.pinpass);
        payButton = findViewById(R.id.pay);
        One = findViewById(R.id.one);
        Two = findViewById(R.id.two);
        Three = findViewById(R.id.three);
        Four = findViewById(R.id.four);
        Five = findViewById(R.id.five);
        Six = findViewById(R.id.six);
        Seven = findViewById(R.id.seven);
        Eight = findViewById(R.id.eight);
        Nine = findViewById(R.id.nine);
        Zero = findViewById(R.id.zero);
        Clrbtn = findViewById(R.id.clrbtn);
        // Set click listeners for numeric buttons
        One.setOnClickListener(v -> appendValueToPassword("1"));
        Two.setOnClickListener(v -> appendValueToPassword("2"));
        Three.setOnClickListener(v -> appendValueToPassword("3"));
        Four.setOnClickListener(v -> appendValueToPassword("4"));
        Five.setOnClickListener(v -> appendValueToPassword("5"));
        Six.setOnClickListener(v -> appendValueToPassword("6"));
        Seven.setOnClickListener(v -> appendValueToPassword("7"));
        Eight.setOnClickListener(v -> appendValueToPassword("8"));
        Nine.setOnClickListener(v -> appendValueToPassword("9"));
        Zero.setOnClickListener(v -> appendValueToPassword("0"));
        Clrbtn.setOnClickListener(v -> deleteLastCharacter());

        // Set click listener for pay button
        payButton.setOnClickListener(v -> {
            // Implement your payment logic here using the password
            String password = upiPasswordEditText.getText().toString();
            // Do something with the password, e.g., initiate payment
        });
        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        double totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
        //String paymentDetails=intent.getStringExtra("paymentDetails");
        ArrayList<Product> receivedProductList = intent.getParcelableArrayListExtra("selectedProducts");


        Toast.makeText(this, "Total amount : "+totalAmount, Toast.LENGTH_SHORT).show();
        totalAmountTextView.setText("₹ "+totalAmount);

        payButton.setOnClickListener(view -> {
            String enteredPassword = upiPasswordEditText.getText().toString();

            if ("123456".equals(enteredPassword)) {
                saveBill(receivedProductList,totalAmount);

            } else {

                Toast.makeText(DummyUPIPayment.this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                passwordBuilder.setLength(0);
                updatePasswordEditText();
            }

        });
    }

    private void appendValueToPassword(String value) {
        if (passwordBuilder.length() < 6) {
            passwordBuilder.append(value);
            updatePasswordEditText();
        }
    }

    private void deleteLastCharacter() {
        if (passwordBuilder.length() > 0) {
            passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
            updatePasswordEditText();
        }
    }

    private void updatePasswordEditText() {
        upiPasswordEditText.setText(passwordBuilder.toString());
    }


    private void saveBill(List<Product> selectedProducts, double totalAmount) {

        // Create a unique ID for the bill
        String billId = generateUniqueBillId();

        // Create a new Bill object
        Bill bill = new Bill();
        bill.setBillId(billId);
        bill.setProducts(selectedProducts);
        bill.setTotalAmount(totalAmount);  // Set the total amount

        // Save the bill to Firebase
        DatabaseReference billsRef = FirebaseDatabase.getInstance().getReference().child("bills");
        billsRef.child(billId).setValue(bill)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful save
                    Intent intent1 = new Intent(DummyUPIPayment.this, HomePage.class);
                    startActivity(intent1);
                    Toast.makeText(DummyUPIPayment.this, "Bill saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(DummyUPIPayment.this, "Failed to save bill", Toast.LENGTH_SHORT).show();
                });
    }

    // Helper method to generate a unique ID for the bill (you can customize this based on your requirements)
    private String generateUniqueBillId() {
        // Implement your own logic to generate a unique ID, e.g., using a timestamp or UUID
        // For simplicity, we're using the current timestamp in milliseconds
        return String.valueOf(System.currentTimeMillis());
    }
}