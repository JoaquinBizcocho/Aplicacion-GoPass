package com.example.aplicaciongopass.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.aplicaciongopass.components.HeaderGoPass

/*
* Esta pantalla permite al usuario ingresar su correo electrónico y contraseña para
* autenticarse mediante Firebase Authentication. Además, proporciona navegación hacia
* la pantalla de registro y, en caso de éxito en el inicio de sesión, hacia la pantalla
* principal
*/
@Composable
fun LoginScreen(navController: NavController) {

    //Instancia de FirebaseAuth que se utilizará para autenticar al usuario.
    val auth = FirebaseAuth.getInstance()

    //Contexto actual de la aplicación, necesario para mostrar Toasts.
    val context = LocalContext.current


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    /*
     * Contenedor principal que ocupa toda la pantalla.
     * Aquí se coloca la cabecera y la columna con los campos de entrada y botones.
     */

    Box(modifier = Modifier.fillMaxSize()) {
        // Cabecera fija
        HeaderGoPass()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            //Titulo de la pantalla
            Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))


            /*
             * Campo de texto para ingresar el correo electronico.
             * Actualiza el estado 'email' cada vez que el usuario escribe.
             */
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(), //visualizacion oculta para proteger la contraseña
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*
             * Botón de inicio de sesión.
             * Valida que los campos no estén vacíos y que la contraseña tenga al menos 6 caracteres.
             * Luego intenta autenticar al usuario con Firebase.
             * En caso de éxito, navega a la pantalla 'home'.
             * En caso de error, muestra un Toast con el mensaje correspondiente.
             */

            Button(onClick = {
                // Aquí validarías usuario con tu base de datos
                if (email.isNotEmpty() && password.length >= 6) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Revisa los campos", Toast.LENGTH_SHORT).show()
                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar")
            }

             // Botón de texto que navega a la pantalla de registro si el usuario no tiene cuenta.

            TextButton(onClick = { navController.navigate("register") }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}
