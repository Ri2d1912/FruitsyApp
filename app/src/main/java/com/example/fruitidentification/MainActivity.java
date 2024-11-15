package com.example.fruitidentification;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitidentification.Model.DBHelper;
import com.example.fruitidentification.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private DBHelper myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        myDB =new DBHelper(this);

    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}