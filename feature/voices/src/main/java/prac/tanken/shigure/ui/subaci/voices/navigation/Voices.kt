package prac.tanken.shigure.ui.subaci.voices.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.base.navigation.MainDestinations
import prac.tanken.shigure.ui.subaci.voices.R as TankenR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Voices : MainDestinations(
    displayName = TankenR.string.home_voices,
    desc = TankenR.string.home_voices_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_person_voice_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_person_voice_24_filled,
)