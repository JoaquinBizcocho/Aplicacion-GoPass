package com.example.aplicaciongopass.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicaciongopass.components.HeaderGoPass
import com.example.aplicaciongopass.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

/*
* Permite al usuario ingresar su información personal (nombre, teléfono, DNI, email, contraseña),
* crear un usuario en Firebase Authentication y almacenar los datos en Firebase Realtime Database.
* También proporciona navegación hacia la pantalla de inicio de sesión y, en caso de éxito,
* hacia la pantalla principal.

 */

@Composable
fun RegisterScreen(navController: NavController) {


     // Contexto actual de la aplicación, necesario para mostrar Toasts.

    val context = LocalContext.current

    //Instancia de FirebaseAuth que se utilizará para registrar al usuario.
    val auth = FirebaseAuth.getInstance()

    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    /*
     * Contenedor principal que ocupa toda la pantalla.
     * Aquí se coloca la cabecera y la columna con los campos de entrada y botones.
     */

    Box(modifier = Modifier.fillMaxSize()) {
        HeaderGoPass() // Cabecera fija

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            Spacer(modifier = Modifier.height(16.dp))

            /*
             * Botón de registro.
             * Valida que todos los campos estén completos y que la contraseña tenga al menos 6 caracteres.
             * Luego crea un usuario en Firebase Auth, actualiza el displayName y guarda los datos en Realtime Database.
             * Muestra Toasts de éxito o error y navega a la pantalla 'home' en caso de éxito.
             */

            Button(
                onClick = {
                    when {
                        nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                            Toast.makeText(context, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
                        }

                        password.length < 6 -> {
                            Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            // Registro con Firebase
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser = auth.currentUser!!
                                        val uid = firebaseUser.uid

                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(nombre)
                                            .build()

                                        firebaseUser.updateProfile(profileUpdates)

                                        val user = User(nombre = nombre, dni = dni, email = email)

                                        FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(uid)
                                            .setValue(user)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                                navController.navigate("home") {
                                                    popUpTo("register") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { ex ->
                                                Toast.makeText(context, "Error guardando usuario: ${ex.message}", Toast.LENGTH_LONG).show()
                                            }

                                    } else {
                                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(8.dp))


            //  Botón de texto que navega a la pantalla de inicio de sesión si el usuario ya tiene cuenta.

            TextButton(onClick = { navController.navigate("login") }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}




