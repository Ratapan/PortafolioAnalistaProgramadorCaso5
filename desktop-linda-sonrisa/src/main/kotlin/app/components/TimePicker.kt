package app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.*

public enum class SectionOfDay {
    AM,
    PM
}


@Composable
fun timePicker(title: String = "Time Picker", time: Instant, setTime: (Instant) -> Unit, twentyFourHourFormat: Boolean = true, setSeconds: Boolean = false, minHour: Int = 0, maxHour: Int = 23, disabled: Boolean = false) {

    // DatePicker
    val (selectedHour, setSelectedHour) = remember { mutableStateOf(LocalTime.ofInstant(time, ZoneId.of("+0")).hour.toString()) }
    val (selectedMinute, setSelectedMinute) = remember { mutableStateOf(LocalTime.ofInstant(time, ZoneId.of("+0")).minute.toString()) }
    val (selectedSecond, setSelectedSecond) = remember { mutableStateOf(LocalTime.ofInstant(time, ZoneId.of("+0")).second.toString()) }


    val (selectedSectionOfDay, setSelectedSectionOfDay) = remember { mutableStateOf(SectionOfDay.AM) }

    setTime(LocalDateTime.of(LocalDate.ofInstant(time, ZoneId.of("-4")), LocalTime.of(selectedHour.toInt(), selectedMinute.toInt(), selectedSecond.toInt())).toInstant(
        ZoneOffset.of("-4")))

    Column{
        Text("${title}:", fontSize = 20.sp)
        Row (verticalAlignment = Alignment.CenterVertically
        ) {
            hourPicker(selectedHour, setSelectedHour, twentyFourHourFormat, selectedSectionOfDay, minHour, maxHour, disabled)
            Text(" : ", fontSize = 20.sp)
            minutePicker(selectedMinute, setSelectedMinute, disabled)
            if (setSeconds) {
                Text(" : ", fontSize = 20.sp)
                secondPicker(selectedSecond, setSelectedSecond, disabled)
            }
            if (!twentyFourHourFormat) {
                Spacer(modifier = Modifier.width(5.dp))
                sectionOfDayPicker(selectedSectionOfDay, setSelectedSectionOfDay)
            }
        }

    }

}
@Composable
fun sectionOfDayPicker(selectedSectionOfDay: SectionOfDay, setSelectedSectionOfDay: (SectionOfDay) -> Unit) {

}

@Composable
fun secondPicker(selectedSecond: String, setSelectedSecond: (String) -> Unit, disabled: Boolean) {
    var secondText by remember { mutableStateOf(selectedSecond) }
    if (secondText.length > 2) {
        secondText = secondText.takeLast(2)
    }
    val minTime = 0
    val maxTime = 59

    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value =
        when (selectedSecond.length) {
            1 -> "0$selectedSecond"
            else -> selectedSecond
        },
            singleLine = true,
            onValueChange = {
                if (!disabled) {
                    if (it.isEmpty()) {
                        setSelectedSecond(1.toString())
                    } else {
                        var hourValue = minTime
                        if (it.length <= 3) {
                            hourValue = it.filter { it.isDigit() }.toInt()
                        }
                        secondText += hourValue.toString().last()
                        when {
                            hourValue < minTime -> {
                                setSelectedSecond(minTime.toString())
                            }
                            hourValue >= maxTime -> {
                                when(secondText.takeLast(2).first()) {
                                    '0' -> {
                                        if (secondText.takeLast(1) == "0") {
                                            setSelectedSecond(minTime.toString())
                                        } else {
                                            setSelectedSecond(secondText.takeLast(1))

                                        }
                                    }
                                    '1' -> {
                                        setSelectedSecond(secondText.takeLast(2))
                                    }
                                    '2' -> {
                                        if (secondText.takeLast(1).toInt() > maxTime.toString().takeLast(1).toInt()) {
                                            setSelectedSecond(maxTime.toString())
                                        } else {
                                            setSelectedSecond(secondText.takeLast(2))
                                        }
                                    }
                                    else -> {
                                        setSelectedSecond(maxTime.toString())
                                    }
                                }
                            }
                            else -> {
                                setSelectedSecond(hourValue.toString())
                            }
                        }
                    }
                }
            },
            label = { Text("Segundos") },
            modifier = Modifier.size(80.dp, 60.dp)
        )
    }
}

