package app.views.dashboard_views.manage_user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.components.tableCell
import app.components.userTableRow
import app.data.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction

private val COLUMN_NAME_VALUES = mapOf(
    "ID" to 1f,
    "Email" to 3f,
    "Rut" to 2f,
    "Nombre" to 3f,
    "Direccion" to 3f,
    "Fecha Nacimiento" to 2f,
    "Eliminado" to 1f,
    "Rol" to 3f
)

@Composable
fun userList(
    toRegister : () -> Unit,
    toEdit : () -> Unit
) {

    val (data, setData) = remember { mutableStateOf(transaction {
        User.all().toList()
    }) }
    val (selectedUser, setSelectedUser) = remember { mutableStateOf(data[0])}
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    userEdit(showDialog, setShowDialog, selectedUser)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text("Lista de Usuarios")
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    for ((k, v) in COLUMN_NAME_VALUES) {
                        tableCell(k, modifier = Modifier
                            .weight(v)
                            .clickable {
                                when (k) {
                                    "ID" ->
                                        setData(data.sortedBy {
                                            it.id
                                        })
                                    "Email" ->
                                        setData(data.sortedBy {
                                            it.email
                                        })
                                    "Rut" ->
                                        setData(data.sortedBy {
                                            it.rut
                                        })
                                    "Nombre" ->
                                        setData(data.sortedBy {
                                            it.nombre
                                        })
                                    "Direccion" ->
                                        setData(data.sortedBy {
                                            it.direccion
                                        })
                                    "Fecha Nacimiento" ->
                                        setData(data.sortedBy {
                                            it.fechaNac
                                        })
                                    "Eliminado" ->
                                        setData(data.sortedBy {
                                            it.eliminado
                                        })
                                    "Rol" ->
                                        setData(data.sortedBy {
                                            it.id_rol
                                        })
                                    else ->
                                        setData(data.sortedBy {
                                            it.id
                                        })
                                }
                            }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    var clickCount = 0
                    items(data, itemContent = { item ->
                        userTableRow(item, modifier = Modifier.clickable(onClick =
                        {
                            clickCount++
                            when (clickCount) {
                                1 -> {
                                    GlobalScope.launch {
                                        delay(1000)
                                        if(clickCount == 1){
                                            clickCount = 0
                                            println("Counter restarted")
                                        }
                                    }
                                }
                                2 -> {
                                    setSelectedUser(item)
                                    setShowDialog(true)
                                    clickCount = 0
                                }
                            }
                        }
                        ))
                    })
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(onClick = { toRegister() }) {
                Text("Register User")
            }
            OutlinedButton(onClick = { toRegister() }) {
                Text("Something")
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}