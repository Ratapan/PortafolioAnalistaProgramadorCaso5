package app.data

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.time.LocalDate

object Administradores : IdTable<String>("administradores") {
    val myIdColumn = varchar("id_adm", 100).uniqueIndex()
    override val id: Column<EntityID<String>> = myIdColumn.entityId()

    val num_telefono: Column<Int> = integer("num_telefono_a")
    val id_user: Column<Int> = integer("users_id_user").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "administrador_PK_id_adm")
}

object Boletas : IntIdTable("Boletas", "id_boleta") {
    val id_servicio: Column<Int> = integer("servicios_id_servicio").references(Servicios.id)
    override val primaryKey = PrimaryKey(id, name = "Boletas_PK_id_boleta")
}


object Citas : IntIdTable("Citas", "id_cita") {
    val estado: Column<Char> = char("estado")
    val fecha_solicitacion: Column<LocalDate> = date("fecha_solicitacion")
    val id_paciente: Column<Int> = integer("pacientes_id_paciente").references(Pacientes.id)
    val id_hora: Column<Int> = integer("horas_id_hora").references(Horas.id)
    override val primaryKey = PrimaryKey(id, name = "Citas_PK_id_cita")
}


object Detalle_Ordenes : IntIdTable("Detalle_Ordenes", "id_producto_orden") {
    val cant_productos: Column<Int> = integer("cant_productos")
    val precio_productos: Column<Int> = integer("precio_productos")
    val id_productos: Column<Int> = integer("productos_id_producto").references(Productos.id)
    val id_orden: Column<Int> = integer("ordenes_id_orden").references(Ordenes.id)
    override val primaryKey = PrimaryKey(id, name = "Detalle_Ordenes_PK_id_producto_orden")
}

object Emp_Tserv : IntIdTable("Emp_Tserv", "id_emp_tserv") {
    val id_empleado: Column<Int> = integer("empleados_id_empleado").references(Empleados.id)
    val id_tipo_servicio: Column<Int> = integer("tipo_servicios_id_t_serv").references(Tipo_Servicios.id)
    override val primaryKey = PrimaryKey(id, name = "Emp_Tserv_PK_id_emp_tserv")
}

object Empleados : IntIdTable("Empleados", "ID_EMPLEADO") {
    val num_telefono: Column<Int> = integer("NUM_TELEFONO_E1")
    val salud_empleado: Column<Char> = char("SALUD_E")
    val salario: Column<Int> = integer("SALARIO")
    val inicio_contrato: Column<LocalDate> = date("INICIO_CONTRATO")
    val fin_contrato: Column<LocalDate> = date("FIN_CONTRATO")
    val afp: Column<String> = varchar("AFP", 30)
    val nombre_banco: Column<String> = varchar("NOMBRE_BANCO", 30)
    val numero_banco: Column<Int> = integer("NUMERO_BANCO")
    val tipo_cuenta: Column<String> = varchar("TIPO_CUENTA", 30)
    val id_sucursal: Column<Int> = integer("SUCURSALES_ID_SUCURSAL").references(Sucursales.id)
    val imagen: Column<ExposedBlob> = blob("IMAGEN")
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "Empleados_PK_id_empleado")
}


object Familia_Productos : IntIdTable("Familia_Productos", "ID_T_FAM") {
    val nombre: Column<String> = varchar("NOMBRE_TIPO_FAMILIA", 30)
    override val primaryKey = PrimaryKey(id, name = "Familia_Productos_PK_ID_T_FAM")
}


object Horas : IntIdTable("Horas", "ID_HORA") {
    val hora_inicio: Column<LocalDate> = date("INICIO_HORA")
    val hora_fin: Column<LocalDate> = date("FIN_HORA")
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Horas_PK_ID_HORA")
}


object Ordenes : IntIdTable("Ordenes", "ID_ORDEN") {
    val tipo_producto: Column<String> = varchar("TIPO_PRODUCTO_O", 3)
    val cancelada: Column<Char> = char("CANCELADA_O")
    val fecha_venc: Column<LocalDate> = date("FECHA_VENC_O")
    val precio_total: Column<Int> = integer("PRECIO_TOTAL")
    val id_proveedor: Column<Int> = integer("PROVEEDORES_ID_PROVEEDOR").references(Proveedores.id)
    val id_empleado: Column<Int> = integer("EMPLEADOS_ID_EMPLEADO").references(Empleados.id)
    override val primaryKey = PrimaryKey(id, name = "Ordenes_PK_ID_ORDEN")
}

object Pacientes : IntIdTable("Pacientes", "ID_PACIENTE") {
    val num_telefono: Column<Int> = integer("NUM_TELEFONO_PA")
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
    val stock: Column<String> = varchar("STOCK_TIPOP", 200)
    val stock_critico: Column<String> = varchar("STOCK_C_TIPOP", 200)
    val id_familia_producto: Column<Int> = integer("FAMILIA_PRODUCTOS_ID_T_FAM").references(Familia_Productos.id)
    override val primaryKey = PrimaryKey(id, name = "Productos_PK_ID_TIPOP")
}

object Proveedores : IntIdTable("Proveedores", "ID_PROVEEDOR") {
    val num_telefono_empresa: Column<Int> = integer("TELEFONO_EMPRESA_P")
    val id_user: Column<Int> = integer("USERS_ID_USER").references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "Proveedores_PK_ID_PROVEEDOR")
}

object Recepciones : IntIdTable("Recepciones", "ID_RECEPCION") {
    val codigo: Column<String> = varchar("CODIGO", 17)
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

object Sucursales : IntIdTable("Sucursales", "ID_SUCURSAL") {
    val nombre: Column<String> = varchar("NOMBRE_SUCURSAL", 30)
    val direccion: Column<String> = varchar("DIRECCION", 30)
    override val primaryKey = PrimaryKey(id, name = "Sucursales_PK_ID_SUCURSAL")
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
