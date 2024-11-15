package com.example.fruitidentification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.fruitidentification.RegistrationFragment.VendorRegistrationFragment4;
import com.example.fruitidentification.ViewModel.regFrag1VM;
import com.example.fruitidentification.ViewModel.shopLocationViewModel;
import com.example.fruitidentification.ViewModel.vendorRegFragVM;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VendorCreateAcc extends AppCompatActivity {

    FrameLayout VendorRegisterFrameLayoutUserName;
    Button btncreate1, btncreate2, btncreate3,btncreate4, btnBack,btnNext,btnNext2,btnNext3, btnNext4;
    TextView labelAccountDetails;
    LinearLayout LayoutbackAndCreate;
    ImageView imageViewUser;
    vendorRegFragVM vendorViewModel;

    private DBHelper myDB;

    //For userAccount in fragment1
    String username, password, confirmPassword, role;
    FloatingActionButton imgCamera;

    byte[] validId_picture, shopProfilePic, shopHeaderProfileImage,dtiFile, birFile;

    String fname, mname, lname, exname, street, barangay, city, province, postal, mobileNo, gender, bday;

    // vendor side
    String shopName, shopStreet, shopBarangay, shopCity, shopProvince, shopPostal, shopMobileNo, shopTelephoneNo, shopEmail, storeHours, description, orderPolicy,reservePolicy, status;
    // shop location
    shopLocationViewModel shopLocVIewModel;
    double latitude, longitude;
    String region, pinAddress;
    boolean isPrimary;
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
        btncreate4 = findViewById(R.id.btncreate4);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnNext2 = findViewById(R.id.btnNext2);
        btnNext3 = findViewById(R.id.btnNext3);
        btnNext4 = findViewById(R.id.btnNext4);


        labelAccountDetails = findViewById(R.id.LabelAccountdetails);
        LayoutbackAndCreate = findViewById(R.id.LayoutbackAndCreate);

        vendorViewModel = new ViewModelProvider(VendorCreateAcc.this).get(vendorRegFragVM.class);
        shopLocVIewModel = new ViewModelProvider(VendorCreateAcc.this).get(shopLocationViewModel.class);


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

        btncreate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to RegistrationFragment3
                replaceFragment(new VendorRegistrationFragment4());

                changeButtons(4);
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

                // Accessing latitude, longitude, region, etc.
                 latitude = shopLocVIewModel.getLatitude().getValue();
                 longitude = shopLocVIewModel.getLongitude().getValue();
                 region = shopLocVIewModel.getRegion().getValue();
                 pinAddress = shopLocVIewModel.getAddress().getValue();
                 isPrimary = shopLocVIewModel.getIsPrimary().getValue();

                // Handle the image selection if it exists in the ViewModel
                Uri shopProfileImageUri = vendorViewModel.getshopProfileImageUri().getValue();
                if (shopProfileImageUri != null) {
                    shopProfilePic = uriToByteArray(shopProfileImageUri);  // Convert image URI to byte array
                }

              if(shopName != null && !shopName.isEmpty()){
                  replaceFragment(new VendorRegistrationFragment4());
                  btncreate4.setEnabled(true);
                  changeButtons(4);
              }
              else{
                  Toast.makeText(VendorCreateAcc.this, "Please input shop name", Toast.LENGTH_LONG).show();

              }
            }
        });

        btnNext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the fragment to access the buttons
                VendorRegistrationFragment4 fragment = (VendorRegistrationFragment4) getSupportFragmentManager()
                        .findFragmentByTag("VendorRegistrationFragment4");

                if (fragment != null) {
                    View fragmentView = fragment.getView();
                    if (fragmentView != null) {
                        // Get buttons
                        Button btnAttachFile = fragmentView.findViewById(R.id.buttonAttachFileImage);
                        Button btnAttachDti = fragmentView.findViewById(R.id.buttonAttachFileDti);
                        Button btnAttachBir = fragmentView.findViewById(R.id.buttonAttachFileBir);

                        // Check if the text is still "Attach File"
                        String shopHeaderProfileImageText = btnAttachFile.getText().toString();
                        String dtiFileText = btnAttachDti.getText().toString();
                        String birFileText = btnAttachBir.getText().toString();

                        boolean isShopHeaderProfileImageEmpty = shopHeaderProfileImageText.equals("Attach File");
                        boolean isDtiFileEmpty = dtiFileText.equals("Attach File");
                        boolean isBirFileEmpty = birFileText.equals("Attach File");

                        if (isShopHeaderProfileImageEmpty || isDtiFileEmpty || isBirFileEmpty) {
                            // If any file is not attached, show a message
                            Toast.makeText(VendorCreateAcc.this, "Please upload DTI / BIR files / Shop Image.", Toast.LENGTH_LONG).show();
                        } else {
                            // Get URIs from ViewModel
                            Uri shopHeaderProfileImageUri = vendorViewModel.getShopHeaderProfileImageUri().getValue();
                            Uri dtiFileUri = vendorViewModel.getDtiFileUri().getValue();
                            Uri birFileUri = vendorViewModel.getBirFileUri().getValue();

                            // Convert URIs to byte arrays
                            shopHeaderProfileImage = uriToByteArray(shopHeaderProfileImageUri);
                            dtiFile = uriToByteArray(dtiFileUri);
                            birFile = uriToByteArray(birFileUri);

                            status = "open";

                            // Insert user data and get vendorId
                            long vendorId = myDB.insertUsers(VendorCreateAcc.this, username, password, role);

                            if (vendorId == -1) {
                                Toast.makeText(VendorCreateAcc.this, "User insertion failed", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Proceed with inserting vendor info with the retrieved vendorId
                            Boolean checkInsertVendorInfo = myDB.insertVendorInfo(VendorCreateAcc.this, username, fname, mname, lname, exname, bday, gender, mobileNo,
                                    street, barangay, city, province, postal, validId_picture);

                            // Insert fruit shop info and get the inserted row's ID (shopId)
                            Boolean checkInsertFruitShopInfo = myDB.insertFruitShopInfo(VendorCreateAcc.this, (int) vendorId, shopName, shopStreet,
                                    shopBarangay, shopCity, shopProvince, shopPostal, shopMobileNo,
                                    shopTelephoneNo, shopEmail, description, storeHours, status,
                                    orderPolicy, reservePolicy, shopProfilePic, dtiFile, birFile, shopHeaderProfileImage);

                            // Retrieve the shopId (last inserted row's ID)
                            int shopId = myDB.getLatestShopId();
                            // Insert shop location with the retrieved shopId
                            Boolean checkInsertShopLocation = myDB.insertOrUpdateShopLocation(shopId, latitude, longitude, region, pinAddress, isPrimary);

                            // Check if all insertions were successful
                            if (checkInsertVendorInfo && checkInsertFruitShopInfo && vendorId != -1 && checkInsertShopLocation) {
                                Intent goLog = new Intent(VendorCreateAcc.this, login.class);
                                startActivity(goLog);
                            } else {
                                Toast.makeText(VendorCreateAcc.this, "Record insertion failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current fragment displayed in the FrameLayout
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.VendorRegisterFrameLayoutUserName);

                // Check the type of the current fragment and replace it accordingly
                if (currentFragment instanceof VendorRegistrationFragment4) {
                    replaceFragment(new VendorRegistrationFragment3());
                    changeButtons(3);
                }
                else if (currentFragment instanceof VendorRegistrationFragment3) {
                    replaceFragment(new VendorRegistrationFragment2());
                    changeButtons(2);
                } else if (currentFragment instanceof VendorRegistrationFragment2) {
                    replaceFragment(new VendorRegistrationFragment1());
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
        transaction.addToBackStack(null);

        // Commit the transaction to apply the fragment replacement
        transaction.commit();
    }

    private byte[] uriToByteArray(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true); // Resize to 500x500 pixels
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream); // Compress to JPEG with 80% quality
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    private void changeButtons(int clickedBtn) {
        // Reset all buttons to default state
        btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate1.setTextColor(Color.parseColor("#808080"));

        btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate2.setTextColor(Color.parseColor("#808080"));

        btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate3.setTextColor(Color.parseColor("#808080"));

        btncreate4.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
        btncreate4.setTextColor(Color.parseColor("#808080"));

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

                btncreate4.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate4.setTextColor(Color.parseColor("#808080"));
                btnNext.setVisibility(View.VISIBLE);
                LayoutbackAndCreate.setVisibility(View.GONE);
                break;

            case 2:
                labelAccountDetails.setText("Vendor Details");

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate2.setTextColor(Color.parseColor("#FFFF00"));

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate1.setTextColor(Color.parseColor("#808080"));

                btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate3.setTextColor(Color.parseColor("#808080"));

                btncreate4.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate4.setTextColor(Color.parseColor("#808080"));


                LayoutbackAndCreate.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnNext2.setVisibility(View.VISIBLE);
                btnNext3.setVisibility(View.GONE);
                btnNext4.setVisibility(View.GONE);

                break;

            case 3:
                labelAccountDetails.setText("Shop Details");

                btncreate3.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate3.setTextColor(Color.parseColor("#FFFF00"));

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate2.setTextColor(Color.parseColor("#808080"));

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate1.setTextColor(Color.parseColor("#808080"));

                btncreate4.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate4.setTextColor(Color.parseColor("#808080"));
                LayoutbackAndCreate.setVisibility(View.VISIBLE);  // To show the button
                LayoutbackAndCreate.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnNext2.setVisibility(View.GONE);
                btnNext3.setVisibility(View.VISIBLE);
                btnNext4.setVisibility(View.GONE);

                break;

            case 4:
                labelAccountDetails.setText("Shop Requirements");

                btncreate3.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate3.setTextColor(Color.parseColor("#808080"));

                btncreate2.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate2.setTextColor(Color.parseColor("#808080"));

                btncreate1.setBackgroundResource(R.drawable.btncreatenavigationtransparent);
                btncreate1.setTextColor(Color.parseColor("#808080"));

                btncreate4.setBackgroundResource(R.drawable.btncreatenavigation);
                btncreate4.setTextColor(Color.parseColor("#FFFF00"));

                LayoutbackAndCreate.setVisibility(View.VISIBLE);  // To show the button
                LayoutbackAndCreate.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnNext2.setVisibility(View.GONE);
                btnNext3.setVisibility(View.GONE);
                btnNext4.setVisibility(View.VISIBLE);
                break;
        }
    }

}