package app.views.dashboard_views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.components.CitasPorSemana
import app.components.lineChart
import app.libs.closestNumber
import app.libs.maxPercentageOf
import org.jetbrains.skija.*


@Composable
fun StockStatusCard() {
    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Stock Status")
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .size(126.dp)
                    .padding(4.dp)
                    .border(BorderStroke(1.dp, color = Color.Gray))
                ){
                    Text("Image Placeholder")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Good", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Text("0/x productos con stock critico", textAlign = TextAlign.Center)
            }
        }
    }
}


@Composable
fun ReservationStatusCard() {
    var data = listOf(
        CitasPorSemana("1/1", 8),
        CitasPorSemana("8/1", 13),
        CitasPorSemana("15/1", 64),
        CitasPorSemana("22/1", 15),
    )
    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Reservation Status")
                Spacer(modifier = Modifier.height(10.dp))
                lineChart(data)
            }
        }
    }
}

@Composable
fun RegisteredPatientsCard() {
    Card(elevation = 4.dp) {
        Box(modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(5.dp)
        ){
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Amount of Patients")
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .size(126.dp)
                    .padding(4.dp)
                    .border(BorderStroke(1.dp, color = Color.Gray))
                ){
                    Text("Image Placeholder")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("10 Total Patients", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Text("X new Patients in the last week", textAlign = TextAlign.Center)
            }
        }
    }
}