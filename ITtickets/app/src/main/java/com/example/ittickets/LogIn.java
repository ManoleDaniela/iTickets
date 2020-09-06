package com.example.ittickets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    EditText me, mp;
    Button mintra;
    TextView mtext1, mtext2, mtext,textView4;
    FirebaseAuth fAuth;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        me = findViewById(R.id.e);
        mp = findViewById(R.id.p);
        mintra = findViewById(R.id.intra);
        mtext1 = findViewById(R.id.text1);
        mtext2 = findViewById(R.id.text2);
        mtext = findViewById(R.id.text);

        fAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);
        textView4=findViewById(R.id.textView4);

        mintra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = me.getText().toString().trim();
                String p = mp.getText().toString().trim();

                if (TextUtils.isEmpty(e)) {
                    me.setError("Completati E-mail-ul!");
                    return;

                }
                if (TextUtils.isEmpty(p)) {
                    mp.setError("Completati parola!");
                    return;
                }

                if (p.length() <= 6) {
                    mp.setError("Parola trebuie sa contina minim 6 caractere!");
                    return;
                }


                progressBar2.setVisibility(View.VISIBLE);

                //auth user

                fAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogIn.this, "Ati intrat in cont.", Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));


                        } else {
                            Toast.makeText(LogIn.this, "Eroare" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }


                });

            }
        });
        mtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Resetati parola?");
                passwordResetDialog.setMessage("Introdu Emailul pentru a primi linkul de resetare");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
String mail= resetMail.getText().toString();
fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        Toast.makeText(LogIn.this, "Linkul a fost trimis", Toast.LENGTH_SHORT).show();
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(LogIn.this, "Linkul nu a putut fi trimis" +e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});


                    }
                });

                passwordResetDialog.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}