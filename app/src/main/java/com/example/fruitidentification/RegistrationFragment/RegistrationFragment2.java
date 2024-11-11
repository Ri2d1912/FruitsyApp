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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationFragment2 extends Fragment {

    private FloatingActionButton imgCamera;
    private ImageView imageViewUser;
    private Uri imageUri = null;
    private TextInputEditText editBday;
    private EditText fnameField, mnameField, lnameField, editExname, streetField, barangayField, cityField, provinceField, postalField, mobileNoField;
    private Spinner genderSpinner;

    private regFrag1VM viewModel;


    // ActivityResultLauncher for gallery selection
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;  // Store the image URI
                    imageViewUser.setImageURI(imageUri);  // Set selected image in ImageView
                    imageViewUser.setTag(imageUri);
                    viewModel.setImageUri(imageUri);  // Save the URI in ViewModel
                } else {
                    Toast.makeText(getContext(), "Image selection canceled", Toast.LENGTH_SHORT).show();
                }
            });

    // ActivityResultLauncher for camera capture
    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
                if (isSuccess && imageUri != null) {
                    imageViewUser.setImageURI(imageUri);  // Set captured image in ImageView
                    imageViewUser.setTag(imageUri);  // Store the URI as tag
                    viewModel.setImageUri(imageUri);  // Save the URI in ViewModel
                } else {
                    Toast.makeText(getContext(), "Image capture failed", Toast.LENGTH_SHORT).show();
                }
            });



    public RegistrationFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration2, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(regFrag1VM.class);

        // Initialize views
        fnameField = view.findViewById(R.id.editFname);
        mnameField = view.findViewById(R.id.editMname);
        lnameField = view.findViewById(R.id.editLname);
        editExname = view.findViewById(R.id.editExname);
        streetField = view.findViewById(R.id.editStreet);
        barangayField = view.findViewById(R.id.editBarangay);
        cityField = view.findViewById(R.id.editCity);
        provinceField = view.findViewById(R.id.editProvince);
        postalField = view.findViewById(R.id.editPostal);
        mobileNoField = view.findViewById(R.id.editMobileNo);
        genderSpinner = view.findViewById(R.id.spinnerGender);
        imgCamera = view.findViewById(R.id.imgCamera);
        imageViewUser = view.findViewById(R.id.imageViewUser);
        editBday = view.findViewById(R.id.editBday);  // Initialize the editBday view
        imgCamera.setOnClickListener(v -> showImageSourceDialog());
        editBday.setOnClickListener(v -> showDatePickerDialog());


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
        imageUri = getTempImageUri();  // Get a temporary URI for storing the image
        if (imageUri != null) {
            cameraLauncher.launch(imageUri);
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

    // Show DatePickerDialog to choose a birthday date
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format and display the selected date in the EditText
                    String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", selectedMonth + 1, selectedDay, selectedYear);
                    editBday.setText(formattedDate);  // Set the selected date to the EditText
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void liveData(){
        // Observe the LiveData from ViewModel to retrieve the saved username

        viewModel.getFName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(fnameField.getText().toString())) {
                fnameField.setText(input);  // Restore the first name when the data changes
            }
        });

        viewModel.getMName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(mnameField.getText().toString())) {
                mnameField.setText(input);  // Restore the middle name when the data changes
            }
        });

        viewModel.getLName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(lnameField.getText().toString())) {
                lnameField.setText(input);  // Restore the last name when the data changes
            }
        });

        viewModel.getExName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editExname.getText().toString())) {
                editExname.setText(input);  // Restore the ex name when the data changes
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                String selectedGender = parentView.getItemAtPosition(position).toString();

                // Update the ViewModel with the selected gender
                viewModel.setGender(selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle case when nothing is selected
            }
        });


        viewModel.getStreet().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(streetField.getText().toString())) {
                streetField.setText(input);  // Restore the street when the data changes
            }
        });

        viewModel.getBarangay().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(barangayField.getText().toString())) {
                barangayField.setText(input);  // Restore the barangay when the data changes
            }
        });

        viewModel.getCity().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(cityField.getText().toString())) {
                cityField.setText(input);  // Restore the city when the data changes
            }
        });
        viewModel.getProvince().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(provinceField.getText().toString())) {
                provinceField.setText(input);  // Restore the province when the data changes
            }
        });

        viewModel.getPostal().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(postalField.getText().toString())) {
                postalField.setText(input);  // Restore the postal code when the data changes
            }
        });

        viewModel.getMobile().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(mobileNoField.getText().toString())) {
                mobileNoField.setText(input);  // Restore the mobile number when the data changes
            }
        });

        viewModel.getGender().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(genderSpinner.getSelectedItem().toString())) {
                // Restore gender selection if needed (spinner update)
                // This may involve setting the correct item on the spinner
            }
        });

        viewModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                imageViewUser.setImageURI(uri);  // Set image on the ImageView
                imageViewUser.setTag(uri);  // Optionally store the URI as a tag
            }
        });
        viewModel.getBday().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editBday.getText().toString())) {
                editBday.setText(input);  // Restore the birthday when the data changes
            }
        });

    }
    private void textWatcher() {

        // Set a TextWatcher to save input data when the user types
        fnameField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the first name input to ViewModel
                viewModel.setFName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        mnameField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the middle name input to ViewModel
                viewModel.setMName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        lnameField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the last name input to ViewModel
                viewModel.setLName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editExname.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the ex name input to ViewModel
                viewModel.setExName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        streetField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the street input to ViewModel
                viewModel.setStreet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        barangayField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the barangay input to ViewModel
                viewModel.setBarangay(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        cityField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the city input to ViewModel
                viewModel.setCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        provinceField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the province input to ViewModel
                viewModel.setProvince(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        postalField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the postal code input to ViewModel
                viewModel.setPostal(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        mobileNoField.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the mobile number input to ViewModel
                viewModel.setMobile(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editBday.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the birthday input to ViewModel
                viewModel.setBday(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
    }


}
