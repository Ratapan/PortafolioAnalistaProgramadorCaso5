package app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.data.User

private const val ID_COLUMN_WEIGHT = 1f
private const val EMAIL_COLUMN_WEIGHT = 3f
private const val RUT_COLUMN_WEIGHT = 2f
private const val NOMBRE_COLUMN_WEIGHT = 3f
private const val DIRECCION_COLUMN_WEIGHT = 3f
private const val FECHA_NACIMIENTO_COLUMN_WEIGHT = 2f
private const val ELIMINADO_COLUMN_WEIGHT = 1f
private const val ROL_COLUMN_WEIGHT = 3f

@Composable
fun userTableRow (
    user : User,
    modifier : Modifier
) {
    Row( modifier = modifier
    ) {
        tableCell(user.id.toString(), modifier = Modifier.weight(ID_COLUMN_WEIGHT))
        tableCell(user.email, modifier = Modifier.weight(EMAIL_COLUMN_WEIGHT))
        tableCell(user.rut, modifier = Modifier.weight(RUT_COLUMN_WEIGHT))
        tableCell(user.nombre, modifier = Modifier.weight(NOMBRE_COLUMN_WEIGHT))
        tableCell(user.direccion, modifier = Modifier.weight(DIRECCION_COLUMN_WEIGHT))
        tableCell(user.fecha_nac.toString(), modifier = Modifier.weight(FECHA_NACIMIENTO_COLUMN_WEIGHT))
        tableCell(
            when(user.eliminado){
                '1' -> "False"
                else -> "True"
            }, modifier = Modifier.weight(ELIMINADO_COLUMN_WEIGHT))
        tableCell(
            when(user.id_rol) {
                1 -> "Paciente"
                2 -> "Administrador"
                3 -> "Empleado"
                4 -> "Proveedor"
                else -> "Error"
            }, modifier = Modifier.weight(ROL_COLUMN_WEIGHT))
    }
}

@Composable
fun tableCell(string : String, modifier: Modifier) {
    Column(modifier = modifier
        .border(BorderStroke(1.dp, Color.Black))
    ){
        Text(string, modifier = Modifier.padding(5.dp))
    }
}