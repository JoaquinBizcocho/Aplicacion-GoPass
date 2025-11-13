package com.example.aplicaciongopass

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializa Firebase globalmente (solo se hace una vez al arrancar la app)
        FirebaseApp.initializeApp(this)
    }
}
