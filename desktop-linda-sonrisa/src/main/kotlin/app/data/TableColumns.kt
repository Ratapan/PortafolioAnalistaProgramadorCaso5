package app.data

//
//fun Table.chileanDate(name: String): Column<LocalDate> = registerColumn(name, JavaLocalDateColumnTypeChile())
//
//
///**
// * A datetime column to store both a date and a time.
// *
// * @param name The column name
// */
//
//private val DEFAULT_DATE_STRING_FORMATTER by lazy {
//    DateTimeFormatter.ofPattern("dd MM yyyy")
//}
//
//
//private val LocalDate.millis get() = atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
//
//class JavaLocalDateColumnTypeChile : ColumnType(), IDateColumnType {
//    override val hasTimePart: Boolean = false
//
//    override fun sqlType(): String = "DATE"
//
//    override fun nonNullValueToString(value: Any): String {
//        val instant = when (value) {
//            is String -> return value
//            is LocalDate -> Instant.from(value.atStartOfDay(ZoneId.systemDefault()))
//            is java.sql.Date -> Instant.ofEpochMilli(value.time)
//            is java.sql.Timestamp -> Instant.ofEpochSecond(value.time / 1000, value.nanos.toLong())
//            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
//        }
//
//        return "'${DEFAULT_DATE_STRING_FORMATTER.format(instant)}'"
//    }
//
//    override fun valueFromDB(value: Any): Any = when (value) {
//        is LocalDate -> value
//        is java.sql.Date -> longToLocalDate(value.time)
//        is java.sql.Timestamp -> longToLocalDate(value.time)
//        is Int -> longToLocalDate(value.toLong())
//        is Long -> longToLocalDate(value)
//        is String -> when (currentDialect) {
//            is SQLiteDialect -> LocalDate.parse(value)
//            else -> value
//        }
//        else -> LocalDate.parse(value.toString())
//    }
//
//    override fun notNullValueToDB(value: Any) = when {
//        value is LocalDate -> java.sql.Date(value.millis)
//        else -> value
//    }
//
//    private fun longToLocalDate(instant: Long) = Instant.ofEpochMilli(instant).atZone(ZoneId.systemDefault()).toLocalDate()
//
//    companion object {
//        internal val INSTANCE = JavaLocalDateColumnType()
//    }
//}