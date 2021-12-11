package com.icafdev.huellitas.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.controllers.ControllerSignIn;
import com.icafdev.huellitas.utils.ValidateEmail;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText tiet_email, tiet_password;
    private TextView tv_rec_password;
    private Button b_signin, b_signup;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        view = findViewById(R.id.cl_signin);

        tiet_email = findViewById(R.id.tiet_email);
        tiet_password = findViewById(R.id.tiet_password);
        tv_rec_password = findViewById(R.id.tv_rec_password);
        b_signin = findViewById(R.id.b_signin);
        b_signup = findViewById(R.id.b_signup);

        tiet_email.addTextChangedListener(textWatcher);
        tiet_password.addTextChangedListener(textWatcher);

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ControllerSignIn.signin(SignInActivity.this, getEmail(), getPassword());

            }
        });

        tv_rec_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, RecoverPasswordActivity.class));
            }
        });

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            enable();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean enable(){

        String email = getEmail().trim();
        String password = getPassword().trim();

        if (ValidateEmail.validate(email) && password.length()>5){
            b_signin.setEnabled(true);
            return true;
        }else{
            b_signin.setEnabled(false);
            return false;
        }

    }

    private String getEmail() {
        return tiet_email.getText().toString();
    }

    private String getPassword() {
        return tiet_password.getText().toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        keyboardHidden(SignInActivity.this, view);
        tiet_email.clearFocus();
        tiet_password.clearFocus();

        return true;
    }

    private void keyboardHidden(Context context, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}