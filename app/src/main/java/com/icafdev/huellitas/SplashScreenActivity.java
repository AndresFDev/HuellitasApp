package com.icafdev.huellitas;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.icafdev.huellitas.views.SignInActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            startNewActivity(SignInActivity.class);
        }else{
            startNewActivity(MainActivity.class);
        }

    }

    private void startNewActivity(Class clase){
        startActivity(new Intent(this, clase));
        finish();
    }

}