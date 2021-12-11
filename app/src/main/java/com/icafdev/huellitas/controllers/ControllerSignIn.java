package com.icafdev.huellitas.controllers;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.icafdev.huellitas.MainActivity;
import com.icafdev.huellitas.views.SignInActivity;

public class ControllerSignIn {

    public static void signin(SignInActivity signInActivity, String email, String password){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            
                            signInActivity.startActivity(new Intent(signInActivity, MainActivity.class));
                            signInActivity.finish();
                            
                        }else{

                            Toast.makeText(signInActivity, "ERROR. No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show();
                            Toast.makeText(signInActivity, "Verifique si correo o contraseña son correctos", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}
