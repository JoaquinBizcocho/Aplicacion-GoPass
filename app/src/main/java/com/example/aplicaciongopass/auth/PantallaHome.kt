package com.example.aplicaciongopass.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicaciongopass.models.User
import com.example.aplicaciongopass.ui.components.HomeHeaderGoPass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun HomeScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val firebaseUser = auth.currentUser
    val dbUser = remember { mutableStateOf(User()) }

    LaunchedEffect(firebaseUser?.uid) {
        val uid = firebaseUser?.uid
        if (uid == null) {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            navController.navigate("login") { popUpTo("home") { inclusive = true } }
            return@LaunchedEffect
        }

        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        ref.get()
            .addOnSuccessListener { snapshot ->
                Log.d("HomeScreen", "Snapshot recibido: $snapshot")
                if (snapshot.exists()) {
                    // Intentamos el mapeo a User
                    val mapped = snapshot.getValue(User::class.java)
                    if (mapped != null) {
                        dbUser.value = mapped
                        Log.d("HomeScreen", "Usuario mapeado: $mapped")
                    } else {
                        // leer campos manualmente
                        val nombre = snapshot.child("nombre").getValue(String::class.java) ?: ""
                        val dni = snapshot.child("dni").getValue(String::class.java) ?: ""
                        val email = snapshot.child("email").getValue(String::class.java) ?: ""
                        dbUser.value = User(nombre = nombre, dni = dni, email = email)
                        Log.w("HomeScreen", "Mapeo nulo — usado fallback: ${dbUser.value}")
                    }
                } else {
                    Toast.makeText(
                        context,
                        "No se encontraron datos de usuario en la base",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { ex ->
                Toast.makeText(
                    context,
                    "Error leyendo DB: ${ex.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("HomeScreen", "Error leyendo users/$uid", ex)
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Cabecera con el nombre del usuario
        val nombreParaCabecera = dbUser.value.nombre.ifEmpty { firebaseUser?.displayName ?: "Usuario" }
        HomeHeaderGoPass(nombreUsuario = nombreParaCabecera)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido: ${if (dbUser.value.email.isNotEmpty()) dbUser.value.email else firebaseUser?.email ?: "Usuario"}",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
