package com.example.fruitidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Registration extends AppCompatActivity {

    Button btnCustomerSign, btnVendorSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnCustomerSign = findViewById(R.id.btnCustomerSign);
        btnVendorSign = findViewById(R.id.btnVendorSign);

        // Click listener for Login Button
        btnCustomerSign.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent loginIntent = new Intent(Registration.this, CustomerCreateAcc.class);
            startActivity(loginIntent);
        });
    }
}