package com.example.animalapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animalapp.Model.Animali;
import com.example.animalapp.Model.Segnalazioni;
import com.example.animalapp.Model.Utente;
import com.example.animalapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class Segnalazioni_Adapter extends RecyclerView.Adapter<Segnalazioni_Adapter.SegnalazioniViewHolder> {
    final private Context mCtx;
    final private List<Segnalazioni> segnalazioniList;
    static Button btn_follow;

    FirebaseUser firebaseUser;

    Utente utente;



    public Segnalazioni_Adapter(Context mCtx, List<Segnalazioni> segnalazioniList){
        this.mCtx = mCtx;
        this.segnalazioniList = segnalazioniList;
    }

    @NonNull
    @Override
    public SegnalazioniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.segnalazioni_item,parent,false);
        return new SegnalazioniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SegnalazioniViewHolder holder, int position) {
        final Segnalazioni segnalazioni = segnalazioniList.get(position);

        //final Animali animali = animalList.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        utente = new Utente();


        if("si".equals(segnalazioni.presaInCarico)){
            String count = String.valueOf(getItemCount());
            Log.d("entrati", count);
            // Se l'animale appartiene al proprietario, rendi il pulsante invisibile
            SegnalazioniViewHolder.btn_follow.setVisibility(View.INVISIBLE);
        }else{
            SegnalazioniViewHolder.btn_follow.setVisibility(View.VISIBLE);
        }







        holder.descrizione.setText(segnalazioni.descrizione);
        holder.tipologiaSegnalazione.setText(segnalazioni.tipologiaSegnalazione);
        trovaNomeCognomeUtente(segnalazioni.idMittente, holder.mittente);





        String count = String.valueOf(getItemCount());
        Log.d("count", count);

        //following tiene traccia degli animali che l'utente sta seguendo
        //followers tiene traccia degli utenti che seguono l'animale
        SegnalazioniViewHolder.btn_follow.setOnClickListener(view -> {
            if(segnalazioni.presaInCarico.equals("no")){



                FirebaseDatabase.getInstance().getReference().child("Follow").setValue(firebaseUser.getUid());

                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("destinatario").setValue(firebaseUser.getUid());
                FirebaseDatabase.getInstance().getReference().child("Segnalazioni").child(firebaseUser.getUid()).child("idPresaInCarico").setValue(firebaseUser.getUid());



                FirebaseDatabase.getInstance().getReference().child("Segnalazioni").child(segnalazioni.getId()).child("destinatario").setValue(firebaseUser.getUid());

                FirebaseDatabase.getInstance().getReference().child("Segnalazioni").child(segnalazioni.getId()).child("presaInCarico").setValue("si");

              /*  FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(segnalazioni.getDestinatario()).child("id").setValue(segnalazioni.destinatario);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                        .child("following").child(segnalazioni.destinatario).child("nome").setValue(segnalazioni.descrizione);

               */

            }

        });


      /*  StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(segnalazioni.imgSegnalazione);
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(holder.img.getContext())
                .load(uri).circleCrop().into(holder.img));

       */

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

    private void isFollowing(String id, Button button){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Segnalazioni").child("destinatario");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()){
                    button.setText(R.string.segnalazioni_prese_in_carico);
                } else {
                    button.setText(R.string.inCarico);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}