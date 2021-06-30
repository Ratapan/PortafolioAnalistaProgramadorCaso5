package app.data


sealed class QueryResult(
    val columnName: String
) {
    data class StringQueryResult(
        val columnNameValue: String,
        val columnValue: String
    ) : QueryResult(columnNameValue)


    data class IntQueryResult(
        val columnNameValue: String,
        val columnValue: Int
    ) : QueryResult(columnNameValue)
}