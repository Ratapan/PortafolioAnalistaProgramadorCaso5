package app.views.dashboard_views.manage_appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
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

public enum class ChooseByEnum {
    Dentista,
    Paciente
}

val appointmentStatus = listOf(
    'R' to "Reservada",
    'C' to "Cancelada",
    'T' to "Terminada",
    'A' to "Atrasada"
)

private const val AMOUNT_PER_PAGE = 30

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

                if (!patients.isEmpty()) {
                    val (selectedPatient, setSelectedPatient) = remember { mutableStateOf(0) }
                    val citas = transaction {
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
                    val (selectedInt, setSelectedInt) = remember { mutableStateOf(1) }
                    Spacer(modifier = Modifier.height(10.dp))
                    dropdownSelect("Paciente", patients, selectedPatient, setSelectedPatient, Arrangement.Start)
                    Spacer(modifier = Modifier.height(10.dp))
                    intSelector(selectedInt, setSelectedInt, "Pagina", 1, (citas.size/AMOUNT_PER_PAGE)+1, modifier = Modifier.size(200.dp, 60.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    if (!citas.isEmpty()) {
                        Row {
                            tableCell("ID", modifier = Modifier.size(30.dp, 30.dp))
                            tableCell("Value", modifier = Modifier.fillMaxWidth().height(30.dp))
                        }
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            citas.subList(0+(AMOUNT_PER_PAGE*(selectedInt-1)),
                                if (AMOUNT_PER_PAGE*selectedInt > citas.lastIndex) {
                                    citas.lastIndex+1
                                } else {
                                    AMOUNT_PER_PAGE*selectedInt+1
                                }
                            ).forEach { item ->
                                Row {
                                    tableCell("${item.first}", modifier = Modifier.size(30.dp, 30.dp))
                                    tableCell("Cita realizada el dia ${(item.second["Cita"] as Cita).fecha_solicitacion} " +
                                            "con el dentista ${((item.second["Empleado"] as kotlin.Pair<*, *>).second as User).nombre} " +
                                            "para la fecha ${(item.second["Hora"] as Hora).hora_inicio} " +
                                            "para el paciente ${patients[selectedPatient].second}", modifier = Modifier.fillMaxWidth().height(30.dp))
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

                    Text("Registrando cita del dentista ${dentist.second}", fontSize = 20.sp)

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