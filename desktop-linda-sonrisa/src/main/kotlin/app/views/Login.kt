package app.views

import androidx.compose.desktop.AppManager.exit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import app.components.formSpacer
import app.data.User
import app.data.Users
import com.toxicbakery.bcrypt.Bcrypt
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun login(onItemClick: () -> Unit) {
    var account by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF0699CF), Color(0xFF32DAC3))
                )
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .size(300.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround

            ) {
                val incorrect = remember { mutableStateOf(false) }
                println(incorrect.value)
                val emptyAccount = remember { mutableStateOf(false) }
                val emptyPassword = remember { mutableStateOf(false) }
                Text("Linda Sonrisa")
                OutlinedTextField(value = account,
                    onValueChange = {
                        account = it
                        emptyAccount.value = false
                    },
                    label = { Text("Rut") })
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        emptyPassword.value = false
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                formSpacer(modifier = Modifier.height(10.dp), incorrect.value, "Credenciales ingresadas incorrectas.")
                formSpacer(modifier = Modifier.height(10.dp), emptyAccount.value, "El rut no puede estar vacio.")
                formSpacer(modifier = Modifier.height(10.dp), emptyPassword.value, "Debe ingresar la contraseña.")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        if (account.isEmpty()) {
                            emptyAccount.value = true
                        }
                        if (password.isEmpty()) {
                            emptyPassword.value = true
                        }
                        transaction {
                            val usuario = User.find {
                                Users.rut.lowerCase() eq account.lowercase() and
                                        (Users.rol eq 2)
                            }.toList()
                            if (usuario.isNotEmpty()) {
                                if (Bcrypt.verify(password, usuario.first().password.toByteArray())) {
                                    println("It's a match!")
                                    onItemClick()
                                    incorrect.value = false
                                } else {
                                    incorrect.value = true
                                }
                            } else {
                                incorrect.value = true
                            }
                        }
                    }) {
                        Text(text = "Login")
                    }
                    Button(onClick = { exit() }) {
                        Text(text = "Exit")
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

