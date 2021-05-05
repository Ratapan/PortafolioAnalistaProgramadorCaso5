package app.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:oracle:thin:@localhost:1521:xe", driver = "oracle.jdbc.driver.OracleDriver",
        user = "bd", password = "bd")

    transaction {
        addLogger(StdOutSqlLogger)

        Administradores.selectAll().forEach {
            println("${it[Administradores.id_user]}")
        }
    }

    val administradores = transaction {
        Administrador.all().toList()
    }

    val empleados = transaction {
        Empleado.all().toList()
    }
    println(administradores[0].id_user)

    empleados.forEach {
        println(it.id_user)
    }

    transaction {
        val primero = Empleado.findById(1)
        println(primero?.num_telefono)

    }
}