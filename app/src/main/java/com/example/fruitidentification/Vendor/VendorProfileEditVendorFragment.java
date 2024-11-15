package com.example.fruitidentification.Vendor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.R;
import com.example.fruitidentification.VendorCreateAcc;
import com.example.fruitidentification.ViewModel.vendorInfoViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VendorProfileEditVendorFragment extends Fragment {
    EditText editVendorFname, editVendorMname, editVendorLname, editVendorExname,
             editVendorStreet, editVendorBarangay, editVendorCity, editVendorProvince,
            editVendorPostal, editVendorMobileNo;

    Spinner spinnerVendorGender;
    TextInputEditText editVendorBday;

    String vendorFname, vendorMname, vendorLname, vendorExname, vendorGender,
            vendorBday, vendorStreet, vendorBarangay, vendorCity, vendorProvince, vendorPostal, vendorMobile;
    private DBHelper myDB;
    AppCompatButton btnSave, btnCancel;
    private long vendorId; // Variable to hold the vendorId

    public VendorProfileEditVendorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_profile_edit_vendor, container, false);
        myDB = new DBHelper(getActivity());  // Initialize the DBHelper object
        // Retrieve the vendorId from arguments passed to the fragment
        if (getArguments() != null) {
            vendorId = getArguments().getLong("vendorId", -1); // Default to -1 if vendorId is not found
        }

        // Initialize the EditText views
        editVendorFname = view.findViewById(R.id.editVendorFname);
        editVendorMname = view.findViewById(R.id.editVendorMname);
        editVendorLname = view.findViewById(R.id.editVendorLname);
        editVendorExname = view.findViewById(R.id.editVendorExname);
        spinnerVendorGender = view.findViewById(R.id.spinnerVendorGender);
        editVendorBday = view.findViewById(R.id.editVendorBday);
        editVendorStreet = view.findViewById(R.id.editVendorStreet);
        editVendorBarangay = view.findViewById(R.id.editVendorBarangay);
        editVendorCity = view.findViewById(R.id.editVendorCity);
        editVendorProvince = view.findViewById(R.id.editVendorProvince);
        editVendorPostal = view.findViewById(R.id.editVendorPostal);
        editVendorMobileNo = view.findViewById(R.id.editVendorMobileNo);
        // Initialize the buttons
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        // Load the vendor profile if vendorId is valid
        if (vendorId != -1) {
            loadVendorProfile(vendorId);
        }

        editVendorBday.setOnClickListener(v -> showDatePickerDialog());
        // Set onClickListeners if needed
        btnSave.setOnClickListener(v -> saveVendorInfo());
        btnCancel.setOnClickListener(v -> cancelEditing());


        return view; // Add this return statement to return the inflated view
    }

    private void saveVendorInfo() {
        // Capture the updated values from the input fields
        vendorFname = editVendorFname.getText().toString().trim();
        vendorMname = editVendorMname.getText().toString().trim();
        vendorLname = editVendorLname.getText().toString().trim();
        vendorExname = editVendorExname.getText().toString().trim();
        vendorGender = spinnerVendorGender.getSelectedItem().toString();
        vendorBday = editVendorBday.getText().toString().trim();
        vendorStreet = editVendorStreet.getText().toString().trim();
        vendorBarangay = editVendorBarangay.getText().toString().trim();
        vendorCity = editVendorCity.getText().toString().trim();
        vendorProvince = editVendorProvince.getText().toString().trim();
        vendorPostal = editVendorPostal.getText().toString().trim();
        vendorMobile = editVendorMobileNo.getText().toString().trim();

        // Validate if required fields are not empty
        if (vendorFname.isEmpty() || vendorLname.isEmpty()) {
            Toast.makeText(getActivity(), "Please Input First Name / Last Name", Toast.LENGTH_LONG).show();
        } else {
            // Call your database method to update the vendor info
            Boolean checkUpdate = myDB.updateVendorInfo(getActivity(), vendorId, vendorFname, vendorMname, vendorLname, vendorExname,
                    vendorGender, vendorBday, vendorStreet, vendorBarangay, vendorCity, vendorProvince, vendorPostal, vendorMobile);

            if (checkUpdate) {
                Toast.makeText(getActivity(), "Vendor information updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to update vendor information", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void cancelEditing() {
        // Add code here to cancel editing and go back
    }
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
    private void loadVendorProfile(long vendorId) {
        // Query the database to get the vendor information using vendorId
        List<vendorInfoViewModel> vendorDetails = myDB.getVendorInfo(vendorId);
        if (vendorDetails != null && !vendorDetails.isEmpty()) {

            vendorInfoViewModel vendor = vendorDetails.get(0); // Get the first vendor
            vendorFname = vendor.getFirstName();
            vendorMname = vendor.getMiddleName();
            vendorLname = vendor.getLastName();
            vendorExname = vendor.getExtensionName();
            vendorGender = vendor.getVendorGender();
            vendorBday = vendor.getVendorBday();
            vendorStreet = vendor.getStreetAddress();
            vendorBarangay = vendor.getBarangay();
            vendorCity = vendor.getCity();
            vendorProvince = vendor.getProvince();
            vendorPostal = vendor.getVendorPostal();
            vendorMobile= vendor.getMobileNumber();

            editVendorFname.setText(vendorFname);
            editVendorMname.setText(vendorMname);
            editVendorLname.setText(vendorLname);
            editVendorExname.setText(vendorExname);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.gender_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerVendorGender.setAdapter(adapter);
            if (vendorGender != null) {
                int spinnerPosition = adapter.getPosition(vendorGender);
                spinnerVendorGender.setSelection(spinnerPosition);
            }


            editVendorBday.setText(vendorBday);
            editVendorStreet.setText(vendorStreet);
            editVendorBarangay.setText(vendorBarangay);
            editVendorCity.setText(vendorCity);
            editVendorProvince.setText(vendorProvince);
            editVendorPostal.setText(vendorPostal);
            editVendorMobileNo.setText(vendorMobile);
        }
    }
}
