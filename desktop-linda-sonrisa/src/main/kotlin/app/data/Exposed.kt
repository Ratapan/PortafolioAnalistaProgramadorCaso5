package app.data

import org.jetbrains.exposed.sql.Database
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/*
 *
 * WARNING: This file purpose it's just to test the database connection. Pwease ignore it. ;-;
 *
 */

//fun app.main() {
//    Database.connect("jdbc:oracle:thin:@localhost:1521:xe", driver = "oracle.jdbc.driver.OracleDriver",
//        user = "bd", password = "bd")
//
////    transaction {
////        addLogger(StdOutSqlLogger)
////
////        Administradores.selectAll().forEach {
////            println("${it[Administradores.id_user]}")
////        }
////    }
////
////    val administradores = transaction {
////        Administrador.all().toList()
////    }
//
//    val users = transaction {
//        addLogger(StdOutSqlLogger)
//        User.all().toList()
//    }
////    println(administradores[0].id_user)
//
//    users.forEach {
//        println("user")
//        println(it.nombre)
//
//// Verify a given password matches a previously hashed password
//        var nombres = it.nombre.split(" ")
//        var password = nombres[0].decapitalize()+"1234"
//        if (Bcrypt.verify(password, it.password.toByteArray())) {
//            println("It's a match!")
//        }
//    }
//
//    transaction {
//        addLogger(StdOutSqlLogger)
//        val primero = Empleado.findById(1)
//        println(primero?.num_telefono)
//
//    }
//}
fun main() {

    Database.connect("jdbc:oracle:thin:@localhost:1521:xe", driver = "oracle.jdbc.driver.OracleDriver",
        user = "bd", password = "bd")

    println(LocalDateTime.now())
    println(Date().toInstant().atZone(ZoneId.of("GMT")))
    println(Date().toInstant().atZone(ZoneId.systemDefault()))
    println(ZoneId.systemDefault())
    println(LocalDate.now().atTime(10, 0).atZone(ZoneId.of("GMT")).toInstant())

//    transaction {
//        Horas.insert {
//        addLogger(StdOutSqlLogger)
//            it[id] = 0
//            it[hora_inicio] = LocalDate.now().atTime(10, 0).atZone(ZoneId.of("GMT")).toInstant()
//            it[hora_fin] = LocalDate.now().atTime(10, 30).atZone(ZoneId.of("GMT")).toInstant()
//            it[estado] = 'D'
//            it[id_empleado] = 7
//        }
//    }

//    val fc = JFileChooser()
//    val returnVal = fc.showSaveDialog(LocalAppWindow.current.window)
//    if (returnVal === JFileChooser.APPROVE_OPTION) {
//        val file = fc.selectedFile
//        //This is where a real application would open the file.
//
//        println("Opening: " + file.absolutePath + "." + "\n")
//        val f = FileInputStream(file)
//        transaction {
//            Empleado.findById(1)?.imagen = ExposedBlob(f.readBytes())
//            commit()
//        }
//
//    } else {
//        println("Open command cancelled by user.\n")
//    }


//    transaction {
//        addLogger(StdOutSqlLogger)
//        User.all().forEach {
//            println(it.nombre)
//        }
//    }
//
//    val users = transaction {
//        addLogger(StdOutSqlLogger)
//        User.all().toList()
//    }
//
//    users.forEach {
//        println("user")
//        println(it.nombre)
//
//        var nombres = it.nombre.split(" ")
//        var password = nombres[0].decapitalize()+"1234"
//        println(password)
//        println(it.password)
//        if (Bcrypt.verify(password, it.password.toByteArray())) {
//            println("It's a match!")
//        }
//    }
//
//
//    transaction {
//        addLogger(StdOutSqlLogger)
//
//        val userid = Users.insert {
//            it[id] = 0
//            it[email] = "email"
//            val hashpassword = BCrypt.withDefaults().hashToString(12, "pepito".toCharArray())
//            println(hashpassword)
//            if (Bcrypt.verify("pepito", hashpassword.toByteArray())) {
//                println("It's a match!")
//            }
//            it[password] = hashpassword.toString()
//            it[rut] = "19.635.817-K"
//            it[nombre] = "Lukas"
//            it[direccion] = "Dirreccion"
//
//            val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
//            it[fecha_nac] = LocalDate.parse("07 03 1997", formatter)
//            it[eliminado] = 'F'
//            it[rol] = 2
//        } get Users.id
//        commit()
//        println(User.findById(userid)?.fechaNac)
//    }
}