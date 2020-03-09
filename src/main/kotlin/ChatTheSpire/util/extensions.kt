package ChatTheSpire.util

fun <T> List<T>?.toSafeArrayList() = if (this == null) ArrayList() else ArrayList(this)
