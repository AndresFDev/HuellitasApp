package com.icafdev.huellitas.views;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.controllers.ControllerProfile;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.User;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private String imgUrl;
    private ImageView iv_photo;
    private TextInputEditText tiet_name, tiet_phone;
    private ListenerRegistration listenerRegistration;
    private Button b_save;
    private ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().hide();

        getData();

        iv_photo = findViewById(R.id.iv_photo);
        tiet_name = findViewById(R.id.tiet_name);
        tiet_phone = findViewById(R.id.tiet_phone);

        ib_back = findViewById(R.id.ib_back);
        b_save = findViewById(R.id.b_save);


        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditProfileActivity.this.finish();

            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(EditProfileActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_img_profile, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.gallery:
                                getPhoto.launch("image/*");
                                break;
                            case R.id.delete:
                                if(imgUrl != null && imgUrl.length() > 0){
                                    ControllerProfile.deletePhoto(EditProfileActivity.this, imgUrl);
                                }else {
                                    Toast.makeText(EditProfileActivity.this, "No hay foto para eliminar", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getName().trim().length() > 0 && getPhone().trim().length() == 10){
                    ControllerProfile.updateData(EditProfileActivity.this, "name", getName());
                    ControllerProfile.updateData(EditProfileActivity.this, "phone", getPhone());
                }else {
                    Toast.makeText(EditProfileActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
                }

                finish();

            }
        });

    }

    private void getData() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(firebaseUser.getUid());

        listenerRegistration = documentReference.addSnapshotListener(infoUser);

    }

    private EventListener<DocumentSnapshot> infoUser = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

            try {
                if(value != null){

                    User user =  value.toObject(User.class);

                    if(user != null){

                        imgUrl = user.getPhoto();
                        String name = user.getName();
                        String phone = user.getPhone();

                        if(imgUrl != null && imgUrl.length() > 0){
                            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_img_profile).error(R.drawable.ic_default_img).into(iv_photo);
                        }else {
                            iv_photo.setImageResource(R.drawable.ic_img_profile);
                        }

                        tiet_name.setText(name);
                        tiet_phone.setText(phone);
                    }
                }
            }catch (NullPointerException | IllegalStateException e){
                e.getCause();
            }
        }
    };

    private String getName(){
        return tiet_name.getText().toString();
    }

    private String getPhone(){
        return tiet_phone.getText().toString();
    }

//    @Override
//    public void onDetach() {
//
//        super.onDetach();
//
//        if(listenerRegistration != null){
//
//            listenerRegistration.remove();
//            listenerRegistration = null;
//
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(listenerRegistration != null){

            listenerRegistration.remove();
            listenerRegistration = null;

        }

    }

    ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if(result != null){
                        ControllerProfile.updatePhoto(EditProfileActivity.this, result);
                    }
                }
            });

}