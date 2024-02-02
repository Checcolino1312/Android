package com.example.animalapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalapp.Model.Segnalazioni;
import com.example.animalapp.Model.Utente;
import com.example.animalapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InCarico_Adapter extends RecyclerView.Adapter<InCarico_Adapter.SegnalazioniViewHolder> {
    final private Context mCtx;
    final private List<Segnalazioni> segnalazioniList;
    static Button btn_follow;

    FirebaseUser firebaseUser;
    CoordinatorLayout coordinatorLayout;



    Utente utente;



    public InCarico_Adapter(Context mCtx, List<Segnalazioni> segnalazioniList){
        this.mCtx = mCtx;
        this.segnalazioniList = segnalazioniList;
    }

    @NonNull
    @Override
    public SegnalazioniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.incarico_item,parent,false);
        return new SegnalazioniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SegnalazioniViewHolder holder, int position) {
        final Segnalazioni segnalazioni = segnalazioniList.get(position);

        //final Animali animali = animalList.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        utente = new Utente();





        holder.descrizione.setText(segnalazioni.descrizione);
        holder.tipologiaSegnalazione.setText(segnalazioni.tipologiaSegnalazione);
        trovaNomeCognomeUtente(segnalazioni.idMittente, holder.mittente);


        Log.d("segnalazioni", segnalazioni.getId());


    }

    @Override
    public int getItemCount() {
        return segnalazioniList.size();
    }


    static class SegnalazioniViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tipologiaSegnalazione,descrizione,mittente;

        List<Segnalazioni> segnalazioniList;
        static Button btn_follow;



        public SegnalazioniViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            mittente =  itemView.findViewById(R.id.txt_email);
            descrizione =  itemView.findViewById(R.id.txt_città);
            tipologiaSegnalazione =  itemView.findViewById(R.id.txt_tipo_segnalazione);
            btn_follow = itemView.findViewById(R.id.follow_segnalazioni);
        }
    }

    private void trovaNomeCognomeUtente(String id, TextView mittente){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Query query = db.getReference("Users").orderByChild("Id").equalTo(id);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Utente utentetmp = snapshot.getValue(Utente.class);
                        mittente.setText(utentetmp.Nome+" "+utentetmp.Cognome);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);


    }


}