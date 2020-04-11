package mx.edu.ittepic.ladm_u3_practica1_jorgeperez

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos (context : Context, nombreBD : String, cursorFactory: SQLiteDatabase.CursorFactory?,
                 numeroVersion : Int) : SQLiteOpenHelper(context, nombreBD, cursorFactory, numeroVersion) {

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db!!.execSQL("CREATE TABLE ACTIVIDADES(ID_Actividad INTEGER PRIMARY KEY AUTOINCREMENT, Descripcion VARCHAR(2000), FechaCaptura DATE, FechaEntrega DATE)")
            db.execSQL("CREATE TABLE EVIDENCIAS(ID_Evidencia INTEGER PRIMARY KEY AUTOINCREMENT, ID_Actividad INTEGER NOT NULL, Foto BLOB, FOREIGN KEY (ID_Actividad) REFERENCES ACTIVIDADES(ID_Actividad))")
        } catch (error : SQLiteException){

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}