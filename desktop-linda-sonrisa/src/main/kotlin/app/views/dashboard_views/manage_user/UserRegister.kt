package app.views.dashboard_views.manage_user

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.v1.DialogProperties
import app.components.datePicker
import app.components.datePickerWithLocalDate
import app.components.dropdownSelect
import app.components.formSpacer
import app.data.*
import app.data.Validator.email
import app.data.Validator.phoneNumberValidator
import app.data.Validator.validaRut
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private val blankProfilePicture = {}.javaClass.classLoader.getResource("images/blank-profile-picture.png")!!


private val eliminate = listOf(
    "False",
    "True"
)

private val roles = transaction {
    Rol.all().toList().map{ it.id.value to it.nombre }
}

private val tiposEmpleados = transaction {
    Tipo_Empleado.all().toList().map{ it.id.value to it.nombre }
}

private val eliminado = listOf(
    'F' to "false",
    'T' to "true"
)

private val salud_e = listOf(
    'I' to "Isapre",
    'F' to "Fonasa"
)

@OptIn(ExperimentalComposeUiApi::class)
private val TAB_KEY = Key.AltLeft.keyCode

@Composable
fun userRegister() {
    // Focus
    val focusRequesters = List(6) { FocusRequester() }

    // File
    val (fileChooserStatus, setFileChooserStatus) = remember { mutableStateOf(false) }

    // DatePicker
    val (selectedDay, setSelectedDay) = remember { mutableStateOf("1")}
    val (selectedMonth, setSelectedMonth) = remember { mutableStateOf("1")}
    val (selectedYear, setSelectedYear) = remember { mutableStateOf("1970")}

    // User data
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    val (selectedEliminado, setSelectedEliminado) = remember { mutableStateOf(0) }
    val (selectedRol, setSelectedRol) = remember { mutableStateOf(0) }

    // User Validators
    val (correctEmail, setCorrectEmail) = remember { mutableStateOf(false) }
    val (correctPassword, setCorrectPassword) = remember { mutableStateOf(false) }
    val (correctRut, setCorrectRut) = remember { mutableStateOf(false) }

    // Admin data
    val (phoneNumber, setPhoneNumber) = remember { mutableStateOf("") } //Also Applies for Empleado, Pacientes and Proveedores

    //Empleados Data
    val (saludEmpleado, setSaludEmpleado) = remember { mutableStateOf(0) }
    val (salario, setSalario) = remember { mutableStateOf(0.toLong()) }
    val (inicioContrato, setInicioContrato) = remember { mutableStateOf(LocalDate.of(1970, 1, 1)) }
    val (finContrato, setFinContrato) = remember { mutableStateOf(LocalDate.of(1970, 1, 1)) }
    val (AFP, setAFP) = remember { mutableStateOf("") }
    val (nombreBanco, setNombreBanco) = remember { mutableStateOf("") }
    val (numeroCuenta, setNumeroCuenta) = remember { mutableStateOf(0.toLong()) }
    val (tipoEmpleado, setTipoEmpleado) = remember { mutableStateOf(0) }
    val (imagen, setImagen) = remember { mutableStateOf(File(blankProfilePicture.toURI()).readBytes()) }

    //Paciente
    val (file, setFile) = remember { mutableStateOf(File(blankProfilePicture.toURI()).readBytes()) }

    // Confirm
    val (confirmWindow, setConfirmWindow) = remember { mutableStateOf(false) }
    println(
        LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt())
    )

    confirmWindowDialog(confirmWindow, setConfirmWindow,
        email, password, rut, nombre, direccion, selectedYear, selectedMonth, selectedDay, selectedEliminado, selectedRol,
        phoneNumber,
        saludEmpleado, salario, inicioContrato, finContrato, AFP, nombreBanco, numeroCuenta, imagen, tipoEmpleado,
        file
    )

    Column (
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = Modifier
                .padding(25.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .width(500.dp),
                horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Register new user", fontSize = 30.sp)

                formSpacer(modifier = Modifier.height(70.dp))

//                focusableOutlinedTextField(email, )

                OutlinedTextField(value = email,
                    onValueChange = {
                        email = it
                        if (email.isNotEmpty()) {
                            setCorrectEmail(email(email).not())
                        } else {
                            setCorrectEmail(false)
                        }
                    },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[0])
                        .onKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[1].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[1].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp), correctEmail, "Invalid email.")

                OutlinedTextField(value = password,
                    onValueChange = {
                        password = it
                        if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                            setCorrectPassword((password == confirmPassword).not())
                        } else {
                            setCorrectPassword(false)
                        }
                    },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[1])
                        .onPreviewKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[2].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[2].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                            setCorrectPassword((password == confirmPassword).not())
                        } else {
                            setCorrectPassword(false)
                        }
                    },
                    label = { Text("Confirm Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[2])
                        .onPreviewKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[3].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[3].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp), correctPassword, "Passwords do not match.")

                OutlinedTextField(value = rut,
                    onValueChange = {
                        rut = it
                        if (rut.isNotEmpty()) {
                            setCorrectRut(validaRut(rut).not())
                        } else {
                            setCorrectRut(false)
                        }
                    },
                    label = { Text("Rut") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[3])
                        .onPreviewKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[4].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[4].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp), correctRut, "Invalid rut.")

                OutlinedTextField(value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[4])
                        .onPreviewKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[5].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[5].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Direccion") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequesters[5])
                        .onPreviewKeyEvent {
                            if (it.key.keyCode == TAB_KEY && it.type == KeyEventType.KeyDown){
                                focusRequesters[0].requestFocus()
                                true //true -> consumed
                            } else false
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {focusRequesters[0].requestFocus()}
                    ),
                    singleLine = true)

                formSpacer(modifier = Modifier.height(10.dp))

                datePicker("Fecha Nacimiento", selectedDay, setSelectedDay, selectedMonth, setSelectedMonth, selectedYear, setSelectedYear)

                formSpacer(modifier = Modifier.height(10.dp))

                dropdownSelect("Rol", roles, selectedRol, setSelectedRol)

                formSpacer(modifier = Modifier.height(10.dp))

                dropdownSelect("Eliminado", eliminado, selectedEliminado, setSelectedEliminado)

                formSpacer(modifier = Modifier.height(10.dp))

            }
            Spacer(modifier = Modifier.width(20.dp))
            if (roles[selectedRol].second == "Administrador") {
                registerAdministradorView(phoneNumber, setPhoneNumber)
            } else if (roles[selectedRol].second == "Empleado") {
                registerEmpleadoView(phoneNumber, setPhoneNumber,
                    saludEmpleado, setSaludEmpleado,
                    salario, setSalario,
                    inicioContrato, setInicioContrato,
                    finContrato, setFinContrato,
                    AFP, setAFP,
                    nombreBanco, setNombreBanco,
                    numeroCuenta, setNumeroCuenta,
                    imagen, setImagen,
                    tipoEmpleado, setTipoEmpleado
                )
            } else if (roles[selectedRol].second == "Proveedor") {
                registerProveedorView(phoneNumber, setPhoneNumber)
            } else {
                registerPacienteView(
                    phoneNumber, setPhoneNumber,
                    saludEmpleado, setSaludEmpleado,
                    file, setFile
                )
            }
