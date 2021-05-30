package app.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction

@Composable
fun focusableOutlinedTextField(
    text: String,
    onValueChange : () -> Unit,
    focusRequester: FocusRequester,
    focusRequesterNext: FocusRequester
) {
    OutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key.keyCode == Key.Tab.keyCode){
                    focusRequesterNext.requestFocus()
                    true //true -> consumed
                } else false },
        value = text,
        onValueChange = { onValueChange() },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {focusRequesterNext.requestFocus()}
        )
    )
}