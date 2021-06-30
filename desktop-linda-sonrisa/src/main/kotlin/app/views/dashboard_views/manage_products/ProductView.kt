package app.views.dashboard_views.manage_products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.components.dropdownSelectById
import app.components.formSpacer
import app.components.helpButton
import app.components.tableCell
import app.data.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

enum class CategoryAction {
    Add,
    Edit,
    Show
}

enum class ProductAction {
    Add,
    Edit,
    Show
}

private const val WEIGHT_ID = 1f
private const val WEIGHT_NAME = 3f
private const val WEIGHT_AMOUNT_PRODUCTS = 2f
private const val WEIGHT_AMOUNT_CRITIC = 2f


private const val WEIGHT_PRODUCT_ID = 1f
private const val WEIGHT_PRODUCT_NAME = 2f
private const val WEIGHT_PRODUCT_DESCRIPTION = 3f
private const val WEIGHT_PRODUCT_PRECIOCOMPRA = 1f
private const val WEIGHT_PRODUCT_PRECIOVENTA = 1f
private const val WEIGHT_PRODUCT_STOCK = 1f
private const val WEIGHT_PRODUCT_STOCKCRITICO = 1f



private const val WEIGHT_PROVIDER_ID = 1f
private const val WEIGHT_PROVIDER_NAME = 3f
private const val WEIGHT_PROVIDER_EMAIL = 3f
private const val WEIGHT_PROVIDER_NUMBER = 3f

