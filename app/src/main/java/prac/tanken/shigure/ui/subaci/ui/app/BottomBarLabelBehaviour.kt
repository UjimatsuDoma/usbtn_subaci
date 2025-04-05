package prac.tanken.shigure.ui.subaci.ui.app

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.R as TankenR

@Serializable
sealed class BottomBarLabelBehaviour(@StringRes val displayName: Int) {
    abstract fun showLabel(selected: Boolean): Boolean

    @Serializable
    data object ShowAlways : BottomBarLabelBehaviour(TankenR.string.app_bottom_bar_label_always_show) {
        override fun showLabel(selected: Boolean): Boolean = true
    }

    @Serializable
    data object ShowWhenSelected : BottomBarLabelBehaviour(TankenR.string.app_bottom_bar_label_show_when_selected) {
        override fun showLabel(selected: Boolean): Boolean = selected
    }

    @Serializable
    data object Hide : BottomBarLabelBehaviour(TankenR.string.app_bottom_bar_label_always_hide) {
        override fun showLabel(selected: Boolean): Boolean = false
    }

    companion object {
        val default = ShowAlways
    }
}