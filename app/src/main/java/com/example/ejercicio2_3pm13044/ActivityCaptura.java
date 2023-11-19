package com.example.ejercicio2_3pm13044;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio2_3pm13044.Conexiones.SQLliteconexion;
import com.example.ejercicio2_3pm13044.Conexiones.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityCaptura extends AppCompatActivity {

    Button btnregresa1,btncapturar,btnguardar;
    EditText txtdesc;

    ImageView Imagen1;
    Bitmap imagen;

    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura);

        txtdesc = (EditText) findViewById(R.id.txtdesc);
        Imagen1 = (ImageView) findViewById(R.id.Imagen1);
        btnregresa1 = (Button) findViewById(R.id.btnregresa1);
        btncapturar = (Button) findViewById(R.id.btncapturar);
        btnguardar=(Button) findViewById(R.id.btnguardar);


        //Eventos de botones
        btncapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Guardar();
            }
        });

        btnregresa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para regresar a la actividad principal*/
                Intent intentregresa1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentregresa1);
            }
        });
    }

    //Funciones
    private void Guardar() {
        try {
            Foto(imagen);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, ActivityCaptura.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarTextos();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Debe de tomarse una Fotografia!",Toast.LENGTH_LONG).show();
        }


    }

    private void Foto( Bitmap bitmap) {

        SQLliteconexion conexion = new SQLliteconexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.descripcion,txtdesc.getText().toString());
        valores.put(String.valueOf(Transacciones.foto),ArrayFoto);

        Long resultado = db.insert(Transacciones.tblFotografias, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Se ha Registrado Exitosamente. Foto N° " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }

    private void LimpiarTextos() {
        txtdesc.setText("");
    }

    private void Permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            TomarFoto();
        }
    }

    private void TomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                TomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Permita el acceso a la camara",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Byte [] arreglo;

        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            Imagen1.setImageBitmap(imagen);
        }

    }
}