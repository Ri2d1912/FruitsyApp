package com.example.fruitidentification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// Import the correct fragments from the RegistrationFragment package
import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment1;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment2;
import com.example.fruitidentification.RegistrationFragment.RegistrationFragment3;
import com.example.fruitidentification.RegistrationFragment.VendorRegistrationFragment1;
import com.example.fruitidentification.RegistrationFragment.VendorRegistrationFragment2;
import com.example.fruitidentification.RegistrationFragment.VendorRegistrationFragment3;
import com.example.fruitidentification.ViewModel.regFrag1VM;
import com.example.fruitidentification.ViewModel.vendorRegFragVM;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VendorCreateAcc extends AppCompatActivity {

    FrameLayout VendorRegisterFrameLayoutUserName;
    Button btncreate1, btncreate2, btncreate3,btncreate4, btnBack,btnNext,btnNext2,btnNext3;
    TextView labelAccountDetails;
    LinearLayout LayoutbackAndCreate;
    ImageView imageViewUser;
    vendorRegFragVM vendorViewModel;
    private DBHelper myDB;

    //For userAccount in fragment1
    String username, password, confirmPassword, role;
    FloatingActionButton imgCamera;

    byte[] validId_picture, shopProfilePic;
    String fname, mname, lname, exname, street, barangay, city, province, postal, mobileNo, gender, bday;

    // vendor side

    String shopName, shopStreet, shopBarangay, shopCity, shopProvince, shopPostal, shopMobileNo, shopTelephoneNo, shopEmail, storeHours, description, orderPolicy,reservePolicy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_acc);
        myDB =new DBHelper(this);

        // Initialize views
        VendorRegisterFrameLayoutUserName = findViewById(R.id.VendorRegisterFrameLayoutUserName);
        btncreate1 = findViewById(R.id.btncreate1);
        btncreate2 = findViewById(R.id.btncreate2);
        btncreate3 = findViewById(R.id.btncreate3);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnNext2 = findViewById(R.id.btnNext2);
        btnNext3 = findViewById(R.id.btnNext3);

        labelAccountDetails = findViewById(R.id.LabelAccountdetails);
        LayoutbackAndCreate = findViewById(R.id.LayoutbackAndCreate);

        vendorViewModel = new ViewModelProvider(VendorCreateAcc.this).get(vendorRegFragVM.class);

        // Set the default fragment (RegistrationFragment1) when the activity is opened
        replaceFragment(new VendorRegistrationFragment1());

        // Set onClickListener for btncreate1
        btncreate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment1
                replaceFragment(new VendorRegistrationFragment1());
                changeButtons(1);
            }
        });

        // Set onClickListener for btncreate2
        btncreate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment2
                replaceFragment(new VendorRegistrationFragment2());
                changeButtons(2);
            }
        });

        // Set onClickListener for btncreate3
        btncreate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment3
                replaceFragment(new VendorRegistrationFragment3());

                changeButtons(3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the data from the ViewModel
                username = vendorViewModel.getUsername().getValue();
                password = vendorViewModel.getPassword().getValue();
                confirmPassword = vendorViewModel.getConfirmPassword().getValue();
                role = "vendor";

                // Check if username and password are non-empty and not null
                if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                    if (myDB.isUsernameExists(username)) {
                        Toast.makeText(VendorCreateAcc.this, "Username already exists", Toast.LENGTH_LONG).show();
                    } else if (password.equals(confirmPassword)) {
                        btncreate2.setEnabled(true);
                        changeButtons(2);
                        replaceFragment(new VendorRegistrationFragment2());
                    } else {
                        Toast.makeText(VendorCreateAcc.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VendorCreateAcc.this, "Please Input Username / Password", Toast.LENGTH_LONG).show();
                }
            }
        });



        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve data from the ViewModel instead of directly from fragment views
                fname = vendorViewModel.getFName().getValue();
                mname = vendorViewModel.getMName().getValue();
                lname = vendorViewModel.getLName().getValue();
                exname = vendorViewModel.getExName().getValue();
                gender = vendorViewModel.getGender().getValue();
                bday = vendorViewModel.getBday().getValue();
                street = vendorViewModel.getStreet().getValue();
                barangay = vendorViewModel.getBarangay().getValue();
                city = vendorViewModel.getCity().getValue();
                province = vendorViewModel.getProvince().getValue();
                postal = vendorViewModel.getPostal().getValue();
                mobileNo = vendorViewModel.getMobile().getValue();

                // Handle the image selection if it exists in the ViewModel
                Uri validIdImageUri = vendorViewModel.getValidIdImageUri().getValue();  // Use getValue() to access the actual Uri from LiveData
                if (validIdImageUri != null) {
                    validId_picture = uriToByteArray(validIdImageUri);  // Convert image URI to byte array
                }

                // Validate required fields
                if (fname != null && !fname.isEmpty() && lname != null && !lname.isEmpty()) {
                     replaceFragment(new VendorRegistrationFragment3());
                     btncreate3.setEnabled(true);
                     changeButtons(3);

                } else {
                    // Show an error message if First Name or Last Name is missing
                    Toast.makeText(VendorCreateAcc.this, "Please Input First Name / Last Name", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve all data from the ViewModel
                shopName = vendorViewModel.getShopName().getValue();
                shopStreet = vendorViewModel.getShopStreet().getValue();
                shopBarangay = vendorViewModel.getShopBarangay().getValue();
                shopCity = vendorViewModel.getShopCity().getValue();
                shopProvince = vendorViewModel.getShopProvince().getValue();
                shopPostal = vendorViewModel.getShopPostal().getValue();
                shopMobileNo = vendorViewModel.getshopNo().getValue();
                shopTelephoneNo = vendorViewModel.getTelNo().getValue();
                shopEmail = vendorViewModel.getShopEmail().getValue();
                storeHours = vendorViewModel.getStoreHrs().getValue();
                description = vendorViewModel.getEditDesc().getValue();
                orderPolicy = vendorViewModel.getOrderPolicy().getValue();
                reservePolicy = vendorViewModel.getReservePolicy().getValue();

                // Handle the image selection if it exists in the ViewModel
                Uri shopProfileImageUri = vendorViewModel.getshopProfileImageUri().getValue();
                if (shopProfileImageUri != null) {
                    shopProfilePic = uriToByteArray(shopProfileImageUri);  // Convert image URI to byte array
                }

                // Set the status, e.g., "open"
                String status = "open";

                // Retrieve vendorId from ViewModel or other source
                int vendorId = 2;  // Ensure vendorId is available

                // Insert data into the database
                Boolean checkInsertData = myDB.insertUsers(VendorCreateAcc.this, username, password, role);
                Boolean checkInsertVendorInfo = myDB.insertVendorInfo(VendorCreateAcc.this, username, fname, mname, lname, exname, bday, gender, mobileNo,
                        street, barangay, city, province, postal, validId_picture);

                // Pass all required data to insertFruitShopInfo, including vendorId
                Boolean checkInsertFruitShopInfo = myDB.insertFruitShopInfo(VendorCreateAcc.this, vendorId, shopName, shopStreet,
                        shopBarangay, shopCity, shopProvince, shopPostal, shopMobileNo,
                        shopTelephoneNo, shopEmail, description, storeHours, status,
                        orderPolicy, reservePolicy, shopProfilePic);

                // Check if all insertions were successful
                if (checkInsertVendorInfo && checkInsertFruitShopInfo && checkInsertData) {
                    Intent goLog = new Intent(VendorCreateAcc.this, login.class);
                    startActivity(goLog);
                } else {
                    Toast.makeText(VendorCreateAcc.this, "Record Failed", Toast.LENGTH_LONG).show();
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
        transaction.replace(R.id.VendorRegisterFrameLayoutUserName, fragment, fragment.getClass().getSimpleName());

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

    public byte[] uriToByteArray(Uri uri) {
        try {
            // Use 'this' for Activity context
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            // Convert to byte array
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}