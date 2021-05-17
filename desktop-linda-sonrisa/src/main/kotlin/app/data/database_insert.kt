package app.data

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.time.LocalDate

private val blankProfilePicture = {}.javaClass.classLoader.getResource("images/blank-profile-picture.png")!!

fun insertEmpleado(
    //User Data
    email: String,
    password: String,
    rut: String,
    nombre: String,
    direccion: String,
    fechaNac: LocalDate,
    eliminado: Char,
    rol: Int,
    //EmpleadoData
    phoneNumber: String,
    saludEmpleado: Char,
    salarioValue: Long,
    inicioContrato: LocalDate,
    finContrato: LocalDate,
    AFP: String,
    nombreBanco: String,
    numeroCuenta: Long,
    id_tipo_empleado: Int,
    imagenFile: ByteArray
):Boolean {
    val userId = insertUser(email, password, rut, nombre, direccion, fechaNac, eliminado, rol)
    transaction {
        addLogger(StdOutSqlLogger)
        Empleados.insert {
            it[id] = 0
            val phoneNumberInt = phoneNumber
                .replace("\\s".toRegex(), "")
            it[num_telefono] = phoneNumberInt
            it[salud_empleado] = saludEmpleado
            it[salario] = salarioValue
            it[inicio_contrato] = inicioContrato
            it[fin_contrato] = finContrato
            it[afp] = AFP
            it[nombre_banco] = nombreBanco
            it[numero_cuenta] = numeroCuenta
            it[id_tipo_emp] = id_tipo_empleado
            if (!imagenFile.contentEquals(File(blankProfilePicture.toURI()).readBytes())) {
                it[imagen] = ExposedBlob(imagenFile)
            }
            it[id_user] = userId
        }
    }
    return true
}

fun insertPaciente(
    //User Data
    email: String,
    password: String,
    rut: String,
    nombre: String,
    direccion: String,
    fechaNac: LocalDate,
    eliminado: Char,
    rol: Int,
    //EmpleadoData
    phoneNumber: String,
    saludEmpleado: Char,
    documentoFile: ByteArray
):Boolean {
    val userId = insertUser(email, password, rut, nombre, direccion, fechaNac, eliminado, rol)
    transaction {
        addLogger(StdOutSqlLogger)
        Pacientes.insert {
            it[id] = 0
            val phoneNumberInt = phoneNumber
                .replace("\\s".toRegex(), "")
            it[num_telefono] = phoneNumberInt
            it[salud_paciente] = saludEmpleado
            if (!documentoFile.contentEquals(File(blankProfilePicture.toURI()).readBytes())) {
                it[documento] = ExposedBlob(documentoFile)
            }
            it[id_user] = userId
        }
    }
    return true
}


fun insertProveedor(
    // User Data
    email: String,
    password: String,
    rut: String,
    nombre: String,
    direccion: String,
    fechaNac: LocalDate,
    eliminado: Char,
    rol: Int,
    // Admin Data
    phoneNumber: String
):Boolean {
    val userId = insertUser(email, password, rut, nombre, direccion, fechaNac, eliminado, rol)
    transaction {
        addLogger(StdOutSqlLogger)
        Proveedores.insert {
            it[id] = 0
            val phoneNumber = phoneNumber
                .replace("[+]".toRegex(), "")
            it[num_telefono_empresa] = phoneNumber
            it[id_user] = userId
        }
    }
    return true
}

fun insertAdmin(
    // User Data
    email: String,
    password: String,
    rut: String,
    nombre: String,
    direccion: String,
    fechaNac: LocalDate,
    eliminado: Char,
    rol: Int,
    // Admin Data
    phoneNumber: String
):Boolean {
    val userId = insertUser(email, password, rut, nombre, direccion, fechaNac, eliminado, rol)
    transaction {
        addLogger(StdOutSqlLogger)
        Administradores.insert {
            it[id] = 0
            val phoneNumber = phoneNumber
                .replace("\\s".toRegex(), "")
            it[num_telefono] = phoneNumber
            it[id_user] = userId
        }
    }
    return true
}

fun insertUser(
    emailValue: String,
    passwordValue: String,
    rutValue: String,
    nombreValue: String,
    direccionValue: String,
    fechaNacValue: LocalDate,
    eliminadoValue: Char,
    rolValue: Int
): Int {
    val id = transaction {
        addLogger(StdOutSqlLogger)
        Users.insert {
            it[id] = 0
            it[email] = emailValue
            val hashpassword = BCrypt.withDefaults().hashToString(12, passwordValue.toCharArray())
            it[password] = hashpassword
            it[rut] = rutValue
            it[nombre] = nombreValue
            it[direccion] = direccionValue
            it[fecha_nac] = fechaNacValue
            it[eliminado] = eliminadoValue
            it[rol] = rolValue
        } get Users.id
    }

    return id.value

}