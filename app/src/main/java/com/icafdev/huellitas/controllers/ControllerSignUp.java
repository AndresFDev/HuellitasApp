package com.icafdev.huellitas.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.User;
import com.icafdev.huellitas.views.ProfilePhotoActivity;

public class ControllerSignUp {

    public static void signup(Context context, String name, String email, String phone, String photo, String password) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        userFirestore(context, name, email, phone, photo);
                        Toast.makeText(context, "¡Se ha registrado con exito!.", Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(context, "ERROR. No se ha podido registrar el usuario.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

    }

    private static void userFirestore(Context context, String name, String email, String phone, String photo) {

        try{

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String id = firebaseUser.getUid();

            long timeCreate = firebaseUser.getMetadata().getCreationTimestamp();

            User user = new User(id, name, email, phone, photo, timeCreate);

            FirebaseFirestore.getInstance()
                    .collection(ConstantFB.USERS)
                    .document(id)
                    .set(user, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Intent intent = new Intent(context, ProfilePhotoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);


                            }else{

                                Toast.makeText(context, "ERROR. No se ha podido guardar el usuario.", Toast.LENGTH_SHORT).show();

                            }

                        }
                     });

        }catch(NullPointerException e){

            e.getCause();

        }

    }



}
