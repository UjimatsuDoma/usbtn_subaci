package prac.tanken.shigure.ui.subaci.core.data.datasource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import prac.tanken.shigure.ui.subaci.core.common.io.readText
import javax.inject.Inject

class AssetsDataSource @Inject constructor(
    @ApplicationContext val applicationContext: Context
) {
    private val assetsManager
        get() = applicationContext.assets

    infix fun openFileDescriptorOf(path: String) = assetsManager.open(path)

    infix fun openFileAsString(path: String) = assetsManager.open(path).readText()
}