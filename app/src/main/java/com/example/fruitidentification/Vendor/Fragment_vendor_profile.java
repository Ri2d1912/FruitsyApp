package com.example.fruitidentification.Vendor;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.shopInfoViewModel;

import java.util.List;

public class Fragment_vendor_profile extends Fragment {
    private DBHelper myDB;
    private TextView profile_fruitshop_name, description1, shopDescAddress, shopstorehoursdescrip, shopContactContent, shopContact2Content,
            shopEmailInfo, shopPolicyOrder, shopPolicy2, shopPostal, shopMobile, shopTelephone,
            shopOpeningHours, emailText;
    private shopInfoViewModel shopviewModel;
    private long vendorId; // Variable to hold the vendorId

    public Fragment_vendor_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_profile, container, false);
        myDB = new DBHelper(getActivity());  // Initialize the DBHelper object

        // Retrieve the vendorId from arguments passed to the fragment
        if (getArguments() != null) {
            vendorId = getArguments().getLong("vendorId", -1); // Default to -1 if vendorId is not found
        }

        // Log the vendorId for debugging
        Log.d("Fragment_vendor_profile", "Retrieved vendorId: " + vendorId);

        // Show a Toast with the vendorId
        if (vendorId != -1) {
            Toast.makeText(getActivity(), "Vendor ID: " + vendorId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Vendor ID not found", Toast.LENGTH_SHORT).show();
        }

        // Initialize the TextViews
        shopDescAddress = view.findViewById(R.id.shopDescAddress);
        description1 = view.findViewById(R.id.description1);
        shopstorehoursdescrip = view.findViewById(R.id.shopstorehoursdescrip);
        shopContactContent = view.findViewById(R.id.shopContactContent);
        shopContact2Content = view.findViewById(R.id.shopContact2Content);
        shopEmailInfo = view.findViewById(R.id.shopEmailInfo);
        shopPolicyOrder = view.findViewById(R.id.shopPolicyOrder);
        shopPolicy2 = view.findViewById(R.id.shopPolicy2);
        shopMobile = view.findViewById(R.id.shopContactContent);
        shopTelephone = view.findViewById(R.id.shopContact2Content);
        shopOpeningHours = view.findViewById(R.id.shopstorehoursdescrip);

        profile_fruitshop_name = view.findViewById(R.id.profile_fruitshop_name);
        emailText = view.findViewById(R.id.emailText);

        // Load the vendor profile if vendorId is valid
        if (vendorId != -1) {
            loadVendorProfile(vendorId);
        }
        return view;
    }

    // Method to load vendor profile details
    private void loadVendorProfile(long vendorId) {
        // Query the database to get the vendor information using vendorId
        List<shopInfoViewModel> vendorDetails = myDB.getFruitShopInfo(vendorId);

        if (vendorDetails != null && !vendorDetails.isEmpty()) {
            // Assuming you want to display the first entry in the list
            shopInfoViewModel vendor = vendorDetails.get(0); // Get the first vendor

            // Retrieve address information from vendor object
            String Fruitshopname = vendor.getShopName();
            String shopDesc = vendor.getShopDesc();
            String shopEmail = vendor.getEmail();
            String shopStreet = vendor.getShopStreet();
            String shopBarangay = vendor.getShopBarangay();
            String shopCity = vendor.getShopCity();
            String shopProvince = vendor.getShopProvince();
            String mobileNumber = vendor.getMobileNumber();
            String telephoneNumber = vendor.getTelephoneNumber();
            String immediateOrderPolicy = vendor.getImmediateOrderPolicy();
            String advanceReservationPolicy = vendor.getAdvanceReservationPolicy();
            String shopOpeningHrs = vendor.getOpeningHrs();


            profile_fruitshop_name.setText(Fruitshopname);
            description1.setText(shopDesc);
            emailText.setText(shopEmail);

            // Construct the full address
            String fullAddress = shopStreet + ", " + shopBarangay + ", " + shopCity + ", " + shopProvince;
            shopDescAddress.setText(fullAddress); // Set the full address
            // Set the mobile number
            shopMobile.setText(mobileNumber);
            // Set the telephone number
            shopTelephone.setText(telephoneNumber);
            shopOpeningHours.setText(shopOpeningHrs);
            // Set the email info
            shopEmailInfo.setText(shopEmail);

            // Set the policies
            shopPolicyOrder.setText(immediateOrderPolicy);
            shopPolicy2.setText(advanceReservationPolicy);

        } else {
            Log.e("Fragment_vendor_profile", "No vendor details found for vendorId: " + vendorId);
            Toast.makeText(getActivity(), "No vendor information found", Toast.LENGTH_SHORT).show();
        }
    }
}
