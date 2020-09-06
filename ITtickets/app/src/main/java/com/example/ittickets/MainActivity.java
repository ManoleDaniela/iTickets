package com.example.ittickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button button;
    EditText mnume, memail, mtel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mnume = findViewById(R.id.nume);
        memail =findViewById(R.id.email);
        mtel= findViewById(R.id.tel);

        button=(Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBilete();
            }
        });

        button=(Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMesaje();
            }
        });

        button=(Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCont();
            }
        });


    }

    public void openBilete(){
        Intent intent=new Intent(this, Bilete.class);
        startActivity(intent);
    }
    public void openMesaje(){
        Intent intent=new Intent(this, Mesaje.class);
        startActivity(intent);
    }

    public void openCont(){
        Intent intent=new Intent(this, Cont.class);
        startActivity(intent);
    }



}
