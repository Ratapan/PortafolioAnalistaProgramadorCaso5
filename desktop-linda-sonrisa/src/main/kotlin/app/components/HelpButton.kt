package app.components

import androidx.compose.foundation.BoxWithTooltip
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun helpButton(
    text: String = "?",
    tooltip: @Composable () -> Unit? = {  }
) {
    BoxWithTooltip(tooltip = {
        tooltip()
    },
        delay = 0
    ){
        Text(text,
            modifier = Modifier
                .size(30.dp)
                .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}