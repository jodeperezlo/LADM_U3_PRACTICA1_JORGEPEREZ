package mx.edu.ittepic.ladm_u3_practica1_jorgeperez.Utilerias

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object Utilerias {
        fun getBytes(bitmap: Bitmap) : ByteArray {
            var stream= ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            return stream.toByteArray()
        }

        fun getImage(image : ByteArray): Bitmap? {
            return BitmapFactory.decodeByteArray(image,0,image.size)
        }
}