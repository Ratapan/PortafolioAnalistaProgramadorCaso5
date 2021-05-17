package app.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.time.Instant
import java.time.LocalDate

object Administradores : IntIdTable("Administradores", "ID_ADM") {
    val num_telefono: Column<String> = varchar("NUM_TELEFONO_A", 20)
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "administrador_PK_id_adm")
}

object Boletas : IntIdTable("Boletas", "ID_BOLETA") {
    val id_servicio: Column<Int> = integer("SERVICIOS_ID_SERVICIO").references(Servicios.id)
    override val primaryKey = PrimaryKey(id, name = "Boletas_PK_id_boleta")
}


object Citas : IntIdTable("Citas", "ID_CITA") {
    val estado: Column<Char> = char("ESTADO")
    val fecha_solicitacion: Column<LocalDate> = date("FECHA_SOLICITACION")
    val id_paciente: Column<Int> = integer("PACIENTES_ID_PACIENTE").references(Pacientes.id)
    val id_hora: Column<Int> = integer("HORAS_ID_HORA").references(Horas.id)
    override val primaryKey = PrimaryKey(id, name = "Citas_PK_id_cita")
}


object Detalle_Ordenes : IntIdTable("Detalle_Ordenes", "ID_PRODUCTO_ORDEN") {
    val cant_productos: Column<Int> = integer("CANT_PRODUCTOS")
    val precio_productos: Column<Int> = integer("PRECIO_PRODUCTOS")
    val id_productos: Column<Int> = integer("PRODUCTOS_ID_TIPOP").references(Productos.id)
    val id_orden: Column<Int> = integer("ORDENES_ID_ORDEN").references(Ordenes.id)
    override val primaryKey = PrimaryKey(id, name = "Detalle_Ordenes_PK_id_producto_orden")
}

object Emp_Tserv : IntIdTable("Emp_Tserv", "ID_EMP_TSER") {
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    val id_tipo_servicio: Column<Int> = integer("TIPO_SERVICIOS_ID_T_SERV").references(Tipo_Servicios.id)
    override val primaryKey = PrimaryKey(id, name = "Emp_Tserv_PK_id_emp_tserv")
}

object Empleados : IntIdTable("Empleados", "ID_EMPLEADO") {
    val num_telefono: Column<String> = varchar("NUM_TELEFONO_E", 20)
    val salud_empleado: Column<Char> = char("SALUD_E")
    val salario: Column<Long> = long("SALARIO")
    val inicio_contrato: Column<LocalDate> = date("INICIO_CONTRATO")
    val fin_contrato: Column<LocalDate> = date("FIN_CONTRATO")
    val afp: Column<String> = varchar("AFP", 30)
    val nombre_banco: Column<String> = varchar("NOMBRE_BANCO", 30)
    val numero_cuenta: Column<Long> = long("NUMERO_CUENTA")
    val imagen: Column<ExposedBlob> = blob("IMAGEN")
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    val id_tipo_emp: Column<Int> = integer("TIPO_EMPLEADOS_ID_TIPO_EMP").references(Tipo_Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Empleados_PK_id_empleado")
}


object Familia_Productos : IntIdTable("Familia_Productos", "ID_T_FAM") {
    val nombre: Column<String> = varchar("NOMBRE_TIPO_FAMILIA", 30)
    override val primaryKey = PrimaryKey(id, name = "Familia_Productos_PK_ID_T_FAM")
}


object Horas : IntIdTable("Horas", "ID_HORA") {
    val hora_inicio: Column<Instant> = timestamp("INICIO_HORA")
    val hora_fin: Column<Instant> = timestamp("FIN_HORA")
    val estado: Column<Char> = char("ESTADO")
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Horas_PK_ID_HORA")
}


object Ordenes : IntIdTable("Ordenes", "ID_ORDEN") {
    val cancelada: Column<Char> = char("CANCELADA_O")
    val fecha_venc: Column<LocalDate> = date("FECHA_VENC_O")
    val precio_total: Column<Int> = integer("PRECIO_TOTAL")
    val id_proveedor: Column<Int> = integer("PROVEEDORES_ID_PROVEEDOR").references(Proveedores.id)
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Ordenes_PK_ID_ORDEN")
}

object Pacientes : IntIdTable("Pacientes", "ID_PACIENTE") {
    val num_telefono: Column<String> = varchar("NUM_TELEFONO_PA", 20)
    val salud_paciente: Column<Char> = char("SALUD_PA")
    val documento: Column<ExposedBlob> = blob("DOCUMENTO")
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "Pacientes_PK_ID_PACIENTE")
}

