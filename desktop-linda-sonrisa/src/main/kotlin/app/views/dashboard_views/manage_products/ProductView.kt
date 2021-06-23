package app.views.dashboard_views.manage_products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.components.dropdownSelect
import app.components.dropdownSelectById
import app.components.formSpacer
import app.components.tableCell
import app.data.Familia_Productos
import app.data.Productos
import app.data.familiaProductoEdit
import app.data.familiaProductoInsert
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


enum class ChooseByEnum {
    Productos,
    Tipos
}
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


@Composable
fun viewProducts() {

    Text("Administracion de Productos", fontSize = 24.sp)
    Spacer(modifier = Modifier.height(10.dp))

    val ChooseBy = listOf(
        ChooseByEnum.Productos to ChooseByEnum.Productos.toString(),
        ChooseByEnum.Tipos to "${ChooseByEnum.Tipos} de Productos",
    )

    val (selectedChooseBy, setSelectedChooseBy) = remember { mutableStateOf(0) }

    dropdownSelect("Ver", ChooseBy, selectedChooseBy, setSelectedChooseBy, Arrangement.Start)

    Spacer(modifier = Modifier.height(10.dp))

    when (ChooseBy[selectedChooseBy].first) {
        ChooseByEnum.Tipos -> {
            Row (
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
            ) {
                val FamiliaSortedBy = remember { mutableStateOf("id") }
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
                            }.sortedBy { it.second[FamiliaSortedBy.value].toString() }
                    }
                )
                val selectedRow = remember { mutableStateOf(0) }
                val selectedProductRow = remember { mutableStateOf(0) }
                var productosData: MutableState<List<Pair<EntityID<Int>, Map<String, *>>>> = mutableStateOf(listOf())
                val selectedProductAction = remember { mutableStateOf(ProductAction.Add) }

                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(850.dp)
                        .padding(20.dp)
                ){

                        var newName by remember { mutableStateOf("") }
                        var editName by remember { mutableStateOf("") }
                        var editNameBoolean by remember { mutableStateOf( false ) }
                        if (!data.value.isEmpty()) {
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
                                        .clickable { FamiliaSortedBy.value = "id" }
                                    )
                                    tableCell("Nombre", modifier = Modifier.weight(WEIGHT_NAME)
                                        .clickable { FamiliaSortedBy.value = "name" }
                                    )
                                    tableCell("Cantidad de Productos", modifier = Modifier.weight(WEIGHT_AMOUNT_PRODUCTS)
                                        .clickable { FamiliaSortedBy.value = "count" }
                                    )
                                    tableCell("Cantidad en estado critico", modifier = Modifier.weight(WEIGHT_AMOUNT_CRITIC)
                                        .clickable { FamiliaSortedBy.value = "countCrit" }
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
                                        .background(color = when (selectedRow.value) {
                                            (it.second["id"] as Int) -> Color.Gray
                                            else -> Color.Transparent
                                        })
                                        .clickable {
                                            selectedRow.value = (it.second["id"] as Int)
                                            selectedData.value = it
                                            editName = it.second["name"].toString()
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
                            if (!data.value.isEmpty()) {
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
                                formSpacer(modifier = Modifier.height(10.dp), !newName.isNotEmpty(), "El nombre no puede estar vacio.")

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
                                                        }.sortedBy { it.second[FamiliaSortedBy.value].toString() }
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

                                if (!data.value.isEmpty() && selectedRow.value != 0) {
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
                                    formSpacer(modifier = Modifier.height(10.dp), !editName.isNotEmpty(), "El nombre no puede estar vacio.")
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
                                                        }.sortedBy { it.second[FamiliaSortedBy.value].toString() }
                                                }
                                        }
                                    }) {
                                        Text("Editar")
                                    }
                                } else if (!data.value.isEmpty() && selectedRow.value == 0) {
                                    Text ("Tipo de producto no seleccionado", fontSize = 18.sp)
                                } else {
                                    Text ("La lista de tipos de productos se encuentra vacia", fontSize = 18.sp)
                                }
                            }
                            CategoryAction.Show -> Column {
                                val productSortedBy = remember { mutableStateOf("id") }
                                if (!data.value.isEmpty() && selectedRow.value != 0) {
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
                                    if (!productosData.value.isEmpty()) {
                                        if (selectedProductRow.value == 0) {
                                            selectedProductRow.value = productosData.value.first().first.value
                                        }
                                        LazyColumn (contentPadding = PaddingValues(10.dp)) {
                                            val modifier = Modifier.height(50.dp)
                                            item {
                                                Column (modifier = Modifier.fillMaxWidth()) {
                                                    Text ("Listado de productos", fontSize = 20.sp)
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Row(
                                                    ) {
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
                                        if (!productosData.value.isEmpty()) {
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
                if (!productosData.value.isEmpty() || selectedProductAction.value == ProductAction.Add) {
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

                                formSpacer(modifier = Modifier.height(10.dp), !productNewName.isNotEmpty(), "El nombre no puede estar vacio.")
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(value = productDescription,
                                    onValueChange = {
                                        productDescription = it
                                    },
                                    label = { Text("Descripcion") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                formSpacer(modifier = Modifier.height(10.dp), !productDescription.isNotEmpty(), "El nombre no puede estar vacio.")
                                Spacer(modifier = Modifier.height(10.dp))

                                Row (modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(value = productPrecioC.toString(),
                                        onValueChange = {
                                            if (it.isEmpty()) {
                                                productPrecioC = minInt
                                            } else {
                                                val stringValue = it.filter { it.isDigit() }.toInt()
                                                if (stringValue < minInt) {
                                                    productPrecioC = minInt
                                                } else if (stringValue > maxInt) {
                                                    productPrecioC = maxInt
                                                } else {
                                                    productPrecioC = stringValue
                                                }
                                            }
                                        },
                                        label = { Text("Precio Compra") },
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    OutlinedTextField(value = productPrecioV.toString(),
                                        onValueChange = {
                                            if (it.isEmpty()) {
                                                productPrecioV = minInt
                                            } else {
                                                val stringValue = it.filter { it.isDigit() }.toInt()
                                                if (stringValue < minInt) {
                                                    productPrecioV = minInt
                                                } else if (stringValue > maxInt) {
                                                    productPrecioV = maxInt
                                                } else {
                                                    productPrecioV = stringValue
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
                                            if (it.isEmpty()) {
                                                productStock = minInt
                                            } else {
                                                val stringValue = it.filter { it.isDigit() }.toInt()
                                                if (stringValue < minInt) {
                                                    productStock = minInt
                                                } else if (stringValue > maxInt) {
                                                    productStock = maxInt
                                                } else {
                                                    productStock = stringValue
                                                }
                                            }
                                        },
                                        label = { Text("Stock Actual") },
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    OutlinedTextField(value = productStockCrit.toString(),
                                        onValueChange = {
                                            if (it.isEmpty()) {
                                                productStockCrit = minInt
                                            } else {
                                                val stringValue = it.filter { it.isDigit() }.toInt()
                                                if (stringValue < minInt) {
                                                    productStockCrit = minInt
                                                } else if (stringValue > maxInt) {
                                                    productStockCrit = maxInt
                                                } else {
                                                    productStockCrit = stringValue
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

                            }
                            ProductAction.Edit -> {
                                Text("Editar Producto", fontSize = 20.sp)
                            }
                            ProductAction.Show -> {
                                Text("Mostrando proveedores", fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }
        ChooseByEnum.Productos -> {

        }
    }

}

fun <T> SnapshotStateList<T>.swapList(newList: List<T>){
    clear()
    addAll(newList)
}