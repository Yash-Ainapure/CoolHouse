package com.example.coolhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GenerateBillActivity extends AppCompatActivity {

    private TextView billTextView;
//    private EditText paymentDetailsEditText;
    private Button saveBillButton;
    private Button payByCashButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);

        billTextView = findViewById(R.id.billTextView);
//        paymentDetailsEditText = findViewById(R.id.paymentDetailsEditText);
        saveBillButton = findViewById(R.id.saveBillButton);
        payByCashButton = findViewById(R.id.payByCash);

        // Retrieve selected products
        List<Product> selectedProducts = SelectedProductsSingleton.getInstance().getSelectedProducts(null);

        // Calculate total amount
        double totalAmount = calculateTotalAmount(selectedProducts);

        // Display selected products and total amount in the TextView
        displayBill(selectedProducts, totalAmount);

        // Handle save bill button click
        saveBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String paymentDetails = paymentDetailsEditText.getText().toString();
                if(totalAmount == 0){
                    Toast.makeText(GenerateBillActivity.this, "No products selected", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(paymentDetails.isEmpty()){
//                    paymentDetailsEditText.setError("Payment details required");
//                    paymentDetailsEditText.requestFocus();
//                    return;
//                }
                Intent intent=new Intent(GenerateBillActivity.this,DummyUPIPayment.class);
                //intent.putExtra("paymentDetails",paymentDetails);
                intent.putExtra("totalAmount",totalAmount);
                ArrayList<Product> productArrayList = new ArrayList<>(selectedProducts);
                intent.putParcelableArrayListExtra("selectedProducts",productArrayList);
                startActivity(intent);

//                String paymentDetails = paymentDetailsEditText.getText().toString();
//                saveBill(selectedProducts, totalAmount, paymentDetails);
            }
        });

        payByCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paymentDetails = "Cash";
                saveBill(selectedProducts, totalAmount, paymentDetails);
            }
        });
    }

    private double calculateTotalAmount(List<Product> selectedProducts) {
        double totalAmount = 0.0;
        for (Product product : selectedProducts) {
            totalAmount += product.price;
        }
        return totalAmount;
    }

    private void displayBill(List<Product> selectedProducts, double totalAmount) {
        // Build a string representation of the bill
        StringBuilder billText = new StringBuilder("Selected Products:\n");
        for (Product product : selectedProducts) {
            billText.append(product.name).append(" - ₹").append(product.price).append("\n");
        }
        billText.append("\nTotal Amount: ₹").append(totalAmount);

        // Display the bill in the TextView
        billTextView.setText(billText.toString());
    }

    // Inside YourNewActivity where you want to save the bill

    private void saveBill(List<Product> selectedProducts, double totalAmount, String paymentDetails) {

        if(totalAmount == 0){
            Toast.makeText(GenerateBillActivity.this, "No products selected", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(paymentDetails.isEmpty()){
//            paymentDetailsEditText.setError("Payment details required");
//            paymentDetailsEditText.requestFocus();
//            return;
//        }
        // Create a unique ID for the bill
        String billId = generateUniqueBillId();

        // Create a new Bill object
        Bill bill = new Bill();
        bill.setBillId(billId);
        bill.setProducts(selectedProducts);
        bill.setTotalAmount(totalAmount);  // Set the total amount
        bill.setPaymentMode(paymentDetails);

        // Save the bill to Firebase
        DatabaseReference billsRef = FirebaseDatabase.getInstance().getReference().child("bills");
        billsRef.child(billId).setValue(bill)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful save
                    Toast.makeText(GenerateBillActivity.this, "Bill saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(GenerateBillActivity.this, "Failed to save bill", Toast.LENGTH_SHORT).show();
                });
    }

    // Helper method to generate a unique ID for the bill (you can customize this based on your requirements)
    private String generateUniqueBillId() {
        // Implement your own logic to generate a unique ID, e.g., using a timestamp or UUID
        // For simplicity, we're using the current timestamp in milliseconds
        return String.valueOf(System.currentTimeMillis());
    }

}