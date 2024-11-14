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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VendorRegistrationFragment2 extends Fragment {

    private ImageView imgViewValidId;
    private Uri ValidIdImageUri = null;
    private TextInputEditText editVendorBday;
    private EditText editVendorFname, editVendorMname, editVendorLname, editVendorExname, editVendorStreet, editVendorBarangay, editVendorCity, editVendorProvince, editVendorPostal, editVendorMobileNo;
    private Spinner spinnerVendorGender;

    private vendorRegFragVM viewModel;


    // ActivityResultLauncher for gallery selection
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    ValidIdImageUri = uri;  // Store the image URI
                    imgViewValidId.setImageURI(ValidIdImageUri);  // Set selected image in ImageView
                    imgViewValidId.setTag(ValidIdImageUri);  // Store the URI as tag
                    viewModel.setValidIdImageUri(ValidIdImageUri);  // Save the URI in ViewModel
                } else {
                    Toast.makeText(getContext(), "Image selection canceled", Toast.LENGTH_SHORT).show();
                }
            });

    public VendorRegistrationFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_registration2, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(vendorRegFragVM.class);

        editVendorFname = view.findViewById(R.id.editVendorFname);
        editVendorMname = view.findViewById(R.id.editVendorMname);
        editVendorLname = view.findViewById(R.id.editVendorLname);
        editVendorExname = view.findViewById(R.id.editVendorExname);
        editVendorBday = view.findViewById(R.id.editVendorBday);

        editVendorStreet = view.findViewById(R.id.editVendorStreet);
        editVendorBarangay = view.findViewById(R.id.editVendorBarangay);
        editVendorCity = view.findViewById(R.id.editVendorCity);
        editVendorProvince = view.findViewById(R.id.editVendorProvince);
        editVendorPostal = view.findViewById(R.id.editVendorPostal);
        editVendorMobileNo = view.findViewById(R.id.editVendorMobileNo);
        spinnerVendorGender = view.findViewById(R.id.spinnerVendorGender);
        imgViewValidId = view.findViewById(R.id.imgViewValidId);


        // Set up the onClickListener for imgViewValidId to open the gallery
        imgViewValidId.setOnClickListener(v -> launchGallery());  // Launch image picker for gallery selection

        editVendorBday.setOnClickListener(v -> showDatePickerDialog());

        liveData();
        textWatcher();
        return view;
    }

    // Launch the gallery for image selection
    private void launchGallery() {
        imagePickerLauncher.launch("image/*");  // Launch image picker for selecting image from gallery
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
                    editVendorBday.setText(formattedDate);  // Set the selected date to the EditText
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void liveData() {
        // Observe the LiveData from ViewModel to retrieve the saved data for each field
        viewModel.getFName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorFname.getText().toString())) {
                editVendorFname.setText(input);  // Restore the first name when the data changes
            }
        });

        viewModel.getMName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorMname.getText().toString())) {
                editVendorMname.setText(input);  // Restore the middle name when the data changes
            }
        });

        viewModel.getLName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorLname.getText().toString())) {
                editVendorLname.setText(input);  // Restore the last name when the data changes
            }
        });

        viewModel.getExName().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorExname.getText().toString())) {
                editVendorExname.setText(input);  // Restore the ex name when the data changes
            }
        });

        viewModel.getStreet().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorStreet.getText().toString())) {
                editVendorStreet.setText(input);  // Restore the street when the data changes
            }
        });

        viewModel.getBarangay().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorBarangay.getText().toString())) {
                editVendorBarangay.setText(input);  // Restore the barangay when the data changes
            }
        });

        viewModel.getCity().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorCity.getText().toString())) {
                editVendorCity.setText(input);  // Restore the city when the data changes
            }
        });

        viewModel.getProvince().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorProvince.getText().toString())) {
                editVendorProvince.setText(input);  // Restore the province when the data changes
            }
        });

        viewModel.getPostal().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorPostal.getText().toString())) {
                editVendorPostal.setText(input);  // Restore the postal code when the data changes
            }
        });

        viewModel.getMobile().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorMobileNo.getText().toString())) {
                editVendorMobileNo.setText(input);  // Restore the mobile number when the data changes
            }
        });

        viewModel.getGender().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(spinnerVendorGender.getSelectedItem().toString())) {
                // Restore gender selection if needed (spinner update)
                int position = getVendorGenderPosition(input); // Method to get the position based on gender string
                spinnerVendorGender.setSelection(position);
            }
        });

        viewModel.getValidIdImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                imgViewValidId.setImageURI(uri);  // Set image on the ImageView
                imgViewValidId.setTag(uri);  // Optionally store the URI as a tag
            }
        });

        viewModel.getBday().observe(getViewLifecycleOwner(), input -> {
            if (input != null && !input.equals(editVendorBday.getText().toString())) {
                editVendorBday.setText(input);  // Restore the birthday when the data changes
            }
        });
    }

    private int getVendorGenderPosition(String gender) {
        // You may need to adjust this based on your spinner's adapter and data
        // For example, if your adapter is a simple list like ["Male", "Female", "Other"], map the gender to its position
        List<String> genderList = Arrays.asList("Male", "Female", "Other");
        return genderList.indexOf(gender);
    }

    private void textWatcher() {
        // Set a TextWatcher to save input data when the user types
        editVendorFname.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setFName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorMname.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setMName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorLname.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setLName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorExname.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setExName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        spinnerVendorGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        editVendorStreet.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setStreet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorBarangay.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setBarangay(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorCity.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setCity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorProvince.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setProvince(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorPostal.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setPostal(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorMobileNo.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setMobile(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editVendorBday.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setBday(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });
    }



}
