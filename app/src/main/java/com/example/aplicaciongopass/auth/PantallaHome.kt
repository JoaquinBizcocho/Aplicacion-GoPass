package com.example.aplicaciongopass.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.* import androidx.compose.runtime.*
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
    val rolUsuario = remember { mutableStateOf("usuario") }

    LaunchedEffect(firebaseUser?.uid) {
        val uid = firebaseUser?.uid
        if (uid == null) {
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            navController.navigate("login") { popUpTo("home") { inclusive = true } }
            return@LaunchedEffect
        }

        val ref = FirebaseDatabase.getInstance().getReference("User/$uid")
        ref.get()
            .addOnSuccessListener { snapshot ->
                Log.d("HomeScreen", "Snapshot recibido: $snapshot")
                if (snapshot.exists()) {

                    val rol = snapshot.child("rol").getValue(String::class.java) ?: "usuario"
                    rolUsuario.value = rol // El rol se almacena aquí

                    val mapped = snapshot.getValue(User::class.java)

                    if (mapped != null) {
                        dbUser.value = mapped.copy(rol = rol) // Aseguramos que el rol esté en el modelo
                        Log.d("HomeScreen", "Usuario mapeado: $mapped con rol: $rol")
                    } else {
                        val nombre = snapshot.child("nombre").getValue(String::class.java) ?: ""
                        val dni = snapshot.child("dni").getValue(String::class.java) ?: ""
                        val email = snapshot.child("email").getValue(String::class.java) ?: ""
                        dbUser.value = User(nombre = nombre, dni = dni, email = email, rol = rol)
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
                Log.e("HomeScreen", "Error leyendo User/$uid", ex)
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu rol es: ${rolUsuario.value.uppercase()}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("elementList")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elementos")
            }

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