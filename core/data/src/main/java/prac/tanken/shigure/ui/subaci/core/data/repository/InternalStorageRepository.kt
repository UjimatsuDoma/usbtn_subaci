package prac.tanken.shigure.ui.subaci.core.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class InternalStorageRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    val cacheDir: File = context.cacheDir
    val filesDir: File = context.filesDir
}