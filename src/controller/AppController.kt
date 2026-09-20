package controller

import model.*
import service.*
import util.ErrorLogger

class AppController {

    private val authService = AuthService()
    private val materiaService = MateriaService()
    private val temaService = TemaService()
    private val materialService = MaterialService()

    private val flashcardService = FlashcardService()
    private val quizService = QuizService()
    private val progresoService = ProgresoService()
    private val recomendacionService = RecomendacionService()
    private val reporteService = ReporteService()

    private val flashcards = mutableListOf<Flashcard>()
    private val quizzes = mutableListOf<Quiz>()
    private val intentos = mutableListOf<IntentoQuiz>()
    private val progresos = mutableListOf<ProgresoTema>()

    private var siguienteFlashcardId = 1
    private var siguienteQuizId = 1
    private var siguientePreguntaId = 1
    private var siguienteIntentoId = 1

    init {
        authService.registrarAdministrador(
            nombre = "Administrador",
            correo = "admin@estudiasmart.com",
            contrasena = "admin123"
        )
    }

    fun iniciar() {

        var salir = false

        while (!salir) {

            println()
            println("================================")
            println("          ESTUDIASMART")
            println("================================")
            println("1. Iniciar sesión")
            println("2. Registrarse")
            println("3. Salir")
            println("--------------------------------")
            print("Seleccione una opción: ")

            when (leerEntero()) {

                1 -> iniciarSesion()

                2 -> registrarEstudiante()

                3 -> {
                    println()
                    println("Gracias por utilizar EstudiaSmart.")
                    salir = true
                }

                else -> {
                    println()
                    println("Opción inválida. Intente nuevamente.")
                }
            }
        }
    }

    private fun registrarEstudiante() {

        println()
        println("================================")
        println("      REGISTRO DE ESTUDIANTE")
        println("================================")

        try {

            print("Nombre: ")
            val nombre = readln().trim()

            print("Correo: ")
            val correo = readln().trim()

            print("Contraseña: ")
            val contrasena = readln()

            val estudiante = authService.registrarEstudiante(
                nombre = nombre,
                correo = correo,
                contrasena = contrasena
            )

            println()
            println("Estudiante registrado correctamente.")
            println("ID asignado: ${estudiante.id}")

        } catch (error: Exception) {
            manejarError(error)
        }
    }

    private fun iniciarSesion() {

        println()
        println("================================")
        println("         INICIAR SESIÓN")
        println("================================")

        print("Correo: ")
        val correo = readln().trim()

        print("Contraseña: ")
        val contrasena = readln()

        val usuario = authService.iniciarSesion(
            correo = correo,
            contrasena = contrasena
        )

        if (usuario == null) {

            println()
            println("Correo o contraseña incorrectos.")

            return
        }

        println()
        println("Bienvenido, ${usuario.nombre}.")

        when (usuario) {

            is Estudiante -> menuEstudiante(usuario)

            is Administrador -> menuAdministrador()

            else -> println("Rol no reconocido.")
        }
    }

