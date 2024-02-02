package com.example.animalapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalapp.Model.Utente;
import com.example.animalapp.R;

import java.util.List;

public class Utente_Adapter extends RecyclerView.Adapter<Utente_Adapter.UtenteViewHolder> {
    final private Context mCtx;
    final private List<Utente> utenteList;

    public Utente_Adapter(Context mCtx, List<Utente> utenteList){
        this.mCtx = mCtx;
        this.utenteList = utenteList;
    }



    @NonNull
    @Override
    public UtenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_item, parent, false);
        return new UtenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UtenteViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return utenteList.size();
    }

    static public class UtenteViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeanimale;
        public TextView specieanimale;
        public ImageView image_profile;
        // public Button btn_follow;

        public UtenteViewHolder(@NonNull View itemView){
            super(itemView);

            nomeanimale = itemView.findViewById(R.id.nome_animale);
            specieanimale = itemView.findViewById(R.id.specie_animale);
            image_profile = itemView.findViewById(R.id.imageAvatar);
            //btn_follow = itemView.findViewById(R.id.btn_follow);


        }
    }
}
