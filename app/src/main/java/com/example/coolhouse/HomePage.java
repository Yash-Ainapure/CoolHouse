package com.example.coolhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        textview=findViewById(R.id.textview1);
        // Your activity initialization code here
        textview.setOnClickListener(v -> {


            Toast.makeText(HomePage.this, "click", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(HomePage.this,Product_Details.class);

            startActivity(i);
        });
    }
}