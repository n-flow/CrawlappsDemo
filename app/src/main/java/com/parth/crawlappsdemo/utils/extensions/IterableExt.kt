package com.parth.crawlappsdemo.utils.extensions

fun <T> ArrayList<T>.deleteIf(predicate: (T) -> Boolean) {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
        }
    }
}

fun <T> Collection<T>.toArrayList(): Collection<T> {
    return ArrayList(this)
}