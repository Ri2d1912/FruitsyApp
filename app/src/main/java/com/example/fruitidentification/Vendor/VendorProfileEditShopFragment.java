package com.example.fruitidentification.Vendor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.dbShopLocationViewModel;
import com.example.fruitidentification.ViewModel.shopInfoViewModel;
import com.example.fruitidentification.ViewModel.shopLocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VendorProfileEditShopFragment extends Fragment implements OnMapReadyCallback {
    private DBHelper myDB;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private EditText editShopName, editStreet, editBarangay, editCity,
            editProvince, editPostal, editMobileNo, editTelephoneNo,
            editShopEmail, editStoreHrs, editDesc;
    private Spinner spinnerOrderPolicy, spinnerReservePolicy;
    ShapeableImageView imgShopProfilePic;
    AppCompatButton btnSave, btnCancel;
    FloatingActionButton imgVendorCamera;
    private long vendorId; // Variable to hold the vendorId

    public VendorProfileEditShopFragment() {
        // Required empty public constructor
    }

    public static VendorProfileEditShopFragment newInstance(String param1, String param2) {
        VendorProfileEditShopFragment fragment = new VendorProfileEditShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_profile_edit_shop, container, false);

        // Initialize DBHelper
        myDB = new DBHelper(getActivity());
        // Retrieve the vendorId from arguments passed to the fragment
        if (getArguments() != null) {
            vendorId = getArguments().getLong("vendorId", -1); // Default to -1 if vendorId is not found
        }
        // Initialize EditTexts
        editShopName = view.findViewById(R.id.editShopName);
        editStreet = view.findViewById(R.id.editStreet);
        editBarangay = view.findViewById(R.id.editBarangay);
        editCity = view.findViewById(R.id.editCity);
        editProvince = view.findViewById(R.id.editProvince);
        editPostal = view.findViewById(R.id.editPostal);
        editMobileNo = view.findViewById(R.id.editMobileNo);
        editTelephoneNo = view.findViewById(R.id.editTelephoneNo);
        editShopEmail = view.findViewById(R.id.editShopEmail);
        editStoreHrs = view.findViewById(R.id.editStoreHrs);
        editDesc = view.findViewById(R.id.editDesc);
        imgShopProfilePic = view.findViewById(R.id.imgShopProfilePic);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        imgVendorCamera = view.findViewById(R.id.imgVendorCamera);

        // Initialize Spinners
        spinnerOrderPolicy = view.findViewById(R.id.spinnerOrderPolicy);
        spinnerReservePolicy = view.findViewById(R.id.spinnerReservePolicy);

        if (vendorId != -1) {
            loadShopProfile(vendorId);
        }
        // Initialize the SupportMapFragment and set the map async callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    // -------------------------------- Map Function --------------------------

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Optionally, enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission and enable my location feature
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Display pins from the database
        displayPinsFromDatabase();

        // Set a click listener to capture pinned location
        mMap.setOnMapClickListener(this::onMapClick);
    }

    private void displayPinsFromDatabase() {
        // Retrieve shop ID based on vendor ID
        long shopId = myDB.getShopIdByVendorId(vendorId);
        Log.d("MapDebug", "Retrieved Shop ID: " + shopId);

        // Call getShopLocation from the myDB instance
        List<dbShopLocationViewModel> shopLocations = myDB.getShopLocation(shopId);
        if (shopLocations == null || shopLocations.isEmpty()) {
            Log.d("MapDebug", "No shop locations found for Shop ID: " + shopId);
            Toast.makeText(getContext(), "No shop locations to display.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the first location to set as the center for the map
        dbShopLocationViewModel firstLocation = shopLocations.get(0);
        LatLng firstLocationLatLng = new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude());

        // Zoom to the first shop location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocationLatLng, 12));

        // Add markers for each location
        for (dbShopLocationViewModel location : shopLocations) {
            Log.d("MapDebug", "Adding location: Lat=" + location.getLatitude() + ", Lng=" + location.getLongitude());
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            String title = location.isPrimary() ? "Primary Location" : "Secondary Location";

            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(location.getAddress())); // Add address as a snippet
        }
    }




    private void onMapClick(LatLng latLng) {
        // Clear any existing markers
        mMap.clear();

        // Add a marker at the tapped location
        mMap.addMarker(new MarkerOptions().position(latLng).title("Pinned Location"));

        // Use Geocoder to get the address from the latitude and longitude
        String address = getAddressFromLatLng(latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), "Address: " + address, Toast.LENGTH_LONG).show();
        String region = getRegionFromLatLng(latLng.latitude, latLng.longitude); // Retrieve region dynamically

        boolean isPrimary = true; // Set this according to your needs

        // Save the location data in the ViewModel
        shopLocationViewModel viewModel = new ViewModelProvider(requireActivity()).get(shopLocationViewModel.class);
        viewModel.setLatitude(latLng.latitude);
        viewModel.setLongitude(latLng.longitude);
        viewModel.setRegion(region);
        viewModel.setAddress(address);
        viewModel.setIsPrimary(isPrimary);
    }


    private String getRegionFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String region = "Unknown Region"; // Default value

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                region = address.getAdminArea(); // Admin area can be the region or province
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return region;
    }
    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        StringBuilder addressBuilder = new StringBuilder();

        try {
            // Get the list of addresses for the given latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            // If geocoder returns a result, get the components
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Retrieve different address components
                String street = address.getThoroughfare(); // Street name
                String barangay = address.getSubLocality(); // Barangay name
                String city = address.getLocality(); // City name
                String province = address.getAdminArea(); // Province name

                // Build the full address
                if (street != null) {
                    addressBuilder.append(street).append(", ");
                }
                if (barangay != null) {
                    addressBuilder.append(barangay).append(", ");
                }
                if (city != null) {
                    addressBuilder.append(city).append(", ");
                }
                if (province != null) {
                    addressBuilder.append(province);
                }
            } else {
                addressBuilder.append("Unknown Address");
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressBuilder.append("Error retrieving address");
        }

        return addressBuilder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getContext(), "Permission denied! Cannot access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadShopProfile(long vendorId) {
        // Query the database to get the shop information using vendorId
        List<shopInfoViewModel> shopDetails = myDB.getFruitShopInfo(vendorId);

        if (shopDetails != null && !shopDetails.isEmpty()) {
            // Assuming we display the first entry in the list
            shopInfoViewModel shopInfo = shopDetails.get(0);

            // Retrieve and set the shop information
            String fruitShopName = shopInfo.getShopName();
            String shopDesc = shopInfo.getShopDesc();
            String shopEmail = shopInfo.getEmail();
            String shopStreet = shopInfo.getShopStreet();
            String shopBarangay = shopInfo.getShopBarangay();
            String shopCity = shopInfo.getShopCity();
            String shopProvince = shopInfo.getShopProvince();
            String shopPostal = shopInfo.getShopPostal();
            String mobileNumber = shopInfo.getMobileNumber();
            String telephoneNumber = shopInfo.getTelephoneNumber();
            String shopOpeningHrs = shopInfo.getOpeningHrs();
            String immediateOrderPolicy = shopInfo.getImmediateOrderPolicy();
            String advanceReservationPolicy = shopInfo.getAdvanceReservationPolicy();
            byte[] shopProfile = shopInfo.getShopProfile();

            // Set the image for shop profile if available
            if (shopProfile != null) {
                Bitmap bitmapShopProfile = BitmapFactory.decodeByteArray(shopProfile, 0, shopProfile.length);
                imgShopProfilePic.setImageBitmap(bitmapShopProfile);
            }

            // Set the text fields with retrieved data
            editShopName.setText(fruitShopName);
            editStreet.setText(shopStreet);
            editBarangay.setText(shopBarangay);
            editCity.setText(shopCity);
            editProvince.setText(shopProvince);
            editPostal.setText(shopPostal);
            editMobileNo.setText(mobileNumber);
            editTelephoneNo.setText(telephoneNumber);
            editShopEmail.setText(shopEmail);
            editStoreHrs.setText(shopOpeningHrs);
            editDesc.setText(shopDesc);

            ArrayAdapter<CharSequence> orderPolicyAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.orderandreserve_options, android.R.layout.simple_spinner_item);
            orderPolicyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerOrderPolicy.setAdapter(orderPolicyAdapter);
            if (immediateOrderPolicy != null) {
                int orderPolicyPosition = orderPolicyAdapter.getPosition(immediateOrderPolicy);
                spinnerOrderPolicy.setSelection(orderPolicyPosition);
            }

            ArrayAdapter<CharSequence> reservePolicyAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.orderandreserve_options, android.R.layout.simple_spinner_item);
            reservePolicyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerReservePolicy.setAdapter(reservePolicyAdapter);
            if (advanceReservationPolicy != null) {
                int reservePolicyPosition = reservePolicyAdapter.getPosition(advanceReservationPolicy);
                spinnerReservePolicy.setSelection(reservePolicyPosition);
            }

        } else {
            Log.e("Fragment_vendor_profile", "No vendor details found for vendorId: " + vendorId);
            Toast.makeText(getActivity(), "No shop information found", Toast.LENGTH_SHORT).show();
        }
    }
}
