package com.example.ittickets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class Cont extends AppCompatActivity {
    TextView txtNume,txtEmail,txtTel;
    //EditText mnume, memail, mtel;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    public Cont() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont);


       txtNume = findViewById(R.id.txtNume);
        //nume=getIntent().getStringExtra("nume");
       // txtNume.setText(nume);

        txtEmail =findViewById(R.id.txtEmail);
        txtTel= findViewById(R.id.txtTel);

    fAuth=FirebaseAuth.getInstance();
    fStore=FirebaseFirestore.getInstance();
    userId=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                txtNume.setText(documentSnapshot.getString("decriptNume"));
                txtEmail.setText(documentSnapshot.getString("email"));
                txtTel.setText(documentSnapshot.getString("decriptTel"));
            }
        });
    }




    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LogIn.class));

        finish();
    }
}
