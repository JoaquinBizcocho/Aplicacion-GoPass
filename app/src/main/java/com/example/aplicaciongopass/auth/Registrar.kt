package com.example.aplicaciongopass.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation


/*
* Permite al usuario ingresar su información personal (nombre, DNI, email, contraseña),
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
    val db =
        Firebase.database("https://gopass-e2a6a-default-rtdb.europe-west1.firebasedatabase.app/")

    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var rol by remember { mutableStateOf("usuario") }



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
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )


            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

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
                            Toast.makeText(
                                context,
                                "Por favor, rellena todos los campos.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        password.length < 6 -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener al menos 6 caracteres.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !validarDni(dni) -> {
                            Toast.makeText(
                                context,
                                "El DNI introducido no es válido.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        password != confirmPassword -> {
                            Toast.makeText(
                                context,
                                "Las contraseñas no coinciden. Por favor, revísalas.",
                                Toast.LENGTH_LONG
                            ).show()
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

                                        val user = User(
                                            nombre = nombre,
                                            dni = dni,
                                            email = email,
                                            rol = rol
                                        )

                                        db.getReference("User") // 'db' es la variable que inicializaste
                                            .child(uid)
                                            .setValue(user)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Registro exitoso, inicia sesión.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate("home") {
                                                    popUpTo("register") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { ex ->
                                                Toast.makeText(
                                                    context,
                                                    "Error guardando usuario: ${ex.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error: ${task.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Centra el contenido horizontalmente
            ) {
                //  Botón de texto que navega a la pantalla de inicio de sesión
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(
                        text = "Iniciar Sesion",
                        // ⬇️ Aplicamos el color primario (suele ser morado/azul en Material)
                        color = MaterialTheme.colors.primary,
                        // Opcional: para hacerlo más visible
                        style = MaterialTheme.typography.button
                    )
                }
        }
    }
}
}
fun validarDni(dni: String): Boolean {
    val dniRegex = Regex("""^\d{8}[A-Za-z]$""")
    if (!dniRegex.matches(dni)) return false

    val letras = "TRWAGMYFPDXBNJZSQVHLCKE"
    val numero = dni.substring(0, 8).toInt()
    val letraCorrecta = letras[numero % 23]

    return dni.last().uppercaseChar() == letraCorrecta
}




