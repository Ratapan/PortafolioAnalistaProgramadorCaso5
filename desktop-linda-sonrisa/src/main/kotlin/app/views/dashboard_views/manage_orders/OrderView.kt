package app.views.dashboard_views.manage_orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.components.dropdownSelect

enum class ChooseByEnum {
    Ordenes,
    Proveedor,
    Empleado
}

@Composable
fun viewOrders() {
    Column {
        Text("Ordenes", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(10.dp))

        val chooseBy = ChooseByEnum.values().map {
            it to it.toString()
        }

        val (selectedChooseBy, setSelectedChooseBy) = remember { mutableStateOf(0) }

        dropdownSelect("Ver ordenes por", chooseBy, selectedChooseBy, setSelectedChooseBy, Arrangement.Start)

        Spacer(modifier = Modifier.height(10.dp))



        when (chooseBy[selectedChooseBy].first) {
            ChooseByEnum.Ordenes -> {

            }
            ChooseByEnum.Proveedor -> {

            }
            ChooseByEnum.Empleado -> {

            }
        }

    }
}