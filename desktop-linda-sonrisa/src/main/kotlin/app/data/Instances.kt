package app.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Administrador(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Administrador>(Administradores)

    var id_user by Administradores.id_user
    var user by User referencedOn Administradores.id_user //this is the actual object, don't call outside of a transaction or BOOM
}

class Boleta(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Boleta>(Boletas)

    var id_servicio by Boletas.id_servicio
    var servicio by Servicio referencedOn Boletas.id_servicio
}

class Cita(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Cita>(Citas)
    var estado by Citas.estado
    var fecha_solicitacion by Citas.fecha_solicitacion

    var id_paciente by Citas.id_paciente
    var paciente by Paciente referencedOn Citas.id_paciente

    var id_hora by Citas.id_hora
    var hora by Hora referencedOn Citas.id_hora
}

class Detalle_Orden(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Detalle_Orden>(Detalle_Ordenes)
    var cant_productos by Detalle_Ordenes.cant_productos
    var precio_productos by Detalle_Ordenes.precio_productos

    var id_productos by Detalle_Ordenes.id_productos
    var productos by Producto referencedOn Detalle_Ordenes.id_productos

    var id_orden by Detalle_Ordenes.id_orden
    var orden by Orden referencedOn Detalle_Ordenes.id_orden
}


class Empleado_Tipo_Servicio(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Empleado_Tipo_Servicio>(Emp_Tserv)

    var id_empleado by Emp_Tserv.id_empleado
    var empleado by Empleado referencedOn Emp_Tserv.id_empleado

    var id_tipo_servicio by Emp_Tserv.id_tipo_servicio
    var tipo_servicio by Empleado_Tipo_Servicio referencedOn Emp_Tserv.id_tipo_servicio
}

class Empleado(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Empleado>(Empleados)
    var num_telefono by Empleados.num_telefono
    var salud_empleado by Empleados.salud_empleado
    var salario by Empleados.salario
    var inicio_contrato by Empleados.inicio_contrato
    var fin_contrato by Empleados.fin_contrato
    var afp by Empleados.afp
    var nombre_banco by Empleados.nombre_banco
    var numero_cuenta by Empleados.numero_cuenta
    var imagen by Empleados.imagen

    var id_tipo_emp by Empleados.id_tipo_emp
    var tipo_empleado by Tipo_Empleado referencedOn Empleados.id_tipo_emp

    var id_user by Empleados.id_user
    var user by User referencedOn Empleados.id_user
}

class Familia_Producto(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Familia_Producto>(Familia_Productos)
    var nombre by Familia_Productos.nombre
}

class Hora(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Hora>(Horas)
    var hora_inicio by Horas.hora_inicio
    var hora_fin by Horas.hora_fin
    var estado by Horas.estado

    var id_empleado by Horas.id_empleado
    var empleado by Empleado referencedOn Horas.id_empleado
}

class Orden(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Orden>(Ordenes)
    var cancelada by Ordenes.estado
    var fecha_venc by Ordenes.fecha_venc
    var precio_total by Ordenes.precio_total

    var id_proveedor by Ordenes.id_proveedor
    var proveedor by Proveedor referencedOn Ordenes.id_proveedor

    var idEmpleado by Ordenes.id_empleado
    var empleado by Empleado referencedOn Ordenes.id_empleado

}

class Paciente(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Paciente>(Pacientes)
    var numTelefono by Pacientes.num_telefono
    var saludPaciente by Pacientes.salud_paciente
    var documento by Pacientes.documento

    var idUser by Pacientes.id_user
    var user by User referencedOn Pacientes.id_user
}


class Producto(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Producto>(Productos)
    var nombre by Productos.nombre
    var descripcion by Productos.descripcion
    var precioC by Productos.precio_c
    var precioV by Productos.precio_v
    var stock by Productos.stock
    var stockCritico by Productos.stock_critico

    var idFamiliaProducto by Productos.id_familia_producto
    var familiaProducto by Familia_Producto referencedOn Productos.id_familia_producto
}


class ProvProducto(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProvProducto>(ProvProductos)

    var idProveedor by ProvProductos.id_proveedor
    var proveedor by Proveedor referencedOn ProvProductos.id_proveedor

    var idProducto by ProvProductos.id_producto
    var producto by Producto referencedOn ProvProductos.id_producto
}

class Proveedor(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Proveedor>(Proveedores)
    var num_telefono_empresa by Proveedores.num_telefono_empresa

    var idUser by Proveedores.id_user
    var user by User referencedOn Proveedores.id_user
}

class Recepcion(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Recepcion>(Recepciones)
    var codigo by Recepciones.codigo
    var estado by Recepciones.estado
    var comentario by Recepciones.comentario

    var idOrden by Recepciones.id_orden
    var orden by Orden referencedOn Recepciones.id_orden

    var idEmpleado by Recepciones.id_empleado
    var empleado by Empleado referencedOn Recepciones.id_empleado
}

class Rol(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Rol>(Roles)
    var nombre by Roles.nombre
    var descripcion by Roles.descripcion
}

class Servicio(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Servicio>(Servicios)

    var idTipoServicio by Servicios.id_tipo_servicio
    var tipoServicio by Tipo_Servicio referencedOn Servicios.id_tipo_servicio

    var id_cita by Servicios.id_cita
    var cita by Cita referencedOn Servicios.id_cita
}

class Tipo_Empleado(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Tipo_Empleado>(Tipo_Empleados)
    var nombre by Tipo_Empleados.nombre
}

class Tipo_Servicio(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Tipo_Servicio>(Tipo_Servicios)
    var nombre by Tipo_Servicios.nombre
    var precio by Tipo_Servicios.precio
    var descripcion by Tipo_Servicios.descripcion
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var email by Users.email
    var password by Users.password
    var rut by Users.rut
    var nombre by Users.nombre
    var direccion by Users.direccion
    var fechaNac by Users.fecha_nac
    var eliminado by Users.eliminado

    var id_rol by Users.rol
    var rol by Rol referencedOn Users.rol
}

