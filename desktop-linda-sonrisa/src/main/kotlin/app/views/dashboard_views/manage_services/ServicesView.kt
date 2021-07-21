package app.views.dashboard_views.manage_services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.components.formSpacer
import app.components.tableCell
import app.data.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


enum class ServicesTypeAction {
    Add,
    Edit,
    Show
}

//  serviceTypeTable

private const val WEIGHT_SERVICE_TYPE_ID = 1f
private const val WEIGHT_SERVICE_TYPE_NAME = 4f
private const val WEIGHT_SERVICE_TYPE_DESCRIPTION = 6f
private const val WEIGHT_SERVICE_TYPE_PRICE = 2f

//  dentistServiceTypeTable
private const val WEIGHT_DENTIST_SERVICE_TYPE_ID = 1f
private const val WEIGHT_DENTIST_SERVICE_TYPE_RUT = 2f
private const val WEIGHT_DENTIST_SERVICE_TYPE_NAME = 3f
private const val WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE = 3f
private const val WEIGHT_DENTIST_SERVICE_TYPE_PHONE = 2f

@Composable
fun viewServices() {
    val selectedServiceTypeRow = remember { mutableStateOf(0) }
    val servicesTypeSortedBy = remember { mutableStateOf("id") }
    val servicesTypeData = mutableStateOf(
        transaction {
            addLogger(StdOutSqlLogger)
            Tipo_Servicios
                .selectAll()
                .map {
                    it[Tipo_Servicios.id] to listOf(
                        QueryResult.IntQueryResult("id", it[Tipo_Servicios.id].value),
                        QueryResult.StringQueryResult("name", it[Tipo_Servicios.nombre]),
                        QueryResult.StringQueryResult("description", it[Tipo_Servicios.descripcion]),
                        QueryResult.IntQueryResult("price", it[Tipo_Servicios.precio])
                    )
                }.sortedBy { rowValue ->
                    rowValue.second.find {
                        it.columnName == servicesTypeSortedBy.value
                    }.toString()
                }
        }
    )

    val selectedServiceTypeAction = remember { mutableStateOf(ServicesTypeAction.Add) }


    val minInt = 0
    val maxInt = Int.MAX_VALUE

//  New Service Type variables
    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf(0) }

//  Edit Service Type variables
    var editName by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var editPrice by remember { mutableStateOf(0) }


    if (servicesTypeData.value.isNotEmpty() && selectedServiceTypeRow.value == 0) {
        selectedServiceTypeRow.value = servicesTypeData.value.first().first.value
        servicesTypeData.value.first().second.forEach {
            when (it.columnName) {
                "name" ->
                    editName = (it as QueryResult.StringQueryResult).columnValue
                "description" ->
                    editDescription = (it as QueryResult.StringQueryResult).columnValue
                "price" ->
                    editPrice = (it as QueryResult.IntQueryResult).columnValue
            }
        }
    }

// Show dentists who have the selected Service Type

    val dentistServicesTypeSortedBy = remember { mutableStateOf("id") }
    val dentistServiceTypeData = mutableStateOf(
        transaction {
            addLogger(StdOutSqlLogger)
            Empleados.innerJoin(Users).innerJoin(Emp_Tserv).innerJoin(Tipo_Empleados)
                .select {
                    Emp_Tserv.id_tipo_servicio eq selectedServiceTypeRow.value
                }
                .map {
                    it[Empleados.id] to listOf(
                        QueryResult.IntQueryResult("id", it[Empleados.id].value),
                        QueryResult.StringQueryResult("rut", it[Users.rut]),
                        QueryResult.StringQueryResult("name", it[Users.nombre]),
                        QueryResult.StringQueryResult("employeeType", it[Tipo_Empleados.nombre]),
                        QueryResult.StringQueryResult("phone", it[Empleados.num_telefono]),
                    )
                }.sortedBy { rowValue ->
                    rowValue.second.find {
                        it.columnName == dentistServicesTypeSortedBy.value
                    }.toString()
                }
        }
    )

