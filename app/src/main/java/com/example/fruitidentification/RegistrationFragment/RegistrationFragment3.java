package com.example.fruitidentification.RegistrationFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fruitidentification.R;


public class RegistrationFragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration3, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Safely access the TextView after the view is created
        TextView checkLabelAgree = view.findViewById(R.id.checkLabelAgree);
        String text = "I have read and agree to <b>Fruitsy’s User Agreement</b> and <b>Privacy Policy</b>";
        checkLabelAgree.setText(Html.fromHtml(text));  // Use Html.fromHtml for the bold text
    }
}
