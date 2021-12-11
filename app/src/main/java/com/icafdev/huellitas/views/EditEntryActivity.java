package com.icafdev.huellitas.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditEntryActivity extends AppCompatActivity {

    private String id="", userId="", userName="", userPhoto="", photo="", status="", date="", city="", address="", pet_name="", type="", gen="", raza="", description="", name="", phone="";
    private int city_select;
    private RadioGroup rg_status, rg_type, rg_gen;
    private RadioButton rb_search, rb_find, rb_adopt, rb_dog, rb_cat, rb_male, rb_female;
    private Spinner s_cities;
    private LinearLayout ll_photo;
    private ImageView iv_photo;
    private ProgressBar progressBar;
    private TextView tv_progress, tiet_date;
    private TextInputEditText tiet_addrees;
    private TextInputEditText tiet_pet_name;
    private TextInputEditText tiet_raza;
    private TextInputEditText tiet_description;
    private TextInputEditText tiet_name;
    private TextInputEditText tiet_phone;
    private MaterialButton b_save_entry;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

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
            gen = bundle.getString("Genero");
            raza = bundle.getString("Raza");
            description = bundle.getString("Descripción");
            name = bundle.getString("Nombre");
            phone = bundle.getString("Teléfono");
            userName = bundle.getString("UserName");
            userPhoto = bundle.getString("UserPhoto");
            userId = bundle.getString("UserId");
        }

        rb_search = findViewById(R.id.rb_search);
        rb_find = findViewById(R.id.rb_find);
        rb_adopt = findViewById(R.id.rb_adopt);
        rb_dog = findViewById(R.id.rb_dog);
        rb_cat = findViewById(R.id.rb_cat);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);

        progressBar = findViewById(R.id.progressBar);
        tv_progress = findViewById(R.id.tv_progress);

        iv_photo = findViewById(R.id.iv_photo);

        rg_status = findViewById(R.id.rg_status);
        tiet_date = findViewById(R.id.tiet_date);
        s_cities = findViewById(R.id.s_cities);
        tiet_addrees = findViewById(R.id.tiet_addrees);
        rg_type = findViewById(R.id.rg_type);
        tiet_pet_name = findViewById(R.id.tiet_pet_name);
        rg_gen = findViewById(R.id.rg_gen);
        tiet_raza = findViewById(R.id.tiet_raza);
        tiet_description = findViewById(R.id.tiet_description);
        tiet_name = findViewById(R.id.tiet_name);
        tiet_phone = findViewById(R.id.tiet_phone);
        ll_photo = findViewById(R.id.ll_photo);
        b_save_entry = findViewById(R.id.b_save_entry);

        firebaseFirestore = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditEntryActivity.this,
                R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_cities.setAdapter(adapter);

        switch (city) {
            case "Armenia":
                city_select = 1;
                s_cities.setSelection(city_select);
                break;
            case "Barranquilla":
                city_select = 2;
                s_cities.setSelection(city_select);
                break;
            case "Bello":
                city_select = 3;
                s_cities.setSelection(city_select);
                break;
            case "Bogotá":
                city_select = 4;
                s_cities.setSelection(city_select);
                break;
            case "Bucaramanga":
                city_select = 5;
                s_cities.setSelection(city_select);
                break;
            case "Cali":
                city_select = 6;
                s_cities.setSelection(city_select);
                break;
            case "Cartagena":
                city_select = 7;
                s_cities.setSelection(city_select);
                break;
            case "Cúcuta":
                city_select = 8;
                s_cities.setSelection(city_select);
                break;
            case "Ibagué":
                city_select = 9;
                s_cities.setSelection(city_select);
                break;
            case "Manizales":
                city_select = 10;
                s_cities.setSelection(city_select);
                break;
            case "Medellín":
                city_select = 11;
                s_cities.setSelection(city_select);
                break;
            case "Montería":
                city_select = 12;
                s_cities.setSelection(city_select);
                break;
            case "Neiva":
                city_select = 13;
                s_cities.setSelection(city_select);
                break;
            case "Pasto":
                city_select = 14;
                s_cities.setSelection(city_select);
                break;
            case "Pereira":
                city_select = 15;
                s_cities.setSelection(city_select);
                break;
            case "Santa Marta":
                city_select = 16;
                s_cities.setSelection(city_select);
                break;
            case "Soacha":
                city_select = 17;
                s_cities.setSelection(city_select);
                break;
            case "Soledad":
                city_select = 18;
                s_cities.setSelection(city_select);
                break;
            case "Valledupar":
                city_select = 19;
                s_cities.setSelection(city_select);
                break;
            case "Villavicencio":
                city_select = 20;
                s_cities.setSelection(city_select);
                break;
        }

        if (status.equals("Buscado")){
            rb_search.setChecked(true);
        }else if (status.equals("Encontrado")){
            rb_find.setChecked(true);
        }else {
            rb_adopt.setChecked(true);
        }

        if (type.equals("Perro")){
            rb_dog.setChecked(true);
        }else {
            rb_cat.setChecked(true);
        }

        if (gen.equals("Macho")){
            rb_male.setChecked(true);
        }else {
            rb_female.setChecked(true);
        }

        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.rb_search:
                        status = rb_search.getText().toString();
                        break;
                    case R.id.rb_find:
                        status = rb_find.getText().toString();
                        break;
                    case R.id.rb_adopt:
                        status = rb_adopt.getText().toString();
                        break;
                }
            }
        });

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.rb_dog:
                        type = rb_dog.getText().toString();
                        break;
                    case R.id.rb_cat:
                        type = rb_cat.getText().toString();
                        break;
                }
            }
        });

        rg_gen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.rb_male:
                        gen = rb_male.getText().toString();
                        break;
                    case R.id.rb_female:
                        gen = rb_female.getText().toString();
                        break;
                }
            }
        });

        tiet_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tiet_date:
                        showDatePickerDialog();
                        break;
                }
            }
        });

        Picasso.get().load(photo).placeholder(R.drawable.ic_default_img).error(R.drawable.ic_warning).into(iv_photo);

        tiet_date.setText(date);
        tiet_addrees.setText(address);
        tiet_pet_name.setText(pet_name);
        tiet_raza.setText(raza);
        tiet_description.setText(description);
        tiet_name.setText(name);
        tiet_phone.setText(phone);

        ll_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPhoto.launch("image/*");

            }
        });

        b_save_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData();
                finish();

                Intent intent = new Intent(EditEntryActivity.this, DetailEntryActivity.class);

                intent.putExtra("Foto", photo);
                intent.putExtra("Estado", status);
                intent.putExtra("Fecha", date);
                intent.putExtra("Ciudad", city);
                intent.putExtra("Dirección", address);
                intent.putExtra("Nombre mascota", pet_name);
                intent.putExtra("Tipo", type);
                intent.putExtra("Genero", gen);
                intent.putExtra("Raza", raza);
                intent.putExtra("Descripción", description);
                intent.putExtra("Nombre", name);
                intent.putExtra("Teléfono", phone);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserPhoto", userPhoto);
                intent.putExtra("UserId", userId);
                startActivity(intent);

                Toast.makeText(EditEntryActivity.this, "Guardando...", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateData(){

        city = s_cities.getSelectedItem().toString();
        date = tiet_date.getText().toString();
        address = tiet_addrees.getText().toString();
        pet_name = tiet_pet_name.getText().toString();
        raza = tiet_raza.getText().toString();
        description = tiet_description.getText().toString();
        name = tiet_name.getText().toString();
        phone = tiet_phone.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("photo", photo);
        map.put("status", status);
        map.put("date", date);
        map.put("city", city);
        map.put("address", address);
        map.put("pet_name", pet_name);
        map.put("type", type);
        map.put("raza", raza);
        map.put("gen", gen);
        map.put("description", description);
        map.put("name", name);
        map.put("phone", phone);

        firebaseFirestore.collection(ConstantFB.ENTRIES).document(id).update(map);

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                Calendar c = Calendar.getInstance();
                c.set(day, month, year);

                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
                String formattedDate = sdf.format(c.getTime());
                tiet_date.setText(formattedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
            int year = c.get(android.icu.util.Calendar.YEAR);
            int month = c.get(android.icu.util.Calendar.MONTH);
            int day = c.get(android.icu.util.Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

    }

    private void setUrlPhoto(Context context, String imgUrl) {

        photo = imgUrl;

        Picasso.get().load(imgUrl).placeholder(R.drawable.ic_default_img).error(R.drawable.ic_warning).into(iv_photo);

    }

    ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if(result != null){
                        loadPhoto(EditEntryActivity.this, result);
                    }
                }
            });

    public void loadPhoto(Context context, Uri photo) {

        StorageReference archivePath = FirebaseStorage.getInstance().getReference()
                .child(ConstantFB.ENTRIES_STORAGE_IMG).child(id+".webp");
        archivePath.putFile(photo)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        archivePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                setUrlPhoto(context, uri.toString());
                                progressBar.setVisibility(View.GONE);
                                tv_progress.setVisibility(View.GONE);

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

                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());

                        tv_progress.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress((int) progress);

                    }

                });

    }

}