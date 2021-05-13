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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun login(onItemClick: () -> Unit) {
    var account by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Row(modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF0699CF), Color(0xFF32DAC3))
        )),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card (modifier = Modifier
            .padding(20.dp)
            .size(300.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column (modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround

            ) {
                Text("Linda Sonrisa")
                OutlinedTextField(value = account,
                    onValueChange = { account = it },
                    label = { Text("Account") })
                OutlinedTextField(value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { onItemClick() }) {
                        Text(text = "Login")
                    }
                    Button(onClick = { exit() }) {
                        Text(text = "Exit")
                    }
                }
            }
        }
    }
}

