package app.data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.jetbrains.skija.Bitmap
import org.jetbrains.skija.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
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