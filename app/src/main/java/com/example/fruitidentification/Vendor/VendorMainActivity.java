package com.example.fruitidentification.Vendor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.fruitidentification.R;
import com.example.fruitidentification.databinding.ActivityMainBinding;

public class VendorMainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new Fragment_vendor_profile());

        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.menu_profile:
                    replaceFragment(new Fragment_vendor_profile());
                    break;
                case R.id.menu_products:
                    break;
                case R.id.menu_orders:
                    break;
                case R.id.menu_identifier:
                    break;
                case R.id.menu_more:
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}