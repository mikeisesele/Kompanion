package com.michael.kompanion.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp



@Composable
fun TextStyle.boldTexStyle(size: Int, color: Color = Color.Black): TextStyle {
    return TextStyle(
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun TextStyle.mediumTexStyle(size: Int, color: Color = Color.Black): TextStyle {
    return TextStyle(
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
fun TextStyle.normalTexStyle(size: Int, color: Color = Color.Black): TextStyle {
    return TextStyle(
        color = color,
        fontSize = size.sp,
        fontWeight = FontWeight.Normal,
    )
}

// Helper function to convert Dp to pixels
fun Dp.toPx(density: Density): Float = with(density) { this@toPx.toPx() }

