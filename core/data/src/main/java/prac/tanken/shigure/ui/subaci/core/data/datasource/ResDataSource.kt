package prac.tanken.shigure.ui.subaci.core.data.datasource

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import prac.tanken.shigure.ui.subaci.core.common.io.readText
import javax.inject.Inject

class ResDataSource @Inject constructor(
    @ApplicationContext val applicationContext: Context
) {
    private val resources
        get() = applicationContext.resources

    infix fun readAsString(@RawRes resId: Int) = resources.openRawResource(resId).readText()

    infix fun getString(@StringRes resId: Int) = resources.getString(resId)
}