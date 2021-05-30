package app.data

import java.time.Instant

data class TimeSlot(
    val id: Int = 0,
    val startTime: Instant = Instant.MIN,
    val endTime: Instant = Instant.MIN,
    val status: Char = 'F'
)
