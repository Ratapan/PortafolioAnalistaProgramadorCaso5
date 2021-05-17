package app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate


private val up = Icons.Default.KeyboardArrowUp
private val down = Icons.Default.KeyboardArrowDown

@Composable
fun weekPickerWithLocalDate(title: String,
                            selectLocalDate: LocalDate,
                            setSelectedLocalDate: (LocalDate) -> Unit
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
            dayPicker(selectedDay, setSelectedDay, selectedMonth, selectedYear, modifier = Modifier)
            Spacer(modifier = Modifier.width(10.dp))
            monthPicker(selectedMonth, setSelectedMonth)
            Spacer(modifier = Modifier.width(10.dp))
            yearPicker(selectedYear, setSelectedYear)
        }
        val DateOfStartWeek = selectLocalDate.with(DayOfWeek.MONDAY)
        val DateOfEndWeek = selectLocalDate.with(DayOfWeek.SUNDAY)


        Row (verticalAlignment = Alignment.CenterVertically
        ) {
            tableCell(DateOfStartWeek.dayOfMonth.toString(), modifier)
            tableCell(DateOfStartWeek.month.value.toString(), modifier)
            tableCell(DateOfStartWeek.year.toString(), modifier)
            Text("->>")
            tableCell(DateOfEndWeek.dayOfMonth.toString(), modifier)
            tableCell(DateOfEndWeek.month.value.toString(), modifier)
            tableCell(DateOfEndWeek.year.toString(), modifier)
        }
    }
}
