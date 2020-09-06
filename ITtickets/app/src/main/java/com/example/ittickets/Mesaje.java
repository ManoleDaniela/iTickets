package com.example.ittickets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class Mesaje extends AppCompatActivity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    String userId, EmailTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String Txt2,Txt1;
    String msj;
    TextView textView5;

    private Button button;
    private TextView bilet;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaje);

        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }
        this.textView5 = findViewById(R.id.textView5);
        this.textView5.setText("");

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                EmailTxt=documentSnapshot.getString("email");
                //em = EmailTxt.split("@");
                //  Toast.makeText(getApplicationContext(),EmailTxt,Toast.LENGTH_SHORT).show();

                button=(Button) findViewById(R.id.button6);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        //writeFile(LinieBilet,TipBilet,PretBilet);
                        writeIstoricFile();
                        writeFile();
                        Toast.makeText(Mesaje.this, "Biletul a fost adaugat in biletele mele", Toast.LENGTH_SHORT).show();
                        openBilete();




                    }
                });


            }
        });


    }

    public void openBilete(){
        Intent intent=new Intent(this, Bilete.class);
        startActivity(intent);

    }



    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter);

        receiveMessageFromDevice(getIntent());
    }



    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
           // Toast.makeText(this, "NUU5", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());
            this.textView5.setText(inMessage);
            msj=inMessage;

        }
    }

    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters


        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);

    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    public void writeIstoricFile(){

        String data= getCurrentDate();


        String salveazatxt="";
        Txt2=EmailTxt +"Istoric" + ".txt";
       // Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();

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
        parts = msj.split("@");
        tot= "Ai Primit"  + "@" + data +"@" + parts[1] + "@" + parts[2] + "@" + parts[3] + "\n" + salveazatxt ;
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

    public void writeFile(){
        //  Toast.makeText(getApplicationContext(),EmailTxt,Toast.LENGTH_SHORT).show();
        String salveazatxt="";
        Txt1=EmailTxt + ".txt";

        try{

            FileInputStream fileInputStream=openFileInput(Txt1);

            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();

            String lines;
            while((lines=bufferedReader.readLine()) !=null){
                stringBuffer.append(lines + "\n");
            }

            salveazatxt=stringBuffer.toString();

        }catch (FileNotFoundException e){
            salveazatxt=creeaza();
            // e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        String tot;
        String id= UUID.randomUUID().toString() ;

        tot= salveazatxt + msj + "\n";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt1,MODE_PRIVATE);
            //  Toast.makeText(getApplicationContext(),salveazatxt,Toast.LENGTH_SHORT).show();
            fileOutputStream.write( tot.getBytes());
            fileOutputStream.close();

        }catch (FileNotFoundException e){

           // Toast.makeText(getApplicationContext(),"nu Merge  " + getFileStreamPath(Txt1),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private String creeaza() {
        String salveaza="";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt1 ,MODE_PRIVATE);
            //  Toast.makeText(getApplicationContext(),"da?",Toast.LENGTH_SHORT).show();
            fileOutputStream.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        try {

            FileInputStream fileInputStream = openFileInput(Txt1);

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































