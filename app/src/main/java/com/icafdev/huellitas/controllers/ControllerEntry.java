package com.icafdev.huellitas.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.icafdev.huellitas.MainActivity;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.Entry;

import java.util.HashMap;

public class ControllerEntry {

    public static void entryFirestore(Context context, String userPhoto, String userName, String photo, String status, String date, String city, String address, String pet_name, String type, String raza, String gen, String description, String name, String phone) {

        try{

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection(ConstantFB.ENTRIES).document();

            String userId = firebaseUser.getUid();

            String id = documentReference.getId();

            long timeCreated = firebaseUser.getMetadata().getCreationTimestamp();

            Entry entry = new Entry(id, userId, userPhoto, userName, photo, status, date, city, address, pet_name, type, raza, gen, description, name, phone, timeCreated);

            FirebaseFirestore.getInstance()
                    .collection(ConstantFB.ENTRIES)
                    .document(id)
                    .set(entry, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);

                                Toast.makeText(context, "Entrada creada con exito.", Toast.LENGTH_SHORT).show();


                            }else{

                                Toast.makeText(context, "ERROR. No se ha podido crear la entrada.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }catch(NullPointerException e){

            e.getCause();

        }

    }

//    public static void updateData(Context context, String key, String value) {
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put(key, value);
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseFirestore.getInstance()
//                .collection(ConstantFB.ENTRYS)
//                .document(id)
//                .set(map, SetOptions.merge())
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        if(task.isSuccessful()){
//                            Toast.makeText(context, "Datos actualizados con exito", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(context, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
    public static void updatePhoto(Context context, Uri photo) {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection(ConstantFB.ENTRIES).document("id");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        StorageReference archivePath = FirebaseStorage.getInstance().getReference()
                .child(ConstantFB.ENTRIES_STORAGE_IMG).child(docRef+".jpg");

        archivePath.putFile(photo)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        archivePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                updatePhotoDB(context, uri.toString(), documentReference.getResult());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.println(archivePath);
                        Toast.makeText(context, "Error al intentar subir imagen", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {



                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                        Toast.makeText(context, "Subiendo imagen: "+(int) progress+"%", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private static void updatePhotoDB(Context context, String imgUrl, String idEntry) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("photo", imgUrl);

        FirebaseFirestore.getInstance()
                .collection(ConstantFB.ENTRIES)
                .document(idEntry)
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
//
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
                .collection(ConstantFB.ENTRIES)
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
