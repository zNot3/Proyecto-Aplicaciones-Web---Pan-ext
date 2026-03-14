package com.panext.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Brand Colors ───────────────────────────────────────────────────────────
val GreenDark      = Color(0xFF2D6A4F)
val GreenLight     = Color(0xFF40916C)
val GreenCard      = Color(0xFF52B788)
val GreenSoft      = Color(0xFFD8F3DC)
val PurpleDark     = Color(0xFF3D1F6D)

val BgColor        = Color(0xFFF4F1EC)
val White          = Color(0xFFFFFFFF)
val Gray100        = Color(0xFFF0EDEA)
val Gray200        = Color(0xFFE2DDD8)
val Gray400        = Color(0xFF9E9891)
val Gray600        = Color(0xFF6B6560)
val Gray800        = Color(0xFF2E2B28)

val RedAlert       = Color(0xFFC94040)
val OrangeAlert    = Color(0xFFD4854A)
val YellowAlert    = Color(0xFFE8B84B)

private val PanExtColorScheme = lightColorScheme(
    primary         = GreenDark,
    onPrimary       = White,
    primaryContainer = GreenSoft,
    secondary       = GreenLight,
    background      = BgColor,
    surface         = White,
    onBackground    = Gray800,
    onSurface       = Gray800,
    outline         = Gray200,
    error           = RedAlert,
)

@Composable
fun PanExtTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PanExtColorScheme,
        content = content
    )
}
