package app.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.navigation.ContentView
import app.views.dashboard_views.dashboard_main.dashboard
import app.views.dashboard_views.manage_appointment.registerAppointment
import app.views.dashboard_views.manage_hours.hoursView
import app.views.dashboard_views.manage_orders.viewOrders
import app.views.dashboard_views.manage_products.viewProducts
import app.views.dashboard_views.manage_services.viewServices
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
                menuButton("Dashboard") { contentState = ContentView.ContentViewDashboard }
                menuButton("Administrar Citas") { contentState = ContentView.ContentViewAppointment }
                menuButton("Administrar Horarios") { contentState = ContentView.ContentViewHours }
                menuButton("Administrar Usuarios") { contentState = ContentView.ContentViewUser }
//                menuButton("Administrar Ordenes") { contentState = ContentView.ContentViewOrders }
                menuButton("Administrar Productos") { contentState = ContentView.ContentViewProducts }
                menuButton("Administrar Servicios") { contentState = ContentView.ContentViewServices }
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
            when (contentState) {
                is ContentView.ContentViewDashboard ->
                    dashboard()

                is ContentView.ContentViewAppointment ->
                    registerAppointment()

                is ContentView.ContentViewUser ->
                    userView()

                is ContentView.ContentViewHours ->
                    hoursView()

                is ContentView.ContentViewOrders ->
                    viewOrders()

                is ContentView.ContentViewProducts ->
                    viewProducts()

                is ContentView.ContentViewServices ->
                    viewServices()
            }
        }
    }
}

@Composable
fun menuButton(text: String, goTo: () -> Unit) {
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