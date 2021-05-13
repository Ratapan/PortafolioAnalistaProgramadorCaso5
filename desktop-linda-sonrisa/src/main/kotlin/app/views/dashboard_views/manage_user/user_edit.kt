package app.views.dashboard_views.manage_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import app.data.User
import org.jetbrains.exposed.sql.transactions.transaction

private val eliminate = listOf(
"False",
"True"
)

@Composable
fun userEdit(active: Boolean, setShowDialog: (Boolean) -> Unit, user: User) {

    if (active) {

        val id by remember { mutableStateOf(user.id) }
        var email by remember { mutableStateOf(user.email) }
        var nombre by remember { mutableStateOf(user.nombre) }
        var eliminado by remember { mutableStateOf(user.eliminado) }
//        var rol by remember { mutableStateOf(user.id_rol) }

        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
            },
            title = {
            },
            confirmButton = {
            },
            dismissButton = {
            },
            text = {
                Column (modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround

                ) {
                    Text("Editing User \"${nombre}\"")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = id.toString(),
                        onValueChange = { },
                        label = { Text("Id") },
                        modifier = Modifier.fillMaxWidth())

                    OutlinedTextField(value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth())

                    OutlinedTextField(value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth())


                    var expanded by remember { mutableStateOf(false) }
//                    val roles = transaction {
//                        Rol.all().toList()
//                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(onClick = {
                        expanded = true
                    },
                        modifier = Modifier.fillMaxWidth()
                    ){
                        val rol_value = when (eliminado) {
                            '1' -> "False"
                            else -> "True"
                        }
                        Text("Eliminado: ${rol_value}")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth().background(
                            Color.Gray)
                    ) {
                        eliminate.forEachIndexed { index, s ->
                            DropdownMenuItem(onClick = {
                                println(index)
                                eliminado = when (index) {
                                    0 -> '1'
                                    else -> 'T'
                                }
                                expanded = false
                            }) {
                                Text(text = s)
                            }
                        }
                    }

//                    OutlinedButton(onClick = {
//                        expanded = true
//                    }){
//                        val rol_value = when (rol) {
//                            1 -> "Paciente"
//                            2 -> "Administrador"
//                            3 -> "Empleado"
//                            4 -> "Proveedor"
//                            else -> "Error"
//                        }
//                        Text("Rol: ${rol_value}")
//                    }

//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false },
//                        modifier = Modifier.fillMaxWidth().background(
//                            Color.Red)
//                    ) {
//                        roles.forEachIndexed { index, s ->
//                            DropdownMenuItem(onClick = {
//                                rol = index+1
//                                expanded = false
//                            }) {
//                                Text(text = s.nombre)
//                            }
//                        }
//                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = { setShowDialog(false)
                            transaction {
                                val usuario = User.findById(id)
                                usuario?.nombre = nombre
                                usuario?.email = email
                                usuario?.eliminado = eliminado
//                                usuario?.id_rol = rol
                            }
                        }) {
                            Text(text = "Confirmar")
                        }
                        Button(onClick = { setShowDialog(false) }) {
                            Text(text = "Cancelar")
                        }
                    }

                }
            },
            properties = DialogProperties(
                title = "Edit user",
                size = IntSize(400, 700)
            )
        )
    }
}