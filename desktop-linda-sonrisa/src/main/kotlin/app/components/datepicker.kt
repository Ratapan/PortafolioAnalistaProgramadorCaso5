package app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth




private val days = listOf(1..31)

private val up = Icons.Default.KeyboardArrowUp
private val down = Icons.Default.KeyboardArrowDown

@Composable
fun datePicker(title: String,
    selectDay: String,
    setSelectedDay: (String) -> Unit,
    selectMonth: String,
    setSelectedMonth: (String) -> Unit,
    selectYear: String,
    setSelectedYear: (String) -> Unit,
) {
    Row (verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${title}:", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(10.dp))
        dayPicker(selectDay, setSelectedDay, selectMonth, selectYear, modifier = Modifier)
        Spacer(modifier = Modifier.width(10.dp))
        monthPicker(selectMonth, setSelectedMonth)
        Spacer(modifier = Modifier.width(10.dp))
        yearPicker(selectYear, setSelectedYear)
    }
}

@Composable
fun datePickerWithLocalDate(title: String,
                            selectLocalDate: LocalDate,
                            setSelectedLocalDate: (LocalDate) -> Unit
) {
    // DatePicker
    val (selectedDay, setSelectedDay) = remember { mutableStateOf(selectLocalDate.dayOfMonth.toString())}
    val (selectedMonth, setSelectedMonth) = remember { mutableStateOf(selectLocalDate.monthValue.toString())}
    val (selectedYear, setSelectedYear) = remember { mutableStateOf(selectLocalDate.year.toString())}

    setSelectedLocalDate(LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()))

    Row (verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${title}:", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(10.dp))
        dayPicker(selectedDay, setSelectedDay, selectedMonth, selectedYear, modifier = Modifier)
        Spacer(modifier = Modifier.width(10.dp))
        monthPicker(selectedMonth, setSelectedMonth)
        Spacer(modifier = Modifier.width(10.dp))
        yearPicker(selectedYear, setSelectedYear)
    }
}

