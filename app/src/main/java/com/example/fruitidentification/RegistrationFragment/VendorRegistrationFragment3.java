package com.example.fruitidentification.RegistrationFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.shopLocationViewModel;
import com.example.fruitidentification.ViewModel.vendorRegFragVM;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VendorRegistrationFragment3 extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private DBHelper myDB;

    private FloatingActionButton imgVendorCamera;
    private ImageView imgShopProfilePic;
    private Uri shopProfileImageUri = null;
    private EditText editShopName, editShopStreet, editShopBarangay, editShopCity, editShopProvince, editShopPostal, editShopNo, editTelephoneNo, editShopEmail, editStoreHrs, editDesc;
    private Spinner spinnerOrderPolicy, spinnerReservePolicy;

    private vendorRegFragVM viewModel;

    public VendorRegistrationFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_registration3, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(vendorRegFragVM.class);
        myDB = new DBHelper(getContext());

        // Initialize views
        editShopName = view.findViewById(R.id.editShopName);
        editShopStreet = view.findViewById(R.id.editStreet);
        editShopBarangay = view.findViewById(R.id.editBarangay);
        editShopCity = view.findViewById(R.id.editCity);
        editShopProvince = view.findViewById(R.id.editProvince);
        editShopPostal = view.findViewById(R.id.editPostal);
        editShopNo = view.findViewById(R.id.editMobileNo);
        editTelephoneNo = view.findViewById(R.id.editTelephoneNo);
        editShopEmail = view.findViewById(R.id.editShopEmail);
        editStoreHrs = view.findViewById(R.id.editStoreHrs);
        editDesc = view.findViewById(R.id.editDesc);

        // Initialize Spinners
        spinnerOrderPolicy = view.findViewById(R.id.spinnerOrderPolicy);
        spinnerReservePolicy = view.findViewById(R.id.spinnerReservePolicy);

        // Initialize ImageView and FloatingActionButton
        imgVendorCamera = view.findViewById(R.id.imgVendorCamera);
        imgShopProfilePic = view.findViewById(R.id.imgShopProfilePic);

        // on click listener for camera
        imgVendorCamera.setOnClickListener(v -> showImageSourceDialog());

        // Initialize the SupportMapFragment and set the map async callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        liveData();
        textWatcher();
        return view;
    }

    // -------------------------------- Map Function --------------------------

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set the default location to Balanga City, Bataan
        LatLng defaultLocation = new LatLng(14.6696, 120.5415);  // Coordinates for Balanga City, Bataan
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));  // Zoom level adjusted for the area

        // Add a marker at the default location
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Balanga City"));

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

        // Use Geocoder to get the address from the latitude and longitude
        String address = getAddressFromLatLng(latLng.latitude, latLng.longitude);
        Toast.makeText(getContext(), "Address: " + address, Toast.LENGTH_LONG).show();
        String region = getRegionFromLatLng(latLng.latitude, latLng.longitude); // Retrieve region dynamically

        int shopId = 1; // You can set this dynamically
        boolean isPrimary = true; // Set this according to your needs

        // Save the location data in the ViewModel
        shopLocationViewModel viewModel = new ViewModelProvider(requireActivity()).get(shopLocationViewModel.class);
        viewModel.setShopId(shopId);
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

    // -------------------------------- Image Function --------------------------

    // ActivityResultLauncher for gallery selection
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    shopProfileImageUri = uri;  // Store the image URI
                    imgShopProfilePic.setImageURI(shopProfileImageUri);  // Set selected image in ImageView
                    imgShopProfilePic.setTag(shopProfileImageUri);
                    viewModel.setshopProfileImageUri(shopProfileImageUri);  // Save the URI in ViewModel
                } else {
                    Toast.makeText(getContext(), "Image selection canceled", Toast.LENGTH_SHORT).show();
                }
            });

    // ActivityResultLauncher for camera capture
    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
                if (isSuccess && shopProfileImageUri != null) {
                    imgShopProfilePic.setImageURI(shopProfileImageUri);  // Set captured image in ImageView
                    imgShopProfilePic.setTag(shopProfileImageUri);  // Store the URI as tag
                    viewModel.setshopProfileImageUri(shopProfileImageUri);  // Save the URI in ViewModel
                } else {
                    Toast.makeText(getContext(), "Image capture failed", Toast.LENGTH_SHORT).show();
                }
            });

    // ActivityResultLauncher to request camera permission
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, proceed with launching the camera
                    launchCamera();
                } else {
                    // Permission denied, notify the user
                    Toast.makeText(getContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                }
            });


    // Show dialog to choose between camera or gallery
    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Image Source")
                .setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        // Check for camera permission before launching the camera
                        checkCameraPermission();
                    } else {
                        // Launch the gallery to select an image
                        launchGallery();
                    }
                });
        builder.create().show();
    }

    // Launch the gallery
    private void launchGallery() {
        imagePickerLauncher.launch("image/*");  // Launch image picker for selecting image from gallery
    }

    // Check for camera permission and request it if not granted
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed with launching the camera
            launchCamera();
        } else {
            // Request camera permission
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    // Launch the camera to take a photo
    private void launchCamera() {
        shopProfileImageUri = getTempImageUri();  // Get a temporary URI for storing the image
        if (shopProfileImageUri != null) {
            cameraLauncher.launch(shopProfileImageUri);
        } else {
            Toast.makeText(getContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
        }
    }

    // Generate a temporary URI for saving a photo captured with the camera
    private Uri getTempImageUri() {
        try {
            // Create a temporary image file in the cache directory
            File tempFile = File.createTempFile("temp_image", ".jpg", requireContext().getCacheDir());

            // Return the URI using FileProvider
            return FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void liveData() {

        viewModel.getshopProfileImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                imgShopProfilePic.setImageURI(uri);
                imgShopProfilePic.setTag(uri);
            }
        });

        // Observe and update UI for Shop fields
        viewModel.getShopName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopName.getText().toString())) {
                editShopName.setText(input);
            }
        });

        viewModel.getShopStreet().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopStreet.getText().toString())) {
                editShopStreet.setText(input);
            }
        });

        viewModel.getShopBarangay().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopBarangay.getText().toString())) {
                editShopBarangay.setText(input);
            }
        });

        viewModel.getShopCity().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopCity.getText().toString())) {
                editShopCity.setText(input);
            }
        });

        viewModel.getShopProvince().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopProvince.getText().toString())) {
                editShopProvince.setText(input);
            }
        });

        viewModel.getShopPostal().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopPostal.getText().toString())) {
                editShopPostal.setText(input);
            }
        });

        viewModel.getshopNo().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopNo.getText().toString())) {
                editShopNo.setText(input);
            }
        });

        viewModel.getTelNo().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editTelephoneNo.getText().toString())) {
                editTelephoneNo.setText(input);
            }
        });

        // Handle additional fields
        viewModel.getShopEmail().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editShopEmail.getText().toString())) {
                editShopEmail.setText(input);
            }
        });

        viewModel.getStoreHrs().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editStoreHrs.getText().toString())) {
                editStoreHrs.setText(input);
            }
        });

        viewModel.getEditDesc().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editDesc.getText().toString())) {
                editDesc.setText(input);
            }
        });

        // Observe the order policy change from ViewModel
        viewModel.getOrderPolicy().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(spinnerOrderPolicy.getSelectedItem().toString())) {
                int position = getOrderPolicyPosition(input); // Get the position based on order policy string
                spinnerOrderPolicy.setSelection(position); // Set the selected item in the spinner
            }
        });


        // Observe the reserve policy change from ViewModel
        viewModel.getReservePolicy().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(spinnerReservePolicy.getSelectedItem().toString())) {
                int position = getReservePolicyPosition(input); // Get the position based on reserve policy string
                spinnerReservePolicy.setSelection(position); // Set the selected item in the spinner
            }
        });

    }

    private int getOrderPolicyPosition(String orderPolicy) {
        // List of order policies (you can also get this from your resources if needed)
        List<String> orderPolicyList = Arrays.asList(
                "Payment Upon Pickup",
                "Deposit Required",
                "Fullpayment in Advanced",
                "Flexible"
        );
        return orderPolicyList.indexOf(orderPolicy); // Return the position of the selected order policy
    }


    private int getReservePolicyPosition(String reservePolicy) {
        // List of reserve policies (you can also get this from your resources if needed)
        List<String> reservePolicyList = Arrays.asList(
                "Payment Upon Pickup",
                "Deposit Required",
                "Fullpayment in Advanced",
                "Flexible"
        );
        return reservePolicyList.indexOf(reservePolicy); // Return the position of the selected reserve policy
    }

    private void textWatcher() {
        // Set a TextWatcher to save input data when the user types
        editShopName.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the shop name input to ViewModel
                viewModel.setShopName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopStreet.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setShopStreet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopBarangay.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setShopBarangay(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopCity.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setShopCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopProvince.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setShopProvince(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopPostal.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setShopPostal(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopNo.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setshopNo(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editTelephoneNo.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the telephone number input to ViewModel
                viewModel.setTelNo(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editShopEmail.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the telephone number input to ViewModel
                viewModel.setShopEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editStoreHrs.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the store hours input to ViewModel
                viewModel.setStoreHrs(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editDesc.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setEditDesc(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
        spinnerOrderPolicy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPolicy = parentView.getItemAtPosition(position).toString();
                viewModel.setOrderPolicy(selectedPolicy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinnerReservePolicy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPolicy = parentView.getItemAtPosition(position).toString();
                viewModel.setReservePolicy(selectedPolicy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
