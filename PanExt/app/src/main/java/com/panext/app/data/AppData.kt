package com.panext.app.data

// ─── Navigation Routes ───────────────────────────────────────────────────────
object Routes {
    const val INICIO         = "inicio"
    const val NOTIFICACIONES = "notificaciones"
    const val PERFIL         = "perfil"
    const val COMPRAS        = "compras"
    const val RECETAS        = "recetas"
    const val RECETAS_VERDES    = "recetas_verdes"
    const val RECETAS_PASTAS    = "recetas_pastas"
    const val RECETAS_DESAYUNOS = "recetas_desayunos"
    const val RECETAS_SOPAS     = "recetas_sopas"
    const val RECETAS_POSTRES   = "recetas_postres"
    const val RECETAS_BEBIDAS   = "recetas_bebidas"
    const val CHAT_IA        = "chat_ia"
    const val INGREDIENTES   = "ingredientes_disponibles"
    const val INVENTARIO     = "inventario"
    const val DETALLE        = "detalle/{itemId}"
    fun detalle(itemId: Int) = "detalle/$itemId"
}

// ─── Data Models ─────────────────────────────────────────────────────────────
data class CompraItem(
    val id: Int,
    val name: String,
    val qty: String,
    var checked: Boolean = false,
    val fromIA: Boolean = false
)

data class InventarioItem(
    val id: Int,
    val name: String,
    val icon: String,
    val expira: String,
    var qty: Int,
    val alert: AlertType? = null,
    val cal: String = "—",
    val prot: String = "—",
    val gras: String = "—",
    val carb: String = "—"
)

enum class AlertType { EXPIRA, POCO }

data class Notificacion(
    val titulo: String,
    val descripcion: String,
    val tiempo: String,
    val tipo: NotifTipo,
    val leida: Boolean = false
)

enum class NotifTipo { URGENTE, AVISO, INFO, IA, SISTEMA }

data class RecetaItem(
    val nombre: String,
    val icon: String,
    val tiempo: String,
    val ingredientes: String,
    val disponible: Boolean,
    val faltanN: Int = 0,
    val porcentaje: Int = 100
)

data class ChatMensaje(
    val texto: String,
    val esUsuario: Boolean
)

// ─── Static Data ─────────────────────────────────────────────────────────────
object AppData {

    val notificaciones = mutableListOf(
        Notificacion("¡Alerta de expiración!", "La leche expira mañana. Úsala pronto.", "Hace 5 min", NotifTipo.URGENTE),
        Notificacion("Stock bajo", "Queda poco pan (1 unidad). Considera comprar más.", "Hace 20 min", NotifTipo.AVISO),
        Notificacion("Recetas disponibles", "Tienes 3 recetas listas con tus ingredientes actuales.", "Hace 1 hora", NotifTipo.INFO),
        Notificacion("IA Sugerencia", "Podrías hacer una pasta con lo que tienes hoy.", "Hace 2 horas", NotifTipo.IA, leida = true),
        Notificacion("Inventario actualizado", "Se añadieron 3 nuevos elementos al inventario.", "Ayer", NotifTipo.SISTEMA, leida = true)
    )

    val compras = mutableListOf(
        CompraItem(1, "Leche entera", "x2", checked = false),
        CompraItem(2, "Pan integral", "x1", checked = false),
        CompraItem(3, "Tomates", "x4", checked = true),
        CompraItem(4, "Huevos", "x12", checked = false),
        CompraItem(5, "Queso manchego", "x1", checked = true),
    )

    val sugerenciasIA = mutableListOf(
        "Aceite de oliva", "Ajo", "Cebolla", "Espinacas", "Yogur natural"
    )

    val inventario = mutableListOf(
        InventarioItem(1, "Leche entera",    "🥛", "22/02/26", 2, AlertType.EXPIRA, "61 kcal",  "3.2g", "3.3g", "4.8g"),
        InventarioItem(2, "Pan integral",    "🍞", "25/02/26", 1, AlertType.POCO,   "247 kcal", "8.5g", "3.4g", "41g"),
        InventarioItem(3, "Huevos",          "🥚", "10/03/26", 6, null,             "155 kcal", "13g",  "11g",  "1.1g"),
        InventarioItem(4, "Queso manchego",  "🧀", "15/03/26", 2, null,             "392 kcal", "25g",  "32g",  "0.5g"),
        InventarioItem(5, "Tomates",         "🍅", "28/02/26", 4, null,             "18 kcal",  "0.9g", "0.2g", "3.9g"),
        InventarioItem(6, "Cebolla",         "🧅", "01/04/26", 3, null,             "40 kcal",  "1.1g", "0.1g", "9.3g"),
        InventarioItem(7, "Aceite de oliva", "🫒", "15/06/26", 1, null,             "884 kcal", "0g",   "100g", "0g"),
        InventarioItem(8, "Ajo",             "🧄", "10/04/26", 5, null,             "149 kcal", "6.4g", "0.5g", "33g"),
    )

