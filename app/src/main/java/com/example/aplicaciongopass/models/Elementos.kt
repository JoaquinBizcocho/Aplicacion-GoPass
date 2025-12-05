package com.example.aplicaciongopass.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@IgnoreExtraProperties
data class Elemento(
    @get:Exclude var id: String? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val fechaCreacion: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)