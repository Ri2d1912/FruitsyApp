package com.example.fruitidentification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Import the correct fragments from the RegistrationFragment package
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment1;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment2;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment3;

public class CustomerCreateAcc extends AppCompatActivity {

    FrameLayout RegisterFrameLayoutUserName;
    Button btncreate1, btncreate2, btncreate3, btnBack,btnNext,btnNext2;
    TextView labelAccountDetails;
    LinearLayout LayoutbackAndCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_create_acc);

        // Initialize views
        RegisterFrameLayoutUserName = findViewById(R.id.RegisterFrameLayoutUserName);
        btncreate1 = findViewById(R.id.btncreate1);
        btncreate2 = findViewById(R.id.btncreate2);
        btncreate3 = findViewById(R.id.btncreate3);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnNext2 = findViewById(R.id.btnNext2);
        labelAccountDetails = findViewById(R.id.LabelAccountdetails);
        LayoutbackAndCreate = findViewById(R.id.LayoutbackAndCreate);


        // Set the default fragment (RegistrationFragment1) when the activity is opened
        replaceFragment(new RegistrationFragment1());

        // Set onClickListener for btncreate1
        btncreate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment1
                replaceFragment(new RegistrationFragment1());
                changeButtons(1);
            }
        });

        // Set onClickListener for btncreate2
        btncreate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment2
                replaceFragment(new RegistrationFragment2());
                changeButtons(2);
            }
        });

        // Set onClickListener for btncreate3
        btncreate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment3
                replaceFragment(new RegistrationFragment3());

                changeButtons(3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the active fragment using the tag of the current fragment
                Fragment registrationFragment = getSupportFragmentManager().findFragmentByTag(RegistrationFragment1.class.getSimpleName());

                if (registrationFragment != null) {
                    // Retrieve data from the fragment
                    EditText editUsernameCreate = registrationFragment.getView().findViewById(R.id.editUsernameCreate);
                    EditText editPasswordCreate = registrationFragment.getView().findViewById(R.id.editPasswordCreate);
                    EditText editConPasswordCreate = registrationFragment.getView().findViewById(R.id.editConPasswordCreate);

                    String username = editUsernameCreate.getText().toString();
                    String password = editPasswordCreate.getText().toString();
                    String confirmPassword = editConPasswordCreate.getText().toString();

                    // Show the input values in a Toast message (or process them as needed)
                    Toast.makeText(CustomerCreateAcc.this, "Username: " + username + "\nPassword: " + password, Toast.LENGTH_SHORT).show();

                    changeButtons(2);

                    // Replace the current fragment with RegistrationFragment2
                    replaceFragment(new RegistrationFragment2());
                }
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with RegistrationFragment3
                replaceFragment(new RegistrationFragment3());
                changeButtons(3);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current fragment displayed in the FrameLayout
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.RegisterFrameLayoutUserName);

                if (currentFragment instanceof RegistrationFragment3) {
                    replaceFragment(new RegistrationFragment2());
                    changeButtons(2);

                } else if (currentFragment instanceof RegistrationFragment2) {
                    replaceFragment(new RegistrationFragment1());
                    changeButtons(1);
                }
            }
        });
    }

    // Method to replace the fragment in the FrameLayout with the given fragment
    private void replaceFragment(Fragment fragment) {
        // Begin a new fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace the current fragment in the FrameLayout with the new fragment
        // Use the fragment's class name as a tag for reference.
        transaction.replace(R.id.RegisterFrameLayoutUserName, fragment, fragment.getClass().getSimpleName());

        // Commit the transaction to apply the fragment replacement
        transaction.commit();
    }

    private void changeButtons(int clickedBtn) {
        // Reset all buttons to default state
        btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate1.setTextColor(Color.parseColor("#808080"));

        btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate2.setTextColor(Color.parseColor("#808080"));

        btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate3.setTextColor(Color.parseColor("#808080"));

        // Update label text based on the clicked button
        switch (clickedBtn) {
            case 1:
                labelAccountDetails.setText("Account Details");

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate1.setTextColor(Color.parseColor("#FFFF00"));

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate2.setTextColor(Color.parseColor("#808080"));


                btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate3.setTextColor(Color.parseColor("#808080"));
                btnNext.setVisibility(View.VISIBLE);
                LayoutbackAndCreate.setVisibility(View.GONE);

                break;

            case 2:
                labelAccountDetails.setText("Customer Details");

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate2.setTextColor(Color.parseColor("#FFFF00"));

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate1.setTextColor(Color.parseColor("#808080"));

                btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate3.setTextColor(Color.parseColor("#808080"));
                LayoutbackAndCreate.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);

                break;

            case 3:
                labelAccountDetails.setText("Terms of Agreement");

                btncreate3.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate3.setTextColor(Color.parseColor("#FFFF00"));

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate2.setTextColor(Color.parseColor("#808080"));

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate1.setTextColor(Color.parseColor("#808080"));
                LayoutbackAndCreate.setVisibility(View.VISIBLE);  // To show the button
                LayoutbackAndCreate.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                break;
        }
    }

}
