package com.panext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.panext.app.data.AlertType
import com.panext.app.data.AppData
import com.panext.app.data.InventarioItem
import com.panext.app.data.Routes
import com.panext.app.ui.components.*
import com.panext.app.ui.theme.*

// ─── Inventario Screen ────────────────────────────────────────────────────────
@Composable
fun InventarioScreen(navController: NavController) {
    var items by remember { mutableStateOf(AppData.inventario.map { it.copy() }.toMutableList()) }
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newIcon by remember { mutableStateOf("🛒") }
    var newExpira by remember { mutableStateOf("") }
    var newQty by remember { mutableStateOf("1") }

    val alertCount = items.count { it.alert != null }

    Scaffold(
        containerColor = BgColor,
        bottomBar = { PanExtBottomNav(navController, Routes.INVENTARIO) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Gray800,
                contentColor = White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 8.dp)
            ) { Text("+", fontSize = 26.sp) }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Inventario", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Gray800)
            Text(
                "${items.size} elementos · $alertCount alerta${if (alertCount != 1) "s" else ""}",
                fontSize = 13.sp, color = Gray400,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            PanExtCard {
                items.forEachIndexed { i, item ->
                    if (i > 0) ItemDivider()
                    InventarioRow(
                        item = item,
                        onIncrement = {
                            items = items.toMutableList().also { list ->
                                val idx = list.indexOfFirst { it.id == item.id }
                                if (idx != -1) list[idx] = list[idx].copy(qty = list[idx].qty + 1)
                            }
                            AppData.inventario.find { it.id == item.id }?.qty = items.find { it.id == item.id }?.qty ?: 0
                        },
                        onDecrement = {
                            items = items.toMutableList().also { list ->
                                val idx = list.indexOfFirst { it.id == item.id }
                                if (idx != -1 && list[idx].qty > 0) list[idx] = list[idx].copy(qty = list[idx].qty - 1)
                            }
                            AppData.inventario.find { it.id == item.id }?.qty = items.find { it.id == item.id }?.qty ?: 0
                        },
                        onTap = { navController.navigate(Routes.detalle(item.id)) }
                    )
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }

    // ── Add item dialog ──
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                newName = ""; newIcon = "🛒"; newExpira = ""; newQty = "1"
            },
            title = { Text("Nuevo elemento", fontWeight = FontWeight.SemiBold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newIcon,
                        onValueChange = { newIcon = it },
                        label = { Text("Emoji (ej: 🍎)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newExpira,
                        onValueChange = { newExpira = it },
                        label = { Text("Fecha expiración (dd/mm/aa)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newQty,
                        onValueChange = { newQty = it.filter { c -> c.isDigit() } },
                        label = { Text("Cantidad") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isNotBlank()) {
                        val newItem = InventarioItem(
                            id = System.currentTimeMillis().toInt(),
                            name = newName.trim(),
                            icon = newIcon.trim().ifEmpty { "🛒" },
                            expira = newExpira.trim().ifEmpty { "—" },
                            qty = newQty.toIntOrNull() ?: 1
                        )
                        items = items.toMutableList().also { it.add(newItem) }
                        AppData.inventario.add(newItem)
                    }
                    showDialog = false
                    newName = ""; newIcon = "🛒"; newExpira = ""; newQty = "1"
                }) { Text("Agregar", color = GreenDark, fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    newName = ""; newIcon = "🛒"; newExpira = ""; newQty = "1"
                }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun InventarioRow(item: InventarioItem, onIncrement: () -> Unit, onDecrement: () -> Unit, onTap: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Gray100)
                .clickable { onTap() }
        ) { Text(item.icon, fontSize = 28.sp) }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onTap() }
        ) {
            Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
            Text("Expira: ${item.expira}", fontSize = 11.sp, color = Gray400, modifier = Modifier.padding(top = 1.dp))
            when (item.alert) {
                AlertType.EXPIRA -> AlertChip("¡Expirará!", RedAlert.copy(alpha = 0.1f), RedAlert)
                AlertType.POCO   -> AlertChip("Queda poco", OrangeAlert.copy(alpha = 0.1f), OrangeAlert)
                null -> {}
            }
        }

        // Qty controls
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QtyButton(label = "−", filled = false) { onDecrement() }
            Text(item.qty.toString(), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Gray800, modifier = Modifier.widthIn(min = 20.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            QtyButton(label = "+", filled = true) { onIncrement() }
        }
    }
}

@Composable
fun AlertChip(text: String, bg: androidx.compose.ui.graphics.Color, fg: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .padding(top = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) { Text(text, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = fg) }
}

@Composable
fun QtyButton(label: String, filled: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (filled) Gray800 else White)
            .then(
                if (!filled) Modifier.then(
                    Modifier.background(
                        color = White,
                        shape = CircleShape
                    )
                ) else Modifier
            )
            .clickable { onClick() }
    ) {
        Text(
            label,
            fontSize = 18.sp,
            color = if (filled) White else Gray800,
            lineHeight = 18.sp
        )
    }
}

// ─── Detalle Ingrediente Screen ───────────────────────────────────────────────
@Composable
fun DetalleScreen(navController: NavController, itemId: Int) {
    val item = AppData.inventario.find { it.id == itemId }

    Scaffold(
        containerColor = BgColor,
        bottomBar = { PanExtBottomNav(navController, Routes.INVENTARIO) }
    ) { padding ->
        if (item == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ingrediente no encontrado")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            BackHeader(title = item.name) { navController.popBackStack() }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                // Hero image
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Gray100)
                        .padding(bottom = 16.dp)
                ) {
                    Text(item.icon, fontSize = 80.sp)
                }

                Spacer(Modifier.height(16.dp))

                // Info card
                PanExtCard(modifier = Modifier.padding(bottom = 12.dp)) {
                    SectionLabel("📋 Información")
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InfoCell(label = "CANTIDAD", value = item.qty.toString(), modifier = Modifier.weight(1f))
                        InfoCell(label = "ESTADO", value = "✓ Bien", isGreen = true, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("REGISTRADO: 05/02/26", fontSize = 11.sp, color = Gray400)
                        Text("EXPIRA: ${item.expira}", fontSize = 11.sp, color = Gray400)
                    }
                }

                // Nutritional card
                PanExtCard(modifier = Modifier.padding(bottom = 12.dp)) {
                    SectionLabel("🍎 Información nutricional")
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        NutriCell("Calorías", item.cal, modifier = Modifier.weight(1f))
                        NutriCell("Proteínas", item.prot, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        NutriCell("Grasas", item.gras, modifier = Modifier.weight(1f))
                        NutriCell("Carbohidratos", item.carb, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun InfoCell(label: String, value: String, isGreen: Boolean = false, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Gray100)
            .padding(14.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Gray400, letterSpacing = 0.8.sp)
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = if (isGreen) GreenDark else Gray800,
                modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun NutriCell(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Gray100)
            .padding(12.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Gray400, letterSpacing = 0.8.sp)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray800, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
