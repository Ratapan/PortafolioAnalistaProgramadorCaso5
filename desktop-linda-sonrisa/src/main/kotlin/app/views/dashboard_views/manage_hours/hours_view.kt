package app.views.dashboard_views.manage_hours

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.components.dropdownSelect
import app.components.tableCell
import app.components.weekPickerWithLocalDate
import app.data.Empleados
import app.data.Horas
import app.data.Tipo_Empleados
import app.data.Users
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

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
    val (selectedDentist, setSelectedDentist) = remember { mutableStateOf(0) }

    val (selectedLocalDate, setSelectedLocalDate) = remember { mutableStateOf(LocalDate.now(Clock.systemUTC())) }
    val startWeek = selectedLocalDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)
    val endWeek = selectedLocalDate.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()//.format(formatter)

    val Hours = transaction {
        addLogger(StdOutSqlLogger)
        Horas.select {
            Horas.hora_inicio.between(startWeek, endWeek) and
                (Horas.id_empleado eq dentistas[selectedDentist].first.value)
        }.map {
            it[Horas.id] to
                    (it[Horas.hora_fin] to it[Horas.estado])
        }
    }
    println(Hours.toString())
    println(Hours.firstOrNull())
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Administrador de Horas", fontSize = 20.sp)
        }
        dropdownSelect("Dentista", dentistas, selectedDentist, setSelectedDentist, Arrangement.Start)
        weekPickerWithLocalDate("Fecha", selectedLocalDate, setSelectedLocalDate)
        Row {
            LazyColumn {
                items(12) {
                    tableCell("Hi", modifier = Modifier
                        .height(40.dp)
                        .width(80.dp)
                    )
                }
            }
        }
    }


}