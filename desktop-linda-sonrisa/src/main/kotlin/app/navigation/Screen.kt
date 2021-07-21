package app.navigation

sealed class MainView {
    object MainViewMenu : MainView()
    object MainViewLogin : MainView()
}

sealed class ContentView {
    object ContentViewDashboard : ContentView()
    object ContentViewAppointment : ContentView()
    object ContentViewUser : ContentView()
    object ContentViewHours : ContentView()
    object ContentViewOrders : ContentView()
    object ContentViewProducts : ContentView()
    object ContentViewServices : ContentView()
}

sealed class UserView {
    object UserViewListOf : UserView()
    object UserViewRegister : UserView()
    object UserViewEdit : UserView()
}