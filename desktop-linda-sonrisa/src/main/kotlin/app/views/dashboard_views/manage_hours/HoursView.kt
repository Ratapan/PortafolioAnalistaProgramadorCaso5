package app.views.dashboard_views.manage_hours

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.v1.DialogProperties
import app.components.*
import app.data.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.*

public val WeeklyDays = DayOfWeek.values()


public val estadoHora = listOf(
    "Disponible",
    "Tomada"
)

@Composable
fun hoursView() {
    val idRolDentista = transaction { Tipo_Empleados.select { Tipo_Empleados.nombre eq "Dentista" }.single()[Tipo_Empleados.id] }.value
    val dentistas = transaction {
        Empleados.innerJoin(Tipo_Empleados).innerJoin(Users)
            .select { Empleados.id_tipo_emp eq idRolDentista }
            .sortedBy { it[Users.nombre] }
            .map {
                it[Empleados.id] to it[Users.nombre]
            }
    }
    val (selectedTimeSlot, setSelectedTimeSlot) = remember { mutableStateOf(TimeSlot(startTime = Instant.now(), endTime = Instant.now()))}
    val (showEditTimeSlot, setShowEditTimeSlot) =  remember { mutableStateOf(false) }
    val (showRegisterTimeSlot, setShowRegisterTimeSlot) =  remember { mutableStateOf(false) }
    val (selectedDentist, setSelectedDentist) = remember { mutableStateOf(0) }

    var clickCount = 0

    val (selectedLocalDate, setSelectedLocalDate) = remember { mutableStateOf(LocalDate.now(Clock.systemUTC())) }
    val startWeek = selectedLocalDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)
    val endWeek = selectedLocalDate.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)

    val hours = transaction {
        addLogger(StdOutSqlLogger)
        Horas.select {
            Horas.hora_inicio.between(startWeek, endWeek) and
                (Horas.id_empleado eq dentistas[selectedDentist].first.value)
        }.map {
            getDailySlot(
                it[Horas.hora_inicio].minusSeconds(14400),
                it[Horas.hora_fin].minusSeconds(14400),
                LocalDate.ofInstant(it[Horas.hora_inicio], ZoneId.of("GMT")).atTime(9, 0).toInstant(ZoneOffset.of(ZoneId.of("+0").id))
            ) to TimeSlot(
                it[Horas.id].value,
                it[Horas.hora_inicio].minusSeconds(14400),
                it[Horas.hora_fin].minusSeconds(14400),
                it[Horas.estado])
        }
    }
    println(hours.toString())
    println(hours.firstOrNull()?.second?.startTime?.atZone(ZoneId.of("GMT")))


    editHourDialog(showEditTimeSlot, setShowEditTimeSlot, selectedTimeSlot, dentistas[selectedDentist].second)
    registerHourDialog(showRegisterTimeSlot, setShowRegisterTimeSlot, selectedTimeSlot, dentistas, selectedDentist)

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Administrador de Horas", fontSize = 20.sp)
        }
        dropdownSelect("Dentista", dentistas, selectedDentist, setSelectedDentist, Arrangement.Start)
        weekPickerWithLocalDate("Fecha", selectedLocalDate, setSelectedLocalDate)
        Row {
            val startTime = LocalTime.of(9, 0)
            val endTime = LocalTime.of(9, 30)
            val modifierTableCell = Modifier
                .height(40.dp)
                .width(100.dp)
            Column {
                tableCell(
                    "Time \\ Day",
                    modifierTableCell
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
                        weekDay.toString(), modifier = modifierTableCell
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
                                                setShowEditTimeSlot(true)
                                                clickCount = 0
                                            }
                                        }
                                    }
                                "T" -> modifierTableCell
                                    .background(Color.Red)
                                else -> modifierTableCell
                            }
                        )
                    }
                }
            }
        }
        OutlinedButton(onClick = {
            setShowRegisterTimeSlot(true)
        }) {
            Text("Registrar Hora")
        }
    }
}