    val recetasDisponibles = listOf(
        RecetaItem("Tortilla española",    "🍳", "15 min", "4 ingredientes · Desayuno",  true,  porcentaje = 100),
        RecetaItem("Ensalada griega",      "🥗", "10 min", "5 ingredientes · Ensaladas", true,  porcentaje = 100),
        RecetaItem("Sopa de tomate",       "🍲", "25 min", "4 ingredientes · Sopas",     true,  porcentaje = 100),
        RecetaItem("Wrap vegetariano",     "🫔", "20 min", "6 ingredientes · Vegano",    true,  porcentaje = 100),
        RecetaItem("Guiso de garbanzos",   "🍛", "40 min", "7 ingredientes · Principal", false, faltanN = 1, porcentaje = 86),
        RecetaItem("Tortitas de avena",    "🥞", "12 min", "4 ingredientes · Desayuno",  true,  porcentaje = 100),
        RecetaItem("Pasta con ajo y aceite","🍝","20 min", "4 ingredientes · Pastas",    true,  porcentaje = 100),
    )

    val recetasVerdesDisponibles = listOf(
        RecetaItem("Ensalada mediterránea", "🥗", "20 min", "4 ingredientes disponibles", true),
        RecetaItem("Wrap de espinaca",      "🫔", "15 min", "3 ingredientes disponibles", true),
        RecetaItem("Sopa de acelga",        "🍲", "35 min", "5 ingredientes disponibles", true),
    )

    val recetasVerdesOtras = listOf(
        RecetaItem("Bowl de kale",  "🥬", "", "Faltan 2 ingredientes", false, faltanN = 2),
        RecetaItem("Pesto casero",  "🌿", "", "Faltan 3 ingredientes", false, faltanN = 3),
    )

    val recetasPastasDisponibles = listOf(
        RecetaItem("Pasta con ajo y aceite", "🍝", "20 min", "4 ingredientes disponibles", true),
        RecetaItem("Pasta al limón",         "🍋", "25 min", "5 ingredientes disponibles", true),
    )
    val recetasPastasOtras = listOf(
        RecetaItem("Pasta carbonara",  "🥚", "", "Faltan 2 ingredientes", false, faltanN = 2),
        RecetaItem("Lasaña vegetal",   "🫑", "", "Faltan 4 ingredientes", false, faltanN = 4),
        RecetaItem("Pasta primavera",  "🌸", "", "Faltan 1 ingrediente",  false, faltanN = 1),
    )

    val recetasDesayunosDisponibles = listOf(
        RecetaItem("Tortilla española",  "🍳", "15 min", "4 ingredientes disponibles", true),
        RecetaItem("Tortitas de avena",  "🥞", "12 min", "4 ingredientes disponibles", true),
    )
    val recetasDesayunosOtras = listOf(
        RecetaItem("Granola casera",     "🌾", "", "Faltan 3 ingredientes", false, faltanN = 3),
        RecetaItem("Smoothie de frutas", "🍓", "", "Faltan 2 ingredientes", false, faltanN = 2),
    )

    val recetasSopasDisponibles = listOf(
        RecetaItem("Sopa de tomate", "🍅", "25 min", "4 ingredientes disponibles", true),
        RecetaItem("Caldo de verduras", "🥕", "30 min", "5 ingredientes disponibles", true),
    )
    val recetasSopasOtras = listOf(
        RecetaItem("Crema de calabaza", "🎃", "", "Faltan 2 ingredientes", false, faltanN = 2),
        RecetaItem("Ramen casero",      "🍜", "", "Faltan 5 ingredientes", false, faltanN = 5),
    )

    val recetasPostresDisponibles = listOf(
        RecetaItem("Flan de huevo",   "🍮", "40 min", "3 ingredientes disponibles", true),
    )
    val recetasPostresOtras = listOf(
        RecetaItem("Brownie de chocolate", "🍫", "", "Faltan 3 ingredientes", false, faltanN = 3),
        RecetaItem("Tarta de manzana",     "🍎", "", "Faltan 4 ingredientes", false, faltanN = 4),
        RecetaItem("Mousse de limón",      "🍋", "", "Faltan 2 ingredientes", false, faltanN = 2),
    )

    val recetasBebidasDisponibles = listOf(
        RecetaItem("Limonada natural",  "🍋", "5 min",  "2 ingredientes disponibles", true),
        RecetaItem("Agua de pepino",    "🥒", "10 min", "3 ingredientes disponibles", true),
    )
    val recetasBebidasOtras = listOf(
        RecetaItem("Smoothie verde",    "🥦", "", "Faltan 2 ingredientes", false, faltanN = 2),
        RecetaItem("Té de jengibre",    "🫚", "", "Faltan 1 ingrediente",  false, faltanN = 1),
    )

    val chatReplies = listOf(
        "¡Excelente elección! Con esos ingredientes puedo prepararte algo delicioso en menos de 30 minutos. 🍽️",
        "Perfecto, tengo justo la receta para ti. ¿Prefieres algo frío o caliente?",
        "¡Me encanta! Voy a crear una receta especial basada en tus ingredientes y preferencias. ✨",
        "Con eso puedo hacer algo increíble. Dame un momento para calcular los pasos. 🧑‍🍳",
        "¡Combinación perfecta! Te recomiendo empezar con los ingredientes más frescos primero. 🥗"
    )
}
