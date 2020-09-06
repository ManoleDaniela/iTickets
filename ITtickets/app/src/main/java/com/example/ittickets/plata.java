package com.example.ittickets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class plata extends AppCompatActivity {
    TextView txtSuma;
    //String[] em;
    String Txt2,Txt1;

    String PretBilet ,TipBilet,LinieBilet;

    String userId, EmailTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private Button button;
    //String TipBilet;
//String LinieBilet;
    private EditText editTextDate;
    private EditText editTextNumber,editTextCVV,editTextPostal;
    private Button sendPostReqButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plata);

        txtSuma=findViewById(R.id.txtSuma);
        PretBilet=getIntent().getStringExtra("Pret");
        txtSuma.setText(PretBilet);

        //txtTip=findViewById(R.id.txtTip);
        TipBilet=getIntent().getStringExtra("Tip");
        //txtTip.setText(TipBilet);

        //txtLinie=findViewById(R.id.txtLinie);
        LinieBilet=getIntent().getStringExtra("Linie");
        //txtLinie.setText(LinieBilet);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        EditText editTextDate = (EditText) findViewById(R.id.editTextDate);
        //String strUserName =editTextDate.getText().toString();






        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                EmailTxt=documentSnapshot.getString("email");


                button=(Button) findViewById(R.id.button8);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nr = editTextNumber.getEditableText().toString();
                        String dataa= editTextDate.getEditableText().toString();


                        System.out.println("Nr :" + editTextNumber + " Data :" + editTextDate);
                        EditText editTextNume = (EditText) findViewById(R.id.editTextNume);
                        String Nume =editTextNume.getText().toString();

                        if(TextUtils.isEmpty(Nume)){
                            editTextNume.setError("Completati numele!");
                            return;
                        }

                        EditText editTextNumber = (EditText) findViewById(R.id.editTextNumber);
                        if(TextUtils.isEmpty(nr)){
                            editTextNumber.setError("Completati numarul cardului!");
                            return;
                        }

                        if(TextUtils.isEmpty(dataa)){
                            editTextDate.setError("Completati data!");
                            return;
                        }





                        EditText editTextCVV = (EditText) findViewById(R.id.editTextCVV);
                        String CVV =editTextCVV.getText().toString();

                        if(TextUtils.isEmpty(CVV)){
                            editTextCVV.setError("Completati CVV!");
                            return;
                        }

                        EditText editTextPostal = (EditText) findViewById(R.id.editTextPostal);
                        String Postal =editTextNume.getText().toString();

                        if(TextUtils.isEmpty(Postal)){
                            editTextPostal.setError("Completati codul postal!");
                            return;
                        }



                        sendPostRequest(nr, dataa);


                    }
                });


            }
        });






    }



    public void openMain(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);

    }


    public void writeFile(String linie,String tip,String pret){

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

        tot= salveazatxt + id+ "@" + linie + "@" + tip + "@" + pret + "\n";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt1,MODE_PRIVATE);

            fileOutputStream.write( tot.getBytes());
            fileOutputStream.close();

        }catch (FileNotFoundException e){


            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private String creeaza() {
        String salveaza="";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt1 ,MODE_PRIVATE);

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


    public void writeIstoricFile(String linie,String tip,String pret){

        String data= getCurrentDate();


        String salveazatxt="";
        Txt2=EmailTxt +"Istoric" + ".txt";


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

        tot= "Ai cumparat"  + "@" + data +"@" + linie + "@" + tip + "@" + pret + "\n" + salveazatxt ;
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt2,MODE_PRIVATE);

            fileOutputStream.write( tot.getBytes());
            fileOutputStream.close();

        }catch (FileNotFoundException e){


            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private String creeaza2() {
        String salveaza="";
        try{

            FileOutputStream fileOutputStream=openFileOutput(Txt2,MODE_PRIVATE);

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



    private void sendPostRequest(String Nr, String Data) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String paramNr = params[0];
                String paramData = params[1];


                System.out.println("*** doInBackground ** paramNr " + paramNr + " paramData :" + paramData);

                HttpClient httpClient = new DefaultHttpClient();



                HttpPost httpPost = new HttpPost("https://web.xn--ciuc-3sa.ro/AO/Licenta/index.php");


                BasicNameValuePair nrBasicNameValuePair = new BasicNameValuePair("paramNr", paramNr);
                BasicNameValuePair dataBasicNameValuePAir = new BasicNameValuePair("paramData", paramData);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(nrBasicNameValuePair);
                nameValuePairList.add(dataBasicNameValuePAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);


                    httpPost.setEntity(urlEncodedFormEntity);

                    try {

                        HttpResponse httpResponse = httpClient.execute(httpPost);


                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("First Exception caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Second Exception caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (UnsupportedEncodingException e) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + e);
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if(result.equals("card valid")){
                    {
                        Toast.makeText(getApplicationContext(), "Plata efectuala cu succes!", Toast.LENGTH_LONG).show();
                        writeFile(LinieBilet, TipBilet, PretBilet);
                        writeIstoricFile(LinieBilet, TipBilet, PretBilet);
                        openMain();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Card invalid...", Toast.LENGTH_LONG).show();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Nr, Data);
    }


}