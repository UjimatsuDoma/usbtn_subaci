package prac.tanken.shigure.ui.subaci.feature.voices.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.feature.base.navigation.MainDestination
import prac.tanken.shigure.ui.subaci.feature.voices.R as TankenR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Voices : MainDestination(
    displayName = TankenR.string.home_voices,
    desc = TankenR.string.home_voices_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_person_voice_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_person_voice_24_filled,
)
