package com.example.tp1_emergencias_repo


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.example.tp1_emergencias_repo.databinding.ActivityChatBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class chat : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    val size: Int = 0
    val db = Firebase.firestore
    private lateinit var binding: ActivityChatBinding

    private lateinit var fotoperfil : ImageView
    private lateinit var nombre : TextView
    private lateinit var vistarecycle : RecyclerView
    private lateinit var txtmensaje : EditText
    private lateinit var btnenviar : Button

    lateinit var volver: Button


    private lateinit var adapter : adaptermensajes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fotoperfil = findViewById(R.id.foto_perfil)
        nombre = findViewById(R.id.nombre)
        vistarecycle = findViewById(R.id.rvmensajes)
        txtmensaje = findViewById(R.id.txt_mensaje)
        btnenviar = findViewById(R.id.btn_enviar)

        volver = findViewById(R.id.back)
        volver.setOnClickListener {
            val intento = Intent(this,MainActivity::class.java)
            startActivity(intento)
        }

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("chat")


        adapter = adaptermensajes(mutableListOf())
        vistarecycle.layoutManager = LinearLayoutManager(this)
        vistarecycle.adapter = adapter

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = sdf.format(Calendar.getInstance().time)

        /* adapter = adaptermensajes(mutableListOf())
         val linearLayoutManager = LinearLayoutManager(this)
         vistarecycle.layoutManager = LinearLayoutManager(this)
         vistarecycle.adapter = adapter*/


        btnenviar.setOnClickListener {
            if (isConnected(this)) {
            val mensaje = mensaje(
                mensaje = txtmensaje.text.toString(),
                nombre = nombre.text.toString(),
                hora = currentTime // Usa la hora actual
            )
            databaseReference.push().setValue(mensaje)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Mensaje enviado correctamente")
                    } else {
                        Log.e(TAG, "Error al enviar mensaje", task.exception)
                    }
                }
            } else {
                Toast.makeText(this, "Estás fuera de línea. No puedes enviar mensajes.", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val m: mensaje? = dataSnapshot.getValue(mensaje::class.java)
                if (m != null) {
                    adapter.addMensaje(m)
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Tu lógica aquí
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // Tu lógica aquí
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Tu lógica aquí
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.e(TAG, "DatabaseError: ${databaseError.message}")
            }
        })



        val docRef = db.collection("clientes").document("oTkbilK8HqZsUmucVAwI")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.text1.text=document.data?.get("nombre").toString()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun setScrollbar() {
        vistarecycle.scrollToPosition(adapter.itemCount - 1)
    }

    fun add(mensaje: mensaje) {
        adapter.addMensaje(mensaje)
    }

    //chequear conexión

    private fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}