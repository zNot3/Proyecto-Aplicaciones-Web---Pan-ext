package com.panext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.panext.app.data.AppData
import com.panext.app.data.CompraItem
import com.panext.app.data.Routes
import com.panext.app.ui.components.*
import com.panext.app.ui.theme.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComprasScreen(navController: NavController) {
    // Local reactive copies
    var items by remember { mutableStateOf(AppData.compras.toMutableList()) }
    var suggestions by remember { mutableStateOf(AppData.sugerenciasIA.toMutableList()) }
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }

    val completed = items.count { it.checked }
    val pending   = items.count { !it.checked }

    Scaffold(
        containerColor = BgColor,
        bottomBar = { PanExtBottomNav(navController, Routes.COMPRAS) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Gray800,
                contentColor = White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("+", fontSize = 26.sp)
            }
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
            Text("Lista de Compras", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Gray800)
            Text(
                "$completed completado${if (completed != 1) "s" else ""} · $pending pendiente${if (pending != 1) "s" else ""}",
                fontSize = 13.sp, color = Gray400,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            // Items list
            PanExtCard(modifier = Modifier.padding(bottom = 12.dp)) {
                SectionLabel("🛒 Lista")
                items.forEachIndexed { i, item ->
                    if (i > 0) ItemDivider()
                    CompraRow(
                        item = item,
                        onToggle = {
                            items = items.toMutableList().also { list ->
                                val idx = list.indexOfFirst { it.id == item.id }
                                if (idx != -1) list[idx] = list[idx].copy(checked = !list[idx].checked)
                            }
                        },
                        onDelete = {
                            items = items.toMutableList().also { list ->
                                list.removeIf { it.id == item.id }
                            }
                        }
                    )
                }
            }

            // Suggestions
            if (suggestions.isNotEmpty()) {
                PanExtCard(modifier = Modifier.padding(bottom = 12.dp)) {
                    SectionLabel("✨ Sugerencias IA")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { sug ->
                            SuggestionChip(text = sug) {
                                // Add to list, remove from suggestions
                                items = items.toMutableList().also { list ->
                                    list.add(CompraItem(System.currentTimeMillis().toInt(), sug, "x1", fromIA = true))
                                }
                                suggestions = suggestions.toMutableList().also { it.remove(sug) }
                                // keep AppData in sync
                                AppData.compras.clear(); AppData.compras.addAll(items)
                                AppData.sugerenciasIA.clear(); AppData.sugerenciasIA.addAll(suggestions)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }

    // Add item dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; newItemName = "" },
            title = { Text("Nuevo elemento") },
            text = {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("Nombre del producto") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newItemName.isNotBlank()) {
                        items = items.toMutableList().also { list ->
                            list.add(CompraItem(System.currentTimeMillis().toInt(), newItemName.trim(), "x1"))
                        }
                        AppData.compras.clear(); AppData.compras.addAll(items)
                    }
                    showDialog = false; newItemName = ""
                }) { Text("Agregar", color = GreenDark) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; newItemName = "" }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun CompraRow(item: CompraItem, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Check circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (item.checked) Gray800 else White)
                .border(2.dp, if (item.checked) Gray800 else Gray200, CircleShape)
                .clickable { onToggle() }
        ) {
            if (item.checked) Text("✓", fontSize = 13.sp, color = White)
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = item.name,
            fontSize = 15.sp,
            color = if (item.checked) Gray400 else Gray800,
            textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.weight(1f)
        )

        Text(item.qty, fontSize = 13.sp, color = Gray400, modifier = Modifier.padding(end = 8.dp))

        Text(
            "✕", fontSize = 14.sp, color = Gray400,
            modifier = Modifier.clickable { onDelete() }.padding(4.dp)
        )
    }
}
