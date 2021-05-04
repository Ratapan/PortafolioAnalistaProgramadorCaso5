package app.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:oracle:thin:@localhost:1521:xe", driver = "oracle.jdbc.driver.OracleDriver",
        user = "c##LindaSonrisa", password = "LindaSonrisa")

    transaction {
        addLogger(StdOutSqlLogger)

        Administrador.selectAll().forEach {
            println("${it[Administrador.id]}: ${it[Administrador.correo]}")
        }
    }
}