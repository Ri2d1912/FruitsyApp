package com.example.fruitidentification.RegistrationFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.vendorRegFragVM;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;

public class VendorRegistrationFragment4 extends Fragment {
    private vendorRegFragVM viewModel;

    // Declare buttons for attaching image files
    AppCompatButton btnAttachFileImage, btnAttachFileDti, btnAttachFileBir;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private AppCompatButton currentButton;
    private Uri shopHeaderUri = null;
    private Uri dtiUri = null;
    private Uri birUri = null;

    // ActivityResultLauncher for gallery selection (image)
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    // Set selected image in the current button
                    currentButton.setText(getFileNameFromUri(uri));  // Set file name on the button
                    currentButton.setTag(uri);  // Store the URI as a tag on the button

                    // Update the appropriate URI based on the current button
                    if (currentButton == btnAttachFileImage) {
                        shopHeaderUri = uri;
                        viewModel.setShopHeaderProfileImageUri(shopHeaderUri);
                    } else if (currentButton == btnAttachFileDti) {
                        dtiUri = uri;
                        viewModel.setDtiFileUri(dtiUri);
                    } else if (currentButton == btnAttachFileBir) {
                        birUri = uri;
                        viewModel.setBirFileUri(birUri);
                    }

                    Toast.makeText(getContext(), "Image selected from gallery", Toast.LENGTH_SHORT).show();
                }
            });

    // ActivityResultLauncher for camera capture
    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), isSuccess -> {
                // Get the URI for the image captured by the camera from the button's tag
                Uri imageUri = (Uri) currentButton.getTag();  // Retrieve the URI stored in the tag

                if (isSuccess && imageUri != null) {
                    // Set the captured image file name on the button
                    currentButton.setText(getFileNameFromUri(imageUri));  // Set file name on the button
                    currentButton.setTag(imageUri);  // Store the URI as a tag on the button

                    // Update the appropriate URI based on the current button
                    if (currentButton == btnAttachFileImage) {
                        shopHeaderUri = imageUri;
                        viewModel.setShopHeaderProfileImageUri(shopHeaderUri);
                        Toast.makeText(getContext(), "Image captured for Shop Header", Toast.LENGTH_SHORT).show();
                    } else if (currentButton == btnAttachFileDti) {
                        dtiUri = imageUri;
                        viewModel.setDtiFileUri(dtiUri);
                        Toast.makeText(getContext(), "Image captured for DTI File", Toast.LENGTH_SHORT).show();
                    } else if (currentButton == btnAttachFileBir) {
                        birUri = imageUri;
                        viewModel.setBirFileUri(birUri);
                        Toast.makeText(getContext(), "Image captured for BIR File", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Image capture failed", Toast.LENGTH_SHORT).show();
                }
            });


    public VendorRegistrationFragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendor_registration4, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(vendorRegFragVM.class);

        // Initialize buttons
        btnAttachFileImage = rootView.findViewById(R.id.buttonAttachFileImage);
        btnAttachFileDti = rootView.findViewById(R.id.buttonAttachFileDti);
        btnAttachFileBir = rootView.findViewById(R.id.buttonAttachFileBir);

        btnAttachFileImage.setOnClickListener(v -> onButtonClicked(btnAttachFileImage));
        btnAttachFileDti.setOnClickListener(v -> onButtonClicked(btnAttachFileDti));
        btnAttachFileBir.setOnClickListener(v -> onButtonClicked(btnAttachFileBir));

        liveData();
        return rootView;
    }

    private void onButtonClicked(AppCompatButton button) {
        currentButton = button;  // Store the clicked button
        showImageSourceDialog();
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

    private void uploadPhotoWithCamera() {
        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Create a temporary URI based on the current button
            Uri tempUri = getTempImageUri();  // Get a temporary URI for storing the image

            if (tempUri != null) {
                // Set the URI to the appropriate one based on the button clicked
                if (currentButton == btnAttachFileImage) {
                    shopHeaderUri = tempUri;
                } else if (currentButton == btnAttachFileDti) {
                    dtiUri = tempUri;
                } else if (currentButton == btnAttachFileBir) {
                    birUri = tempUri;
                }

                // Set the URI as a tag on the current button before launching the camera
                currentButton.setTag(tempUri);

                // Launch the camera with the appropriate URI
                cameraLauncher.launch(tempUri);
            } else {
                Toast.makeText(getContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
            }
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

    // Helper method to extract the file name from the URI
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };

        try (Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName != null ? fileName : "File Selected";
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening the camera
                uploadPhotoWithCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void liveData() {
        // Observe and update UI for each image URI
        viewModel.getShopHeaderProfileImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                btnAttachFileImage.setText(getFileNameFromUri(uri));
                btnAttachFileImage.setTag(uri);
            }
        });

        viewModel.getDtiFileUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                btnAttachFileDti.setText(getFileNameFromUri(uri));
                btnAttachFileDti.setTag(uri);
            }
        });

        viewModel.getBirFileUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                btnAttachFileBir.setText(getFileNameFromUri(uri));
                btnAttachFileBir.setTag(uri);
            }
        });
    }

}
