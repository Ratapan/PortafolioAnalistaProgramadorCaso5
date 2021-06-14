package app.views.dashboard_views.manage_appointment

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.v1.Dialog
import androidx.compose.ui.window.v1.DialogProperties
import app.components.*
import app.data.*
import app.views.dashboard_views.manage_hours.WeeklyDays
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


public enum class ChooseByEnum {
    Dentista,
    Paciente
}

public enum class OrderByEnum {
}

val appointmentStatus = listOf(
    'R' to "Reservada",
    'C' to "Cancelada",
    'T' to "Terminada",
    'A' to "Atrasada"
)

private const val AMOUNT_PER_PAGE = 30

private const val WEIGHT_ID = 1f
private const val WEIGHT_DATE = 2f
private const val WEIGHT_START_TIME = 3f
private const val WEIGHT_END_TIME = 3f
private const val WEIGHT_DENTIST = 2f
private const val WEIGHT_STATUS = 1f

@Composable
fun registerAppointment() {
    val ChooseBy = listOf(
        ChooseByEnum.Dentista to ChooseByEnum.Dentista.toString(),
        ChooseByEnum.Paciente to ChooseByEnum.Paciente.toString(),
    )

    val (selectedChooseBy, setSelectedChooseBy) = remember { mutableStateOf(0) }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Administrador de Citas", fontSize = 20.sp)
        }
        dropdownSelect("Ver citas por", ChooseBy, selectedChooseBy, setSelectedChooseBy, Arrangement.Start)
        val idRolDentista = transaction { Tipo_Empleados.select { Tipo_Empleados.nombre eq "Dentista" }.single()[Tipo_Empleados.id] }.value
        val dentistas = transaction {
            Empleados.innerJoin(Tipo_Empleados).innerJoin(Users)
                .select { Empleados.id_tipo_emp eq idRolDentista }
                .sortedBy { it[Users.nombre] }
                .map {
                    it[Empleados.id] to it[Users.nombre]
                }
        }
        val patients = transaction {
            addLogger(StdOutSqlLogger)
            Pacientes.innerJoin(Users).selectAll().map {
                it[Pacientes.id] to it[Users.nombre]
            }
        }
        when (ChooseBy[selectedChooseBy].first) {
            ChooseByEnum.Dentista -> {
                val (showRegisterAppointment, setShowRegisterAppointment) = remember { mutableStateOf(false) }
                val (showEditAppointment, setShowEditAppointment) = remember { mutableStateOf(false) }
                Spacer(modifier = Modifier.height(10.dp))
                val (selectedDentist, setSelectedDentist) = remember { mutableStateOf(0) }

                val (selectedLocalDate, setSelectedLocalDate) = remember { mutableStateOf(LocalDate.now(Clock.systemUTC())) }
                val startWeek = selectedLocalDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)
                val endWeek = selectedLocalDate.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)

                val (selectedTimeSlot, setSelectedTimeSlot) =  remember { mutableStateOf(TimeSlot()) }

                val hours = transaction {
                    addLogger(StdOutSqlLogger)
                    Horas.select {
                        Horas.hora_inicio.between(startWeek, endWeek) and
                                (Horas.id_empleado eq dentistas[selectedDentist].first.value)
                    }.map {
                        getDailySlot(
                            it[Horas.hora_inicio].minusSeconds(14400),
                            it[Horas.hora_fin].minusSeconds(14400),
                            LocalDate.ofInstant(it[Horas.hora_inicio], ZoneId.of("GMT")).atTime(9, 0).toInstant(
                                ZoneOffset.of(ZoneId.of("+0").id))
                        ) to TimeSlot(
                            it[Horas.id].value,
                            it[Horas.hora_inicio].minusSeconds(14400),
                            it[Horas.hora_fin].minusSeconds(14400),
                            it[Horas.estado])
                    }
                }

                var clickCount = 0

                registerAppointmentDialogForDentist(showRegisterAppointment, setShowRegisterAppointment, dentistas[selectedDentist], selectedTimeSlot, patients)

                editAppointmentDialogForDentist(showEditAppointment, setShowEditAppointment, dentistas[selectedDentist], selectedTimeSlot)

                dropdownSelect("Dentista", dentistas, selectedDentist, setSelectedDentist, Arrangement.Start)

                Spacer(modifier = Modifier.height(10.dp))

                weekPickerWithLocalDate("Fecha", selectedLocalDate, setSelectedLocalDate)

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    val startTime = LocalTime.of(9, 0)
                    val endTime = LocalTime.of(9, 30)
                    val modifierTableCell = Modifier
                        .height(40.dp)
                        .width(100.dp)
                    Column {
                        tableCell(
                            "Time \\ Day",modifier = Modifier
                                .height(50.dp)
                                .width(100.dp)
                        )
                        for (i in 0..17) {
                            tableCell(
                                "${startTime.plusMinutes(30*i.toLong())} - ${endTime.plusMinutes(30*i.toLong())}",
                                modifierTableCell
                            )
                        }
                    }
                    WeeklyDays.forEach { weekDay ->
                        val timeSlots = MutableList(18) {
                            TimeSlot()
                        }
                        hours.filter { LocalDate.ofInstant(it.second.startTime, ZoneId.systemDefault()) == selectedLocalDate.with(
                            weekDay
                        ) }.forEach {
                            println(it.first)
                            timeSlots[it.first] = it.second
                        }
                        Column {
                            tableCell(
                                "$weekDay\n${selectedLocalDate.with(weekDay).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}", modifier = Modifier
                                    .height(50.dp)
                                    .width(100.dp)
                            )
                            timeSlots.forEach {
                                tableCell(
                                    when (it.status.toString()) {
                                        "F" -> " "
                                        else -> it.status.toString()
                                    },
                                    when (it.status.toString()) {
                                        "F" -> modifierTableCell
                                        "D" -> modifierTableCell
                                            .background(Color.Green)
                                            .clickable {
                                                clickCount++
                                                when (clickCount) {
                                                    1 -> {
                                                        GlobalScope.launch {
                                                            delay(500)
                                                            if(clickCount == 1){
                                                                clickCount = 0
                                                                println("Counter restarted")
                                                            }
                                                        }
                                                    }
                                                    2 -> {
                                                        setSelectedTimeSlot(it)
                                                        setShowRegisterAppointment(true)
                                                        clickCount = 0
                                                    }
                                                }
                                            }
                                        "T" -> modifierTableCell
                                            .background(Color.Red)
                                            .clickable {
                                                clickCount++
                                                when (clickCount) {
                                                    1 -> {
                                                        GlobalScope.launch {
                                                            delay(500)
                                                            if(clickCount == 1){
                                                                clickCount = 0
                                                                println("Counter restarted")
                                                            }
                                                        }
                                                    }
                                                    2 -> {
                                                        setSelectedTimeSlot(it)
                                                        setShowEditAppointment(true)
                                                        clickCount = 0
                                                    }
                                                }
                                            }
                                        else -> modifierTableCell
                                    }
                                )
                            }
                        }
                    }
                }
            }
            ChooseByEnum.Paciente -> {
                val (showRegisterAppointment, setShowRegisterAppointment) = remember { mutableStateOf(false) }
                val noon = Instant.ofEpochSecond(
                    LocalDateTime.of(
                        LocalDate.now(), LocalTime.NOON
                    ).toEpochSecond(
                        ZoneOffset.ofHours(0)
                    ))
                val (selectedTimeSlot, setSelectedTimeSlot) = remember { mutableStateOf( TimeSlot(startTime = noon, endTime = noon) ) }

                if (!patients.isEmpty()) {
                    val (selectedPatient, setSelectedPatient) = remember { mutableStateOf(0) }
                    val citas = remember { mutableStateOf( transaction {
                        addLogger(StdOutSqlLogger)
                        (Citas innerJoin Pacientes innerJoin Users).select { Citas.id_paciente eq patients[selectedPatient].first.value }.map {
                            it[Citas.id] to mapOf(
                                "Cita" to Cita.findById(it[Citas.id]),
                                "Hora" to Hora.findById(it[Citas.id_hora]),
                                "Paciente" to (Paciente.findById(it[Citas.id_paciente]) to User.findById(Paciente.findById(it[Citas.id_paciente])!!.idUser)),
                                "Empleado" to (Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado) to User.findById(Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado)!!.id_user))
                            )
                        }
                    }
                    )}
                    val (selectedInt, setSelectedInt) = remember { mutableStateOf(1) }
                    registerAppointmentDialogForPatients(showRegisterAppointment, setShowRegisterAppointment, patients[selectedPatient].first, patients[selectedPatient].second, dentistas, selectedTimeSlot, setSelectedTimeSlot)
                    Spacer(modifier = Modifier.height(10.dp))
                    dropdownSelect(
                        "Paciente",
                        patients,
                        selectedPatient,
                        setSelectedPatient,
                        Arrangement.Start,
                        reloadButton = {
                            OutlinedButton(
                                onClick = {
                                    citas.value = transaction {
                                        addLogger(StdOutSqlLogger)
                                        (Citas innerJoin Pacientes innerJoin Users).select { Citas.id_paciente eq patients[selectedPatient].first.value }
                                            .map {
                                                it[Citas.id] to mapOf(
                                                    "Cita" to Cita.findById(it[Citas.id]),
                                                    "Hora" to Hora.findById(it[Citas.id_hora]),
                                                    "Paciente" to (Paciente.findById(it[Citas.id_paciente]) to User.findById(
                                                        Paciente.findById(it[Citas.id_paciente])!!.idUser
                                                    )),
                                                    "Empleado" to (Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado) to User.findById(
                                                        Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado)!!.id_user
                                                    ))
                                                )
                                            }
                                    }
                                },
                                modifier = Modifier
                                    .height(IntrinsicSize.Min)
                            ) {
                                Text("Buscar", color = Color.Black)
                            }
                        }
                        ) {
                        citas.value = transaction {
                            addLogger(StdOutSqlLogger)
                            (Citas innerJoin Pacientes innerJoin Users).select { Citas.id_paciente eq patients[selectedPatient].first.value }
                                .map {
                                    it[Citas.id] to mapOf(
                                        "Cita" to Cita.findById(it[Citas.id]),
                                        "Hora" to Hora.findById(it[Citas.id_hora]),
                                        "Paciente" to (Paciente.findById(it[Citas.id_paciente]) to User.findById(
                                            Paciente.findById(it[Citas.id_paciente])!!.idUser
                                        )),
                                        "Empleado" to (Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado) to User.findById(
                                            Empleado.findById(Hora.findById(it[Citas.id_hora])!!.id_empleado)!!.id_user
                                        ))
                                    )
                                }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    intSelector(selectedInt, setSelectedInt, "Pagina", 1, (citas.value.size/AMOUNT_PER_PAGE)+1, modifier = Modifier.size(200.dp, 60.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    if (!citas.value.isEmpty()) {
                        Row (modifier = Modifier.fillMaxWidth()){
                            tableCell("ID", modifier = Modifier.weight(WEIGHT_ID)
                                .clickable {
                                    citas.value = citas.value.sortedBy { (it.second["Cita"] as Cita).id.value }
                                })
                            tableCell("Dentista", modifier = Modifier.weight(WEIGHT_DENTIST)
                                .clickable {
                                    citas.value = citas.value.sortedBy { ((it.second["Empleado"] as Pair<*, *>).second as User).nombre }
                                })
                            tableCell("Fecha de Solicitud", modifier = Modifier.weight(WEIGHT_DATE)
                                .clickable {
                                    citas.value = citas.value.sortedBy { (it.second["Cita"] as Cita).fecha_solicitacion }
                                })
                            tableCell("Hora de Inicio", modifier = Modifier.weight(WEIGHT_START_TIME)
                                .clickable {
                                    citas.value = citas.value.sortedBy { (it.second["Hora"] as Hora).hora_inicio }
                                })
                            tableCell("Hora de Fin", modifier = Modifier.weight(WEIGHT_END_TIME)
                                .clickable {
                                    citas.value = citas.value.sortedBy { (it.second["Hora"] as Hora).hora_fin }
                                })
                            tableCell("Estado", modifier = Modifier.weight(WEIGHT_STATUS)
                                .clickable {
                                    citas.value = citas.value.sortedBy { (it.second["Cita"] as Cita).estado }
                                })
                        }
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            val timerFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                                .withLocale(Locale.US)
                                .withZone(ZoneId.of("-4"))
                            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                .withLocale(Locale.US)
                                .withZone(ZoneId.of("-4"))
                            citas.value.subList(0+(AMOUNT_PER_PAGE*(selectedInt-1)),
                                if (AMOUNT_PER_PAGE*selectedInt > citas.value.lastIndex) {
                                    citas.value.lastIndex+1
                                } else {
                                    AMOUNT_PER_PAGE*selectedInt+1
                                }
                            ).forEach { item ->
                                Row (modifier = Modifier.fillMaxWidth()
                                ){
                                    tableCell("${item.first}", modifier = Modifier.weight(WEIGHT_ID))
                                    tableCell(((item.second["Empleado"] as kotlin.Pair<*, *>).second as User).nombre, modifier = Modifier.weight(WEIGHT_DENTIST))
                                    tableCell((item.second["Cita"] as Cita).fecha_solicitacion.format(dateFormatter), modifier = Modifier.weight(WEIGHT_DATE))
                                    tableCell(timerFormatter.format((item.second["Hora"] as Hora).hora_inicio), modifier = Modifier.weight(WEIGHT_START_TIME))
                                    tableCell(timerFormatter.format((item.second["Hora"] as Hora).hora_fin), modifier = Modifier.weight(WEIGHT_END_TIME))
                                    tableCell(appointmentStatus[appointmentStatus.indexOfFirst {
                                        it.first == (item.second["Cita"] as Cita).estado
                                    }].second, modifier = Modifier.weight(WEIGHT_STATUS))
                                }
                            }
                        }
                    } else {
                        Text("No hay citas registradas para el paciente seleccionado", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(onClick = {
                        setShowRegisterAppointment(true)
                    }) {
                        Text("Registrar Cita")
                    }


                } else {
                    Text("No hay pacientes registrados en el sistema", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun registerAppointmentDialogForDentist(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    dentist: Pair<*, String> = 1 to " ",
    timeSlot: TimeSlot,
    patients: List<Pair<EntityID<Int>, String>>
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = {
            },
            confirmButton = {
            },
            dismissButton = {
            },
            text = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround

                ) {
                    val (selectedPatient, setSelectedPatient) =  remember { mutableStateOf(0) }
                    val (selectedStatus, setSelectedStatus) =  remember { mutableStateOf(0) }

                    Text("Registrando cita del dentista ${dentist.second}", fontSize = 24.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    DisableSelection {
                        datePickerWithLocalDate(title = "Hora de Inicio", LocalDate.ofInstant(timeSlot.startTime, ZoneId.systemDefault()), { }, disabled = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        timePicker(title = "Hora de Inicio", timeSlot.startTime, { }, minHour = 9, maxHour = 17, disabled = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        timePicker(title = "Hora de Fin", timeSlot.endTime, {  }, minHour = 9, maxHour = 18, disabled = true)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Estado", appointmentStatus, selectedStatus, setSelectedStatus)

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Paciente", patients, selectedPatient, setSelectedPatient)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            setShowDialog(false)
                            appointmentInsert(estadoCita = appointmentStatus[selectedStatus].first, idPaciente = patients[selectedPatient].first.value, idHora = timeSlot.id)
                        }) {
                            Text(text = "Confirmar")
                        }
                        Button(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Cancelar")
                        }
                    }
                }
            },
            properties = DialogProperties(
                title = "Registrar Cita para el dentista ${dentist.second}",
                size = IntSize(500, 700)
            )
        )
    }
}



@Composable
fun editAppointmentDialogForDentist(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    dentist: Pair<*, String> = 1 to " ",
    timeSlot: TimeSlot
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = {
            },
            confirmButton = {
            },
            dismissButton = {
            },
            text = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround

                ) {
                    val cita = transaction {
                        Cita.find { Citas.id_hora eq timeSlot.id and (Citas.estado inList listOf('R', 'T', 'A')) }.toList()
                    }
                    val (selectedStatus, setSelectedStatus) =  remember { mutableStateOf(
                        appointmentStatus.indexOfFirst {
                            it.first == cita.first().estado
                        }) }
                    println(cita.first().estado)
                    val paciente = transaction {
                        User.findById(
                            Paciente.findById(
                                cita.first().id_paciente
                            )!!.idUser
                        )!!.nombre
                    }


                    Text("Editando cita del dentista ${dentist.second}", fontSize = 24.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    DisableSelection {
                        datePickerWithLocalDate(title = "Hora de Inicio", LocalDate.ofInstant(timeSlot.startTime, ZoneId.systemDefault()), { }, disabled = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        timePicker(title = "Hora de Inicio", timeSlot.startTime, { }, minHour = 9, maxHour = 17, disabled = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        timePicker(title = "Hora de Fin", timeSlot.endTime, {  }, minHour = 9, maxHour = 18, disabled = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Paciente: $paciente", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Estado", appointmentStatus, selectedStatus, setSelectedStatus)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            setShowDialog(false)
                            appointmentEdit(estadoCita = appointmentStatus[selectedStatus].first, idCita = cita.first().id.value, idHora = timeSlot.id)
                        }) {
                            Text(text = "Confirmar")
                        }
                        Button(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Cancelar")
                        }
                    }
                }
            },
            properties = DialogProperties(
                title = "Editando Cita para el dentista ${dentist.second}",
                size = IntSize(500, 700)
            )
        )
    }
}

