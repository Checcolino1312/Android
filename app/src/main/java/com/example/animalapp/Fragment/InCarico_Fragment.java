package com.example.animalapp.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.animalapp.Adapter.InCarico_Adapter;
import com.example.animalapp.Adapter.Utente_Adapter;
import com.example.animalapp.Home_Ente_Activity;
import com.example.animalapp.Home_Veterinario_Activity;
import com.example.animalapp.Model.Animali;

import com.example.animalapp.Model.Segnalazioni;
import com.example.animalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class  InCarico_Fragment extends Fragment {

    FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    //ANIMALE ADAPTER
    private List<Animali> mUtente;
    private Utente_Adapter adapter;

    CoordinatorLayout coordinatorLayout;

    TextView nullTextView;

    private ArrayList<Segnalazioni> segnalazionis;
    private DatabaseReference reffSegnalazioni;
    DatabaseReference dbUtente;

    private ArrayList<Segnalazioni> segnalazioni;

    //SEGNALAZIONI ADAPTER
    private InCarico_Adapter segnalazioniAdapter;

    com.getbase.floatingactionbutton.FloatingActionButton floatingButtonNuovaSegnalazione;
    private List<Segnalazioni> mSegnalazioni;
    DatabaseReference db;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    private RecyclerView elencosegnalazioni;


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

        View view = inflater.inflate(R.layout.fragment_in_carico_veterinario, container, false);
        Activity activity = getActivity();
        if (activity instanceof Home_Veterinario_Activity) {
            ((Home_Veterinario_Activity) activity).setCustomBackEnabled(true);
        } else if (activity instanceof Home_Ente_Activity) {
            ((Home_Ente_Activity) activity).setCustomBackEnabled(true);
        }


        //LOGICA SEGNALAZIONI ADAPTER
        recyclerView = view.findViewById(R.id.recycler_view_incarico);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        nullTextView = view.findViewById(R.id.textnull);

       /* mSegnalazioni = new ArrayList<>();
        segnalazioniAdapter = new Segnalazioni_Adapter(this.getContext(), mSegnalazioni);

        Query db = FirebaseDatabase.getInstance().getReference("Segnalazioni/destinatario").orderByChild("destinatario").equalTo(auth.getCurrentUser().getUid());
        db.addValueEventListener(valueEventListener);

        */

       // recyclerView.setAdapter(segnalazioniAdapter);

        /*Query db= FirebaseDatabase.getInstance().getReference("Segnalazioni").orderByChild("idPresaInCarico").equalTo(auth.getCurrentUser().getUid());
        db.addValueEventListener(valueEventListener);

         */

        segnalazionis = new ArrayList<>();
        loadSegnalazioni();

        inflater.inflate(R.layout.fragment_in_carico_veterinario, container, false);
        elencosegnalazioni = view.findViewById(R.id.recycler_view_incarico);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        elencosegnalazioni.setLayoutManager(layoutManager);




        // Inflate the layout for this fragment
        return view;
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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Segnalazioni segn = snap.getValue(Segnalazioni.class);

                    if ((segn.destinatario != null)){
                        if (segn.destinatario.equals(firebaseUser.getUid())){
                            segnalazionis.add(segn);
                            segnalazioniAdapter = new InCarico_Adapter(getContext(), segnalazionis);
                            elencosegnalazioni.setLayoutManager(new LinearLayoutManager(getActivity()));
                            elencosegnalazioni.setItemAnimator(new DefaultItemAnimator());
                            elencosegnalazioni.setAdapter(segnalazioniAdapter);
                            elencosegnalazioni.setAdapter(segnalazioniAdapter);
                        }
                        else {

                            if ((segn.destinatario == null)) {
                                nullTextView.setVisibility(View.VISIBLE);
                                nullTextView.setText("Non ci sono segnalazioni in carico");
                            }

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


}

