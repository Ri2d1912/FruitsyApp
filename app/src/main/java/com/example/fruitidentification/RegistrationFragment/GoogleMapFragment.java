package com.example.fruitidentification.RegistrationFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper myDB;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);

        // Initialize the SupportMapFragment and set the map async callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set a default location and zoom level
        LatLng defaultLocation = new LatLng(-34, 151);  // Example: Sydney, Australia
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        // Add a marker at the default location
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Marker in Sydney"));

        // Optionally, enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission and enable my location feature
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set a click listener to capture pinned location
        mMap.setOnMapClickListener(this::onMapClick);
    }

    private void onMapClick(LatLng latLng) {
        // Clear any existing markers
        mMap.clear();

        // Add a marker at the tapped location
        mMap.addMarker(new MarkerOptions().position(latLng).title("Pinned Location"));

        // Example details for the location; replace with dynamic data as needed
        int shopId = 1;                  // Example shop ID
        String region = "Example Region"; // Example region
        String address = "Example Address"; // Example address
        boolean isPrimary = true;         // Example primary flag

        // Save the location data in the database
        saveLocationToDatabase(shopId, latLng.latitude, latLng.longitude, region, address, isPrimary);
    }

    private void saveLocationToDatabase(int shopId, double latitude, double longitude, String region, String address, boolean isPrimary) {
        myDB.insertOrUpdateShopLocation(shopId, latitude, longitude, region, address, isPrimary);
        Toast.makeText(getContext(), "Location saved to database!", Toast.LENGTH_SHORT).show();
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
}
