package com.example.ittickets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Cumpara_bilet extends AppCompatActivity {

    private RecyclerView mRvData;
    private BileteRecyclerAdapter allDataAdapter;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    BileteRecyclerAdapter adapter;
    ArrayList<Bilet> bilete = new ArrayList<>();




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cumpara_bilet);


       // Button b = findViewById(R.id.button);
       /* b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBilete();
            }
        });

        */
       getBilete();
        refresh();

    }


    private void refresh() {
        final SwipeRefreshLayout swipeContainer = Cumpara_bilet.this.findViewById(R.id.refresh_bilete);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.getLista_bilete().clear();
                getBilete();
                // Log.e("TEST", "DUPA GET BILETE");
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void getBilete() {
        mRvData = findViewById(R.id.recycler_view);
        mRvData.setLayoutManager(new LinearLayoutManager(Cumpara_bilet.this.getApplicationContext()));
        final ArrayList<Bilet> bilete = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bilete").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Bilet b = new Bilet(document.getData().get("linie_bilet").toString(), document.getData().get("tip_bilet").toString(),document.getData().get("pret_bilet").toString());
                        bilete.add(b);
                    }
                    adapter = new BileteRecyclerAdapter(Cumpara_bilet.this.getApplicationContext(), bilete);
                    mRvData.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                 //   Log.e("TEST", "SFARSIT GET BILETE");
                }
            }
        });
    }

    public void openplata(){
        Intent intent=new Intent(this, plata.class);
        startActivity(intent);
    }


}