//            Column (
//                modifier = Modifier
//                    .padding(5.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    "Hi"
//                )
//                Button(onClick = {
//                    setFileChooserStatus(true)
////                val s = "c:"
////                val p = Paths.get(s)
////                Desktop.getDesktop().openHelpViewer()
////                val file = File("c:")
////                val uri = URI("c:/")
////                val desktop = Desktop.getDesktop()
////                desktop.browse(uri)
//                }) {
//                    Text("Open")
//                }
//                val fc = JFileChooser()
//                fc.fileFilter = FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp")
//                if (fileChooserStatus) {
//                    var returnVal = fc.showSaveDialog(LocalAppWindow.current.window)
//                    setFileChooserStatus(false)
//                    if (returnVal === JFileChooser.APPROVE_OPTION) {
//                        val file = fc.selectedFile
//                        //This is where a real application would open the file.
//
//                        println("Opening: " + file.absolutePath + "." + "\n")
//                        val blobValue = FileInputStream(file).readBytes()
//                        val imageBitMap = byteArrayToBitMap(blobValue)
//                        Image(
//                            bitmap = imageBitMap,
//                            contentDescription = "Temporary Image",
//                            modifier= Modifier.fillMaxSize()
//                        )
//                    transaction {
//                        Empleado.findById(1)?.imagen = ExposedBlob(f.readBytes())
//                        commit()
//                    }
//                    } else {
//                        println("Open command cancelled by user.\n")
//                    }
//                }
//            val imagen = transaction { Empleado.findById(1)?.imagen?.bytes }
//            val x = ByteArrayInputStream(imagen)
//            val bm = asImageAsset(ImageIO.read(x))
//            Image(
//                bitmap = bm,
//                contentDescription = "Hi",
//                modifier= Modifier.fillMaxSize())


