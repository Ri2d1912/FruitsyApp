package com.example.fruitidentification.RegistrationFragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fruitidentification.R;

public class VendorRegistrationFragment4 extends Fragment {

    // Declare buttons for attaching image and PDF files
    AppCompatButton btnAttachFileImage, btnAttachFileDti, btnAttachFileBir;

    // Declare the file picker result launcher
    private final ActivityResultLauncher<String> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri result) {
                            if (result != null) {
                                // Get the file name from the URI
                                String fileName = getFileNameFromUri(result);

                                // Store the file URI for later use (you can save it in a variable or database)
                                storeFileUri(result);

                                // Check which button was clicked and update its text
                                if (currentButton != null) {
                                    currentButton.setText(fileName);
                                }

                                // Optionally show the file name in a toast
                                Toast.makeText(getContext(), "File selected: " + fileName, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

    private AppCompatButton currentButton; // Variable to store the current button clicked

    // Variable to store the selected file URI (you can use this for later use)
    private Uri selectedFileUri;

    public VendorRegistrationFragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vendor_registration4, container, false);

        // Initialize buttons
        btnAttachFileImage = rootView.findViewById(R.id.buttonAttachFileImage);
        btnAttachFileDti = rootView.findViewById(R.id.buttonAttachFileDti);
        btnAttachFileBir = rootView.findViewById(R.id.buttonAttachFileBir);

        // Set click listeners for each button to launch file picker
        btnAttachFileImage.setOnClickListener(v -> onButtonClicked(btnAttachFileImage, "image/*"));
        btnAttachFileDti.setOnClickListener(v -> onButtonClicked(btnAttachFileDti, "application/pdf")); // Only PDF for DTI
        btnAttachFileBir.setOnClickListener(v -> onButtonClicked(btnAttachFileBir, "application/pdf")); // Only PDF for BIR

        return rootView;
    }

    // This method is triggered when any button is clicked
    private void onButtonClicked(AppCompatButton button, String mimeType) {
        currentButton = button; // Store the clicked button
        openFilePicker(mimeType); // Launch the file picker with the specific MIME type
    }

    private void openFilePicker(String mimeType) {
        // Launch the file picker with the provided MIME type (image/* or application/pdf for PDFs)
        filePickerLauncher.launch(mimeType);
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

    // Store the file URI for later use
    private void storeFileUri(Uri uri) {
        selectedFileUri = uri; // Store the URI to be used later
        // You can also store this URI in SharedPreferences or a database if needed
    }

    // Method to get the selected file URI later (for use)
    public Uri getSelectedFileUri() {
        return selectedFileUri;
    }
}
