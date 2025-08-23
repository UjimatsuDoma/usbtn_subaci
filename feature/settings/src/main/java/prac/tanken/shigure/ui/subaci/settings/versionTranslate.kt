package prac.tanken.shigure.ui.subaci.settings

import android.os.Build

fun versionTranslate(version: Int) = when (version) {
    Build.VERSION_CODES.O,
    Build.VERSION_CODES.O_MR1 -> 8

    Build.VERSION_CODES.P -> 9
    Build.VERSION_CODES.Q -> 10

    Build.VERSION_CODES.S,
    Build.VERSION_CODES.S_V2 -> 12

    Build.VERSION_CODES.TIRAMISU -> 13
    Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> 14
    Build.VERSION_CODES.VANILLA_ICE_CREAM -> 15

    else -> -1
}