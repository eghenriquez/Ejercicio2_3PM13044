package com.example.ejercicio2_3pm13044;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnsalir,btncaptura,btnlista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsalir=(Button) findViewById(R.id.btnsalir);
        btncaptura=(Button) findViewById(R.id.btncaptura);
        btnlista=(Button) findViewById(R.id.btnlista);

        btncaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de captura de foto*/
                Intent intentcaptura = new Intent(getApplicationContext(), ActivityCaptura.class);
                startActivity(intentcaptura);
            }
        });

        btnlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de lista*/
                Intent intentlista = new Intent(getApplicationContext(), ActivityLista.class);
                startActivity(intentlista);
            }
        });

        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para llamar a la actividad de salir del programa*/
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });





    }
}