//  Show dentists

    val selectedDentistRow = remember { mutableStateOf(0) }
    val dentistSortedBy = remember { mutableStateOf("id") }
    val dentistData = mutableStateOf(
        transaction {
            addLogger(StdOutSqlLogger)
            Empleados.innerJoin(Users).innerJoin(Tipo_Empleados)
                .selectAll()
                .map {
                    it[Empleados.id] to listOf(
                        QueryResult.IntQueryResult("id", it[Empleados.id].value),
                        QueryResult.StringQueryResult("rut", it[Users.rut]),
                        QueryResult.StringQueryResult("name", it[Users.nombre]),
                        QueryResult.StringQueryResult("employeeType", it[Tipo_Empleados.nombre]),
                        QueryResult.StringQueryResult("email", it[Users.email]),
                        QueryResult.StringQueryResult("phone", it[Empleados.num_telefono]),
                    )
                }.sortedBy { rowValue ->
                    rowValue.second.find {
                        it.columnName == dentistSortedBy.value
                    }.toString()
                }
        }
    )

    if (dentistData.value.isNotEmpty() && selectedDentistRow.value == 0) {
        selectedDentistRow.value = dentistData.value.first().first.value
    }

//  Get Interaction of dentists with service type
    val serviceTypeDataForDentistOrderBy = remember { mutableStateOf("id") }
    val servicesTypeDataForDentist = mutableStateOf(
        transaction {
            addLogger(StdOutSqlLogger)
            Tipo_Servicios
                .selectAll()
                .map {
                    it[Tipo_Servicios.id] to listOf(
                        QueryResult.IntQueryResult("id", it[Tipo_Servicios.id].value),
                        QueryResult.StringQueryResult("name", it[Tipo_Servicios.nombre]),
                        QueryResult.StringQueryResult("description", it[Tipo_Servicios.descripcion]),
                        QueryResult.IntQueryResult("price", it[Tipo_Servicios.precio])
                    )
                }.sortedBy { rowValue ->
                    rowValue.second.find {
                        it.columnName == serviceTypeDataForDentistOrderBy.value
                    }.toString()
                }
        }
    )


    val serviceTypeInDentistList = remember { mutableStateOf( listOf<Int>() ) }
    if (selectedDentistRow.value != 0) {
        serviceTypeInDentistList.value = transaction {
            Emp_Tserv
                .select { Emp_Tserv.id_empleado eq selectedDentistRow.value }
                .map {
                    it[Emp_Tserv.id_tipo_servicio]
                } .toList()
        }
    }

