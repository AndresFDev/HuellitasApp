package com.icafdev.huellitas.controllers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.icafdev.huellitas.views.RecoverPasswordActivity;

public class RecoverPasswordController {

    public static void recover(RecoverPasswordActivity recoverPasswordActivity, String email) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isComplete()){
                            recoverPasswordActivity.finish();
                            Toast.makeText(recoverPasswordActivity, "Se ha enviado un correo para el cambio de contraseña", Toast.LENGTH_SHORT).show();
                            
                        }else {
                            Toast.makeText(recoverPasswordActivity, "Error al intentar recuperar contraseña", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
