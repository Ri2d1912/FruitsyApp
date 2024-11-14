package com.example.fruitidentification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LandingPage extends AppCompatActivity {

    Button loginButton, signUpButton, guestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Buttons
         loginButton = findViewById(R.id.login_btn);
         signUpButton = findViewById(R.id.signup_btn);
         guestButton = findViewById(R.id.guest_btn);

        // Click listener for Login Button
        loginButton.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent loginIntent = new Intent(LandingPage.this, login.class);
            startActivity(loginIntent);
        });

        // Click listener for Sign Up Button
        signUpButton.setOnClickListener(view -> {
            // Navigate to CreateAccountActivity
            Intent signUpIntent = new Intent(LandingPage.this, Registration.class);
            startActivity(signUpIntent);
        });
//
//        // Click listener for Guest Button
//        guestButton.setOnClickListener(view -> {
//            // Navigate to GuestActivity or simply handle the guest flow
//            Intent guestIntent = new Intent(LandingPage.this, GuestActivity.class);
//            startActivity(guestIntent);
//        });
    }
}
