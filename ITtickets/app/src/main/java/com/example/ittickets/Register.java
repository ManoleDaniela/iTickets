package com.example.ittickets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Register extends AppCompatActivity {
    EditText mnume, memail, mtel, mparola;
    Button mcreeazabtn ;
    TextView mtext1, mtext2, mcont;
    FirebaseAuth fAuth;
    ProgressBar progressBar3;
    FirebaseFirestore fStore;
    String userID,cod,cod2,cod1,cod3;
    String AES="AES";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mnume = findViewById(R.id.nume);
        memail =findViewById(R.id.email);
        mtel= findViewById(R.id.tel);
        mparola=findViewById(R.id.parola);
        mcreeazabtn=findViewById(R.id.creeazabtn);
        mtext1=findViewById(R.id.text1);
        mtext2=findViewById(R.id.text2);
        mcont=findViewById(R.id.cont);

        fAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        progressBar3 = findViewById(R.id.progressBar3);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            this.finish();
        }

        mcreeazabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=memail.getText().toString().trim();
                String parola=mparola.getText().toString().trim();
                final String nume=mnume.getText().toString().trim();
                final String tel=mtel.getText().toString().trim();



                if(TextUtils.isEmpty(email)){
                    memail.setError("Completati E-mail-ul!");
                    return;

                }
                if(TextUtils.isEmpty(parola)){
                    mparola.setError("Completati parola!");
                    return;
                }

                if(parola.length()<=6){
                    mparola.setError("Parola trebuie sa contina minim 6 caractere!");
                    return;
                }
                try {
                    cod = encrypt(mnume.getText().toString(), "paroladecriptare");
                    cod1=encrypt(mtel.getText().toString(),"paroladecriptare1");

                    //inregistrarea usersului
                }catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    cod2 = decrypt(cod, "paroladecriptare");
                    cod3 = decrypt(cod1, "paroladecriptare1");
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Register.this,"Cheie gresita",Toast.LENGTH_SHORT).show();
                }

                progressBar3.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Cont creat.", Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference =fStore.collection("users").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("nume",cod);
                            user.put("email",email);
                            user.put("tel",cod1);
                            user.put("decriptNume",cod2);
                            user.put("decriptTel",cod3);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d(  "TAG","onSucces:user profile is created for "+userID);

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Register.class));

                        }else{
                            Toast.makeText(Register.this, "Eroare" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();}
                        progressBar3.setVisibility(View.GONE);
                    }
                });

            }
        });

        mcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });
    }

    private String decrypt(String cod, String cheie) throws Exception {
        SecretKeySpec key=generateKey(cheie);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(cod,Base64.DEFAULT);
        byte[] decValue =c.doFinal(decodedValue);
        String decryptedValue=new String(decValue);
        return decryptedValue;
    }

    private String encrypt(String date, String cheie) throws Exception {
        SecretKeySpec key=generateKey(cheie);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(date.getBytes());
        String encryptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String cheie) throws  Exception {
        final MessageDigest digest =MessageDigest.getInstance("SHA-256");
        byte[] bytes =cheie.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte [] key=digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
}
