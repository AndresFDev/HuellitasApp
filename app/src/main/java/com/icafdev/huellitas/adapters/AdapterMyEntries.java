package com.icafdev.huellitas.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.icafdev.huellitas.R;
import com.icafdev.huellitas.models.firebase.Entry;
import com.icafdev.huellitas.views.DetailEntryActivity;
import com.squareup.picasso.Picasso;

public class AdapterMyEntries extends FirestoreRecyclerAdapter<Entry, AdapterMyEntries.ViewHolder> {

    private Fragment context;

    public AdapterMyEntries(FirestoreRecyclerOptions<Entry> options , Fragment context) {
        super(options);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Entry entry) {

        Picasso.get()
                .load(entry.getPhoto())
                .placeholder(R.drawable.ic_default_img)
                .into(holder.iv_photo);

        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getContext(), DetailEntryActivity.class);

                intent.putExtra("id", entry.getId());
                intent.putExtra("Foto", entry.getPhoto());
                intent.putExtra("Nombre", entry.getName());
                intent.putExtra("Teléfono", entry.getPhone());
                intent.putExtra("Estado", entry.getStatus());
                intent.putExtra("Nombre mascota", entry.getPet_name());
                intent.putExtra("Tipo", entry.getType());
                intent.putExtra("Raza", entry.getRaza());
                intent.putExtra("Genero", entry.getGen());
                intent.putExtra("Fecha", entry.getDate());
                intent.putExtra("Ciudad", entry.getCity());
                intent.putExtra("Dirección", entry.getAddress());
                intent.putExtra("Descripción", entry.getDescription());
                intent.putExtra("UserId", entry.getUserId());
                intent.putExtra("UserName", entry.getUserName());
                intent.putExtra("UserPhoto", entry.getUserPhoto());

                context.startActivity(intent);
            }
        });

        holder.tv_status.setText(entry.getStatus());
        holder.tv_pet_name.setText(entry.getPet_name());
        holder.type = entry.getType();

        if (holder.tv_status.getText().toString().equals("Buscado")){
            holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e1919")));
        }else {
            if (holder.tv_status.getText().toString().equals("Encontrado")){
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF339567")));
            }else {
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3c75c2")));
            }
        }

        if (holder.type != null) {
            if (holder.type.equals("Perro")) {
                holder.iv_type.setBackgroundResource(R.drawable.ic_dog);
            } else {
                holder.iv_type.setBackgroundResource(R.drawable.ic_cat);
            }
        }

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.b_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getContext(), DetailEntryActivity.class);

                intent.putExtra("id", entry.getId());
                intent.putExtra("Foto", entry.getPhoto());
                intent.putExtra("Nombre", entry.getName());
                intent.putExtra("Teléfono", entry.getPhone());
                intent.putExtra("Estado", entry.getStatus());
                intent.putExtra("Nombre mascota", entry.getPet_name());
                intent.putExtra("Tipo", entry.getType());
                intent.putExtra("Raza", entry.getRaza());
                intent.putExtra("Genero", entry.getGen());
                intent.putExtra("Fecha", entry.getDate());
                intent.putExtra("Ciudad", entry.getCity());
                intent.putExtra("Dirección", entry.getAddress());
                intent.putExtra("Descripción", entry.getDescription());
                intent.putExtra("UserId", entry.getUserId());
                intent.putExtra("UserName", entry.getUserName());
                intent.putExtra("UserPhoto", entry.getUserPhoto());

                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_my_entries, viewGroup, false);
        return new ViewHolder(view);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private String type;
        private TextView tv_status, tv_pet_name;
        private ImageView iv_photo, iv_type;
        private MaterialButton b_detail;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            iv_photo = itemView.findViewById(R.id.iv_photo);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_pet_name = itemView.findViewById(R.id.tv_pet_name);
            iv_type = itemView.findViewById(R.id.iv_type);
            b_detail = itemView.findViewById(R.id.b_detail);

        }

    }

}