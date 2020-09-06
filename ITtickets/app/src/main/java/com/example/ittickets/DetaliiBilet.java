package com.example.ittickets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ittickets.nfc.OutcomingNfcManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DetaliiBilet extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {
    TextView id2,linie2,tip2,pret2;
    String PretBilet ,TipBilet,LinieBilet,IdBilet;
    String bilet="";
    String userId, EmailTxt,Txt2;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private Button button;
    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;
    String ok="nu",operatie="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalii_bilet);

        id2=findViewById(R.id.id2);
        IdBilet=getIntent().getStringExtra("Id");
        id2.setText("Id" + IdBilet);

        linie2=findViewById(R.id.linie2);
        LinieBilet=getIntent().getStringExtra("Linie");
        linie2.setText("Linia: " + LinieBilet);

        tip2=findViewById(R.id.tip2);
        TipBilet=getIntent().getStringExtra("Tip");
        tip2.setText("Tip: " + TipBilet);

        pret2=findViewById(R.id.pret2);
        PretBilet=getIntent().getStringExtra("Pret");
        pret2.setText("Pret: " + PretBilet);

        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc nu este suportat pe acest dispozitiv", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC-ul este oprit, va rugam sa-l porniti", Toast.LENGTH_SHORT).show();
        }

        //  initViews();

        // Toast.makeText(this, bilet, Toast.LENGTH_SHORT).show();
        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);

        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);

        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);



        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                EmailTxt=documentSnapshot.getString("email");





                        button=(Button) findViewById(R.id.foloseste);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bilet=IdBilet;
                                operatie="Ai folosit";
                              //  Toast.makeText(DetaliiBilet.this, ok, Toast.LENGTH_SHORT).show();
                              //  writeIstoricFile1(LinieBilet,TipBilet,PretBilet);
                             //   deleteFile();
                               // openMain();
                            }
                        });

                button=(Button) findViewById(R.id.trimite);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        bilet=IdBilet + "@" + LinieBilet +"@" + TipBilet + "@" + PretBilet;
                        operatie="Ai trimis";

                      //  Toast.makeText(DetaliiBilet.this, "Setat", Toast.LENGTH_SHORT).show();

                       // if(ok.equals("da")) {
                        //    Toast.makeText(DetaliiBilet.this, "Sterge", Toast.LENGTH_SHORT).show();

                            // openMain();
                       // }
                    }
                });
               // Toast.makeText(DetaliiBilet.this, ok, Toast.LENGTH_SHORT).show();
               // writeIstoricFile1(LinieBilet,TipBilet,PretBilet);
              //  deleteFile();


                    }
                });


            }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Toast.makeText(this, "Da1", Toast.LENGTH_SHORT).show();

        setIntent(intent);
    }


    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        return this.nfcAdapter != null;

    }


    public void deleteFile() {
        EmailTxt=EmailTxt + ".txt";
        String id = "",lines="";
        String linie = "";
        String tip = "";
        String pret = "",bun="",bun2="",linii_bune="";
        String id_sters="6";
        String temp1="",temp2="nu";
        String[] parts;


        try {

            FileInputStream fileInputStream = openFileInput(EmailTxt);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            while ((lines = bufferedReader.readLine()) != null) {

                linii_bune=lines;
                parts = linii_bune.split("@");

                if(!(parts[0].equals(IdBilet)))
                    bun=bun + lines + "\n";
                //if(!(lines.startsWith("3/")))
                //  bun=bun + "," +  parts[0] + "," + "\n";

            }


            //scrierea dupa ce a fost sters
            try{

                FileOutputStream fileOutputStream=openFileOutput(EmailTxt,MODE_PRIVATE);
              //  Toast.makeText(getApplicationContext(),"Merge",Toast.LENGTH_SHORT).show();
                fileOutputStream.write( bun.getBytes());
                fileOutputStream.close();

            }catch (FileNotFoundException e){
                //Toast.makeText(getApplicationContext(),"Nu Merge" + Environment.getExternalStorageDirectory().toString(),Toast.LENGTH_SHORT).show();
                // getFileStreamPath("new.xml");
              //  Toast.makeText(getApplicationContext(),"nu Merge  " + getFileStreamPath(EmailTxt),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }



            inputStreamReader.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void openMain(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Plata efectuata cu succes",Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getOutcomingMessage() {
        return this.bilet;
    }

    @Override
    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
                // should be triggered on UI thread. We specify it explicitly
                // cause onNdefPushComplete is called from the Binder thread
                        runOnUiThread(() ->
                        Toast.makeText(DetaliiBilet.this, "Trimiterea a fost realizata", Toast.LENGTH_SHORT).show());
                         ok="da";
            if(operatie.equals("Ai trimis"))
            {

                writeIstoricFile1(LinieBilet,TipBilet,PretBilet);
                deleteFile();
                openMain();
            }else if(operatie.equals("Ai folosit"))
            {
                writeIstoricFile2(LinieBilet,TipBilet,PretBilet);
                deleteFile();
                openMain();
            }
    }


    public void writeIstoricFile1(String linie,String tip,String pret){
//        Toast.makeText(DetaliiBilet.this, "Da1", Toast.LENGTH_SHORT).show();
        String data= getCurrentDate();


        String salveazatxt="";
        Txt2=EmailTxt +"Istoric" + ".txt";
     //   Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();

        //mai intai citeste

        try{

            FileInputStream fileInputStream=openFileInput(Txt2);

            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();

            String lines;
            while((lines=bufferedReader.readLine()) !=null){
                stringBuffer.append(lines + "\n");
            }

            salveazatxt=stringBuffer.toString();

        }catch (FileNotFoundException e){
            salveazatxt=creeaza2();
            // e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        String tot;
        String id= UUID.randomUUID().toString() ;
        String[] parts;
        //parts = msj.split("@");
        tot= operatie  + "@" + data +"@" + linie + "@" + tip + "@" + pret + "\n" + salveazatxt ;
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt2,MODE_PRIVATE);
            //  Toast.makeText(getApplicationContext(),salveazatxt,Toast.LENGTH_SHORT).show();
            fileOutputStream.write( tot.getBytes());
            fileOutputStream.close();

        }catch (FileNotFoundException e){

            //  Toast.makeText(getApplicationContext(),"nu Merge  " + getFileStreamPath(Txt2),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void writeIstoricFile2(String linie,String tip,String pret){

        String data= getCurrentDate();


        String salveazatxt="";
        Txt2=EmailTxt +"Istoric" + ".txt";
      //  Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();

        //mai intai citeste

        try{

            FileInputStream fileInputStream=openFileInput(Txt2);

            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();

            String lines;
            while((lines=bufferedReader.readLine()) !=null){
                stringBuffer.append(lines + "\n");
            }

            salveazatxt=stringBuffer.toString();

        }catch (FileNotFoundException e){
            salveazatxt=creeaza2();
            // e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        String tot;
        String id= UUID.randomUUID().toString() ;
        String[] parts;
        //parts = msj.split("@");
        tot= operatie  + "@" + data +"@" + linie + "@" + tip + "@" + pret + "\n" + salveazatxt ;
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt2,MODE_PRIVATE);
            //  Toast.makeText(getApplicationContext(),salveazatxt,Toast.LENGTH_SHORT).show();
            fileOutputStream.write( tot.getBytes());
            fileOutputStream.close();

        }catch (FileNotFoundException e){

            //  Toast.makeText(getApplicationContext(),"nu Merge  " + getFileStreamPath(Txt2),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private String creeaza2() {
        String salveaza="";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt2,MODE_PRIVATE);
            //  Toast.makeText(getApplicationContext(),"da?",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        try {

            FileInputStream fileInputStream = openFileInput(Txt2);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
            }

            salveaza = stringBuffer.toString();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return salveaza;

    }


    public static final String DATE_FORMAT= "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }



}


















