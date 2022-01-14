package com.minenick.practica

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.minenick.practica.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            revisarPermisos()
        }
    }

    private fun revisarPermisos() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
            //Permiso no aceptado por el momento
            requestPermissionStorage()
        }else{
            guardarLayout()
        }
    }

    private fun requestPermissionStorage() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //El usuario ya ha rechazado los permisos
        }else{
            //Pedir permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),777)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==777){
            //nuestros permisos
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                guardarLayout()
            }else{
                //El permiso no ha sido aceptado

            }
        }
    }

    private fun guardarLayout() {
        binding.contenido2.isDrawingCacheEnabled
        binding.contenido2.buildDrawingCache()
        var bmap:Bitmap=binding.contenido2.getDrawingCache()
        try{
            guardarImagenMetodo(bmap)
        }catch (e:Exception){
            Log.d("GuardarLayout","GuardarLayout: "+e.message)
        }finally {
            binding.contenido.destroyDrawingCache()
        }
    }

    private fun guardarImagenMetodo(bitmap: Bitmap) {
        val rutaArchivo = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/DCMI/"
        ) //creamos un directorio
        if (!rutaArchivo.exists()) { //sino existe, se crea
            val rutaCrear =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/DCMI/")
            rutaCrear.mkdirs() //creamos
        }
        val archivo = File(
            rutaArchivo,
            "tuImagenNombre" + ".jpg"
        ) //crearemos el archivo en la ruta y con un nombre
        if (archivo.exists()) {
            archivo.delete() //si este archivo existe, con ese nombre, lo reemplazará
        }
        try {
            val out = FileOutputStream(archivo)
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                out
            ) //creamos la imagen en formato jpg, con la calidad 100
            out.flush()
            out.close()
            Toast.makeText(this, "¡Se ha guardado con éxito tu imagen!", Toast.LENGTH_SHORT).show()
            Log.d(
                "guardarImageb",
                "try: \nRuta: $rutaArchivo\nArchivo: $archivo"
            ) //a traves del logd voy controlando si se crea o no el archivo viéndolo desde el logcat
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                this,
                "¡Ha ocurrido un error al intentar guardar tu imagen!",
                Toast.LENGTH_SHORT
            ).show()
            Log.d(
                "guardarImagen", """
     Catch: ${e.message}
     Ruta: $rutaArchivo
     Archivo: $archivo
     """
            )
        }
    }


}