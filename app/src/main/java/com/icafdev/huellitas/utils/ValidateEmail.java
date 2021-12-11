package com.icafdev.huellitas.utils;

import android.util.Patterns;

public class ValidateEmail {

    public static boolean validate(String email){

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

}
