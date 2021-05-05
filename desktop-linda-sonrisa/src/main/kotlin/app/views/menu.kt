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
import app.navigation.ContentView
import app.views.dashboard_views.dashboard_main.dashboard
import app.views.dashboard_views.manage_appointment.registerAppointment
import app.views.dashboard_views.manage_user.userView

@Composable
fun menu(onBack: () -> Unit) {
    var contentState by remember { mutableStateOf<ContentView>(ContentView.ContentViewDashboard) }
    Row {
        Column(modifier = Modifier
            .width(200.dp)
            .padding(0.dp)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text ("Linda Sonrisa",
                    modifier = Modifier.padding(5.dp))
                MenuButton("Dashboard", { contentState = ContentView.ContentViewDashboard })
                MenuButton("Administrar Citas", { contentState = ContentView.ContentViewAppointment })
                MenuButton("Administrar Horarios", {})
                MenuButton("Administrar Usuarios", { contentState = ContentView.ContentViewUser })
            }
            Column(modifier = Modifier
                .padding(5.dp)
            ) {
                OutlinedButton(onClick = { onBack() }){
                    Text("Log out", color = Color.Black)
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
            .fillMaxWidth()
            .padding(4.dp)
        ) {
            when (val content = contentState) {
                is ContentView.ContentViewDashboard ->
                    dashboard()

                is ContentView.ContentViewAppointment ->
                    registerAppointment()

                is ContentView.ContentViewUser ->
                    userView()
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