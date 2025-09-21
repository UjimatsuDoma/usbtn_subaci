package prac.tanken.shigure.ui.subaci.core.common.android

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ToastUtil @Inject constructor(
    @param:ApplicationContext val appContext: Context
) {
    fun toast(message: String) = {
        Toast.makeText(
            appContext, message, Toast.LENGTH_SHORT
        ).show()
    }.invoke()
}