    private fun menuEstudiante(estudiante: Estudiante) {

        var cerrarSesion = false

        while (!cerrarSesion) {

            println()
            println("================================")
            println("        MENÚ ESTUDIANTE")
            println("================================")
            println("1. Materias")
            println("2. Temas")
            println("3. Materiales")
            println("4. Flashcards")
            println("5. Quizzes")
            println("6. Mi progreso")
            println("7. ¿Qué debo estudiar hoy?")
            println("8. Reporte personal")
            println("9. Cerrar sesión")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> menuMaterias(estudiante)

                    2 -> menuTemas(estudiante)

                    3 -> menuMateriales(estudiante)

                    4 -> menuFlashcards(estudiante)

                    5 -> menuQuizzes(estudiante)

                    6 -> mostrarProgreso(estudiante)

                    7 -> mostrarRecomendaciones(estudiante)

                    8 -> mostrarReportePersonal(estudiante)

                    9 -> {
                        println()
                        println("Sesión cerrada correctamente.")
                        cerrarSesion = true
                    }

                    else -> {
                        println()
                        println("Opción inválida. Intente nuevamente.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun menuAdministrador() {

        var cerrarSesion = false

        while (!cerrarSesion) {

            println()
            println("================================")
            println("       MENÚ ADMINISTRADOR")
            println("================================")
            println("1. Ver usuarios registrados")
            println("2. Ver reporte general")
            println("3. Cerrar sesión")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> mostrarUsuarios()

                    2 -> mostrarReporteGeneral()

                    3 -> {
                        println()
                        println("Sesión de administrador cerrada.")
                        cerrarSesion = true
                    }

                    else -> {
                        println()
                        println("Opción inválida. Intente nuevamente.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun mostrarUsuarios() {

        val usuarios = authService.listarUsuarios()

        println()
        println("================================")
        println("       USUARIOS REGISTRADOS")
        println("================================")

        if (usuarios.isEmpty()) {

            println("No hay usuarios registrados.")
            return
        }

        usuarios.forEach { usuario ->

            val estado =
                if (usuario.activo) {
                    "ACTIVO"
                } else {
                    "INACTIVO"
                }

            println(
                "${usuario.id}. ${usuario.nombre}"
            )

            println(
                "   Correo: ${usuario.correo}"
            )

            println(
                "   Rol: ${usuario.rol}"
            )

            println(
                "   Estado: $estado"
            )

            println("--------------------------------")
        }
    }

    private fun menuMaterias(estudiante: Estudiante) {

        var regresar = false

        while (!regresar) {

            println()
            println("================================")
            println("             MATERIAS")
            println("================================")
            println("1. Crear materia")
            println("2. Listar materias")
            println("3. Actualizar materia")
            println("4. Eliminar materia")
            println("5. Regresar")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> {

                        println()
                        println("CREAR MATERIA")
                        println("--------------------------------")

                        print("Nombre: ")
                        val nombre = readln().trim()

                        print("Descripción: ")
                        val descripcion = readln().trim()

                        val materia =
                            materiaService.crearMateria(
                                estudianteId = estudiante.id,
                                nombre = nombre,
                                descripcion = descripcion
                            )

                        println()
                        println(
                            "Materia '${materia.nombre}' creada correctamente."
                        )
                    }

                    2 -> {

                        val materias =
                            materiaService.listarMaterias(
                                estudiante.id
                            )

                        println()
                        println("MIS MATERIAS")
                        println("--------------------------------")

                        if (materias.isEmpty()) {

                            println("No hay materias registradas.")

                        } else {

                            materias.forEach { materia ->

                                println(
                                    "${materia.id}. ${materia.nombre}"
                                )

                                println(
                                    "   ${materia.descripcion}"
                                )

                                println("--------------------------------")
                            }
                        }
                    }

                    3 -> {

                        println()
                        println("ACTUALIZAR MATERIA")
                        println("--------------------------------")

                        print("ID de la materia: ")
                        val id = leerEntero()

                        print("Nuevo nombre: ")
                        val nombre = readln().trim()

                        print("Nueva descripción: ")
                        val descripcion = readln().trim()

                        val resultado =
                            materiaService.actualizarMateria(
                                id = id,
                                estudianteId = estudiante.id,
                                nuevoNombre = nombre,
                                nuevaDescripcion = descripcion
                            )

                        println()

                        println(
                            if (resultado) {
                                "Materia actualizada correctamente."
                            } else {
                                "Materia no encontrada."
                            }
                        )
                    }

                    4 -> {

                        println()
                        println("ELIMINAR MATERIA")
                        println("--------------------------------")

                        print("ID de la materia: ")
                        val id = leerEntero()

                        val resultado =
                            materiaService.eliminarMateria(
                                id = id,
                                estudianteId = estudiante.id
                            )

                        println()

                        println(
                            if (resultado) {
                                "Materia eliminada correctamente."
                            } else {
                                "Materia no encontrada."
                            }
                        )
                    }

                    5 -> regresar = true

                    else -> {
                        println()
                        println("Opción inválida.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun menuTemas(estudiante: Estudiante) {

        var regresar = false

        while (!regresar) {

            println()
            println("================================")
            println("              TEMAS")
            println("================================")
            println("1. Crear tema")
            println("2. Listar temas")
            println("3. Actualizar tema")
            println("4. Eliminar tema")
            println("5. Regresar")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> {

                        println()
                        println("CREAR TEMA")
                        println("--------------------------------")

                        print("ID de la materia: ")
                        val materiaId = leerEntero()

                        val materia =
                            materiaService.buscarMateria(
                                materiaId,
                                estudiante.id
                            )

                        if (materia == null) {

                            println()
                            println(
                                "La materia indicada no existe."
                            )

                        } else {

                            print("Nombre del tema: ")
                            val nombre = readln().trim()

                            print("Descripción: ")
                            val descripcion = readln().trim()

                            val tema =
                                temaService.crearTema(
                                    materiaId = materiaId,
                                    estudianteId = estudiante.id,
                                    nombre = nombre,
                                    descripcion = descripcion
                                )

                            println()
                            println(
                                "Tema '${tema.nombre}' creado correctamente."
                            )
                        }
                    }

                    2 -> {

                        print("ID de la materia: ")
                        val materiaId = leerEntero()

                        val temas =
                            temaService.listarTemas(
                                materiaId,
                                estudiante.id
                            )

                        println()
                        println("TEMAS")
                        println("--------------------------------")

                        if (temas.isEmpty()) {

                            println("No hay temas registrados.")

                        } else {

                            temas.forEach { tema ->

                                println(
                                    "${tema.id}. ${tema.nombre}"
                                )

                                println(
                                    "   ${tema.descripcion}"
                                )

                                println("--------------------------------")
                            }
                        }
                    }

                    3 -> {

                        println()
                        println("ACTUALIZAR TEMA")
                        println("--------------------------------")

                        print("ID del tema: ")
                        val id = leerEntero()

                        print("Nuevo nombre: ")
                        val nombre = readln().trim()

                        print("Nueva descripción: ")
                        val descripcion = readln().trim()

                        val resultado =
                            temaService.actualizarTema(
                                id = id,
                                estudianteId = estudiante.id,
                                nuevoNombre = nombre,
                                nuevaDescripcion = descripcion
                            )

                        println()

                        println(
                            if (resultado) {
                                "Tema actualizado correctamente."
                            } else {
                                "Tema no encontrado."
                            }
                        )
                    }

                    4 -> {

                        println()
                        println("ELIMINAR TEMA")
                        println("--------------------------------")

                        print("ID del tema: ")
                        val id = leerEntero()

                        val resultado =
                            temaService.eliminarTema(
                                id = id,
                                estudianteId = estudiante.id
                            )

                        println()

                        println(
                            if (resultado) {
                                "Tema eliminado correctamente."
                            } else {
                                "Tema no encontrado."
                            }
                        )
                    }

                    5 -> regresar = true

                    else -> {
                        println()
                        println("Opción inválida.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun menuMateriales(estudiante: Estudiante) {

        var regresar = false

        while (!regresar) {

            println()
            println("================================")
            println("           MATERIALES")
            println("================================")
            println("1. Crear material")
            println("2. Listar materiales")
            println("3. Actualizar material")
            println("4. Eliminar material")
            println("5. Regresar")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> {

                        println()
                        println("CREAR MATERIAL")
                        println("--------------------------------")

                        print("ID del tema: ")
                        val temaId = leerEntero()

                        val tema =
                            temaService.buscarTema(
                                temaId,
                                estudiante.id
                            )

                        if (tema == null) {

                            println()
                            println(
                                "El tema indicado no existe."
                            )

                        } else {

                            print("Título: ")
                            val titulo = readln().trim()

                            print("Contenido: ")
                            val contenido = readln().trim()

                            val material =
                                materialService.crearMaterial(
                                    temaId = temaId,
                                    estudianteId = estudiante.id,
                                    titulo = titulo,
                                    contenido = contenido
                                )

                            println()
                            println(
                                "Material '${material.titulo}' creado correctamente."
                            )
                        }
                    }

                    2 -> {

                        print("ID del tema: ")
                        val temaId = leerEntero()

                        val materiales =
                            materialService.listarMateriales(
                                temaId,
                                estudiante.id
                            )

                        println()
                        println("MATERIALES")
                        println("--------------------------------")

                        if (materiales.isEmpty()) {

                            println(
                                "No hay materiales registrados."
                            )

                        } else {

                            materiales.forEach { material ->

                                println(
                                    "${material.id}. ${material.titulo}"
                                )

                                println(
                                    "   ${material.contenido}"
                                )

                                println("--------------------------------")
                            }
                        }
                    }

                    3 -> {

                        println()
                        println("ACTUALIZAR MATERIAL")
                        println("--------------------------------")

                        print("ID del material: ")
                        val id = leerEntero()

                        print("Nuevo título: ")
                        val titulo = readln().trim()

                        print("Nuevo contenido: ")
                        val contenido = readln().trim()

                        val resultado =
                            materialService.actualizarMaterial(
                                id = id,
                                estudianteId = estudiante.id,
                                nuevoTitulo = titulo,
                                nuevoContenido = contenido
                            )

                        println()

                        println(
                            if (resultado) {
                                "Material actualizado correctamente."
                            } else {
                                "Material no encontrado."
                            }
                        )
                    }

                    4 -> {

                        println()
                        println("ELIMINAR MATERIAL")
                        println("--------------------------------")

                        print("ID del material: ")
                        val id = leerEntero()

                        val resultado =
                            materialService.eliminarMaterial(
                                id = id,
                                estudianteId = estudiante.id
                            )

                        println()

                        println(
                            if (resultado) {
                                "Material eliminado correctamente."
                            } else {
                                "Material no encontrado."
                            }
                        )
                    }

                    5 -> regresar = true

                    else -> {
                        println()
                        println("Opción inválida.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun menuFlashcards(estudiante: Estudiante) {

        var regresar = false

        while (!regresar) {

            println()
            println("================================")
            println("           FLASHCARDS")
            println("================================")
            println("1. Crear flashcard")
            println("2. Listar flashcards")
            println("3. Revisar flashcard")
            println("4. Ver flashcards pendientes")
            println("5. Regresar")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> crearFlashcard(estudiante)

                    2 -> listarFlashcards(estudiante)

                    3 -> revisarFlashcard(estudiante)

                    4 ->
                        mostrarFlashcardsPendientes(
                            estudiante
                        )

                    5 -> regresar = true

                    else -> {
                        println()
                        println("Opción inválida.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun crearFlashcard(estudiante: Estudiante) {

        println()
        println("CREAR FLASHCARD")
        println("--------------------------------")

        print("ID del tema: ")
        val temaId = leerEntero()

        val tema =
            temaService.buscarTema(
                temaId,
                estudiante.id
            )

        if (tema == null) {

            println()
            println("El tema indicado no existe.")

            return
        }

        print("Pregunta: ")
        val pregunta = readln().trim()

        print("Respuesta: ")
        val respuesta = readln().trim()

        require(pregunta.isNotBlank()) {
            "La pregunta no puede estar vacía."
        }

        require(respuesta.isNotBlank()) {
            "La respuesta no puede estar vacía."
        }

        val flashcard =
            Flashcard(
                id = siguienteFlashcardId++,
                temaId = temaId,
                pregunta = pregunta,
                respuesta = respuesta
            )

        flashcards.add(flashcard)

        println()
        println("Flashcard creada correctamente.")
    }

    private fun listarFlashcards(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val tarjetas =
            flashcards.filter {
                it.temaId in temaIds
            }

        println()
        println("MIS FLASHCARDS")
        println("--------------------------------")

        if (tarjetas.isEmpty()) {

            println("No hay flashcards registradas.")

            return
        }

        tarjetas.forEach { flashcard ->

            println(
                "${flashcard.id}. ${flashcard.pregunta}"
            )

            println(
                "   Dificultad: ${flashcard.dificultad}"
            )

            println(
                "   Racha de aciertos: " +
                flashcard.rachaAciertos
            )

            println(
                "   Próxima revisión: " +
                "${flashcard.proximaRevision ?: "Pendiente"}"
            )

            println("--------------------------------")
        }
    }

    private fun revisarFlashcard(
        estudiante: Estudiante
    ) {

        println()
        println("REVISAR FLASHCARD")
        println("--------------------------------")

        print("ID de la flashcard: ")
        val id = leerEntero()

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val flashcard =
            flashcards.find {
                it.id == id &&
                it.temaId in temaIds
            }

        if (flashcard == null) {

            println()
            println("Flashcard no encontrada.")

            return
        }

        println()
        println("Pregunta:")
        println(flashcard.pregunta)

        println()
        print(
            "Presione ENTER para mostrar la respuesta..."
        )
        readln()

        println()
        println("Respuesta:")
        println(flashcard.respuesta)

        println()
        println("Seleccione la dificultad:")
        println("1. Difícil")
        println("2. Regular")
        println("3. Fácil")
        println()
        println(
            "Puede escribir el número o el nombre."
        )
        print("Opción: ")

        val dificultad = leerDificultad()

        println()
        print(
            "¿Respondió correctamente? (s/n): "
        )

        val acierto = leerSiNo()

        flashcardService.revisarFlashcard(
            flashcard = flashcard,
            dificultadSeleccionada = dificultad,
            acierto = acierto
        )

        actualizarProgresoDesdeFlashcard(
            temaId = flashcard.temaId
        )

        println()
        println("Revisión registrada correctamente.")

        println(
            "Dificultad: ${flashcard.dificultad}"
        )

        println(
            "Racha de aciertos: " +
            flashcard.rachaAciertos
        )

        println(
            "Última revisión: " +
            flashcard.ultimaRevision
        )

        println(
            "Próxima revisión: " +
            flashcard.proximaRevision
        )
    }

    private fun mostrarFlashcardsPendientes(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val tarjetas =
            flashcards.filter {
                it.temaId in temaIds
            }

        val pendientes =
            flashcardService
                .obtenerFlashcardsPendientes(
                    tarjetas
                )

        println()
        println("FLASHCARDS PENDIENTES")
        println("--------------------------------")

        if (pendientes.isEmpty()) {

            println("No hay flashcards pendientes.")

            return
        }

        pendientes.forEach {

            println(
                "${it.id}. ${it.pregunta}"
            )

            println(
                "   Próxima revisión: " +
                "${it.proximaRevision ?: "Hoy"}"
            )

            println("--------------------------------")
        }
    }

    private fun menuQuizzes(estudiante: Estudiante) {

        var regresar = false

        while (!regresar) {

            println()
            println("================================")
            println("             QUIZZES")
            println("================================")
            println("1. Crear quiz")
            println("2. Listar quizzes")
            println("3. Realizar quiz")
            println("4. Regresar")
            println("--------------------------------")
            print("Seleccione una opción: ")

            try {

                when (leerEntero()) {

                    1 -> crearQuiz(estudiante)

                    2 -> listarQuizzes(estudiante)

                    3 -> realizarQuiz(estudiante)

                    4 -> regresar = true

                    else -> {
                        println()
                        println("Opción inválida.")
                    }
                }

            } catch (error: Exception) {
                manejarError(error)
            }
        }
    }

    private fun crearQuiz(estudiante: Estudiante) {

        println()
        println("CREAR QUIZ")
        println("--------------------------------")

        print("ID del tema: ")
        val temaId = leerEntero()

        val tema =
            temaService.buscarTema(
                temaId,
                estudiante.id
            )

        if (tema == null) {

            println()
            println("El tema indicado no existe.")

            return
        }

        print("Nombre del quiz: ")
        val nombre = readln().trim()

        require(nombre.isNotBlank()) {
            "El nombre del quiz no puede estar vacío."
        }

        val quiz =
            Quiz(
                id = siguienteQuizId++,
                temaId = temaId,
                nombre = nombre
            )

        println()
        println("Ingrese 3 preguntas para el quiz.")

        repeat(3) { indice ->

            println()
            println("--------------------------------")
            println("Pregunta ${indice + 1}")
            println("--------------------------------")

            print("Enunciado: ")
            val enunciado = readln().trim()

            print("Opción A: ")
            val opcionA = readln().trim()

            print("Opción B: ")
            val opcionB = readln().trim()

            print("Opción C: ")
            val opcionC = readln().trim()

            print("Opción D: ")
            val opcionD = readln().trim()

            require(enunciado.isNotBlank()) {
                "El enunciado no puede estar vacío."
            }

            require(
                opcionA.isNotBlank() &&
                opcionB.isNotBlank() &&
                opcionC.isNotBlank() &&
                opcionD.isNotBlank()
            ) {
                "Las opciones no pueden estar vacías."
            }

            print(
                "Respuesta correcta (A/B/C/D): "
            )

            val respuestaCorrecta =
                leerRespuestaABCD()

            quiz.preguntas.add(
                Pregunta(
                    id = siguientePreguntaId++,
                    quizId = quiz.id,
                    enunciado = enunciado,
                    opcionA = opcionA,
                    opcionB = opcionB,
                    opcionC = opcionC,
                    opcionD = opcionD,
                    respuestaCorrecta =
                        respuestaCorrecta
                )
            )
        }

        quizzes.add(quiz)

        println()
        println("Quiz creado correctamente.")
    }

    private fun listarQuizzes(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val quizzesEstudiante =
            quizzes.filter {
                it.temaId in temaIds
            }

        println()
        println("MIS QUIZZES")
        println("--------------------------------")

        if (quizzesEstudiante.isEmpty()) {

            println("No hay quizzes registrados.")

            return
        }

        quizzesEstudiante.forEach {

            println(
                "${it.id}. ${it.nombre}"
            )

            println(
                "   Preguntas: ${it.preguntas.size}"
            )

            println("--------------------------------")
        }
    }

    private fun realizarQuiz(
        estudiante: Estudiante
    ) {

        println()
        println("REALIZAR QUIZ")
        println("--------------------------------")

        print("ID del quiz: ")
        val quizId = leerEntero()

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val quiz =
            quizzes.find {
                it.id == quizId &&
                it.temaId in temaIds
            }

        if (quiz == null) {

            println()
            println("Quiz no encontrado.")

            return
        }

        val respuestas =
            mutableMapOf<Int, Char>()

        quiz.preguntas.forEachIndexed {
            indice,
            pregunta ->

            println()
            println("--------------------------------")
            println(
                "Pregunta ${indice + 1} de " +
                quiz.preguntas.size
            )
            println("--------------------------------")

            println(pregunta.enunciado)
            println("A. ${pregunta.opcionA}")
            println("B. ${pregunta.opcionB}")
            println("C. ${pregunta.opcionC}")
            println("D. ${pregunta.opcionD}")

            print("Respuesta: ")

            respuestas[pregunta.id] =
                leerRespuestaABCD()
        }

        val intento =
            quizService.evaluarQuiz(
                quiz = quiz,
                respuestasUsuario = respuestas,
                idIntento = siguienteIntentoId++
            )

        intentos.add(intento)

        println()
        println("================================")
        println("        RESULTADO DEL QUIZ")
        println("================================")

        println(
            "Respuestas correctas: " +
            "${intento.respuestasCorrectas}/" +
            "${intento.totalPreguntas}"
        )

        println(
            "Puntuación: " +
            "${"%.1f".format(intento.puntuacion)}%"
        )

        println(
            "Nivel: " +
            quizService.obtenerNivelDominio(
                intento.puntuacion
            )
        )

        println(
            "Preguntas falladas: " +
            if (
                intento.preguntasFalladas.isEmpty()
            ) {
                "Ninguna"
            } else {
                intento.preguntasFalladas
                    .joinToString()
            }
        )

        actualizarProgresoDesdeQuiz(
            temaId = quiz.temaId,
            puntuacion = intento.puntuacion
        )
    }

    private fun actualizarProgresoDesdeQuiz(
        temaId: Int,
        puntuacion: Double
    ) {

        val tarjetasTema =
            flashcards.filter {
                it.temaId == temaId
            }

        val rendimientoFlashcards =
            calcularRendimientoFlashcards(
                tarjetasTema
            )

        val progreso =
            progresos.find {
                it.temaId == temaId
            } ?: ProgresoTema(
                temaId = temaId
            ).also {
                progresos.add(it)
            }

        progresoService.actualizarProgreso(
            progreso = progreso,
            precisionQuiz = puntuacion,
            rendimientoFlashcards =
                rendimientoFlashcards
        )
    }

    private fun actualizarProgresoDesdeFlashcard(
        temaId: Int
    ) {

        val progreso =
            progresos.find {
                it.temaId == temaId
            } ?: return

        val tarjetasTema =
            flashcards.filter {
                it.temaId == temaId
            }

        val rendimientoFlashcards =
            calcularRendimientoFlashcards(
                tarjetasTema
            )

        progresoService.actualizarProgreso(
            progreso = progreso,
            precisionQuiz =
                progreso.precisionQuiz,
            rendimientoFlashcards =
                rendimientoFlashcards
        )
    }

    private fun calcularRendimientoFlashcards(
        tarjetas: List<Flashcard>
    ): Double {

        if (tarjetas.isEmpty()) {
            return 0.0
        }

        val acertadas =
            tarjetas.count {
                it.rachaAciertos > 0
            }

        return (
            acertadas.toDouble() /
            tarjetas.size
        ) * 100.0
    }

    private fun mostrarProgreso(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val progresosEstudiante =
            progresos.filter {
                it.temaId in temaIds
            }

        println()
        println("================================")
        println("           MI PROGRESO")
        println("================================")

        if (progresosEstudiante.isEmpty()) {

            println(
                "Todavía no existen resultados de progreso."
            )

            println(
                "Realice un quiz para comenzar."
            )

            return
        }

        progresosEstudiante.forEach {
            progreso ->

            val tema =
                temaService.buscarTema(
                    progreso.temaId,
                    estudiante.id
                )

            println()
            println(
                tema?.nombre
                    ?: "Tema ${progreso.temaId}"
            )

            println("--------------------------------")

            println(
                "Quiz: " +
                "${"%.1f".format(progreso.precisionQuiz)}%"
            )

            println(
                "Flashcards: " +
                "${"%.1f".format(progreso.rendimientoFlashcards)}%"
            )

            println(
                "Dominio: " +
                "${"%.1f".format(progreso.dominio)}%"
            )

            println(
                "Nivel: " +
                progresoService.obtenerNivelDominio(
                    progreso.dominio
                )
            )

            println("--------------------------------")
        }
    }

    private fun mostrarRecomendaciones(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val progresosEstudiante =
            progresos.filter {
                it.temaId in temaIds
            }

        if (progresosEstudiante.isEmpty()) {

            println()
            println(
                "Primero debe realizar al menos un quiz."
            )

            return
        }

        val estados =
            progresosEstudiante.map {
                progreso ->

                val tarjetasTema =
                    flashcards.filter {
                        it.temaId ==
                            progreso.temaId
                    }

                val ultimaRevision =
                    tarjetasTema
                        .mapNotNull {
                            it.ultimaRevision
                        }
                        .maxOrNull()

                val dificultad =
                    tarjetasTema
                        .maxByOrNull {
                            when (it.dificultad) {

                                Dificultad.DIFICIL ->
                                    3

                                Dificultad.REGULAR ->
                                    2

                                Dificultad.FACIL ->
                                    1
                            }
                        }
                        ?.dificultad
                        ?: Dificultad.REGULAR

                val tema =
                    temaService.buscarTema(
                        progreso.temaId,
                        estudiante.id
                    )

                EstadoTema(
                    temaId = progreso.temaId,
                    nombreTema =
                        tema?.nombre
                            ?: "Tema ${progreso.temaId}",
                    dominio = progreso.dominio,
                    ultimaRevision =
                        ultimaRevision,
                    dificultad = dificultad
                )
            }

        val recomendaciones =
            recomendacionService
                .generarRecomendaciones(
                    temas = estados
                )

        println()
        println("================================")
        println("     ¿QUÉ DEBO ESTUDIAR HOY?")
        println("================================")

        recomendaciones.forEachIndexed {
            indice,
            recomendacion ->

            println()
            println(
                "${indice + 1}. " +
                recomendacion.nombreTema
            )

            println(
                "   Dominio: " +
                "${"%.1f".format(recomendacion.dominio)}%"
            )

            println(
                "   Días sin repaso: " +
                recomendacion.diasSinRepaso
            )

            println(
                "   Dificultad: " +
                recomendacion.dificultad
            )

            println(
                "   Prioridad: " +
                "${"%.1f".format(recomendacion.prioridad)}"
            )

            println("--------------------------------")
        }
    }

    private fun mostrarReportePersonal(
        estudiante: Estudiante
    ) {

        val temaIds =
            obtenerTemaIdsDelEstudiante(
                estudiante.id
            )

        val flashcardsEstudiante =
            flashcards.filter {
                it.temaId in temaIds
            }

        val quizzesEstudiante =
            quizzes.filter {
                it.temaId in temaIds
            }

        val quizIds =
            quizzesEstudiante
                .map {
                    it.id
                }
                .toSet()

        val intentosEstudiante =
            intentos.filter {
                it.quizId in quizIds
            }

        val progresosEstudiante =
            progresos.filter {
                it.temaId in temaIds
            }

        val reporte =
            reporteService.generarReporte(
                intentos = intentosEstudiante,
                flashcards =
                    flashcardsEstudiante,
                progresos =
                    progresosEstudiante
            )

        val reportePersonal =
            reporte.replace(
                "REPORTE GENERAL ESTUDIASMART",
                "REPORTE PERSONAL DE ESTUDIO"
            )

        println()
        println(reportePersonal)
    }

    private fun mostrarReporteGeneral() {

        println()

        println(
            reporteService.generarReporte(
                intentos = intentos,
                flashcards = flashcards,
                progresos = progresos
            )
        )
    }

    private fun obtenerTemaIdsDelEstudiante(
        estudianteId: Int
    ): Set<Int> {

        return materiaService
            .listarMaterias(estudianteId)
            .flatMap { materia ->

                temaService.listarTemas(
                    materia.id,
                    estudianteId
                )
            }
            .map {
                it.id
            }
            .toSet()
    }

    private fun leerDificultad(): Dificultad {

        while (true) {

            val entrada =
                readln()
                    .trim()
                    .lowercase()

            when (entrada) {

                "1",
                "dificil",
                "difícil" ->
                    return Dificultad.DIFICIL

                "2",
                "regular" ->
                    return Dificultad.REGULAR

                "3",
                "facil",
                "fácil" ->
                    return Dificultad.FACIL

                else -> {

                    println()
                    println(
                        "Opción inválida."
                    )

                    println(
                        "Ingrese 1, 2, 3 o el nombre de la dificultad."
                    )

                    print("Opción: ")
                }
            }
        }
    }

    private fun leerSiNo(): Boolean {

        while (true) {

            val respuesta =
                readln()
                    .trim()
                    .lowercase()

            when (respuesta) {

                "s",
                "si",
                "sí" ->
                    return true

                "n",
                "no" ->
                    return false

                else -> {

                    println(
                        "Respuesta inválida. Escriba s o n."
                    )

                    print(
                        "¿Respondió correctamente? (s/n): "
                    )
                }
            }
        }
    }

    private fun leerRespuestaABCD(): Char {

        while (true) {

            val respuesta =
                readln()
                    .trim()
                    .uppercase()

            if (
                respuesta.length == 1 &&
                respuesta[0] in listOf(
                    'A',
                    'B',
                    'C',
                    'D'
                )
            ) {
                return respuesta[0]
            }

            println(
                "Respuesta inválida. Ingrese A, B, C o D."
            )

            print("Respuesta: ")
        }
    }

    private fun leerEntero(): Int {

        val entrada =
            readlnOrNull()
                ?.trim()
                ?.removeSuffix(".")

        return entrada
            ?.toIntOrNull()
            ?: -1
    }

    private fun manejarError(
        error: Exception
    ) {

        println()
        println("--------------------------------")

        println(
            "Error: " +
            (error.message
                ?: "Error desconocido.")
        )

        ErrorLogger.registrarError(
            error
        )

        println(
            "El error fue registrado en logs/errors.txt"
        )

        println("--------------------------------")
    }
}