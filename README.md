# Sprint (RunTracker)

**Sprint** es una aplicación Android nativa para registrar actividad física, consultar el clima antes de entrenar y hacer seguimiento del progreso personal. El proyecto combina autenticación en la nube, persistencia de datos y consumo de APIs externas.

| | |
|---|---|
| **Nombre en pantalla** | Sprint |
| **Package** | `com.example.sprint` |
| **Lenguaje** | Java 11 |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 |

## Funcionalidades

- **Autenticación:** registro e inicio de sesión con email/contraseña y Google Sign-In (Firebase Auth).
- **Dashboard:** resumen diario de distancia, calorías estimadas, duración, carreras del día, objetivo de pasos y racha de días consecutivos.
- **Entrenamientos:** registro de correr, caminar, senderismo y ciclismo con cronómetro, pausa/reanudar y métricas en tiempo real.
- **Entrenamientos personalizados:** creación de rutinas con nombre y meta opcional (tiempo o distancia).
- **Perfil:** edición de datos personales, foto de perfil, IMC y logros desbloqueables.
- **Historial:** listado de carreras guardadas con opción de borrado masivo.
- **Clima:** consulta del clima actual vía OpenWeatherMap con recomendaciones para entrenar.

## Stack tecnológico

- **UI:** Activities, XML layouts, Material Design
- **Backend:** Firebase Authentication + Cloud Firestore
- **Ubicación:** Google Play Services Location (Fused Location Provider)
- **Red:** Retrofit 2 + Gson
- **Clima:** OpenWeatherMap API
- **Build:** Gradle 9.5, Android Gradle Plugin 9.2.1

## Arquitectura

La app sigue una estructura clásica de Android con separación por capas:

```
Activities (UI)
    ↓
Repositories (RunRepository, WorkoutRepository)
    ↓
Firebase / modelos de dominio (Run, Workout, ActivityType)
    ↓
Clases de estadísticas (DashboardStats, RunStats)
```

Los datos de usuario se guardan en Firestore bajo `users/{uid}`, con subcolecciones `runs` y `workouts`.

## Requisitos previos

- [Android Studio](https://developer.android.com/studio) (versión reciente, recomendado Ladybug o superior)
- **JDK 11** o superior
- Cuenta de [Firebase](https://console.firebase.google.com/)
- API key de [OpenWeatherMap](https://openweathermap.org/api) (plan gratuito disponible)
- Dispositivo físico o emulador con **Android 7.0+** e internet

## Cómo inicializar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/gonreynoso/parcial-2-am-acn4a-reynoso-vitale.git
cd parcial-2-am-acn4a-reynoso-vitale
```

### 2. Abrir en Android Studio

1. **File → Open** y seleccioná la carpeta del proyecto.
2. Esperá a que Gradle sincronice las dependencias (puede tardar unos minutos la primera vez).
3. Si Android Studio pide instalar componentes del SDK (API 36, Build Tools, etc.), aceptá la instalación.

### 3. Configurar Firebase

El archivo `app/google-services.json` debe estar presente en el módulo `app/`. Si vas a usar tu propio proyecto Firebase:

1. Creá un proyecto en [Firebase Console](https://console.firebase.google.com/).
2. Agregá una app Android con el package `com.example.sprint`.
3. Descargá `google-services.json` y reemplazá el existente en `app/`.
4. En Firebase, activá:
   - **Authentication** → Email/Password y Google
   - **Cloud Firestore** → base de datos en modo producción o prueba
5. Para **Google Sign-In**, registrá el SHA-1 de tu keystore de debug en la consola de Firebase:

   ```bash
   # Windows (desde la carpeta del proyecto)
   .\gradlew signingReport
   ```

6. Configurá reglas de Firestore para que cada usuario acceda solo a sus datos (por ejemplo, restringiendo lectura/escritura por `request.auth.uid`).

### 4. Configurar OpenWeatherMap

En `WeatherActivity.java`, reemplazá la constante `API_KEY` por tu propia clave de OpenWeatherMap:

```java
private final String API_KEY = "TU_API_KEY_AQUI";
```

> **Nota:** en producción conviene mover la clave a `local.properties` o variables de entorno, no dejarla hardcodeada en el código.

### 5. Ejecutar la app

1. Conectá un dispositivo con depuración USB activada **o** iniciá un emulador.
2. Seleccioná el módulo `app` y pulsá **Run** (▶).
3. La app arranca en `SplashActivity`, verifica la sesión y redirige a login o al dashboard.

### 6. Build desde terminal (opcional)

```bash
# Windows
.\gradlew assembleDebug

# Linux / macOS
./gradlew assembleDebug
```

El APK de debug se genera en `app/build/outputs/apk/debug/`.

## Permisos

La app solicita:

| Permiso | Uso |
|---------|-----|
| `INTERNET` | Firebase, OpenWeatherMap |
| `ACCESS_FINE_LOCATION` | Ubicación (preparado para tracking GPS) |
| `ACCESS_COARSE_LOCATION` | Ubicación aproximada |

## Estructura del proyecto

```
app/src/main/java/com/example/runtracker/
├── SplashActivity.java          # Pantalla inicial
├── LoginActivity.java           # Autenticación
├── MainActivity.java            # Dashboard
├── WorkoutSelectionActivity.java
├── WorkoutTrackingActivity.java # Sesión de entrenamiento
├── CreateWorkoutActivity.java
├── ProfileActivity.java
├── EditProfileActivity.java
├── MyRunsActivity.java
├── WeatherActivity.java
├── RunRepository.java           # Persistencia de carreras
├── WorkoutRepository.java       # Entrenamientos personalizados
├── Stopwatch.java               # Cronómetro con pausa
├── ActivityType.java            # Tipos de actividad
├── DashboardStats.java          # Métricas del dashboard
└── climaApi/                    # Cliente Retrofit del clima
```

## Flujo principal

```
Splash → Login (si no hay sesión) → Dashboard
                                    ├── Clima
                                    ├── Iniciar entrenamiento → Tracking
                                    └── Perfil → Editar / Historial
```

## Documentación adicional

Informe técnico del proyecto (Word):

`docs/informe/Informe_Sprint_RunTracker.docx`

## Autores

Proyecto académico — **Gonzalo Agustín Reynoso** y **Tobias Vitale**.