@Composable
fun viewProducts() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Administracion de Productos", fontSize = 24.sp)
        helpButton {
            Surface(
                modifier = Modifier
                    .background(Color.Transparent)
                    .border(width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(5.dp)
                )
            ) {
                Column (
                    modifier = Modifier
                        .padding(5.dp)
                        .width(200.dp)
                ) {
                    Text("Administracion de productos", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Esta seccion es utilizada para agregar, editar y mostrar tanto categoria de productos como productos dentro de estas. A su vez se puede seleccionar los diversos provedorees para estos.",
                        modifier=Modifier,
                        textAlign = TextAlign.Start)
                }
            }
        }
    }
    Row (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        val categorySortedBy = remember { mutableStateOf("id") }
        val data = mutableStateOf(
            transaction {
                addLogger(StdOutSqlLogger)
                Familia_Productos
                    .selectAll()
                    .map {
                        it[Familia_Productos.id] to mapOf(
                            "id" to it[Familia_Productos.id].value,
                            "name" to it[Familia_Productos.nombre],
                            "count" to (Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value }
                                .count()),
                            "countCrit" to Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value and (Productos.stock lessEq Productos.stock_critico) }
                                .count()
                        )
                    }.sortedBy { it.second[categorySortedBy.value].toString() }
            }
        )
        val selectedRow = remember { mutableStateOf(0) }
        var productosData: MutableState<List<Pair<EntityID<Int>, Map<String, *>>>> = mutableStateOf(listOf())
        val selectedProductRow = remember { mutableStateOf(0) }
        val selectedProductAction = remember { mutableStateOf(ProductAction.Add) }
        val productSortedBy = remember { mutableStateOf("id") }
        var editProductValues by remember { mutableStateOf( false ) }

        Column (
            modifier = Modifier
                .fillMaxHeight()
                .width(850.dp)
                .padding(20.dp)
        ){
            var newName by remember { mutableStateOf("") }
            var editName by remember { mutableStateOf("") }
            var editNameBoolean by remember { mutableStateOf( false ) }

            Text("Tipos de productos", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(10.dp))

            if (data.value.isNotEmpty()) {
                if (selectedRow.value == 0) {
                    selectedRow.value = data.value.first().first.value
                }
                val selectedData = remember {
                    mutableStateOf(
                        data.value.find {
                            it.first.value == selectedRow.value
                        }
                    )
                }
                Column (
                    modifier = Modifier
                        .width(800.dp)
                ) {
                    Row (modifier = Modifier.fillMaxWidth()){
                        tableCell("ID", modifier = Modifier.weight(WEIGHT_ID)
                            .clickable { categorySortedBy.value = "id" }
                        )
                        tableCell("Nombre", modifier = Modifier.weight(WEIGHT_NAME)
                            .clickable { categorySortedBy.value = "name" }
                        )
                        tableCell("Cantidad de Productos", modifier = Modifier.weight(WEIGHT_AMOUNT_PRODUCTS)
                            .clickable { categorySortedBy.value = "count" }
                        )
                        tableCell("Cantidad en estado critico", modifier = Modifier.weight(WEIGHT_AMOUNT_CRITIC)
                            .clickable { categorySortedBy.value = "countCrit" }
                        )
                    }
                }
                LazyColumn (
                    modifier = Modifier
                        .height(400.dp)
                        .width(800.dp)
                ) {
                    items(data.value) {
                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .background(color =
                                when (selectedRow.value) {
                                    (it.second["id"].toString().toInt()) -> Color.Gray
                                    else -> Color.Transparent
                                }
                            )
                            .clickable {
                                selectedRow.value = (it.second["id"].toString().toInt())
                                selectedData.value = it
                                editName = it.second["name"].toString()
                                productosData = mutableStateOf(
                                    transaction {
                                        Productos.select {
                                            Productos.id_familia_producto eq selectedRow.value
                                        }.map {
                                            it[Productos.id] to mapOf(
                                                "id" to it[Productos.id].value,
                                                "name" to it[Productos.nombre],
                                                "description" to it[Productos.descripcion],
                                                "precioCompra" to it[Productos.precio_c],
                                                "precioVenta" to it[Productos.precio_v],
                                                "stock" to it[Productos.stock],
                                                "stockCritico" to it[Productos.stock_critico],
                                            )
                                        } .sortedBy { it.second[productSortedBy.value].toString() }
                                    }
                                )
                                if (productosData.value.isNotEmpty()) {
                                    selectedProductRow.value = productosData.value.first().first.value
                                }
                                editProductValues = false
                            }
                        ) {
                            val modifier = Modifier.background(color = Color.Transparent)
                            tableCell(
                                it.second["id"].toString(), modifier = modifier
                                    .weight(WEIGHT_ID)
                            )
                            tableCell(
                                it.second["name"].toString(), modifier = modifier
                                    .weight(WEIGHT_NAME)
                            )
                            tableCell(
                                it.second["count"].toString(), modifier = modifier
                                    .weight(WEIGHT_AMOUNT_PRODUCTS)
                            )
                            tableCell(
                                it.second["countCrit"].toString(), modifier = modifier
                                    .weight(WEIGHT_AMOUNT_CRITIC)
                            )
                        }
                    }
                }
            }
            else {
                Text("No hay tipos de productos registrados.", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(5.dp))
            Divider(modifier = Modifier.height(1.dp).fillMaxWidth(), color = Color.Gray)
            Spacer(modifier = Modifier.height(5.dp))

            val setSelectedAction = remember { mutableStateOf( CategoryAction.Add ) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = {
                    setSelectedAction.value = CategoryAction.Add
                }) {
                    Text("Agregar tipo de producto")
                }
                if (data.value.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedButton(onClick = {
                        setSelectedAction.value = CategoryAction.Edit
                    }) {
                        Text("Editar tipo de producto")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedButton(onClick = {
                        setSelectedAction.value = CategoryAction.Show
                    }) {
                        Text("Mostrar productos")
                    }
                }
            }
            when (setSelectedAction.value) {
                CategoryAction.Add -> Column {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text ("Agregar nuevo tipo de producto.", fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = newName,
                        onValueChange = {
                            newName = it
                        },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    formSpacer(modifier = Modifier.height(10.dp), newName.isEmpty(), "El nombre no puede estar vacio.")

                    Spacer(modifier = Modifier.height(10.dp))

                    Row (modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(onClick = {
                            if (newName.isNotEmpty()) {
                                familiaProductoInsert(newName)
                                data.value =
                                    transaction {
                                        addLogger(StdOutSqlLogger)
                                        Familia_Productos
                                            .selectAll()
                                            .map {
                                                it[Familia_Productos.id] to mapOf(
                                                    "id" to it[Familia_Productos.id].value,
                                                    "name" to it[Familia_Productos.nombre],
                                                    "count" to (Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value }
                                                        .count()),
                                                    "countCrit" to Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value and (Productos.stock lessEq Productos.stock_critico) }
                                                        .count()
                                                )
                                            }.sortedBy { it.second[categorySortedBy.value].toString() }
                                    }
                                newName = ""
                            }
                        }) {
                            Text("Registrar")
                        }
                    }

                }
                CategoryAction.Edit -> Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text ("Editar tipo de producto existente", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (data.value.isNotEmpty() && selectedRow.value != 0) {
                        if (editName == "" && !editNameBoolean) {
                            editName = data.value.find {
                                it.first.value == selectedRow.value
                            }!!.second["name"].toString()
                            editNameBoolean = true
                        }
                        OutlinedTextField(value = editName,
                            onValueChange = {
                                editName = it
                            },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        formSpacer(modifier = Modifier.height(10.dp), editName.isEmpty(), "El nombre no puede estar vacio.")
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(onClick = {
                            if (editName.isNotEmpty()) {
                                familiaProductoEdit(selectedRow.value, editName)
                                data.value =
                                    transaction {
                                        addLogger(StdOutSqlLogger)
                                        Familia_Productos
                                            .selectAll()
                                            .map {
                                                it[Familia_Productos.id] to mapOf(
                                                    "id" to it[Familia_Productos.id].value,
                                                    "name" to it[Familia_Productos.nombre],
                                                    "count" to (Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value }
                                                        .count()),
                                                    "countCrit" to Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value and (Productos.stock lessEq Productos.stock_critico) }
                                                        .count()
                                                )
                                            }.sortedBy { it.second[categorySortedBy.value].toString() }
                                    }
                            }
                        }) {
                            Text("Editar")
                        }
                    } else if (data.value.isNotEmpty() && selectedRow.value == 0) {
                        Text ("Tipo de producto no seleccionado", fontSize = 18.sp)
                    } else {
                        Text ("La lista de tipos de productos se encuentra vacia", fontSize = 18.sp)
                    }
                }
                CategoryAction.Show -> Column {
                    if (data.value.isNotEmpty() && selectedRow.value != 0) {
                        productosData = mutableStateOf(
                            transaction {
                                Productos.select {
                                    Productos.id_familia_producto eq selectedRow.value
                                }.map {
                                    it[Productos.id] to mapOf(
                                        "id" to it[Productos.id].value,
                                        "name" to it[Productos.nombre],
                                        "description" to it[Productos.descripcion],
                                        "precioCompra" to it[Productos.precio_c],
                                        "precioVenta" to it[Productos.precio_v],
                                        "stock" to it[Productos.stock],
                                        "stockCritico" to it[Productos.stock_critico],
                                    )
                                } .sortedBy { it.second[productSortedBy.value].toString() }
                            }
                        )
                        println(productosData.value)
                        if (productosData.value.isNotEmpty()) {
                            if (selectedProductRow.value == 0) {
                                selectedProductRow.value = productosData.value.first().first.value
                            }
                            LazyColumn (contentPadding = PaddingValues(10.dp)) {
                                val modifier = Modifier.height(50.dp)
                                item {
                                    Column (modifier = Modifier.fillMaxWidth()) {
                                        Text ("Listado de productos", fontSize = 20.sp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row {
                                            tableCell("ID", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_ID)
                                                .clickable { productSortedBy.value = "id" })
                                            tableCell("Nombre", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_NAME)
                                                .clickable { productSortedBy.value = "name" })
                                            tableCell("Descripcion", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_DESCRIPTION)
                                                .clickable { productSortedBy.value = "description" })
                                            tableCell("Precio compra", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_PRECIOCOMPRA)
                                                .clickable { productSortedBy.value = "precioCompra" })
                                            tableCell("Precio venta", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_PRECIOVENTA)
                                                .clickable { productSortedBy.value = "precioVenta" })
                                            tableCell("Stock", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_STOCK)
                                                .clickable { productSortedBy.value = "stock" })
                                            tableCell("Stock critico", modifier = modifier
                                                .weight(WEIGHT_PRODUCT_STOCKCRITICO)
                                                .clickable { productSortedBy.value = "stockCritico" })
                                        }
                                    }
                                }
                                items(productosData.value) {
                                    Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .background(color = when (selectedProductRow.value) {
                                                (it.second["id"] as Int) -> Color.Gray
                                                else -> Color.Transparent
                                            })
                                            .clickable {
                                                selectedProductRow.value = (it.second["id"] as Int)
                                                editProductValues = false
                                            }
                                    ) {
                                        tableCell(it.first.toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_ID))
                                        tableCell(it.second["name"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_NAME))
                                        tableCell(it.second["description"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_DESCRIPTION))
                                        tableCell(it.second["precioCompra"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_PRECIOCOMPRA))
                                        tableCell(it.second["precioVenta"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_PRECIOVENTA))
                                        tableCell(it.second["stock"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_STOCK))
                                        tableCell(it.second["stockCritico"].toString(), modifier = modifier
                                            .weight(WEIGHT_PRODUCT_STOCKCRITICO))
                                    }
                                }

                            }
                        } else {
                            Column (modifier = Modifier
                                .padding(10.dp)
                            ) {
                                Text("No hay productos registrados bajo esta categoria.", fontSize = 20.sp)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                            ,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = {
                                selectedProductAction.value = ProductAction.Add
                            }) {
                                Text("Agregar Producto")
                            }
                            if (productosData.value.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedButton(onClick = {
                                    selectedProductAction.value = ProductAction.Edit
                                }) {
                                    Text("Editar Producto")
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedButton(onClick = {
                                    selectedProductAction.value = ProductAction.Show
                                }) {
                                    Text("Mostrar Proveedores")
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider(modifier = Modifier
            .fillMaxHeight()
            .width(1.dp),
            color = Color.Gray
        )
        if (productosData.value.isNotEmpty() || selectedProductAction.value == ProductAction.Add) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
            ) {
                println(selectedProductAction.value)
                var productNewName by remember { mutableStateOf("") }
                var productDescription by remember { mutableStateOf("") }
                var productPrecioC by remember { mutableStateOf(0) }
                var productPrecioV by remember { mutableStateOf(0) }
                var productStock by remember { mutableStateOf(0) }
                var productStockCrit by remember { mutableStateOf(0) }
                val minInt = 0
                val maxInt = Int.MAX_VALUE
                when(selectedProductAction.value) {
                    ProductAction.Add -> {
                        Text("Agregar un nuevo producto", fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = productNewName,
                            onValueChange = {
                                productNewName = it
                            },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        formSpacer(modifier = Modifier.height(10.dp), productNewName.isEmpty(), "El nombre no puede estar vacio.")
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = productDescription,
                            onValueChange = {
                                productDescription = it
                            },
                            label = { Text("Descripcion") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        formSpacer(modifier = Modifier.height(10.dp), productDescription.isEmpty(), "El nombre no puede estar vacio.")
                        Spacer(modifier = Modifier.height(10.dp))

                        Row (modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = productPrecioC.toString(),
                                onValueChange = {
                                    productPrecioC = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Precio Compra") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(value = productPrecioV.toString(),
                                onValueChange = {
                                    productPrecioV = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Precio Venta") },
                                modifier = Modifier.weight(1f)
                            )

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row (modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = productStock.toString(),
                                onValueChange = {
                                    productStock = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Stock Actual") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(value = productStockCrit.toString(),
                                onValueChange = {
                                    productStockCrit = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Stock Critico") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        dropdownSelectById("Tipo producto",
                            data.value.map{ it.first to it.second["name"].toString() },
                            selectedRow.component1(),
                            selectedRow.component2(),
                            Arrangement.Start
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Proveedores", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        val providersList = remember { mutableStateOf( listOf<Int>() ) }

                        val providersData = transaction {
                            Proveedores.innerJoin(Users).selectAll().map {
                                it[Proveedores.id] to listOf(
                                    QueryResult.IntQueryResult("id", it[Proveedores.id].value),
                                    QueryResult.StringQueryResult("name", it[Users.nombre]),
                                    QueryResult.StringQueryResult("email", it[Users.email]),
                                    QueryResult.StringQueryResult("number", it[Proveedores.num_telefono_empresa])
                                )
                            }.toList().sortedBy { it.first.value }
                        }
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row (modifier = Modifier.fillMaxWidth()){
                                tableCell("ID",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_ID)
                                )
                                tableCell("Nombre",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_NAME)
                                )
                                tableCell("Email",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_EMAIL)
                                )
                                tableCell("Numero",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_NUMBER)
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            items(providersData) {
                                println(providersList.value.toString())
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color =
                                        if (it.first.value in providersList.value) {
                                            Color.Gray
                                        } else {
                                            Color.Transparent
                                        }
                                        )
                                        .clickable {
                                            val value = it.first.value
                                            if (value in providersList.value) {
                                                providersList.value = providersList.value.filterNot { it == value }
                                            } else {
                                                providersList.value = providersList.value + value
                                            }
                                            println(providersList.value.toString())
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
                                                when(it.columnName) {
                                                    "id" ->
                                                        WEIGHT_PROVIDER_ID
                                                    "name" ->
                                                        WEIGHT_PROVIDER_NAME
                                                    "email" ->
                                                        WEIGHT_PROVIDER_EMAIL
                                                    "number" ->
                                                        WEIGHT_PROVIDER_NUMBER
                                                    else ->
                                                        1f
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Row {
                            OutlinedButton(onClick = {
                                if (productNewName.isNotEmpty() &&
                                    productDescription.isNotEmpty()
                                ) {
                                    productoInsert(
                                        productNewName,
                                        productDescription,
                                        productPrecioC,
                                        productPrecioV,
                                        productStock,
                                        productStockCrit,
                                        selectedRow.component1(),
                                        providersList.value
                                    )

                                    data.value =
                                        transaction {
                                            addLogger(StdOutSqlLogger)
                                            Familia_Productos
                                                .selectAll()
                                                .map {
                                                    it[Familia_Productos.id] to mapOf(
                                                        "id" to it[Familia_Productos.id].value,
                                                        "name" to it[Familia_Productos.nombre],
                                                        "count" to (Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value }
                                                            .count()),
                                                        "countCrit" to Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value and (Productos.stock lessEq Productos.stock_critico) }
                                                            .count()
                                                    )
                                                }.sortedBy { it.second[categorySortedBy.value].toString() }
                                        }

                                    productosData = mutableStateOf(
                                        transaction {
                                            Productos.select {
                                                Productos.id_familia_producto eq selectedRow.value
                                            }.map {
                                                it[Productos.id] to mapOf(
                                                    "id" to it[Productos.id].value,
                                                    "name" to it[Productos.nombre],
                                                    "description" to it[Productos.descripcion],
                                                    "precioCompra" to it[Productos.precio_c],
                                                    "precioVenta" to it[Productos.precio_v],
                                                    "stock" to it[Productos.stock],
                                                    "stockCritico" to it[Productos.stock_critico],
                                                )
                                            } .sortedBy { it.second[productSortedBy.value].toString() }
                                        }
                                    )

                                    productNewName = ""
                                    productDescription = ""
                                    productPrecioC = 0
                                    productPrecioV = 0
                                    productStock = 0
                                    productStockCrit = 0
                                    providersList.value = listOf<Int>()
                                }
                            }) {
                                Text("Crear")
                            }
                        }

                    }
                    ProductAction.Edit -> {
                        val providersList = remember { mutableStateOf( listOf<Int>() ) }
                        val selectProductCategory  = remember { mutableStateOf(0) }
                        val selectedProduct = productosData.value.find {
                            it.first.value == selectedProductRow.value
                        }
                        if (selectedProduct != null) {
                            if (!editProductValues) {
                                productNewName = selectedProduct.second["name"].toString()
                                productDescription = selectedProduct.second["description"].toString()
                                productPrecioC = selectedProduct.second["precioCompra"].toString().toInt()
                                productPrecioV = selectedProduct.second["precioVenta"].toString().toInt()
                                productStock = selectedProduct.second["stock"].toString().toInt()
                                productStockCrit = selectedProduct.second["stockCritico"].toString().toInt()
                                if (selectProductCategory.value == 0) {
                                    selectProductCategory.value = selectedRow.value
                                }
                                editProductValues = true
                            }

                            providersList.value = transaction {
                                ProvProductos
                                    .select { ProvProductos.id_producto eq selectedProduct.first.value }
                                    .map {
                                        it[ProvProductos.id_proveedor]
                                    } .toList()
                            }
                        }

                        Text("Editar Producto", fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = productNewName,
                            onValueChange = {
                                productNewName = it
                            },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        formSpacer(modifier = Modifier.height(10.dp), productNewName.isEmpty(), "El nombre no puede estar vacio.")
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = productDescription,
                            onValueChange = {
                                productDescription = it
                            },
                            label = { Text("Descripcion") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        formSpacer(modifier = Modifier.height(10.dp), productDescription.isEmpty(), "El nombre no puede estar vacio.")
                        Spacer(modifier = Modifier.height(10.dp))

                        Row (modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = productPrecioC.toString(),
                                onValueChange = {
                                    productPrecioC = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Precio Compra") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(value = productPrecioV.toString(),
                                onValueChange = {
                                    productPrecioV = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Precio Venta") },
                                modifier = Modifier.weight(1f)
                            )

                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Row (modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(value = productStock.toString(),
                                onValueChange = {
                                    productStock = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Stock Actual") },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(value = productStockCrit.toString(),
                                onValueChange = {
                                    productStockCrit = if (it.isEmpty()) {
                                        minInt
                                    } else {
                                        val stringValue = it.filter { char -> char.isDigit() }.toInt()
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
                                        }
                                    }
                                },
                                label = { Text("Stock Critico") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        dropdownSelectById("Tipo producto",
                            data.value.map{ it.first to it.second["name"].toString() },
                            selectProductCategory.component1(),
                            selectProductCategory.component2(),
                            Arrangement.Start
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Proveedores", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        val providersData = transaction {
                            Proveedores.innerJoin(Users).selectAll().map {
                                it[Proveedores.id] to listOf(
                                    QueryResult.IntQueryResult("id", it[Proveedores.id].value),
                                    QueryResult.StringQueryResult("name", it[Users.nombre]),
                                    QueryResult.StringQueryResult("email", it[Users.email]),
                                    QueryResult.StringQueryResult("number", it[Proveedores.num_telefono_empresa])
                                )
                            }.toList().sortedBy { it.first.value }
                        }
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row (modifier = Modifier.fillMaxWidth()){
                                tableCell("ID",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_ID)
                                )
                                tableCell("Nombre",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_NAME)
                                )
                                tableCell("Email",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_EMAIL)
                                )
                                tableCell("Numero",
                                    modifier = Modifier
                                        .weight(WEIGHT_PROVIDER_NUMBER)
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            items(providersData) {
                                println(providersList.value.toString())
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color =
                                        if (it.first.value in providersList.value) {
                                            Color.Gray
                                        } else {
                                            Color.Transparent
                                        }
                                        )
                                        .clickable {
                                            val value = it.first.value
                                            if (value in providersList.value) {
                                                providersList.value = providersList.value.filterNot { it == value }
                                            } else {
                                                providersList.value = providersList.value + value
                                            }
                                            println(providersList.value.toString())
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
                                                when(it.columnName) {
                                                    "id" ->
                                                        WEIGHT_PROVIDER_ID
                                                    "name" ->
                                                        WEIGHT_PROVIDER_NAME
                                                    "email" ->
                                                        WEIGHT_PROVIDER_EMAIL
                                                    "number" ->
                                                        WEIGHT_PROVIDER_NUMBER
                                                    else ->
                                                        1f
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        Row {
                            OutlinedButton(onClick = {
                                if (productNewName.isNotEmpty() &&
                                    productDescription.isNotEmpty()
                                ) {
                                    productoUpdate(
                                        selectedProductRow.value,
                                        productNewName,
                                        productDescription,
                                        productPrecioC,
                                        productPrecioV,
                                        productStock,
                                        productStockCrit,
                                        selectProductCategory.component1(),
                                        providersList.value
                                    )

                                    data.value =
                                        transaction {
                                            addLogger(StdOutSqlLogger)
                                            Familia_Productos
                                                .selectAll()
                                                .map {
                                                    it[Familia_Productos.id] to mapOf(
                                                        "id" to it[Familia_Productos.id].value,
                                                        "name" to it[Familia_Productos.nombre],
                                                        "count" to (Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value }
                                                            .count()),
                                                        "countCrit" to Productos.select { Productos.id_familia_producto eq it[Familia_Productos.id].value and (Productos.stock lessEq Productos.stock_critico) }
                                                            .count()
                                                    )
                                                }.sortedBy { it.second[categorySortedBy.value].toString() }
                                        }
                                    selectedRow.value = selectProductCategory.value

                                    productosData = mutableStateOf(
                                        transaction {
                                            Productos.select {
                                                Productos.id_familia_producto eq selectedRow.value
                                            }.map {
                                                it[Productos.id] to mapOf(
                                                    "id" to it[Productos.id].value,
                                                    "name" to it[Productos.nombre],
                                                    "description" to it[Productos.descripcion],
                                                    "precioCompra" to it[Productos.precio_c],
                                                    "precioVenta" to it[Productos.precio_v],
                                                    "stock" to it[Productos.stock],
                                                    "stockCritico" to it[Productos.stock_critico],
                                                )
                                            } .sortedBy { it.second[productSortedBy.value].toString() }
                                        }
                                    )
                                }
                            }) {
                                Text("Editar")
                            }
                        }

                    }
                    ProductAction.Show -> {
                        Text("Mostrando proveedores", fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        val providersData = transaction {
                            ProvProductos.innerJoin(Proveedores.innerJoin(Users)).select {
                                ProvProductos.id_producto eq selectedProductRow.value
                            }.map {
                                it[Proveedores.id] to listOf(
                                    QueryResult.IntQueryResult("id", it[Proveedores.id].value),
                                    QueryResult.StringQueryResult("name", it[Users.nombre]),
                                    QueryResult.StringQueryResult("email", it[Users.email]),
                                    QueryResult.StringQueryResult("number", it[Proveedores.num_telefono_empresa])
                                )
                            }.toList().sortedBy { it.first.value }
                        }
                        if (providersData.isEmpty()) {
                            Text("El producto no posee proveedores.")
                        } else {
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row (modifier = Modifier.fillMaxWidth()){
                                    tableCell("ID",
                                        modifier = Modifier
                                            .weight(WEIGHT_PROVIDER_ID)
                                    )
                                    tableCell("Nombre",
                                        modifier = Modifier
                                            .weight(WEIGHT_PROVIDER_NAME)
                                    )
                                    tableCell("Email",
                                        modifier = Modifier
                                            .weight(WEIGHT_PROVIDER_EMAIL)
                                    )
                                    tableCell("Numero",
                                        modifier = Modifier
                                            .weight(WEIGHT_PROVIDER_NUMBER)
                                    )
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            ) {
                                items(providersData) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
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
                                                    when(it.columnName) {
                                                        "id" ->
                                                            WEIGHT_PROVIDER_ID
                                                        "name" ->
                                                            WEIGHT_PROVIDER_NAME
                                                        "email" ->
                                                            WEIGHT_PROVIDER_EMAIL
                                                        "number" ->
                                                            WEIGHT_PROVIDER_NUMBER
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
        }
    }
}
