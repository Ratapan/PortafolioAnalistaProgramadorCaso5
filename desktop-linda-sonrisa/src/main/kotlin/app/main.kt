import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.navigation.Screen
import app.view.login
import app.view.menu

fun main() = Window {
//    var text by remember { mutableStateOf("Hello, World!") }
//
//    MaterialTheme {
//        login()
//    }
    Main()
}

@Composable
fun Main() {
    var screenState by remember { mutableStateOf<Screen>(Screen.Login) }

    when (val screen = screenState) {
        is Screen.Login ->
            login(
                onItemClick = { screenState = Screen.Menu }
            )

        is Screen.Menu ->
            menu(
                onBack = { screenState = Screen.Login }
            )
    }
}