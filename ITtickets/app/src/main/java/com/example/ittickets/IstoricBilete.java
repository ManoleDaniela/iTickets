package com.example.ittickets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class IstoricBilete extends AppCompatActivity {
    private RecyclerView mRvData;
    private BileteRecyclerAdapter allDataAdapter;
    // private DatabaseReference mDatabase;
    // private FirebaseDatabase mFirebaseInstance;
    IstoricBileteRecyclerAdapter adapter;
    ArrayList<BiletIstoric> bilete = new ArrayList<>();
    String userId, EmailTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istoric_bilete);
        // Button b = findViewById(R.id.button);
       /* b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBilete();
            }
        });

        */
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                EmailTxt = documentSnapshot.getString("email");
                getBiletIstoric();
                refresh();
            }
        });




            }


            private void refresh() {
                final SwipeRefreshLayout swipeContainer = IstoricBilete.this.findViewById(R.id.refresh_bilete3);
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adapter.getLista_bilete().clear();
                        getBiletIstoric();
                        // Log.e("TEST", "DUPA GET BILETE");
                        swipeContainer.setRefreshing(false);
                    }
                });
            }

            private void getBiletIstoric() {
                // writeFile2();
                EmailTxt = EmailTxt +"Istoric.txt" ;
                mRvData = findViewById(R.id.recycler_view4);
                mRvData.setLayoutManager(new LinearLayoutManager(IstoricBilete.this.getApplicationContext()));
                final ArrayList<BiletIstoric> bilete = new ArrayList<>();


                String id = "", lines = "", data = "";
                String linie = "";
                String tip = "";
                String pret = "", bun = "", bun2 = "", linii_bune = "";
                String id_sters = "6";
                String temp1 = "", temp2 = "nu";
                String[] parts;


                try {

                    FileInputStream fileInputStream = openFileInput(EmailTxt);

                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();

                    while ((lines = bufferedReader.readLine()) != null) {

                        linii_bune = lines;
                        //  bun2=linii_bune;

                        parts = linii_bune.split("@");
                        BiletIstoric b = new BiletIstoric(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        bilete.add(b);
                        adapter = new IstoricBileteRecyclerAdapter(IstoricBilete.this.getApplicationContext(), bilete);
                        mRvData.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        // bun = bun + lines + "\n";
                    }
                    inputStreamReader.close();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }





  /*  BiletulMeu b = new BiletulMeu("1","linia 48","o calatorie","1,3 lei");
        bilete.add(b);
        adapter = new BileteleMeleRecyclerAdapter(BileteleMele.this.getApplicationContext(), bilete);
        mRvData.setAdapter(adapter);
        adapter.notifyDataSetChanged();


   */
            }

            public void writeFile2() {
                String text = "dana";
                try {

                    FileOutputStream fileOutputStream = openFileOutput(EmailTxt, MODE_PRIVATE);

                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
                    writer.println("1@Linia 48@O calatorie@1,3 lei");
                    // writer.println("Linia2");
                    writer.close();


                } catch (FileNotFoundException e) {

                    Toast.makeText(getApplicationContext(), "nu Merge  " + getFileStreamPath(EmailTxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }


        }