package mx.edu.ittepic.ladm_u3_practica1_jorgeperez

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import mx.edu.ittepic.ladm_u3_practica1_jorgeperez.Utilerias.Utilerias

class MainActivity : AppCompatActivity() {
    var nombreBD = "TRABAJADOR"
    var imagenes = ArrayList<ImageView>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutImagen.setInAnimation(this, android.R.anim.slide_in_left)
        layoutImagen.setInAnimation(this, android.R.anim.slide_out_right)

        btnAddImagen.setOnClickListener {
            añadirImagen()
        }

        btnInsertar.setOnClickListener {
            insertarActividad()
        }

        btnBuscar.setOnClickListener {
            var otroActivity = Intent(this, Main2Activity :: class.java)
            startActivity(otroActivity)
        }

        btnAnterior.setOnClickListener {
            layoutImagen.showPrevious()
        }

        btnSiguiente.setOnClickListener {
            layoutImagen.showNext()
        }
    }

    fun añadirImagen() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 10 && resultCode == Activity.RESULT_OK && data != null){
            var path = data.data
            var imagenNueva = ImageView(this)
            imagenNueva.setImageURI(path)
            imagenNueva.adjustViewBounds = true
            var param : LinearLayout.LayoutParams = LinearLayout.LayoutParams(500, 500)
            imagenNueva.layoutParams = param
            layoutImagen.addView(imagenNueva)

            imagenes.add(imagenNueva)
            layoutImagen.background = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun insertarActividad() {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var insertar = baseDatos.writableDatabase
            var SQL = "INSERT INTO ACTIVIDADES VALUES(NULL, '${txtDescripcion.text.toString()}', '${txtFechaCaptura.text.toString()}', '${txtFechaEntrega.text.toString()}')"

            insertar.execSQL(SQL)
            insertar.close()
            baseDatos.close()

            insertarEvidencia()
        } catch (error : SQLiteException) {
            mensaje(error.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun insertarEvidencia() {
        try {
            (0..imagenes.size-1).forEach{
                val bitmap = (imagenes.get(it).drawable as BitmapDrawable).bitmap
                var evidencia = Evidencias(Utilerias.getBytes(bitmap))
                evidencia.asignarPuntero(this)
                evidencia.idActividad = retornaID()
                var resultado = evidencia.insertarImagen()

                if(resultado == true){
                    mensajeToast("SE INSERTÓ CORRECTAMENTE")
                }else{
                    mensajeToast("ERROR, NO SE INSERTÓ")
                }
            }
            limpiarCampos()

        } catch (error : SQLiteException) {
            mensaje(error.message.toString())
        }
    }

    fun retornaID() : String{
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var columnas = arrayOf("ID_Actividad")

            var cursor = select.query("ACTIVIDADES", columnas, null, null,null,null,null)

            if(cursor.moveToLast()) {
                return cursor.getString(0)
            }

            select.close()
            baseDatos.close()
        } catch (error : SQLiteException){
            mensaje(error.message.toString())
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun limpiarCampos() {
        txtFechaEntrega.setText("")
        txtFechaCaptura.setText("")
        txtDescripcion.setText("")
        layoutImagen.removeAllViews()
        imagenes = ArrayList<ImageView>()
        layoutImagen.background = getDrawable(R.drawable.nodisponible)
    }

    fun mensajeToast (s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    fun mensaje(s : String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage(s)
            .setPositiveButton("OK") {d , i -> }
            .show()
    }
}