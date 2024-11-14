package com.example.fruitidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.Vendor.VendorMainActivity;

public class login extends AppCompatActivity {
    TextView forgotPasswordLink, createAccountLink;
    EditText editUsername, editPassword;
    Button btnLogin;
    private DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        myDB =new DBHelper(this);

        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        createAccountLink = findViewById(R.id.createAccountLink);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered username and password
                String checkUsername = editUsername.getText().toString();
                String checkUserpass = editPassword.getText().toString();

                // Retrieve the password and role associated with the entered username from the database
                String passwordFromDB = myDB.getPassword(checkUsername);
                String userrole = myDB.getRole(checkUsername);

                if (passwordFromDB != null && passwordFromDB.equals(checkUserpass)) {
                    // Check the user role
                    if ("customer".equals(userrole)) {
                        // If the user is a customer, navigate to the landing page
                        Intent goSign = new Intent(login.this, LandingPage.class);
                        startActivity(goSign);
                    } else if ("vendor".equals(userrole)) {
                        // Retrieve the vendorId associated with the username from the database
                        long vendorId = myDB.getVendorId(checkUsername);
                        Log.d("VendorIdCheck", "Retrieved vendorId: " + vendorId);  // Log the value to verify it

                        // Pass the vendorId to VendorMainActivity or other activities as needed
                        Intent goSign = new Intent(login.this, VendorMainActivity.class);
                        goSign.putExtra("vendorId", vendorId);  // Pass vendorId through Intent
                        startActivity(goSign);
                    }
                } else {
                    // Username not found or password mismatch
                    Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });




        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSign = new Intent(login.this, Registration.class);
                startActivity(goSign);
            }
        });

    }

}