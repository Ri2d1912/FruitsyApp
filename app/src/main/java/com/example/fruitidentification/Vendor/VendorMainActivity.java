package com.example.fruitidentification.Vendor;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fruitidentification.R;
import com.example.fruitidentification.databinding.ActivityVendorMainBinding;

public class VendorMainActivity extends AppCompatActivity {

    private ActivityVendorMainBinding binding;
    private long vendorId;  // Variable to hold vendorId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityVendorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve vendorId from Intent
        vendorId = getIntent().getLongExtra("vendorId", -1);  // Default value -1 if vendorId is not found

        // Check if the vendorId was successfully retrieved
        if (vendorId != -1) {
            Log.d("VendorMainActivity", "Vendor ID: " + vendorId);
        } else {
            Log.d("VendorMainActivity", "No vendorId passed in the Intent.");
        }

        // Set up the BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Create a Bundle to pass vendorId to each fragment
            Bundle bundle = new Bundle();
            bundle.putLong("vendorId", vendorId);  // Pass the vendorId

            switch (item.getItemId()) {
                case R.id.menu_profile:
                    selectedFragment = new Fragment_vendor_profile();
                    break;
                case R.id.menu_products:
                    selectedFragment = new VendorProductsFragment();
                    break;
                case R.id.menu_orders:
                    selectedFragment = new VendorOrdersFragment();
                    break;
                case R.id.menu_identifier:
                    selectedFragment = new VendorIdentifierFragment();
                    break;
                case R.id.menu_more:
                    selectedFragment = new VendorMoreFragment();
                    break;
            }

            // Set the bundle to the selected fragment
            if (selectedFragment != null) {
                selectedFragment.setArguments(bundle);
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // Immediately load the profile fragment with the vendorId when the activity starts
        Fragment_vendor_profile profileFragment = new Fragment_vendor_profile();
        Bundle profileBundle = new Bundle();
        profileBundle.putLong("vendorId", vendorId);  // Pass the vendorId
        profileFragment.setArguments(profileBundle);
        replaceFragment(profileFragment);
    }

    // Method to replace fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment); // Ensure frameLayout exists
        fragmentTransaction.commit();
    }
}