@Composable
fun minutePicker(selectedMinute: String, setSelectedMinute: (String) -> Unit, disabled: Boolean) {
    var minuteText by remember { mutableStateOf(selectedMinute) }
    if (minuteText.length > 2) {
        minuteText = minuteText.takeLast(2)
    }
    val minTime = 0
    val maxTime = 59

    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value =
        when (selectedMinute.length) {
            1 -> "0$selectedMinute"
            else -> selectedMinute
        },
            singleLine = true,
            onValueChange = {
                if (!disabled) {
                    if (it.isEmpty()) {
                        setSelectedMinute(1.toString())
                    } else {
                        var hourValue = minTime
                        if (it.length <= 3) {
                            hourValue = it.filter { it.isDigit() }.toInt()
                        }
                        minuteText += hourValue.toString().last()
                        when {
                            hourValue < minTime -> {
                                setSelectedMinute(minTime.toString())
                            }
                            hourValue >= maxTime -> {
                                when(minuteText.takeLast(2).first()) {
                                    '0' -> {
                                        if (minuteText.takeLast(1) == "0") {
                                            setSelectedMinute(minTime.toString())
                                        } else {
                                            setSelectedMinute(minuteText.takeLast(1))

                                        }
                                    }
                                    '1' -> {
                                        setSelectedMinute(minuteText.takeLast(2))
                                    }
                                    '2' -> {
                                        if (minuteText.takeLast(1).toInt() > maxTime.toString().takeLast(1).toInt()) {
                                            setSelectedMinute(maxTime.toString())
                                        } else {
                                            setSelectedMinute(minuteText.takeLast(2))
                                        }
                                    }
                                    else -> {
                                        setSelectedMinute(maxTime.toString())
                                    }
                                }
                            }
                            else -> {
                                setSelectedMinute(hourValue.toString())
                            }
                        }
                    }
                }
            },
            label = { Text("Minutos") },
            modifier = Modifier.size(80.dp, 60.dp)
        )
    }
}

@Composable
fun hourPicker(
    selectedHour: String,
    setSelectedHour: (String) -> Unit,
    twentyFourHourFormat: Boolean,
    selectedSectionOfDay: SectionOfDay,
    minHour: Int,
    maxHour: Int,
    disabled: Boolean
) {
    var hourText by remember { mutableStateOf(selectedHour) }
    if (hourText.length > 2) {
        hourText = hourText.takeLast(2)
    }
    val minTime = minHour
    val maxTime = maxHour

    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(value =
            when (selectedHour.length) {
                1 -> "0$selectedHour"
                else -> selectedHour
            },
            singleLine = true,
            onValueChange = {
                if (disabled) {
                    if (it.isEmpty()) {
                        setSelectedHour(minHour.toString())
                    } else {
                        var hourValue = minTime
                        if (it.length <= 3) {
                            hourValue = it.filter { it.isDigit() }.toInt()
                        }
                        hourText += hourValue.toString().last()
                        when {
                            hourValue < minTime -> {
                                setSelectedHour(minTime.toString())
                            }
                            hourValue >= maxTime -> {
                                when(hourText.takeLast(2).first()) {
                                    '0' -> {
                                        if (hourText.takeLast(1) == "0") {
                                            setSelectedHour(minTime.toString())
                                        } else {
                                            setSelectedHour(hourText.takeLast(1))

                                        }
                                    }
                                    '1' -> {
                                        setSelectedHour(hourText.takeLast(2))
                                    }
                                    '2' -> {
                                        if (hourText.takeLast(1).toInt() > maxTime.toString().takeLast(1).toInt()) {
                                            setSelectedHour(maxTime.toString())
                                        } else {
                                            setSelectedHour(hourText.takeLast(2))
                                        }
                                    }
                                    else -> {
                                        setSelectedHour(maxTime.toString())
                                    }
                                }
                            }
                            else -> {
                                setSelectedHour(hourValue.toString())
                            }
                        }
                    }
                }
            },
            label = { Text("Horas") },
            modifier = Modifier.size(80.dp, 60.dp)
        )
    }
}
