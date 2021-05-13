package app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun formSpacer(modifier : Modifier = Modifier, validation: Boolean = false, text: String = "") {
    Box  {
        Row(modifier = Modifier){
            DisableSelection {
                Text(" ")
            }
        }
        if (validation) {
            Text(text = text,
                color = Color.Red,
                fontSize = 15.sp
            )
        }
    }
}
