package app.views.dashboard_views.manage_user

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.components.datePickerWithLocalDate
import app.components.dropdownSelect
import app.components.formSpacer
import app.data.byteArrayToBitMap
import app.data.validator
import java.io.File
import java.io.FileInputStream
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

