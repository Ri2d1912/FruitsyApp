package com.example.fruitidentification.RegistrationFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fruitidentification.R;
import com.example.fruitidentification.ViewModel.regFrag1VM;

public class RegistrationFragment1 extends Fragment {

    private regFrag1VM viewModel;
    private EditText editUsernameCreate, editPasswordCreate, editConPasswordCreate;

    public RegistrationFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration1, container, false);

        // Initialize the ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(regFrag1VM.class);

        // Find the EditText fields
        editUsernameCreate = view.findViewById(R.id.editUsernameCreate);
        editPasswordCreate = view.findViewById(R.id.editPasswordCreate);
        editConPasswordCreate = view.findViewById(R.id.editConPasswordCreate);

            // Observe the LiveData from ViewModel to retrieve the saved username
            viewModel.getUsername().observe(getViewLifecycleOwner(), input -> {
                if (input != null && !input.equals(editUsernameCreate.getText().toString())) {
                    editUsernameCreate.setText(input);  // Restore the username when the data changes
                }
            });

            // Observe the LiveData from ViewModel for password and confirm password
            viewModel.getPassword().observe(getViewLifecycleOwner(), input -> {
                if (input != null && !input.equals(editPasswordCreate.getText().toString())) {
                    editPasswordCreate.setText(input);
                }
            });

            viewModel.getConfirmPassword().observe(getViewLifecycleOwner(), input -> {
                if (input != null && !input.equals(editConPasswordCreate.getText().toString())) {
                    editConPasswordCreate.setText(input);
                }
            });

            // Set a TextWatcher to save input data when the user types
            editUsernameCreate.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    // Save the username input to ViewModel
                    viewModel.setUsername(charSequence.toString());
                }

                @Override
                public void afterTextChanged(android.text.Editable editable) {}
            });

        // Set TextWatchers for password fields to save data
        editPasswordCreate.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the password input to ViewModel
                viewModel.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        editConPasswordCreate.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Save the confirm password input to ViewModel
                viewModel.setConfirmPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        return view;
    }
}
