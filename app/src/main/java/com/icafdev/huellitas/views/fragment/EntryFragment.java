package com.icafdev.huellitas.views.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.controllers.ControllerEntry;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class EntryFragment extends Fragment {

    private static final String TAG = "";
    View view;

    private static ImageView iv_photo;
    private static ProgressBar progressBar;
    private static TextView tv_progress;
    private static String image, userP, userN;
    private static TextView et_date;

    private String status, type, gen;

    private RadioGroup rg_status, rg_type, rg_gen;
    private RadioButton rb_search, rb_find, rb_adopt, rb_dog, rb_cat, rb_male, rb_female;
    private EditText et_adrees, et_pet_name, et_raza, et_description, et_name, et_phone;
    private Spinner s_cities;
    private MaterialButton b_create_entry, b_next_1, b_next_2, b_preview_1, b_preview_2;
    private TextView tv_status, tv_type, tv_gen, tv_cities;
    private ImageButton ib_back;

    private LinearLayout ll_photo;
    private static final int GALLERY_RESULT = 1;
    private static final int RESULT_PHOTO = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_entry, container, false);

        getUser();

        tv_cities = view.findViewById(R.id.tv_cities);
        s_cities = view.findViewById(R.id.s_cities);

        ll_photo = view.findViewById(R.id.ll_photo);
        iv_photo = view.findViewById(R.id.iv_photo);

        rb_find = view.findViewById(R.id.rb_find);
        rb_search = view.findViewById(R.id.rb_search);
        rb_adopt = view.findViewById(R.id.rb_adopt);

        rb_cat = view.findViewById(R.id.rb_cat);
        rb_dog = view.findViewById(R.id.rb_dog);

        rb_male = view.findViewById(R.id.rb_male);
        rb_female = view.findViewById(R.id.rb_female);

        progressBar = view.findViewById(R.id.progressBar);
        tv_progress = view.findViewById(R.id.tv_progress);

        et_date = view.findViewById(R.id.et_date);
        et_adrees = view.findViewById(R.id.et_adrees);
        et_pet_name = view.findViewById(R.id.et_pet_name);
        et_raza = view.findViewById(R.id.et_raza);
        et_description = view.findViewById(R.id.et_description);
        et_name = view.findViewById(R.id.et_name);
        et_phone = view.findViewById(R.id.et_phone);

        b_next_1 = view.findViewById(R.id.b_next_1);
        b_next_2 = view.findViewById(R.id.b_next_2);
        b_preview_1 = view.findViewById(R.id.b_preview_1);
        b_preview_2 = view.findViewById(R.id.b_preview_2);
        b_create_entry = view.findViewById(R.id.b_create_entry);

        tv_status = view.findViewById(R.id.tv_status);
        rg_status = view.findViewById(R.id.rg_status);
        tv_type = view.findViewById(R.id.tv_type);
        rg_type = view.findViewById(R.id.rg_type);
        tv_gen = view.findViewById(R.id.tv_gen);
        rg_gen = view.findViewById(R.id.rg_gen);

        et_name.addTextChangedListener(textWatcher);
        et_adrees.addTextChangedListener(textWatcher);
        et_phone.addTextChangedListener(textWatcher);
        et_pet_name.addTextChangedListener(textWatcher);
        et_description.addTextChangedListener(textWatcher);
        et_raza.addTextChangedListener(textWatcher);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_cities.setAdapter(adapter);

        b_next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_status.setVisibility(View.GONE);
                rg_status.setVisibility(View.GONE);
                et_pet_name.setVisibility(View.GONE);
                tv_type.setVisibility(View.GONE);
                rg_type.setVisibility(View.GONE);
                tv_gen.setVisibility(View.GONE);
                rg_gen.setVisibility(View.GONE);
                et_raza.setVisibility(View.GONE);
                et_description.setVisibility(View.GONE);
                b_next_1.setVisibility(View.GONE);

                et_date.setVisibility(View.VISIBLE);
                tv_cities.setVisibility(View.VISIBLE);
                s_cities.setVisibility(View.VISIBLE);
                et_adrees.setVisibility(View.VISIBLE);
                b_preview_1.setVisibility(View.VISIBLE);
                b_next_2.setVisibility(View.VISIBLE);
            }
        });

        b_preview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_status.setVisibility(View.VISIBLE);
                rg_status.setVisibility(View.VISIBLE);
                et_pet_name.setVisibility(View.VISIBLE);
                tv_type.setVisibility(View.VISIBLE);
                rg_type.setVisibility(View.VISIBLE);
                tv_gen.setVisibility(View.VISIBLE);
                rg_gen.setVisibility(View.VISIBLE);
                et_raza.setVisibility(View.VISIBLE);
                et_description.setVisibility(View.VISIBLE);
                b_next_1.setVisibility(View.VISIBLE);

                et_date.setVisibility(View.GONE);
                tv_cities.setVisibility(View.GONE);
                s_cities.setVisibility(View.GONE);
                et_adrees.setVisibility(View.GONE);
                b_preview_1.setVisibility(View.GONE);
                b_next_2.setVisibility(View.GONE);
            }
        });

        b_next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date.setVisibility(View.GONE);
                tv_cities.setVisibility(View.GONE);
                s_cities.setVisibility(View.GONE);
                et_adrees.setVisibility(View.GONE);
                b_preview_1.setVisibility(View.GONE);
                b_next_2.setVisibility(View.GONE);

                ll_photo.setVisibility(View.VISIBLE);
                et_name.setVisibility(View.VISIBLE);
                et_phone.setVisibility(View.VISIBLE);
                b_preview_2.setVisibility(View.VISIBLE);
                b_create_entry.setVisibility(View.VISIBLE);
            }
        });

        b_preview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date.setVisibility(View.VISIBLE);
                tv_cities.setVisibility(View.VISIBLE);
                s_cities.setVisibility(View.VISIBLE);
                et_adrees.setVisibility(View.VISIBLE);
                b_preview_1.setVisibility(View.VISIBLE);
                b_next_2.setVisibility(View.VISIBLE);

                ll_photo.setVisibility(View.GONE);
                et_name.setVisibility(View.GONE);
                et_phone.setVisibility(View.GONE);
                b_preview_2.setVisibility(View.GONE);
                b_create_entry.setVisibility(View.GONE);
            }
        });

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

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }

        });

        ll_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto.launch("image/*");
            }
        });

        b_create_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerEntry.entryFirestore(getActivity(), getUserPhoto(), getUserName(), getPhoto(), getStatus(), getDate(), getCity(), getAddress(), getPet_name(), getType(), getRaza(), getGen(), getDescription(), getName(), getPhone());
            }

        });

        return view;

    }

    public String getUserPhoto() {
        return userP;
    }

    public String getUserName() {
        return userN;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int day, int month, int year) {
            Calendar c = Calendar.getInstance();
            c.set(day, month, year);

            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
            String formattedDate = sdf.format(c.getTime());
            et_date.setText(formattedDate);

        }
    }

    private static void setUrlPhoto(Context context, String imgUrl) {

        image = imgUrl;

        Picasso.get().load(imgUrl).placeholder(R.drawable.ic_default_img).error(R.drawable.ic_warning).into(iv_photo);

    }

    ActivityResultLauncher<String> getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        loadPhoto(getActivity(), result);
                    }
                }
            });

    public void loadPhoto(Context context, Uri photo) {

        String id = UUID.randomUUID().toString();

        StorageReference archivePath = FirebaseStorage.getInstance().getReference()
                .child(ConstantFB.ENTRIES_STORAGE_IMG).child(id+".jpg");
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

    public static void getUser(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        FirebaseFirestore.getInstance()
                .collection(ConstantFB.USERS)
                .document(firebaseUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                if(user!=null){

                    userP = user.getPhoto();
                    userN = user.getName();

                }

            }
        });

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            enable();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean enable(){

        String name = getName().trim();
        String pet_Name = getPet_name().trim();
        String phone = getPhone().trim();
        String address = getAddress().trim();
        String description = getDescription().trim();
        String raza = getRaza().trim();

        if ((name.length()>2) && (pet_Name.length()>2) && (address.length()>2) && (description.length()>2) && (raza.length()>2) && phone.length() == 10){
            b_create_entry.setEnabled(true);
            return true;
        }else{
            b_create_entry.setEnabled(false);
            return false;
        }

    }

    private String getPhoto() { return image; }

    private String getStatus() {
        return status;
    }

    private String getDate() {
        return et_date.getText().toString();
    }

    private String getCity() {
        return s_cities.getSelectedItem().toString();
    }

    private String getAddress() {
        return et_adrees.getText().toString();
    }

    private String getPet_name() {
        return et_pet_name.getText().toString();
    }

    private String getType() {
        return type;
    }

    private String getRaza() {
        return et_raza.getText().toString();
    }

    private String getGen() {
        return gen;
    }

    private String getDescription() { return et_description.getText().toString(); }

    private String getName() {
        return et_name.getText().toString();
    }

    private String getPhone() {
        return et_phone.getText().toString();
    }


    public boolean onTouchEvent(MotionEvent event) {

        keyboardHidden(getActivity(), view);
        et_name.clearFocus();
        et_pet_name.clearFocus();
        et_adrees.clearFocus();
        et_raza.clearFocus();
        et_description.clearFocus();
        et_phone.clearFocus();

        return true;
    }

    private void keyboardHidden(Context context, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}