package com.panext.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.panext.app.data.AppData
import com.panext.app.data.Notificacion
import com.panext.app.data.NotifTipo
import com.panext.app.data.Routes
import com.panext.app.ui.components.*
import com.panext.app.ui.theme.*

@Composable
fun NotificacionesScreen(navController: NavController) {
    Scaffold(
        containerColor = BgColor,
        bottomBar = { PanExtBottomNav(navController, Routes.NOTIFICACIONES) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            BackHeader(title = "Notificaciones") { navController.popBackStack() }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                PanExtCard {
                    AppData.notificaciones.forEachIndexed { i, notif ->
                        if (i > 0) ItemDivider()
                        NotifRow(notif)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun NotifRow(notif: Notificacion) {
    val (badgeText, badgeBg, badgeFg) = when (notif.tipo) {
        NotifTipo.URGENTE -> Triple("Urgente",  Color(0xFFFDECEA), RedAlert)
        NotifTipo.AVISO   -> Triple("Aviso",    Color(0xFFFEF3E2), OrangeAlert)
        NotifTipo.INFO    -> Triple("Info",     Gray100, Gray600)
        NotifTipo.IA      -> Triple("IA",       Color(0xFFEEF0FF), Color(0xFF5C6BC0))
        NotifTipo.SISTEMA -> Triple("Sistema",  Gray100, Gray600)
    }
    val dotColor = if (notif.leida) Gray200 else GreenDark

    Row(
        modifier = Modifier.padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
                .align(Alignment.Top)
                .padding(top = 6.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(notif.titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray800)
                Badge(badgeText, badgeBg, badgeFg)
            }
            Spacer(Modifier.height(2.dp))
            Text(notif.descripcion, fontSize = 13.sp, color = Gray400)
            Text(notif.tiempo, fontSize = 11.sp, color = Gray400, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
