package com.example.fruitidentification.RegistrationFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.Vendor.VendorMainActivity;
import com.example.fruitidentification.ViewModel.dbShopLocationViewModel;
import com.example.fruitidentification.ViewModel.shopLocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private View fillBackNavigation;
    private GoogleMap mMap;
    private DBHelper myDB;
    long vendorId;
    shopLocationViewModel viewModel;
    // Request code for location permission
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public GoogleMapFragment() {
        // Required empty public constructor
    }

    public static GoogleMapFragment newInstance(String param1, String param2) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);

        // Initialize DBHelper
        myDB = new DBHelper(requireActivity());

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(shopLocationViewModel.class);

        // Assign vendorId only after initializing the ViewModel
        vendorId = viewModel.getShopId() != null ? viewModel.getShopId() : -1;

        fillBackNavigation = view.findViewById(R.id.fillBackNavigation);

        // Initialize the SupportMapFragment and set the map async callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fillBackNavigation.setOnClickListener(v -> {
            if (getActivity() instanceof VendorMainActivity) {
                VendorMainActivity vendorMainActivity = (VendorMainActivity) getActivity();
                if (vendorMainActivity != null) {
                    // Show the BottomNavigationView
                    vendorMainActivity.showBottomNavigation();
                }
            }

            // Navigate back
            getActivity().onBackPressed();
        });

        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set the default location to Balanga City, Bataan
        LatLng defaultLocation = new LatLng(14.6696, 120.5415); // Coordinates for Balanga City, Bataan
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12)); // Zoom level adjusted for the area

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission and enable my location feature
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set a click listener to capture pinned location
        mMap.setOnMapClickListener(this::onMapClick);
        // Display pins from the database
        if (vendorId != -1) {
            displayPinsFromDatabase();
        }
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
        Toast.makeText(requireContext(), "Address: " + address, Toast.LENGTH_LONG).show();
        String region = getRegionFromLatLng(latLng.latitude, latLng.longitude); // Retrieve region dynamically

        boolean isPrimary = true; // Set this according to your needs

        // Save the location data in the ViewModel
        viewModel.setLatitude(latLng.latitude);
        viewModel.setLongitude(latLng.longitude);
        viewModel.setRegion(region);
        viewModel.setAddress(address);
        viewModel.setIsPrimary(isPrimary);
    }

    private String getRegionFromLatLng(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
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
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
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
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied! Cannot access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
