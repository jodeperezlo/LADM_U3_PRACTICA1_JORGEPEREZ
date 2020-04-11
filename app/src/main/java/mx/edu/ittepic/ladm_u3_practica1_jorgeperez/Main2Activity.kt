package mx.edu.ittepic.ladm_u3_practica1_jorgeperez

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var nombreBD = "TRABAJADOR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        cargarLista()

        btnBuscarID.setOnClickListener {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(txtBuscarID.text.toString())
            var cursor = select.query("ACTIVIDADES", columnas, "ID_Actividad = ?", idBuscar, null, null, null)
            listaID = ArrayList<String>()

            if(cursor.moveToFirst()) {
                var arreglo = ArrayList<String>()
                var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
                arreglo.add(data)
                listaID.add(cursor.getString(0))
                lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    llamarOtroIntent(listaID[position])
                }
            }
        }
    }

    fun cargarLista() {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES"
            var cursor = select.rawQuery(SQL, null)
            listaID = ArrayList<String>()
            if(cursor.count > 0) {
                var arreglo = ArrayList<String>()
                this.listaID = ArrayList<String>()
                cursor.moveToFirst()
                var cantidad = cursor.count-1

                (0..cantidad).forEach {
                    var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
                    arreglo.add(data)
                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()
                }

                lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    llamarOtroIntent(listaID[position])
                }
            }

            select.close()
            baseDatos.close()
        } catch (error : SQLiteException){
            mensajeToast(error.message.toString())
        }
    }

    fun llamarOtroIntent(id : String) {
        var otroActivity = Intent(this, Main3Activity :: class.java)
        otroActivity.putExtra("id", id)
        startActivity(otroActivity)
    }
    // MOSTRAR MENSAJE POR TOAST
    fun mensajeToast(s : String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}