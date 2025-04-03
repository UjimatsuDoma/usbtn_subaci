package prac.tanken.shigure.ui.subaci.data.util

fun <T> List<T>.randomList(size: Int) = mutableListOf<T>().also { list ->
    require(size < this.size) {
        "Illegal random list size."
    }

    while (list.size < size) {
        val randomItem = this.random()
        if (randomItem !in list) list.add(randomItem)
    }
}

inline infix fun <reified T> List<*>.isListOf(contract: T): Boolean {
    var result = true
    for (element in this) {
        if (element !is T) {
            result = false
            break
        }
    }
    return result
}