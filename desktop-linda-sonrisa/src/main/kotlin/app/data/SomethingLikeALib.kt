package app.data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.jetbrains.skija.Bitmap
import org.jetbrains.skija.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.imageio.ImageIO

private val blankProfilePicture = {}.javaClass.classLoader.getResource("images/blank-profile-picture.png")!!

fun bufferedImageToBitMap(image: BufferedImage): ImageBitmap {
    val baos = ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)

    return Image.makeFromEncoded(baos.toByteArray()).asImageBitmap()
}

fun byteArrayToBitMap(image: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(image).asImageBitmap()

}

fun bitMapToByteArray(image: Bitmap):ByteArray {
    return image.readPixels()!!
}

fun fileIsEmpty(file: File): String {
    return when (file.absolutePath) {
        File(blankProfilePicture.toURI()).absolutePath -> {
            "..."
        }
        else -> {
            file.absolutePath
        }
    }
}

fun getDailySlot(startTime: Instant, endTime: Instant, minTime: Instant):Int{
    val difference = startTime.toEpochMilli()-minTime.toEpochMilli()
    val duration = endTime.toEpochMilli()-startTime.toEpochMilli()
    println("Difference $difference")
    println("Duration $duration")
    return (difference/duration).toInt()
}

fun main() {
    val minTime = LocalDateTime.of(2021, 5, 18, 9, 0).toInstant(ZoneOffset.UTC)
    val startTime = LocalDateTime.of(2021, 5, 18, 11, 0).toInstant(ZoneOffset.UTC)
    val endTime = LocalDateTime.of(2021, 5, 18, 11, 30).toInstant(ZoneOffset.UTC)
    println(getDailySlot(startTime, endTime, minTime))

}