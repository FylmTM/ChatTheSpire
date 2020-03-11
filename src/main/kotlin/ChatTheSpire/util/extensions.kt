package ChatTheSpire.util

fun <T> List<T>?.toSafeArrayList() =
    if (this == null) ArrayList() else ArrayList(this)

fun <T> List<T>.getByPosition(position: Int?): T? =
    if (position == null) null
    else this.getOrNull(position - 1)
