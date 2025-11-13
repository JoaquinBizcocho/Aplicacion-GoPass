

/*
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

 */


package com.example.aplicaciongopass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.aplicaciongopass.Navigate.AppNavGraph
import com.example.aplicaciongopass.ui.theme.AplicacionGoPassTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController)
        }
    }
}

//--------------------------------------------------------------------------------------
//El codigo comentado es otra parte de la aplicacion mas adelante para poder mostrar codigo qr unico
/*
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") { MainScreen(navController) }
                composable("qr") { QRScreen(navController) }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Button(onClick = { navController.navigate("qr") }) {
            Text("Ver mi QR")
        }
    }
}

@Composable
fun QRScreen(navController: NavHostController) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        try {
            val bitMatrix = MultiFormatWriter().encode(
                "Dni: 29553399D",
                BarcodeFormat.QR_CODE,
                600,
                600
            )
            qrBitmap = BarcodeEncoder().createBitmap(bitMatrix)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        /*verticalArrangement = Arrangement.SpaceBetween,*/ //  Espacia QR y botón
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(1.dp)) // espacio arriba
        qrBitmap?.let { bmp ->
            Image(bitmap = bmp.asImageBitmap(), contentDescription = "Código QR")
        }

        Button(
                onClick = { navController.navigate("main") },
            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
            .padding(bottom = 32.dp)
        ) {
            Text("Volver")
        }
    }

    /*Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        qrBitmap?.let { bmp ->
            Image(bitmap = bmp.asImageBitmap(), contentDescription = "Código QR")
        } ?: Text("Generando QR...")
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Button(onClick = { navController.navigate("main") }) {
            Text("volver")
        }
    }*/
}


/*class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil) // tu layout con TextView + ImageView

        // Referencia al ImageView
        val imageViewQR = findViewById<ImageView>(R.id.imageViewQR)

        try {
            // Genera el QR con el contenido que quieras
            val bitMatrix = MultiFormatWriter().encode(
                "https://www.marca.com/", // contenido del QR
                BarcodeFormat.QR_CODE,     // tipo de código
                600,                       // ancho en píxeles
                600                        // alto en píxeles
            )

            // Convierte la matriz a Bitmap
            val bitmap: Bitmap = BarcodeEncoder().createBitmap(bitMatrix)

            // Muestra el QR en el ImageView
            imageViewQR.setImageBitmap(bitmap)

        } catch (e: Exception) {
            e.printStackTrace() // si hay error, lo ves en Logcat
        }
    }
}*/

 */
