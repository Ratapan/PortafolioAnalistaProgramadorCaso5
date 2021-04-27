import androidx.compose.desktop.Window
import androidx.compose.runtime.*
import app.navigation.Screen
import app.views.login
import app.views.menu

fun main() = Window {
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