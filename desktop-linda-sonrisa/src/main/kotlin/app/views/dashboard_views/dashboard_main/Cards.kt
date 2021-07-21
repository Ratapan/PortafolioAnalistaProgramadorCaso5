package app.views.dashboard_views.dashboard_main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.components.CitasPorSemana
import app.components.lineChart
import app.data.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val stockBoxImage = {}.javaClass.classLoader.getResource("images/box-614x460.png")!!
private val dataFileForAmountOfPatients = "data.txt"
private val blankProfilePicture = {}.javaClass.classLoader.getResource("images/blank-profile-picture.png")!!

@Composable
fun showStockStatusCard() {
    val productCountFromDatabase = transaction {
        Productos.selectAll().count()
    }
    val productCountUnderCriticalFromDatabase = transaction {
        Productos.select {
            Productos.stock lessEq Productos.stock_critico
        }.count()
    }
    val stockVsCrit = productCountUnderCriticalFromDatabase.toDouble()/productCountFromDatabase.toDouble()
    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Estado de los productos")
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .size(126.dp)
                    .padding(4.dp)
                ){
                    val imagen = byteArrayToBitMap(File(stockBoxImage.toURI()).readBytes())
                    val green = Color(34,139,34)
                    val yellow = Color(218,165,32)
                    val red = Color(139,0,0)
                    Image(imagen, "Image", modifier = Modifier.size(126.dp, 126.dp), colorFilter =
                        ColorFilter.tint(
                            if (stockVsCrit <= 0) green
                            else if (stockVsCrit > 0 && stockVsCrit < 1) yellow
                            else (red)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    if (stockVsCrit <= 0) "Bien"
                    else if (stockVsCrit > 0 && stockVsCrit < 1) "Se necesitan productos"
                    else ("No hay nada! D:"), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                val correct = productCountUnderCriticalFromDatabase
                val questionNum = productCountFromDatabase
                val percent = correct * 100.0f / questionNum
                Text("$percent% de los productos con stock critico", textAlign = TextAlign.Center)
            }
        }
    }
}


@Composable
fun reservationStatusCard() {
    val todaysDate = LocalDate.now()


    val data = mutableListOf<CitasPorSemana>()

    for (i in 0..3) {
        val dateOfWeek = todaysDate.plusDays(7L*i)
        val dateOfStartWeek = dateOfWeek.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val dateOfEndWeek = dateOfWeek.with(DayOfWeek.SUNDAY).atStartOfDay(ZoneId.systemDefault()).toInstant()
        data.add(CitasPorSemana(
            dateOfWeek.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("dd/MM")),
            transaction {
                Citas.innerJoin(Horas).select {
                    Horas.hora_inicio.between(dateOfStartWeek, dateOfEndWeek)
                }.count().toInt()
            }
        ))
    }

    val appointmentsThisMonth = data.sumOf { it.cantidad }

    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column () {
                Text("Estado de las citas.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                lineChart(data)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Hay un total de $appointmentsThisMonth citas este mes.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun registeredPatientsCard() {
    val patientsCountFromDatabase = transaction {
        Pacientes.selectAll().count()
    }
    var amountOfNewPatients = 0L
    val oldAmountOfPatients = File(dataFileForAmountOfPatients).takeIf {
        it.isFile
    }?.readText()?.split(" ")?.firstOrNull()?.filter { it.isDigit() }?.toLongOrNull()
    if (oldAmountOfPatients != null) {
        amountOfNewPatients = patientsCountFromDatabase-oldAmountOfPatients
    }
    println("pacientes $patientsCountFromDatabase")
    println("old $oldAmountOfPatients")
    println("amount $amountOfNewPatients")
    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Estado de los pacientes")
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .size(126.dp)
                    .padding(4.dp)
                ){
                    val imagen = byteArrayToBitMap(File(blankProfilePicture.toURI()).readBytes())
                    Image(imagen, "Image", modifier = Modifier.size(126.dp, 126.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    if (patientsCountFromDatabase >= 2) "$patientsCountFromDatabase pacientes totales."
                    else if (patientsCountFromDatabase == 1L) "Hay $patientsCountFromDatabase paciente."
                    else ("No hay pacientes.")
                    , textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Text("$amountOfNewPatients nuevos pacientes desde la ultima vez.", textAlign = TextAlign.Center)
            }
        }
    }
}