package app.views.dashboard_views.dashboard_main

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@Composable
fun dashboard() {
    var windowSize by remember { mutableStateOf(IntSize(800, 600)) }
//    var width by remember { mutableStateOf(0.dp) }
//    var height by remember { mutableStateOf(0.dp) }

    LocalAppWindow.current.apply {
        events.onResize = {
            windowSize = it
        }
    }

//    width = LocalDensity.current.run {windowSize.width.toDp()}
//    height = LocalDensity.current.run {windowSize.height.toDp()}
//    Text(text = "Size: $windowSize, Width: $width, Height: $height")
    when(windowSize.width) {
        in 0..650 -> singleColumnLayout()
        in 651..900 -> doubleColumnLayout()
        else -> okeyLetsMakeIt2Rows()
    }
}


@Composable
fun singleColumnLayout(){
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        showStockStatusCard()
        Spacer(modifier = Modifier.height(10.dp))
        reservationStatusCard()
        Spacer(modifier = Modifier.height(10.dp))
        registeredPatientsCard()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun doubleColumnLayout(){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.width(40.dp))
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            showStockStatusCard()
            Spacer(modifier = Modifier.height(40.dp))
            reservationStatusCard()
            Spacer(modifier = Modifier.height(40.dp))
        }
        Spacer(modifier = Modifier.width(40.dp))
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            registeredPatientsCard()
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun okeyLetsMakeIt2Rows(){
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ){
        Spacer(modifier = Modifier.width(10.dp))
        showStockStatusCard()
        Spacer(modifier = Modifier.width(10.dp))
        reservationStatusCard()
        Spacer(modifier = Modifier.width(10.dp))
        registeredPatientsCard()
        Spacer(modifier = Modifier.width(10.dp))
    }
}