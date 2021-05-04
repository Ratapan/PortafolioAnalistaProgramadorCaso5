package app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.views.dashboard_views.manage_user.User

private const val ID_COLUMN_WEIGHT = 1f
private const val EMAIL_COLUMN_WEIGHT = 3f
private const val PASSWORD_COLUMN_WEIGHT = 3f
private const val ELIMINADO_COLUMN_WEIGHT = 1f
private const val ROL_COLUMN_WEIGHT = 3f

@Composable
fun tableRow (
    user : User = User("1", "email1", "password1", 'F', "rol1")
) {
    Row {
        tableCell(user.id, modifier = Modifier.weight(ID_COLUMN_WEIGHT))
        tableCell(user.email, modifier = Modifier.weight(EMAIL_COLUMN_WEIGHT))
        tableCell(user.password, modifier = Modifier.weight(PASSWORD_COLUMN_WEIGHT))
        tableCell(user.eliminado.toString(), modifier = Modifier.weight(ELIMINADO_COLUMN_WEIGHT))
        tableCell(user.rol, modifier = Modifier.weight(ROL_COLUMN_WEIGHT))
    }
}

@Composable
fun tableCell(string : String, modifier: Modifier) {
    Column(modifier = modifier
        .border(BorderStroke(1.dp, Color.Black))
    ){
        Text(string)
    }
}