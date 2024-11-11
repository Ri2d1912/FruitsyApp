package com.example.fruitidentification.RegistrationFragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.example.fruitidentification.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegistrationFragment2 extends Fragment {

    private FloatingActionButton imgCamera;
    private ImageView imageViewUser;
    private Uri imageUri = null;

    // Register the activity result callback to handle the result for the gallery picker
    private final ActivityResultCallback<Uri> imagePickerResultCallback = uri -> {
        if (uri != null) {
            imageUri = uri; // Get the image URI from the result
            imageViewUser.setImageURI(imageUri); // Set the selected image to ImageView
        }
    };

    // Register the activity result launcher for picking an image from the gallery
    private final androidx.activity.result.ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), imagePickerResultCallback);

    // Register the activity result launcher for the camera
    private final androidx.activity.result.ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
                if (isSuccess && imageUri != null) {
                    // If the photo was successfully taken, set the image
                    imageViewUser.setImageURI(imageUri);
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

        // Image Picker to select the profile picture
        imgCamera.setOnClickListener(v -> {
            // Show a dialog to choose between camera or gallery
            showImageSourceDialog();
        });

        return view;
    }

    // Show dialog with options for camera or gallery
    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source")
                .setItems(new String[]{"Take Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Launch the camera to take a photo
                            uploadPhotoWithCamera();
                        } else {
                            // Launch the gallery to select an image
                            launchGallery();
                        }
                    }
                });
        builder.create().show();
    }

    // Launch the camera using ImagePicker library
    private void uploadPhotoWithCamera() {
        ImagePicker.with(getActivity())
                .cameraOnly()  // Specify to use the camera only
                .crop()        // Crop image (Optional)
                .compress(1024)  // Compress image (Optional)
                .maxResultSize(1080, 1080)  // Max image resolution (Optional)
                .start();  // Start the camera intent
    }

    // Launch the gallery
    private void launchGallery() {
        imagePickerLauncher.launch("image/*");  // We specify we want to pick an image
    }

    // Handle the result from ImagePicker (Camera or Gallery)
    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            // Extract the URI from the returned data. The URI points to the data (e.g., an image) that the user selected
            Uri uri = data.getData();
            // Set the URI of the imageViewUser to display the image that the user selected
            imageViewUser.setImageURI(uri);
            imageViewUser.setTag(uri); // Store the URI for later use
        }
    }

    // Convert the selected image URI to byte array (if needed for saving/uploading)
    public byte[] getProfileImageByteArray() {
        if (imageUri == null) {
            return null;  // No image selected
        }

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
