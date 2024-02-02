package com.example.animalapp.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.example.animalapp.Adapter.Segnalazioni_Adapter;
import com.example.animalapp.MainActivity;
import com.example.animalapp.Home_Ente_Activity;
import com.example.animalapp.Home_Veterinario_Activity;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.animalapp.Model.Segnalazioni;
import com.example.animalapp.Model.Utente;
import com.example.animalapp.R;
import com.example.animalapp.Recycler_Item_click_Listener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Veterinario_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private Segnalazioni_Adapter segnalazioniAdapter;
    FloatingActionButton floatingButtonNuovaSegnalazione;
    //Fragment selectedFragment=null;
    private List<Segnalazioni> mSegnalazioni;
    FirebaseDatabase db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Utente utente;

    private DatabaseReference reffSegnalazioni;



    private ArrayList<Segnalazioni> segnalazioni;



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mSegnalazioni.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Segnalazioni segnalazioni = snapshot.getValue(Segnalazioni.class);
                    mSegnalazioni.add(segnalazioni);
                }
                segnalazioniAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_per_te_veterinario, container, false);
        Activity activity = getActivity();
        if (activity instanceof Home_Veterinario_Activity) {
            ((Home_Veterinario_Activity) activity).setCustomBackEnabled(true);
        } else if (activity instanceof Home_Ente_Activity) {
            ((MainActivity) activity).setCustomBackEnabled(true);
        }else if (activity instanceof MainActivity) {
            ((MainActivity) activity).setCustomBackEnabled(true);
        }



        recyclerView = view.findViewById(R.id.recycler_view_veterinario);
        floatingButtonNuovaSegnalazione = view.findViewById(R.id.btn_nuova_segnalazione);

//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

/*
       // mSegnalazioni = new ArrayList<>();
        segnalazioniAdapter = new Segnalazioni_Adapter(this.getContext(), mSegnalazioni);

        segnalazioni = new ArrayList<>();


        recyclerView.setAdapter(segnalazioniAdapter);

 */

        segnalazioni = new ArrayList<>();
        //loadSegnalazioni();

       // db= FirebaseDatabase.getInstance();


      /*  FirebaseDatabase db = FirebaseDatabase.getInstance();
        Query queryUtente = db.getReference("Users").orderByChild("Id").equalTo(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        queryUtente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot tmpsnapshot : snapshot.getChildren()) {
                        utente = tmpsnapshot.getValue(Utente.class);

                    }
                }

                switch (utente.TipoUtente){
                    case "EntePubblico":
                        Query queryEnte = db.getReference("Segnalazioni").orderByChild("destinatarioEnte").equalTo("si");
                        queryEnte.addValueEventListener(valueEventListener);
                        break;
                    case "Utente Amico":
                        Query queryUtenteAmico = db.getReference("Segnalazioni").orderByChild("destinatarioUtente").equalTo("si");
                        queryUtenteAmico.addValueEventListener(valueEventListener);
                        break;
                    case "Veterinario":
                        Query queryVeterinario = db.getReference("Segnalazioni").orderByChild("destinatarioVeterionario").equalTo("si");
                        queryVeterinario.addValueEventListener(valueEventListener);
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




       */

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //BOTTONE NUOVA SEGNALAZIONE
        floatingButtonNuovaSegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rendiInvisibileView();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Aggiungi_Segnalazione_Fragment(utente)).addToBackStack(null).commit();


            }
        });

        //CLICK ITEM RECYCLERVIEW
        recyclerView.addOnItemTouchListener(
                new Recycler_Item_click_Listener(getContext(), recyclerView ,new Recycler_Item_click_Listener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //rendiInvisibileView();
                        Segnalazioni tmp = mSegnalazioni.get(position);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new DettagliSegnalazioni_Fragment(tmp,utente)).addToBackStack(null).commit();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void rendiVisibileView(){
        recyclerView.setVisibility(View.VISIBLE);
        floatingButtonNuovaSegnalazione.setVisibility(View.VISIBLE);
    }

    private void rendiInvisibileView(){
        recyclerView.setVisibility(View.INVISIBLE);
        floatingButtonNuovaSegnalazione.setVisibility(View.INVISIBLE);
    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Sei sicuro di voler effettuare il logout?");
        builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Effettua il logout da Firebase
                FirebaseAuth.getInstance().signOut();

                // Chiudi l'activity o esegui altre azioni di logout se necessario
                requireActivity().finish();
            }
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void loadSegnalazioni() {

        /* la lista viene pulita poiche altrimenti ogni volta ce si ricarica la pagina
         *  verrebbero aggiunti gli stessi segalazioni */
       /* if (!segnalazioni.isEmpty()){
            segnalazioni.clear();
        }

        */

        reffSegnalazioni = FirebaseDatabase.getInstance().getReference().child("Segnalazioni");

        reffSegnalazioni.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.

                Log.d("seg", snapshot.toString());

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Segnalazioni seg = snap.getValue(Segnalazioni.class);

                    segnalazioni.add(seg);

                }

                segnalazioniAdapter = new Segnalazioni_Adapter(getContext(), segnalazioni);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(segnalazioniAdapter);

                // after getting the value we are setting
                // our value to our text view in below line.
                //retrieveTV.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


       // reffSegnalazioni.addValueEventListener(postListener);

        //homeSwipeRefresh.setRefreshing(false);




}