//            }
        }
        Row (
            modifier = Modifier
                .padding(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = {
                setConfirmWindow(true)
            }
            ) {
                Text("Register", color = Color.Black)
            }

            OutlinedButton(onClick = {

            }
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    }
}


@Composable
fun confirmWindowDialog(
    // Window Parameters
    confirmWindow: Boolean,
    setConfirmWindow: (Boolean) -> Unit,
    // User Data
    email: String,
    password: String,
    rut: String,
    nombre: String,
    direccion: String,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    selectedEliminado: Int,
    selectedRol: Int,
    // Admin Data (and for everything else tbh)
    phoneNumber: String,
    // Empleados Data
    saludEmpleado: Int,
    salario: Long,
    inicioContrato: LocalDate,
    finContrato: LocalDate,
    AFP: String,
    nombreBanco: String,
    numeroCuenta: Long,
    imagen: ByteArray,
    tipoEmpleado: Int,
    file: ByteArray
) {
    if (confirmWindow) {
        AlertDialog(
            onDismissRequest = {
                setConfirmWindow(false)
            },
            title = {
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    if (roles[selectedRol].second == "Administrador") {
                        insertAdmin(
                            email, password, rut, nombre, direccion,
                            LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()),
                            eliminado[selectedEliminado].first, roles[selectedRol].first, phoneNumber
                        )
                    } else if (roles[selectedRol].second == "Empleado") {
                        insertEmpleado(
                            email, password, rut, nombre, direccion,
                            LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()),
                            eliminado[selectedEliminado].first, roles[selectedRol].first,
                            phoneNumber,
                            salud_e[saludEmpleado].first, salario,
                            inicioContrato, finContrato,
                            AFP, nombreBanco, numeroCuenta,
                            tiposEmpleados[tipoEmpleado].first, imagen
                        )
                    } else if (roles[selectedRol].second == "Proveedor") {
                        insertProveedor(
                            email, password, rut, nombre, direccion,
                            LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()),
                            eliminado[selectedEliminado].first, roles[selectedRol].first, phoneNumber
                        )
                    } else if (roles[selectedRol].second == "Paciente") {
                        insertPaciente(
                            email, password, rut, nombre, direccion,
                            LocalDate.of(selectedYear.toInt(), selectedMonth.toInt(), selectedDay.toInt()),
                            eliminado[selectedEliminado].first, roles[selectedRol].first,
                            phoneNumber,
                            salud_e[saludEmpleado].first, file
                        )
                    }
                    setConfirmWindow(false)
                }
                ) {
                    Text("Confirmar", color = Color.Black)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    setConfirmWindow(false)
                }
                ) {
                    Text("Cancelar", color = Color.Black)
                }
            },
            text = {
                Column {
                    Text("Seguro que quieres registrar al usuario con los detalles:")
                    Text("Email: $email")
                    Text("Rut: $rut")
                    Text("Nombre: $nombre")
                    Text("Direccion: $direccion")
                    Text("Fecha de nacimiento: ${selectedDay}/${selectedMonth}/${selectedYear}")
                    Text("Eliminado: ${eliminado[selectedEliminado].second}")
                    Text("Rol: ${roles[selectedRol].second}")
                    Spacer(modifier = Modifier.height(20.dp))
                    if (roles[selectedRol].second == "Administrador") {
                        Text("Numero de Telefono: $phoneNumber")
                    }
                }
            },
            properties = DialogProperties(
                title = "Registrar Usuario",
                undecorated = true
            ),
            modifier = Modifier
                .border(
                    width = 1.dp,
                    MaterialTheme.colors.primary
                )
        )
    }
}


