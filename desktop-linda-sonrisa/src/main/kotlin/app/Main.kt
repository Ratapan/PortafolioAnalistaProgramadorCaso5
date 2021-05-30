package app

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.runtime.*
import app.navigation.MainView
import app.views.login
import app.views.menu
import org.jetbrains.exposed.sql.Database

fun main() = Window {
    mainWindow()
}

@Composable
fun mainWindow() {
    Database.connect("jdbc:oracle:thin:@localhost:1521:xe", driver = "oracle.jdbc.driver.OracleDriver",
        user = "bd", password = "bd")
    var screenState by remember { mutableStateOf<MainView>(MainView.MainViewLogin) }

    LocalAppWindow.current.apply {
        events.onOpen = { maximize() }
        window.title ="Linda Sonrisa - Desktop"
    }

    when (screenState) {
        is MainView.MainViewLogin ->
            login(
                onItemClick = { screenState = MainView.MainViewMenu }
            )

        is MainView.MainViewMenu ->
            menu(
                onBack = { screenState = MainView.MainViewLogin }
            )
    }
}