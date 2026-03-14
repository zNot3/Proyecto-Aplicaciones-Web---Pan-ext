# Pan-Ext — Android Static Prototype

Aplicación de gestión de despensa con inventario, lista de compras, recetas e IA.
Desarrollada con **Kotlin + Jetpack Compose** para Android Studio.

---

## Requisitos

| Herramienta        | Versión mínima  |
|--------------------|-----------------|
| Android Studio     | Hedgehog (2023.1) o superior |
| JDK                | 11+             |
| Android SDK        | API 26 (Android 8.0) mínimo |
| Gradle             | 8.7             |
| Compose BOM        | 2024.08.00      |

---

## Cómo abrir el proyecto

1. Descomprimí el archivo `PanExt.zip`
2. Abrí **Android Studio**
3. Seleccioná **File → Open** y navegá hasta la carpeta `PanExt/`
4. Esperá a que Gradle sincronice (puede tardar unos minutos la primera vez)
5. Corré el proyecto en un emulador o dispositivo físico con **Run → Run 'app'**

> ⚠️ Asegurate de actualizar el `sdk.dir` en `local.properties` con la ruta correcta de tu Android SDK.

---

## Estructura del proyecto

```
PanExt/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/panext/app/
│       │   ├── MainActivity.kt          ← Entry point
│       │   ├── NavGraph.kt              ← Navegación entre pantallas
│       │   ├── data/
│       │   │   └── AppData.kt           ← Modelos y datos estáticos
│       │   └── ui/
│       │       ├── theme/
│       │       │   └── Theme.kt         ← Colores y tema
│       │       ├── components/
│       │       │   └── Components.kt    ← Componentes reutilizables
│       │       └── screens/
│       │           ├── InicioScreen.kt
│       │           ├── NotificacionesScreen.kt
│       │           ├── PerfilScreen.kt
│       │           ├── ComprasScreen.kt
│       │           ├── RecetasScreen.kt  ← Incluye Verdes e Ingredientes
│       │           ├── ChatIAScreen.kt
│       │           └── InventarioScreen.kt ← Incluye DetalleScreen
│       └── res/
│           └── values/
│               ├── strings.xml
│               └── themes.xml
├── gradle/
│   ├── libs.versions.toml               ← Version catalog
│   └── wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## Pantallas implementadas

| Pantalla               | Ruta                     | Interactividad                          |
|------------------------|--------------------------|-----------------------------------------|
| Inicio                 | `inicio`                 | Accesos rápidos a todas las secciones   |
| Notificaciones         | `notificaciones`         | Lista con badges por tipo               |
| Perfil / Yo            | `perfil`                 | Header verde con stats, menú config     |
| Lista de Compras       | `compras`                | ✅ Check/uncheck, eliminar, agregar IA, FAB |
| Recetas                | `recetas`                | Navegación a categorías e IA            |
| Recetas Verdes         | `recetas_verdes`         | Lista disponibles y bloqueadas          |
| Receta con IA          | `chat_ia`                | ✅ Chat funcional con respuestas auto   |
| Ingredientes disponibles | `ingredientes_disponibles` | Lista con % compatibilidad           |
| Inventario             | `inventario`             | ✅ +/- por ítem, alertas, FAB           |
| Detalle ingrediente    | `detalle/{itemId}`       | Info nutricional, cantidad, fechas      |

---

## Tecnologías

- **Kotlin** — lenguaje principal
- **Jetpack Compose** — UI declarativa
- **Navigation Compose** — navegación entre pantallas
- **Material 3** — sistema de diseño
- **State hoisting** — gestión de estado reactivo

---

## Notas

- Este es un prototipo **estático** — los datos viven en memoria (`AppData.kt`) y se resetean al reiniciar la app.
- La pantalla de Chat IA responde con mensajes predefinidos (sin llamadas a API reales).
- Para conectar con backend, reemplazá `AppData` con un `ViewModel` + `Repository`.
