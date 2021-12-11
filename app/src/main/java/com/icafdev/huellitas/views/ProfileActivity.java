package com.icafdev.huellitas.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.icafdev.huellitas.R;
import com.icafdev.huellitas.views.fragment.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_profile, new ProfileFragment(), ProfileFragment.class.getSimpleName())
                .commit();

    }
}