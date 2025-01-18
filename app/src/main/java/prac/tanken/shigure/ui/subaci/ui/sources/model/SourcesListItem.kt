package prac.tanken.shigure.ui.subaci.ui.sources.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.jpeg.JpegDirectory
import prac.tanken.shigure.ui.subaci.data.model.Voice
import java.io.InputStream

data class SourcesListItem(
    val videoId: String,
    val title: String,
    val voices: List<Voice>,
) {
    val url: String
        get() = "file:///android_asset/subaciThumbs/$videoId.jpg"
    val imageIs: InputStream
        @Composable get() = LocalContext.current
            .assets.openFd("subaciThumbs/$videoId.jpg")
            .createInputStream()

    @Composable
    fun thumbAspectRatio(): Float =
        ImageMetadataReader.readMetadata(imageIs)
            .getFirstDirectoryOfType(JpegDirectory::class.java)
            .run { imageWidth.toFloat() / imageHeight.toFloat() }

}
