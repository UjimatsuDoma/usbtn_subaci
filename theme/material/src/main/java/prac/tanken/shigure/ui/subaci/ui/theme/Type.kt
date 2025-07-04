package prac.tanken.shigure.ui.subaci.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

private val DefaultTypography = Typography()

val AppTypography
    @Composable get() = Typography(
        displayLarge = DefaultTypography.displayLarge.copy(fontFamily = NotoSansMultiLang),
        displayMedium = DefaultTypography.displayMedium.copy(fontFamily = NotoSansMultiLang),
        displaySmall = DefaultTypography.displaySmall.copy(fontFamily = NotoSansMultiLang),
        headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = NotoSansMultiLang),
        headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = NotoSansMultiLang),
        headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = NotoSansMultiLang),
        titleLarge = DefaultTypography.titleLarge.copy(fontFamily = NotoSansMultiLang),
        titleMedium = DefaultTypography.titleMedium.copy(fontFamily = NotoSansMultiLang),
        titleSmall = DefaultTypography.titleSmall.copy(fontFamily = NotoSansMultiLang),
        bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = NotoSansMultiLang),
        bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = NotoSansMultiLang),
        bodySmall = DefaultTypography.bodySmall.copy(fontFamily = NotoSansMultiLang),
        labelLarge = DefaultTypography.labelLarge.copy(fontFamily = NotoSansMultiLang),
        labelMedium = DefaultTypography.labelMedium.copy(fontFamily = NotoSansMultiLang),
        labelSmall = DefaultTypography.labelSmall.copy(fontFamily = NotoSansMultiLang),
    )

