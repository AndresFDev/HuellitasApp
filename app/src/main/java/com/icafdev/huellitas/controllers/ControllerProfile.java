package com.icafdev.huellitas.controllers;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.icafdev.huellitas.models.firebase.ConstantFB;

import java.util.HashMap;

public class ControllerProfile {

    private static double progress;

        public static void updateData(Context context, String key, String value) {

        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(firebaseUser.getUid())
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(context, "Datos actualizados con exito", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void updatePhoto(Context context, Uri photo) {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            StorageReference archivePath = FirebaseStorage.getInstance().getReference()
                    .child(ConstantFB.USERS_STORAGE_IMG_PROFILE).child(firebaseUser.getUid()+".jpg");

            archivePath.putFile(photo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            archivePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    updatePhotoDB(context, uri.toString(), firebaseUser.getUid());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error al intentar subir imagen", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                            Toast.makeText(context, "Subiendo imagen: "+(int) progress+"%", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    public static double progress(){
        return progress;
    }

    private static void updatePhotoDB(Context context, String imgUrl, String idUser) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("photo", imgUrl);

        FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(idUser)
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(context, "Imagen guardada", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Error al intentar guardar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void deletePhoto(Context context, String imgUrl) {

        StorageReference refImg = FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl);

        refImg.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            deletePhotoDB(context);
                        }else {
                            Toast.makeText(context, "Error al intentar eliminar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static void deletePhotoDB(Context contexto) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("photo", "");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(firebaseUser.getUid())
                .set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(contexto, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(contexto, "Error al intentar eliminar imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}