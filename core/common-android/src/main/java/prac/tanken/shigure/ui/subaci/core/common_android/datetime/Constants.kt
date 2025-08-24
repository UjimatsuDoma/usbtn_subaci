package prac.tanken.shigure.ui.subaci.core.common_android.datetime

import android.os.Build
import prac.tanken.shigure.ui.subaci.core.common.datetime.todayStrKotlinX
import prac.tanken.shigure.ui.subaci.core.common.datetime.todayStrLegacy

val todayStr: String
    get() = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) todayStrKotlinX else todayStrLegacy