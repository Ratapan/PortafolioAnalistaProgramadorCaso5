package app.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import app.navigation.Content
import app.views.dashboard_views.dashboard
import app.views.dashboard_views.registerAppointment

@Composable
fun menu(onBack: () -> Unit) {
    var contentState by remember { mutableStateOf<Content>(Content.Dashboard) }
    Row {
        Column(modifier = Modifier
            .weight(2f)
            .padding(0.dp)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text ("Linda Sonrisa",
                    modifier = Modifier.padding(5.dp))
                MenuButton("Dashboard", { contentState = Content.Dashboard})
                MenuButton("Registrar Cita", { contentState = Content.Appointment})
                MenuButton("Registrar Horario", {})
                MenuButton("Registrar Usuario", {})
            }
            Column(modifier = Modifier
                .padding(5.dp)
            ) {
                Button(onClick = { onBack() }){
                    Text("Log out")
                }
            }
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(modifier = Modifier
            .weight(5f)
            .padding(4.dp)
        ) {
            when (val content = contentState) {
                is Content.Dashboard ->
                    dashboard()

                is Content.Appointment ->
                    registerAppointment()
            }
        }
    }
}

@Composable
fun MenuButton(text: String, goTo: () -> Unit) {
    OutlinedButton(
        onClick = { goTo() },
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, contentColor = Color.Transparent)
    ){
        Text(text = text,
            textAlign = TextAlign.Left,
            color = Color.Black,
            fontWeight = FontWeight.Normal)

    }
}