object Productos : IntIdTable("Productos", "ID_TIPOP") {
    val nombre: Column<String> = varchar("NOMBRE_TIPOP", 200)
    val descripcion: Column<String> = varchar("DESC_TIPOP", 200)
    val precio_c: Column<Int> = integer("PRECIO_C_TIPOP")
    val precio_v: Column<Int> = integer("PRECIO_V_TIPOP")
    val stock: Column<Int> = integer("STOCK_TIPOP")
    val stock_critico: Column<Int> = integer("STOCK_C_TIPOP")
    val id_familia_producto: Column<Int> = integer("FAMILIA_PRODUCTOS_ID_T_FAM").references(Familia_Productos.id)
    override val primaryKey = PrimaryKey(id, name = "Productos_PK_ID_TIPOP")
}

object Proveedores : IntIdTable("Proveedores", "ID_PROVEEDOR") {
    val num_telefono_empresa: Column<String> = varchar("TELEFONO_EMPRESA_P", 20)
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "Proveedores_PK_ID_PROVEEDOR")
}

object Recepciones : IntIdTable("Recepciones", "ID_RECEPCION") {
    val codigo: Column<String> = varchar("CODIGO", 17)
    val comentario: Column<String> = varchar("COMENTARIO", 200)
    val estado: Column<Char> = char("ESTADO")
    val id_orden: Column<Int> = integer("ORDENES_ID_ORDEN").references(Ordenes.id)
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Recepciones_PK_ID_RECEPCION")
}

object Roles : IntIdTable("Roles", "ID_ROL") {
    val nombre: Column<String> = varchar("NOMBRE_ROL", 20)
    val descripcion: Column<String> = varchar("DESCRIPCION_ROL", 50)
    override val primaryKey = PrimaryKey(id, name = "Roles_PK_ID_ROL")
}

object Servicios : IntIdTable("Servicios", "ID_SERVICIO") {
    val id_tipo_servicio: Column<Int> = integer("TIPO_SERVICIOS_ID_T_SERV").references(Tipo_Servicios.id)
    val id_cita: Column<Int> = integer("CITAS_ID_CITA").references(Citas.id)
    override val primaryKey = PrimaryKey(id, name = "Servicios_PK_ID_SERVICIO")
}

object Tipo_Empleados : IntIdTable("Tipo_Empleados", "ID_TIPO_EMP") {
    val nombre: Column<String> = varchar("NOMBRE_TIPO_EMP", 50)
    override val primaryKey = PrimaryKey(id, name = "Tipo_Empleados_PK_ID_TIPO_EMP")
}

object Tipo_Servicios : IntIdTable("Tipo_Servicios", "ID_T_SERV") {
    val nombre: Column<String> = varchar("NOMBRE_SERVICIO", 30)
    val precio: Column<Int> = integer("PRECIO")
    val descripcion: Column<String> = varchar("DESCRIPCION_SERVICIO", 100)
    override val primaryKey = PrimaryKey(id, name = "Tipo_Servicios_PK_ID_T_SERV")
}

object Users : IntIdTable("Users", "ID_USER") {
    val email: Column<String> = varchar("EMAIL", 100)
    val password: Column<String> = varchar("PASSWORD", 100)
    val rut: Column<String> = varchar("RUT", 12)
    val nombre: Column<String> = varchar("NOMBRE_APE", 50)
    val direccion: Column<String> = varchar("DIRECCION", 50)
    val fecha_nac: Column<LocalDate> = date("FECHA_NAC")
    val eliminado: Column<Char> = char("ELIMINADO")
    val rol: Column<Int> = integer("ROLES_ID_ROL").references(Roles.id)
    override val primaryKey = PrimaryKey(id, name = "Users_PK_ID_USER")
}
