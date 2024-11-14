package com.example.fruitidentification.Vendor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fruitidentification.R;
import com.example.fruitidentification.databinding.ActivityVendorMainBinding;

public class VendorMainActivity extends AppCompatActivity {

    private ActivityVendorMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityVendorMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_profile:
                    replaceFragment(new Fragment_vendor_profile());
                    break;
                case R.id.menu_products:
                    replaceFragment(new VendorProductsFragment());
                    break;
                case R.id.menu_orders:
                    replaceFragment(new VendorOrdersFragment());
                    break;
                case R.id.menu_identifier:
                    // Handle identifier fragment
                    break;
                case R.id.menu_more:
                    replaceFragment(new VendorMoreFragment());
                    break;
            }

            return true;
        });

        // Optionally, you can set a default fragment to be loaded initially
        replaceFragment(new Fragment_vendor_profile());
    }

    // Method to replace fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment); // Ensure frameLayout exists
        fragmentTransaction.commit();
    }
}
