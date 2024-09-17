package com.example.tp1_emergencias_repo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.media.MediaRecorder
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

    private var cameraFlash = false
    private var flashon = false

    //grabadora
    lateinit var mr: MediaRecorder
    lateinit var btPlay: Button
    lateinit var btStop: Button
    lateinit var btStart: Button
    lateinit var btnmapa: Button
    lateinit var btnchat: Button
    lateinit var path: String

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

        val bl: Button = findViewById(R.id.B2)

        cameraFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        bl.setOnClickListener{
            if (cameraFlash){
                if(flashon){
                    flashon = false
                    bl.setBackgroundColor(RED)

                    flashLightoff()
                }
                else{
                    flashon = true
                    bl.setBackgroundColor(GREEN)

                    flashLighton()
                }
            }
        }

        //grabadora

        path = getExternalFilesDir(null)?.absolutePath + "/myrec.3gp"
        mr = MediaRecorder()

        btPlay = findViewById(R.id.btnPlay)
        btPlay.isEnabled = false

        btStop = findViewById(R.id.btnStop)
        btStop.isEnabled = false

        btStart = findViewById(R.id.btnStart)
        btStart.isEnabled = false



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                111
            )
        } else {
            btPlay.isEnabled = true
            btStart.isEnabled = true
        }

        // Start Recording
        btStart.setOnClickListener {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            btStop.isEnabled = true
            btStart.isEnabled = false
        }

        // Stop Recording
        btStop.setOnClickListener {
            mr.stop()
            mr.reset()
            btStart.isEnabled = true
            btStop.isEnabled = false
            btPlay.isEnabled = true
        }

        // Play Recording
        btPlay.setOnClickListener {
            val mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }

        btnmapa = findViewById(R.id.B4)
        btnmapa.setOnClickListener {
            val intento2 = Intent(this,Mapa::class.java)
            startActivity(intento2)
        }

        btnchat = findViewById(R.id.Bchat)
        btnchat.setOnClickListener {
            val intento3 = Intent(this,chat::class.java)
            startActivity(intento3)
        }


    }

    //grabadora
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            btPlay.isEnabled = true
            btStart.isEnabled = true
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
        val intento1 = Intent(this,camara::class.java)
        startActivity(intento1)

    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show()
        }else{
            //pedir permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }
    }

    /*override fun onRequestPermissionsResult(
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
    }*/

    //linterna

    private fun flashLightoff() {
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try {
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
            Toast.makeText(this,"Linterna apagada",Toast.LENGTH_SHORT).show()
        }catch (e: Exception){}

    }

    private fun flashLighton() {
        val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        var cameraId: String
        try {
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
            Toast.makeText(this,"Linterna encendida",Toast.LENGTH_SHORT).show()
        }catch (e: Exception){}
    }

}