@Composable
fun dayPicker(
    selectDay: String,
    setSelectedDay: (String) -> Unit,
    selectMonth: String,
    selectYear: String,
    modifier: Modifier.Companion
) {
    val yearMonthObject = YearMonth.of(selectYear.toInt(), selectMonth.toInt())
    val maxDays = yearMonthObject.lengthOfMonth()

    var dayText by remember { mutableStateOf(selectDay) }
    if (dayText.length > 2) {
        dayText = dayText.takeLast(2)
    }

    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = selectDay,
            singleLine = true,
            onValueChange = {
                if (it.isEmpty()) {
                    setSelectedDay(1.toString())
                } else {
                    var dayValue = 1
                    if (it.length <= 3) {
                        dayValue = it.filter { it.isDigit() }.toInt()
                    }
                    dayText += dayValue.toString().last()
                    when {
                        dayValue < 1 -> {
                            setSelectedDay(1.toString())
                        }
                        dayValue >= maxDays -> {
                            when(dayText.takeLast(2).first()) {
                                '0' -> {
                                    setSelectedDay(dayText.takeLast(1))
                                }
                                '1', '2' -> {
                                    setSelectedDay(dayText.takeLast(2))
                                }
                                '3' -> {
                                    if (dayText.takeLast(1).toInt() > 1) {
                                        setSelectedDay(maxDays.toString())
                                    } else {
                                        setSelectedDay(dayText.takeLast(2))
                                    }
                                }
                                else -> {
                                    setSelectedDay(maxDays.toString())
                                }
                            }
                        }
                        else -> {
                            setSelectedDay(dayValue.toString())
                        }
                    }
                }
            },
            label = { Text("Day") },
          modifier = Modifier.size(60.dp, 50.dp)
        )
        Column {
            Image(up,
                "Add Day",
                modifier = Modifier.clickable {
                    val newDay = (selectDay.toInt()+1)
                    when {
                        newDay < 1 -> {
                            setSelectedDay(1.toString())
                        }
                        newDay >= maxDays -> {
                            setSelectedDay(maxDays.toString())
                        }
                        else -> {
                            setSelectedDay(newDay.toString())
                        }
                    }
                }
            )
            Image(down,
                "Add Day",
                modifier = Modifier.clickable {
                    val newDay = (selectDay.toInt()-1)
                    when {
                        newDay < 1 -> {
                            setSelectedDay(1.toString())
                        }
                        newDay >= maxDays -> {
                            setSelectedDay(maxDays.toString())
                        }
                        else -> {
                            setSelectedDay(newDay.toString())
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun monthPicker(
    selectMonth: String,
    setSelectedMonth: (String) -> Unit
) {
    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = selectMonth,
            singleLine = true,
            onValueChange = {
                if (it.isEmpty()) {
                    setSelectedMonth(1.toString())
                } else {
                    var monthValue = 1
                    if (it.length <= 3) {
                        monthValue = it.filter { it.isDigit() }.toInt()
                    }
                    when {
                        monthValue <= 0 -> {
                            setSelectedMonth(1.toString())
                        }
                        monthValue > 12 -> {
                            setSelectedMonth(monthValue.toString().last().toString())
                        }
                        else -> {
                            setSelectedMonth(monthValue.toString())
                        }
                    }
                }
            },
            label = { Text("Month") },
        modifier = Modifier.size(70.dp, 50.dp)
        )
        Column {
            Image(up,
                "Add Month",
                modifier = Modifier.clickable {
                    val newMonth = (selectMonth.toInt()+1)
                    when {
                        newMonth < 1 -> {
                            setSelectedMonth(1.toString())
                        }
                        newMonth >= 12 -> {
                            setSelectedMonth(12.toString())
                        }
                        else -> {
                            setSelectedMonth(newMonth.toString())
                        }
                    }
                }
            )
            Image(down,
                "Sub Month",
                modifier = Modifier.clickable {
                    val newMonth = (selectMonth.toInt()-1)
                    when {
                        newMonth < 1 -> {
                            setSelectedMonth(1.toString())
                        }
                        newMonth >= 12 -> {
                            setSelectedMonth(12.toString())
                        }
                        else -> {
                            setSelectedMonth(newMonth.toString())
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun yearPicker(
    selectYear: String,
    setSelectedYear: (String) -> Unit
) {
    var yearText by remember { mutableStateOf("990") }
    if (yearText.length > 3) {
        yearText = yearText.takeLast(3)
    }
    Row ( verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(value = selectYear,
            singleLine = true,
            onValueChange = {
                if (it.isEmpty()) {
                    setSelectedYear(1990.toString())
                } else {
                    var numValue = 1990
                    if (it.length <= 5) {
                        numValue = it.filter { it.isDigit() }.toInt()
                    }
                    when {
                        numValue < 1900 -> {
                            setSelectedYear(1900.toString())
                        }
                        numValue >= 2100 -> {
                            yearText += numValue.toString().last()
                            when {
                                yearText.takeLast(3).first() == '0' -> {
                                    setSelectedYear("2${yearText.takeLast(3)}")
                                }
                                yearText.takeLast(3).first() == '9' -> {
                                    setSelectedYear("1${yearText.takeLast(3)}")
                                }
                                else -> {
                                    setSelectedYear(2100.toString())
                                }
                            }
                        }
                        else -> {
                            setSelectedYear(numValue.toString())
                        }
                    }
                }
            },
            label = { Text("Year") },
            modifier = Modifier
                .height(50.dp)
                .width(90.dp)
        )
        Column {
            Image(up,
                "Add Year",
                modifier = Modifier.clickable {
                    val newYear = (selectYear.toInt()+1)
                    when {
                        newYear <= 1899 -> {
                            setSelectedYear(1900.toString())
                        }
                        newYear >= 2100 -> {
                            setSelectedYear(2100.toString())
                        }
                        else -> {
                            setSelectedYear(newYear.toString())
                        }
                    }
                }
            )
            Image(down,
                "Sub Year",
                modifier = Modifier.clickable {
                    val newYear = (selectYear.toInt()-1)
                    when {
                        newYear < 1900 -> {
                            setSelectedYear(1900.toString())
                        }
                        newYear >= 2100 -> {
                            setSelectedYear(2100.toString())
                        }
                        else -> {
                            setSelectedYear(newYear.toString())
                        }
                    }
                }
            )
        }
    }
}