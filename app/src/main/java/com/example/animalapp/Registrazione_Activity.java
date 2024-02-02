package com.example.animalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Registrazione_Activity extends AppCompatActivity {
    EditText email, password, nickname, cognome, nome;
    Button register;
    TextView txt_login;

    Spinner tipologia;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get the spinner from the xml.
        tipologia = findViewById(R.id.tipologia);
//create a list of items for the spinner.
        String[] items = new String[]{"Utente Amico", "Veterinario", "EntePubblico"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        tipologia.setAdapter(adapter);

        nickname = findViewById(R.id.name);
        nome = findViewById(R.id.firstName);
        cognome = findViewById(R.id.acognome);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(Registrazione_Activity.this);
                pd.setMessage("Caricamento...");
                pd.show();


                String typeuser = tipologia.getSelectedItem().toString();
                String strinsertname = nickname.getText().toString();
                String strnome = nome.getText().toString();
                String strcognome = cognome.getText().toString();
                String strEmail = email.getText().toString();
                String strPassword = password.getText().toString();

                if (typeuser.isEmpty() || typeuser == null) {
                    System.out.println("sono nel primo if");
                } else if (TextUtils.isEmpty(strEmail)) {
                    email.setError("Email Richiesta!!");
                } else if (TextUtils.isEmpty(strPassword)) {
                    password.setError("Password Richiesta!!");
                } else if (TextUtils.isEmpty(strinsertname)) {
                    nickname.setError("Nickname Richiesto!!");
                } else if (TextUtils.isEmpty(strnome)) {
                    nome.setError("Nome Richiesto!!");
                } else if (TextUtils.isEmpty(strcognome)) {
                    cognome.setError("Cognome Richiesto!!");
                } else if (password.length() < 6) {
                    password.setError("Password minimo 6 caratteri!!");
                } else {
                    register(typeuser, strinsertname, strnome, strcognome, strEmail, strPassword);
                }
            }
        });
    }

    private void register(String typeuser, String minsertname, String mnome, String mcognome, String memail, String mpassword) {
        auth.createUserWithEmailAndPassword(memail, mpassword).addOnCompleteListener(Registrazione_Activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Id", userid);
                    hashMap.put("TipoUtente", typeuser);
                    hashMap.put("Nickname", minsertname);
                    hashMap.put("Nome", mnome);
                    hashMap.put("Cognome", mcognome);
                    hashMap.put("ImgUrl", "gs://ioandroid-57364.appspot.com/images/circleako.png");
                    hashMap.put("Email", memail);
                    hashMap.put("Password", mpassword);

                    // REALTIME DATABASE
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://ioandroid-57364-default-rtdb.firebaseio.com/");
                    reference = database.getReference().child("Users").child(userid);
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                // Avvia l'attività corrispondente al tipo di utente
                                Intent intent;
                                switch (typeuser) {
                                    case "Utente Amico":
                                        intent = new Intent(Registrazione_Activity.this, MainActivity.class);
                                        break;
                                    case "Veterinario":
                                        intent = new Intent(Registrazione_Activity.this, Home_Veterinario_Activity.class);
                                        break;
                                    case "EntePubblico":
                                        intent = new Intent(Registrazione_Activity.this, Home_Ente_Activity.class);
                                        break;
                                    default:
                                        // Se il tipo di utente non corrisponde a nessuno dei casi, avvia l'attività principale
                                        intent = new Intent(Registrazione_Activity.this, MainActivity.class);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                    // CLOUDSTORAGE DATABASE
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("Users").document(userid).set(hashMap);
                    pd.dismiss();
                } else {
                    pd.dismiss();
                    Toast.makeText(Registrazione_Activity.this, "Non puoi registrarti con questa email o password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
