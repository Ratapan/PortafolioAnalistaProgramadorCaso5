package app.views.dashboard_views.manage_user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.components.tableRow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class User(
    val id: String,
    val email: String,
    val password: String,
    val eliminado: Char?,
    val rol: String,
)

@Composable
fun userList(){
    var data = listOf<User>(
        User("1", "email1", "password1", null, "rol1",),
        User("2", "email2", "password2", 'F', "rol2",),
        User("3", "email3", "password3", 'F', "rol3",),
        User("4", "email4", "password4", 'F', "rol4",),
        User("5", "email5", "password5", 'F', "rol5",),
        User("6", "email6", "password6", 'F', "rol6",),
        User("7", "email7", "password7", 'F', "rol7",)
    )
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
    ) {
        Spacer(modifier=Modifier.height(10.dp))
        Text("Lista de Usuarios")
        Row (modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ){
            Spacer(modifier=Modifier.height(10.dp))
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
            ){
                items(data, itemContent = { item ->
                    tableRow(item)
                })
            }
            Spacer(modifier=Modifier.height(10.dp))

        }
    }
}