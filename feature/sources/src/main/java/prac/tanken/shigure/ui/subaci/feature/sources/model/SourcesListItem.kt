package prac.tanken.shigure.ui.subaci.feature.sources.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.jpeg.JpegDirectory
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.VoicesVO
import java.io.InputStream

@Serializable
data class SourcesListItem(
    val videoId: String,
    val title: String,
    val voices: List<VoicesVO>,
) {
    val url: String
        get() = "file:///android_asset/subaciThumbs/$videoId.jpg"
    val imageIs: InputStream
        @Composable get() = LocalContext.current
            .assets.openFd("subaciThumbs/$videoId.jpg")
            .createInputStream()
    val thumbAspectRatio: Float
        @Composable get() = ImageMetadataReader.readMetadata(imageIs)
            .getFirstDirectoryOfType(JpegDirectory::class.java)
            .run { imageWidth.toFloat() / imageHeight.toFloat() }
}