package com.icafdev.huellitas.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.controllers.ControllerSignUp;
import com.icafdev.huellitas.utils.ValidateEmail;

public class SignUpActivity extends AppCompatActivity {


    private TextInputEditText tiet_name, tiet_email, tiet_phone, tiet_password, tiet_conf_password;
    private Button b_signup;
    private ImageButton ib_back;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        view = findViewById(R.id.cl_signup);

        tiet_name = findViewById(R.id.tiet_name);
        tiet_email = findViewById(R.id.tiet_email);
        tiet_phone = findViewById(R.id.tiet_phone);
        tiet_password = findViewById(R.id.tiet_password);
        tiet_conf_password = findViewById(R.id.tiet_conf_password);
        b_signup = findViewById(R.id.b_signup);
        ib_back = findViewById(R.id.ib_back);

        tiet_name.addTextChangedListener(textWatcher);
        tiet_email.addTextChangedListener(textWatcher);
        tiet_phone.addTextChangedListener(textWatcher);
        tiet_password.addTextChangedListener(textWatcher);
        tiet_conf_password.addTextChangedListener(textWatcher);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ControllerSignUp.signup(SignUpActivity.this, getName(), getEmail(), getPhone(), getPhoto(), getPassword());

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

        String name = getName().trim();
        String email = getEmail().trim();
        String phone = getPhone().trim();
        String password = getPassword().trim();
        String confPassword = getConfPassword().trim();

        if ((name.length()>2) && ValidateEmail.validate(email) && phone.length() == 10 && password.length()>5 && confPassword.equals(password)){
            b_signup.setEnabled(true);
            return true;
        }else{
            b_signup.setEnabled(false);
            return false;
        }

    }

    private String getName() {
        return tiet_name.getText().toString();
    }

    private String getEmail() {
        return tiet_email.getText().toString();
    }

    private String getPhoto() {
        return "0";
    }

    private String getPhone() {
        return tiet_phone.getText().toString();
    }

    private String getPassword() {
        return tiet_password.getText().toString();
    }

    private String getConfPassword() {
        return tiet_conf_password.getText().toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        keyboardHidden(SignUpActivity.this, view);
        tiet_name.clearFocus();
        tiet_email.clearFocus();
        tiet_phone.clearFocus();
        tiet_password.clearFocus();
        tiet_conf_password.clearFocus();

        return true;
    }

    private void keyboardHidden(Context context, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}