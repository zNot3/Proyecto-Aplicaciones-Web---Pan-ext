package com.panext.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.panext.app.data.Routes
import com.panext.app.ui.theme.*

// ─── Bottom Navigation Bar ────────────────────────────────────────────────────
data class NavItem(val label: String, val icon: String, val route: String)

@Composable
fun PanExtBottomNav(navController: NavController, currentRoute: String) {
    val items = listOf(
        NavItem("Inicio",     "🏠", Routes.INICIO),
        NavItem("Compras",    "🛒", Routes.COMPRAS),
        NavItem("Recetas",    "🍽️", Routes.RECETAS),
        NavItem("Inventario", "📦", Routes.INVENTARIO),
        NavItem("Yo",         "👤", Routes.PERFIL),
    )

    Surface(
        shadowElevation = 8.dp,
        color = White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val isActive = currentRoute == item.route ||
                    (item.route == Routes.RECETAS && (currentRoute == Routes.RECETAS_VERDES ||
                            currentRoute == Routes.CHAT_IA || currentRoute == Routes.INGREDIENTES)) ||
                    (item.route == Routes.INVENTARIO && currentRoute.startsWith("detalle"))
                NavItemView(item, isActive) {
                    if (item.route == Routes.INICIO) {
                        // Always pop everything back to Inicio
                        navController.navigate(Routes.INICIO) {
                            popUpTo(Routes.INICIO) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(Routes.INICIO) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavItemView(item: NavItem, isActive: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text = item.icon, fontSize = 22.sp)
        Text(
            text = item.label,
            fontSize = 10.sp,
            color = if (isActive) GreenDark else Gray400,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ─── Card Container ────────────────────────────────────────────────────────────
@Composable
fun PanExtCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        content = { Column(modifier = Modifier.padding(16.dp), content = content) }
    )
}

// ─── Section Label ────────────────────────────────────────────────────────────
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Gray400,
        letterSpacing = 0.8.sp,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

// ─── Back Header ─────────────────────────────────────────────────────────────
@Composable
fun BackHeader(title: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Gray100)
                .clickable { onBack() }
        ) {
            Text("‹", fontSize = 22.sp, color = Gray800)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
    }
}

// ─── Badge ────────────────────────────────────────────────────────────────────
@Composable
fun Badge(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}

// ─── Suggestion Chip ──────────────────────────────────────────────────────────
@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, Gray200, RoundedCornerShape(20.dp))
            .background(White)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = "+ $text", fontSize = 13.sp, color = Gray800)
    }
}

// ─── Divider ─────────────────────────────────────────────────────────────────
@Composable
fun ItemDivider() {
    HorizontalDivider(color = Gray100, thickness = 1.dp)
}
