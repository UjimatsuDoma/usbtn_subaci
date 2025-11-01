package prac.tanken.shigure.ui.subaci.core.data.settings.ui

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.R

@Serializable
sealed class NavigationLabelBehaviour(@param:StringRes val displayName: Int) {
    abstract fun showLabel(selected: Boolean): Boolean

    @Serializable
    data object ShowAlways : NavigationLabelBehaviour(R.string.app_bottom_bar_label_always_show) {
        override fun showLabel(selected: Boolean): Boolean = true
    }

    @Serializable
    data object ShowWhenSelected : NavigationLabelBehaviour(R.string.app_bottom_bar_label_show_when_selected) {
        override fun showLabel(selected: Boolean): Boolean = selected
    }

    @Serializable
    data object Hide : NavigationLabelBehaviour(R.string.app_bottom_bar_label_always_hide) {
        override fun showLabel(selected: Boolean): Boolean = false
    }

    companion object {
        val default = ShowAlways
    }
}