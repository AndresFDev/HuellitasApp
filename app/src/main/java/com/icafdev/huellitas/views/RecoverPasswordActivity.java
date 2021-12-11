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
import com.icafdev.huellitas.controllers.RecoverPasswordController;
import com.icafdev.huellitas.utils.ValidateEmail;

public class RecoverPasswordActivity extends AppCompatActivity {

    private TextInputEditText tiet_email;
    private Button b_send;
    private ImageButton ib_back;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        getSupportActionBar().hide();

        view =findViewById(R.id.cl_rec_password);

        tiet_email = findViewById(R.id.tiet_email);
        b_send = findViewById(R.id.b_send);
        ib_back = findViewById(R.id.ib_back);

        tiet_email.addTextChangedListener(textWatcher);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        b_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecoverPasswordController.recover(RecoverPasswordActivity.this, getEmail());
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

        if (ValidateEmail.validate(email)){
            b_send.setEnabled(true);
//            b_signup.setTextColor(getColor(R.color.colorOnPrimary));
            return true;
        }else{
            b_send.setEnabled(false);
//            b_signup.setTextColor(getColor(R.color.colorPrimary));
            return false;
        }

    }

    private String getEmail() {
        return tiet_email.getText().toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        keyboardHidden(RecoverPasswordActivity.this, view);
        tiet_email.clearFocus();

        return true;
    }

    private void keyboardHidden(Context context, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

}