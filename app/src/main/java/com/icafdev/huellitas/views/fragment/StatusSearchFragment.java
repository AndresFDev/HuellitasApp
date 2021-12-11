package com.icafdev.huellitas.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.adapters.AdapterHome;
import com.icafdev.huellitas.models.firebase.ConstantFB;
import com.icafdev.huellitas.models.firebase.Entry;

public class StatusSearchFragment extends Fragment {

    View view;

    private RecyclerView rv_search;
    private AdapterHome adapterHome;
    private CollectionReference collectionReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_status_search, container, false);

        collectionReference = FirebaseFirestore.getInstance().collection(ConstantFB.ENTRIES);

        rv_search = view.findViewById(R.id.rv_search);

        rv_search.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Query query = collectionReference.whereEqualTo("status", "Buscado");
        FirestoreRecyclerOptions<Entry> entryFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Entry>().setQuery(query, Entry.class).build();
        adapterHome = new AdapterHome(entryFirestoreRecyclerOptions, this);
        adapterHome.notifyDataSetChanged();
        rv_search.setAdapter(adapterHome);

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