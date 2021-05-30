package app.views.dashboard_views.manage_user

import androidx.compose.runtime.*
import app.navigation.UserView

@Composable
fun userView() {
    var contentState by remember { mutableStateOf<UserView>(UserView.UserViewListOf) }
    when (contentState) {
        is UserView.UserViewListOf ->
            userList(
                toRegister = {
                    contentState = UserView.UserViewRegister
                },
                toEdit = {
                    contentState = UserView.UserViewEdit
                }
            )
        is UserView.UserViewEdit -> {

        }
        is UserView.UserViewRegister -> {
            userRegister()
        }
    }
}