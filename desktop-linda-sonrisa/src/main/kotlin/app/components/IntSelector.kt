package app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val up = Icons.Default.KeyboardArrowUp
private val down = Icons.Default.KeyboardArrowDown

@Composable
fun intSelector(selectedInt: Int, setSelelectedInt: (Int) -> Unit,title: String = "Int", minInt: Int = 1, maxInt: Int = Int.MAX_VALUE, modifier : Modifier) {

    var dayText by remember { mutableStateOf(selectedInt.toString()) }
    if (dayText.length > selectedInt.toString().length) {
        dayText = dayText.takeLast(selectedInt.toString().length)
    }

    Row (verticalAlignment = Alignment.CenterVertically, modifier = modifier
    ) {
        Text("${title}:", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(value = selectedInt.toString(),
                singleLine = true,
                onValueChange = {
                    if (it.isEmpty()) {
                        setSelelectedInt(1)
                    } else {
                        var dayValue = 1
                        if (it.length <= 3) {
                            dayValue = it.filter { it.isDigit() }.toInt()
                        }
                        dayText += dayValue.toString().last()
                        when {
                            dayValue < minInt -> {
                                setSelelectedInt(minInt)
                            }
                            dayValue >= maxInt -> {
                                when(dayText.takeLast(2).first()) {
                                    '0' -> {
                                        if (dayText.takeLast(1) == "0") {
                                            setSelelectedInt(minInt)
                                        } else {
                                            if (dayText.takeLast(1).toInt() > maxInt) {
                                                setSelelectedInt(maxInt)
                                            } else {
                                                setSelelectedInt(dayText.takeLast(1).toInt())
                                            }
                                        }
                                    }
                                    else -> {
                                        setSelelectedInt(maxInt)
                                    }
                                }
                            }
                            else -> {
                                setSelelectedInt(dayValue)
                            }
                        }
                    }
                },
                label = { Text("#") },
                modifier = Modifier.size(60.dp, 60.dp)
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                Image(up,
                    "Add 1",
                    modifier = Modifier.clickable {
                        val newDay = (selectedInt.toInt()+1)
                        when {
                            newDay < 1 -> {
                                setSelelectedInt(1)
                            }
                            newDay >= maxInt -> {
                                setSelelectedInt(maxInt)
                            }
                            else -> {
                                setSelelectedInt(newDay)
                            }
                        }
                    }
                )
                Image(down,
                    "Sub 1",
                    modifier = Modifier.clickable {
                        val newDay = (selectedInt.toInt()-1)
                        when {
                            newDay < 1 -> {
                                setSelelectedInt(1)
                            }
                            newDay >= maxInt -> {
                                setSelelectedInt(maxInt)
                            }
                            else -> {
                                setSelelectedInt(newDay)
                            }
                        }
                    }
                )
            }
    }
}