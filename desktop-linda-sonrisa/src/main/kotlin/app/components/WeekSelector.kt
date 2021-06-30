package app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate


@Composable
fun weekPickerWithLocalDate(title: String,
                            selectLocalDate: LocalDate,
                            setSelectedLocalDate: (LocalDate) -> Unit,
                            disabled: Boolean = false
) {
    // DatePicker
    val (selectedDay, setSelectedDay) = remember { mutableStateOf(selectLocalDate.dayOfMonth.toString())}
    val (selectedMonth, setSelectedMonth) = remember { mutableStateOf(selectLocalDate.monthValue.toString())}
    val (selectedYear, setSelectedYear) = remember { mutableStateOf(selectLocalDate.year.toString())}

    setSelectedLocalDate(LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()))

    val modifier = Modifier.size(30.dp, 30.dp)

    Column {
        Row (verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${title}:", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            dayPicker(selectedDay, setSelectedDay, selectedMonth, selectedYear, disabled)
            Spacer(modifier = Modifier.width(10.dp))
            monthPicker(selectedMonth, setSelectedMonth, disabled)
            Spacer(modifier = Modifier.width(10.dp))
            yearPicker(selectedYear, setSelectedYear, disabled)
        }
        val dateOfStartWeek = selectLocalDate.with(DayOfWeek.MONDAY)
        val dateOfEndWeek = selectLocalDate.with(DayOfWeek.SUNDAY)


        Row (verticalAlignment = Alignment.CenterVertically
        ) {
            tableCell(dateOfStartWeek.dayOfMonth.toString(), modifier)
            tableCell(dateOfStartWeek.month.value.toString(), modifier)
            tableCell(dateOfStartWeek.year.toString(), modifier)
            Text("->>")
            tableCell(dateOfEndWeek.dayOfMonth.toString(), modifier)
            tableCell(dateOfEndWeek.month.value.toString(), modifier)
            tableCell(dateOfEndWeek.year.toString(), modifier)
        }
    }
}
