package prac.tanken.shigure.ui.subaci.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
fun CommonExtension<*, *, *, *, *, *>.appConfig(
    block: ApplicationExtension.() -> Unit
): CommonExtension<*, *, *, *, *, *> {
    if(this@appConfig is BaseAppModuleExtension) {
        block()
    }
    return this
}