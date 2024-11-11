package com.example.fruitidentification.RegistrationFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.example.fruitidentification.R;
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

    // ActivityResultLauncher for gallery selection
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;  // Store the image URI
                    imageViewUser.setImageURI(imageUri);  // Set selected image in ImageView
                    imageViewUser.setTag(imageUri);
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

        // Initialize views
        imgCamera = view.findViewById(R.id.imgCamera);
        imageViewUser = view.findViewById(R.id.imageViewUser);
        editBday = view.findViewById(R.id.editBday);  // Initialize the editBday view

        // Set onClick listener for FloatingActionButton to choose image source
        imgCamera.setOnClickListener(v -> showImageSourceDialog());
        editBday.setOnClickListener(v -> showDatePickerDialog());

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

}
