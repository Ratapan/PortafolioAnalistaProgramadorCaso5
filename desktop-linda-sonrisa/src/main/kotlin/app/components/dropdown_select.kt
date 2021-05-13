package app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


@Composable
fun dropdownSelect(name: String, list: List<Pair<Any, String>>, selectedValue: Int, setSelectedValue: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$name:", fontSize = 20.sp)
        Box {
            OutlinedButton(
                onClick = {
                    expanded = true
                },
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                Text(list[selectedValue].second, color = Color.Black)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
            ) {
                list.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        onClick = {
                            setSelectedValue(index)
                            expanded = false
                        },
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                    ) {
                        Text(text = s.second)
                    }
                }
            }
        }
    }

}