@Composable
fun registerAppointmentDialogForPatients(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    idPatient: EntityID<Int>,
    namePatient: String,
    dentists: List<Pair<EntityID<Int>, String>>,
    selectedTimeSlot: TimeSlot,
    setSelectedTimeSlot: (TimeSlot) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            content = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(5.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    LocalAppWindow.current.apply {
                        events.onOpen = { maximize() }
                    }
                    val showPopUpCalendar = remember { mutableStateOf(false) }

                    val (selectedDentist, setSelectedDentist) = remember { mutableStateOf(0) }
                    val (selectedStatus, setSelectedStatus) = remember { mutableStateOf(0) }


                    val (fechaSolicitud, setFechaSolicitud) = remember { mutableStateOf(LocalDate.ofInstant(selectedTimeSlot.startTime, ZoneId.systemDefault())) }

                    val (selectedLocalDate, setSelectedLocalDate) = remember { mutableStateOf(LocalDate.now(Clock.systemUTC())) }
                    val startWeek = selectedLocalDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault())
                        .toInstant()//.format(formatter)
                    val endWeek =
                        selectedLocalDate.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(ZoneId.systemDefault())
                            .toInstant()//.format(formatter)

                    val noon = Instant.ofEpochSecond(
                        LocalDateTime.of(
                            LocalDate.now(), LocalTime.NOON
                        ).toEpochSecond(
                            ZoneOffset.ofHours(0)
                        ))
                    val baseTimeSlot = TimeSlot(startTime = noon, endTime = noon)

                    val timerFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                        .withLocale(Locale.US)
                        .withZone(ZoneId.of("+0"))

//                    selectTimeSlot(setSelectedTimeSlot, showPopUpCalendar, selectedDentist, dentists)

                    Text("Registrando cita del paciente $namePatient", fontSize = 24.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Dentista", dentists, selectedDentist, setSelectedDentist, Arrangement.Start)

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = {
                        showPopUpCalendar.value = !showPopUpCalendar.value
                    }) {
                        Text(text = "Mostrar Horarios Disponibles")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Estado", appointmentStatus, selectedStatus, setSelectedStatus, Arrangement.Start)

                    Spacer(modifier = Modifier.height(10.dp))

                    datePickerWithLocalDate(title = "Fecha de Solicitud", fechaSolicitud, setFechaSolicitud)

                    Spacer(modifier = Modifier.height(10.dp))


                    Text( "Hora de Inicio: ${timerFormatter.format(selectedTimeSlot.startTime)}", fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text( "Hora de Fin: ${timerFormatter.format(selectedTimeSlot.endTime)}", fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    println(!selectedTimeSlot.equals(baseTimeSlot))

                    formSpacer(modifier = Modifier.height(10.dp), selectedTimeSlot.equals(baseTimeSlot), "Porfavor seleccionar una hora valida, ")

                    if (showPopUpCalendar.value) {

                        val hours = transaction {
                            addLogger(StdOutSqlLogger)
                            Horas.select {
                                Horas.hora_inicio.between(startWeek, endWeek) and
                                        (Horas.id_empleado eq dentists[selectedDentist].first.value)
                            }.map {
                                getDailySlot(
                                    it[Horas.hora_inicio].minusSeconds(14400),
                                    it[Horas.hora_fin].minusSeconds(14400),
                                    LocalDate.ofInstant(it[Horas.hora_inicio], ZoneId.of("GMT")).atTime(9, 0).toInstant(
                                        ZoneOffset.of(ZoneId.of("+0").id)
                                    )
                                ) to TimeSlot(
                                    it[Horas.id].value,
                                    it[Horas.hora_inicio].minusSeconds(14400),
                                    it[Horas.hora_fin].minusSeconds(14400),
                                    it[Horas.estado]
                                )
                            }
                        }

                        Column (modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally)
                        ) {

                            Spacer(modifier = Modifier.height(10.dp))

                            weekPickerWithLocalDate("Fecha", selectedLocalDate, setSelectedLocalDate)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row {
                                val startingTime = LocalTime.of(9, 0)
                                val endingTime = LocalTime.of(9, 30)
                                val modifierTableCell = Modifier
                                    .height(40.dp)
                                    .width(100.dp)
                                Column {
                                    tableCell(
                                        "Time \\ Day", modifier = Modifier
                                            .height(50.dp)
                                            .width(100.dp)
                                    )
                                    for (i in 0..17) {
                                        tableCell(
                                            "${startingTime.plusMinutes(30 * i.toLong())} - ${endingTime.plusMinutes(30 * i.toLong())}",
                                            modifierTableCell
                                        )
                                    }
                                }
                                WeeklyDays.forEach { weekDay ->
                                    val timeSlots = MutableList(18) {
                                        TimeSlot()
                                    }
                                    hours.filter {
                                        LocalDate.ofInstant(
                                            it.second.startTime,
                                            ZoneId.systemDefault()
                                        ) == selectedLocalDate.with(
                                            weekDay
                                        )
                                    }.forEach {
                                        println(it.first)
                                        timeSlots[it.first] = it.second
                                    }
                                    Column {
                                        tableCell(
                                            "$weekDay\n${
                                                selectedLocalDate.with(weekDay)
                                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            }", modifier = Modifier
                                                .height(50.dp)
                                                .width(100.dp)
                                        )
                                        timeSlots.forEach {
                                            tableCell(
                                                when (it.status.toString()) {
                                                    "F" -> " "
                                                    else -> it.status.toString()
                                                },
                                                when (it.status.toString()) {
                                                    "F" -> modifierTableCell
                                                    "T" -> modifierTableCell
                                                        .background(Color.Red)
                                                    "D" -> modifierTableCell
                                                        .background(Color.Green)
                                                        .clickable {
                                                            setSelectedTimeSlot(it)
                                                            showPopUpCalendar.value = false
                                                        }
                                                    else -> modifierTableCell
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(onClick = {
                                showPopUpCalendar.value = false
                            }) {
                                Text(text = "Cancel")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            if (selectedTimeSlot.equals(baseTimeSlot)) {
                                setShowDialog(false)
                            } else {
                                appointmentInsert(estadoCita = appointmentStatus[selectedStatus].first, idPaciente = idPatient.value, idHora = selectedTimeSlot.id)
                                setShowDialog(false)
                            }

                        /**
                         *
                         * Todo
                         * Add appointmentInsert but working with patients
                         * Edit appointments from the list of appointments w/ clickable
                         *
                        **/
                        },
                            modifier = when (selectedTimeSlot.equals(baseTimeSlot)) {
                                true -> Modifier
                                    .background(Color.Gray)
                                else -> Modifier.background(Color.White)
                            }
                        ) {
                            Text(text = "Confirmar")
                        }
                        Button(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(text = "Cancelar")
                        }
                    }
                }
            },
            properties = DialogProperties(
                title = "Registrar Cita para el paciente ${namePatient}",
                size = IntSize(500, 700)
            )
        )
    }
}



@Composable
fun selectTimeSlot(
    setSelectedTimeSlot: (TimeSlot) -> Unit,
    showPopUpCalendar: MutableState<Boolean>,
    selectedDentist: Int,
    dentists: List<Pair<EntityID<Int>, String>>
) {
    val (selectedLocalDate, setSelectedLocalDate) = remember { mutableStateOf(LocalDate.now(Clock.systemUTC())) }
    val startWeek = selectedLocalDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault())
        .toInstant()//.format(formatter)
    val endWeek =
        selectedLocalDate.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(ZoneId.systemDefault())
            .toInstant()//.format(formatter)


    if (showPopUpCalendar.value) {
        Dialog(
            onDismissRequest = {
                showPopUpCalendar.value = false
            },
            content = {

                val hours = transaction {
                    addLogger(StdOutSqlLogger)
                    Horas.select {
                        Horas.hora_inicio.between(startWeek, endWeek) and
                                (Horas.id_empleado eq dentists[selectedDentist].first.value)
                    }.map {
                        getDailySlot(
                            it[Horas.hora_inicio].minusSeconds(14400),
                            it[Horas.hora_fin].minusSeconds(14400),
                            LocalDate.ofInstant(it[Horas.hora_inicio], ZoneId.of("GMT")).atTime(9, 0).toInstant(
                                ZoneOffset.of(ZoneId.of("+0").id)
                            )
                        ) to TimeSlot(
                            it[Horas.id].value,
                            it[Horas.hora_inicio].minusSeconds(14400),
                            it[Horas.hora_fin].minusSeconds(14400),
                            it[Horas.estado]
                        )
                    }
                }

                Column {

                    Spacer(modifier = Modifier.height(10.dp))

                    weekPickerWithLocalDate("Fecha", selectedLocalDate, setSelectedLocalDate)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        val startingTime = LocalTime.of(9, 0)
                        val endingTime = LocalTime.of(9, 30)
                        val modifierTableCell = Modifier
                            .height(40.dp)
                            .width(100.dp)
                        Column {
                            tableCell(
                                "Time \\ Day", modifier = Modifier
                                    .height(50.dp)
                                    .width(100.dp)
                            )
                            for (i in 0..17) {
                                tableCell(
                                    "${startingTime.plusMinutes(30 * i.toLong())} - ${endingTime.plusMinutes(30 * i.toLong())}",
                                    modifierTableCell
                                )
                            }
                        }
                        WeeklyDays.forEach { weekDay ->
                            val timeSlots = MutableList(18) {
                                TimeSlot()
                            }
                            hours.filter {
                                LocalDate.ofInstant(
                                    it.second.startTime,
                                    ZoneId.systemDefault()
                                ) == selectedLocalDate.with(
                                    weekDay
                                )
                            }.forEach {
                                println(it.first)
                                timeSlots[it.first] = it.second
                            }
                            Column {
                                tableCell(
                                    "$weekDay\n${
                                        selectedLocalDate.with(weekDay)
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    }", modifier = Modifier
                                        .height(50.dp)
                                        .width(100.dp)
                                )
                                timeSlots.forEach {
                                    tableCell(
                                        when (it.status.toString()) {
                                            "F" -> " "
                                            else -> it.status.toString()
                                        },
                                        when (it.status.toString()) {
                                            "F" -> modifierTableCell
                                            "T" -> modifierTableCell
                                                .background(Color.Red)
                                            "D" -> modifierTableCell
                                                .background(Color.Green)
                                                .clickable {
                                                    setSelectedTimeSlot(it)
                                                    showPopUpCalendar.value = false
                                                }
                                            else -> modifierTableCell
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {
                        showPopUpCalendar.value = false
                    }) {
                        Text(text = "Cancel")
                    }

                }
            },
            properties = DialogProperties(
                title = "Edit user",
                size = IntSize(900, 1200)
            ),
        )
    }
}