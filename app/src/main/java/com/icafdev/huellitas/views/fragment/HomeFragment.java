package com.icafdev.huellitas.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.adapters.AdapterHome;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.Entry;

public class HomeFragment extends Fragment {

    View view;

    private RecyclerView rv_home;
    private AdapterHome adapterHome;
    private CollectionReference collectionReference;

    private FloatingActionButton fab_search_p, fab_adoption, fab_find, fab_search;

    private TextView tv_adoption, tv_find, tv_search;

    private Boolean isAllFabsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        collectionReference = FirebaseFirestore.getInstance().collection(ConstantFB.ENTRIES);

        rv_home = view.findViewById(R.id.rv_home);

        fab_search_p = view.findViewById(R.id.fab_search_p);
        fab_adoption = view.findViewById(R.id.fab_adoption);
        fab_find = view.findViewById(R.id.fab_find);
        fab_search = view.findViewById(R.id.fab_search);

        tv_adoption = view.findViewById(R.id.tv_adoption);
        tv_find = view.findViewById(R.id.tv_find);
        tv_search = view.findViewById(R.id.tv_search);

        fab_adoption.setVisibility(View.GONE);
        fab_find.setVisibility(View.GONE);
        fab_search.setVisibility(View.GONE);
        tv_adoption.setVisibility(View.GONE);
        tv_find.setVisibility(View.GONE);
        tv_search.setVisibility(View.GONE);

        isAllFabsVisible = false;

        fab_search_p.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            fab_adoption.show();
                            fab_find.show();
                            fab_search.show();
                            tv_adoption.setVisibility(View.VISIBLE);
                            tv_find.setVisibility(View.VISIBLE);
                            tv_search.setVisibility(View.VISIBLE);

                            isAllFabsVisible = true;
                        } else {

                            fab_adoption.hide();
                            fab_find.hide();
                            fab_search.hide();
                            tv_adoption.setVisibility(View.GONE);
                            tv_find.setVisibility(View.GONE);
                            tv_search.setVisibility(View.GONE);

                            isAllFabsVisible = false;
                        }
                    }
                });

        fab_adoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new StatusAdoptFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cl_home, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fab_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new StatusFindFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cl_home, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new StatusSearchFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.cl_home, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        rv_home.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Query query = collectionReference.orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Entry> entryFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Entry>().setQuery(query, Entry.class).build();
        adapterHome = new AdapterHome(entryFirestoreRecyclerOptions, this);
        adapterHome.notifyDataSetChanged();
        rv_home.setAdapter(adapterHome);

        return view;

    }

    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
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
    public void onStart() {
        super.onStart();
        adapterHome.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterHome.stopListening();
    }

}