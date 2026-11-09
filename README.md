# EstudiaSmart

EstudiaSmart es una aplicación móvil Android orientada a apoyar a los estudiantes en la organización de sus actividades académicas y en el seguimiento de su aprendizaje.

La aplicación permite administrar materias, temas y materiales de estudio, utilizar flashcards, realizar quizzes, consultar el progreso académico y recibir recomendaciones de estudio.

---

## Avance 2 – Firebase Authentication y Cloud Firestore

En este avance se integraron **Firebase Authentication** y **Cloud Firestore** con el objetivo de incorporar autenticación de usuarios y persistencia de datos en la nube.

La aplicación permite iniciar sesión mediante correo electrónico y contraseña o mediante una cuenta de Google. Además, la información académica de cada usuario queda asociada a su UID de Firebase y almacenada de forma independiente en Cloud Firestore.

---

## Integrantes

- **Josseline Beatriz Pérez Martínez** – PM171434
- **Erick Roberto Zavaleta Rivera** – ZR171491

---

## Repositorio

Repositorio oficial del proyecto:

https://github.com/robertoz04/EstudiaSmart

---

## Video de demostración

Video correspondiente al Avance 2:

https://youtu.be/pGw48jX9L-Q

---

## Documento del Avance 2

El documento completo de investigación, implementación, evidencias, pruebas y conclusiones se encuentra dentro del repositorio.

[Ver documento PDF](documentacion/Avance_2_EstudiaSmart_Firebase_Firestore.pdf)

---

## Funcionalidades implementadas

### Firebase Authentication

- Registro e inicio de sesión mediante correo electrónico y contraseña.
- Inicio de sesión mediante cuenta de Google.
- Manejo de sesión del usuario autenticado.
- Obtención del UID del usuario.
- Cierre de sesión seguro.

### Cloud Firestore

- Persistencia de materias.
- Persistencia de temas.
- Persistencia de materiales.
- Persistencia de flashcards.
- Persistencia de quizzes.
- Persistencia del historial de intentos de Quiz.
- Asociación de la información mediante UID.
- Sincronización de registros almacenados en Firestore.

---

## Estructura de datos

La información de cada usuario se organiza utilizando la siguiente estructura:

```text
usuarios
└── {uid}
    ├── uid
    ├── correo
    ├── nombre
    ├── ultimoAcceso
    │
    ├── materias
    ├── temas
    ├── materiales
    ├── flashcards
    ├── quizzes
    └── historialQuiz
```

Cada usuario mantiene sus propios registros académicos asociados a su UID.

---

## Materias, temas y materiales

La aplicación permite administrar la información académica mediante una estructura lógica:

```text
Materia
   ↓
Tema
   ↓
Material
```

Los registros quedan almacenados en Cloud Firestore y pueden recuperarse posteriormente cuando el usuario vuelve a iniciar sesión.

---

## Flashcards

El módulo de flashcards permite crear tarjetas de estudio relacionadas con los temas.

Además de la pregunta y respuesta, se almacenan datos utilizados para el seguimiento del repaso:

- Dificultad.
- Racha.
- Número de revisiones.
- Próxima fecha de revisión.

Esto permite conservar la información relacionada con la repetición espaciada del estudiante.

---

## Quizzes

La aplicación permite crear quizzes asociados a los temas.

Cada Quiz puede contener:

- Título.
- Tema relacionado.
- Preguntas.
- Opciones de respuesta.
- Respuesta correcta.

Los quizzes quedan almacenados en Cloud Firestore.

---

## Historial de Quiz

Cada vez que un estudiante completa un Quiz se registra un intento.

El historial almacena información como:

- Quiz realizado.
- Respuestas correctas.
- Total de preguntas.
- Porcentaje obtenido.
- Fecha del intento.

Esto permite conservar los resultados aunque la aplicación se cierre.

---

## Seguridad de Firestore

Se implementaron reglas de seguridad para que cada usuario pueda acceder únicamente a la información asociada a su propio UID.

```text
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {

    match /usuarios/{userId} {

      allow read, write:
        if request.auth != null
        && request.auth.uid == userId;

      match /{document=**} {

        allow read, write:
          if request.auth != null
          && request.auth.uid == userId;
      }
    }
  }
}
```

Estas reglas verifican que exista un usuario autenticado y que el UID de la sesión corresponda con el propietario de los datos.

---

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Firebase Authentication
- Cloud Firestore
- Credential Manager
- Git
- GitHub

---

## Arquitectura general

```text
Usuario
   ↓
EstudiaSmart
   ↓
Firebase Authentication
   ↓
UID del usuario
   ↓
Cloud Firestore
   ↓
Datos académicos
```

Cloud Firestore almacena:

```text
Materias
Temas
Materiales
Flashcards
Quizzes
Historial
```

---

## Trabajo colaborativo

El desarrollo se realizó utilizando ramas y commits independientes para conservar evidencia de la participación de los integrantes.

Entre los principales commits del avance se encuentran:

```text
a548263  feat: configurar Firebase Authentication y Firestore
4253ae2  feat: implementar autenticacion Firebase por correo
c181f29  feat: agregar inicio de sesion con Google

3057f29  feat: implementar persistencia de materias en Firestore
45fea71  feat: implementar persistencia de temas en Firestore
38bc5b1  feat: implementar persistencia de materiales en Firestore
c4f866b  feat: implementar persistencia de flashcards en Firestore
584b63c  feat: implementar quizzes e historial en Firestore
```

Posteriormente se realizó la integración mediante:

```text
4f68f68  merge: integrar persistencia Firestore desarrollada por Beatriz
5173d2b  merge: completar Avance 2 con Firebase y Firestore
```

---

## Ejecución del proyecto

Para ejecutar la aplicación:

1. Clonar el repositorio.

```bash
git clone https://github.com/robertoz04/EstudiaSmart.git
```

2. Abrir en Android Studio la carpeta:

```text
EstudiaSmart/android-app
```

3. Sincronizar las dependencias de Gradle.

4. Seleccionar un emulador o dispositivo Android.

5. Ejecutar la aplicación.

---

## Resultado final

Con este avance, EstudiaSmart cuenta con:

- Autenticación por correo electrónico.
- Autenticación mediante Google.
- Persistencia de datos en Cloud Firestore.
- Separación de información por usuario.
- Flashcards persistentes.
- Quizzes almacenados en la nube.
- Historial de resultados.
- Reglas de seguridad por UID.
- Sincronización de información académica.

La integración de Firebase permitió transformar EstudiaSmart en una aplicación con autenticación, persistencia remota y manejo independiente de los datos de cada estudiante.