package com.example.fruitidentification.Vendor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.fruitidentification.RegistrationFragment.GoogleMapFragment;
import com.example.fruitidentification.VendorCreateAcc;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private Uri shopProfileImageUri = null;


    String fruitShopName, shopDesc, shopEmail, shopStreet, shopBarangay, shopCity, shopProvince,
            shopPostal, mobileNumber, telephoneNumber, shopOpeningHrs, immediateOrderPolicy, advanceReservationPolicy;
    byte[] shopProfile;
    private Spinner spinnerOrderPolicy, spinnerReservePolicy;
    ShapeableImageView imgShopProfilePic;
    AppCompatButton btnSave, btnCancel;
    FloatingActionButton imgVendorCamera;
    private long vendorId; // Variable to hold the vendorId
    View overlayView;
    shopLocationViewModel shopLocVIewModel;


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
        shopLocVIewModel = new ViewModelProvider(requireActivity()).get(shopLocationViewModel.class);

        // Initialize DBHelper
        myDB = new DBHelper(getActivity());
        // Retrieve the vendorId from arguments passed to the fragment
        if (getArguments() != null) {
            vendorId = getArguments().getLong("vendorId", -1); // Default to -1 if vendorId is not found
        }
        // Initialize EditTexts
        editShopName = view.findViewById(R.id.editShopName);
        editDesc = view.findViewById(R.id.editDesc);
        editShopEmail = view.findViewById(R.id.editShopEmail);
        editStreet = view.findViewById(R.id.editStreet);
        editBarangay = view.findViewById(R.id.editBarangay);
        editCity = view.findViewById(R.id.editCity);
        editProvince = view.findViewById(R.id.editProvince);
        editPostal = view.findViewById(R.id.editPostal);
        editMobileNo = view.findViewById(R.id.editMobileNo);
        editTelephoneNo = view.findViewById(R.id.editTelephoneNo);
        editStoreHrs = view.findViewById(R.id.editStoreHrs);
        imgShopProfilePic = view.findViewById(R.id.imgShopProfilePic);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        imgVendorCamera = view.findViewById(R.id.imgVendorCamera);
        // Initialize Spinners
        spinnerOrderPolicy = view.findViewById(R.id.spinnerOrderPolicy);
        spinnerReservePolicy = view.findViewById(R.id.spinnerReservePolicy);
        overlayView = view.findViewById(R.id.overlay_view);

        if (vendorId != -1) {
            loadShopProfile(vendorId);
        }
        // Initialize the SupportMapFragment and set the map async callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        imgVendorCamera.setOnClickListener(v -> showImageSourceDialog());

        btnSave.setOnClickListener(v -> saveShopInfo());

        overlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the fragment container exists and perform the fragment transaction
                if (getActivity() != null) {
                    // Access VendorMainActivity and hide BottomNavigationView
                    VendorMainActivity vendorMainActivity = (VendorMainActivity) getActivity();
                    if (vendorMainActivity != null) {
                        vendorMainActivity.hideBottomNavigation();
                    }

                    // Perform the fragment transaction
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    GoogleMapFragment googleMapFragment = new GoogleMapFragment();

                    // Replace the current fragment with GoogleMapFragment
                    fragmentTransaction.replace(R.id.frameContainer, googleMapFragment);
                    fragmentTransaction.addToBackStack(null); // Optional: Add this to back stack
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

    private void saveShopInfo() {
        // Capture the updated values from the input fields
        fruitShopName = editShopName.getText().toString().trim();
        shopDesc = editDesc.getText().toString().trim();
        shopEmail = editShopEmail.getText().toString().trim();
        shopStreet = editStreet.getText().toString().trim();
        shopBarangay = editBarangay.getText().toString().trim();
        shopCity = editCity.getText().toString().trim();
        shopProvince = editProvince.getText().toString().trim();
        shopPostal = editPostal.getText().toString().trim();
        mobileNumber = editMobileNo.getText().toString().trim();
        telephoneNumber = editTelephoneNo.getText().toString().trim();
        shopOpeningHrs = editStoreHrs.getText().toString().trim();
        immediateOrderPolicy = spinnerOrderPolicy.getSelectedItem().toString();
        advanceReservationPolicy = spinnerReservePolicy.getSelectedItem().toString();
        // Check if an image is set for the profile picture and convert it to byte array
        if (imgShopProfilePic.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imgShopProfilePic.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            shopProfile = byteArrayOutputStream.toByteArray();
        } else {
            shopProfile = null; // If no image is set
        }

        // Validate if required fields are not empty
        if (fruitShopName.isEmpty()) {
            Toast.makeText(getActivity(), "Please Input Shop name", Toast.LENGTH_LONG).show();
        } else {
            // Call your database method to update the vendor info
            boolean checkUpdate = myDB.updateFruitShopInfo(getActivity(), vendorId, fruitShopName, shopStreet, shopBarangay,
                    shopCity, shopProvince, shopPostal, mobileNumber, telephoneNumber, shopEmail, shopDesc,
                    shopOpeningHrs, immediateOrderPolicy, advanceReservationPolicy, shopProfile);

            double latitude = shopLocVIewModel.getLatitude().getValue();
            double longitude = shopLocVIewModel.getLongitude().getValue();
            String region = shopLocVIewModel.getRegion().getValue();
            String pinAddress = shopLocVIewModel.getAddress().getValue();
            boolean isPrimary = shopLocVIewModel.getIsPrimary().getValue();
            long shopId = myDB.getShopIdByVendorId(vendorId);

            boolean checkpUpdateLoc = myDB.insertOrUpdateShopLocation(shopId, latitude, longitude, region, pinAddress, isPrimary);


            if (checkUpdate && checkpUpdateLoc) {
                Toast.makeText(getActivity(), "Vendor information updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to update vendor information", Toast.LENGTH_SHORT).show();
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
            fruitShopName = shopInfo.getShopName();
            shopDesc = shopInfo.getShopDesc();
            shopEmail = shopInfo.getEmail();
            shopStreet = shopInfo.getShopStreet();
            shopBarangay = shopInfo.getShopBarangay();
            shopCity = shopInfo.getShopCity();
            shopProvince = shopInfo.getShopProvince();
            shopPostal = shopInfo.getShopPostal();
            mobileNumber = shopInfo.getMobileNumber();
            telephoneNumber = shopInfo.getTelephoneNumber();
            shopOpeningHrs = shopInfo.getOpeningHrs();
            immediateOrderPolicy = shopInfo.getImmediateOrderPolicy();
            advanceReservationPolicy = shopInfo.getAdvanceReservationPolicy();
            shopProfile = shopInfo.getShopProfile();

            // Set the image for shop profile if available
            if (shopProfile != null) {
                Bitmap bitmapShopProfile = BitmapFactory.decodeByteArray(shopProfile, 0, shopProfile.length);
                imgShopProfilePic.setImageBitmap(bitmapShopProfile);
            }
            editShopName.setText(fruitShopName);
            editDesc.setText(shopDesc);
            editShopEmail.setText(shopEmail);
            editStreet.setText(shopStreet);
            editBarangay.setText(shopBarangay);
            editCity.setText(shopCity);
            editProvince.setText(shopProvince);
            editPostal.setText(shopPostal);
            editMobileNo.setText(mobileNumber);
            editTelephoneNo.setText(telephoneNumber);
            editStoreHrs.setText(shopOpeningHrs);


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

    // -------------------------------- Image Function --------------------------

    // ActivityResultLauncher for gallery selection
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    shopProfileImageUri = uri;  // Store the image URI
                    imgShopProfilePic.setImageURI(shopProfileImageUri);  // Set selected image in ImageView
                    imgShopProfilePic.setTag(shopProfileImageUri);
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
