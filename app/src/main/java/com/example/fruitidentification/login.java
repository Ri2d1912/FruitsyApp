package com.example.fruitidentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fruitidentification.Model.DBHelper;

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

                // Retrieve the password associated with the entered username from the database
                String passwordFromDB = myDB.getPassword(checkUsername);

                if(passwordFromDB != null && passwordFromDB.equals(checkUserpass.toString())){
                    Intent goSign = new Intent(login.this, MainActivity.class);
                    startActivity(goSign);
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