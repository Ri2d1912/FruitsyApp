package com.example.fruitidentification.RegistrationFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.regFrag1VM;
import com.example.fruitidentification.ViewModel.vendorRegFragVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class VendorRegistrationFragment3 extends Fragment {

    private FloatingActionButton imgVendorCamera;
    private ImageView imgShopProfilePic;
    private Uri shopProfileImageUri = null;
    private EditText editShopName, editShopStreet, editShopBarangay, editShopCity, editShopProvince, editShopPostal, editShopNo, editTelephoneNo, editShopEmail, editStoreHrs, editDesc;
    private Spinner spinnerOrderPolicy, spinnerReservePolicy;

    private vendorRegFragVM viewModel;


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



    public VendorRegistrationFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_registration3, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(vendorRegFragVM.class);

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


        // on click listener
        imgVendorCamera.setOnClickListener(v -> showImageSourceDialog());


        liveData();
        textWatcher();
        return view;
    }

    // Show dialog to choose between camera or gallery
    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Image Source")
                .setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        // Launch the camera to take a photo
                        uploadPhotoWithCamera();
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

    // Launch the camera and create a temporary URI for saving the captured image
    private void uploadPhotoWithCamera() {
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

        viewModel.getEditDesc().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editDesc.getText().toString())) {
                editDesc.setText(input);
            }
        });

        viewModel.getOrderPolicy().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(spinnerOrderPolicy.getSelectedItem().toString())) {
                int position = getPositionInSpinner(spinnerOrderPolicy, input);
                if (position >= 0) {
                    spinnerOrderPolicy.setSelection(position);
                }
            }
        });

        viewModel.getReservePolicy().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(spinnerReservePolicy.getSelectedItem().toString())) {
                int position = getPositionInSpinner(spinnerReservePolicy, input);
                if (position >= 0) {
                    spinnerReservePolicy.setSelection(position);
                }
            }
        });
    }
    private int getPositionInSpinner(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equals(value)) {
                return i;
            }
        }
        return -1; // If not found
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