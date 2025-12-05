package com.example.aplicaciongopass.elementos

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicaciongopass.models.Elemento
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementFormScreen(navController: NavController, elementId: String?) {
    val context = LocalContext.current
    val elementosRef = FirebaseDatabase.getInstance().getReference("elementos")

    var rolUsuario by remember { mutableStateOf("usuario") }
    val auth = FirebaseAuth.getInstance()
    LaunchedEffect(auth.currentUser?.uid) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("User/$uid")
            userRef.get().addOnSuccessListener { snapshot ->
                rolUsuario = snapshot.child("rol").getValue(String::class.java) ?: "usuario"
                if (rolUsuario != "admin") {
                    Toast.makeText(context, "Acceso denegado. Solo para administradores.", Toast.LENGTH_LONG).show()
                    navController.popBackStack("elementList", inclusive = false)
                }
            }
        }
    }

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val isEditMode = elementId != null

    LaunchedEffect(elementId) {
        if (isEditMode && elementId != null) {
            elementosRef.child(elementId).get().addOnSuccessListener { snapshot ->
                val elemento = snapshot.getValue(Elemento::class.java)
                if (elemento != null) {
                    nombre = elemento.nombre
                    descripcion = elemento.descripcion
                } else {
                    Toast.makeText(context, "Elemento no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Error al cargar datos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun guardarElemento() {
        if (nombre.isBlank() || descripcion.isBlank()) {
            Toast.makeText(context, "El nombre y la descripción no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoElemento = Elemento(nombre = nombre, descripcion = descripcion)

        if (rolUsuario != "admin") {
            Toast.makeText(context, "No tienes permisos para guardar.", Toast.LENGTH_LONG).show()
            return
        }

        val task = if (isEditMode && elementId != null) {
            elementosRef.child(elementId).setValue(nuevoElemento)
        } else {
            elementosRef.push().setValue(nuevoElemento)
        }

        task.addOnSuccessListener {
            Toast.makeText(context, "Elemento guardado con éxito.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }.addOnFailureListener {
            Toast.makeText(context, "Error al guardar el elemento: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Elemento" else "Nuevo Elemento") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campos de texto (Fase 6-12) [cite: 63]
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Elemento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            )

            // Botón Guardar (Fase 6-12) [cite: 64]
            Button(
                onClick = ::guardarElemento,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Actualizar" else "Guardar")
            }
        }
    }
}