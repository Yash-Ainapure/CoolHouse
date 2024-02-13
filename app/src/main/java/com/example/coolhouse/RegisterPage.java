package com.example.coolhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class RegisterPage extends AppCompatActivity {
    EditText email, password, confirmpassword, firstName, lastName, mobileNo;
    TextView loginpage;
    Button register1;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        firstName = findViewById(R.id.RegFirstName);
        lastName = findViewById(R.id.RegLastName);
        mobileNo = findViewById(R.id.RegPhoneNo);
        register1 = findViewById(R.id.registerbtn);

        register1.setOnClickListener(v -> {
            String emailid = email.getText().toString();
            String pass = password.getText().toString();
            String confirmpass = confirmpassword.getText().toString();
            String fname = firstName.getText().toString();
            String lname = lastName.getText().toString();
            String mobno = mobileNo.getText().toString();
            if (fname.isEmpty()) {
                firstName.setError("Please enter first name");
                firstName.requestFocus();
            } else if (lname.isEmpty()) {
                lastName.setError("Please enter last name");
                lastName.requestFocus();
            } else if (mobno.isEmpty()) {
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
            } else if (emailid.isEmpty() && pass.isEmpty() && confirmpass.isEmpty() && fname.isEmpty() && lname.isEmpty() && mobno.isEmpty()) {
                email.setError("Please enter email id");
                email.requestFocus();
                password.setError("Please enter password");
                password.requestFocus();
                confirmpassword.setError("Please enter confirm password");
                confirmpassword.requestFocus();
                firstName.setError("Please enter first name");
                firstName.requestFocus();
                lastName.setError("Please enter last name");
                lastName.requestFocus();
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
            } else if (!pass.equals(confirmpass)) {
                Toast.makeText(RegisterPage.this, "Password and Confirm Password are not same", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(RegisterPage.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterPage.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                    } else {

//                        saveAdditionalUserInfo(task.getResult().getUser().getUid(),fname, lname, mobno,emailid);

                        startActivity(new Intent(RegisterPage.this, MainActivity.class));
                        Toast.makeText(this, "registered successfully,now you can login with the credentials", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        loginpage = findViewById(R.id.loginbtnreg);
        loginpage.setOnClickListener(view -> {
            Intent stm = new Intent(RegisterPage.this, MainActivity.class);
            startActivity(stm);
        });
    }
}