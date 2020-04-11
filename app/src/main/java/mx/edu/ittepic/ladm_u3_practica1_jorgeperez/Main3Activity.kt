package mx.edu.ittepic.ladm_u3_practica1_jorgeperez

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import mx.edu.ittepic.ladm_u3_practica1_jorgeperez.Utilerias.Utilerias
import kotlinx.android.synthetic.main.activity_main3.*
import java.lang.Exception


class Main3Activity : AppCompatActivity() {
    var nombreBD = "TRABAJADOR"
    var idFoto = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extrs = intent.extras
        var idEliminar = extrs?.getString("id").toString()

        mostrarRegistro(idEliminar)

        btnEliminar.setOnClickListener {
            eliminarRegistro(idEliminar)
            var otroActivity = Intent(this, Main2Activity :: class.java)
            startActivity(otroActivity)
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        btnAnterior3.setOnClickListener {
            VS.showPrevious()
        }

        btnSiguiente3.setOnClickListener {
            VS.showNext()
        }
    }

    fun mostrarRegistro(id : String) {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES WHERE Id_Actividad = ?"
            var parametros = arrayOf(id)
            var cursor = select.rawQuery(SQL, parametros)

            if(cursor.moveToFirst()){
                //SI HAY RESULTADO
                lblID.setText("ID:" + cursor.getString(0))
                lblDescripcion.setText("Descripcion: " + cursor.getString(1))
                lblFechaCaptura.setText("Fecha Captura: " + cursor.getString(2))
                lblFechaEntrega.setText("Fecha Entrega: " + cursor.getString(3))
            } else {
                //NO HAY RESULTADO
                mensajeToast("NO SE ENCONTRARON RESULTADOS")
            }
            select.close()
            baseDatos.close()

            recuperarImagenes(id)

        } catch (error : SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun recuperarImagenes(id : String) {
        var nulo : ByteArray? = null
        var evidencia = Evidencias(nulo)
        evidencia!!.asignarPuntero(this)
        var imagenes = evidencia.buscarImagen(id)
        var img = ArrayList<ImageView>()

        VS.setInAnimation(this, android.R.anim.slide_in_left)
        VS.setInAnimation(this, android.R.anim.slide_out_right)

        try {
            (0..imagenes.size-1).forEach {
                var imgNew = ImageView(this)
                imgNew.adjustViewBounds = true
                val bitmap = Utilerias.getImage(imagenes[it])
                imgNew.setImageBitmap(bitmap)
                var param : LinearLayout.LayoutParams = LinearLayout.LayoutParams(500, 500)
                imgNew.layoutParams = param
                VS.addView(imgNew)
            }
        } catch (error : Exception){
        }
    }

    fun eliminarRegistro(id : String) {
        try {
            var base = BaseDatos(this, nombreBD, null, 1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())
            var respuesta = eliminar.delete("EVIDENCIAS", "ID_Actividad = ?", idEliminar)

            if(respuesta.toInt() == 0) {
                mensaje("NO SE HA ELIMINÓ")
            }

            var respuesta2 = eliminar.delete("ACTIVIDADES", "ID_Actividad = ?", idEliminar)

            if(respuesta2.toInt() == 0) {
                mensaje("NO SE ELIMINÓ")
            }
        } catch (e : SQLiteException) {
            mensaje(e.message.toString())
        }
    }

    // MOSTRAR MENSAJE MEDIANTE LENGTH
    fun mensajeToast(s : String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
    // MOSTRAR MENSAJE DE DIÁLOGO
    fun mensaje(s : String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage(s)
            .setPositiveButton("OK") {d , i -> }
            .show()
    }
}
