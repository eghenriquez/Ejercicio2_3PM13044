package com.example.ejercicio2_3pm13044;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ejercicio2_3pm13044.Conexiones.SQLliteconexion;
import com.example.ejercicio2_3pm13044.Conexiones.Transacciones;
import com.example.ejercicio2_3pm13044.Modelo.Fotografia;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ActivityLista extends AppCompatActivity {

    SQLliteconexion conexion = new SQLliteconexion(this, Transacciones.NameDatabase, null, 1);

    Button btnregresa2;
    ListView txtlista;
    ArrayList<Fotografia> listaFotos = new ArrayList<Fotografia>();
    ImageView imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        SQLiteDatabase db = conexion.getWritableDatabase();
        String sql = "SELECT * FROM fotografia";
        Cursor cursor = db.rawQuery(sql, new String[] {});

        btnregresa2=(Button) findViewById(R.id.btnregresa2);
        txtlista = findViewById(R.id.txtlista);

        while (cursor.moveToNext()){
            listaFotos.add(new Fotografia(cursor.getInt(0),cursor.getString(1) , cursor.getBlob(2)));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        AdaptadorFotografia adaptador = new AdaptadorFotografia(this);

        txtlista.setAdapter(adaptador);


        txtlista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CapturarFoto(i);
            }
        });


        btnregresa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*crear intent para regresar a la actividad principal*/
                Intent intentregresa2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentregresa2);
            }
        });
    }

    private void CapturarFoto( int id) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Fotografia lista_Fotografia = null;
        listaFotos = new ArrayList<Fotografia>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tblFotografias,null);

        while (cursor.moveToNext())
        {
            lista_Fotografia = new Fotografia();
            lista_Fotografia.setId(cursor.getInt(0));
            lista_Fotografia.setDescription(cursor.getString(1));
            listaFotos.add(lista_Fotografia);
        }
        cursor.close();
        Fotografia fotografia = listaFotos.get(id);

    }

    class AdaptadorFotografia extends ArrayAdapter<Fotografia> {

        AppCompatActivity appCompatActivity;

        AdaptadorFotografia(AppCompatActivity context) {
            super(context, R.layout.fotografia, listaFotos);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.fotografia, null);

            imageView4 = item.findViewById(R.id.imageView4);

            SQLiteDatabase db = conexion.getWritableDatabase();

            String sql = "SELECT * FROM fotografia";

            Cursor cursor = db.rawQuery(sql, new String[] {});
            Bitmap bitmap = null;
            TextView textView1 = item.findViewById(R.id.textView3);

            if (cursor.moveToNext()){
                textView1.setText(listaFotos.get(position).getDescription());
                byte[] blob = listaFotos.get(position).getFoto();
                ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(bais);
                imageView4.setImageBitmap(bitmap);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
            return(item);
        }
    }
}