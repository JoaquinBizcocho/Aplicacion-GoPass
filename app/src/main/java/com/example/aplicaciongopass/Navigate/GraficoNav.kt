package com.example.aplicaciongopass.Navigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicaciongopass.auth.LoginScreen
import com.example.aplicaciongopass.auth.RegisterScreen
import com.example.aplicaciongopass.auth.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(navController: NavHostController? = null) {
    // Si te pasan un navController desde MainActivity, úsalo; si no, crea uno nuevo.
    val internalNavController = navController ?: rememberNavController()

    // Obtenemos la instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()

    // Determinamos la pantalla inicial
    val startDestination = if (auth.currentUser != null) "home" else "login"

    // Usamos startDestination dinámico
    NavHost(
        navController = internalNavController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(internalNavController) }
        composable("register") { RegisterScreen(internalNavController) }
        composable("home") { HomeScreen(internalNavController) }
    }
}
