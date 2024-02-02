package com.example.animalapp.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animalapp.Adapter.Preferiti_Adapter;
import com.example.animalapp.Adapter.Segnalazioni_Adapter;
import com.example.animalapp.Home_Ente_Activity;
import com.example.animalapp.Home_Veterinario_Activity;
import com.example.animalapp.MainActivity;
import com.example.animalapp.Model.Follow;
import com.example.animalapp.Model.Segnalazioni;
import com.example.animalapp.Model.Utente;
import com.example.animalapp.R;
import com.example.animalapp.Recycler_Item_click_Listener;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Segnalazioni_Fragment extends Fragment {
    //Segnalazioni segnalazioni;
    Utente utente;

    private RecyclerView segnalazioniRecyclerView;

    FloatingActionButton floatingButtonNuovaSegnalazione;
    private Segnalazioni_Adapter segnalazioniAdapter;
    private List<Segnalazioni> mSegnalazioni;

    private List<Follow> listAnimal;

    FirebaseDatabase db;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseUser firebaseUser;
    private DatabaseReference reffSegnalazioni;

    DatabaseReference reference;
    private ArrayList<Segnalazioni> segnalazioni;

    //private Preferiti_Adapter prefAdapter;


    public Segnalazioni_Fragment(){

    }

   /* Segnalazioni_Fragment(Segnalazioni s, Utente u){
        this.segnalazioni=s;
        this.utente=u;
    }

    */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_per_te_veterinario, container, false);

        Activity activity = getActivity();
        if (activity instanceof Home_Veterinario_Activity) {
            ((Home_Veterinario_Activity) activity).setCustomBackEnabled(false);
        } else if (activity instanceof Home_Ente_Activity) {
            ((Home_Ente_Activity) activity).setCustomBackEnabled(false);
        }else if (activity instanceof MainActivity) {
            ((MainActivity) activity).setCustomBackEnabled(false);
        }

        segnalazioniRecyclerView = view.findViewById(R.id.recycler_view_veterinario);
        floatingButtonNuovaSegnalazione = view.findViewById(R.id.btn_nuova_segnalazione);

//        segnalazioniRecyclerView.setHasFixedSize(true);
        segnalazioniRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        segnalazioni = new ArrayList<>();
        loadSegnalazioni();



        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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
     /*   segnalazioniRecyclerView.addOnItemTouchListener(
                new Recycler_Item_click_Listener(getContext(), segnalazioniRecyclerView ,new Recycler_Item_click_Listener.OnItemClickListener() {
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

      */

    }

    @Override
    public void onResume() {
        super.onResume();
       /* btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(segnalazioni.lattitudine!=0 && segnalazioni.longitudine!=0){
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", segnalazioni.lattitudine, segnalazioni.longitudine);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Nessuna Posizione", Toast.LENGTH_SHORT).show();
                }

            }
        });

        */
    }



    public void loadSegnalazioni() {

        /* la lista viene pulita poiche altrimenti ogni volta ce si ricarica la pagina
         *  verrebbero aggiunti gli stessi segalazioni */
        if (!segnalazioni.isEmpty()){
            segnalazioni.clear();
        }



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
                segnalazioniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                segnalazioniRecyclerView.setItemAnimator(new DefaultItemAnimator());
                segnalazioniRecyclerView.setAdapter(segnalazioniAdapter);






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

}