package com.example.aplicaciongopass.elementos

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicaciongopass.auth.HomeScreen
import com.example.aplicaciongopass.models.Elemento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.compose.runtime.DisposableEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementListScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val elementosRef = FirebaseDatabase.getInstance().getReference("elementos")
    val elementos = remember { mutableStateOf(emptyList<Elemento>()) }


    var rolUsuario by remember { mutableStateOf("usuario") }
    val esAdmin = rolUsuario == "admin"

    LaunchedEffect(auth.currentUser?.uid) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("User/$uid")
            userRef.get().addOnSuccessListener { snapshot ->
                rolUsuario = snapshot.child("rol").getValue(String::class.java) ?: "usuario"
            }
        }
    }

    DisposableEffect(elementosRef) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaTemporal = snapshot.children.mapNotNull {
                    // Mapeo a Elemento y asignación del ID (key) [cite: 31]
                    it.getValue(Elemento::class.java)?.copy(id = it.key)
                }
                elementos.value = listaTemporal
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ElementListScreen", "Error leyendo elementos: ${error.message}") // [cite: 37]
                Toast.makeText(context, "Error cargando lista de elementos.", Toast.LENGTH_SHORT).show()
            }
        }
        elementosRef.addValueEventListener(valueEventListener)
        onDispose {
            elementosRef.removeEventListener(valueEventListener)
        }
    }


    fun eliminarElemento(elementId: String) {
        if (esAdmin) {
            elementosRef.child(elementId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Elemento eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar el elemento", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Elementos Compartidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (esAdmin) {
                FloatingActionButton(onClick = { navController.navigate("elementForm") }) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar elemento")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(elementos.value) { elemento ->
                ElementoCard(
                    elemento = elemento,
                    esAdmin = esAdmin,
                    onEdit = {
                        elemento.id?.let { id -> navController.navigate("elementForm/$id") }
                    },
                    onDelete = {
                        elemento.id?.let { id -> eliminarElemento(id) }
                    }
                )
            }
        }
    }
}

@Composable
fun ElementoCard(
    elemento: Elemento,
    esAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = elemento.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = elemento.descripcion, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Fecha: ${elemento.fechaCreacion}", style = MaterialTheme.typography.bodySmall)
            }

            if (esAdmin) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}