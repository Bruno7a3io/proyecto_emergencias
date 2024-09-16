package com.example.tp1_emergencias_repo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tp1_emergencias_repo.R
import com.example.tp1_emergencias_repo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bcall: Button = findViewById(R.id.B5)
        bcall.setOnClickListener { checkPermission() }


        val bcalling: Button = findViewById(R.id.B1)
        bcalling.setOnClickListener { checkcallPermission()}

        val balert : Button = findViewById(R.id.B3)

        val sonido : MediaPlayer = MediaPlayer.create(this, R.raw.barbaros)


        balert.setOnClickListener {
            sonido.start()
        }



    }

    private fun checkcallPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permiso no aceptado
            requestcallPermissions()
        } else {
            //abrir camara
            call()
        }
    }

    private fun requestcallPermissions() {


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            when {
                ContextCompat.checkSelfPermission(
                    this,Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    call()
                }
                else -> requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }

        }else{
            call()
        }
    }

    private fun call() {
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:113")))
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted->
        call()
        if(isGranted){
            call()
        }else{
            Toast.makeText(this,"habilitar permisos para llamada", Toast.LENGTH_SHORT).show()
        }

    }

    //camara
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permiso no aceptado
            requestCameraPermission()
        } else {
            //abrir camara
            opencamera()
        }
    }

    //camara
    private fun opencamera() {
        Toast.makeText(this, "Abriendo camara", Toast.LENGTH_SHORT).show()
        //codigo para abrir camara y guardar la imagen
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show()
        }else{
            //pedir permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 123){
            //nuestros permisos
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera()
            }else{
                Toast.makeText(this, "Permiso rechazado por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }
}