package com.icafdev.huellitas.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.icafdev.huellitas.MainActivity;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.controllers.ControllerProfile;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.User;
import com.squareup.picasso.Picasso;

public class ProfilePhotoActivity extends AppCompatActivity {

    private View view;

    private static final int GALLERY_RESULT = 1;
    private static final int RESULT_PHOTO = 2;

    private String imgUrl;
    private ImageView iv_photo;
    private LinearLayout ll_photo;
    private ProgressBar progressBar;
    private TextView tv_progress;
    private Button b_save;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);

        getSupportActionBar().hide();

        getData();

        view = findViewById(R.id.rl_profile_image);

        ll_photo = findViewById(R.id.ll_photo);
        iv_photo = findViewById(R.id.iv_photo);
        tv_progress = findViewById(R.id.tv_progress);
        b_save = findViewById(R.id.b_save);

        progressBar = findViewById(R.id.progressBar);

        if (ControllerProfile.progress() < 0){
            tv_progress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress((int) ControllerProfile.progress());
        } else if (ControllerProfile.progress() == 100) {
            tv_progress.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        ll_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                PopupMenu popupMenu = new PopupMenu(ProfilePhotoActivity.this, v);
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
                                    ControllerProfile.deletePhoto(ProfilePhotoActivity.this, imgUrl);
                                }else {
                                    Toast.makeText(ProfilePhotoActivity.this, "No hay foto para eliminar", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(ProfilePhotoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if(result != null){
                        ControllerProfile.updatePhoto(ProfilePhotoActivity.this, result);
                    }
                }
            });

    private void getData() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(firebaseUser.getUid());

        listenerRegistration = documentReference.addSnapshotListener(infoUser);

    }

    private EventListener<DocumentSnapshot> infoUser = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {

            try {
                if(value != null){

                    User user =  value.toObject(User.class);

                    if(user != null){

                        imgUrl = user.getPhoto();

                        if(imgUrl != null && imgUrl.length() > 0){
                            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_img_profile).error(R.drawable.ic_default_img).into(iv_photo);
                        }else {
                            iv_photo.setImageResource(R.drawable.ic_img_profile);
                        }

                    }
                }
            }catch (NullPointerException | IllegalStateException e){
                e.getCause();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(listenerRegistration != null){

            listenerRegistration.remove();
            listenerRegistration = null;

        }

    }

}