@Composable
fun registerPacienteView(
    phoneNumber: String,
    setPhoneNumber: (String) -> Unit,
    saludEmpleado: Int,
    setSaludEmpleado: (Int) -> Unit,
    fileByteArray: ByteArray,
    setFileByteArray: (ByteArray) -> Unit
) {
    val (correctPhoneNumber, setCorrectPhoneNumber) = remember { mutableStateOf(false) }

    var file by remember { mutableStateOf(File(blankProfilePicture.toURI())) }

    val (fileChooserStatus, setFileChooserStatus) = remember { mutableStateOf(false) }
    val fc = JFileChooser()
    fc.fileFilter = FileNameExtensionFilter("Documents", "pdf")
    if (fileChooserStatus) {
        val returnVal = fc.showSaveDialog(LocalAppWindow.current.window)
        setFileChooserStatus(false)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.selectedFile
            //This is where a real application would open the file.

            println("Opening: " + file.absolutePath + "." + "\n")
            val blobValue = FileInputStream(file).readBytes()
            setFileByteArray(file.readBytes())
        } else {
            println("Open command cancelled by user.\n")
        }
    }
    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(500.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Spacer(modifier = Modifier.height(59.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                setPhoneNumber(it)
                if (phoneNumber.isNotEmpty() && phoneNumber.isNotBlank()) {
                    setCorrectPhoneNumber(Validator.phoneNumberValidator(phoneNumber).not())
                } else {
                    setCorrectPhoneNumber(false)
                }
            },
            label = { Text("Numero de Telefono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        formSpacer(modifier = Modifier.height(10.dp), correctPhoneNumber, "Invalid phone number.")

        dropdownSelect("Salud Paciente", salud_e, saludEmpleado, setSaludEmpleado)

        formSpacer(modifier = Modifier.height(10.dp))

        Column {
            OutlinedTextField(
                value = fileIsEmpty(file),
                onValueChange = {
                },
                label = { Text("Archivo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row {
                OutlinedButton(onClick = {
                    setFileChooserStatus(true)
                }) {
                    Text("Seleccionar", color = Color.Black)
                }

                OutlinedButton(onClick = {
                    setFileByteArray(File(blankProfilePicture.toURI()).readBytes())
                    file = File(blankProfilePicture.toURI())
                }) {
                    Text("Reiniciar", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun registerAdministradorView(phoneNumber: String, setPhoneNumber: (String) -> Unit) {

    val (correctPhoneNumber, setCorrectPhoneNumber) = remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(5.dp)
            .width(600.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Spacer(modifier = Modifier.height(59.dp))

        OutlinedTextField(value = phoneNumber,
            onValueChange = {
                setPhoneNumber(it)
                if (phoneNumber.isNotEmpty() && phoneNumber.isNotBlank()) {
                    setCorrectPhoneNumber(phoneNumberValidator(phoneNumber).not())
                } else {
                    setCorrectPhoneNumber(false)
                }
            },
            label = { Text("Numero de Telefono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true)

        formSpacer(modifier = Modifier.height(10.dp), correctPhoneNumber, "Invalid phone number.")

    }
}


@Composable
fun registerEmpleadoView(
    phoneNumber: String,
    setPhoneNumber: (String) -> Unit,
    saludEmpleado: Int,
    setSaludEmpleado: (Int) -> Unit,
    salario: Long,
    setSalario: (Long) -> Unit,
    inicioContrato: LocalDate,
    setInicioContrato: (LocalDate) -> Unit,
    finContrato: LocalDate,
    setFinContrato: (LocalDate) -> Unit,
    AFP: String,
    setAFP: (String) -> Unit,
    nombreBanco: String,
    setNombreBanco: (String) -> Unit,
    numeroCuenta: Long,
    setNumeroCuenta: (Long) -> Unit,
    imagen: ByteArray,
    setImagen: (ByteArray) -> Unit,
    tipoEmpleado: Int,
    setTipoEmpleado: (Int) -> Unit,
) {

    val (correctPhoneNumber, setCorrectPhoneNumber) = remember { mutableStateOf(false) }

    val (fileChooserStatus, setFileChooserStatus) = remember { mutableStateOf(false) }
    val fc = JFileChooser()
    fc.fileFilter = FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp")
    if (fileChooserStatus) {
        val returnVal = fc.showSaveDialog(LocalAppWindow.current.window)
        setFileChooserStatus(false)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            //This is where a real application would open the file.

            println("Opening: " + file.absolutePath + "." + "\n")
            val blobValue = FileInputStream(file).readBytes()
            setImagen(file.readBytes())
        } else {
            println("Open command cancelled by user.\n")
        }
    }

    Row (
        modifier = Modifier
            .padding(5.dp)
            .width(1000.dp)
    ){
        Column (
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {

            Spacer(modifier = Modifier.height(59.dp))

            OutlinedTextField(value = phoneNumber,
                onValueChange = {
                    setPhoneNumber(it)
                    if (phoneNumber.isNotEmpty() && phoneNumber.isNotBlank()) {
                        setCorrectPhoneNumber(phoneNumberValidator(phoneNumber).not())
                    } else {
                        setCorrectPhoneNumber(false)
                    }
                },
                label = { Text("Numero de Telefono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            formSpacer(modifier = Modifier.height(10.dp), correctPhoneNumber, "Invalid phone number.")

            dropdownSelect("Salud Empleado", salud_e, saludEmpleado, setSaludEmpleado)

            formSpacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = "$${salario.toString()}",
                onValueChange = {
                    if (it.isEmpty()) {
                        setSalario(0)
                    } else {
                        if (it.length > 1 ){
                            val value = it
                                .replace("$", "")
                                .filter { it.isDigit() }
                                .toLong()
                            if (salario == 0.toLong()) {
                                setSalario(value/10)
                            } else {
                                setSalario(value)
                            }
                        } else {
                            setSalario(0)
                        }
                    }
                },
                label = { Text("Salario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            formSpacer(modifier = Modifier.height(10.dp))

            datePickerWithLocalDate("Inicio Contrato", inicioContrato, setInicioContrato)

            formSpacer(modifier = Modifier.height(10.dp))

            datePickerWithLocalDate("Fin Contrato", finContrato, setFinContrato)


            formSpacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = AFP,
                onValueChange = {
                    setAFP(it)
                },
                label = { Text("AFP") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            formSpacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = nombreBanco,
                onValueChange = {
                    setNombreBanco(it)
                },
                label = { Text("Nombre Banco") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            formSpacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = numeroCuenta.toString(),
                onValueChange = {
                    if (it.isEmpty()) {
                        setNumeroCuenta(0)
                    } else {
                        if (it.length > 0 ){
                            val value = it
                                .filter { it.isDigit() }
                                .toLong()
                            if (numeroCuenta == 0.toLong()) {
                                setNumeroCuenta(value/10)
                            } else {
                                setNumeroCuenta(value)
                            }
                        } else {
                            setNumeroCuenta(0)
                        }
                    }
                },
                label = { Text("Numero Cuenta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true)

            formSpacer(modifier = Modifier.height(10.dp))

            dropdownSelect("Tipo Empleado", tiposEmpleados, tipoEmpleado, setTipoEmpleado)
        }
        Spacer(modifier=Modifier.width(20.dp))
        Column (
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card (
                elevation = 4.dp,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        setFileChooserStatus(true)
                    }
            ) {
                Image(byteArrayToBitMap(imagen), "Image", modifier = Modifier.size(300.dp, 300.dp))
            }
            formSpacer(modifier = Modifier.height(70.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedButton(onClick = {

                    setFileChooserStatus(true)
                }) {
                    Text("Seleccionar", color = Color.Black)
                }

                OutlinedButton(onClick = {
                    setImagen(File(blankProfilePicture.toURI()).readBytes())
                }) {
                    Text("Reiniciar", color = Color.Black)
                }
            }
        }
    }
}


@Composable
fun registerProveedorView(phoneNumber: String, setPhoneNumber: (String) -> Unit) {

    val (correctPhoneNumber, setCorrectPhoneNumber) = remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(5.dp)
            .width(600.dp),
        horizontalAlignment = Alignment.Start,
    ) {

        Spacer(modifier = Modifier.height(59.dp))

        OutlinedTextField(value = phoneNumber,
            onValueChange = {
                setPhoneNumber(it)
                if (phoneNumber.isNotEmpty() && phoneNumber.isNotBlank()) {
                    setCorrectPhoneNumber(phoneNumberValidator(phoneNumber).not())
                } else {
                    setCorrectPhoneNumber(false)
                }
            },
            label = { Text("Numero de Telefono") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true)

        formSpacer(modifier = Modifier.height(10.dp), correctPhoneNumber, "Invalid phone number.")

    }
}