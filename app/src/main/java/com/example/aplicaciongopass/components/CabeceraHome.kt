package com.example.aplicaciongopass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeHeaderGoPass(nombreUsuario: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF141450),
                        Color(0xFF2F2FCF)
                    )
                )
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Lado izquierdo: Logo GoPass ✦
        Text(
            text = "GoPass ✦",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )

        // Lado derecho: Nombre del usuario
        Text(
            text = nombreUsuario,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(max = 180.dp) // Evita que se desborde
        )
    }
}