@Composable
fun editHourDialog(active: Boolean, setShowDialog: (Boolean) -> Unit, timeSlot: TimeSlot, dentistName: String = "") {

    if (active) {

        val id by remember { mutableStateOf(timeSlot.id) }
        val (startTime, setStartTime) = remember { mutableStateOf(timeSlot.startTime) }
        val (endTime, setEndTime) = remember { mutableStateOf(timeSlot.endTime) }
        var status by remember { mutableStateOf(timeSlot.status) }

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
                    Text("Editando Hora del dentista $dentistName")

                    Spacer(modifier = Modifier.height(10.dp))

                    timePicker(title = "Hora de Inicio", startTime, setStartTime, minHour = 9, maxHour = 17)

                    Spacer(modifier = Modifier.height(10.dp))

                    timePicker(title = "Hora de Fin", endTime, setEndTime, minHour = 9, maxHour = 18)

                    Spacer(modifier = Modifier.height(10.dp))

                    Box {
                        var expanded by remember { mutableStateOf(false) }
                        OutlinedButton(
                            onClick = {
                                expanded = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val statusValue = when (status) {
                                'D' -> "Disponible"
                                'T' -> "Tomada"
                                else -> " "
                            }
                            Text("Estado: $statusValue")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth().background(
                                Color.Gray
                            )
                        ) {
                            estadoHora.forEachIndexed { index, s ->
                                DropdownMenuItem(onClick = {
                                    println(index)
                                    status = when (index) {
                                        0 -> 'D'
                                        1 -> 'T'
                                        else -> 'F'
                                    }
                                    expanded = false
                                }) {
                                    Text(text = s)
                                }
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
                            setShowDialog(false)
                            hourEdit(id, startTime, endTime, status)
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
                title = "Editar Hora",
                size = IntSize(400, 700)
            )
        )
    }
}


@Composable
fun registerHourDialog(
    active: Boolean,
    setShowDialog: (Boolean) -> Unit,
    timeSlot: TimeSlot = TimeSlot(startTime = Instant.now(), endTime = Instant.now()),
    dentistas: List<Pair<EntityID<Int>, String>>,
    selectedDentistValue: Int,
) {

    if (active) {

        val id by remember { mutableStateOf(timeSlot.id) }
        val (date, setDate) = remember { mutableStateOf(LocalDate.ofInstant(timeSlot.startTime, ZoneId.of("+0"))) }
        val (startTime, setStartTime) = remember { mutableStateOf(timeSlot.startTime) }
        val (endTime, setEndTime) = remember { mutableStateOf(timeSlot.endTime) }
        val (selectedDentist, setSelectedDentist) = remember { mutableStateOf(selectedDentistValue) }
        var status by remember { mutableStateOf(timeSlot.status) }

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
                    Text("Registrando una hora")

                    Spacer(modifier = Modifier.height(10.dp))

                    dropdownSelect("Dentista", dentistas, selectedDentist, setSelectedDentist, Arrangement.Start)

                    Spacer(modifier = Modifier.height(10.dp))

                    datePickerWithLocalDate(title = "Hora de Inicio", date, setDate)

                    Spacer(modifier = Modifier.height(10.dp))

                    timePicker(title = "Hora de Inicio", startTime, setStartTime, minHour = 9, maxHour = 17)

                    Spacer(modifier = Modifier.height(10.dp))

                    timePicker(title = "Hora de Fin", endTime, setEndTime, minHour = 9, maxHour = 18)

                    Spacer(modifier = Modifier.height(10.dp))

                    Box {
                        var expanded by remember { mutableStateOf(false) }
                        OutlinedButton(
                            onClick = {
                                expanded = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val statusValue = when (status) {
                                'D' -> "Disponible"
                                'T' -> "Tomada"
                                else -> "Por definir"
                            }
                            Text("Estado: $statusValue")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth().background(
                                Color.Gray
                            )
                        ) {
                            estadoHora.forEachIndexed { index, s ->
                                DropdownMenuItem(onClick = {
                                    println(index)
                                    status = when (index) {
                                        0 -> 'D'
                                        1 -> 'T'
                                        else -> 'F'
                                    }
                                    expanded = false
                                }) {
                                    Text(text = s)
                                }
                            }
                        }
                    }

                    formSpacer(modifier = Modifier, !listOf('D', 'T').contains(status), "Porfavor seleccione el estado de la Hora.")

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            if (listOf('D', 'T').contains(status)) {
                                setShowDialog(false)
                                LocalDateTime.of(date, LocalTime.ofInstant(startTime, ZoneId.of("+0"))).toInstant(
                                    ZoneOffset.ofHours(+0))
                                hourInsert(
                                    LocalDateTime.of(date, LocalTime.ofInstant(startTime, ZoneId.of("+0"))).toInstant(
                                        ZoneOffset.ofHours(+0)),
                                    LocalDateTime.of(date, LocalTime.ofInstant(endTime, ZoneId.of("+0"))).toInstant(
                                        ZoneOffset.ofHours(+0)), status, dentistas[selectedDentist].first.value)
                            }
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
                title = "Registrar Hora",
                size = IntSize(550, 700)
            )
        )
    }
}