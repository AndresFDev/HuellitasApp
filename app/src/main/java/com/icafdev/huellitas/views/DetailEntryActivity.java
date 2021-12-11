package com.icafdev.huellitas.views;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.icafdev.huellitas.R;
import com.squareup.picasso.Picasso;

public class DetailEntryActivity extends AppCompatActivity {

    private String photo="", status="", date="", city="", address="", pet_name="", type="", raza="", gen="", description="", name="", phone="", userId="", id="", userName= "", userPhoto= "";
    private TextView tv_pet_name, tv_date, tv_city, tv_address, tv_status, tv_gen, tv_raza, tv_description, tv_name, tv_phone, tv_fijo, tv_user_name;
    private ImageView iv_photo, iv_type, iv_gen, iv_user_photo;
    private MaterialButton b_back, b_editar;
    private LinearLayout ll_bgfoot;
    private View v_line;

    private FirebaseFirestore firebaseFirestore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_entry);

        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            id = bundle.getString("id");
            photo = bundle.getString("Foto");
            status = bundle.getString("Estado");
            date = bundle.getString("Fecha");
            city = bundle.getString("Ciudad");
            address = bundle.getString("Dirección");
            pet_name = bundle.getString("Nombre mascota");
            type = bundle.getString("Tipo");
            raza = bundle.getString("Raza");
            gen = bundle.getString("Genero");
            description = bundle.getString("Descripción");
            name = bundle.getString("Nombre");
            phone = bundle.getString("Teléfono");
            userId = bundle.getString("UserId");
            userName = bundle.getString("UserName");
            userPhoto = bundle.getString("UserPhoto");

        }

        iv_user_photo = findViewById(R.id.iv_user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_photo = findViewById(R.id.iv_photo);
        tv_status = findViewById(R.id.tv_status);
        tv_date = findViewById(R.id.tv_date);
        tv_city = findViewById(R.id.tv_city);
        tv_address = findViewById(R.id.tv_address);
        tv_pet_name = findViewById(R.id.tv_pet_name);
        iv_type = findViewById(R.id.iv_type);
        tv_raza = findViewById(R.id.tv_raza);
        tv_gen = findViewById(R.id.tv_gen);
        iv_gen = findViewById(R.id.iv_gen);
        tv_description = findViewById(R.id.tv_description);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_fijo = findViewById(R.id.tv_fijo);
        ll_bgfoot = findViewById(R.id.ll_bgfoot);
        v_line = findViewById(R.id.v_line);
        b_back = findViewById(R.id.b_back);
        b_editar = findViewById(R.id.b_editar);

        String type_text = type;
        tv_user_name.setText(userName);
        tv_status.setText(status);
        tv_date.setText(date);
        tv_city.setText(city);
        tv_address.setText(address);
        tv_pet_name.setText(pet_name);
        tv_raza.setText(raza);
        tv_gen.setText(gen);
        tv_description.setText(description);
        tv_name.setText(name);
        tv_phone.setText(phone);

        Picasso.get().load(photo).placeholder(R.drawable.ic_default_img).error(R.drawable.ic_warning).into(iv_photo);
        Picasso.get().load(userPhoto).placeholder(R.drawable.ic_profile).error(R.drawable.ic_warning).into(iv_user_photo);

        if (type_text != null) {
            if (type_text.equals(("Perro"))) {
                iv_type.setBackgroundResource(R.drawable.ic_dog);
            } else {
                iv_type.setBackgroundResource(R.drawable.ic_cat);
            }
        }

        if (type_text != null) {
            if (tv_gen.getText().toString().equals(("Macho"))) {
                iv_gen.setImageResource(R.drawable.ic_male);
            } else {
                iv_gen.setImageResource(R.drawable.ic_female);
            }
        }

        if (tv_status.getText().toString().equals("Buscado")){
            tv_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            ll_bgfoot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            tv_fijo.setText("Buscado desde:");
        }else {
            if (tv_status.getText().toString().equals("Encontrado")){
                tv_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                ll_bgfoot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                tv_fijo.setText("Encontrado desde:");
            }else {
                if (tv_status.getText().toString().equals("En adopción")){
                    tv_status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    ll_bgfoot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    tv_fijo.setText("En adopción desde:");
                }
            }
        }

        tv_description.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(DetailEntryActivity.this, MainActivity.class);
            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (userId != null) {
            if (userId.equals(firebaseUser.getUid())) {

                v_line.setVisibility(View.VISIBLE);
                b_editar.setVisibility(View.VISIBLE);

            }
        }

        b_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "¿Seguro desea editar?", Snackbar.LENGTH_LONG)
                        .setAction("Si", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                finish();

                                Toast.makeText(DetailEntryActivity.this, "Editando...", Toast.LENGTH_LONG).show();

                                status = tv_status.getText().toString();
                                date = tv_date.getText().toString();
                                city = tv_city.getText().toString();
                                address = tv_address.getText().toString();
                                pet_name = tv_pet_name.getText().toString();
                                type = type_text;
                                raza = tv_raza.getText().toString();
                                gen = tv_gen.getText().toString();
                                description = tv_description.getText().toString();
                                name = tv_name.getText().toString();
                                phone = tv_phone.getText().toString();

                                Intent intent = new Intent(DetailEntryActivity.this, EditEntryActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("Foto", photo);
                                intent.putExtra("Estado", status);
                                intent.putExtra("Fecha", date);
                                intent.putExtra("Ciudad", city);
                                intent.putExtra("Dirección", address);
                                intent.putExtra("Nombre mascota", pet_name);
                                intent.putExtra("Tipo", type);
                                intent.putExtra("Raza", raza);
                                intent.putExtra("Genero", gen);
                                intent.putExtra("Descripción", description);
                                intent.putExtra("Nombre", name);
                                intent.putExtra("Teléfono", phone);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserPhoto", userPhoto);
                                intent.putExtra("UserId", userId);
                                startActivity(intent);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(DetailEntryActivity.this, R.color.colorOnPrimary))
                        .setTextColor(ContextCompat.getColor(DetailEntryActivity.this, R.color.colorOnPrimary))
                        .show();
            }

        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {


            Slide slide = new Slide(Gravity.BOTTOM);
            slide.setDuration(1000);
            getWindow().setEnterTransition(slide);

            getWindow().setReturnTransition(new Fade());
        }

    }

}