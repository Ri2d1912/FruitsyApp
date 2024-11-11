package com.example.fruitidentification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// Import the correct fragments from the RegistrationFragment package
import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment1;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment2;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment3;
import com.google.android.material.textfield.TextInputEditText;

public class CustomerCreateAcc extends AppCompatActivity {

    FrameLayout RegisterFrameLayoutUserName;
    Button btncreate1, btncreate2, btncreate3, btnBack,btnNext,btnNext2,btnNext3;
    TextView labelAccountDetails;
    LinearLayout LayoutbackAndCreate;

    private DBHelper myDB;

    //For userAccount in fragment1
    String username, password, confirmPassword, role;

    String fname, mname, lname, street, barangay, city, province, postal, mobileNo, gender, bday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_create_acc);
        myDB =new DBHelper(this);

        // Initialize views
        RegisterFrameLayoutUserName = findViewById(R.id.RegisterFrameLayoutUserName);
        btncreate1 = findViewById(R.id.btncreate1);
        btncreate2 = findViewById(R.id.btncreate2);
        btncreate3 = findViewById(R.id.btncreate3);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnNext2 = findViewById(R.id.btnNext2);
        btnNext3 = findViewById(R.id.btnNext3);
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
                    username = ((EditText) registrationFragment.getView().findViewById(R.id.editUsernameCreate)).getText().toString();
                    password = ((EditText) registrationFragment.getView().findViewById(R.id.editPasswordCreate)).getText().toString();
                    confirmPassword = ((EditText) registrationFragment.getView().findViewById(R.id.editConPasswordCreate)).getText().toString();
                    role = "customer";

                    if(password.equals(confirmPassword)){
                        changeButtons(2);
                        replaceFragment(new RegistrationFragment2());
                    }

                    else{
                        Toast.makeText(CustomerCreateAcc.this, "Passwords do not match", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment registrationFragment2 = getSupportFragmentManager().findFragmentByTag(RegistrationFragment2.class.getSimpleName());

                if (registrationFragment2 != null) {
                    // Retrieve data from the fragment
                    fname = ((EditText) registrationFragment2.getView().findViewById(R.id.editFname)).getText().toString();
                    mname = ((EditText) registrationFragment2.getView().findViewById(R.id.editMname)).getText().toString();
                    lname = ((EditText) registrationFragment2.getView().findViewById(R.id.editLname)).getText().toString();
                    street = ((EditText) registrationFragment2.getView().findViewById(R.id.editStreet)).getText().toString();
                    barangay = ((EditText) registrationFragment2.getView().findViewById(R.id.editBarangay)).getText().toString();
                    city = ((EditText) registrationFragment2.getView().findViewById(R.id.editCity)).getText().toString();
                    province = ((EditText) registrationFragment2.getView().findViewById(R.id.editProvince)).getText().toString();
                    postal = ((EditText) registrationFragment2.getView().findViewById(R.id.editPostal)).getText().toString();
                    mobileNo = ((EditText) registrationFragment2.getView().findViewById(R.id.editMobileNo)).getText().toString();
                    gender = ((Spinner) registrationFragment2.getView().findViewById(R.id.spinnerGender)).getSelectedItem().toString();
                    bday = ((TextInputEditText) registrationFragment2.getView().findViewById(R.id.editBday)).getText().toString();

                    // Replace the current fragment with RegistrationFragment3
                    replaceFragment(new RegistrationFragment3());
                    changeButtons(3);
                }
            }
        });

        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment registrationFragment3 = getSupportFragmentManager().findFragmentByTag(RegistrationFragment3.class.getSimpleName());

                if (registrationFragment3 != null) {
                    CheckBox checkAgree = registrationFragment3.getView().findViewById(R.id.checkAgree);

                    // Check if the checkbox is checked
                    boolean isChecked = checkAgree.isChecked();
                    if (isChecked) {
                        // Insert customer info and user data if checkbox is checked
                        Boolean checkInsertCustInfo = myDB.insertCustomerInfo(CustomerCreateAcc.this, username, fname, mname, lname, "", bday, gender, mobileNo,
                                street, barangay, city, province, postal, null);

                        Boolean checkInsertData = myDB.insertUsers(CustomerCreateAcc.this, username, password, role);

                        if (checkInsertCustInfo && checkInsertData) {
                            Intent goLog = new Intent(CustomerCreateAcc.this, login.class);
                            startActivity(goLog);
                        } else {
                            Toast.makeText(CustomerCreateAcc.this, "Record Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // If checkbox is not checked, show a message
                        Toast.makeText(CustomerCreateAcc.this, "Please agree to the terms", Toast.LENGTH_SHORT).show();
                    }
                }
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
                btnNext2.setVisibility(View.VISIBLE);
                btnNext3.setVisibility(View.GONE);

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
                btnNext2.setVisibility(View.GONE);
                btnNext3.setVisibility(View.VISIBLE);

                break;
        }
    }

}