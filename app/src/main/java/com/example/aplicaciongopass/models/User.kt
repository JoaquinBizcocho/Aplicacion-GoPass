package com.example.aplicaciongopass.models



data class User(
    val nombre: String = "",
    val dni: String = "",
    val email: String = "",
    val rol: String = "usuario",
)
