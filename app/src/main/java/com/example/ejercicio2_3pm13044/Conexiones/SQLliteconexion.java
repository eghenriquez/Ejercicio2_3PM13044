package com.example.ejercicio2_3pm13044.Conexiones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLliteconexion extends SQLiteOpenHelper {


    public SQLliteconexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(Transacciones.CreateTableFotografia);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL(Transacciones.DropTableFotografia);
        onCreate(sqLiteDatabase);
    }
}
