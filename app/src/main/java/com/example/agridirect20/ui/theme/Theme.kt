package com.example.agridirect20.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: replace these with your exact Figma colors
private val LightColors = lightColorScheme(
    primary = Color(0xFF2E7D32),      // main green
    onPrimary = Color.White,
    secondary = Color(0xFF8BC34A),    // lighter green
    onSecondary = Color.Black,
    background = Color(0xFFF7F7F7),
    onBackground = Color(0xFF212121),
    surface = Color.White,
    onSurface = Color(0xFF212121)
)

// TODO: tweak these to match your Figma text styles (sizes, weights)
private val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

@Composable
fun AgriDirectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
