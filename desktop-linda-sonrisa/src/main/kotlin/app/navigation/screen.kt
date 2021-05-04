package app.navigation

sealed class Screen {
    object Menu : Screen()
    object Login : Screen()
}

sealed class Content {
    object Dashboard : Content()
    object Appointment : Content()
    object User : Content()
}