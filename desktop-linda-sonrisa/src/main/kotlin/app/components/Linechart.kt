package app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import app.libs.closestNumber
import app.libs.maxPercentageOf
import org.jetbrains.skija.Font
import org.jetbrains.skija.FontMgr
import org.jetbrains.skija.FontStyle
import org.jetbrains.skija.Paint


class CitasPorSemana(val semana: String, val cantidad: Int)
class Point(val x: Float, val y: Float)

@Composable
fun lineChart(data : List<CitasPorSemana>) {
    Canvas(modifier = Modifier
        .size(150.dp)
        .offset(x = 20.dp)
        .border(BorderStroke(1.dp, color = Color.Black))
    ) {
        drawLine(
            start = Offset(x = 20f, y = 20f),
            end = Offset(x = 20f, y = 130f),
            color = Color.DarkGray,
            strokeWidth = 1F
        )
        drawLine(
            start = Offset(x = 20f, y = 130f),
            end = Offset(x = 130f, y = 130f),
            color = Color.DarkGray,
            strokeWidth = 1F
        )
        drawIntoCanvas {
            val face = FontMgr.getDefault().matchFamilyStyle("Menlo", FontStyle.NORMAL)

//          We get the max value
            val topWeek = data.maxByOrNull { p-> p.cantidad }
            val maxValue = closestNumber(topWeek?.cantidad, 10) +10
            println(maxValue)

//          We draw the intervals for X
            for (i in 1..4) {
                println(i)
                val drawing = (maxValue.times(i)).div(4)
                it.nativeCanvas.drawString(
                    "$drawing",
                    5f,
                    135f-(26f*i),
                    Font(face, 12f),
                    Paint().setColor(0xFF000000.toInt())
                )
            }
            val pointList: MutableList<Point> = mutableListOf()
//          We iterate thru the data
            for ((index, semana) in data.withIndex()) {
//                We draw the weeks
                it.nativeCanvas.drawString(
                    semana.semana,
                    20f+(30f*index),
                    145f,
                    Font(face, 10f),
                    Paint().setColor(0xFF000000.toInt())
                )
                it.nativeCanvas.drawLine(
                    36f+(30f*index),
                    128f,
                    36f+(30f*index),
                    132f,
                    Paint().setColor(0xFF000000.toInt())
                )
/*
    So we have that 100% it's on Y31 (since the Y it's inverted)
        and the 25% it's on 109
    therefore, if we want to get the Y value we have to get
        y = -1,04x + 135

        Now we just need to draw dots with this
*/
                val percentage = maxPercentageOf(semana.cantidad, maxValue)
                val elevation =  -1.04*(percentage) + 130
                val radius = 3f
                it.nativeCanvas.drawCircle(
                    36f+(30f*index),
                    elevation.toFloat(),
                    radius,
                    Paint().setColor(0xFF000000.toInt())
                        .setStroke(true)
                )
                pointList.add(
                    Point(
                        36f+(30f*index),
                        elevation.toFloat()
                    )
                )
            }
            for (point in pointList){
                println("${point.x}, ${point.y}")
            }
            println(pointList.lastIndex)
            val difference = 1.5f
            for (i in 1..pointList.lastIndex) {
                it.nativeCanvas.drawLine(
                    pointList[i-1].x + difference,
                    pointList[i-1].y,
                    pointList[i].x + difference,
                    pointList[i].y,
                    Paint().setColor(0xFF000000.toInt())
                )
            }

            for ((index, semana) in data.withIndex()) {
                val percentage = maxPercentageOf(semana.cantidad, maxValue)
                val elevation =  -1.04*(percentage) + 130
                val radius = 3f
                it.nativeCanvas.drawCircle(
                    36f+(30f*index),
                    elevation.toFloat(),
                    radius-0.2f,
                    Paint().setColor(0xFFFFFFFF.toInt())
                )
                it.nativeCanvas.drawString(
                    "${semana.cantidad}",
                    36f+(30f*index)-2f,
                    elevation.toFloat()-5f,
                    Font(face, 12f),
                    Paint().setColor(0xFF000000.toInt())
                )
            }
        }
    }
}