package com.icafdev.huellitas.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.adapters.AdapterMyEntries;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.Entry;
import com.icafdev.huellitas.models.firebase.User;
import com.icafdev.huellitas.views.EditProfileActivity;
import com.icafdev.huellitas.views.SettingsActivity;
import com.icafdev.huellitas.views.SignInActivity;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    View view;

    private String imgUrl;
    private ImageView iv_photo;
    private TextView tv_name, tv_city, tv_phone, tv_email;
    private ListenerRegistration listenerRegistration;
    private ImageButton ib_settings;
    private MaterialButton b_edit;

    private RecyclerView rv_my_entries;
    private AdapterMyEntries adapterMyEntries;
    private CollectionReference collectionReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getData();

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        collectionReference = FirebaseFirestore.getInstance().collection(ConstantFB.ENTRIES);

        rv_my_entries = view.findViewById(R.id.rv_my_entries);

        rv_my_entries.setLayoutManager( new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        String firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = collectionReference.whereEqualTo("userId", firebaseUser);
        FirestoreRecyclerOptions<Entry> entryFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Entry>().setQuery(query, Entry.class).build();
        adapterMyEntries = new AdapterMyEntries(entryFirestoreRecyclerOptions, this);
        adapterMyEntries.notifyDataSetChanged();
        rv_my_entries.setAdapter(adapterMyEntries);

        iv_photo = view.findViewById(R.id.iv_photo);
        tv_name = view.findViewById(R.id.tv_name);
        tv_city = view.findViewById(R.id.tv_city);
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);

        ib_settings = view.findViewById(R.id.ib_settings);
        b_edit = view.findViewById(R.id.b_edit);

        ib_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_configs, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.configs:
                                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.signout:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                getActivity().finish();
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });

        return view;

    }

    private void getData() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        DocumentReference documentReference= FirebaseFirestore.getInstance()
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

                    if(user!=null){

                        imgUrl = user.getPhoto();
                        String name = user.getName();
                        String email = user.getEmail();
                        String phone = user.getPhone();

                        if(imgUrl != null && imgUrl.length() > 0){
                            Picasso.get().load(imgUrl).placeholder(R.drawable.ic_img_profile).error(R.drawable.ic_default_img).into(iv_photo);
                        }else {
                            iv_photo.setImageResource(R.drawable.ic_img_profile);
                        }

                        tv_name.setText(name);
                        tv_email.setText(email);
                        tv_phone.setText(phone);
                    }
                }
            }catch (NullPointerException | IllegalStateException e){
                e.getCause();
            }
        }
    };

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(listenerRegistration != null){
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterMyEntries.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterMyEntries.stopListening();
    }

}