package com.example.tp1_emergencias_repo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HolderMensajes(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nombre: TextView = itemView.findViewById(R.id.nombre_chat)
    val mensaje: TextView = itemView.findViewById(R.id.mensaje_chat)
    val hora: TextView = itemView.findViewById(R.id.hora_chat)
    val fotoperfil: ImageView = itemView.findViewById(R.id.foto_perfil_chat)

}