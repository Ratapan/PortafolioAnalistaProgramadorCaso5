package app.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.time.Instant
import java.time.LocalDate

object Rol : Table() {
    val id: Column<String> = varchar("id_rol", 100)
    val nombre: Column<String> = varchar("sequel_id", 20)
    val descripcion: Column<String> = varchar("descripcion_rol", 50)
    val director: Column<String> = varchar("director", 50)
    override val primaryKey = PrimaryKey(id, name = "Rol_PK_id_rol")
}

object User : Table() {
    val id: Column<String> = varchar("id_user", 100)
    val email: Column<String> = varchar("email_user", 20)
    val contraseña: Column<Instant> = timestamp("contraseña")
    val password: Column<String> = varchar("password", 50)
    val eliminado: Column<Char> = char("eliminado")
    val rol: Column<String> = varchar("Rol_id_rol", 100).references(Rol.id)
    override val primaryKey = PrimaryKey(id, name = "user_PK_id_user")
}

object Administrador : Table() {
    val id: Column<String> = varchar("id_adm", 100)
    val rut: Column<String> = varchar("rut_a", 12)
    val nombre: Column<String> = varchar("nombre_a", 20)
    val correo: Column<String> = varchar("correo_a", 40)
    val direccion: Column<String> = varchar("direccion_a", 30)
    val numero_fono: Column<Int> = integer("num_telefono_a")
    val fecha_nac: Column<LocalDate> = date("fecha_nac_a")
    val user: Column<String> = varchar("user_id_user", 100).references(User.id)
    override val primaryKey = PrimaryKey(id, name = "administrador_PK_id_adm")
}