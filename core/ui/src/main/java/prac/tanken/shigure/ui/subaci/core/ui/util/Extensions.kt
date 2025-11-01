package prac.tanken.shigure.ui.subaci.core.ui.util

import prac.tanken.shigure.ui.subaci.core.common.serialization.encodeJsonString

inline infix fun <reified T> Int.combineKey(item: T) = LazyListItemKey(this, encodeJsonString(item))