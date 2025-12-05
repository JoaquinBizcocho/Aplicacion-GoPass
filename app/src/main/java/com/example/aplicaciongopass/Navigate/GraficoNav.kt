package com.example.aplicaciongopass.Navigate

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicaciongopass.auth.LoginScreen
import com.example.aplicaciongopass.auth.RegisterScreen
import com.example.aplicaciongopass.auth.HomeScreen
import com.example.aplicaciongopass.elementos.ElementFormScreen // 🚨 NUEVO
import com.example.aplicaciongopass.elementos.ElementListScreen // 🚨 NUEVO
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(navController: NavHostController? = null) {
    val internalNavController = navController ?: rememberNavController()
    val firebaseAuth = FirebaseAuth.getInstance()
    var currentUser by remember { mutableStateOf(firebaseAuth.currentUser) }

    LaunchedEffect(Unit) {
        firebaseAuth.addAuthStateListener { auth ->
            currentUser = auth.currentUser
        }
    }

    val startDestination = if (currentUser != null) "home" else "login"

    NavHost(
        navController = internalNavController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(internalNavController) }
        composable("register") { RegisterScreen(internalNavController) }
        composable("home") { HomeScreen(internalNavController) }

        composable("elementList") { ElementListScreen(internalNavController) }

        composable("elementForm/{elementId}") { backStackEntry ->
            val elementId = backStackEntry.arguments?.getString("elementId")
            ElementFormScreen(internalNavController, elementId)
        }
        composable("elementForm") { ElementFormScreen(internalNavController, null) }
    }
}