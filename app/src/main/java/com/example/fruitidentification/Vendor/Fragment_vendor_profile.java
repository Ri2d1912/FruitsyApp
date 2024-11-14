package com.example.fruitidentification.Vendor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.shopInfoViewModel;
import com.example.fruitidentification.ViewModel.vendorInfoViewModel;

import java.util.List;

public class Fragment_vendor_profile extends Fragment {
    private DBHelper myDB;
    private TextView profile_fruitshop_name, description1, shopDescAddress, shopstorehoursdescrip, shopContactContent, shopContact2Content,
            shopEmailInfo, shopPolicyOrder, shopPolicy2, shopMobile, shopTelephone,
            shopOpeningHours, shopUsername;

    //Vendor side
    private TextView vendorName, vendorDescAddress, vendorContactContent;

    ImageView profile_pfp, profile_header_image;
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

        // Initialize the TextViews
        //------------------------------- Shop Info ----------------------
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
        profile_header_image = view.findViewById(R.id.profile_header_image);
        profile_pfp = view.findViewById(R.id.profile_pfp);
        profile_fruitshop_name = view.findViewById(R.id.profile_fruitshop_name);
        shopUsername = view.findViewById(R.id.shopUserName);

        //------------------------------- Vendor Info ----------------------
        vendorName = view.findViewById(R.id.vendorName);
        vendorDescAddress = view.findViewById(R.id.vendorDescAddress);
        vendorContactContent = view.findViewById(R.id.vendorContactContent);

        // Load the vendor profile if vendorId is valid
        if (vendorId != -1) {
            loadShopProfile(vendorId);
            loadVendorProfile(vendorId);
        }
        return view;
    }

    // Method to load shop profile details
    private void loadShopProfile(long vendorId) {
        // Query the database to get the vendor information using vendorId
        List<shopInfoViewModel> shopDetails = myDB.getFruitShopInfo(vendorId);

        if (shopDetails != null && !shopDetails.isEmpty()) {
            // Assuming you want to display the first entry in the list
            shopInfoViewModel shopInfo = shopDetails.get(0); // Get the first vendor

            // Retrieve address information from vendor object
            String Fruitshopname = shopInfo.getShopName();
            String shopDesc = shopInfo.getShopDesc();
            String shopEmail = shopInfo.getEmail();
            String shopStreet = shopInfo.getShopStreet();
            String shopBarangay = shopInfo.getShopBarangay();
            String shopCity = shopInfo.getShopCity();
            String shopProvince = shopInfo.getShopProvince();
            String mobileNumber = shopInfo.getMobileNumber();
            String telephoneNumber = shopInfo.getTelephoneNumber();
            String immediateOrderPolicy = shopInfo.getImmediateOrderPolicy();
            String advanceReservationPolicy = shopInfo.getAdvanceReservationPolicy();
            String shopOpeningHrs = shopInfo.getOpeningHrs();
            byte[] shopHeader = shopInfo.getShopHeaderImg();
            byte[] shopProfile = shopInfo.getShopProfile();

            if (shopProfile != null) {
                Bitmap bitmapShopProfile = BitmapFactory.decodeByteArray(shopProfile, 0, shopProfile.length);
                profile_pfp.setImageBitmap(bitmapShopProfile);
            }

            if (shopHeader != null) {
                Bitmap bitmapHeader = BitmapFactory.decodeByteArray(shopHeader, 0, shopHeader.length);
                profile_header_image.setImageBitmap(bitmapHeader);
            }
            profile_fruitshop_name.setText(Fruitshopname);
            description1.setText(shopDesc);

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

    // Method to load vendor profile details
    private void loadVendorProfile(long vendorId) {
        // Query the database to get the vendor information using vendorId
        List<vendorInfoViewModel> vendorDetails = myDB.getVendorInfo(vendorId);
        if (vendorDetails != null && !vendorDetails.isEmpty()) {
            // Assuming you want to display the first entry in the list

            vendorInfoViewModel vendor = vendorDetails.get(0); // Get the first vendor
            String vendorUsername = vendor.getUserName();
            String vendorFname = vendor.getFirstName();
            String vendorMname = vendor.getMiddleName();
            String vendorLname = vendor.getLastName();
            String vendorExname = vendor.getExtensionName();
            String vendorStreet = vendor.getStreetAddress();
            String vendorBarangay = vendor.getBarangay();
            String vendorCity = vendor.getCity();
            String vendorProvince = vendor.getProvince();
            String vendorMobile= vendor.getMobileNumber();

            shopUsername.setText("@"+vendorUsername);

            String vendorFullName =  vendorFname + ", " + vendorExname + ", " + vendorMname + ", " + vendorLname;;
            vendorName.setText(vendorFullName);
            String vendorFullAddress =  vendorStreet + ", " + vendorBarangay + ", " + vendorCity + ", " + vendorProvince;;
            vendorDescAddress.setText(vendorFullAddress);
            vendorContactContent.setText(vendorMobile);

        }
    }
}
