package com.panext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.panext.app.data.AppData
import com.panext.app.data.ChatMensaje
import com.panext.app.data.Routes
import com.panext.app.ui.components.BackHeader
import com.panext.app.ui.components.PanExtBottomNav
import com.panext.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatIAScreen(navController: NavController) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMensaje("¡Hola! Cuéntame tu idea o dime qué ingredientes tienes disponibles y te creo una receta personalizada 🔍", esUsuario = false),
                ChatMensaje("Tengo pollo, limón y ajo. Quiero algo saludable y rápido", esUsuario = true),
                ChatMensaje("¡Perfecto! Te sugiero un **Pollo al limón con ajo**. Se prepara en 25 min y solo necesitas esos ingredientes más aceite de oliva y sal. ¿Quieres que te dé los pasos detallados?", esUsuario = false),
            )
        )
    }
    var input by remember { mutableStateOf("") }
    var replyIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = BgColor,
        bottomBar = { PanExtBottomNav(navController, Routes.CHAT_IA) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            BackHeader(title = "✨ Receta con IA") { navController.popBackStack() }
            HorizontalDivider(color = Gray100)

            // Messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                }
            }

            HorizontalDivider(color = Gray100)

            // Input
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("Escribe tu idea o ingredientes…", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Gray200,
                        focusedBorderColor = GreenDark,
                        unfocusedContainerColor = Gray100,
                        focusedContainerColor = Gray100
                    )
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Gray800)
                ) {
                    IconButton(onClick = {
                        if (input.isNotBlank()) {
                            val userMsg = input.trim()
                            input = ""
                            val reply = AppData.chatReplies[replyIndex % AppData.chatReplies.size]
                            replyIndex++
                            messages = messages + ChatMensaje(userMsg, esUsuario = true)
                            scope.launch {
                                kotlinx.coroutines.delay(600)
                                messages = messages + ChatMensaje(reply, esUsuario = false)
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }) {
                        Text("▶", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMensaje) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.esUsuario) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    if (msg.esUsuario)
                        RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp)
                    else
                        RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp)
                )
                .background(if (msg.esUsuario) Gray800 else Color.White)
                .padding(12.dp, 10.dp)
        ) {
            Text(
                text = msg.texto,
                fontSize = 14.sp,
                color = if (msg.esUsuario) Color.White else Gray800,
                lineHeight = 20.sp
            )
        }
    }
}
