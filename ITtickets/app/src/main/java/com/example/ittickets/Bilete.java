package com.example.ittickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Bilete extends AppCompatActivity {
private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilete);

        button=(Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCumpara_bilet();
            }
        });

        button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBileteleMele();
            }
        });

        button=(Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIstoricBilete();
            }
        });


    }



    public void openCumpara_bilet(){
        Intent intent=new Intent(this, Cumpara_bilet.class);
        startActivity(intent);
    }

    public void openBileteleMele(){
        Intent intent=new Intent(this, BileteleMele.class);
        startActivity(intent);}


    public void openIstoricBilete(){
        Intent intent=new Intent(this, IstoricBilete.class);
        startActivity(intent);}
}
