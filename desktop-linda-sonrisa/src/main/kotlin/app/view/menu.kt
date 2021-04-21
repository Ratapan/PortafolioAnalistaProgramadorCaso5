package app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun menu(onBack: () -> Unit) {
    Row {
        Column(modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        ) {
            Text ("Hi")
            Button(onClick = { onBack() }){
                Text("Log out")
            }
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Column(modifier = Modifier
            .weight(3f)
            .padding(5.dp)
        ) {
            Text("Content")
        }
    }
}