//  UI

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text("Administracion de servicios", fontSize = 24.sp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(10.dp)
        ) {
            Text("Listado de tipos de servicios", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))

            if (servicesTypeData.value.isEmpty()) {
                Text("No existen tipos de servicios.")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        tableCell(
                            "ID",
                            modifier = Modifier
                                .weight(WEIGHT_SERVICE_TYPE_ID)
                                .clickable { servicesTypeSortedBy.value = "id" }
                        )
                        tableCell(
                            "Nombre",
                            modifier = Modifier
                                .weight(WEIGHT_SERVICE_TYPE_NAME)
                                .clickable { servicesTypeSortedBy.value = "name" }
                        )
                        tableCell(
                            "Descripcion",
                            modifier = Modifier
                                .weight(WEIGHT_SERVICE_TYPE_DESCRIPTION)
                                .clickable { servicesTypeSortedBy.value = "description" }
                        )
                        tableCell(
                            "Precio",
                            modifier = Modifier
                                .weight(WEIGHT_SERVICE_TYPE_PRICE)
                                .clickable { servicesTypeSortedBy.value = "price" }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(1.dp, Color.Gray)
                ) {
                    items(servicesTypeData.value) { itemData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color =
                                    if (selectedServiceTypeRow.value == itemData.first.value) {
                                        Color.Gray
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .clickable {
                                    selectedServiceTypeRow.value = itemData.first.value
                                    itemData.second.forEach {
                                        when (it) {
                                            is QueryResult.IntQueryResult ->
                                                if (it.columnName == "price") {
                                                    editPrice = it.columnValue
                                                }
                                            is QueryResult.StringQueryResult ->
                                                if (it.columnName == "name") {
                                                    editName = it.columnValue
                                                } else if (it.columnName == "description") {
                                                    editDescription = it.columnValue
                                                }
                                        }
                                    }
                                }
                        ) {
                            itemData.second.forEach {
                                tableCell(
                                    when (it) {
                                        is QueryResult.IntQueryResult ->
                                            if (it.columnName == "price") {
                                                "$ ${it.columnValue}"
                                            } else {
                                                it.columnValue.toString()
                                            }
                                        is QueryResult.StringQueryResult ->
                                            it.columnValue
                                    },
                                    modifier = Modifier
                                        .height(60.dp)
                                        .weight(
                                            when (it.columnName) {
                                                "id" ->
                                                    WEIGHT_SERVICE_TYPE_ID
                                                "name" ->
                                                    WEIGHT_SERVICE_TYPE_NAME
                                                "description" ->
                                                    WEIGHT_SERVICE_TYPE_DESCRIPTION
                                                "price" ->
                                                    WEIGHT_SERVICE_TYPE_PRICE
                                                else ->
                                                    1f
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedButton(onClick = {
                    selectedServiceTypeAction.value = ServicesTypeAction.Add
                }) {
                    Text("Agregar tipo de servicio")
                }
                if (servicesTypeData.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedButton(onClick = {
                        selectedServiceTypeAction.value = ServicesTypeAction.Edit
                        val selected = servicesTypeData.value.find {
                            it.first.value == selectedServiceTypeRow.value
                        }
                        selected?.second?.forEach {
                            when (it.columnName) {
                                "name" ->
                                    editName = (it as QueryResult.StringQueryResult).columnValue
                                "description" ->
                                    editDescription = (it as QueryResult.StringQueryResult).columnValue
                                "price" ->
                                    editPrice = (it as QueryResult.IntQueryResult).columnValue
                            }
                        }
                    }) {
                        Text("Editar tipo de servicio")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedButton(onClick = {
                        selectedServiceTypeAction.value = ServicesTypeAction.Show
                        val selected = servicesTypeData.value.find {
                            it.first.value == selectedServiceTypeRow.value
                        }
                        selected?.second?.forEach {
                            when (it.columnName) {
                                "name" ->
                                    editName = (it as QueryResult.StringQueryResult).columnValue
                                "description" ->
                                    editDescription = (it as QueryResult.StringQueryResult).columnValue
                                "price" ->
                                    editPrice = (it as QueryResult.IntQueryResult).columnValue
                            }
                        }
                    }) {
                        Text("Mostrar dentistas")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(10.dp))
            when (selectedServiceTypeAction.value) {
                ServicesTypeAction.Add -> {

                    Text("Agregar nuevo servicio.", fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newName,
                        onValueChange = {
                            newName = it
                        },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    formSpacer(modifier = Modifier.height(10.dp), newName.isEmpty(), "El nombre no puede estar vacio.")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = {
                            newDescription = it
                        },
                        label = { Text("Descripcion") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    formSpacer(modifier = Modifier.height(10.dp), newDescription.isEmpty(), "La descripcion no puede estar vacio.")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = "$ $newPrice",
                        onValueChange = {
                            newPrice = if (it.isEmpty()) {
                                minInt
                            } else {
                                val stringValue = it.filter { char -> char.isDigit() }.toLong()
                                when {
                                    stringValue < minInt -> {
                                        minInt
                                    }
                                    stringValue > maxInt -> {
                                        maxInt
                                    }
                                    else -> {
                                        stringValue
                                    }
                                }.toInt()
                            }
                        },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = {
                            if (newName.isNotEmpty() && newDescription.isNotEmpty()) {
                                serviceTypeInsert(newName, newDescription, newPrice)
                                servicesTypeData.value =
                                    transaction {
                                        addLogger(StdOutSqlLogger)
                                        Tipo_Servicios
                                            .selectAll()
                                            .map {
                                                it[Tipo_Servicios.id] to listOf(
                                                    QueryResult.IntQueryResult("id", it[Tipo_Servicios.id].value),
                                                    QueryResult.StringQueryResult("name", it[Tipo_Servicios.nombre]),
                                                    QueryResult.StringQueryResult("description", it[Tipo_Servicios.descripcion]),
                                                    QueryResult.IntQueryResult("price", it[Tipo_Servicios.precio])
                                                )
                                            }.sortedBy { rowValue ->
                                                rowValue.second.find {
                                                    it.columnName == servicesTypeSortedBy.value
                                                }.toString()
                                            }
                                    }
                                newName = ""
                                newDescription = ""
                                newPrice = 0
                            }
                        }) {
                            Text("Registrar")
                        }
                    }
                }
                ServicesTypeAction.Edit -> {

                    Text("Editar servicio.", fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = {
                            editName = it
                        },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    formSpacer(modifier = Modifier.height(10.dp), editName.isEmpty(), "El nombre no puede estar vacio.")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = {
                            editDescription = it
                        },
                        label = { Text("Descripcion") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    formSpacer(modifier = Modifier.height(10.dp), editDescription.isEmpty(), "La descripcion no puede estar vacio.")

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = "$ $editPrice",
                        onValueChange = {
                            editPrice = if (it.isEmpty()) {
                                minInt
                            } else {
                                val stringValue = it.filter { char -> char.isDigit() }.toLong()
                                when {
                                    stringValue < minInt -> {
                                        minInt
                                    }
                                    stringValue > maxInt -> {
                                        maxInt
                                    }
                                    else -> {
                                        stringValue
                                    }
                                }.toInt()
                            }
                        },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = {
                            if (editName.isNotEmpty() && editDescription.isNotEmpty()) {
                                serviceTypeEdit(selectedServiceTypeRow.value, editName, editDescription, editPrice)
                                servicesTypeData.value =
                                    transaction {
                                        addLogger(StdOutSqlLogger)
                                        Tipo_Servicios
                                            .selectAll()
                                            .map {
                                                it[Tipo_Servicios.id] to listOf(
                                                    QueryResult.IntQueryResult("id", it[Tipo_Servicios.id].value),
                                                    QueryResult.StringQueryResult("name", it[Tipo_Servicios.nombre]),
                                                    QueryResult.StringQueryResult("description", it[Tipo_Servicios.descripcion]),
                                                    QueryResult.IntQueryResult("price", it[Tipo_Servicios.precio])
                                                )
                                            }.sortedBy { rowValue ->
                                                rowValue.second.find {
                                                    it.columnName == servicesTypeSortedBy.value
                                                }.toString()
                                            }
                                    }
                            }
                        }) {
                            Text("Editar")
                        }
                    }
                }
                ServicesTypeAction.Show -> {
                    Text("Listado de dentistas que entregan el servicio $editName.", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (servicesTypeData.value.isEmpty() || dentistServiceTypeData.value.isEmpty()) {
                        Text("No existen dentistas que entregen este tipo de servicio.")
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                tableCell(
                                    "ID",
                                    modifier = Modifier
                                        .weight(WEIGHT_DENTIST_SERVICE_TYPE_ID)
                                        .clickable { dentistServicesTypeSortedBy.value = "id" }
                                )
                                tableCell(
                                    "Rut",
                                    modifier = Modifier
                                        .weight(WEIGHT_DENTIST_SERVICE_TYPE_RUT)
                                        .clickable { dentistServicesTypeSortedBy.value = "rut" }
                                )
                                tableCell(
                                    "Nombre",
                                    modifier = Modifier
                                        .weight(WEIGHT_DENTIST_SERVICE_TYPE_NAME)
                                        .clickable { dentistServicesTypeSortedBy.value = "name" }
                                )
                                tableCell(
                                    "Tipo de Empleado",
                                    modifier = Modifier
                                        .weight(WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE)
                                        .clickable { dentistServicesTypeSortedBy.value = "employeeType" }
                                )
                                tableCell(
                                    "Telefono",
                                    modifier = Modifier
                                        .weight(WEIGHT_DENTIST_SERVICE_TYPE_PHONE)
                                        .clickable { dentistServicesTypeSortedBy.value = "phone" }
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            items(dentistServiceTypeData.value) { itemData ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent)
                                ) {
                                    itemData.second.forEach {
                                        tableCell(
                                            when (it) {
                                                is QueryResult.IntQueryResult ->
                                                    it.columnValue.toString()
                                                is QueryResult.StringQueryResult ->
                                                    it.columnValue
                                            },
                                            modifier = Modifier
                                                .weight(
                                                    when (it.columnName) {
                                                        "id" ->
                                                            WEIGHT_DENTIST_SERVICE_TYPE_ID
                                                        "rut" ->
                                                            WEIGHT_DENTIST_SERVICE_TYPE_RUT
                                                        "name" ->
                                                            WEIGHT_DENTIST_SERVICE_TYPE_NAME
                                                        "employeeType" ->
                                                            WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE
                                                        "phone" ->
                                                            WEIGHT_DENTIST_SERVICE_TYPE_PHONE
                                                        else ->
                                                            1f
                                                    }
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.Gray
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(10.dp)
        ) {
            Text("Listado de dentistas.", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            if (dentistData.value.isEmpty()) {
                Text("No existen dentistas en la base de datos.")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        tableCell(
                            "ID",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_ID)
                                .clickable { dentistSortedBy.value = "id" }
                        )
                        tableCell(
                            "Rut",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_RUT)
                                .clickable { dentistSortedBy.value = "rut" }
                        )
                        tableCell(
                            "Nombre",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_NAME)
                                .clickable { dentistSortedBy.value = "name" }
                        )
                        tableCell(
                            "Tipo de empleado",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE)
                                .clickable { dentistSortedBy.value = "employeeType" }
                        )
                        tableCell(
                            "Email",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE)
                                .clickable { dentistSortedBy.value = "email" }
                        )
                        tableCell(
                            "Telefono",
                            modifier = Modifier
                                .weight(WEIGHT_DENTIST_SERVICE_TYPE_PHONE)
                                .clickable { dentistSortedBy.value = "phone" }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    items(dentistData.value) { itemData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color =
                                    if (selectedDentistRow.value == itemData.first.value) {
                                        Color.Gray
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .clickable {
                                    selectedDentistRow.value = itemData.first.value
                                    itemData.second.forEach {
                                        when (it) {
                                            is QueryResult.IntQueryResult ->
                                                if (it.columnName == "price") {
                                                    editPrice = it.columnValue
                                                }
                                            is QueryResult.StringQueryResult ->
                                                if (it.columnName == "name") {
                                                    editName = it.columnValue
                                                } else if (it.columnName == "description") {
                                                    editDescription = it.columnValue
                                                }
                                        }
                                    }
                                }
                        ) {
                            itemData.second.forEach {
                                tableCell(
                                    when (it) {
                                        is QueryResult.IntQueryResult ->
                                            it.columnValue.toString()
                                        is QueryResult.StringQueryResult ->
                                            it.columnValue
                                    },
                                    modifier = Modifier
                                        .weight(
                                            when (it.columnName) {
                                                "id" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_ID
                                                "rut" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_RUT
                                                "name" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_NAME
                                                "employeeType" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE
                                                "email" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_EMPLOYEE_TYPE
                                                "phone" ->
                                                    WEIGHT_DENTIST_SERVICE_TYPE_PHONE
                                                else ->
                                                    1f
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
                if (selectedDentistRow.value != 0) {
                    Spacer( modifier = Modifier.height(10.dp) )
                    if (servicesTypeDataForDentist.value.isEmpty()) {
                        Text("No existen tipos de servicios.")
                    } else {
                        Row (modifier = Modifier.fillMaxWidth()){
                            tableCell(
                                "ID",
                                modifier = Modifier
                                    .weight(WEIGHT_SERVICE_TYPE_ID)
                                    .clickable { serviceTypeDataForDentistOrderBy.value = "id" }
                            )
                            tableCell(
                                "Nombre",
                                modifier = Modifier
                                    .weight(WEIGHT_SERVICE_TYPE_NAME)
                                    .clickable { serviceTypeDataForDentistOrderBy.value = "name" }
                            )
                            tableCell(
                                "Descripcion",
                                modifier = Modifier
                                    .weight(WEIGHT_SERVICE_TYPE_DESCRIPTION)
                                    .clickable { serviceTypeDataForDentistOrderBy.value = "description" }
                            )
                            tableCell(
                                "Precio",
                                modifier = Modifier
                                    .weight(WEIGHT_SERVICE_TYPE_PRICE)
                                    .clickable { serviceTypeDataForDentistOrderBy.value = "price" }
                            )
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            items(servicesTypeDataForDentist.value) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color =
                                            if (it.first.value in serviceTypeInDentistList.value) {
                                                Color.Gray
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                        .clickable {
                                            val value = it.first.value
                                            if (value in serviceTypeInDentistList.value) {
                                                serviceTypeInDentistList.value = serviceTypeInDentistList.value.filterNot { it == value }
                                            } else {
                                                serviceTypeInDentistList.value = serviceTypeInDentistList.value + value
                                            }
                                            println(serviceTypeInDentistList.value.toString())
                                        }
                                ) {
                                    it.second.forEach {
                                        tableCell(
                                            when (it) {
                                                is QueryResult.IntQueryResult ->
                                                    it.columnValue.toString()
                                                is QueryResult.StringQueryResult ->
                                                    it.columnValue
                                            },
                                            modifier = Modifier.weight(
                                                when (it.columnName) {
                                                    "id" ->
                                                        WEIGHT_SERVICE_TYPE_ID
                                                    "name" ->
                                                        WEIGHT_SERVICE_TYPE_NAME
                                                    "description" ->
                                                        WEIGHT_SERVICE_TYPE_DESCRIPTION
                                                    "price" ->
                                                        WEIGHT_SERVICE_TYPE_PRICE
                                                    else ->
                                                        1f
                                                }
                                            )
                                            .height(60.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(onClick = {
                            employeeUpdateServiceType(
                                selectedDentistRow.value,
                                serviceTypeInDentistList.value
                            )
                        }) {
                            Text("Actualizar Servicios")
                        }
                    }
                }
            }
        }
    }
}
