package mx.edu.ittepic.ladm_u3_practica1_jorgeperez

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Evidencias (img : ByteArray?) {
    var foto = img
    var idActividad = ""
    var error = -1
    val nombreBD = "TRABAJADOR"
    var puntero : Context?= null

    fun asignarPuntero(p : Context){
        puntero = p
    }

    fun insertarImagen() : Boolean{
        try {
            var base = BaseDatos(puntero!!,nombreBD,null,1)
            var insertar = base.writableDatabase
            var datos = ContentValues()
            datos.put("Foto", foto)
            datos.put("ID_Actividad", idActividad)
            var respuesta = insertar.insert("EVIDENCIAS","ID_Evidencia", datos)
            if(respuesta.toInt() == -1){
                return false
            }
        }catch (e: SQLiteException){
            error = 1
            return false
        }
        return true
    }

    fun buscarImagen(id : String) : ArrayList<ByteArray> {
        var arreglo = ArrayList<ByteArray>()
        try {
            var baseDatos = BaseDatos(puntero!!, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM EVIDENCIAS WHERE ID_Actividad = ?"
            var parametros = arrayOf(id)
            var cursor = select.rawQuery(SQL, parametros)
            if (cursor.moveToFirst()){
                do{
                    arreglo.add(cursor.getBlob(cursor.getColumnIndex("Foto")))
                } while (cursor.moveToNext())
            }
        } catch (error : SQLiteException){

        }
        return arreglo
    }
}